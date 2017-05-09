package com.example.m.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.m.bindingadapter.BR;
import com.example.m.bindingadapter.R;
import com.example.m.bindingadapter.bindingadapter.BindingAdapterItem;

/**
 * Created by m on 2017/5/6.
 */

public class TextBean extends BaseObservable implements BindingAdapterItem {
    @Override
    public int getViewType() {
        return R.layout.adapter_text;
    }

    public TextBean(String text) {
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
