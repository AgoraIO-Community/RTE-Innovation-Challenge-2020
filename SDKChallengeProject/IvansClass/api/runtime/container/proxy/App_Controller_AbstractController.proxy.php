<?php

declare (strict_types=1);
/**
 * This file is part of Hyperf.
 *
 * @link     https://www.hyperf.io
 * @document https://hyperf.wiki
 * @contact  group@hyperf.io
 * @license  https://github.com/hyperf/hyperf/blob/master/LICENSE
 */
namespace App\Controller;

use Hyperf\Di\Annotation\Inject;
use Hyperf\HttpServer\Contract\RequestInterface;
use Hyperf\HttpServer\Contract\ResponseInterface;
use Psr\Container\ContainerInterface;
abstract class AbstractController
{
    use \Hyperf\Di\Aop\ProxyTrait;
    use \Hyperf\Di\Aop\PropertyHandlerTrait;
    function __construct()
    {
        self::__handlePropertyHandler(__CLASS__);
    }
    /**
     * @Inject
     * @var ContainerInterface
     */
    protected $container;
    /**
     * @Inject
     * @var RequestInterface
     */
    protected $request;
    /**
     * @Inject
     * @var ResponseInterface
     */
    protected $response;
    /**
     * 请求成功
     *
     * @param        $data
     * @param string $message
     *
     * @return array
     */
    public function success($data, $message = 'success')
    {
        $code = $this->response->getStatusCode();
        return ['msg' => $message, 'code' => $code, 'data' => $data];
    }
    /**
     * 请求失败.
     *
     * @param string $message
     *
     * @return array
     */
    public function failed($message = 'Request format error!')
    {
        return ['msg' => $message, 'code' => 500, 'data' => ''];
    }
}