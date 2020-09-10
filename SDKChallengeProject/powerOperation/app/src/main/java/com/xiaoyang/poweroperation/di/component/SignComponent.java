package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.SignModule;
import com.xiaoyang.poweroperation.worktab.contract.SignContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.worktab.SignActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/13/2020 12:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SignModule.class, dependencies = AppComponent.class)
public interface SignComponent {
    void inject(SignActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        SignComponent.Builder view(SignContract.View view);
        SignComponent.Builder appComponent(AppComponent appComponent);
        SignComponent build();
    }
}