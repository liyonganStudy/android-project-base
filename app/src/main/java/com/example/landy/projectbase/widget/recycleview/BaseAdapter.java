package com.example.landy.projectbase.widget.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.landy.projectbase.R;
import com.example.landy.projectbase.data.datamanager.DataLoadingSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by landy on 16/10/12.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DataLoadingSubject.DataLoadingCallbacks {

    public static final int VIEW_TYPE_LOADING_MORE = 0;
    public static final int VIEW_TYPE_EMPTY = 1;
    public static final int VIEW_TYPE_NORMAL = 100;

    private List<T> dataItems;
    private DataLoadingSubject dataLoadingSubject;
    private boolean showLoadingMore = false;

    private boolean hasEmptyView;
    private CharSequence emptyContent;
    private View.OnClickListener emptyClickListener;
    private boolean showEmptyView;

    public BaseAdapter(DataLoadingSubject dataLoadingSubject) {
        this.dataLoadingSubject = dataLoadingSubject;
        this.dataLoadingSubject.registerCallback(this);
        dataItems = new ArrayList<>();
    }

    public void setDataItems(List<T> items) {
        dataItems.clear();
        dataItems.addAll(items);
        log("setDataItems, size = " + items.size());
        notifyDataSetChanged();
    }

    public void addDataItems(List<T> items) {
        int insertPosition = dataItems.size();
        dataItems.addAll(items);
        log("addDataItems, size = " + items.size());
        notifyItemRangeInserted(insertPosition, items.size());
    }

    public T getDataItemAt(int position) {
        return dataItems.get(position);
    }

    public List<T> getDataItems() {
        return dataItems;
    }

    public void showEmptyView(CharSequence content, View.OnClickListener onClickListener) {
        showEmptyView = true;
        emptyContent = content;
        emptyClickListener = onClickListener;
        if (!hasEmptyView) {
            hasEmptyView = true;
            notifyItemInserted(getEmptyViewAdapterPosition());
        } else {
            notifyItemChanged(getEmptyViewAdapterPosition());
        }
    }

    public void hideEmptyView() {
        if (hasEmptyView) {
            showEmptyView = false;
            notifyItemChanged(getEmptyViewAdapterPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LOADING_MORE:
                return createLoadingMoreViewHolder(parent);
            case VIEW_TYPE_EMPTY:
                return createEmptyViewHolder(parent);
            case VIEW_TYPE_NORMAL:
                return onCreateNormalViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case VIEW_TYPE_LOADING_MORE:
                bindLoadingMoreViewHolder((LoadingMoreHolder) holder);
                log("onBindViewHolder " + "VIEW_TYPE_LOADING_MORE + position at " + position);
                break;
            case VIEW_TYPE_EMPTY:
                bindEmptyViewHolder((EmptyViewHolder) holder);
                log("onBindViewHolder " + "VIEW_TYPE_EMPTY + position at " + position);
                break;
            case VIEW_TYPE_NORMAL:
                onBindNormalViewHolder(holder, position);
                log("onBindViewHolder " + "VIEW_TYPE_NORMAL + position at " + position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getDataItemsCount() + (showLoadingMore ? 1 : 0) + (hasEmptyView && showEmptyView ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemsCount = getDataItemsCount();
        if (position < dataItemsCount && dataItemsCount > 0) {
            return getNormalItemViewType(position);
        } if (showEmptyView && position == dataItemsCount) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_LOADING_MORE;
        }
    }

    @Override
    public void dataStartedLoading() {
        if (showLoadingMore) {
            return;
        }
        hideEmptyView();
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void dataFinishedLoading() {
        if (!showLoadingMore) {
            return;
        }
        int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }

    public abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    protected int getNormalItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    private int getEmptyViewAdapterPosition() {
        return dataItems.size();
    }

    public int getDataItemsCount() {
        return dataItems.size();
    }

    private LoadingMoreHolder createLoadingMoreViewHolder(ViewGroup parent) {
        return new LoadingMoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.infinite_loading, parent, false));
    }

    private void bindLoadingMoreViewHolder(LoadingMoreHolder holder) {
        if (dataLoadingSubject.isDataLoading()) {
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    private EmptyViewHolder createEmptyViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        RelativeLayout loadItemView = new RelativeLayout(context);
        loadItemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        EmptyView emptyView = new EmptyView(context);
        loadItemView.addView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new EmptyViewHolder(loadItemView);
    }

    private void bindEmptyViewHolder(EmptyViewHolder holder) {
        if (showEmptyView) {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(emptyContent);
            holder.content.setOnClickListener(emptyClickListener);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        TextView content;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            content = (TextView) ((ViewGroup) itemView).getChildAt(0);
        }
    }

    private class LoadingMoreHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }

    private class EmptyView extends TextView {
         public EmptyView(Context context) {
             super(context);
             int padding = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()) + 0.5);
             setPadding(0, padding, 0, padding);
             setGravity(Gravity.CENTER);
         }
    }

    protected void log(String log) {
        Log.i("RecycleViewAdapter", ">>>>>>>>>" + log);
    }
}
