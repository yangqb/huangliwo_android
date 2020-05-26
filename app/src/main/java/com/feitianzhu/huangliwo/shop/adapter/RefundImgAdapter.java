package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.shop.adapter
 * user: yangqinbo
 * date: 2020/5/26
 * time: 20:43
 * email: 694125155@qq.com
 */
public class RefundImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RefundImgAdapter(List<String> list) {
        super(R.layout.item_refund_img, list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item)
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.imageView));
    }


}
