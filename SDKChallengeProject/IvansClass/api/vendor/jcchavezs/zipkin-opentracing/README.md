# Zipkin PHP OpenTracing

[![Travis CI](https://travis-ci.org/jcchavezs/zipkin-php-opentracing.svg?branch=master)](https://travis-ci.org/jcchavezs/zipkin-php-opentracing)
[![OpenTracing Badge](https://img.shields.io/badge/OpenTracing-enabled-blue.svg)](http://opentracing.io)
[![Total Downloads](https://poser.pugx.org/jcchavezs/zipkin-opentracing/downloads)](https://packagist.org/packages/jcchavezs/zipkin-opentracing)
[![Minimum PHP Version](https://img.shields.io/badge/php-%3E%3D%205.6-8892BF.svg)](https://php.net/)
[![License](https://img.shields.io/packagist/l/jcchavezs/zipkin-opentracing.svg)](https://github.com/jcchavezs/zipkin-php-opentracing/blob/master/LICENSE)

[Zipkin](http://zipkin.io) implementation for OpenTracingTracer in PHP.

This library allows OpenTracing API consumers to use Zipkin as their tracing backend.
For details on how to work with spans and traces we suggest looking at the documentation
and README from the [OpenTracing API](https://github.com/opentracing/opentracing-php).

## Getting started

### Required Reading

In order to understand OpenTracing API, one must first be familiar with the [OpenTracing project](http://opentracing.io) and [terminology](http://opentracing.io/spec/) more generally.

To understand how Zipkin works, you can look at [Zipkin Architecture](http://zipkin.io/pages/architecture.html) and [Zipkin PHP](https://github.com/zipkin/zipkin-php) documentation.

### Installation

```bash
composer require jcchavezs/zipkin-opentracing
```

### Usage

Firstly, we need to setup a tracer:

```php
use OpenTracing\GlobalTracer;
use Psr\Log\NullLogger;
use Zipkin\Endpoint;
use Zipkin\Samplers\BinarySampler;
use Zipkin\TracingBuilder;
use Zipkin\Reporters\Http;

$endpoint = Endpoint::create('my_service', '127.0.0.1', null, 8081);
$reporter = new Zipkin\Reporters\Http();
$sampler = BinarySampler::createAsAlwaysSample();
$tracing = TracingBuilder::create()
	->havingLocalEndpoint($endpoint)
   ->havingSampler($sampler)
   ->havingReporter($reporter)
   ->build();

$zipkinTracer = new ZipkinOpenTracing\Tracer($tracing);

GlobalTracer::set($zipkinTracer); // optional
```

### Creating Spans

- [Starting a root span](https://github.com/opentracing/opentracing-php#starting-an-empty-trace-by-creating-a-root-span)
- [Starting a span for a given request](https://github.com/opentracing/opentracing-php#creating-a-span-given-an-existing-request)
- [Active span and scope manager](https://github.com/opentracing/opentracing-php#active-spans-and-scope-manager)
	- [Creating a child span assigning parent manually](https://github.com/opentracing/opentracing-php#creating-a-child-span-assigning-parent-manually)
	- [Creating a child span using automatic active span management](https://github.com/opentracing/opentracing-php#creating-a-child-span-using-automatic-active-span-management)
- [Using span options](https://github.com/opentracing/opentracing-php#using-span-options)

### Propagation of context

- [Serializing context to the wire](https://github.com/opentracing/opentracing-php#serializing-to-the-wire)
- [Deserializing context from the wire](https://github.com/opentracing/opentracing-php#deserializing-from-the-wire)
- [Propagation formats](https://github.com/opentracing/opentracing-php#propagation-formats)

### Flushing Spans to the agent

PHP as a request scoped language has no simple means to pass the collected spans data to a background process without 
blocking the main request thread/process. It is mandatory to execute the `Tracer::flush()` after the response is served 
to the client by using [`register_shutdown_function`](http://php.net/manual/en/function.register-shutdown-function.php).

```php
use OpenTracing\GlobalTracer;

$application->run();

register_shutdown_function(function() {
    GlobalTracer::get()->flush();
});
```

## Contribution

### Run tests

```
composer test
```

### Fix lint

```
composer fix-lint
```
