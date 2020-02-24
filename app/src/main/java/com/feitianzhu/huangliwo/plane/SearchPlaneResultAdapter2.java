package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class SearchPlaneResultAdapter2 extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchPlaneResultAdapter2(@Nullable List<String> data) {
        super(R.layout.layout_plane_result2, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }
}
