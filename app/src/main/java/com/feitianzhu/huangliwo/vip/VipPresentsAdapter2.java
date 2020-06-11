package com.feitianzhu.huangliwo.vip;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.VipGifListInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.vip
 * user: yangqinbo
 * date: 2020/4/8
 * time: 15:20
 * email: 694125155@qq.com
 */
public class VipPresentsAdapter2 extends BaseQuickAdapter<VipGifListInfo.VipPresentsModel, BaseViewHolder> {
    public VipPresentsAdapter2(@Nullable List<VipGifListInfo.VipPresentsModel> data) {
        super(R.layout.layout_vip_present, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VipGifListInfo.VipPresentsModel item) {
        helper.setText(R.id.present_name, item.giftName);
        setSpannableString(MathUtils.subZero(String.valueOf(item.discountPrice)), helper.getView(R.id.discountPrice));
        setSpannableString2(MathUtils.subZero(String.valueOf(item.originalPrice)), helper.getView(R.id.originalPrice));
        Glide.with(mContext).load(item.giftImg).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((ImageView) helper.getView(R.id.present_img));
        if (item.isGet == 1) {
            helper.setBackgroundRes(R.id.item, R.color.color_fde29a);
            helper.setBackgroundRes(R.id.select_img, R.mipmap.vip_selected);
        } else {
            helper.setBackgroundRes(R.id.item, R.color.color_fde29a);
            helper.setBackgroundRes(R.id.select_img, R.mipmap.vip_select);
        }
        helper.addOnClickListener(R.id.rl_select);
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥ ";
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

    @SuppressLint("SetTextI18n")
    private void setSpannableString2(String str3, TextView view) {
        String str1 = "¥ ";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#999999"));
        span1.setSpan(new AbsoluteSizeSpan(9, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#999999"));
        span3.setSpan(new AbsoluteSizeSpan(9, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

    }
}
