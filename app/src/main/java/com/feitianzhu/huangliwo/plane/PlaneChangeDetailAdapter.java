package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.List;

public class PlaneChangeDetailAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PlaneChangeDetailAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_plane_change_detail, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
