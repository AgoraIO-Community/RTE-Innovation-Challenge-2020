package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.device.contract.AddDeviceContract;
import com.xiaoyang.poweroperation.device.model.AddDeviceModel;


@Module
public abstract class AddDeviceModule {

    @Binds
    abstract AddDeviceContract.Model bindAddDeviceModel(AddDeviceModel model);
}