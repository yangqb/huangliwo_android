package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.TimePointChargsInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

public class PlaneChangeServiceAdapter extends BaseQuickAdapter<TimePointChargsInfo, BaseViewHolder> {
    public PlaneChangeServiceAdapter(@Nullable List<TimePointChargsInfo> data) {
        super(R.layout.item_change_service, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TimePointChargsInfo item) {
        helper.setText(R.id.date, item.timeText);
        helper.setText(R.id.price, "¥" + MathUtils.subZero(String.valueOf(item.changeFee)) + "/人");
    }
}
