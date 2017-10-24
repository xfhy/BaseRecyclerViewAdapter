package com.xfhy.baserecyclerviewadapterhelper.adapter;

import com.xfhy.basequickadapter.BaseSectionQuickAdapter;
import com.xfhy.basequickadapter.BaseViewHolder;
import com.xfhy.baserecyclerviewadapterhelper.R;
import com.xfhy.baserecyclerviewadapterhelper.entity.MySection;

import java.util.List;

/**
 * author feiyang
 * create at 2017/10/24 13:48
 * description：
 */
public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    public SectionAdapter(int layoutResId, int sectionHeadResId, List<MySection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.header, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, MySection item) {
        int layoutPosition = holder.getLayoutPosition();
        switch (layoutPosition % 3) {
            case 0:
                holder.setBackgroundColor(R.id.tv_list, 0xffEE82EE);
                break;
            case 1:
                holder.setBackgroundColor(R.id.tv_list, 0xffFFCE87);
                break;
            case 2:
                holder.setBackgroundColor(R.id.tv_list, 0xff85E6EE);
                break;
        }
        holder.setText(R.id.tv_list, item.getItemContent());
    }
}
