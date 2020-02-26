package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class PlaneDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PlaneDetailAdapter(@Nullable List<String> data) {
        super(R.layout.layout_plane_detail, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.btn_reserve);
    }
}
