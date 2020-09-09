package com.xylib.base.widgets.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.xylib.base.R;


/**
 * 全局统一的自定义font的textView
 */
public class FontTextView extends AppCompatTextView {

    // Style
    public static final int NORMAL = 0;
    public static final int LIGHT = 1;
    public static final int MEDIUM = 2;
    public static final int BOLD = 3;

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            int textStyle = typedArray.getInt(R.styleable.FontTextView_font_style, 0);
            initFont(textStyle);
        }
    }

    private void initFont(int textStyle) {
        switch (textStyle) {
            case NORMAL:
                Typeface typeface = FontManager.getInstance().getNormalTypeface();
                if (typeface == null)
                    return;
                setTypeface(typeface);
                break;
            case LIGHT:
                Typeface lightTypeface = FontManager.getInstance().getLightTypeface();
                if (lightTypeface == null)
                    return;
                setTypeface(lightTypeface);
                break;
            case MEDIUM:
                Typeface mediumTypeface = FontManager.getInstance().getMediumTypeface();
                if (mediumTypeface == null)
                    return;
                setTypeface(mediumTypeface);
                break;
            case BOLD:
                Typeface boldTypeface = FontManager.getInstance().getBoldTypeface();
                if (boldTypeface == null)
                    return;
                setTypeface(boldTypeface);
                break;
        }
    }
}
