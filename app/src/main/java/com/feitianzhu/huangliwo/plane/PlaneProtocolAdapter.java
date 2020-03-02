package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class PlaneProtocolAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PlaneProtocolAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_plane_protocol, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.imageView));

    }
}
