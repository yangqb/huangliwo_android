package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;

import java.util.List;


/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/3
 * time: 17:02
 * email: 694125155@qq.com
 */
public class SelfMerchantsOrderAdapter extends BaseMultiItemQuickAdapter<SelfMerchantsModel, BaseViewHolder> {
    public SelfMerchantsOrderAdapter(@Nullable List<SelfMerchantsModel> data) {
        super(data);
        addItemType(SelfMerchantsModel.OFFLINE_TYPE, R.layout.layout_merchants_off_order);
        addItemType(SelfMerchantsModel.ONLINE_TYPE, R.layout.layout_merchants_on_order);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelfMerchantsModel item) {

    }
}
