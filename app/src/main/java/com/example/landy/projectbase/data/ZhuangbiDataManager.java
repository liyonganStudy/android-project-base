package com.example.landy.projectbase.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.landy.projectbase.data.datamanager.CachedDataManager;
import com.example.landy.projectbase.data.model.ZhuangBiImage;
import com.example.landy.projectbase.data.network.Network;

import java.util.List;

import rx.Observable;

/**
 * Created by landy on 16/10/16.
 */

public abstract class ZhuangbiDataManager extends CachedDataManager<List<ZhuangBiImage>> {

    public ZhuangbiDataManager(@NonNull Context context) {
        super(context);
    }

    @Override
    protected Observable<List<ZhuangBiImage>> getDataInner() {
        return Network.getZhuangBiApi().search("装逼");
    }
}
