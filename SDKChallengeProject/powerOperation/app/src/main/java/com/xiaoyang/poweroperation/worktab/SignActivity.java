package com.xiaoyang.poweroperation.worktab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.app.utils.LocationUtils;
import com.xiaoyang.poweroperation.app.utils.StringUtil;
import com.xiaoyang.poweroperation.data.entity.SignRecord;
import com.xiaoyang.poweroperation.data.entity.SignType;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerSignComponent;
import com.xiaoyang.poweroperation.worktab.contract.SignContract;
import com.xiaoyang.poweroperation.worktab.presenter.SignPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class SignActivity extends BaseActivity<SignPresenter> implements SignContract.View {


    private static final int REQUEST_PERMISSION_LOCATION = 0;
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
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_sign_view)
    TextView tvSignView;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.rl_sign)
    RelativeLayout rlSign;
    @BindView(R.id.edt_temp)
    EditText edtTemp;
    private List<SignType> mDataList;
    private String mSignType;
    private QMUIPopup mNormalPopup;
    private SignType mSelectType;
    private String address;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSignComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_sign; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("签到");
        mDataList = new ArrayList<>();
        //设置定位权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }

        }
        getLocation();
        getSignTypeData();
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


    /**
     * 获取签到类型
     */
    private void getSignTypeData() {
        BmobQuery<SignType> categoryBmobQuery = new BmobQuery<SignType>();
        categoryBmobQuery.findObjects(new FindListener<SignType>() {
            @Override
            public void done(List<SignType> object, BmobException e) {
                if (e == null) {
                    Log.v("yxy", "查询成功" + object.size() + object.get(0).getType());
                    mDataList = object;
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
    }

    /**
     * 签到
     */
    private void addSign() {
        SignRecord p2 = new SignRecord();
        p2.setLocation(address);
        if (StringUtil.isBlank(mSignType)) {
            ArmsUtils.snackbarText("请选择签到类型");
            return;
        }
        p2.setSignType(mSelectType);
        String tem = edtTemp.getText().toString().trim();
        if (StringUtil.isBlank(tem)) {
            ArmsUtils.snackbarText("请填写测量温度");
            return;
        }
        double temp = Double.valueOf(tem);
        p2.setTemperature(Double.valueOf(tem));
        if (temp >= 36.1 && temp <= 37) {
            p2.setHealthstatus(true);
        } else {
            p2.setHealthstatus(false);
        }
        p2.setSignTypeId(mSignType);
        p2.setUser(BmobUser.getCurrentUser(User.class));
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    edtTemp.setText("");
                    tvSignView.setText("");
                    ArmsUtils.snackbarText("签到成功");
                } else {
                    ArmsUtils.snackbarText("签到失败");
                }
            }
        });
    }

    private void getLocation() {
        LocationUtils.getLocation(location -> {
            if (location.getErrorCode() == 0) {
                //针对location进行相关操作,如location.getCity(),无需验证location是否为null;
                address = location.getAddress();
                tvAddress.setText(address);
                Log.v("yxy", "city==>" + address);
            } else {
                tvAddress.setText("定位失败，点击重试");
                Log.e("AmapError", "location Error, ErrCode:"
                        + location.getErrorCode() + ", errInfo:"
                        + location.getErrorInfo());
            }

        });
    }


    @OnClick({R.id.tv_sign_view, R.id.rl_sign, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_view:
                List<String> mListType = new ArrayList<>();
                for (int i = 0; i < mDataList.size(); i++) {
                    mListType.add(mDataList.get(i).getType());
                }
                String[] listItems = mListType.toArray(new String[mListType.size()]);
                List<String> data = new ArrayList<>();
                Collections.addAll(data, listItems);
                ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, data);
                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SignType type = mDataList.get(i);
                        tvSignView.setText(type.getType());
                        mSignType = type.getType_id();
                        mSelectType = type;
                        if (mNormalPopup != null) {
                            mNormalPopup.dismiss();
                        }
                    }
                };
                mNormalPopup = QMUIPopups.listPopup(this,
                        QMUIDisplayHelper.dp2px(this, 250),
                        QMUIDisplayHelper.dp2px(this, 300),
                        adapter,
                        onItemClickListener)
                        .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                        .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                        .shadow(true)
                        .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                        .skinManager(QMUISkinManager.defaultInstance(this))
                        .show(view);
                break;
            case R.id.rl_sign:
                addSign();
                break;
            case R.id.tv_address:
                getLocation();
                break;
        }
    }

}
