package com.example.landy.projectbase.widget.recycleview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.landy.projectbase.data.datamanager.BaseDataManager;
import com.example.landy.projectbase.data.datamanager.DataLoadingSubject;

/**
 * Created by landy on 16/10/20.
 */

public class BaseRecyclerView extends RecyclerView {

    private BaseAdapter adapter;

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void enableLoadingMore(final BaseDataManager dataManager) {
        if (getLayoutManager() == null) {
            throw new IllegalStateException("enableLoadingMore must after setLayoutManager");
        }
        addOnScrollListener(new InfiniteScrollListener(getLayoutManager(), dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadData();
            }
        });
    }

    public void enableRefreshingData(final BaseDataManager dataManager, @Nullable final SwipeRefreshLayout swipeRefreshLayout) {
        if (getLayoutManager() == null || swipeRefreshLayout == null) {
            throw new IllegalStateException("enableLoadingMore must after setLayoutManager");
        }
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!dataManager.isDataLoading()) {
                    dataManager.loadData(false);
                    if (adapter == null) {
                        throw new IllegalStateException("swipe refresh has no adapter");
                    }
                    adapter.hideEmptyView();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        dataManager.registerCallback(new DataLoadingSubject.DataLoadingCallbacks() {
            @Override
            public void dataStartedLoading() {
            }

            @Override
            public void dataFinishedLoading() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            delegateSpanSizeLookup(layoutManager, adapter);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        Adapter adapter = getAdapter();
        if (adapter != null) {
            if (!(adapter instanceof BaseAdapter)) {
                throw new IllegalStateException("must use BaseAdapter");
            }
            delegateSpanSizeLookup(layout, (BaseAdapter) adapter);
        }
        super.setLayoutManager(layout);
    }

    private void delegateSpanSizeLookup(LayoutManager layoutManager, BaseAdapter adapter) {
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager localLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            localLayoutManager.setSpanSizeLookup(new DelegateSpanSizeLookup(spanSizeLookup, localLayoutManager.getSpanCount(), adapter));
        }
    }

    private class DelegateSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private GridLayoutManager.SpanSizeLookup spanSizeLookup;
        private int spanCount;
        private BaseAdapter adapter;

        public DelegateSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup, int spanCount, BaseAdapter adapter) {
            this.spanSizeLookup = spanSizeLookup;
            this.spanCount = spanCount;
            this.adapter = adapter;
        }


        @Override
        public int getSpanSize(int position) {
            return isSpecialType(position) ? spanCount: spanSizeLookup.getSpanSize(position);
        }

        @Override
        public int getSpanIndex(int position, int spanCount) {
            return isSpecialType(position) ? 0 : spanSizeLookup.getSpanIndex(position, spanCount);
        }

        @Override
        public int getSpanGroupIndex(int adapterPosition, int spanCount) {
            return isSpecialType(adapterPosition) ? super.getSpanGroupIndex(adapterPosition, spanCount) : spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount);
        }

        private boolean isSpecialType(int position) {
            int viewType = adapter.getItemViewType(position);
            return viewType == BaseAdapter.VIEW_TYPE_EMPTY || viewType == BaseAdapter.VIEW_TYPE_LOADING_MORE;
        }
    }
}
