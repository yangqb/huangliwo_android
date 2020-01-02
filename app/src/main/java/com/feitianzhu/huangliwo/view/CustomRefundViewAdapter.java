package com.feitianzhu.huangliwo.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/6
 * time: 11:49
 * email: 694125155@qq.com
 */
public class CustomRefundViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CustomRefundViewAdapter(@Nullable List<String> data) {
        super(R.layout.refund_dialog_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tvContent, item);

    }
}
