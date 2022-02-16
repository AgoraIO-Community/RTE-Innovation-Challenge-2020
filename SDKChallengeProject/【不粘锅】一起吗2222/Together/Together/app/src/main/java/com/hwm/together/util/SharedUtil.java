package com.hwm.together.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwm.together.MyApplication;

public class SharedUtil {
    private static final String TAG = "SharedUtil";

    /**
     * 保存
     * @param token
     */
    public static void saveToken(String token){
        try{
            SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token",token);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取
     * @return
     */
    public static String getToken(){
        String token = "";
        try{
            SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
            token = sharedPreferences.getString("token","");
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

}
