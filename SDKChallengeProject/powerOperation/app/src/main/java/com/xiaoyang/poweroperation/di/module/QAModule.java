package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.mine.contract.QAContract;
import com.xiaoyang.poweroperation.mine.model.QAModel;


@Module
public abstract class QAModule {

    @Binds
    abstract QAContract.Model bindQAModel(QAModel model);
}