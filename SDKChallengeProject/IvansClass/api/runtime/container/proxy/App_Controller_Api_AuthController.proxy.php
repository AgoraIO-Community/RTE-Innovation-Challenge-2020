<?php

/**
 * Created by PhpStorm.
 * User: Zhuyunfeng
 * Date: 2020/9/6
 * Time: 6:21 下午
 */
namespace App\Controller\Api;

use App\Controller\AbstractController;
use App\Model\User;
use Hyperf\Di\Annotation\Inject;
use Phper666\JWTAuth\JWT;
class AuthController extends AbstractController
{
    use \Hyperf\Di\Aop\ProxyTrait;
    use \Hyperf\Di\Aop\PropertyHandlerTrait;
    function __construct()
    {
        if (method_exists(parent::class, '__construct')) {
            parent::__construct(...func_get_args());
        }
        self::__handlePropertyHandler(__CLASS__);
    }
    public function register()
    {
        $account = $this->request->post('account', '');
        $mobile = $this->request->post('mobile', '');
        $password = $this->request->post('password', '');
        if (empty($account)) {
        }
        if (empty($mobile)) {
        }
        if (empty($password)) {
        }
        $user = new User();
        $user->account = $account;
        $user->mobile = $mobile;
        $user->password = password_hash($password, PASSWORD_DEFAULT);
        $result = $user->save();
        var_dump($result);
        return $this->success($result);
    }
    /**
     * @Inject()
     *
     * @var JWT
     */
    protected $jwt;
    public function login()
    {
        $user = User::query()->where('account', $this->request->input('account'))->first();
        //验证用户账户密码
        if (!empty($user->password) && password_verify($this->request->input('password'), $user->password)) {
            $userData = ['uid' => $user->id, 'account' => $user->account];
            $token = $this->jwt->getToken($userData);
            $data = ['token' => (string) $token, 'exp' => $this->jwt->getTTL()];
            return $this->success($data);
        }
        return $this->failed('登录失败');
    }
    public function logout()
    {
    }
}