package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.SignRecord;
import com.xiaoyang.poweroperation.di.component.DaggerSignRecordComponent;
import com.xiaoyang.poweroperation.mine.adapter.SignRecordAdapter;
import com.xiaoyang.poweroperation.mine.contract.SignRecordContract;
import com.xiaoyang.poweroperation.mine.presenter.SignRecordPresenter;
import com.xylib.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SignRecordActivity extends BaseActivity<SignRecordPresenter> implements SignRecordContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.tv_toolbar_title_tv)
    TextView tvToolbarTitleTv;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.rlv_record)
    RecyclerView rlvRecord;
    private List<SignRecord> mDataList;
    private SignRecordAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSignRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_sign_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("签到记录");
        initMessageRecycleView();
        getSignRecordList();
    }

    private void initMessageRecycleView() {
        mDataList = new ArrayList<>();
        mAdapter = new SignRecordAdapter(R.layout.item_sign_record, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {


        });
        rlvRecord.setAdapter(mAdapter);
        rlvRecord.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
    }

    private void getSignRecordList() {
        BmobQuery<SignRecord> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.include("signType");
        categoryBmobQuery.findObjects(new FindListener<SignRecord>() {
            @Override
            public void done(List<SignRecord> object, BmobException e) {
                if (e == null) {
                    Log.v("yxy", "查询成功" + object.size());
                    mDataList = object;
                    mAdapter.replaceData(object);
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
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

    @Override
    public void killMyself() {
        finish();
    }


}
