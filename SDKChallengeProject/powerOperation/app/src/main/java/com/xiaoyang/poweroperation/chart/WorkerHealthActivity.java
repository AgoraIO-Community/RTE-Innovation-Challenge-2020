package com.xiaoyang.poweroperation.chart;

import android.content.Intent;
import android.os.Bundle;
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
import com.xiaoyang.poweroperation.chart.contract.WorkerHealthContract;
import com.xiaoyang.poweroperation.chart.presenter.WorkerHealthPresenter;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.di.component.DaggerWorkerHealthComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      WorkerHealthActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/19 16:59
 * Description:
 */
public class WorkerHealthActivity extends BaseActivity<WorkerHealthPresenter> implements WorkerHealthContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chart_health)
    PieChart pcChart;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWorkerHealthComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_worker_health; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    private void setChartData(List<Device> dataList, String content) {
        pcChart.getDescription().setEnabled(false);//设置描述
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int totalCount = 0;

        for (int i = 0; i < dataList.size(); i++) {//list为数据列表
            Device be = dataList.get(i);
//            int num = be.get();
//            switch (be.getValue()){
//                case "正常":
//                    colors.add(Color.parseColor("#1afa29"));
//                    break;
//                case "注意":
//                    colors.add(Color.parseColor("#e9e28d"));
//                    break;
//                case "异常":
//                    colors.add(Color.parseColor("#f4ea2a"));
//                    break;
//                case "严重":
//                    colors.add(Color.parseColor("#d81e06"));
//                    break;
//            }

//            entries.add(new PieEntry((float) num, be.getValue()));
//            totalCount += num;
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
