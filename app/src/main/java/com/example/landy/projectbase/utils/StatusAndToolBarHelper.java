package com.example.landy.projectbase.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.landy.projectbase.R;
import com.example.landy.projectbase.widget.FitSystemWindowHackFrameLayout;
import com.example.landy.projectbase.widget.StatusBarHolderView;

import static com.example.landy.projectbase.R.id.statusBar;

/**
 * Created by landy on 16/10/22.
 */

public class StatusAndToolBarHelper {

    private StatusBarHolderView statusBarHolderView;
    private Toolbar toolbar;

    private boolean hadHackFitSystemWindow;
    private Activity activity;
    private boolean needForceHackFitSystemWindow;
    private Boolean hasEditView = null;
    private boolean immersive;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public StatusAndToolBarHelper(Activity activity, boolean immersive, boolean inDrawerLayout) {
        this.activity = activity;
        this.immersive = immersive;

        transparentSystemStatusBar(true);
        statusBarHolderView = new StatusBarHolderView(activity);
        statusBarHolderView.setId(statusBar);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.addView(statusBarHolderView);

        if (!immersive && !inDrawerLayout) {
            View contentViewLocal = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            int paddingTop = contentViewLocal.getPaddingTop() + getStatusBarHeight(activity);
            contentViewLocal.setPadding(contentViewLocal.getPaddingLeft(), paddingTop, contentViewLocal.getPaddingRight(), contentViewLocal.getPaddingBottom());
            setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }

    public void addToolbar(ViewGroup toolbarContainer, Toolbar toolbar) {
        if (toolbarContainer instanceof LinearLayout) {
            toolbarContainer.addView(toolbar, 0);
        } else {
            int childCount = toolbarContainer.getChildCount();
            toolbarContainer.addView(toolbar, childCount);
            if (immersive) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
                params.topMargin = getStatusBarHeight(activity);
                toolbar.setLayoutParams(params);
            } else {
                if (toolbarContainer instanceof RelativeLayout) {
                    ((RelativeLayout.LayoutParams) toolbarContainer.getChildAt(0).getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.toolbar);
                }
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getToolbarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(R.dimen.toolbarHeight);
    }

    public static int getToolbarStatusBarHeight(Context context) {
        return getStatusBarHeight(context) + getToolbarHeight(context);
    }

    public void setStatusBarColor(@ColorInt int color) {
        statusBarHolderView.setBackgroundColor(color);
    }

    public void setNeedForceHackFitSystemWindow(boolean need) {
        needForceHackFitSystemWindow = need;
    }

    public void transparentSystemStatusBar(boolean enable) {
        boolean needHackFitSystemWindow = false;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            if (enable) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            needHackFitSystemWindow = true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enable) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            needHackFitSystemWindow = true;
        }
        if (needHackFitSystemWindow && !hadHackFitSystemWindow) {
            ViewGroup rootView = (ViewGroup) window.getDecorView().findViewById(android.R.id.content);
            if (hasEditTextView(rootView) || needForceHackFitSystemWindow) {
                hadHackFitSystemWindow = true;
                View childView = rootView.getChildAt(0);
                rootView.removeView(childView);
                FitSystemWindowHackFrameLayout wrapView = new FitSystemWindowHackFrameLayout(activity);
                wrapView.addView(childView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                rootView.addView(wrapView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    private boolean hasEditTextView(ViewGroup viewGroup) {
        if (hasEditView == null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    boolean ret = hasEditTextView((ViewGroup) viewGroup.getChildAt(i));
                    if (ret) {
                        hasEditView = true;
                    }
                } else {
                    boolean ret = viewGroup.getChildAt(i) instanceof EditText;
                    if (ret) {
                        hasEditView = true;
                    }
                }
            }
            hasEditView = false;
        }
        return hasEditView;
    }
}
