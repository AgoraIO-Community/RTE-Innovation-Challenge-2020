package com.hwm.together.util;

import android.view.View;

public interface OnItemClickCallback<T> {

    // 点击事件
    public void onClick(View view , T info);
    // 长按事件
    public void onLongClick(View view , T info);
}
