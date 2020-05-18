package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomTgqChangeModel;
import com.feitianzhu.huangliwo.model.TgqPointCharges;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

public class PlaneCancelChangeAdapter extends BaseQuickAdapter<CustomTgqChangeModel, BaseViewHolder> {

    public PlaneCancelChangeAdapter(@Nullable List<CustomTgqChangeModel> data) {
        super(R.layout.layout_cancel_change, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CustomTgqChangeModel item) {
        helper.setText(R.id.timeText, item.timeText);
        helper.setText(R.id.amount, item.amountText);
    }
}
