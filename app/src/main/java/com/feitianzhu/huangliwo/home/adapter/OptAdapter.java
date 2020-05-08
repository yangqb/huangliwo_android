package com.feitianzhu.huangliwo.home.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.home.adapter
 * user: yangqinbo
 * date: 2020/5/8
 * time: 16:55
 * email: 694125155@qq.com
 */
public class OptAdapter extends BaseQuickAdapter<MerchantsModel, BaseViewHolder> {
    public OptAdapter(@Nullable List<MerchantsModel> data) {
        super(R.layout.item_opt_merchant, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MerchantsModel item) {

    }
}
