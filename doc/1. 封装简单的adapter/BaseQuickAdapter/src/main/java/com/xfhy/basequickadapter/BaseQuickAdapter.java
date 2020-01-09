package com.xfhy.basequickadapter;

import android.content.Context;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author feiyang
 * create at 2017/10/16 15:45
 * description：
 */
public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends RecyclerView
        .Adapter<K> {

    private Context mContext;
    /**
     * 数据集合
     */
    private List<T> dataList;
    /**
     * RecyclerView中普通item的布局id
     */
    private int layoutResId;
    /**
     * 子项item点击事件
     */
    private OnItemClickListener onItemClickListener;
    /**
     * 子项item长按事件
     */
    private OnItemLongClickListener onItemLongClickListener;

    public BaseQuickAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 此构造方法必须调用
     *
     * @param context     Context
     * @param layoutResId 子项普通item布局
     * @param dataList    子项数据集合
     */
    public BaseQuickAdapter(Context context, int layoutResId, List<T> dataList) {
        this.mContext = context;
        this.layoutResId = layoutResId;
        this.dataList = dataList;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutResId, parent, false);
        final BaseViewHolder baseViewHolder = new BaseViewHolder(view);

        // item点击事件
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition());
                }
            });
        }

        // item长按事件
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongClick(v, baseViewHolder
                            .getLayoutPosition());
                }
            });
        }
        return (K) baseViewHolder;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        convert(holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    /**
     * 绑定数据
     *
     * @param holder BaseViewHolder
     * @param item   item数据
     */
    protected abstract void convert(BaseViewHolder holder, T item);

    /**
     * 设置item点击事件
     *
     * @param onItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(@NonNull OnItemClickListener
                                               onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     *
     * @param onItemLongClickListener OnItemLongClickListener
     */
    public void setOnItemLongClickListener(@NonNull OnItemLongClickListener
                                                   onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * item点击监听器
     */
    public interface OnItemClickListener {
        /**
         * item点击事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         */
        void onItemClick(View view, int position);
    }

    /**
     * item长按监听器
     */
    public interface OnItemLongClickListener {
        /**
         * item长按事件回调
         *
         * @param view     触发事件View
         * @param position 触发事件的view所在RecyclerView中的位置
         * @return 是否消费
         */
        boolean onLongClick(View view, int position);
    }

    /**
     * 删除item
     *
     * @param position 删除item的位置
     */
    public void removeItem(@IntRange(from = 0) int position) {
        if (dataList == null) {
            return;
        }
        if (position >= dataList.size()) {
            return;
        }

        dataList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 新增item
     *
     * @param position 新增item位置
     * @param item     item数据
     */
    public void addItem(@IntRange(from = 0) int position, @NonNull T item) {
        if (dataList == null) {
            return;
        }
        if (position > dataList.size()) {
            return;
        }
        dataList.add(position, item);
        notifyItemInserted(position);
    }

}
