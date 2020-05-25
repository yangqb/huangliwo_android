package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.SFBaseAdapter;
import com.feitianzhu.huangliwo.model.ShopsIndex;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ShopHomeAdapter extends SFBaseAdapter<ShopsIndex.NearByMerchantListBean, BaseViewHolder> {
    public ShopHomeAdapter(@Nullable List<ShopsIndex.NearByMerchantListBean> data) {
        super(R.layout.shop_item_home, data);
    }

    @Override
    protected void convert(BaseViewHolder mBaseViewHolder, ShopsIndex.NearByMerchantListBean mShopTypeModel) {
        ImageView mView = mBaseViewHolder.getView(R.id.item_icon);
        Glide.with(mContext).load(mShopTypeModel.merchantHeadImg)
                .apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
                .into(mView);
        mBaseViewHolder.setText(R.id.item_name,mShopTypeModel.merchantName);
        mBaseViewHolder.setText(R.id.item_juli,mShopTypeModel.distinceStr+"");
        mBaseViewHolder.setText(R.id.item_address,mShopTypeModel.provinceName+mShopTypeModel.cityName+"");
        mBaseViewHolder.setText(R.id.item_msg,mShopTypeModel.dtlAddr+"");
    }
}
