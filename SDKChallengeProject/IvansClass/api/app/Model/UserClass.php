<?php

declare (strict_types=1);
namespace App\Model;

use Hyperf\DbConnection\Model\Model;
/**
 */
class UserClass extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'user_classes';
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = ['user_id','class_id'];
    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [];

    public function class(){
        return $this->hasOne(Classes::class,'id','class_id');
    }
}
