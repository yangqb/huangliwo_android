package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/5
 * time: 20:09
 * email: 694125155@qq.com
 */
public class LogisticsAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public LogisticsAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_logistics_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.line1, false);
        } else {
            helper.setVisible(R.id.line1, true);
        }
        if (helper.getAdapterPosition() == this.getItemCount() - 1) {
            helper.setVisible(R.id.line2, false);
        } else {
            helper.setVisible(R.id.line2, true);
        }

    }
}
