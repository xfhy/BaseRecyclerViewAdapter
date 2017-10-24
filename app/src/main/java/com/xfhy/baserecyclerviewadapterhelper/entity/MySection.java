package com.xfhy.baserecyclerviewadapterhelper.entity;

import com.xfhy.basequickadapter.entity.SectionEntity;

/**
 * author feiyang
 * create at 2017/10/24 13:49
 * description：
 */
public class MySection extends SectionEntity {

    private String itemContent;

    public MySection(boolean isHeader) {
        super(isHeader);
    }

    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
}
