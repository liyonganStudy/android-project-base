package com.example.landy.projectbase.data.datamanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Base class for loading data; extending types are responsible for providing implementations of
 * {@link #onDataLoaded(Object)} to do something with the data and {@link #cancelLoading()} to
 * cancel any activity.
 */
public abstract class BaseDataManager<T> implements DataLoadingSubject {

    private final AtomicInteger loadingCount;
    private List<DataLoadingCallbacks> loadingCallbacks;
    protected Subscription subscription;
    protected Observer<T> observer = new Observer<T>() {
        @Override
        public void onCompleted() {
            onCompletedInner();
            loadFinished();
            log("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            onErrorInner(e);
            loadFinished();
            log("onFinished");
        }

        @Override
        public void onNext(T t) {
            onNextInner(t);
            onDataLoaded(t);
            loadFinished(); // TODO: 16/10/20
            log("onNext");
        }
    };

    public BaseDataManager(@NonNull Context context) {
        loadingCount = new AtomicInteger(0);
    }

    public abstract void onDataLoaded(T data);

    /**
     * 获取Observable数据或者变换后的数据
     * 不用处理线程切换，交由loadData方法处理线程切换
     * @return
     */
    protected abstract Observable<T> getDataInner();

    protected void onNextInner(T t) {};

    protected void onCompletedInner() {}

    protected void onErrorInner(Throwable e) {}

    /**
     * 给外部调用的接口，开始加载数据
     * @return
     */
    public Subscription loadData() {
        return loadData(true);
    }

    public Subscription loadData(boolean dispatchStartCallback) {
        loadStarted(dispatchStartCallback);
        Observable<T> observable = getDataInner();
        if (observable == null) {
            log("observable == null when loadData");
            return null;
        }
        subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return subscription;
    }

    public void cancelLoading() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }

    @Override
    public void registerCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks == null) {
            loadingCallbacks = new ArrayList<>(1);
        }
        loadingCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks != null && loadingCallbacks.contains(callback)) {
            loadingCallbacks.remove(callback);
        }
    }

    protected void loadStarted(boolean dispatch) {
        if (0 == loadingCount.getAndIncrement()) {
            if (dispatch) {
                dispatchLoadingStartedCallbacks();
            }
        }
    }

    private void loadFinished() {
        if (0 == loadingCount.decrementAndGet()) {
            dispatchLoadingFinishedCallbacks();
        }
    }

    protected void resetLoadingCount() {
        loadingCount.set(0);
    }

    protected void dispatchLoadingStartedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) {
            return;
        }
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataStartedLoading();
        }
    }

    protected void dispatchLoadingFinishedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) {
            return;
        }
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataFinishedLoading();
        }
    }

    protected void log(String log) {
        Log.i("DataManager", ">>>>>>>>>>" + log);
    }

}
