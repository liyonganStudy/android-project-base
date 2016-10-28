package com.example.landy.projectbase.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by landy on 16/10/15.
 */

public class ImageLoader {

    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .transform(new CircleTransform(context))
                .into(imageView);
    }
}
