package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.UserByJobModule;
import com.xiaoyang.poweroperation.chart.contract.UserByJobContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.chart.UserByJobActivity;   


@ActivityScope
@Component(modules = UserByJobModule.class, dependencies = AppComponent.class)
public interface UserByJobComponent {
    void inject(UserByJobActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        UserByJobComponent.Builder view(UserByJobContract.View view);
        UserByJobComponent.Builder appComponent(AppComponent appComponent);
        UserByJobComponent build();
    }
}