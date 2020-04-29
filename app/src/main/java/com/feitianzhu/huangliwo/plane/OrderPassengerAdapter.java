package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/3/31
 * time: 15:51
 * email: 694125155@qq.com
 */
public class OrderPassengerAdapter extends BaseQuickAdapter<DocOrderDetailPassengersInfo, BaseViewHolder> {
    public OrderPassengerAdapter(@Nullable List<DocOrderDetailPassengersInfo> data) {
        super(R.layout.layout_order_passenger, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DocOrderDetailPassengersInfo item) {
        helper.setText(R.id.name, item.name);
        helper.setText(R.id.ageType, "(" + item.type + ")");
        helper.setText(R.id.cardNo, item.cardType + item.cardNum);
        if (item.ticketNo == null || TextUtils.isEmpty(item.ticketNo)) {
            helper.setGone(R.id.ticketNo, false);
        } else {
            helper.setGone(R.id.ticketNo, true);
            helper.setText(R.id.ticketNo, item.ticketNo);
        }
        helper.addOnClickListener(R.id.rl_copy);


    }
}
