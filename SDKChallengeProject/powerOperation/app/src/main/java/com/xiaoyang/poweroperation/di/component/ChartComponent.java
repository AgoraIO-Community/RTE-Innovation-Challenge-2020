package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.ChartModule;
import com.xiaoyang.poweroperation.main.contract.ChartContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xiaoyang.poweroperation.main.ChartFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/09/2020 22:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = ChartModule.class, dependencies = AppComponent.class)
public interface ChartComponent {
    void inject(ChartFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChartComponent.Builder view(ChartContract.View view);

        ChartComponent.Builder appComponent(AppComponent appComponent);

        ChartComponent build();
    }
}