<?php
/**
 * Elasticsearch PHP client
 *
 * @link      https://github.com/elastic/elasticsearch-php/
 * @copyright Copyright (c) Elasticsearch B.V (https://www.elastic.co)
 * @license   http://www.apache.org/licenses/LICENSE-2.0 Apache License, Version 2.0
 * @license   https://www.gnu.org/licenses/lgpl-2.1.html GNU Lesser General Public License, Version 2.1 
 * 
 * Licensed to Elasticsearch B.V under one or more agreements.
 * Elasticsearch B.V licenses this file to you under the Apache 2.0 License or
 * the GNU Lesser General Public License, Version 2.1, at your option.
 * See the LICENSE file in the project root for more information.
 */
declare(strict_types = 1);

namespace Elasticsearch\Endpoints;

use Elasticsearch\Endpoints\AbstractEndpoint;

/**
 * Class Reindex
 * Elasticsearch API name reindex
 * Generated running $ php util/GenerateEndpoints.php 7.9
 */
class Reindex extends AbstractEndpoint
{

    public function getURI(): string
    {

        return "/_reindex";
    }

    public function getParamWhitelist(): array
    {
        return [
            'refresh',
            'timeout',
            'wait_for_active_shards',
            'wait_for_completion',
            'requests_per_second',
            'scroll',
            'slices',
            'max_docs'
        ];
    }

    public function getMethod(): string
    {
        return 'POST';
    }

    public function setBody($body): Reindex
    {
        if (isset($body) !== true) {
            return $this;
        }
        $this->body = $body;

        return $this;
    }
}