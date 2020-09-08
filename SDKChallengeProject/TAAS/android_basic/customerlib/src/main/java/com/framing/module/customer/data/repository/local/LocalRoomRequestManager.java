package com.framing.module.customer.data.repository.local;

import android.content.Context;

// 仓库角色 本地/DB
public class LocalRoomRequestManager implements ILocalRequest,IDatabaseRequest {

    // TODO 可扩展 ...
    /** TODO ********************** 下面这一系列都是 本地相关的 ************************/

    private static LocalRoomRequestManager INSTANCE;

    private LocalRoomRequestManager(Context context) {
    }

    public static LocalRoomRequestManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalRoomRequestManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalRoomRequestManager(context);
                }
            }
        }
        return INSTANCE;
    }
}
