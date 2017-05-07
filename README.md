阅读这篇笔记你需要了解安卓的数据绑定框架**databinding**
首先贴上校长看到的感觉写得最好的两篇 介绍**databinding**的文章：
[1. CornorLin：Android Data Binding 系列(一) -- 详细介绍与使用](http://connorlin.github.io/2016/07/02/Android-Data-Binding-%E7%B3%BB%E5%88%97-%E4%B8%80-%E8%AF%A6%E7%BB%86%E4%BB%8B%E7%BB%8D%E4%B8%8E%E4%BD%BF%E7%94%A8/)
[2. QQ音乐技术团队：Android DataBinding 数据绑定](http://chuansong.me/n/848619251632)


不管作为一名安卓还是android程序猿，总是少不了一直没完没了的重复制造adapter，viewholder，就像
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5650829-caaeb32faadfb235.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
等等，啊喂，这根本是同一个类呀！哦不好意思，实在太像了，搞错了，真实的情况是这样的：

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5650829-fe35fc32b75527e4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这简直可以做一个找茬游戏了，整天在这弄重复的代码，不禁要想，除了那些肮脏（滑稽）的大洋我们整天这样图的是什么。作为一名有追求的程序猿大爷，最为一名统筹大局立志作为一名架构师，作为一名从来懒得多写代码的程序猿这样的情况怎么能忍！

#####Adapter与ViewHolder的作用：
-  `ViewHolder`是用来通过findviewById来存放item对应的layout里边的View控件的，
-  `Adapter`中` onCreateViewHolder(ViewGroup parent, int viewType)`方法负责将获取将ViewHolder取出;` void onBindViewHolder(BindingHolder holder, int position)`负责将实体类的内容一条一条的通过set方法显示到对应的界面上。

#####再看看databinding的作用：
-  通过DatabindingUtils的`public static <T extends ViewDataBinding> T inflate(LayoutInflater inflater, int layoutId,
            @Nullable ViewGroup parent, boolean attachToParent)`方法获取一个`ViewDataBinding`，包含了Layout中所有的控件；
- `boolean setVariable(int variableId, Object value)`负责将数据与界面绑定自动完成类似`textview.setText(item.text)`这样的工作，

是不是感觉职能高度重合呢，而且**databinding**好像用起来更省力，那是不是可以利用`ViewDataBinding` 替代ViewHoldr里边的没完没了的findViewById呢，能不能用一个`boolean setVariable(int variableId, Object value)`来替代` onBindViewHolder(BindingHolder holder, int position)`中没完没了的set房呢。答案是可以的

现在假定你已经阅读过那两篇文章，对于**databinding**有了一定的理解。先上demo的[***代码地址***](https://github.com/laohezi/bindingadapterdemo)
先看一下效果图：

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5650829-42d4b2a8328c4e57.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看出来在demo中有三种item，按照以前的惯例。我们需要三个Adapter，三个ViewHolder，三个实体Bean，三个layout文件。但是呢，让我们看一下demode代码结构

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5650829-b146fd21594a17a8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
三个实体bean，三个layout，但是只有一个Adapter，里边有一个ViewHolder。但是实现了三种item的效果好神奇吧，并且即使我想再加一种item，只需要添加一个实体Bean，再加一个layout文件就好了不用去写什么ViewHolder跟Adapter了，哈哈神奇吧。

首先看看我们是怎么用的吧：
```
  List<BindingAdapterItem> items = new ArrayList<>();
        items.add(new TextItem("哈哈哈哈"));
        items.add(new ImageItem());
        items.add(new Image2Item());
        items.add(new Image2Item());
        items.add(new TextItem("我又来啦"));
        items.add(new Image2Item());
        items.add(new ImageItem());
        items.add(new TextItem("我还来"));
        items.add(new TextItem("就是不让你看美女"));
        items.add(new Image2Item());
        items.add(new ImageItem());
        items.add(new TextItem("哈哈你当不住我看见啦"));

//一个BindingAdapter适配这几种布局
        BindingAdapter adapter = new BindingAdapter();
        adapter.setItems(items);
        //这也是一个坑，经常忘了加LayoutManger导致东西Item无法显示，RecyclerView把测量，布局的工作甩给了LayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
```
就像平常使用RecyclerView一样，用一个List包装要显示的数据，其中TextItem，ImageItem，Image2Item是对应三种不同布局的实体类。那么这些实体类里边一定要有一些信息能够让BindingAdapter识别他们的布局信息，最简单的方法就是在这些实体重直接返回布局文件，把他们返回布局的共同方法命名为`int getViewType()`并创造一个新的iterface来封装这个方法:
```
public interface BindingAdapterItem {
    int getViewType();
}
```

以后每一个Item只需要实现这个接口中的int getViewType()方法就能告诉Adapter自己的布局了。

例如TextItem的实现为：
```
public class TextItem extends BaseObservable implements BindingAdapterItem {
    @Override
    public int getViewType() {
//返回对应的布局文件
        return R.layout.adapter_text;
    }

    public TextItem(String text) {
        this.text = text;
    }

    private String text;

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }
}

```
继承BaseObservalable是为了将数据与界面绑定，详情请阅读开头的两篇文章。
再看一下TextItem的layout的内容：
```
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="item"
            type="com.example.m.Item.TextItem"/>
    </data>
    <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{item.text}"
            android:gravity="center"
            android:textSize="25sp"
            />
</layout>
```
刚刚讲过`ViewDataBing`通过
```
boolean setVariable(int variableId, Object value)
```
方法来将数据绑定到界面上,其中`int variableId`指的是变量在BR类中的ID,
```
 <variable
            name="item"
            type="com.example.m.Item.TextItem"/>
    </data>
```
中的name,而`Object value`对应其中的type,在
```
 android:text="@{item.text}"
```
中将`TextItem`中的`text`属性绑定到对对应的控件上.
```
  <data>
        <variable
            name="item"
            type="com.example.m.Item.TextItem"/>
    </data>
```
好的下面去往通用的BindingAdapter去看看
```
public class BindingAdapter extends RecyclerView.Adapter<BindingAdapter.BindingHolder> {


    public List<BindingAdapterItem> getItems() {
        return items;
    }

    public void setItems(List<BindingAdapterItem> items) {
        this.items = items;
    }
    List<BindingAdapterItem> items = new ArrayList<>();
    
    /**
     * @return 返回的是adapter的view
     */
    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new BindingHolder(binding);
    }
    /*
    * 数据绑定
    * */
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bindData(items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;
         /**
         * @param binding   可以看作是这个hodler代表的布局的马甲，getRoot()方法会返回整个holder的最顶层的view
         * */
        public BindingHolder(ViewDataBinding binding) {
            //
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(BindingAdapterItem item) {
            binding.setVariable(BR.item,item);
        }

    }
}

```
这个类的基本要求是:
- 能够根据传进来的对定的item判断对应的布局,
- 能够自动的把传进来的数据显示到对应的布局上；

adpter获取正确的布局很简单，只需要重写int getItemViewType(int position)方法，在里边直接返回item里边的layout就行了：
```
 @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }
```
现在先用ViewDataBinding来取代View，标准的VIewholder应该是通过layout的rootView来构造，我们可以通过ViewDataBinding.getRoot()来返回这个rootview
```
  ViewDataBinding binding;
         /**
         * @param binding   可以看作是这个hodler代表的布局的马甲，getRoot()方法会返回整个holder的最顶层的view
         * */
        public BindingHolder(ViewDataBinding binding) {
            //
            super(binding.getRoot());
            this.binding = binding;
        }
```
绑定数据的时候只需要将实例化后的实体类对象传入ViewDataBinding的对应的virable中就好了：
```
public void bindData(BindingAdapterItem item) {
            //
            binding.setVariable(BR.item,item);
        }
```
***因为这里的`int variableId`是固定的`BR.item`所以每一个layout中variable的`name`属性必须为item!***
在Adapter的`onCreateViewHolder(ViewGroup parent, int viewType)`中改用获取对应的ViewDataBing来初始化ViewHodler：
```
 @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new BindingHolder(binding);
    }
```

然后在`onBindViewHolder(BindingHolder holder, int position)`中调用就好了：
```
 @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bindData(items.get(position));
    }
```
