<?php
/**
 * Created by PhpStorm.
 * User: Zhuyunfeng
 * Date: 2020/9/6
 * Time: 9:04 下午
 */

namespace App\Controller\Api;


use App\Controller\AbstractController;
use App\Model\Classes;
use App\Model\Live;
use App\Model\UserClass;

class ClassController extends AbstractController
{
    public function index()
    {
        $user = $this->request->getAttribute('user');
        $userClass = UserClass::where('user_id', $user->id)->select('class_id')->get();
//        if ($userClass->isEmpty()) return $this->failed('暂无相关班级');
        $userClassIds = array_values(array_column($userClass->toArray(), 'class_id'));

        $courseId = $this->request->input('course_id');
        $result = Classes::where('course_id', $courseId)->orderBy('grade')->get();
        if ($result->isNotEmpty()) {
            foreach ($result as &$value) {
                $value->isJoined = false;
                if (!empty($userClassIds) && in_array($value->id, $userClassIds)) {
                    $value->isJoined = true;
                } else {
                    continue;
                }
            }
        }
        return $this->success($result);
    }

    public function show()
    {
        $classId = $this->request->input('class_id');
        $result = Classes::find($classId);
        return $this->success($result);
    }

    public function joinClass()
    {

    }

    public function fetchLives()
    {
        $classId = $this->request->input('class_id');

        $lives = Live::with(['classPlan'])->where('class_id',$classId)->orderBy('plan_id')->get();

        return $this->success($lives);
    }

}
