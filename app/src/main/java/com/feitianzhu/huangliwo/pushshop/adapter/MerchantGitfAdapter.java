package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MerchantGitModel;

import java.util.List;
import java.util.Locale;

public class MerchantGitfAdapter extends BaseItemDraggableAdapter<MerchantGitModel, BaseViewHolder> {
    public MerchantGitfAdapter(@Nullable List<MerchantGitModel> data) {
        super(R.layout.layout_merchant_gift, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MerchantGitModel s) {
        baseViewHolder.setText(R.id.name, s.giftName);
        baseViewHolder.setText(R.id.price, "¥" + String.format(Locale.getDefault(), "%.2f", s.price));
    }
}
