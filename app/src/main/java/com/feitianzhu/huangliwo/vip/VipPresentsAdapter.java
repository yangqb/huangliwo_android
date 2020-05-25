package com.feitianzhu.huangliwo.vip;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.VipGifListInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

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
        setSpannableString(MathUtils.subZero(String.valueOf(item.price)), helper.getView(R.id.price));
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

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "价值 ¥ ";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(14, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);
    }
}
