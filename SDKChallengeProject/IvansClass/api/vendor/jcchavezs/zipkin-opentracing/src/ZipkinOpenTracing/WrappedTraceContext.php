<?php

namespace ZipkinOpenTracing;

use Zipkin\Propagation\SamplingFlags;

interface WrappedTraceContext
{
    /**
     * @return SamplingFlags
     */
    public function getContext();
}
