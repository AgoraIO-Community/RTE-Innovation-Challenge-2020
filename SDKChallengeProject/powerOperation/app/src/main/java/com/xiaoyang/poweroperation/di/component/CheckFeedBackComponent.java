package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.CheckFeedBackModule;
import com.xiaoyang.poweroperation.device.contract.CheckFeedBackContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.device.CheckFeedBackActivity;   


@ActivityScope
@Component(modules = CheckFeedBackModule.class, dependencies = AppComponent.class)
public interface CheckFeedBackComponent {
    void inject(CheckFeedBackActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        CheckFeedBackComponent.Builder view(CheckFeedBackContract.View view);
        CheckFeedBackComponent.Builder appComponent(AppComponent appComponent);
        CheckFeedBackComponent build();
    }
}