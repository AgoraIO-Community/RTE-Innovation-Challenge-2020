package com.xiaoyang.poweroperation.device;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.app.utils.SPUtils;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.data.entity.DeviceType;
import com.xiaoyang.poweroperation.device.adapter.DeviceAdapter;
import com.xiaoyang.poweroperation.device.contract.DeviceListContract;
import com.xiaoyang.poweroperation.device.presenter.DeviceListPresenter;
import com.xiaoyang.poweroperation.di.component.DaggerDeviceListComponent;
import com.xiaoyang.poweroperation.map.DriveRouteActivity;
import com.xylib.base.utils.GsonUtils;
import com.xylib.base.utils.StringUtil;
import com.xylib.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.xylib.base.utils.MobileUtil.hideKeyboard;

/**
 * Package:
 * ClassName:      DeviceListActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/11 13:16
 * Description:   设备列表
 */
public class DeviceListActivity extends BaseActivity<DeviceListPresenter> implements DeviceListContract.View {


    @BindView(R.id.rbt_map)
    RadioButton rbtMap;
    @BindView(R.id.rbt_list)
    RadioButton rbtList;
    @BindView(R.id.rgb_map)
    RadioGroup rgbMap;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rlv_device)
    RecyclerView rlvContent;
    @BindView(R.id.refreshLayoutMine)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    private List<Device> mDataList;
    private DeviceAdapter mAdapter;
    private static final int ADD_DEVICE_DATA = 2051;
    private AMap aMap;
    Marker marker = null;
    boolean isDestroy = false;
    private Marker clickMaker;
    private Marker curShowWindowMarker;
    private boolean infoWindowShown = false;
    private String loadType = "map";
    private String keyword = "";
    private String device_type;
    private String deviceTypeJson;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        imgRight.setImageResource(R.drawable.icon_add);
        edtSearch.setHint("请输入设备名称可供搜索");
        imgRight.setVisibility(View.VISIBLE);
        device_type = getIntent().getStringExtra("device_type");
        getDeviceTypeData();
        initDeviceRecycleView();
        init();
        initEvent();
        getDeviceList();
    }


    private void initDeviceRecycleView() {
        mDataList = new ArrayList<>();
        mAdapter = new DeviceAdapter(R.layout.item_device_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Device device = mDataList.get(position);
            Intent intent = new Intent(this, DeviceDetailActivity.class);
            intent.putExtra("device_id", device.getDevice_id());
            startActivity(intent);

        });
        rlvContent.setAdapter(mAdapter);
        rlvContent.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
    }


    private void getDeviceTypeData() {
        BmobQuery<DeviceType> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("device_type_id", device_type);
        categoryBmobQuery.findObjects(new FindListener<DeviceType>() {
            @Override
            public void done(List<DeviceType> object, BmobException e) {
                if (e == null) {
                    Log.v("yxy", "查询成功" + object.size());
                    deviceTypeJson = GsonUtils.toJson(object.get(0));
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

    private void getDeviceList() {
        keyword = edtSearch.getText().toString().trim();
        BmobQuery<Device> categoryBmobQuery = new BmobQuery<>();
        if (StringUtil.isNotBlank(keyword)) {
            categoryBmobQuery.addWhereMatches("device_name", keyword);
        }
        Log.v("yxy", device_type);
        if (StringUtil.isNotBlank(device_type)) {
            categoryBmobQuery.addWhereEqualTo("device_type_id", device_type);
        }
        categoryBmobQuery.findObjects(new FindListener<Device>() {
            @Override
            public void done(List<Device> object, BmobException e) {
                if (e == null) {
                    if (loadType.equals("list")) {
                        Log.v("yxy", "查询成功" + object.size());
                        mDataList = object;
                        mAdapter.replaceData(object);
                    } else if (loadType.equals("map")) {
                        mDataList = object;
                        initMap();
                    }
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

    private void initEvent() {
        rgbMap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbt_map:
                        loadType = "map";
                        mapView.setVisibility(View.VISIBLE);
                        mRefreshLayout.setVisibility(View.GONE);
                        getDeviceList();
                        break;
                    case R.id.rbt_list:
                        loadType = "list";
                        mapView.setVisibility(View.GONE);
                        mRefreshLayout.setVisibility(View.VISIBLE);
                        getDeviceList();
                        break;
                }
            }
        });
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent(DeviceListActivity.this, DriveRouteActivity.class);
                intent.putExtra("latitude", latLng.latitude + "");
                intent.putExtra("longitude", latLng.longitude + "");
                startActivity(intent);
            }
        });
        edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //点击搜索的时候隐藏软键盘
                hideKeyboard(edtSearch);
                getDeviceList();
                return true;
            }
            return false;
        });
    }

    private ProgressDialog progDialog = null;// 添加海量点时

    /**
     * 初始化地图
     */
    private void initMap() {
        aMap.clear();
        aMap.setMinZoomLevel(8);
        aMap.setMaxZoomLevel(20);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //判断当前marker信息窗口是否显示
                if (clickMaker != null && clickMaker.isInfoWindowShown()) {
                    clickMaker.hideInfoWindow();
                }
                //点击其它地方隐藏infoWindow
                if (curShowWindowMarker.isInfoWindowShown() && !infoWindowShown) {
                    infoWindowShown = true;
                    return;
                }

                if (curShowWindowMarker.isInfoWindowShown() && infoWindowShown) {
                    curShowWindowMarker.hideInfoWindow();
                }

            }
        });
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                curShowWindowMarker = marker;
                infoWindowShown = false;
                return false;
            }
        });
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent(DeviceListActivity.this, DriveRouteActivity.class);
                intent.putExtra("latitude", latLng.latitude + "");
                intent.putExtra("longitude", latLng.longitude + "");
                startActivity(intent);
            }
        });
        //添加一个Marker用来展示海量点点击效果
        marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.anchor(0.5f, 0.5f);
        final MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        aMap.setOnMultiPointClickListener(new AMap.OnMultiPointClickListener() {
            @Override
            public boolean onPointClick(MultiPointItem pointItem) {
                if (marker.isRemoved()) {
                    //调用amap clear之后会移除marker，重新添加一个
                    MarkerOptions markOptions = new MarkerOptions();
                    markOptions.position(pointItem.getLatLng());
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText("到" + pointItem.getTitle() + "去");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.BLACK);
                    textView.setBackgroundResource(R.drawable.custom_info_bubble);
                    markOptions.icon(BitmapDescriptorFactory.fromView(textView));
                    marker = aMap.addMarker(markOptions);
                }
                clickMaker = marker;
                return false;
            }
        });


        showProgressDialog();
        //开启子线程读取文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MultiPointItem> list = new ArrayList<MultiPointItem>();
                for (Device pole : mDataList) {
                    addCustomMarker(pole);
                }
                if (multiPointOverlay != null) {
                    multiPointOverlay.setItems(list);
                    multiPointOverlay.setEnable(true);
                }
                dismissProgressDialog();
            }
        }).start();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_DEVICE_DATA:
                    getDeviceList();
                    break;
            }
        }
    }

    private void addCustomMarker(Device info) {
        double lat;
        double lon;
        lat = info.getLatitude();
        lon = info.getLongitude();
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(lat, lon));

        int img = 0;
        if (info != null) {
            switch (info.getStatus()) {
                case 1:
                    img = R.drawable.icon_location_green;
                    break;
                case 2:
                    img = R.drawable.icon_location_light_yellow;
                    break;
                case 3:
                    img = R.drawable.icon_location_yellow;
                    break;
                case 4:
                    img = R.drawable.icon_location_red;
                    break;
            }
        }
//        img = R.drawable.icon_location_blue;
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(img);
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed);
        markerOptions.icon(bitmapDescriptor);
        markerOptions.title(info.getDevice_name() + "\n" + info.isRunstatus() + "\n" + "\n到这去");
        aMap.addMarker(markerOptions);
    }

    /**
     * 隐藏进度框
     */
    private void dismissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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
            searchDistrict();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
        }
    }

    private void searchDistrict() {
        String city = SPUtils.getInstance().getString("city");
        String province = "洛阳市";
        if (StringUtil.isNotBlank(city)) {
            province = city;
        }

        DistrictSearch districtSearch = new DistrictSearch(getApplicationContext());
        DistrictSearchQuery districtSearchQuery = new DistrictSearchQuery();
        districtSearchQuery.setKeywords(province);
        districtSearchQuery.setShowBoundary(true);
        districtSearchQuery.setShowChild(true);
        districtSearch.setQuery(districtSearchQuery);
        districtSearch.setOnDistrictSearchListener(districtResult -> {
            if (districtResult != null || districtResult.getDistrict() != null) {
                ArrayList<DistrictItem> data = districtResult.getDistrict();
                if (data != null && data.size() > 0) {
                    LatLonPoint center = data.get(0).getCenter();
                    Log.v("yxy", "center==>" + center.getLatitude() + "==" + center.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(center.getLatitude(), center.getLongitude()), 12));
                }
                return;
            }
        });
        //请求边界数据 开始展示
        districtSearch.searchDistrictAsyn();
    }


    @OnClick(R.id.img_right)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        intent.putExtra("type", device_type);
        intent.putExtra("json", deviceTypeJson);
        startActivity(intent);
    }
}
