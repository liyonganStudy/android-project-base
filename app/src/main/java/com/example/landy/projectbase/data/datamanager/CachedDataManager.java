package com.example.landy.projectbase.data.datamanager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.landy.projectbase.MyApplication;
import com.example.landy.projectbase.data.model.ZhuangBiImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by landy on 16/10/20.
 */

public abstract class CachedDataManager<T> extends BaseDataManager<T> {

    private static String DATA_FILE_NAME = "data.db";
    private File dataFile = new File(MyApplication.getInstance().getFilesDir(), DATA_FILE_NAME);
    private Gson gson = new Gson();
    private BehaviorSubject<T> cache;

    public CachedDataManager(@NonNull Context context) {
        super(context);
    }

    public Subscription loadData() {
        return loadData(true);
    }

    public Subscription loadData(boolean dispatchStartCallback) {
        loadStarted(dispatchStartCallback);
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    T items = readItems();
                    if (items == null) {
                        loadFromNetwork();
                    } else {
                        subscriber.onNext(items);
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe(cache);
        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void refreshData() {
        cache = null;
        loadData(false);
    }

    public void loadFromNetwork() {
        getDataInner().subscribeOn(Schedulers.io())
                .doOnNext(new Action1<T>() {
                    @Override
                    public void call(T items) {
                        writeItems(items);
                    }
                })
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }, new Action0() {
                    @Override
                    public void call() {

                    }
                });
    }

    public T readItems() {
        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<ZhuangBiImage>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(T items) {
        String json = gson.toJson(items);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
