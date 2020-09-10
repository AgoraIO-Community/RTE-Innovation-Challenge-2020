package com.xiaoyang.poweroperation.account.session;


import com.xiaoyang.poweroperation.app.utils.SPUtils;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xylib.base.utils.GsonUtils;
import com.xylib.base.utils.StringUtil;


/**
 * Created by Zyj on 2016/12/29.
 */
public class UserSessionManager {

    private static UserSessionManager instance = new UserSessionManager();
    private User user;
    private boolean isRefreshToken;

    public static UserSessionManager getInstance() {
        return instance;
    }


    public void setRefreshToken(boolean isRefreshToken) {
        this.isRefreshToken = isRefreshToken;
    }

    public boolean isRefreshToken() {
        return isRefreshToken;
    }



    public void setUserInfo(User userInfo) {
        this.user = userInfo;
        SPUtils.getInstance().put("user", GsonUtils.toJson(userInfo));
    }


//    public String getToken() {
//        if (tempData != null) {
//            return tempData.getToken();
//        }
//        return SPUtils.getInstance().getString("token");
//    }

    public User getUser() {
        String user = SPUtils.getInstance().getString("user");
        if (StringUtil.isNotBlank(user)) {
            return GsonUtils.fromJson(user, User.class);
        }
        return null;
    }





    public void cleanSession() {
        cleanTempData();
        SPUtils.getInstance().clear();
    }


    public void cleanTempData() {

        if (user != null) {
            user = null;
        }
    }

    public void logout() {
        cleanSession();
    }
}
