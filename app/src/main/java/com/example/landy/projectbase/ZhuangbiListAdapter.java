package com.example.landy.projectbase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.landy.projectbase.data.datamanager.DataLoadingSubject;
import com.example.landy.projectbase.data.model.ZhuangBiImage;
import com.example.landy.projectbase.imageloader.ImageLoader;
import com.example.landy.projectbase.widget.recycleview.BaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by landy on 16/10/16.
 */

public class ZhuangbiListAdapter extends BaseAdapter<ZhuangBiImage> {

    public ZhuangbiListAdapter(DataLoadingSubject dataLoadingSubject) {
        super(dataLoadingSubject);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        ZhuangBiImage zhuangBiImage = getDataItemAt(position);
        if (zhuangBiImage != null) {
            imageViewHolder.description.setText(zhuangBiImage.description);
            ImageLoader.loadImage(holder.itemView.getContext(), imageViewHolder.imageView, zhuangBiImage.image_url);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ImageViewHolder(view);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.itemImage) ImageView imageView;
        @Bind(R.id.description) TextView description;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
