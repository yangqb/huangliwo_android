package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengerTypesInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;

import java.util.List;

public class RefundPlaneTicketPassengerAdapter extends BaseQuickAdapter<DocOrderDetailPassengersInfo, BaseViewHolder> {
    public RefundPlaneTicketPassengerAdapter(@Nullable List<DocOrderDetailPassengersInfo> data) {
        super(R.layout.item_refund_plane_ticket, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DocOrderDetailPassengersInfo item) {
        helper.setText(R.id.name, item.name + "（" + item.type + "）");
    }
}
