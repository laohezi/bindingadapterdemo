package com.example.m.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.m.bean.Image2Bean;
import com.example.m.bean.ImageBean;
import com.example.m.bean.TextBean;
import com.example.m.bindingadapter.R;
import com.example.m.bindingadapter.bindingadapter.BindingAdapter;
import com.example.m.bindingadapter.bindingadapter.BindingAdapterItem;
import com.example.m.bindingadapter.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initListView();

    }

    private void initListView() {
        List<BindingAdapterItem> items = new ArrayList<>();
        items.add(new TextBean("哈哈哈哈"));
        items.add(new ImageBean());
        items.add(new Image2Bean());
        items.add(new Image2Bean());
        items.add(new TextBean("我又来啦"));
        items.add(new Image2Bean());
        items.add(new ImageBean());
        items.add(new TextBean("我还来"));
        items.add(new TextBean("就是不让你看美女"));
        items.add(new Image2Bean());
        items.add(new ImageBean());
        items.add(new TextBean("哈哈你当不住我看见啦"));
        BindingAdapter adapter = new BindingAdapter();
        adapter.setItems(items);
        //这也是一个坑，经常忘了加LayoutManger导致东西Item无法显示，RecyclerView把测量，布局的工作甩给了LayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
