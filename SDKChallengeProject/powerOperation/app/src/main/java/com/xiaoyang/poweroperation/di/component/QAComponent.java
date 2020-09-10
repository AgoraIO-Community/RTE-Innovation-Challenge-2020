package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.QAModule;
import com.xiaoyang.poweroperation.mine.contract.QAContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.QAActivity;   


@ActivityScope
@Component(modules = QAModule.class, dependencies = AppComponent.class)
public interface QAComponent {
    void inject(QAActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        QAComponent.Builder view(QAContract.View view);
        QAComponent.Builder appComponent(AppComponent appComponent);
        QAComponent build();
    }
}