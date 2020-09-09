package com.xiaoyang.poweroperation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.di.component.DaggerWorkTabComponent;
import com.xiaoyang.poweroperation.main.contract.WorkTabContract;
import com.xiaoyang.poweroperation.main.presenter.WorkTabPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 *
 * Package:
 * ClassName:      WorkTabFragment
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/9 22:51
 * Description:工作台
 */
public class WorkTabFragment extends BaseFragment<WorkTabPresenter> implements WorkTabContract.View {

    public static WorkTabFragment newInstance() {
        WorkTabFragment fragment = new WorkTabFragment();
        return fragment;
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerWorkTabComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_tab, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

}
