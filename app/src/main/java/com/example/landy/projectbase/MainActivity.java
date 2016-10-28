package com.example.landy.projectbase;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.example.landy.projectbase.activity.BaseActivity;
import com.example.landy.projectbase.data.ZhuangbiDataManager;
import com.example.landy.projectbase.data.datamanager.BaseDataManager;
import com.example.landy.projectbase.data.model.ZhuangBiImage;
import com.example.landy.projectbase.widget.recycleview.BaseRecyclerView;
import com.example.landy.projectbase.widget.recycleview.GridItemDividerDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.recyclerView) BaseRecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private ZhuangbiListAdapter adapter;
    private BaseDataManager<List<ZhuangBiImage>> dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rxjava);
        ButterKnife.bind(this);

//        dataManager = new GankBeautyDataManager(MainActivity.this) {
//            @Override
//            public void onDataLoaded(List<ZhuangBiImage> data) {
//                if (dataManager.isFirstLoad()) {
//                    adapter.setDataItems(data);
//                } else {
//                    adapter.addDataItems(data);
//                }
//            }
//
//            @Override
//            protected void onErrorInner(Throwable e) {
//                super.onErrorInner(e);
//                if (adapter.getDataItemsCount() == 0) {
//                    adapter.showEmptyView("empty", null);
//                }
//            }
//        };
        dataManager = new ZhuangbiDataManager(MainActivity.this) {

            @Override
            public void onDataLoaded(List<ZhuangBiImage> data) {
                adapter.setDataItems(data);
            }

            @Override
            protected void onErrorInner(Throwable e) {
                super.onErrorInner(e);
                if (adapter.getDataItemsCount() == 0) {
                    adapter.showEmptyView("empty", null);
                }
            }
        };
        adapter = new ZhuangbiListAdapter(dataManager);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.enableLoadingMore(dataManager);
        recyclerView.enableRefreshingData(dataManager, swipeRefreshLayout);
        recyclerView.addItemDecoration(new GridItemDividerDecoration(this, R.dimen.dividerWidth, R.color.colorAccent));
        subscription = dataManager.loadData();
    }

}
