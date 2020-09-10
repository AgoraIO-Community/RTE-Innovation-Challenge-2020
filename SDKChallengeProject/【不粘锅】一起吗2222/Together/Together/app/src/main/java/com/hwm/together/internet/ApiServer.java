package com.hwm.together.internet;

import com.hwm.together.bean.FriendApplication;
import com.hwm.together.bean.FriendInfoBean;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.weather.WeatherDataBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServer {

    @GET("users/list")
    Call<ResponseBody> getUser();

    @GET("simpleWeather/query?")
    Call<WeatherDataBean> getWeather(@QueryMap Map<String, String> params);

    //注册
    @POST("userAccount/account/")
    Call<BaseResponse> postRegister(@Body UserInfo params);

    //登录
    @POST("userAccount/login")
    Call<BaseResponse> postLogin(@Body UserInfo params);

    //修改密码
    @PUT("userAccount/passwd")
    Call<BaseResponse> putResetPassword(@Body UserInfo params);

    //更新用户信息
    @PUT("userAccount/account")
    Call<BaseResponse> putUpdatePersonInfo(@Body UserInfo params);

    //获取用户信息
    @GET("userAccount/account")
    Call<UserInfoResponse> getUserInfo(@Query("account") String account);

    //上传头像
    @Multipart
    @POST("userAccount/img")
    Call<BaseResponse> postUploadImg(@Query("account")String account, @Part MultipartBody.Part img);

    //添加好友
    @POST("socialContact/friendApplication")
    Call<BaseResponse> postAddFriend(@Body FriendApplication friendApplication);

    //查询好友列表
    @GET("socialContact/userFriend")
    Call<GetFriendsListResponse> getFriendsList(@Query("userId") String userId);

    //查询申请列表
    @GET("socialContact/friendApplication")
    Call<GetApplyListResponse> getApplyList(@Query("applicantId") String applicantId);

    //修改申请状态 1同意，2，拒绝
    @PUT("socialContact/friendApplication")
    Call<BaseResponse> updateFriendStatus(@Body FriendApplication friendApplication);

    //更新好友信息--修改备注
    @PUT("socialContact/userFriend")
    Call<BaseResponse> updateFriendNoteName(@Body FriendInfoBean friendInfoBean);

//    @DELETE("socialContact/userFriend")
    //删除好友
    @HTTP(method = "DELETE", path = "socialContact/userFriend", hasBody = true)
    Call<BaseResponse> deleteFriend(@Body FriendInfoBean friendInfoBean);


}
