package com.example.landy.projectbase.activity;

import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.landy.projectbase.R;
import com.example.landy.projectbase.utils.MeasureUtil;
import com.example.landy.projectbase.utils.StatusAndToolBarHelper;

import rx.Subscription;

/**
 * Created by landy on 16/10/23.
 */

public class BaseActivity extends AppCompatActivity {

    protected Subscription subscription;
    protected StatusAndToolBarHelper statusAndToolBarHelper;
    protected Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initBarHelper();
    }

    protected void initBarHelper() {
        statusAndToolBarHelper = new StatusAndToolBarHelper(this, needImmersive(), this instanceof BaseDrawerActivity);
        if (needToolbar()) {
            ViewGroup toolbarContainer = getToolbarContainer();
            statusAndToolBarHelper.addToolbar(toolbarContainer, initToolbar(toolbarContainer));
            setSupportActionBar(toolbar);
        }
    }

    protected void setContentViewWithOutInject(int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected ViewGroup getToolbarContainer() {
        return (ViewGroup) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
    }

    protected Toolbar initToolbar(ViewGroup toolbarContainer) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, toolbarContainer, false);
        }
        return toolbar;
    }

    protected boolean needToolbarUpIcon() {
        return true;
    }

    protected boolean needImmersive() {
        return false;
    }

    protected boolean needToolbar() {
        return true;
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (needToolbarUpIcon()) {
            setToolbarBackIcon();
        }
    }

    protected void setToolbarBackIcon() {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setContentInsetStartWithNavigation(MeasureUtil.dip2px(56));
        toolbar.setNavigationIcon(R.mipmap.actionbar_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onIconClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        checkToolbar();
        toolbar.setTitle(title);
    }

    public void setStatusBarColor(@ColorInt int color) {
        statusAndToolBarHelper.setStatusBarColor(color);
    }

    protected void onIconClick() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void checkToolbar() {
        if (toolbar == null) {
            throw new IllegalStateException("toolbar is null");
        }
    }
}
