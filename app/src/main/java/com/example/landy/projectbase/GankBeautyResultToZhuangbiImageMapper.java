package com.example.landy.projectbase;

import com.example.landy.projectbase.data.model.GankBeauty;
import com.example.landy.projectbase.data.model.GankBeautyResult;
import com.example.landy.projectbase.data.model.ZhuangBiImage;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by landy on 16/10/16.
 */

public class GankBeautyResultToZhuangbiImageMapper implements Func1<GankBeautyResult, List<ZhuangBiImage>>{
    private static GankBeautyResultToZhuangbiImageMapper INSTANCE;

    private GankBeautyResultToZhuangbiImageMapper() {

    }

    public static GankBeautyResultToZhuangbiImageMapper getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GankBeautyResultToZhuangbiImageMapper();
        }
        return INSTANCE;
    }

    @Override
    public List<ZhuangBiImage> call(GankBeautyResult gankBeautyResult) {
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;
        List<ZhuangBiImage> zhuangBiImages = new ArrayList<>(gankBeauties.size());

        for (GankBeauty gankBeauty : gankBeauties) {
            ZhuangBiImage zhuangBiImage = new ZhuangBiImage();
            zhuangBiImage.description = gankBeauty.createdAt;
            zhuangBiImage.image_url = gankBeauty.url;
            zhuangBiImages.add(zhuangBiImage);
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zhuangBiImages;
    }
}
