package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.pushshop.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 17:56
 * email: 694125155@qq.com
 */
public class ReasonAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ReasonAdapter(@Nullable List<String> data) {
        super(R.layout.layout_reason, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_reason, item);

    }
}
