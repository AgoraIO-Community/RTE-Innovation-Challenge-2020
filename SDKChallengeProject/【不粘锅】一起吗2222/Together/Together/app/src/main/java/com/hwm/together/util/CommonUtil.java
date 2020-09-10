package com.hwm.together.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CommonUtil {
    /**
     * 把yyyy-MM-dd转成yyyy格式
     * @param time
     * @return
     */
    public static int formatDate(String time, String format){
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2 =new SimpleDateFormat(format);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parseInt(sfstr);
    }

    /**
     * String转int
     *
     * @param data
     * @return
     */
    public static int parseInt(String data) {
        try {
            if(TextUtils.isEmpty(data)){
                return 0;
            }
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //裁剪图片
    public static void cropPic(Activity mActivity,Uri data) {
        if (data == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(data, "image/*");

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true");
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("scale", true);
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        cropIntent.putExtra("return-data", true);
        // 当 return-data 为 false 的时候需要设置这句
//        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
        // 图片输出格式
//        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
//        cropIntent.putExtra("noFaceDetection", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mActivity.startActivityForResult(cropIntent, 3);//CROP_RESULT_CODE
    }
}
