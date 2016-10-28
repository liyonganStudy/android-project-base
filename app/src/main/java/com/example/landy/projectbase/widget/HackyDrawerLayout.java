package com.example.landy.projectbase.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyDrawerLayout extends DrawerLayout {
    public HackyDrawerLayout(Context context) {
        super(context);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean mIsDisallowIntercept = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    // https://code.google.com/p/android/issues/detail?id=60464
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            // the incorrect array size will only happen in the multi-touch scenario.
            if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
                requestDisallowInterceptTouchEvent(false);
                boolean handled = super.dispatchTouchEvent(ev);
                requestDisallowInterceptTouchEvent(true);
                return handled;
            } else {
                return super.dispatchTouchEvent(ev);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {//Caused by: java.lang.NullPointerException
        	return false;
        } catch (ExceptionInInitializerError e) {
            return false;
        }
    }
}
