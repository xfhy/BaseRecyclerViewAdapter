package com.xfhy.baserecyclerviewadapterhelper;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xfhy.basequickadapter.BaseQuickAdapter;
import com.xfhy.baserecyclerviewadapterhelper.adapter.PullToRefreshAdapter;
import com.xfhy.baserecyclerviewadapterhelper.data.DataServer;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends AppCompatActivity implements BaseQuickAdapter
        .RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private PullToRefreshAdapter pullToRefreshAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pull);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

    @Override
    public void onLoadMoreRequested() {
        Log.e("xfhy", "onLoadMoreRequested");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshAdapter.addData(DataServer.getSampleData(6));
                    }
                });

            }
        }).start();


        pullToRefreshAdapter.loadMoreFail();

    }

    private void initAdapter() {
        pullToRefreshAdapter = new PullToRefreshAdapter();
        // 设置加载更多监听器
        pullToRefreshAdapter.setOnLoadMoreListener(this, mRecyclerView);
        //pullToRefreshAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(pullToRefreshAdapter);
        //pullToRefreshAdapter.disableLoadMoreIfNotFullPage();
        pullToRefreshAdapter.setEnableLoadMore(true);

    }


}
