package com.hwm.together.util.navigation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NavigationViewPager extends ViewPager {
    /*
    private boolean isVertical = true;
    public NavigationViewPager(Context context) {
        super(context);
    }

    public NavigationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //    private MotionEvent swapTouchEvent(MotionEvent event) {
    //        float width = getWidth();
    //        float height = getHeight();
    //        event.setLocation((event.getY() / height) * width, (event.getX() / width) * height);
    //        return event;
    //    }
    //
    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent event) {
    //        return super.onInterceptTouchEvent(swapTouchEvent(MotionEvent.obtain(event)));
    //    }
    //
    //    @Override
    //    public boolean onTouchEvent(MotionEvent ev) {
    //        return super.onTouchEvent(swapTouchEvent(MotionEvent.obtain(ev)));
    //    }
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isVertical) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
            return intercepted;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isVertical) {
            return super.onTouchEvent(swapXY(ev));
        } else {
            return super.onTouchEvent(ev);
        }
    }
    */


    private boolean noScroll = false;

    public NavigationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
