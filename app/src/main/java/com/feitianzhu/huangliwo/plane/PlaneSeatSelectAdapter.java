package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class PlaneSeatSelectAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PlaneSeatSelectAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_plane_seat_select, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
