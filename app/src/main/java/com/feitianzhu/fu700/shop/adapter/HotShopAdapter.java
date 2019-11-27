package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.RecommndShopModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class HotShopAdapter extends BaseQuickAdapter<RecommndShopModel.ListEntity, BaseViewHolder> {
    public HotShopAdapter(@Nullable List<RecommndShopModel.ListEntity> data) {
        super(R.layout.hot_shop_item, data);
    }

    @Override
    protected void convert(BaseViewHolder mBaseViewHolder, RecommndShopModel.ListEntity mShopTypeModel) {
        ImageView mView = mBaseViewHolder.getView(R.id.item_icon);
        Glide.with(mContext).load(mShopTypeModel.merchantHeadImg).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate()).into(mView);
        mBaseViewHolder.setText(R.id.item_name,mShopTypeModel.merchantName);
//        mBaseViewHolder.setText(R.id.item_juli,mShopTypeModel.distince/1000+"km");
//        mBaseViewHolder.setText(R.id.item_address,mShopTypeModel.provinceName+mShopTypeModel.cityName+"");
        mBaseViewHolder.setText(R.id.item_msg,mShopTypeModel.dtlAddr+"");
    }
}
