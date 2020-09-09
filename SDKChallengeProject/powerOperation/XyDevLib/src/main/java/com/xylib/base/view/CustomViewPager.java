package com.xylib.base.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ProjectName: lituan_android
 * CreateDate: 2019-06-15
 * ClassName: CustomViewPager
 * Author: xiaoyangyan
 * note
 */
public class CustomViewPager extends ViewPager {
    float x, y;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                return super.onInterceptHoverEvent(event);
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - event.getX()) > Math.abs(y - event.getY()))
                    return true;
                else return false;
            case MotionEvent.ACTION_UP:
                return super.onInterceptHoverEvent(event);
        }
        return super.onInterceptHoverEvent(event);

    }
}
