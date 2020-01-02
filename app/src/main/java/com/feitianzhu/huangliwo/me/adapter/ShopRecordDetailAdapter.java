package com.feitianzhu.huangliwo.me.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.ShopRecordDetailModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ShopRecordDetailAdapter extends BaseQuickAdapter<ShopRecordDetailModel.ListBean, BaseViewHolder> {
  public ShopRecordDetailAdapter(@Nullable List<ShopRecordDetailModel.ListBean> data) {
    super(R.layout.fragment_shop_detail_item, data);
  }

  @Override protected void convert(BaseViewHolder holder, ShopRecordDetailModel.ListBean item) {
    ImageView iv = holder.getView(R.id.iv_icon);
    Glide.with(mContext).load(item.getConsumePlaceImg()).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai)).into(iv);
      holder.setText(R.id.orderIdTxt,item.getUserId()+"")
              .setText(R.id.tv_date,item.getCreateDate())
              .setText(R.id.tv_percent,item.getHandleFeeRate()+"%")
              .setText(R.id.tv_money,"¥"+item.getConsumeAmount())
              .setText(R.id.handFee,"¥"+item.getHandleFee());
  }
}
