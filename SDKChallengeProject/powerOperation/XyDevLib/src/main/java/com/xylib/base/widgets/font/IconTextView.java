package com.xylib.base.widgets.font;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xylib.base.utils.Utils;

public class IconTextView extends TextView {
    public IconTextView(Context context) {
        super(context);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
//        Typeface typeface = FontManager.getInstance().getIconTypeface();
//        if (typeface == null)
//            return;
//        setTypeface(typeface);
        //设置字体图标
        Typeface font = Typeface.createFromAsset(Utils.getApp().getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }


}
