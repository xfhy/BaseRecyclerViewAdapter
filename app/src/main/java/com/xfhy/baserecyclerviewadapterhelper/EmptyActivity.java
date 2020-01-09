package com.xfhy.baserecyclerviewadapterhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.xfhy.basequickadapter.BaseQuickAdapter;
import com.xfhy.basequickadapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class EmptyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // （可选）如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        final MyAdapter myAdapter = new MyAdapter(R.layout.item_list, getData());
        myAdapter.bindToRecyclerView(mRecyclerView);
        //myAdapter.setEmptyView(R.layout.layout_empty_view);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                myAdapter.clearData();
                myAdapter.setEmptyView(R.layout.layout_empty_view);
            }
        });

        mRecyclerView.setAdapter(myAdapter);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        return list;
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, String item) {
            holder.setText(R.id.tv_list, item);
        }
    }

}
