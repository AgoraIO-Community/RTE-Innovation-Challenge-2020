package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.DeviceTypeModule;
import com.xiaoyang.poweroperation.device.contract.DeviceTypeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.device.DeviceTypeActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/12/2020 13:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DeviceTypeModule.class, dependencies = AppComponent.class)
public interface DeviceTypeComponent {
    void inject(DeviceTypeActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceTypeComponent.Builder view(DeviceTypeContract.View view);
        DeviceTypeComponent.Builder appComponent(AppComponent appComponent);
        DeviceTypeComponent build();
    }
}