package com.xiaoyang.poweroperation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xiaoyang.poweroperation.mine.contract.PhoneBookContract;
import com.xiaoyang.poweroperation.mine.model.PhoneBookModel;


@Module
public abstract class PhoneBookModule {

    @Binds
    abstract PhoneBookContract.Model bindPhoneBookModel(PhoneBookModel model);
}