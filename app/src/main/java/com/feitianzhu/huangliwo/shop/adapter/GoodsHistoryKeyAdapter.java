package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class GoodsHistoryKeyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GoodsHistoryKeyAdapter(@Nullable List<String> data) {
        super(R.layout.item_goods_key, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tvContent, item);
    }
}
