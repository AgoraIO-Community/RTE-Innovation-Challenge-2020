package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.mine.contract.ChatContract;
import com.xiaoyang.poweroperation.mine.model.ChatModel;


@Module
public abstract class ChatModule {

    @Binds
    abstract ChatContract.Model bindChatModel(ChatModel model);
}