<?php

namespace ZipkinOpenTracing;

use ArrayIterator;
use OpenTracing\SpanContext as OTSpanContext;
use Zipkin\Propagation\TraceContext;

final class SpanContext implements OTSpanContext, WrappedTraceContext
{
    private $traceContext;
    private $baggageItems;

    private function __construct(TraceContext $traceContext, array $baggageItems = [])
    {
        $this->traceContext = $traceContext;
        $this->baggageItems = $baggageItems;
    }

    /**
     * @param TraceContext $traceContext
     * @return SpanContext
     */
    public static function fromTraceContext(TraceContext $traceContext)
    {
        return new self($traceContext);
    }

    /**
     * @inheritdoc
     */
    public function getIterator()
    {
        return new ArrayIterator($this->baggageItems);
    }

    /**
     * @inheritdoc
     */
    public function getBaggageItem($key)
    {
        return \array_key_exists($key, $this->baggageItems) ? $this->baggageItems[$key] : null;
    }

    /**
     * @inheritdoc
     */
    public function withBaggageItem($key, $value)
    {
        return new self($this->traceContext, [$key => $value] + $this->baggageItems);
    }

    /**
     * @inheritdoc
     */
    public function getContext()
    {
        return $this->traceContext;
    }
}
