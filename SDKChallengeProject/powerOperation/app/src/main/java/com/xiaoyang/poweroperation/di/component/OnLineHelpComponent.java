package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.OnLineHelpModule;
import com.xiaoyang.poweroperation.mine.contract.OnLineHelpContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.OnLineHelpActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/12/2020 13:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = OnLineHelpModule.class, dependencies = AppComponent.class)
public interface OnLineHelpComponent {
    void inject(OnLineHelpActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        OnLineHelpComponent.Builder view(OnLineHelpContract.View view);
        OnLineHelpComponent.Builder appComponent(AppComponent appComponent);
        OnLineHelpComponent build();
    }
}