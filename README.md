# BaseRecyclerViewAdapterHelper

> 经常使用RecyclerView,每次都要写很多重复的代码,真的烦.这不,最近在写的一个小项目很多地方都需要用到RecyclerView,于是我干脆将那个项目停下来,
写这个BaseAdapter.
当然,我觉得应该会踩很多坑吧,毕竟菜鸟一枚.要加油了!

## 声明
- 此项目由本人看BRAVH( https://github.com/CymChad/BaseRecyclerViewAdapterHelper )源码,然后抽取出的一部分功能比较适合自己的RecyclerViewAdapter库,主要是为了方便自己食用.在抽取的过程中,我把源码分析了一遍,然后抽取了一些功能在此项目中.在此,感谢原作者,感谢开源.
- 本人写的分析BRAVH源码的一系列博客地址: http://blog.csdn.net/xfhy_/article/details/78274652

## 引入

**Step 1**. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
          ...
          maven { url 'https://jitpack.io' }
	}
}
```

**Step 2**. Add the dependency
```gradle
dependencies {
    compile 'com.github.xfhy:BaseRecyclerViewAdapter:v1.0'
}
```

## 使用

### 添加Item事件
Item的点击事件

```java
adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemClick: ");
        Toast.makeText(ItemClickActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
    }
});
```
Item的长按事件
```java
adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemLongClick: ");
        Toast.makeText(ItemClickActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
        return false;
    }
});
```
Item子控件的点击事件
```java
holder.setOnClickListener(R.id.tv_list, new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        LogUtils.e("你好啊");
    }
});
```
Item子控件的长按事件
```java
holder.setOnLongClickListener(R.id.tv_list, new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        LogUtils.e("你好");
        return true;
    }
});
```

### 添加列表加载动画
一行代码轻松切换5种默认动画。

开启动画(默认为渐显效果)
`adapter.openLoadAnimation();`

默认提供6种动画（渐显、缩放、从下到上，从左到右、从右到左、从上往下）

```java
    BaseQuickAdapter.ALPHAIN           //渐显
    BaseQuickAdapter.SCALEIN           //缩放
    BaseQuickAdapter.SLIDEIN_BOTTOM    //从下到上
    BaseQuickAdapter.SLIDEIN_LEFT      //从左到右
    BaseQuickAdapter.SLIDEIN_RIGHT     //从右到左
    BaseQuickAdapter.SLIDEIN_TOP       //从上往下
```

切换动画

`quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);`

自定义动画
```java
quickAdapter.openLoadAnimation(new BaseAnimation() {
        @Override
        public Animator[] getAnimators(View view) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
            };
        }
    });
```

动画默认只执行一次,如果想重复执行可设置

`mQuickAdapter.isFirstOnly(false);`

设置不显示动画数量(设置的这count个item是不执行动画的)

`adapter.setNotDoAnimationCount(count);`
首次到界面的item都依次执行加载动画

由于进入界面的item都是很多的速度进来的所以不会出现滑动显示的依次执行动画效果，这个时候会一起执行动画，如果觉得这样的效果不好可以使用setNotDoAnimationCount设置第一屏item不执行动画，但是如果需要依次执行动画可以重写startAnim让第一个屏幕的item动画延迟执行即可。
```java
@Override
protected void startAnim(Animator anim, int index) {
    super.startAnim(anim, index);
    if (index < count)
    anim.setStartDelay(index * 150);
}
```
### 添加头部、尾部
一行代码搞定，感觉又回到ListView时代。

添加头布局、尾部局
```java
mQuickAdapter.addHeaderView(view1);
mQuickAdapter.addFooterView(view2);
```

删除头、尾布局中指定view
```java
mQuickAdapter.removeHeaderView(view1);
mQuickAdapter.removeFooterView(view2);
```
删除头、尾布局中所有view
```java
mQuickAdapter.removeAllHeaderView();
mQuickAdapter.removeAllFooterView();
```
默认出现了头部就不会显示EmptyView，和尾部局，配置以下方法也支持同时显示(同时显示头布局和空布局)：
```java
//同时显示头布局和空布局
mQuickAdapter.setHeaderAndEmpty(true);
//同时显示尾布局和空布局
mQuickAdapter.setHeaderFooterEmpty(true);
```
默认头部尾部都是占满一行，如果需要不占满可以配置：
```java
//设置头布局占满一行
mQuickAdapter.setHeaderViewAsFlow(false);
//设置尾布局占满一行
mQuickAdapter.setFooterViewAsFlow(false);
```

### 自动加载
上拉加载无需监听滑动事件,可自定义加载布局，显示异常提示，自定义异常提示。同时支持下拉加载。

```java
// 滑动最后一个Item的时候回调onLoadMoreRequested方法
mQuickAdapter.setOnLoadMoreListener(RequestLoadMoreListener);

//或者
pullToRefreshAdapter.setOnLoadMoreListener(RequestLoadMoreListener, mRecyclerView);
```

默认第一次加载会回调上面的RequestLoadMoreListener，如果第一次不想让它回调则可以配置(下面的方法用于设置当item未占满一屏幕时,不开启load more)：
```java
mQuickAdapter.bindToRecyclerView(mRecyclerView);
mQuickAdapter.disableLoadMoreIfNotFullPage();
```

处理回调
```java
@Override
public void onLoadMoreRequested() {
    Log.e("xfhy", "onLoadMoreRequested");
    //在这里去处理加载更多的逻辑
}
```

加载完更多数据之后
```java
//加载完成  （注意不是加载结束，而是本次数据加载结束并且还有下页数据）
mQuickAdapter.loadMoreComplete();

//加载失败  显示文字为:加载失败，请点我重试     加载更多布局在这个时候是支持点击的,这个时候点击加载更多布局的话,就回去通知启动回调并重新加载更多
mQuickAdapter.loadMoreFail();

//加载结束(即没有更多数据了)  加载更多的布局可见   显示文字为:客官,没有更多数据了~
mQuickAdapter.loadMoreEnd();

//加载结束(即没有更多数据了)   true:加载更多的布局不可见   false:可见
mQuickAdapter.loadMoreEnd(true);
```

设置什么时候回调?
```java
/**
    * 设置当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested()方法
    * @param preLoadNumber
    */
public void setPreLoadNumber(int preLoadNumber) {
    if (preLoadNumber > 1) {
        mPreLoadNumber = preLoadNumber;
    }
}
```

注意：如果上拉结束后，下拉刷新需要再次开启上拉监听，需要使用setNewData()方法填充数据。

设置上拉刷新是否启用(比如在下拉加载时就需要控制一下是否启用)
mQuickAdapter.setEnableLoadMore(boolean);

### 分组布局
随心定义分组头部。

实体类必须继承SectionEntity
```java
public class MySection extends SectionEntity<Video> {
    private boolean isMore;
    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(Video t) {
        super(t);
    }
}
```

adapter构造需要传入两个布局id，第一个是item的，第二个是head的，在convert方法里面加载item数据，在convertHead方法里面加载head数据
```java
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

```

### 多布局
简单配置、无需重写额外方法。

实体类必须实现MultiItemEntity，在设置数据的时候，需要给每一个数据设置itemType
```java
public class MultipleItem implements MultiItemEntity {
    //用来标识该item类型
    private int itemType;
    //设置该item所跨的度
    private int spanSize;
    private String content;

    public MultipleItem(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
```

在构造里面addItemType绑定type和layout的关系
```java
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.text_view);
        addItemType(MultipleItem.IMG, R.layout.image_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                helper.setImageUrl(R.id.iv, item.getContent());
                break;
        }
    }

}
```

如果是GridLayoutManager,如果需要设置不同的item跨不同的跨度,可以做如下设置
```java
  multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
```
### 设置空布局

没有数据的时候默认显示该布局
```java
//方式1: 
mQuickAdapter.setEmptyView(layoutResId,viewGroup);
//方式2: 
mQuickAdapter.setEmptyView(view);
//方式3: 
mQuickAdapter.bindToRecyclerView(RecyclerView)
mQuickAdapter.setEmptyView(layoutResId);

//设置当数据为空时是否显示headerView
mQuickAdapter.setHeaderAndEmpty(true);

//设置数据为空时是否显示headerView和footerView
mQuickAdapter.setHeaderFooterEmpty(boolean isHeadAndEmpty, boolean isFootAndEmpty);

//设置是否需要使用EmptyView
mQuickAdapter.isUseEmpty(boolean isUseEmpty);
```