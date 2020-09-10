<?php

declare (strict_types=1);
namespace App\Model;

use Hyperf\DbConnection\Model\Model;
/**
 * @property int $id
 * @property int $user_id
 * @property int $room_id
 * @property int $class_id
 * @property string $status
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class LiveRoomUser extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'live_room_users';
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
//    protected $fillable = [];
    protected $guarded = [];
    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = ['id' => 'integer', 'user_id' => 'integer', 'room_id' => 'integer', 'class_id' => 'integer', 'created_at' => 'datetime', 'updated_at' => 'datetime'];

    public function user(){
        return $this->hasOne(User::class,'id','user_id');
    }
}
