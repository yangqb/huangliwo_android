package com.feitianzhu.huangliwo.shop.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.GlideUtils;

import java.util.List;

public class ShopsDetailImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ShopsDetailImgAdapter(@Nullable List<String> data) {
        super(R.layout.layout_goods_detail_img, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.imageView);
        Glide.with(mContext).load(item).apply(new RequestOptions().error(R.mipmap.g10_03weijiazai).placeholder(R.mipmap.g10_03weijiazai)).into(GlideUtils.getImageView((Activity) mContext, item, imageView));
    }
}
