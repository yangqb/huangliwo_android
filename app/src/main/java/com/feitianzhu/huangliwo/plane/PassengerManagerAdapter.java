package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.PassengerModel;

import java.util.List;

public class PassengerManagerAdapter extends BaseQuickAdapter<PassengerModel, BaseViewHolder> {
    public PassengerManagerAdapter(@Nullable List<PassengerModel> data) {
        super(R.layout.layout_passenger_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PassengerModel item) {
        helper.addOnClickListener(R.id.rl_select);
        if (item.isSelect) {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_02quan);
        } else {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_01quan);
        }
    }
}
