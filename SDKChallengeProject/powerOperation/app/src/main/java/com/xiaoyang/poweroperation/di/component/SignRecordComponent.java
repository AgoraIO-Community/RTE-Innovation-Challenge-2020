package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.SignRecordModule;
import com.xiaoyang.poweroperation.mine.contract.SignRecordContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.SignRecordActivity;   


@ActivityScope
@Component(modules = SignRecordModule.class, dependencies = AppComponent.class)
public interface SignRecordComponent {
    void inject(SignRecordActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        SignRecordComponent.Builder view(SignRecordContract.View view);
        SignRecordComponent.Builder appComponent(AppComponent appComponent);
        SignRecordComponent build();
    }
}