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
use App\Model\ClassPlan;
use App\Model\Course;
use App\Model\Live;
use App\Model\UserClass;

class SystemController extends AbstractController
{
    public function generateLives(){
        $class = Classes::get();

        foreach ($class as $k=>$v){
            $classId = $v->id;

            $classPlans =ClassPlan::where('class_id',$classId)->get();
            foreach ($classPlans as $k1=>$v1){
                Live::updateOrCreate([
                    'class_id'=>$classId,
                    'plan_id'=>$v1->id
                ],[
                    'started_at'=>$v1->started_at,
                    'ended_at'=>$v1->ended_at
                ]);
            }



        }


    }

}
