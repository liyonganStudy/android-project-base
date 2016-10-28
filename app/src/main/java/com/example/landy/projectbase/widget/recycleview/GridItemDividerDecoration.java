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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * A {@link RecyclerView.ItemDecoration} which draws dividers (along the right & bottom)
 * for certain {@link RecyclerView.ViewHolder} types.
 */
public class GridItemDividerDecoration extends RecyclerView.ItemDecoration {

    private final Class[] dividedClasses;
    private final int dividerSize;
    private final Paint paint;

    public GridItemDividerDecoration(Class[] dividedClasses, int dividerSize, @ColorInt int dividerColor) {
        this.dividedClasses = dividedClasses;
        this.dividerSize = dividerSize;
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public GridItemDividerDecoration(@NonNull Context context, @DimenRes int dividerSizeResId, @ColorRes int dividerColorResId) {
        this(null, context, dividerSizeResId, dividerColorResId);
    }

    public GridItemDividerDecoration(Class[] dividedClasses, @NonNull Context context,
                                     @DimenRes int dividerSizeResId,
                                     @ColorRes int dividerColorResId) {
        this(dividedClasses, context.getResources().getDimensionPixelSize(dividerSizeResId),
                ContextCompat.getColor(context, dividerColorResId));
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.isAnimating()) return;

        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        int spanCount = getSpanCount(parent);
        int itemCount = parent.getAdapter().getItemCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int itemPosition = lm.getPosition(child);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);
            if (requiresDivider(viewHolder)) {
                final int right = lm.getDecoratedRight(child);
                final int bottom = lm.getDecoratedBottom(child);
                // draw the bottom divider
                if (!isLastRow(itemPosition, itemCount, spanCount)) {
                    canvas.drawRect(lm.getDecoratedLeft(child), bottom - dividerSize, right, bottom, paint);
                }
                // draw the right edge divider
                if (!isLastColumn(itemPosition, spanCount)) {
                    canvas.drawRect(right - dividerSize, lm.getDecoratedTop(child), right, bottom - dividerSize, paint);
                }
            }
        }
    }

    /**
     * onDrawOver只是在上面绘制分割线，如果需要实际分割每个item的话，需要实现getItemOffsets方法。
     * @param viewHolder
     * @return
     */
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//    }

    private boolean requiresDivider(RecyclerView.ViewHolder viewHolder) {
        if (dividedClasses == null) {
            return true;
        }
        for (int i = 0; i < dividedClasses.length; i++) {
            if (dividedClasses[i].isInstance(viewHolder))
                return true;
        }
        return false;
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRow(int position, int childCount, int spanCount) {
        return (childCount - position) <= spanCount;
    }

    private boolean isLastColumn(int pos, int spanCount) {
        return (pos + 1) % spanCount == 0;
    }

}
