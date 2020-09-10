package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.DeviceListModule;
import com.xiaoyang.poweroperation.device.contract.DeviceListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.device.DeviceListActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/11/2020 13:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DeviceListModule.class, dependencies = AppComponent.class)
public interface DeviceListComponent {
    void inject(DeviceListActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceListComponent.Builder view(DeviceListContract.View view);
        DeviceListComponent.Builder appComponent(AppComponent appComponent);
        DeviceListComponent build();
    }
}