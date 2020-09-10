package com.xiaoyang.poweroperation.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.xiaoyang.poweroperation.di.module.MessageDetailModule;
import com.xiaoyang.poweroperation.mine.contract.MessageDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xiaoyang.poweroperation.mine.MessageDetailActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/12/2020 13:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = MessageDetailModule.class, dependencies = AppComponent.class)
public interface MessageDetailComponent {
    void inject(MessageDetailActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        MessageDetailComponent.Builder view(MessageDetailContract.View view);
        MessageDetailComponent.Builder appComponent(AppComponent appComponent);
        MessageDetailComponent build();
    }
}