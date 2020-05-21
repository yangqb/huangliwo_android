package com.feitianzhu.huangliwo.travel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.travel.bean.OilOrederBean;

import java.util.List;

/**
 * Created by bch on 2020/5/19
 */
public class TraveFormAdapter extends BaseQuickAdapter<OilOrederBean, BaseViewHolder> {

    public TraveFormAdapter(@Nullable List<OilOrederBean> data) {
        super(R.layout.item_trave_form, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilOrederBean item) {
        helper.setText(R.id.payNum, item.getAmountPay());
        helper.setText(R.id.name, item.getGasName());
        helper.setText(R.id.level1, item.getOilNo());
        helper.setText(R.id.level2, String.valueOf(item.getGunNo())+"号");
        helper.setText(R.id.formId, "订单号: "+item.getOrderId());
        helper.setText(R.id.payState, item.getOrderStatusName());
        helper.setText(R.id.time, item.getPayTime());
    }



}
