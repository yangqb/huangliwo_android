package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class PlaneCancelChangeAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PlaneCancelChangeAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_cancel_change, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
