<?php

declare (strict_types=1);
namespace App\Model;

use Hyperf\DbConnection\Model\Model;
/**
 * @property int $id
 * @property int $live_id
 * @property int $teacher_id
 * @property int $room_name
 * @property string $started_at
 * @property string $ended_at
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 */
class LiveRoom extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'live_rooms';
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $guarded = [];
    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
//    protected $casts = ['id' => 'integer', 'live_id' => 'integer', 'teacher_id' => 'integer', 'room_name' => 'integer', 'created_at' => 'datetime', 'updated_at' => 'datetime'];
}
