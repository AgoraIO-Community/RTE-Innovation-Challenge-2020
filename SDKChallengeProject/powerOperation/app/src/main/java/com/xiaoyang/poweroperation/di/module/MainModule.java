package com.xiaoyang.poweroperation.di.module;

import dagger.Binds;
import dagger.Module;

import com.xiaoyang.poweroperation.ui.contract.MainContract;
import com.xiaoyang.poweroperation.ui.model.MainModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/09/2019 17:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class MainModule {

    @Binds
    abstract MainContract.Model bindMainModel(MainModel model);
}