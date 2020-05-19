package com.feitianzhu.huangliwo.shop.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultiItemShopAndMerchants;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 3:29
 */
public class LeftAdapter1 extends BaseQuickAdapter<String, BaseViewHolder> {
    private int pos = 0;

    public LeftAdapter1(List<String> list) {
        super(R.layout.shop_left_item1, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
        if (helper.getAdapterPosition() == pos) {
            helper.getView(R.id.text).setSelected(true);
            helper.getView(R.id.view).setSelected(true);
        } else {
            helper.getView(R.id.text).setSelected(false);
            helper.getView(R.id.view).setSelected(false);
        }

    }

    public void setSelect(int position) {
        this.pos = position;
    }
}
