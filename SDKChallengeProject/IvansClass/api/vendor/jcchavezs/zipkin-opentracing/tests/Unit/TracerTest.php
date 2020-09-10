<?php

namespace ZipkinOpenTracing\Tests\Unit;

use Zipkin\Sampler;
use Prophecy\Argument;
use OpenTracing\Formats;
use Zipkin\TracingBuilder;
use ZipkinOpenTracing\Span;
use ZipkinOpenTracing\Tracer;
use PHPUnit_Framework_TestCase;
use ZipkinOpenTracing\NoopSpan;
use Zipkin\Samplers\BinarySampler;
use ZipkinOpenTracing\SpanContext;
use Zipkin\Propagation\TraceContext;
use ZipkinOpenTracing\PartialSpanContext;
use Prophecy\Argument\Token\AnyValuesToken;
use Zipkin\Propagation\DefaultSamplingFlags;
use OpenTracing\Exceptions\UnsupportedFormat;

final class TracerTest extends PHPUnit_Framework_TestCase
{
    const OPERATION_NAME = 'test';
    const TRACE_ID = '48485a3953bb6124';
    const SPAN_ID = '48485a3953bb6125';
    const SAMPLED = '1';
    const DEBUG = '1';

    public function testExtractOfSamplingFlagsSuccess()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);
        $extractedContext = $tracer->extract(Formats\TEXT_MAP, [
            'x-b3-sampled' => self::SAMPLED,
            'x-b3-flags' => self::DEBUG,
        ]);

        $this->assertTrue($extractedContext instanceof PartialSpanContext);
        $this->assertTrue(
            $extractedContext->getContext()->isEqual(
                DefaultSamplingFlags::create(self::SAMPLED === '1', self::DEBUG === '1')
            )
        );
    }

    public function testExtractOfTraceContextSuccess()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);
        $extractedContext = $tracer->extract(Formats\TEXT_MAP, [
            'x-b3-traceid' => self::TRACE_ID,
            'x-b3-spanid' => self::SPAN_ID,
            'x-b3-sampled' => self::SAMPLED,
            'x-b3-flags' => self::DEBUG,
        ]);

        $this->assertTrue($extractedContext instanceof SpanContext);
        $this->assertTrue(
            $extractedContext->getContext()->isEqual(TraceContext::create(
                self::TRACE_ID,
                self::SPAN_ID,
                null,
                self::SAMPLED === '1',
                self::DEBUG === '1'
            ))
        );
    }

    public function testStartSpanAsNoopWithNoParentSuccess()
    {
        $sampler = $this->prophesize(Sampler::class);
        $sampler->isSampled(new AnyValuesToken())->willReturn(false);
        $tracing = TracingBuilder::create()->havingSampler($sampler->reveal())->build();
        $tracer = new Tracer($tracing);
        $span = $tracer->startSpan(self::OPERATION_NAME);
        $this->assertInstanceOf(NoopSpan::class, $span);
    }

    public function testStartSpanWithNoParentSuccess()
    {
        $sampler = $this->prophesize(Sampler::class);
        $sampler->isSampled(Argument::any())->willReturn(true);
        $tracing = TracingBuilder::create()->havingSampler($sampler->reveal())->build();
        $tracer = new Tracer($tracing);
        $span = $tracer->startSpan(self::OPERATION_NAME);

        $this->assertEquals(self::OPERATION_NAME, $span->getOperationName());
        $this->assertInstanceOf(Span::class, $span);
    }

    public function testStartSpanWithParentSuccess()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);
        $parentSpan = $tracer->startSpan(self::OPERATION_NAME);

        $childSpan = $tracer->startSpan(self::OPERATION_NAME, [
            'child_of' => $parentSpan,
        ]);

        $parentContext = $parentSpan->getContext()->getContext();
        $childContext = $childSpan->getContext()->getContext();

        $this->assertEquals($parentContext->getTraceId(), $childContext->getTraceId());
        $this->assertEquals($parentContext->getSpanId(), $childContext->getParentId());
    }

    public function testStartActiveSpanActivatesScope()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);

        $expectedScope = $tracer->startActiveSpan(self::OPERATION_NAME);

        $scopeManager = $tracer->getScopeManager();
        $activeScope = $scopeManager->getActive();

        $this->assertEquals($expectedScope, $activeScope);
    }

    public function testGetActiveSpan()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);

        $scope = $tracer->startActiveSpan(self::OPERATION_NAME);
        $expectedSpan = $scope->getSpan();
        $actualSpan = $tracer->getActiveSpan();

        $this->assertEquals($expectedSpan, $actualSpan);
    }

    public function testExtractContextFromRequestHeadersSuccess()
    {
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);
        $headers = new Request([
            'x-b3-traceid' => self::TRACE_ID,
            'x-b3-spanid' => self::SPAN_ID,
            'x-b3-sampled' => self::SAMPLED,
            'x-b3-flags' => self::DEBUG,
        ]);
        $extractedContext = $tracer->extract(Formats\HTTP_HEADERS, $headers);

        $this->assertTrue($extractedContext instanceof SpanContext);
        $this->assertTrue(
            $extractedContext->getContext()->isEqual(TraceContext::create(
                self::TRACE_ID,
                self::SPAN_ID,
                null,
                self::SAMPLED === '1',
                self::DEBUG === '1'
            ))
        );
    }

    public function testInjectContextWithUnkownFormatFails()
    {
        $this->expectException(UnsupportedFormat::class);
        $tracing = TracingBuilder::create()->build();
        $tracer = new Tracer($tracing);
        $span = $tracer->startSpan("test");

        $headers = new Request();
        $tracer->inject($span->getContext(), 'unknown_format', $headers);
    }

    /**
     * @dataProvider samplers
     */
    public function testInjectContextToRequestHeadersSuccess(Sampler $sampler)
    {
        $tracing = TracingBuilder::create()->havingSampler($sampler)->build();
        $tracer = new Tracer($tracing);
        $span = $tracer->startSpan("test");

        $headers = new Request();
        $tracer->inject($span->getContext(), Formats\HTTP_HEADERS, $headers);
        $this->assertTrue($headers->hasHeader('x-b3-traceid'));
        $this->assertTrue($headers->hasHeader('x-b3-spanid'));
    }

    public function samplers()
    {
        return [
            [BinarySampler::createAsAlwaysSample()],
            [BinarySampler::createAsNeverSample()],
        ];
    }
}
