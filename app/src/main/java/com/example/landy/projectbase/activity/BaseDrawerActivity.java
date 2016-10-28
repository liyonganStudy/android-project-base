package com.example.landy.projectbase.activity;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.landy.projectbase.R;
import com.example.landy.projectbase.utils.StatusAndToolBarHelper;

/**
 * Created by landy on 16/10/25.
 */

public class BaseDrawerActivity extends BaseActivity {

    private ViewGroup contentView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentViewWithOutInject(R.layout.activity_drawer);
        contentView = (ViewGroup) findViewById(R.id.realContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, contentView, true);
        initBarHelper();
        setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected Toolbar initToolbar(ViewGroup toolbarContainer) {
        Toolbar toolbar = super.initToolbar(toolbarContainer);
        toolbar.getLayoutParams().height = StatusAndToolBarHelper.getToolbarStatusBarHeight(this);
        toolbar.setPadding(0, StatusAndToolBarHelper.getStatusBarHeight(this), 0, 0);
        return toolbar;
    }

    @Override
    protected ViewGroup getToolbarContainer() {
        return contentView;
    }

}
