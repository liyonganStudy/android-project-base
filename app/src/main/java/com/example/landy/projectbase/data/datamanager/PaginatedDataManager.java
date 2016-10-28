package com.example.landy.projectbase.data.datamanager;

import android.content.Context;

import rx.Observable;

public abstract class PaginatedDataManager<T> extends BaseDataManager<T> {

    // state
    private int page = 0;
    protected boolean moreDataAvailable = true;

    public PaginatedDataManager(Context context) {
        super(context);
    }

    public boolean isFirstLoad() {
        return page <= 1;
    }

    public void clear() {
        page = 0;
        moreDataAvailable = true;
        resetLoadingCount();
    }

    @Override
    protected Observable<T> getDataInner() {
        log("getDataInner, page = " + page);
        if (!moreDataAvailable) {
            log("no more data");
            return null;
        }
        return getDataInner(++page);
    }

    @Override
    protected void onNextInner(T t) {
        super.onNextInner(t);
        checkIfHasMoreData(t);
    }

    protected abstract Observable<T> getDataInner(int page);

    public abstract void checkIfHasMoreData(T t);
}
