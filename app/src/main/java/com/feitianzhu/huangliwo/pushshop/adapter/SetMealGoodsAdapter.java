package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;

import java.util.List;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/13
 * time: 18:10
 * email: 694125155@qq.com
 */
public class SetMealGoodsAdapter extends BaseItemDraggableAdapter<SingleGoodsModel, BaseViewHolder> {
    public SetMealGoodsAdapter(@Nullable List<SingleGoodsModel> data) {
        super(R.layout.setmeal_goods_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SingleGoodsModel item) {
        helper.setText(R.id.setMealName, item.getName());
        helper.setText(R.id.setMealNum, item.getNum() + "");
        helper.setText(R.id.setMealPrice, String.valueOf(item.getSinglePrice()));
    }

    @Override
    public int getParentPosition(@NonNull SingleGoodsModel item) {
        return POSITION_NONE;
    }
}
