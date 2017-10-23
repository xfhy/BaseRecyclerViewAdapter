# 关于header和footer的完善

> 之前的源码阅读,添加header和footer,但是那个还不够完美,只能是在LinearLayoutManager时使用.

## 设置header和footer宽度为跨区域

```java
/**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW) {
            //设置为跨区域  比如是StaggeredGridLayoutManager时,header或者footer等应该如何展示
            setFullSpan(holder);
        } else {
            //添加动画到holder的itemView上,并执行动画
            addAnimation(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager
                    .LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }
```
当一个view被创建并添加到window时会调用onViewAttachedToWindow()方法,而在此时,我们做一下判断,
如果是空布局,header,footer,加载中布局,那么将该item设置为跨区域.
这样就不会出现在StaggeredGridLayoutManager中上面的那些布局是只占了一个跨度.说不清楚,看图
![](http://olg7c0d2n.bkt.clouddn.com/17-10-23/79484860.jpg)

如果不这样做,那么可能图中的那里标注出来的可能就会是header的效果,而不是跨2格.

## 设置每个item所占跨度
```java

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use {@link GridLayoutManager},and it will ignore span size.
     * 如果asFlow是true的话,那么footer和header就像正常item一样,不会跨区域
     * 默认是false,header和footer都占满屏幕
     */
    private boolean headerViewAsFlow, footerViewAsFlow;

    /**
     * 设置header不跨区域  就像正常item
     * true:不跨区域   false:跨区域
     */
    public void setHeaderViewAsFlow(boolean headerViewAsFlow) {
        this.headerViewAsFlow = headerViewAsFlow;
    }

    public boolean isHeaderViewAsFlow() {
        return headerViewAsFlow;
    }

    /**
     * 设置footer不跨区域  就像正常item
     * true:不跨区域   false:跨区域
     */
    public void setFooterViewAsFlow(boolean footerViewAsFlow) {
        this.footerViewAsFlow = footerViewAsFlow;
    }

    public boolean isFooterViewAsFlow() {
        return footerViewAsFlow;
    }

    /**
     * 判断当前type是否是特殊的type
     */
    protected boolean isFixedViewType(int type) {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW;
    }

    /**
     * RecyclerView在开始观察该适配器时调用。
     * 请记住，多个RecyclerView可能会观察到相同的适配器。
     * <p>
     * Adapter与RecyclerView关联起来
     * 这里面主要是做表格布局管理器的头布局和脚布局自占一行的适配
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            //设置adapter中每个Item所占用的跨度数
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);

                    //如果当前type为header并且asFlow为true:那么header设置为不跨区域,就和正常item一样.只占1格
                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
                        return 1;
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
                        return 1;
                    }

                    //如果用户没有自定义SpanSizeLookup  SpanSizeLookup是用来查询每个item占用的跨度数的实例
                    if (mSpanSizeLookup == null) {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么就是占1个格子,不跨
                        */
                        return isFixedViewType(type) ? gridManager.getSpanCount() : 1;
                    } else {
                        /*
                        1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么交给外部调用者来处理每个item应该占多少个格
                        */
                        return isFixedViewType(type) ? gridManager.getSpanCount() :
                                mSpanSizeLookup.getSpanSize(gridManager,
                                        position - getHeaderLayoutCount());
                    }
                }


            });
        }
    }

    private SpanSizeLookup mSpanSizeLookup;

    /**
    用于外部调用者设置每个item的跨度,除了header,footer,emptyView,loadMoreView
    */
    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     用于查询每个item占用的跨度数的实例   
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

```

首先,我们将`empty_view`,`header_view`,`footer_view`,`loading_view`这些特殊的布局,单独写个方法去判断是否属于特殊的布局.
并且我们设置了2个属性,用于开发者可能会将布局设置为header(footer)和普通item一样都只占1个格子,而不是占满屏幕(占gridManager的SpanCount个格子).
在RecyclerView在开始观察该适配器时调用onAttachedToRecyclerView()方法,然后在里面判断是否是GridLayoutManager,如果是则
设置adapter中每个Item所占用的跨度数.

getSpanSize()逻辑:
- 如果当前type为header或footer并且asFlow为true:那么header或footer设置为不跨区域,就和正常item一样.只占1格.
- 如果用户没有自定义SpanSizeLookup  SpanSizeLookup是用来查询每个item占用的跨度数的实例
    - 1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度  
                        2.如果item不是特殊的item,那么就是占1个格子,不跨
    -  1.如果是特殊的type,那么item的跨度设置为当前gridManager的SpanCount
                        即如果我设置的是mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        那么当前item占2格的跨度
                        2.如果item不是特殊的item,那么交给外部调用者来处理每个item应该占多少个格


## 
