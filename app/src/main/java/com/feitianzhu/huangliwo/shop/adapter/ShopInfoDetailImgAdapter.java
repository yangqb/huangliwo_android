package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.view
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/26 0026 下午 2:39
 */
public class ShopInfoDetailImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShopInfoDetailImgAdapter(@Nullable List<String> data) {
        super(R.layout.shop_info_detail_img, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.img));
    }
}
