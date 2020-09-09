package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.net.Uri;
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
import com.xiaoyang.poweroperation.data.entity.QA;
import com.xiaoyang.poweroperation.di.component.DaggerQAComponent;
import com.xiaoyang.poweroperation.mine.adapter.QaAdapter;
import com.xiaoyang.poweroperation.mine.contract.QAContract;
import com.xiaoyang.poweroperation.mine.presenter.QAPresenter;
import com.xylib.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class QAActivity extends BaseActivity<QAPresenter> implements QAContract.View {


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
    @BindView(R.id.img_qa)
    ImageView imgQa;
    @BindView(R.id.rl_qa)
    RelativeLayout rlQa;
    @BindView(R.id.img_phone)
    ImageView imgPhone;
    @BindView(R.id.rl_call)
    RelativeLayout rlCall;
    @BindView(R.id.rlv_qa)
    RecyclerView rlvQa;
    private List<QA> mDataList;
    private QaAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerQAComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_q; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("支持与服务");
        initQaRecycleView();
        getQAData();
    }

    private void initQaRecycleView() {
        mDataList = new ArrayList<>();
        mAdapter = new QaAdapter(R.layout.item_qa_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        rlvQa.setAdapter(mAdapter);
        rlvQa.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
    }

    /**
     * 获取qa数据
     */
    private void getQAData() {
        BmobQuery<QA> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<QA>() {
            @Override
            public void done(List<QA> object, BmobException e) {
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


    @OnClick(R.id.rl_call)
    public void onViewClicked() {
        callPhone("688658888");
    }

    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
