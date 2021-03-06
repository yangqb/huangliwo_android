package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.LogisticsInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/5
 * time: 20:09
 * email: 694125155@qq.com
 */
public class LogisticsAdapter extends BaseQuickAdapter<LogisticsInfo, BaseViewHolder> {

    public LogisticsAdapter(@Nullable List<LogisticsInfo> data) {
        super(R.layout.layout_logistics_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LogisticsInfo item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.line1, false);
            helper.setBackgroundRes(R.id.current_status_view, R.drawable.shape_logistics_fed428);
        } else {
            helper.setVisible(R.id.line1, true);
            helper.setBackgroundRes(R.id.current_status_view, R.drawable.shape_logistics_cccccc);
        }
        if (helper.getAdapterPosition() == this.getItemCount() - 1) {
            helper.setVisible(R.id.line2, false);
        } else {
            helper.setVisible(R.id.line2, true);
        }

        helper.setText(R.id.content, item.getContext());
        if (item.getFtime() == null || TextUtils.isEmpty(item.getFtime())) {
            helper.setText(R.id.time, "");
        } else {
            String[] date = DateUtils.strToString(item.getFtime()).split(" ");
            helper.setText(R.id.time, date[0] + "\n" + date[1]);
        }
    }

}
