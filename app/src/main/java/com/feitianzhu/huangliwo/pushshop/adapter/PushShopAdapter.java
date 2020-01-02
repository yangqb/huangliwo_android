package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.pushshop.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 14:04
 * email: 694125155@qq.com
 */
public class PushShopAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PushShopAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_push_shop, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {

    }
}
