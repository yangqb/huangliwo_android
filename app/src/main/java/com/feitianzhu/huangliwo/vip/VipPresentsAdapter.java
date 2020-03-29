package com.feitianzhu.huangliwo.vip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.PresentsModel;
import com.feitianzhu.huangliwo.model.VipGifListInfo;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.fu700.vip
 * user: yangqinbo
 * date: 2019/12/30
 * time: 14:35
 * email: 694125155@qq.com
 */
public class VipPresentsAdapter extends BaseQuickAdapter<VipGifListInfo.VipGifModel, BaseViewHolder> {
    public VipPresentsAdapter(@Nullable List<VipGifListInfo.VipGifModel> data) {
        super(R.layout.layout_vip_presents, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VipGifListInfo.VipGifModel item) {

        helper.setText(R.id.merchants_name, item.merchantName);
        helper.setText(R.id.distance, item.distance);
        helper.setText(R.id.gif_name, item.giftName);
        helper.setText(R.id.price, "价值¥" + String.format(Locale.getDefault(), "%.2f", item.price));
        if (item.isGet == 0) {
            helper.setBackgroundRes(R.id.button, R.drawable.shape_fed428_r5);
            helper.setText(R.id.button, "领取");
            helper.setTextColor(R.id.button, mContext.getResources().getColor(R.color.color_333333));
        } else {
            helper.setText(R.id.button, "已领取");
            helper.setBackgroundRes(R.id.button, R.drawable.shape_999999_r5);
            helper.setTextColor(R.id.button, mContext.getResources().getColor(R.color.white));
        }
        helper.addOnClickListener(R.id.button);

        /*Glide.with(mContext).load(item.getGiftImg()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((ImageView) helper.getView(R.id.imageView));
        helper.setText(R.id.tvTitle, item.getGiftTitle());*/
    }
}
