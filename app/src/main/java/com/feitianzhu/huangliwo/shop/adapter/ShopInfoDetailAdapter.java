package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;

import java.util.List;
import java.util.Locale;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/26 0026 下午 2:51
 */
public class ShopInfoDetailAdapter extends BaseQuickAdapter<SingleGoodsModel, BaseViewHolder> {

    public ShopInfoDetailAdapter(@Nullable List<SingleGoodsModel> data) {
        super(R.layout.shop_info_detail_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingleGoodsModel item) {
        helper.setText(R.id.singleName, item.getName());
        helper.setText(R.id.singlePrice, "¥"+String.format(Locale.getDefault(), "%.2f", item.getSinglePrice()));
    }
}
