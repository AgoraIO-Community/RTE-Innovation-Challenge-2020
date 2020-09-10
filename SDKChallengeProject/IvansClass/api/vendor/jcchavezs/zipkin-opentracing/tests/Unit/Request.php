<?php

namespace ZipkinOpenTracing\Tests\Unit;

use LogicException;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\StreamInterface;
use Psr\Http\Message\UriInterface;

final class Request implements RequestInterface
{
    /**
     * @var array
     */
    private $headers;

    public function __construct(array $headers = [])
    {
        foreach ($headers as $key => $value) {
            $this->headers[strtolower($key)] = [$value];
        }
    }

    /**
     *  @return string[][]
     */
    public function getHeaders()
    {
        return $this->headers;
    }

    /**
     * @return bool
     */
    public function hasHeader($name)
    {
        foreach ($this->headers as $key => $value) {
            if ($key === strtolower($name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return string[]
     */
    public function getHeader($name)
    {
        foreach ($this->headers as $key => $value) {
            if ($key === strtolower($name)) {
                return $value;
            }
        }

        return [];
    }

    public function withHeader($name, $value)
    {
        $this->headers[strtolower($name)] = [$value];
        return $this;
    }

    // These functions are required by the interface but no used during
    // parsing of headers
    //
    // phpcs:disable
    public function withAddedHeader($name, $value)
    {
        throw new LogicException('not implemented');
    }

    public function getProtocolVersion()
    {
        throw new LogicException('not implemented');
    }

    public function withProtocolVersion($version)
    {
        throw new LogicException('not implemented');
    }

    public function getHeaderLine($name)
    {
        throw new LogicException('not implemented');
    }

    public function withoutHeader($name)
    {
        throw new LogicException('not implemented');
    }

    public function getBody()
    {
        throw new LogicException('not implemented');
    }

    public function withBody(StreamInterface $body)
    {
        throw new LogicException('not implemented');
    }

    public function getRequestTarget()
    {
        throw new LogicException('not implemented');
    }

    public function withRequestTarget($requestTarget)
    {
        throw new LogicException('not implemented');
    }

    public function getMethod()
    {
        throw new LogicException('not implemented');
    }

    public function withMethod($method)
    {
        throw new LogicException('not implemented');
    }

    public function getUri()
    {
        throw new LogicException('not implemented');
    }

    public function withUri(UriInterface $uri, $preserveHost = false)
    {
        throw new LogicException('not implemented');
    }
    // phpcs:enable
}
