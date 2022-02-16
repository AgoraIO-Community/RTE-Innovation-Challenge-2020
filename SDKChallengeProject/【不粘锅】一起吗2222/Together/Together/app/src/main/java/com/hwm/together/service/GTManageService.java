package com.hwm.together.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hwm.together.MyApplication;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.ToastUtil;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GTManageService extends GTIntentService {
    private static final String TAG = "GTManageService";

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String s) {
        Log.i(TAG,"cid: " + s);
        if (MyApplication.mUserInfo != null) {
            sendClientId(s);
        }
    }

    // 处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
//        ToastUtil.makeShort(context,"透传消息-收到好友请求" + gtTransmitMessage);
        EventBus.getDefault().post(new MessageEvent(2));
        Log.i(TAG, "onReceiveMessageData: 透传消息");
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Log.i(TAG, "onReceiveOnlineState: 离线上线通知");
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.i(TAG, "onReceiveCommandResult: 各种事件处理回执");
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.i(TAG, "onNotificationMessageArrived: 通知到达，只有个推通道下发的通知会回调此方法");
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.i(TAG, "onNotificationMessageClicked: 通知点击，只有个推通道下发的通知会回调此方法");
    }

    private void sendClientId(String clientId){
        MyApplication.mUserInfo.setClientId(clientId);
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().putUpdatePersonInfo(MyApplication.mUserInfo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    MyLogUtil.i(TAG,"更新ClientId成功");
                }else {
                    MyLogUtil.i(TAG,code + message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                MyLogUtil.i(TAG,"更新ClientId失败");
                t.printStackTrace();
            }
        });
    }
}
