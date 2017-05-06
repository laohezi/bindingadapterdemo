package com.example.m.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.m.Item.Image2Item;
import com.example.m.Item.ImageItem;
import com.example.m.Item.TextItem;
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


        BindingAdapter adapter = new BindingAdapter();
        adapter.setItems(items);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
