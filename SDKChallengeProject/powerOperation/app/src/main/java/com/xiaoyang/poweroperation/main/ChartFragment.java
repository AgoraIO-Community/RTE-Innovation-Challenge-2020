package com.xiaoyang.poweroperation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.chart.DeviceClassifyCountActivity;
import com.xiaoyang.poweroperation.chart.DeviceHealthActivity;
import com.xiaoyang.poweroperation.chart.UserByJobActivity;
import com.xiaoyang.poweroperation.data.entity.DataBean;
import com.xiaoyang.poweroperation.data.entity.WorkTableEntity;
import com.xiaoyang.poweroperation.di.component.DaggerChartComponent;
import com.xiaoyang.poweroperation.main.adapter.ImageAdapter;
import com.xiaoyang.poweroperation.main.adapter.WorkTableAdapter;
import com.xiaoyang.poweroperation.main.contract.ChartContract;
import com.xiaoyang.poweroperation.main.presenter.ChartPresenter;
import com.xylib.base.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      ChartFragment
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/9 22:51
 * Description:
 */
public class ChartFragment extends BaseFragment<ChartPresenter> implements ChartContract.View {

    @BindView(R.id.rlv_user)
    RecyclerView rlvUser;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.banner)
    Banner banner;

    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerChartComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initBannerView();
        initUserRecycleView();
        initDeviceRecycleView();
    }

    private void initBannerView() {
        ImageAdapter adapter = new ImageAdapter(DataBean.getTestData());
        banner.setAdapter(adapter)//设置适配器
//              .setCurrentItem(3,false)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
//                .setBannerRound(BannerUtils.dp2px(15))//圆角
//                .addPageTransformer(new AlphaPageTransformer())//添加切换效果
                .setIndicator(new CircleIndicator(getActivity()))//设置指示器
                .addOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                })//添加切换监听
                .setOnBannerListener((data, position) -> {
                    Snackbar.make(banner, ((DataBean) data).title, Snackbar.LENGTH_SHORT).show();
                    LogUtils.d("position：" + position);
                });//设置点击事件,传this也行
//        banner.setBannerGalleryEffect(18, 10);
        banner.setIndicatorGravity(IndicatorConfig.Direction.CENTER);
    }


    private void initUserRecycleView() {
        List<WorkTableEntity> mDataList = new ArrayList<>();
        WorkTableEntity work = null;
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setTitle("人员统计");
        work.setType("tongji");
        mDataList.add(work);
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setType("faqi");
        work.setTitle("健康统计");
//        mDataList.add(work);
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setTitle("性别统计");
        work.setType("task");
//        mDataList.add(work);
        WorkTableAdapter mAdapter = new WorkTableAdapter(R.layout.item_work_table_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
                    WorkTableEntity workData = mDataList.get(position);
                    Intent intent = null;
                    switch (workData.getType()) {
                        case "tongji":
                            intent = new Intent(getContext(), UserByJobActivity.class);
                            startActivity(intent);
                            break;
//                        case "faqi":
//                            intent = new Intent(getContext(), MyInitiateActivity.class);
//                            startActivity(intent);
//                            break;
//                        case "task":
//                            intent = new Intent(getContext(), MyTaskActivity.class);
//                            startActivity(intent);
//                            break;
                    }

                }
        );
        rlvUser.setAdapter(mAdapter);
        rlvUser.setLayoutManager(new GridLayoutManager(Utils.getApp(), 4));
    }

    private void initDeviceRecycleView() {
        List<WorkTableEntity> mDataList = new ArrayList<>();
        WorkTableEntity work = null;
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setTitle("设备统计");
        work.setType("1");
        mDataList.add(work);
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setType("2");
        work.setTitle("设备健康统计");
        mDataList.add(work);
        work = new WorkTableEntity();
        work.setImg(R.drawable.icon_chart_tongji);
        work.setTitle("");
        work.setType("3");
        mDataList.add(work);
        WorkTableAdapter mAdapter = new WorkTableAdapter(R.layout.item_work_table_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
                    WorkTableEntity workData = mDataList.get(position);
                    Intent intent = null;
                    switch (workData.getType()) {
                        case "1":
                            intent = new Intent(getContext(), DeviceClassifyCountActivity.class);
                            startActivity(intent);
                            break;
                        case "2":
                            intent = new Intent(getContext(), DeviceHealthActivity.class);
                            startActivity(intent);
                            break;
                        case "3":

                            break;
                    }
                }
        );
        rlvDevice.setAdapter(mAdapter);
        rlvDevice.setLayoutManager(new GridLayoutManager(Utils.getApp(), 4));
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

    @Override
    public void killMyself() {

    }
}
