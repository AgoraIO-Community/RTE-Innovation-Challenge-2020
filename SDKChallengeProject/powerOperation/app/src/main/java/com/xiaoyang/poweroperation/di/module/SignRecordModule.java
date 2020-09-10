package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.mine.contract.SignRecordContract;
import com.xiaoyang.poweroperation.mine.model.SignRecordModel;


@Module
public abstract class SignRecordModule {

    @Binds
    abstract SignRecordContract.Model bindSignRecordModel(SignRecordModel model);
}