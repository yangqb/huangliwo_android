package com.feitianzhu.huangliwo.strategy.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 3:29
 */
public class StrategyItem1Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public StrategyItem1Adapter(List<String> list) {
        super(R.layout.fragment_strategy_child1, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_number, item);
        helper.setText(R.id.content, item);



    }


}
