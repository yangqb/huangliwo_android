package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.view
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/26 0026 下午 2:39
 */
public class ShopInfoDetailImgAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public ShopInfoDetailImgAdapter(@Nullable List<Integer> data) {
        super(R.layout.shop_info_detail_img, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        RoundedImageView imageView = helper.getView(R.id.img);
        Glide.with(mContext).load(R.mipmap.e01_16taocantupian1).into(imageView);
    }
}
