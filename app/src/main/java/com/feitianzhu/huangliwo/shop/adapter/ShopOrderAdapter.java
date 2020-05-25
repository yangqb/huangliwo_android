package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.SFBaseAdapter;
import com.feitianzhu.huangliwo.model.ShopOrderModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ShopOrderAdapter extends SFBaseAdapter<ShopOrderModel.ListEntity, BaseViewHolder> {
  public ShopOrderAdapter(@Nullable List<ShopOrderModel.ListEntity> data) {
    super(R.layout.item_shop_order_view, data);
  }

  @Override protected void convert(BaseViewHolder mBaseViewHolder, ShopOrderModel.ListEntity mShopTypeModel) {
    if ("0".equals(mShopTypeModel.isEval)){
      mBaseViewHolder.setVisible(R.id.to_revalate,true);
    }else{
      mBaseViewHolder.setVisible(R.id.to_revalate,false);
    }
      ImageView mView = mBaseViewHolder.getView(R.id.item_icon);
      Glide.with(mContext).load(mShopTypeModel.merchant.merchantHeadImg)
              .apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
              .into(mView);
      mBaseViewHolder.setText(R.id.item_name,mShopTypeModel.merchant.merchantName+"");
      mBaseViewHolder.setText(R.id.item_time,mShopTypeModel.createDate+"");
      mBaseViewHolder.setText(R.id.item_money,"￥"+mShopTypeModel.consumeAmount+"");
      mBaseViewHolder.addOnClickListener(R.id.to_revalate);
  }
}
