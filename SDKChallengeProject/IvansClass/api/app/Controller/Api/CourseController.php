<?php
/**
 * Created by PhpStorm.
 * User: Zhuyunfeng
 * Date: 2020/9/6
 * Time: 9:04 下午
 */

namespace App\Controller\Api;


use App\Controller\AbstractController;
use App\Model\Course;
use App\Model\UserClass;

class CourseController extends AbstractController
{
    public function index(){
        $result = Course::orderByDesc('created_at')->get();
        return $this->success($result);

    }

    public function show(){
         $courseId = $this->request->input('course_id');
        $result = Course::find($courseId);
        return $this->success($result);
    }

    public function joinClass(){

    }

}
