/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.landy.projectbase.widget.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.landy.projectbase.data.datamanager.DataLoadingSubject;

/**
 * A scroll listener for RecyclerView to load more items as you approach the end.
 *
 * Adapted from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    // The minimum number of items remaining before we should loading more.
    private static final int VISIBLE_THRESHOLD = 1;

    private final RecyclerView.LayoutManager layoutManager;
    private final DataLoadingSubject dataLoading;
    private int[] mVisibleItemPositions;

    public InfiniteScrollListener(@NonNull RecyclerView.LayoutManager layoutManager,
                                  @NonNull DataLoadingSubject dataLoading) {
        this.layoutManager = layoutManager;
        this.dataLoading = dataLoading;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // bail out if scrolling upward or already loading data
        if (dy < 0 || dataLoading.isDataLoading()) {
            return;
        }

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = totalItemCount;
        if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (mVisibleItemPositions == null) {
                mVisibleItemPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            staggeredGridLayoutManager.findFirstVisibleItemPositions(mVisibleItemPositions);
            for (int visibleItemPosition : mVisibleItemPositions) {
                if (visibleItemPosition < firstVisibleItemPosition) {
                    firstVisibleItemPosition = visibleItemPosition;
                }
            }
        }
        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + VISIBLE_THRESHOLD)) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

}
