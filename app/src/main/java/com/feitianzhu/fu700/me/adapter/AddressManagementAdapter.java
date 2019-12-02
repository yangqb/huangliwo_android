package com.feitianzhu.fu700.me.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;

import java.util.List;

public class AddressManagementAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public AddressManagementAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_address_management, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
