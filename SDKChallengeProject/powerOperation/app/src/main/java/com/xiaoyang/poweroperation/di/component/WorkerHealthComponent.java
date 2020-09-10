package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.WorkerHealthModule;
import com.xiaoyang.poweroperation.chart.contract.WorkerHealthContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.chart.WorkerHealthActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/19/2020 16:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = WorkerHealthModule.class, dependencies = AppComponent.class)
public interface WorkerHealthComponent {
    void inject(WorkerHealthActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        WorkerHealthComponent.Builder view(WorkerHealthContract.View view);
        WorkerHealthComponent.Builder appComponent(AppComponent appComponent);
        WorkerHealthComponent build();
    }
}