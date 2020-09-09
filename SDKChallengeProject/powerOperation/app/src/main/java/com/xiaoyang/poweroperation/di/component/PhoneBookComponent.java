package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.PhoneBookModule;
import com.xiaoyang.poweroperation.mine.contract.PhoneBookContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.PhoneBookActivity;   


@ActivityScope
@Component(modules = PhoneBookModule.class, dependencies = AppComponent.class)
public interface PhoneBookComponent {
    void inject(PhoneBookActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        PhoneBookComponent.Builder view(PhoneBookContract.View view);
        PhoneBookComponent.Builder appComponent(AppComponent appComponent);
        PhoneBookComponent build();
    }
}