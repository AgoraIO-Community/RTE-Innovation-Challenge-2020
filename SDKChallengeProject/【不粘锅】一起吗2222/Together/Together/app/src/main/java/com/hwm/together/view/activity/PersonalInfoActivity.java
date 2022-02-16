package com.hwm.together.view.activity;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.eventbus.PickerMessageEvent;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.CommonUtil;
import com.hwm.together.util.FileUtilcll;
import com.hwm.together.util.GlideUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.SoftKeyboardUtil;
import com.hwm.together.util.StatusBarUtil;
import com.hwm.together.util.ToastUtil;
import com.hwm.together.util.picker.PickerUntil;
import com.hwm.together.view.fragment.MineFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PersonalInfoActivity";
    private ImageButton ibBack;
    private EditText etNickName;
    private TextView tvTitle,tvGender,tvLocation,tvDate;
    private PickerUntil pickerUntil;
    private String checkedProvince;
    private String checkedCity;
    private ImageView headImg;

    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE = 6;
    //打开相册的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;
    //相机回调
    private static final int CAMERA_RESULT_CODE = 1;
    ////相册回调
    private static final int PHOTO_RESULT_CODE = 2;
    //裁剪返回
    private static final int CROP_RESULT_CODE = 3;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;
    //拍照输出位置
    private Uri cameraPhotoUri;
    //截取图片位置
    private String cropPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        setContentView(R.layout.activity_personal_info);
        initView();
        getLastData();
        pickerUntil = new PickerUntil(this);
    }

    private void initView(){
        etNickName = findViewById(R.id.person_info_et_nickname);
        ibBack = findViewById(R.id.person_in_top_bar).findViewById(R.id.include_title_black_back);
        tvTitle = findViewById(R.id.person_in_top_bar).findViewById(R.id.include_title_black_title);
        tvGender = findViewById(R.id.person_info_tv_get_gender);
        tvDate = findViewById(R.id.person_info_tv_get_birthday);
        tvLocation = findViewById(R.id.person_info_tv_get_location);
        headImg = findViewById(R.id.person_info_headimg);
        tvTitle.setText("请完善个人信息");

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View view = findViewById(R.id.person_info_cl_parent_view);
        SoftKeyboardUtil.updateUI(this,view);
    }

    private void getLastData(){
        if (MyApplication.mUserInfo != null) {
            GlideUtil.loadCircleImg(this,MyApplication.mUserInfo.getHeadImg(),headImg);
            etNickName.setText(MyApplication.mUserInfo.getNickName());
            switch (MyApplication.mUserInfo.getSex()){
                case "0":
                    tvGender.setText("女");
                    break;
                case "1":
                    tvGender.setText("男");
                    break;
                case "2":
                    tvGender.setText("请选择生日");
            }
            if (MyApplication.mUserInfo.getBirthDay() != null ) {
                tvDate.setText(MyApplication.mUserInfo.getBirthDay().substring(0,10));
            }
            if (!TextUtils.equals("",MyApplication.mUserInfo.getProvince())){
                tvLocation.setText(MyApplication.mUserInfo.getProvince() + " " + MyApplication.mUserInfo.getCity());
            }
        }
    }

    //选择性别
    public void selectGender(View view){
        pickerUntil.showGenderPicker();
    }

    //选择生日
    public void selectBirthday(View view){
        pickerUntil.showDatePicker(tvDate.getText().toString());
    }

    //选择地区
    public void selectLocation(View view){
        pickerUntil.showLocationPicker();
    }

    public void setInfo(View view){
        UserInfo userInfo = new UserInfo();
        if(MyApplication.mUserInfo == null){
            ToastUtil.makeShort(PersonalInfoActivity.this,"登录信息异常，请重新登录");
            return;
        }
        if (etNickName.getText() == null || TextUtils.equals("",etNickName.getText())) {
            ToastUtil.makeShort(PersonalInfoActivity.this,"昵称不能为空");
            return;
        }
        tvLocation.getText().toString().split(" ");
        userInfo.setAccount(MyApplication.mUserInfo.getAccount());
        userInfo.setNickName(etNickName.getText().toString());
        userInfo.setProvince(checkedProvince);
        userInfo.setCity(checkedCity);
        if (!TextUtils.equals("请选择生日",tvDate.getText())) {
            userInfo.setBirthDay(tvDate.getText().toString());
        }
        if (!TextUtils.equals("请选择性别",tvGender.getText())) {
            if (TextUtils.equals("女",tvGender.getText().toString())) {
                userInfo.setSex("0");
            }else{
                userInfo.setSex("1");
            }
        }
        sendSetInfo(userInfo);
    }

    public void showPhotoDialog(View view) {
        builder = new AlertDialog.Builder(this);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo_type, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        takePhotoTV = layout.findViewById(R.id.photograph);
        choosePhotoTV = layout.findViewById(R.id.photo);
        cancelTV = layout.findViewById(R.id.cancel);
        //设置监听
        takePhotoTV.setOnClickListener(this);
        choosePhotoTV.setOnClickListener(this);
        cancelTV.setOnClickListener(this);
    }

    private void sendSetInfo(UserInfo userInfo){
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().putUpdatePersonInfo(userInfo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    if(!TextUtils.equals("",cropPhotoPath)){
                        //上传头像
                        sendUploadImg(MyApplication.mUserInfo.getAccount(),FileUtilcll.getFile());
                    }else {
                        //不上传
                        ToastUtil.makeShort(PersonalInfoActivity.this,"信息修改成功！");
                        MineFragment.refreshUserInfo = true;
                        Intent intent = new Intent(PersonalInfoActivity.this,NavigationFragmentActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(PersonalInfoActivity.this,code + message + data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(PersonalInfoActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    private void sendUploadImg(String account,File file){
        //上传图片需要MultipartBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().postUploadImg(account,body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() == null) {
                    ToastUtil.makeShort(PersonalInfoActivity.this,"上传图片出错");
                    return;
                }
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    MyLogUtil.i(TAG,"图片上传成功");
                    ToastUtil.makeShort(PersonalInfoActivity.this,"信息修改成功！");
                    MineFragment.refreshUserInfo = true;
                    Intent intent = new Intent(PersonalInfoActivity.this,NavigationFragmentActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(PersonalInfoActivity.this,code + message + data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(PersonalInfoActivity.this,"图片上次失败，信息修改成功！");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            //注册事件
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            //注销事件
            EventBus.getDefault().unregister(this);
        }
    }

    //处理选择器发送结果事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(PickerMessageEvent messageEvent){
        switch (messageEvent.getPickerType()){//1-性别，2-行业，3-现居地，4-日期
            case 1:
                tvGender.setText(messageEvent.getMessage());
                break;
            case 2:
                checkedProvince = messageEvent.getMessage();
                break;
            case 3:
                checkedCity = messageEvent.getMessage();
                tvLocation.setText(checkedProvince + " " + checkedCity);
                break;
            case 4:
                tvDate.setText(messageEvent.getMessage());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pickerUntil.removeHandler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.photograph:
                //"点击了照相";
                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE);
                } else {
                    try {
                        //有权限,去打开摄像头
                        takePhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                break;
            case R.id.photo:
                //"点击了相册";
                //  6.0之后动态申请权限 SD卡写入权限
                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE2);

                } else {
                    //打开相册
                    choosePhoto();
                }
                dialog.dismiss();
                break;
            case R.id.cancel:
                dialog.dismiss();//关闭对话框
                break;
            default:break;
        }
    }

    //调用照相机
    private void takePhoto() throws IOException {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = createFileIfNeed("headImg.png");
        if (Build.VERSION.SDK_INT >= 24) {
            cameraPhotoUri = FileProvider.getUriForFile(this.getApplicationContext(), "com.hwm.together.fileprovider", tempFile);
        } else {
            cameraPhotoUri = Uri.fromFile(tempFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_RESULT_CODE);
    }

    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed(String fileName) throws IOException {
        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/nbinpic";
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 打开相册
     */
    private void choosePhoto() {
        //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PHOTO_RESULT_CODE);
    }

    /**
     * 申请权限回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_ADD_CASE_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this,"拒绝了你的请求",Toast.LENGTH_SHORT).show();
                //"权限拒绝");
                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
            }
        }


        if (requestCode == MY_ADD_CASE_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                //"权限拒绝");
                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * startActivityForResult执行后的回调方法，接收返回的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_RESULT_CODE:
                //没有拍
                if (resultCode == 0){
                    return;
                }
                if (cameraPhotoUri != null) {
                    CommonUtil.cropPic(PersonalInfoActivity.this,cameraPhotoUri);
                }
                break;
            case PHOTO_RESULT_CODE:
                if(data != null)
                    CommonUtil.cropPic(PersonalInfoActivity.this,data.getData());
                break;
            case CROP_RESULT_CODE:
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", true); 处理方案如下
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        // 把裁剪后的图片保存至本地 返回路径
                        String urlpath = FileUtilcll.saveFile(this, "crop.jpg", bitmap);
                        MyLogUtil.d(TAG,"裁剪图片地址->" + urlpath);
                        cropPhotoPath = urlpath;
                        GlideUtil.loadCircleImg(PersonalInfoActivity.this,urlpath,headImg);
                        cameraPhotoUri = null;
                    }
                }
                break;
        }
    }

}