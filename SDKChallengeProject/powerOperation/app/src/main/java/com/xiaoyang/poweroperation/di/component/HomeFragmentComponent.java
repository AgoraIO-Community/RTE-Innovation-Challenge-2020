package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.HomeFragmentModule;
import com.xiaoyang.poweroperation.main.contract.HomeFragmentContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xiaoyang.poweroperation.main.HomeFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/09/2020 22:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = HomeFragmentModule.class, dependencies = AppComponent.class)
public interface HomeFragmentComponent {
    void inject(HomeFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HomeFragmentComponent.Builder view(HomeFragmentContract.View view);

        HomeFragmentComponent.Builder appComponent(AppComponent appComponent);

        HomeFragmentComponent build();
    }
}