package com.feitianzhu.huangliwo.shop.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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
public class LeftAdapter extends BaseMultiItemQuickAdapter<MultiItemShopAndMerchants, BaseViewHolder> {
    private int pos = 0;

    public LeftAdapter(List<MultiItemShopAndMerchants> list) {
        super(list);
        addItemType(MultiItemShopAndMerchants.MERCHANTS_TYPE, R.layout.shop_left_item1);
        addItemType(MultiItemShopAndMerchants.SHOP_TYPE, R.layout.shop_left_item1);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemShopAndMerchants item) {

        switch (helper.getItemViewType()) {
            case MultiItemShopAndMerchants.MERCHANTS_TYPE:
                helper.setText(R.id.text, item.getMerchantsClassifyModel().getClsName());
                if (helper.getAdapterPosition() == pos) {
                    helper.getView(R.id.text).setSelected(true);
                    helper.getView(R.id.view).setSelected(true);
                } else {
                    helper.getView(R.id.text).setSelected(false);
                    helper.getView(R.id.view).setSelected(false);
                }
                break;
            case MultiItemShopAndMerchants.SHOP_TYPE:
                helper.setText(R.id.text, item.getShopClassifyModel().getClsName());
                if (helper.getAdapterPosition() == pos) {
                    helper.getView(R.id.text).setSelected(true);
                    helper.getView(R.id.view).setSelected(true);
                } else {
                    helper.getView(R.id.text).setSelected(false);
                    helper.getView(R.id.view).setSelected(false);
                }
                break;
        }
    }

    public int getPos() {
        return pos;
    }

    public void setSelect(int position) {
        this.pos = position;
    }
}
