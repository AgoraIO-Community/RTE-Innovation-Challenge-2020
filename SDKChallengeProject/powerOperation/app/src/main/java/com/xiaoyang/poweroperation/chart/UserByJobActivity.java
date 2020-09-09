package com.xiaoyang.poweroperation.chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.app.utils.Fill;
import com.xiaoyang.poweroperation.chart.contract.UserByJobContract;
import com.xiaoyang.poweroperation.chart.presenter.UserByJobPresenter;
import com.xiaoyang.poweroperation.data.entity.ChartDataEntity;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerUserByJobComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UserByJobActivity extends BaseActivity<UserByJobPresenter> implements UserByJobContract.View, OnChartValueSelectedListener {

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
    @BindView(R.id.chart1)
    BarChart chart;
    private List<ChartDataEntity> mDataList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserByJobComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_user_by_job; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("人员统计");
        mDataList = new ArrayList<>();
        initChart();
        getUserCountByType();
    }

    @Override
    public void showLoading() {

    }

    /**
     * 获取用户统计
     */
    private void getUserCountByType() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        //是否返回所统计的总条数
        bmobQuery.setHasGroupCount(true);
        bmobQuery.groupby(new String[]{"job"});
        bmobQuery.findStatistics(User.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Log.e("yxy", "查询成功：" + jsonArray.length() + jsonArray.toString());
                    ArmsUtils.snackbarText("查询成功：" + jsonArray.length() + jsonArray.toString());
//                    mDataList = GsonUtils.fromJson(jsonArray.toString(), new TypeToken<List<ChartDataEntity>>() {
//                    }.getType());

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChartDataEntity chart = new ChartDataEntity();
                            chart.setNum(Integer.valueOf(jsonArray.getJSONObject(i).get("_count").toString()));
                            chart.setId(jsonArray.getJSONObject(i).get("job").toString());
                            mDataList.add(chart);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        int sum = jsonObject.getInt("_sumScore");
                        Log.e("yxy", "sum：" + jsonObject.toString());
                        ArmsUtils.snackbarText("sum：" + jsonObject.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    setChartData(4, 150);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
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


    private void initChart() {
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(true);
        chart.getDescription().setEnabled(false);

        chart.setMaxVisibleValueCount(60);

        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(4);

        chart.animateXY(2000, 2000);
    }

    private void setChartData(int count, float range) {
        Log.v("yxy", "===>" + mDataList.size());
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            values.add(new BarEntry(i, mDataList.get(i).getNum()));
        }
        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, endColor1));
            gradientFills.add(new Fill(startColor2, endColor2));
            gradientFills.add(new Fill(startColor3, endColor3));
            gradientFills.add(new Fill(startColor4, endColor4));
            gradientFills.add(new Fill(startColor5, endColor5));
//
//            set1.setF(gradientFills);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
