package com.feitianzhu.huangliwo.travel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;

import java.util.List;

public class MyOilAdapter extends BaseQuickAdapter <OilListBean, BaseViewHolder>{

    public MyOilAdapter( @Nullable List<OilListBean> data) {
        super(R.layout.oil_adapter_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilListBean item) {

    }
}
