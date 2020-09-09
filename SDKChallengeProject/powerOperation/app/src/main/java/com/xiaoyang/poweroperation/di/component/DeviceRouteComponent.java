package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.DeviceRouteModule;
import com.xiaoyang.poweroperation.map.contract.DeviceRouteContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.map.DeviceRouteActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/18/2020 18:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DeviceRouteModule.class, dependencies = AppComponent.class)
public interface DeviceRouteComponent {
    void inject(DeviceRouteActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceRouteComponent.Builder view(DeviceRouteContract.View view);
        DeviceRouteComponent.Builder appComponent(AppComponent appComponent);
        DeviceRouteComponent build();
    }
}