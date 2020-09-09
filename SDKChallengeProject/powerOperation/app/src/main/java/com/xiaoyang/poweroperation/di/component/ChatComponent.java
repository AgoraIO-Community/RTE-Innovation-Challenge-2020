package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.ChatModule;
import com.xiaoyang.poweroperation.mine.contract.ChatContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.ChatActivity;   


@ActivityScope
@Component(modules = ChatModule.class, dependencies = AppComponent.class)
public interface ChatComponent {
    void inject(ChatActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatComponent.Builder view(ChatContract.View view);
        ChatComponent.Builder appComponent(AppComponent appComponent);
        ChatComponent build();
    }
}