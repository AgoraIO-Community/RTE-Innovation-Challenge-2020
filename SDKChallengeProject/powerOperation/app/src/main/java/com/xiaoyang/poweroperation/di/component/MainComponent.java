package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.MainModule;
import com.xiaoyang.poweroperation.ui.contract.MainContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.ui.activity.MainActivity;


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
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        MainComponent.Builder view(MainContract.View view);
        MainComponent.Builder appComponent(AppComponent appComponent);
        MainComponent build();
    }
}