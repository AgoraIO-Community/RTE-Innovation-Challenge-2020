<?php

declare(strict_types=1);

/**
 * This file is part of Hyperf.
 *
 * @link     https://www.hyperf.io
 * @document https://hyperf.wiki
 * @contact  group@hyperf.io
 * @license  https://github.com/hyperf/hyperf/blob/master/LICENSE
 */

use Hyperf\HttpServer\Router\Router;

Router::addGroup('/api/', function () {
    Router::post('register', 'App\Controller\Api\AuthController@register');
    Router::post('login', 'App\Controller\Api\AuthController@login');


    Router::addGroup('courses', function () {
        Router::get('', 'App\Controller\Api\CourseController@index');
        Router::get('/show', 'App\Controller\Api\CourseController@show');
        Router::get('/classes', 'App\Controller\Api\ClassController@index');
        Router::get('/classes/show', 'App\Controller\Api\ClassController@show');
        Router::get('/classes/lives', 'App\Controller\Api\ClassController@fetchLives');
    }, [
        'middleware' => [App\Middleware\JwtAuthMiddleware::class]
    ]);

    Router::addGroup('user/', function () {
        Router::get('courses', 'App\Controller\Api\UserController@fetchUserCourse');
        Router::post('course/subscribe', 'App\Controller\Api\UserController@subscribeCourse');
        Router::get('lives', 'App\Controller\Api\UserController@fetchUserLives');
        Router::post('live/enter', 'App\Controller\Api\UserController@enterLive');
        Router::get('profile', 'App\Controller\Api\UserController@fetchUserProfile');
        Router::post('profile/save', 'App\Controller\Api\UserController@saveUserProfile');
        Router::post('class/join', 'App\Controller\Api\UserController@joinClass');
        Router::get('classes', 'App\Controller\Api\UserController@fetchUserClasses');
    },
        [
            'middleware' => [App\Middleware\JwtAuthMiddleware::class]
        ]);
    Router::get('generateLives','App\Controller\Api\SystemController@generateLives');
});


Router::addRoute(['GET', 'POST', 'HEAD'], '/', 'App\Controller\IndexController@index');
//
//Router::get('/favicon.ico', function () {
//    return '';
//});
