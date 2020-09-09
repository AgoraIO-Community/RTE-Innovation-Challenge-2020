package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.ui.widgets.MyGlideEngine;
import com.xylib.base.utils.MobileUtil;
import com.xylib.base.widgets.roundedimageview.RoundedImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Package:
 * ClassName:      MineInfoActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/24 09:54
 * Description:   个人中心
 */
public class MineInfoActivity extends BaseActivity {


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
    @BindView(R.id.img_photo)
    RoundedImageView imgPhoto;
    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_legal_username)
    TextView tvLegalUsername;
    @BindView(R.id.tv_legal_phone)
    TextView tvLegalPhone;
    @BindView(R.id.tv_emergency_name)
    TextView tvEmergencyName;
    @BindView(R.id.tv_emergency_phone)
    TextView tvEmergencyPhone;
    private static int REQUEST_CODE_CHOOSE = 2020;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_info;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("个人中心");
    }

    @OnClick(R.id.ll_photo)
    public void onViewClicked() {
        ArmsUtils.makeText(this, "更换头像");
//        customHelper.takePic(getTakePhoto());
        Matisse.from(MineInfoActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
//                .showPreview(false) // Default is `true`
                .forResult(REQUEST_CODE_CHOOSE);
    }

    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            String path = MobileUtil.getRealFilePath(this, mSelected.get(0));

            Log.v("yxy", "mSelected: " + mSelected.get(0).getPath() + path);
            Log.v("yxy", "path: " + path);

        }
    }

    private void uploadFile(String path) {
//        String picPath = "sdcard/temp.jpg";
        BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    ArmsUtils.snackbarText("上传文件成功:" + bmobFile.getFileUrl());
                } else {
                    Log.v("yxy", "===失败=》" + e.getMessage());
                    ArmsUtils.snackbarText("上传文件失败：" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

}