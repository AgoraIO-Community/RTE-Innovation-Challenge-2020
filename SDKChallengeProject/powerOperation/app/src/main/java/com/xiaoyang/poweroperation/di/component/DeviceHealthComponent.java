package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.DeviceHealthModule;
import com.xiaoyang.poweroperation.chart.contract.DeviceHealthContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.chart.DeviceHealthActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/19/2020 16:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DeviceHealthModule.class, dependencies = AppComponent.class)
public interface DeviceHealthComponent {
    void inject(DeviceHealthActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceHealthComponent.Builder view(DeviceHealthContract.View view);
        DeviceHealthComponent.Builder appComponent(AppComponent appComponent);
        DeviceHealthComponent build();
    }
}