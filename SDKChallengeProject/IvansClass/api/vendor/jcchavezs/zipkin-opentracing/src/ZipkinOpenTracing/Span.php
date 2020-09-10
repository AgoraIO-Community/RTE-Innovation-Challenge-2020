<?php

namespace ZipkinOpenTracing;

use InvalidArgumentException;
use OpenTracing\Tags;
use OpenTracing\Span as OTSpan;
use OpenTracing\SpanContext;
use Zipkin\Endpoint;
use Zipkin\Span as ZipkinSpan;
use Zipkin\Timestamp;
use ZipkinOpenTracing\SpanContext as ZipkinOpenTracingContext;

final class Span implements OTSpan
{
    /**
     * @var ZipkinSpan
     */
    private $span;

    /**
     * @var string
     */
    private $operationName;

    /**
     * @var SpanContext
     */
    private $context;

    /**
     * @var bool
     */
    private $hasRemoteEndpoint;

    /**
     * @var array;
     */
    private $remoteEndpointArgs;

    /**
     * @var Scope
     */
    private $scope;

    /**
     * @var bool
     */
    private $shouldCloseScopeOnFinish = true;

    private function __construct($operationName, ZipkinSpan $span, array $remoteEndpointArgs = null)
    {
        $this->operationName = $operationName;
        $this->span = $span;
        $this->context = ZipkinOpenTracingContext::fromTraceContext($span->getContext());
        $this->hasRemoteEndpoint = $remoteEndpointArgs !== null;
        $this->remoteEndpointArgs = $this->hasRemoteEndpoint ?
            $remoteEndpointArgs : [Endpoint::DEFAULT_SERVICE_NAME, null, null, null];
    }

    /**
     * @param string $operationName
     * @param ZipkinSpan $span
     * @param array|null $remoteEndpointArgs
     * @return Span
     */
    public static function create($operationName, ZipkinSpan $span, array $remoteEndpointArgs = null)
    {
        return new self($operationName, $span, $remoteEndpointArgs);
    }

    /**
     * @inheritdoc
     */
    public function getOperationName()
    {
        return $this->operationName;
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
    public function finish($finishTime = null, array $logRecords = [])
    {
        if ($this->hasRemoteEndpoint) {
            $this->span->setRemoteEndpoint(Endpoint::create(...$this->remoteEndpointArgs));
        }

        $this->span->finish($finishTime ?: Timestamp\now());

        if ($this->scope) {
            $this->scope->close();
        }
    }

    /**
     * @inheritdoc
     */
    public function overwriteOperationName($newOperationName)
    {
        $this->operationName = $newOperationName;
        $this->span->setName($newOperationName);
    }

    /**
     * @inheritdoc
     */
    public function setTag($key, $value)
    {
        if ($value === (bool) $value) {
            $value = $value ? 'true' : 'false';
        }

        if ($key === Tags\SPAN_KIND) {
            $this->span->setKind(\strtoupper($value));
            return;
        }

        if ($key === Tags\PEER_SERVICE) {
            $this->hasRemoteEndpoint = true;
            $this->remoteEndpointArgs[0] = $value;
            return;
        }

        if ($key === Tags\PEER_HOST_IPV4) {
            $this->hasRemoteEndpoint = true;
            $this->remoteEndpointArgs[1] = $value;
            return;
        }

        if ($key === Tags\PEER_HOST_IPV6) {
            $this->hasRemoteEndpoint = true;
            $this->remoteEndpointArgs[2] = $value;
            return;
        }

        if ($key === Tags\PEER_PORT) {
            $this->hasRemoteEndpoint = true;
            $this->remoteEndpointArgs[3] = $value;
            return;
        }

        $this->span->tag($key, (string) $value);
    }

    /**
     * @inheritdoc
     */
    public function log(array $fields = [], $timestamp = null)
    {
        if ($timestamp === null) {
            $timestamp = Timestamp\now();
        } else {
            if ($timestamp instanceof \DateTimeInterface) {
                $timestamp = $timestamp->getTimestamp();
            } elseif (!is_float($timestamp) && !is_int($timestamp)) {
                throw new InvalidArgumentException(
                    sprintf('Invalid timestamp. Expected float, int or DateTime, got %s', $timestamp)
                );
            }
            $timestamp = $timestamp * 1000 * 1000;
        }

        foreach ($fields as $field) {
            $this->span->annotate($field, $timestamp);
        }
    }

    /**
     * @inheritdoc
     */
    public function addBaggageItem($key, $value)
    {
        $this->context = $this->context->withBaggageItem($key, $value);
    }

    /**
     * @inheritdoc
     */
    public function getBaggageItem($key)
    {
        return $this->context->getBaggageItem($key);
    }

    public function setScope(Scope $scope)
    {
        $this->scope = $scope;
    }

    public function shouldCloseScopeOnFinish($flag = true)
    {
        $this->shouldCloseScopeOnFinish = $flag;
    }
}
