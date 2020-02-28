package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultiPlaneOrderModel;

import java.util.List;

public class PlaneOrderAdapter extends BaseMultiItemQuickAdapter<MultiPlaneOrderModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PlaneOrderAdapter(List<MultiPlaneOrderModel> data) {
        super(data);
        addItemType(MultiPlaneOrderModel.GO_TYPE, R.layout.item_go_plane_order);
        addItemType(MultiPlaneOrderModel.GO_COME_TYPE, R.layout.item_go_come_plane_order);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiPlaneOrderModel item) {

    }
}
