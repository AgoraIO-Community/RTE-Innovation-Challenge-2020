package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.map.contract.LineContract;
import com.xiaoyang.poweroperation.map.model.LineModel;


@Module
public abstract class LineModule {

    @Binds
    abstract LineContract.Model bindLineModel(LineModel model);
}