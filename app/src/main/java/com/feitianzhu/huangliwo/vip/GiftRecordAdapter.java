package com.feitianzhu.huangliwo.vip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.GiftRecordModel;

import java.util.List;
import java.util.Locale;

public class GiftRecordAdapter extends BaseQuickAdapter<GiftRecordModel, BaseViewHolder> {
    public GiftRecordAdapter(@Nullable List<GiftRecordModel> data) {
        super(R.layout.layout_gift_record, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, GiftRecordModel giftRecordModel) {
        holder.setText(R.id.merchants_name, giftRecordModel.merchantName);
        holder.setText(R.id.gitf_name, giftRecordModel.giftName);
        holder.setText(R.id.price, "¥" + String.format(Locale.getDefault(), "%.2f", giftRecordModel.price));
        holder.setText(R.id.date, giftRecordModel.createTime);
    }
}
