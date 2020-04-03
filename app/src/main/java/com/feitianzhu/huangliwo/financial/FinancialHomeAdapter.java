package com.feitianzhu.huangliwo.financial;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.financial.bean.MultiFinancialInfo;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.financial
 * user: yangqinbo
 * date: 2020/3/30
 * time: 14:43
 * email: 694125155@qq.com
 */
public class FinancialHomeAdapter extends BaseMultiItemQuickAdapter<MultiFinancialInfo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FinancialHomeAdapter(List<MultiFinancialInfo> data) {
        super(data);
        addItemType(MultiFinancialInfo.All_FINANCIAL, R.layout.item_financial);
        addItemType(MultiFinancialInfo.MY_FINANCIAL, R.layout.item_my_financial);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiFinancialInfo item) {
        //禁用状态
        /*seekBar.setClickable(false);
        seekBar.setEnabled(false);
        seekBar.setFocusable(false);*/
    }
}
