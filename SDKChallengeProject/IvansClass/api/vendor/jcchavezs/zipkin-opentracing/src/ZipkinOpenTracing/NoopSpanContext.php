<?php

namespace ZipkinOpenTracing;

use ArrayIterator;
use OpenTracing\SpanContext as OTSpanContext;
use Zipkin\Propagation\TraceContext;

/**
 * @deprecated
 *
 * This shouldn't have been introduced as propagation expects an actual
 * context not a noop one.
 */
class NoopSpanContext implements OTSpanContext, WrappedTraceContext
{
    /**
     * @var TraceContext
     */
    private $context;

    private function __construct(TraceContext $context)
    {
        $this->context = $context;
    }

    public static function create(TraceContext $context)
    {
        return new self($context);
    }

    /**
     * @inheritdoc
     */
    public function getContext()
    {
        return $this->context;
    }

    /**
     * @inheritdoc
     */
    public function getIterator()
    {
        return new ArrayIterator([]);
    }

    /**
     * @inheritdoc
     */
    public function getBaggageItem($key)
    {
        return null;
    }

    /**
     * @inheritdoc
     */
    public function withBaggageItem($key, $value)
    {
        return $this;
    }
}
