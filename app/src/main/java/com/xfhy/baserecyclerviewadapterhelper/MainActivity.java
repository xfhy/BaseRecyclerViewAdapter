package com.xfhy.baserecyclerviewadapterhelper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xfhy.basequickadapter.BaseQuickAdapter;
import com.xfhy.basequickadapter.BaseViewHolder;
import com.xfhy.baserecyclerviewadapter.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mRv = (RecyclerView) findViewById(R.id.rv_test);
        mRv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation
                ()));
        MyAdapter adapter = new MyAdapter(this);
        mRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "长按事件   position:" + position, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MyAdapter(Context context) {
            super(context, R.layout.item_list, getData());
        }

        @Override
        protected void convert(BaseViewHolder holder, String item) {
            holder.setText(R.id.tv_list, item);
        }
    }

    private static List<String> getData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add(String.valueOf(i));
        }
        return dataList;
    }

}
