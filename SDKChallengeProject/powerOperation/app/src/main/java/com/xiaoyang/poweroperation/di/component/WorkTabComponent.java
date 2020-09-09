package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.WorkTabModule;
import com.xiaoyang.poweroperation.main.contract.WorkTabContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.main.WorkTabFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/09/2020 22:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = WorkTabModule.class, dependencies = AppComponent.class)
public interface WorkTabComponent {
    void inject(WorkTabFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        WorkTabComponent.Builder view(WorkTabContract.View view);
        WorkTabComponent.Builder appComponent(AppComponent appComponent);
        WorkTabComponent build();
    }
}