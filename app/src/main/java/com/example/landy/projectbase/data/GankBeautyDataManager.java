package com.example.landy.projectbase.data;

import android.content.Context;

import com.example.landy.projectbase.GankBeautyResultToZhuangbiImageMapper;
import com.example.landy.projectbase.data.datamanager.PaginatedDataManager;
import com.example.landy.projectbase.data.model.ZhuangBiImage;
import com.example.landy.projectbase.data.network.Network;

import java.util.List;

import rx.Observable;

/**
 * Created by landy on 16/10/16.
 */

public abstract class GankBeautyDataManager extends PaginatedDataManager<List<ZhuangBiImage>> {

    private static final int LIMIT = 10;
    public GankBeautyDataManager(Context context) {
        super(context);
    }

    @Override
    protected Observable<List<ZhuangBiImage>> getDataInner(int page) {
        return Network.getGankApi().getBeauties(LIMIT, page)
                .map(GankBeautyResultToZhuangbiImageMapper.getINSTANCE());
    }

    @Override
    public void checkIfHasMoreData(List<ZhuangBiImage> newData) {
        moreDataAvailable = newData.size() >= LIMIT;
    }
}
