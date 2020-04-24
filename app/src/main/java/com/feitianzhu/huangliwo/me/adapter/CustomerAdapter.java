package com.feitianzhu.huangliwo.me.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.HelperModel;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.me.adapter
 * user: yangqinbo
 * date: 2020/4/21
 * time: 18:24
 * email: 694125155@qq.com
 */
public class CustomerAdapter extends BaseQuickAdapter<HelperModel, BaseViewHolder> {
    public CustomerAdapter(@Nullable List<HelperModel> data) {
        super(R.layout.item_layout_customer, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HelperModel item) {
        helper.setText(R.id.title, item.title);
        helper.setText(R.id.service_time, item.serviceTime);
        Glide.with(mContext).load(item.helpImg).into((ImageView) helper.getView(R.id.img));
        if (item.type == 1) {
            helper.setGone(R.id.service_time, false);
        } else if (item.type == 3) {
            helper.setGone(R.id.service_time, false);
        } else {
            helper.setGone(R.id.service_time, true);
        }
    }
}
