package com.example.landy.projectbase.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.landy.projectbase.utils.StatusAndToolBarHelper;

public class StatusBarHolderView extends View {
    private boolean isTranslucent = true, isColorDrawable;

    public StatusBarHolderView(Context context) {
        this(context, null);
    }

    public StatusBarHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(StatusAndToolBarHelper.getStatusBarHeight(getContext()), MeasureSpec.getMode(heightMeasureSpec)));
    }

    public void setStatusBarTranslucent(boolean isTranslucent) {
        this.isTranslucent = isTranslucent;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        isColorDrawable = background instanceof ColorDrawable;
        if (isColorDrawable && !isTranslucent) {
            int color = ((ColorDrawable) background).getColor();
            background = new ColorDrawable(getColor700from500(color));
        }
        super.setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    public void setBackgroundResource(int resid) {
        setBackgroundDrawable(getContext().getResources().getDrawable(resid));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isColorDrawable && !isTranslucent) {
            canvas.drawARGB(33, 0, 0, 0);
        }
    }

    private int getColor700from500(int color) {
        float[] result = new float[3];
        Color.colorToHSV(color, result);
        result[2]  = result[2] * (color == Color.WHITE ? 0.8f : 0.85f);
        return Color.HSVToColor(result);
    }
}
