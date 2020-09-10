<?php

use OpenTracing\Formats;

require_once __DIR__ . '/functions.php';
require_once __DIR__ . '/vendor/autoload.php';

$tracer = build_tracer('backend', '127.0.0.2');

\OpenTracing\GlobalTracer::set($tracer);

$carrier = getallheaders();

$traceContext = $tracer->extract(Formats\TEXT_MAP, $carrier);

$span = $tracer->startSpan('http_request', [
    'child_of' => $traceContext
]);

usleep(100);

$childSpan = $tracer->startSpan('user:get_list:mysql_query', [
    'child_of' => $span,
]);

$childSpan->finish();

$span->finish();

register_shutdown_function(function () use ($tracer) {
    $tracer->flush();
});
