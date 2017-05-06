package com.example.m.Item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.m.bindingadapter.BR;
import com.example.m.bindingadapter.R;
import com.example.m.bindingadapter.bindingadapter.BindingAdapterItem;

/**
 * Created by m on 2017/5/6.
 */

public class TextItem extends BaseObservable implements BindingAdapterItem {
    @Override
    public int getViewType() {
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
