package com.example.landy.projectbase.data.network.api;

import com.example.landy.projectbase.data.model.ZhuangBiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by landy on 16/10/16.
 */

public interface ZhuangBiApi {
    @GET("search")
    Observable<List<ZhuangBiImage>> search(@Query("q") String query);
}
