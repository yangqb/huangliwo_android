package com.feitianzhu.huangliwo.financial;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.financial
 * user: yangqinbo
 * date: 2020/3/30
 * time: 14:43
 * email: 694125155@qq.com
 */
public class FinancialHomeAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public FinancialHomeAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_financial, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        //禁用状态
        /*seekBar.setClickable(false);
        seekBar.setEnabled(false);
        seekBar.setFocusable(false);*/
    }
}
