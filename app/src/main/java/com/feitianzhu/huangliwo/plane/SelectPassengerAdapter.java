package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class SelectPassengerAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public SelectPassengerAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_select_passenger, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
