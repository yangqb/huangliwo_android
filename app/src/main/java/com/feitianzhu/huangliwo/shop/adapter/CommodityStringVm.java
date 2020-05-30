package com.feitianzhu.huangliwo.shop.adapter;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.LayoutInflater;
import android.view.View;

import com.feitianzhu.huangliwo.databinding.ItemCommdityStringBinding;

public class CommodityStringVm {
    ItemCommdityStringBinding binding;
    public ObservableField<String> observableField = new ObservableField<>();

    public CommodityStringVm() {

    }

    public View getView(LayoutInflater layoutInflater) {
        binding = ItemCommdityStringBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    public void setTitle(String name) {
        observableField.set(name);
    }

    public String getTitle() {
        return observableField.get();
    }
}
