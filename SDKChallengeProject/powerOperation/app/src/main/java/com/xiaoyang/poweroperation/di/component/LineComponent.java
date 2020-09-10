package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.LineModule;
import com.xiaoyang.poweroperation.map.contract.LineContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.map.LineActivity;   


@ActivityScope
@Component(modules = LineModule.class, dependencies = AppComponent.class)
public interface LineComponent {
    void inject(LineActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        LineComponent.Builder view(LineContract.View view);
        LineComponent.Builder appComponent(AppComponent appComponent);
        LineComponent build();
    }
}