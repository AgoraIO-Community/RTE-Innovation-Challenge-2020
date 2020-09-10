package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.AddDeviceModule;
import com.xiaoyang.poweroperation.device.contract.AddDeviceContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.device.AddDeviceActivity;   


@ActivityScope
@Component(modules = AddDeviceModule.class, dependencies = AppComponent.class)
public interface AddDeviceComponent {
    void inject(AddDeviceActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        AddDeviceComponent.Builder view(AddDeviceContract.View view);
        AddDeviceComponent.Builder appComponent(AppComponent appComponent);
        AddDeviceComponent build();
    }
}