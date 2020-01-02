package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.SFBaseAdapter;
import com.feitianzhu.huangliwo.model.ServeOrderModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ServeOrderAdapter extends SFBaseAdapter<ServeOrderModel.ListBean, BaseViewHolder> {
  public ServeOrderAdapter(@Nullable List<ServeOrderModel.ListBean> data) {
    super(R.layout.item_serve_order_view, data);
  }

  @Override protected void convert(BaseViewHolder mBaseViewHolder, ServeOrderModel.ListBean mShopTypeModel) {
      ImageView mView = mBaseViewHolder.getView(R.id.item_icon);
      Glide.with(mContext).load(mShopTypeModel.service.adImg)
              .apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
              .into(mView);
      mBaseViewHolder.setText(R.id.item_name,mShopTypeModel.service.serviceName+"");
      mBaseViewHolder.setText(R.id.item_time,mShopTypeModel.createDate+"");
      mBaseViewHolder.setText(R.id.item_money,"￥"+mShopTypeModel.amount+"");
      mBaseViewHolder.setText(R.id.item_fanli,"￥"+mShopTypeModel.rebatePv+"");
  }
}
