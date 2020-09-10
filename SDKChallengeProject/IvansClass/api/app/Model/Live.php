<?php

declare (strict_types=1);
namespace App\Model;

use Hyperf\DbConnection\Model\Model;
/**
 */
class Live extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'lives';
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = ['class_id','plan_id','started_at','ended_at'];
    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [];

    public function classPlan(){
        return $this->hasOne(ClassPlan::class,'id','plan_id');
    }
}
