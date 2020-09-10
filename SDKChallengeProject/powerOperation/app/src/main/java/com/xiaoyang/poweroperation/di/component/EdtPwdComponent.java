package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.EdtPwdModule;
import com.xiaoyang.poweroperation.account.contract.EdtPwdContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.account.EdtPwdActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/18/2020 17:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = EdtPwdModule.class, dependencies = AppComponent.class)
public interface EdtPwdComponent {
    void inject(EdtPwdActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        EdtPwdComponent.Builder view(EdtPwdContract.View view);
        EdtPwdComponent.Builder appComponent(AppComponent appComponent);
        EdtPwdComponent build();
    }
}