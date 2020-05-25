package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.PassengerModel;

import java.util.List;

public class PassengerManagerAdapter extends BaseItemDraggableAdapter<PassengerModel, BaseViewHolder> {
    public PassengerManagerAdapter(@Nullable List<PassengerModel> data) {
        super(R.layout.layout_passenger_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PassengerModel item) {
        helper.addOnClickListener(R.id.rl_select);
        helper.setText(R.id.name, item.name);
        if (item.ageType == 0) {
            helper.setText(R.id.passenger_type, "成人");
        } else {
            helper.setText(R.id.passenger_type, "儿童");
        }
        if ("NI".equals(item.cardType)) {
            helper.setText(R.id.cardType, "身份证");
        } else if ("PP".equals(item.cardType)) {
            helper.setText(R.id.cardType, "护照");
        } else if ("GA".equals(item.cardType)) {
            helper.setText(R.id.cardType, "港澳通行证");
        } else if ("TW".equals(item.cardType)) {
            helper.setText(R.id.cardType, "台湾通行证");
        } else if ("TB".equals(item.cardType)) {
            helper.setText(R.id.cardType, "台胞证");
        } else if ("HX".equals(item.cardType)) {
            helper.setText(R.id.cardType, "回乡证");
        } else if ("HY".equals(item.cardType)) {
            helper.setText(R.id.cardType, "国际海员证");
        } else {
            helper.setText(R.id.cardType, "其他");
        }
        helper.setText(R.id.cardNo, item.cardNo);

        if (item.isSelect) {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_02quan);
        } else {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_01quan);
        }
    }
}
