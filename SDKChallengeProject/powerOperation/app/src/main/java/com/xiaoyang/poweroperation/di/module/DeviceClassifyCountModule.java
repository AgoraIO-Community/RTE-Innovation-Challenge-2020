package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.chart.contract.DeviceClassifyCountContract;
import com.xiaoyang.poweroperation.chart.model.DeviceClassifyCountModel;


@Module
public abstract class DeviceClassifyCountModule {

    @Binds
    abstract DeviceClassifyCountContract.Model bindDeviceClassifyCountModel(DeviceClassifyCountModel model);
}