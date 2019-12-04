package com.feitianzhu.fu700.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ShopClassify;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 3:29
 */
public class LeftAdapter extends BaseQuickAdapter<ShopClassify.GGoodsClsListBean, BaseViewHolder> {
    private int pos = -1;

    public LeftAdapter(List<ShopClassify.GGoodsClsListBean> list) {
        super(R.layout.shop_left_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopClassify.GGoodsClsListBean item) {
        helper.setText(R.id.text, item.getClsName());
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
