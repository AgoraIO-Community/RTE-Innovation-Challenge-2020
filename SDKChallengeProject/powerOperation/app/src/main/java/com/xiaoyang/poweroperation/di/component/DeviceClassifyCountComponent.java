package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.DeviceClassifyCountModule;
import com.xiaoyang.poweroperation.chart.contract.DeviceClassifyCountContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.chart.DeviceClassifyCountActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/19/2020 17:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DeviceClassifyCountModule.class, dependencies = AppComponent.class)
public interface DeviceClassifyCountComponent {
    void inject(DeviceClassifyCountActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceClassifyCountComponent.Builder view(DeviceClassifyCountContract.View view);
        DeviceClassifyCountComponent.Builder appComponent(AppComponent appComponent);
        DeviceClassifyCountComponent build();
    }
}