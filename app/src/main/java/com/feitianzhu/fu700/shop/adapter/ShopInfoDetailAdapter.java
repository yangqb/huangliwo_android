package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/26 0026 下午 2:51
 */
public class ShopInfoDetailAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public ShopInfoDetailAdapter(@Nullable List<Integer> data) {
        super(R.layout.shop_info_detail_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {

    }
}
