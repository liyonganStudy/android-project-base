package com.example.landy.projectbase.utils;

import android.util.TypedValue;

import com.example.landy.projectbase.MyApplication;

/**
 * Created by landy on 16/10/25.
 */

public class MeasureUtil {

    public static int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, MyApplication.getInstance().getResources().getDisplayMetrics());
    }
}
