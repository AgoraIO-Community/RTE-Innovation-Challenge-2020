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
use App\Model\LiveRoom;
use App\Model\LiveRoomUser;
use App\Model\UserClass;
use App\Model\UserCourse;
use App\Model\UserLive;
use Hyperf\DbConnection\Db;

class UserController extends AbstractController
{
    /**
     * 用户课程
     * @return array
     * Created on 2020/9/8 1:02 上午
     * Create by Zhuyunfeng
     */
    public function fetchUserCourse()
    {
        $user = $this->request->getAttribute('user');
        $result = UserCourse::with(['course'])->where('user_id', $user->id)->get();
        return $this->success($result);
    }

    /**
     * 用户班级
     * @return array
     * Created on 2020/9/8 1:02 上午
     * Create by Zhuyunfeng
     */
    public function fetchUserClasses()
    {
        $user = $this->request->getAttribute('user');
        $result = UserClass::with(['class', 'class.course'])->where('user_id', $user->id)->orderByDesc('created_at')->get();
        return $this->success($result);
    }

    /**
     * 用户直播课程
     * @return array
     * Created on 2020/9/8 1:02 上午
     * Create by Zhuyunfeng
     */
    public function fetchUserLives()
    {
        $user = $this->request->getAttribute('user');
        $result = UserLive::where('user_id', $user->id)->get();


        return $this->success($result);
    }

    public function saveUserProfile()
    {

    }

    public function fetchUserProfile()
    {

    }

    /**
     * 加入班级
     * @return array
     * Created on 2020/9/8 1:02 上午
     * Create by Zhuyunfeng
     */
    public function joinClass()
    {
        $user = $this->request->getAttribute('user');
        $classId = $this->request->input('class_id');
        $courseId = $this->request->input('course_id');

        //用户加入班级
        $data = [
            'user_id' => $user->id,
            'class_id' => $classId
        ];
        try {
            Db::beginTransaction();
            $result = UserClass::UpdateOrCreate($data);

            //给用户排课
            $lives = Live::where('class_id', $classId)->get();
            $classInfo = Classes::find($classId);
            $type = $classInfo->type;   //直播间人数 1v1 1v2 1v3
            if ($lives->isNotEmpty()) {
                foreach ($lives as $k => $value) {
                    //查询是否有可用房间
                    $checkRoom = LiveRoom::where('live_id', $value->id)
                        ->where('class_id', $classId)
                        ->where('available_num', $type)
                        ->where('remain_num', '>', 0)
                        ->first();

                    if (!empty($checkRoom)) {
                        LiveRoomUser::create([
                            'user_id' => $user->id,
                            'room_id' => $checkRoom->id,
                            'class_id' => $classId,
                            'status' => 0 //初始化
                        ]);
                        LiveRoom::where('id', $checkRoom->id)->decrement('remain_num');
                    } else {
                        //新建教室
                        $createRoom = LiveRoom::create([
                            'live_id' => $value->id,
                            'teacher_id' => 4,
                            'class_id' => $classId,
                            'remain_num' => $type,
                            'available_num' => $type,
                            'room_name' => 'Room-' . $classId . '-' . $value->id . '-4-' . time(),
                            'started_at' => $value->started_at,
                            'ended_at' => $value->ended_at
                        ]);

                        if (!empty($result)) {

                            LiveRoomUser::create([
                                'user_id' => $user->id,
                                'room_id' => $createRoom->id,
                                'class_id' => $classId,
                                'status' => 0 //初始化
                            ]);
                            LiveRoom::where('id', $createRoom->id)->decrement('remain_num');
                        }
                    }

                }
            }

            Db::commit();;
            return $this->success($result);
        } catch (\Exception $exception) {
            Db::rollBack();;
            return $this->failed($exception->getMessage());
        }

    }

    /**
     * 订阅课程
     * Created on 2020/9/8 1:01 上午
     * Create by Zhuyunfeng
     */
    public function subscribeCourse()
    {
        $user = $this->request->getAttribute('user');
        $courseId = $this->request->input('course_id');
    }

    /**
     * 进入直播
     * @return array
     * Created on 2020/9/8 1:01 上午
     * Create by Zhuyunfeng
     */
    public function enterLive()
    {
        $user = $this->request->getAttribute('user');
        $liveId = $this->request->input('live_id');
        $classId = $this->request->input('class_id');

        $room = LiveRoom::where('class_id',$classId)->where('live_id',$liveId)->first();
        $data = [
            'userInfo' => $user,
            'liveInfo' => Live::find($liveId),
            'liveRoomInfo'=>$room,
            'roomUsers' =>LiveRoomUser::with(['user'])->where('room_id',$room->id)->get()
        ];
        return $this->success($data);


    }

}
