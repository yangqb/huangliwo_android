package com.feitianzhu.fu700.me.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.AddressInfo;

import java.util.List;

public class AddressManagementAdapter extends BaseQuickAdapter<AddressInfo.ShopAddressListBean, BaseViewHolder> {
    public AddressManagementAdapter(@Nullable List<AddressInfo.ShopAddressListBean> data) {
        super(R.layout.layout_address_management, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AddressInfo.ShopAddressListBean item) {
        helper.setText(R.id.name, item.getUserName());
        helper.setText(R.id.phone, item.getPhone());
        helper.setText(R.id.address, item.getDetailAddress());
        if (item.getIsDefalt() == 1) {
            helper.setVisible(R.id.isDefault, true);
        } else {
            helper.setVisible(R.id.isDefault, false);
        }
    }
}
