<?php
/**
 * Created by PhpStorm.
 * User: Zhuyunfeng
 * Date: 2020/9/4
 * Time: 12:10 上午
 */

namespace App\Controller;


class LoginController extends AbstractController
{
    public function login(){
        $username = $this->request->input('username','');
        $password = $this->request->input('password','');

    }

    public function register(){

    }

    public function logout(){

    }
}
