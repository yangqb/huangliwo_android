package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.SFBaseAdapter;
import com.feitianzhu.huangliwo.model.ShopsService;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ShopsServeAdapter extends SFBaseAdapter<ShopsService.ListEntity, BaseViewHolder> {
  public ShopsServeAdapter(@Nullable List<ShopsService.ListEntity> data) {
    super(R.layout.shops_item_serve, data);
  }

  @Override
  protected void convert(BaseViewHolder mBaseViewHolder, ShopsService.ListEntity mShopTypeModel) {
    if ("0".equals(mShopTypeModel.status)) {
      ImageView mView = mBaseViewHolder.getView(R.id.item_state);
      mView.setVisibility(View.VISIBLE);
      Glide.with(mContext).load(R.mipmap.icon_shenhezhong).into(mView);
    } else if("-1".equals(mShopTypeModel.status)){
      ImageView mView = mBaseViewHolder.getView(R.id.item_state);
      mView.setVisibility(View.VISIBLE);
      Glide.with(mContext).load(R.mipmap.icon_shenheshibai).into(mView);
    }else{
      ImageView mView = mBaseViewHolder.getView(R.id.item_state);
      mView.setVisibility(View.GONE);
    }
    mBaseViewHolder.setText(R.id.item_name, "" + mShopTypeModel.serviceName);
    mBaseViewHolder.setText(R.id.item_price, "￥" + mShopTypeModel.price);
    mBaseViewHolder.setText(R.id.item_rebate, mShopTypeModel.rebate + "pv");
    ImageView mView = mBaseViewHolder.getView(R.id.item_icon);
    /*
    * 服务器没有one_photo字段返回
    * */
    /*Glide.with(mContext)
        .load(mShopTypeModel.one_photo)
        .dontAnimate()
        .placeholder(R.mipmap.pic_fuwutujiazaishibai)
        .into(mView);*/

    Glide.with(mContext)
            .load(mShopTypeModel.adImg)
            .apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
            .into(mView);
  }
}
