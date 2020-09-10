package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.device.contract.CheckFeedBackContract;
import com.xiaoyang.poweroperation.device.model.CheckFeedBackModel;


@Module
public abstract class CheckFeedBackModule {

    @Binds
    abstract CheckFeedBackContract.Model bindCheckFeedBackModel(CheckFeedBackModel model);
}