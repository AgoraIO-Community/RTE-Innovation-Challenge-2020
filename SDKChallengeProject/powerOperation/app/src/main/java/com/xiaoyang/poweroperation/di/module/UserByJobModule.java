package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.chart.contract.UserByJobContract;
import com.xiaoyang.poweroperation.chart.model.UserByJobModel;


@Module
public abstract class UserByJobModule {

    @Binds
    abstract UserByJobContract.Model bindUserByJobModel(UserByJobModel model);
}