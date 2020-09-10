<?php

namespace ZipkinOpenTracing\Tests\Unit;

use DateTime;
use OpenTracing\Tags;
use PHPUnit_Framework_TestCase;
use Prophecy\Argument;
use Zipkin\Propagation\DefaultSamplingFlags;
use Zipkin\Span as ZipkinSpan;
use Zipkin\Propagation\TraceContext;
use ZipkinOpenTracing\Span;

final class SpanTest extends PHPUnit_Framework_TestCase
{
    const OPERATION_NAME = 'test_name';
    const SPAN_KIND = 'kind';
    const TAG_KEY = 'test_key';
    const TAG_VALUE = 'test_value';
    const PEER_SERVICE_VALUE = 'test_service';
    const PEER_HOST_IPV4_VALUE = '127.0.0.2';
    const PEER_HOST_IPV6_VALUE = null;
    const PEER_PORT_VALUE = 12345;

    public function testASpanIsCreatedAndHasTheExpectedValues()
    {
        $context = TraceContext::createAsRoot(DefaultSamplingFlags::createAsEmpty());
        $zipkinSpan = $this->prophesize(ZipkinSpan::class);
        $zipkinSpan->getContext()->willReturn($context);
        $span = Span::create(self::OPERATION_NAME, $zipkinSpan->reveal());
        $this->assertEquals($span->getOperationName(), self::OPERATION_NAME);
    }

    public function testTagASpanSuccess()
    {
        $context = TraceContext::createAsRoot(DefaultSamplingFlags::createAsEmpty());

        $zipkinSpan = $this->prophesize(ZipkinSpan::class);
        $zipkinSpan->getContext()->willReturn($context);
        $zipkinSpan->setKind(strtoupper(self::SPAN_KIND))->shouldBeCalled();
        $zipkinSpan->tag(self::TAG_KEY, self::TAG_VALUE)->shouldBeCalled();
        $zipkinSpan->tag(Tags\PEER_SERVICE, self::PEER_SERVICE_VALUE)->shouldNotBeCalled();
        $zipkinSpan->tag(Tags\PEER_HOST_IPV4, self::PEER_HOST_IPV4_VALUE)->shouldNotBeCalled();
        $zipkinSpan->tag(Tags\PEER_HOST_IPV6, self::PEER_HOST_IPV6_VALUE)->shouldNotBeCalled();
        $zipkinSpan->tag(Tags\PEER_PORT, self::PEER_PORT_VALUE)->shouldNotBeCalled();

        $span = Span::create(self::OPERATION_NAME, $zipkinSpan->reveal());
        $span->setTag('span.kind', self::SPAN_KIND);
        $span->setTag(self::TAG_KEY, self::TAG_VALUE);
        $span->setTag(Tags\PEER_SERVICE, self::PEER_SERVICE_VALUE);
        $span->setTag(Tags\PEER_HOST_IPV4, self::PEER_HOST_IPV4_VALUE);
        $span->setTag(Tags\PEER_HOST_IPV6, self::PEER_HOST_IPV6_VALUE);
        $span->setTag(Tags\PEER_PORT, self::PEER_PORT_VALUE);
    }

    public function testLogASpanWithNullTimestampSuccess()
    {
        $context = TraceContext::createAsRoot(DefaultSamplingFlags::createAsEmpty());

        /**
         * @var ZipkinSpan
         */
        $zipkinSpan = $this->prophesize(ZipkinSpan::class);
        $zipkinSpan->getContext()->willReturn($context);
        $zipkinSpan->annotate('field', Argument::any())->shouldBeCalled();

        $span = Span::create(self::OPERATION_NAME, $zipkinSpan->reveal());
        $span->log(['field']);
    }

    /**
     * @dataProvider logTimestamps
     */
    public function testLogASpanWithTimestampSuccess($timestamp, $expectedTimestamp)
    {
        $context = TraceContext::createAsRoot(DefaultSamplingFlags::createAsEmpty());

        /**
         * @var ZipkinSpan
         */
        $zipkinSpan = $this->prophesize(ZipkinSpan::class);
        $zipkinSpan->getContext()->willReturn($context);
        $zipkinSpan->annotate('field', $expectedTimestamp)->shouldBeCalled();

        $span = Span::create(self::OPERATION_NAME, $zipkinSpan->reveal());
        $span->log(['field'], $timestamp);
    }

    /**
     * @return array
     */
    public function logTimestamps()
    {
        $now = time();
        $expectedTimestamp = $now * 1000000;
        return [
            [$now, $expectedTimestamp],
            [(float)$now, $expectedTimestamp],
            [(new DateTime)->setTimestamp($now), $expectedTimestamp],
        ];
    }
}
