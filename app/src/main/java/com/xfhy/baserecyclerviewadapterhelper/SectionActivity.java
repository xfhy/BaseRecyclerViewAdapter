package com.xfhy.baserecyclerviewadapterhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xfhy.basequickadapter.BaseQuickAdapter;
import com.xfhy.baserecyclerviewadapterhelper.adapter.SectionAdapter;
import com.xfhy.baserecyclerviewadapterhelper.entity.MySection;
import com.xfhy.baserecyclerviewadapterhelper.entity.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017年10月24日13:44:29
 * 分组布局
 */
public class SectionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MySection> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        //2列
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mData = getSampleData();
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_list, R.layout.def_section_head,
                mData);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader)
                    Toast.makeText(SectionActivity.this, mySection.header, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SectionActivity.this, mySection.getItemContent(), Toast.LENGTH_SHORT).show();
            }
        });
        /*sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SectionUseActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });*/
        mRecyclerView.setAdapter(sectionAdapter);
    }

    private List<MySection> getSampleData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "Section 1"));
        for(int i=0;i<5;i++){
            MySection mySection = new MySection(false);
            mySection.setItemContent("哈哈");
            list.add(mySection);
        }
        list.add(new MySection(true, "Section 2"));
        for(int i=0;i<4;i++){
            MySection mySection = new MySection(false);
            mySection.setItemContent("哈哈");
            list.add(mySection);
        }
        list.add(new MySection(true, "Section 3"));
        for(int i=0;i<3;i++){
            MySection mySection = new MySection(false);
            mySection.setItemContent("哈哈");
            list.add(mySection);
        }
        list.add(new MySection(true, "Section 4"));
        for(int i=0;i<2;i++){
            MySection mySection = new MySection(false);
            mySection.setItemContent("哈哈");
            list.add(mySection);
        }

        return list;
    }
}
