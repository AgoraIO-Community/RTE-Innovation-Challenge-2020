package com.xiaoyang.poweroperation.chart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.chart.contract.DeviceHealthContract;
import com.xiaoyang.poweroperation.chart.presenter.DeviceHealthPresenter;
import com.xiaoyang.poweroperation.data.entity.ChartDataEntity;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.di.component.DaggerDeviceHealthComponent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      DeviceHealthActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/19 16:44
 * Description:   设备健康
 */
public class DeviceHealthActivity extends BaseActivity<DeviceHealthPresenter> implements DeviceHealthContract.View {

    @BindView(R.id.chart_health)
    PieChart pcChart;
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
    private List<ChartDataEntity> mDataList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceHealthComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device_health; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("设备健康统计");
        mDataList = new ArrayList<>();
        getDeviceList();

    }

    /**
     * 初始化图表
     */
    private void setChartData(List<ChartDataEntity> dataList, String content) {
        pcChart.getDescription().setEnabled(false);//设置描述
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int totalCount = 0;

        for (int i = 0; i < dataList.size(); i++) {
            ChartDataEntity be = dataList.get(i);
            int num = be.getNum();
            switch (be.getId()) {
                case "1":
                    colors.add(Color.parseColor("#1afa29"));
                    break;
                case "2":
                    colors.add(Color.parseColor("#e9e28d"));
                    break;
                case "3":
                    colors.add(Color.parseColor("#f4ea2a"));
                    break;
                case "4":
                    colors.add(Color.parseColor("#d81e06"));
                    break;
            }

            entries.add(new PieEntry((float) num, be.getLabel()));
            totalCount += num;
        }

        pcChart.setDrawEntryLabels(true);

        PieDataSet dataSet = new PieDataSet(entries, "");

        //设置饼状Item之间的间隙
        dataSet.setSliceSpace(0f);
        //设置饼状Item被选中时变化的距离
        dataSet.setSelectionShift(5f);
        // 为DataSet中的数据匹配上颜色集(饼图Item颜色)
        dataSet.setColors(colors);
        //dataSet.setValueTextColor(getResources().getColor(R.color.transparent));
        // 引线的长度基准值
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        // 设置数字引线（斜线）的长度
        dataSet.setValueLinePart1Length(0.3f);
        // 设置数字引线（水平线）的长度
        dataSet.setValueLinePart2Length(0.6f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        // 设置y值在图内还是图外
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);


        // 设置是否显示图标
        dataSet.setDrawIcons(true);
        dataSet.setDrawValues(true);

        Legend legend = pcChart.getLegend();
//        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        legend.setEnabled(true);
        legend.setTextColor(getResources().getColor(R.color.black));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setForm(Legend.LegendForm.SQUARE); //设置图例的形状
        legend.setFormSize(10);                      //设置图例的大小
        legend.setFormToTextSpace(10f);              //设置每个图例实体中标签和形状之间的间距
        legend.setDrawInside(true);
        legend.setWordWrapEnabled(true);              //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        legend.setXEntrySpace(10f);
        legend.setTextSize(12f);                      //设置图例标签文本的大小
        legend.setTextColor(getResources().getColor(R.color.black));//设置图例标签文本的颜色
        PieData data = new PieData(dataSet);
        // 设置是否显示数据实体(百分比，true:以下属性才有意义)
        data.setDrawValues(true);
        data.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(R.color.white));
        pcChart.setData(data);

        // undo all highlights
        pcChart.highlightValues(null);
        pcChart.setCenterText(totalCount + "\n" + content);
        pcChart.animateY(1400, Easing.EaseInOutQuad);// 设置pieChart图表展示动画效果
        pcChart.setDrawSliceText(false);
        // 将图表重绘以显示设置的属性和数据
        pcChart.invalidate();

    }

    private void getDeviceList() {
        BmobQuery<Device> bmobQuery = new BmobQuery<>();
        //是否返回所统计的总条数
        bmobQuery.setHasGroupCount(true);
        bmobQuery.groupby(new String[]{"status"});
        bmobQuery.findStatistics(Device.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChartDataEntity chart = new ChartDataEntity();
                            chart.setNum(Integer.valueOf(jsonArray.getJSONObject(i).get("_count").toString()));
                            String status = jsonArray.getJSONObject(i).get("status").toString();
                            chart.setId(status);
                            String label = "";
                            switch (status) {
                                case "1":
                                    label = "正常";
                                    break;
                                case "2":
                                    label = "注意";
                                    break;
                                case "3":
                                    label = "警告";
                                    break;
                                case "4":
                                    label = "严重";
                                    break;
                            }
                            chart.setLabel(label);
                            mDataList.add(chart);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
                setChartData(mDataList, "设备健康统计");
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
