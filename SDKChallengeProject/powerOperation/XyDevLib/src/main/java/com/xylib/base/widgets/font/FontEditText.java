package com.xylib.base.widgets.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Zyj on 2016/8/22.
 */
public class FontEditText extends EditText {

    public FontEditText(Context context) {
        this(context, null);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = FontManager.getInstance().getNormalTypeface();
        if (typeface == null)
            return;
        setTypeface(typeface);
    }
}
