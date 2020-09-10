<?php

namespace ZipkinOpenTracing;

use Zipkin\Timestamp;
use OpenTracing\Formats;
use OpenTracing\Reference;
use Zipkin\Propagation\Map;
use OpenTracing\StartSpanOptions;
use Zipkin\Tracer as ZipkinTracer;
use OpenTracing\Tracer as OTTracer;
use Zipkin\Propagation\TraceContext;
use Zipkin\Tracing as ZipkinTracing;
use Zipkin\Propagation\SamplingFlags;
use Zipkin\Propagation\RequestHeaders;
use OpenTracing\Exceptions\UnsupportedFormat;
use OpenTracing\SpanContext as OTSpanContext;
use ZipkinOpenTracing\Span as ZipkinOpenTracingSpan;
use ZipkinOpenTracing\NoopSpan as ZipkinOpenTracingNoopSpan;
use ZipkinOpenTracing\SpanContext as ZipkinOpenTracingContext;
use ZipkinOpenTracing\PartialSpanContext as ZipkinOpenPartialTracingContext;

final class Tracer implements OTTracer
{
    /**
     * @var ZipkinTracer
     */
    private $tracer;

    /**
     * @var callable[]|array
     */
    private $injectors;

    /**
     * @var callable[]|array
     */
    private $extractors;

    public function __construct(ZipkinTracing $tracing)
    {
        $propagation = $tracing->getPropagation();
        $this->injectors = [
            Formats\TEXT_MAP => $propagation->getInjector(new Map()),
            Formats\HTTP_HEADERS => $propagation->getInjector(new RequestHeaders())
        ];
        $this->extractors = [
            Formats\TEXT_MAP => $propagation->getExtractor(new Map()),
            Formats\HTTP_HEADERS => $propagation->getExtractor(new RequestHeaders())
        ];

        $this->tracer = $tracing->getTracer();
        $this->scopeManager = new ScopeManager();
    }

    /**
     * @inheritdoc
     */
    public function getScopeManager()
    {
        return $this->scopeManager;
    }

    /**
     * @inheritdoc
     */
    public function getActiveSpan()
    {
        $activeScope = $this->scopeManager->getActive();
        if ($activeScope === null) {
            return null;
        }

        return $activeScope->getSpan();
    }

    /**
     * @inheritdoc
     */
    public function startActiveSpan($operationName, $options = [])
    {
        if (!$options instanceof StartSpanOptions) {
            $options = StartSpanOptions::create($options);
        }

        if (!$this->hasParentInOptions($options) && $this->getActiveSpan() !== null) {
            $parent = $this->getActiveSpan()->getContext();
            $options = $options->withParent($parent);
        }

        $span = $this->startSpan($operationName, $options);
        $scope = $this->scopeManager->activate($span, $options->shouldFinishSpanOnClose());

        return $scope;
    }

    /**
     * @inheritdoc
     * @return OTSpan|Span
     */
    public function startSpan($operationName, $options = [])
    {
        if (!($options instanceof StartSpanOptions)) {
            $options = StartSpanOptions::create($options);
        }

        if (!$this->hasParentInOptions($options) && $this->getActiveSpan() !== null) {
            $parent = $this->getActiveSpan()->getContext();
            $options = $options->withParent($parent);
        }

        if (empty($options->getReferences())) {
            $span = $this->tracer->newTrace();
        } else {
            /**
             * @var ZipkinOpenTracingContext $refContext
             */
            $refContext = $options->getReferences()[0]->getContext();
            $context = $refContext->getContext();

            if ($context instanceof TraceContext) {
                $span = $this->tracer->newChild($context);
            } else {
                $span = $this->tracer->nextSpan($context);
            }
        }

        if ($span->isNoop()) {
            return ZipkinOpenTracingNoopSpan::create($span);
        }

        $span->start($options->getStartTime() ?: Timestamp\now());
        $span->setName($operationName);

        $otSpan = ZipkinOpenTracingSpan::create($operationName, $span, null);

        foreach ($options->getTags() as $key => $value) {
            $otSpan->setTag($key, $value);
        }

        return $otSpan;
    }

    /**
     * @inheritdoc
     * @throws \InvalidArgumentException
     * @throws \UnexpectedValueException
     */
    public function inject(OTSpanContext $spanContext, $format, &$carrier)
    {
        if ($spanContext instanceof ZipkinOpenTracingContext) {
            $injector = $this->getInjector($format);
            return $injector($spanContext->getContext(), $carrier);
        }

        throw new \InvalidArgumentException(\sprintf(
            'Invalid span context. Expected ZipkinOpenTracing\SpanContext, got %s.',
            \is_object($spanContext) ? \get_class($spanContext) : \gettype($spanContext)
        ));
    }

    /**
     * @inheritdoc
     * @throws \UnexpectedValueException
     */
    public function extract($format, $carrier)
    {
        $extractor = $this->getExtractor($format);
        $extractedContext = $extractor($carrier);

        if ($extractedContext instanceof TraceContext) {
            return ZipkinOpenTracingContext::fromTraceContext($extractedContext);
        }

        if ($extractedContext instanceof SamplingFlags) {
            return ZipkinOpenPartialTracingContext::fromSamplingFlags($extractedContext);
        }

        throw new \UnexpectedValueException(\sprintf(
            'Invalid extracted context. Expected Zipkin\SamplingFlags, got %s',
            \is_object($extractedContext) ? \get_class($extractedContext) : \gettype($extractedContext)
        ));
    }

    /**
     * @inheritdoc
     */
    public function flush()
    {
        $this->tracer->flush();
    }

    /**
     * @param string $format
     * @return callable
     * @throws UnsupportedFormat
     */
    private function getInjector($format)
    {
        if (array_key_exists($format, $this->injectors)) {
            return $this->injectors[$format];
        }

        throw new UnsupportedFormat(\sprintf('Format %s not implemented', $format));
    }

    /**
     * @param string $format
     * @return callable
     * @throws UnsupportedFormat
     */
    private function getExtractor($format)
    {
        if (array_key_exists($format, $this->extractors)) {
            return $this->extractors[$format];
        }

        throw new UnsupportedFormat($format);
    }

    private function hasParentInOptions(StartSpanOptions $options)
    {
        $references = $options->getReferences();
        foreach ($references as $ref) {
            if ($ref->isType(Reference::CHILD_OF)) {
                return $ref->getContext();
            }
        }

        return null;
    }
}
