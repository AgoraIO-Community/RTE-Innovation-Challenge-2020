package com.xiaoyang.poweroperation.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.Line;
import com.xiaoyang.poweroperation.di.component.DaggerLineComponent;
import com.xiaoyang.poweroperation.map.contract.LineContract;
import com.xiaoyang.poweroperation.map.presenter.LinePresenter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LineActivity extends BaseActivity<LinePresenter> implements LineContract.View {

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
    @BindView(R.id.basicMap)
    MapView mapView;
    private List<Line> mDataList;
    private AMap aMap;
    Marker marker = null;
    private ProgressDialog progDialog = null;// 添加海量点时
    boolean isDestroy = false;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_line; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("洛阳关林路支线");
        mapView.onCreate(savedInstanceState);
        mDataList = new ArrayList<>();
//        lineName = getIntent().getStringExtra("line_name");
//        lineguid = getIntent().getStringExtra("lineguid");
        tvToolbarTitleTv.setText("");
        imgRight.setVisibility(View.GONE);
        imgRight.setImageResource(R.drawable.icon_add);
        init();
        getLineData();
    }

    private void initMap() {
        aMap.setMinZoomLevel(8);
        aMap.setMaxZoomLevel(20);
        //添加一个Marker用来展示海量点点击效果
        marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_blue);
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(bitmapDescriptor);
        overlayOptions.anchor(0.5f, 0.5f);

        final MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        aMap.setOnMultiPointClickListener(new AMap.OnMultiPointClickListener() {
            @Override
            public boolean onPointClick(MultiPointItem pointItem) {
                Log.i("amap ", "onPointClick");

                if (marker.isRemoved()) {
                    //调用amap clear之后会移除marker，重新添加一个

                    marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                //添加一个Marker用来展示海量点点击效果
                marker.setPosition(pointItem.getLatLng());
                marker.setToTop();
                return false;
            }
        });

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.619702, 112.453895), 12));
        showProgressDialog();
        //开启子线程读取文件
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<MultiPointItem> list = new ArrayList<MultiPointItem>();
                FileOutputStream outputStream = null;
                InputStream inputStream = null;
                String filePath = null;
                for (Line pole : mDataList) {
                    double lat = pole.getLatitude();
                    double lon = pole.getLongitude();
                    addCustomMarker(pole);
                }
                if (multiPointOverlay != null) {
                    multiPointOverlay.setItems(list);
                    multiPointOverlay.setEnable(true);
                }
                dismissProgressDialog();
            }
        }).start();

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent(LineActivity.this, DriveRouteActivity.class);
                intent.putExtra("latitude", latLng.latitude + "");
                intent.putExtra("longitude", latLng.longitude + "");
                startActivity(intent);
            }
        });
    }

    private void addCustomMarker(Line info) {
        double lat;
        double lon;
        lat = info.getLatitude();
        lon = info.getLongitude();
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(lat, lon));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_blue);
        markerOptions.icon(bitmapDescriptor);
        markerOptions.title(info.getName() + "\n" + info.getVoltagelevel() + "\n到这去");
        aMap.addMarker(markerOptions);
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在解析添加海量点中，请稍后...");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dismissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }

    }

    /**
     * 获取线路数据
     */
    private void getLineData() {
        BmobQuery<Line> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<Line>() {
            @Override
            public void done(List<Line> object, BmobException e) {
                if (e == null) {
                    mDataList = object;
                    Log.v("yxy", "getLineData" + mDataList.size());
                    initMap();
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
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
