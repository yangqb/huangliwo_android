package com.feitianzhu.huangliwo.plane;

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

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiPriceInfo;
import com.feitianzhu.huangliwo.utils.DoubleUtil;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

public class PlaneDetailAdapter extends BaseMultiItemQuickAdapter<MultiPriceInfo, BaseViewHolder> {
    public PlaneDetailAdapter(@Nullable List<MultiPriceInfo> data) {
        super(data);
        addItemType(MultiPriceInfo.DOMESTIC_TYPE, R.layout.layout_plane_detail);
        addItemType(MultiPriceInfo.DOMESTIC_GO_BACK_TYPE, R.layout.layout_plane_detail);
        addItemType(MultiPriceInfo.INTERNATIONAL_TYPE, R.layout.layout_plane_detail);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiPriceInfo item) {
        helper.addOnClickListener(R.id.btn_reserve)
                .addOnClickListener(R.id.ll_rebate)
                .addOnClickListener(R.id.luggage_change_notice);

        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        if (userInfo.getAccountType() != 0) {
            helper.setGone(R.id.ll_rebate, false);
            helper.setGone(R.id.vip_rebate, true);
        } else {
            helper.setGone(R.id.ll_rebate, true);
            helper.setGone(R.id.vip_rebate, false);
        }

        if (helper.getItemViewType() == MultiPriceInfo.DOMESTIC_TYPE) {
            setSpannableString(MathUtils.subZero(String.valueOf(item.venDorsInfo.barePrice)), helper.getView(R.id.price));
            if ("A".equals(item.venDorsInfo.cabinCount)) {
                helper.setText(R.id.cabinCount, "");
                helper.setGone(R.id.cabinCount, false);
            } else if ("BCDEFGHIJKLMNOPQRSTUVWXYZ".contains(item.venDorsInfo.cabinCount)) {
                helper.setText(R.id.cabinCount, "");
                helper.setGone(R.id.cabinCount, false);
            } else {
                helper.setText(R.id.cabinCount, "剩" + item.venDorsInfo.cabinCount + "张");
                helper.setGone(R.id.cabinCount, true);
            }

            if (item.venDorsInfo.cabinType == 0) {
                helper.setText(R.id.luggage_change_notice, "经济舱  退改详情>");
            } else if (item.venDorsInfo.cabinType == 1) {
                helper.setText(R.id.luggage_change_notice, "头等舱  退改详情>");
            } else if (item.venDorsInfo.cabinType == 2) {
                helper.setText(R.id.luggage_change_notice, "商务舱  退改详情>");
            } else if (item.venDorsInfo.cabinType == 3) {
                helper.setText(R.id.luggage_change_notice, "济舱精选  退改详情>");
            } else if (item.venDorsInfo.cabinType == 4) {
                helper.setText(R.id.luggage_change_notice, "经济舱y舱  退改详情>");
            } else if (item.venDorsInfo.cabinType == 5) {
                helper.setText(R.id.luggage_change_notice, "经超值头等舱  退改详情>");
            } else {
                helper.setText(R.id.luggage_change_notice, "未配置舱位  退改详情>");
            }
            helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.venDorsInfo.zk, item.venDorsInfo.barePrice))));
            helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.venDorsInfo.zk, item.venDorsInfo.barePrice))));
        } else if (helper.getItemViewType() == MultiPriceInfo.INTERNATIONAL_TYPE) {
            setSpannableString(MathUtils.subZero(String.valueOf(item.internationalPriceInfo.price)), helper.getView(R.id.price));
            helper.setText(R.id.cabinCount, "");
            helper.setGone(R.id.cabinCount, false);
            helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalPriceInfo.zk, item.internationalPriceInfo.price))));
            helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalPriceInfo.zk, item.internationalPriceInfo.price))));
            if (item.internationalPriceInfo.cPrice == 0) {
                if ("economy".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童婴儿不可订经济舱退改¥111>");
                } else if ("first".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童婴儿不可订头等舱退改¥111>");
                } else if ("business".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童婴儿不可订商务舱退改¥111>");
                } else {
                    helper.setText(R.id.luggage_change_notice, "儿童婴儿不可订未配置舱位退改¥111>");
                }
            } else {
                if ("economy".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童¥" + MathUtils.subZero(String.valueOf(item.internationalPriceInfo.cPrice)) + "婴儿不可订经济舱退改¥111>");
                } else if ("first".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童¥" + MathUtils.subZero(String.valueOf(item.internationalPriceInfo.cPrice)) + "婴儿不可订头等舱退改¥111>");
                } else if ("business".equals(item.internationalPriceInfo.cabinLevel)) {
                    helper.setText(R.id.luggage_change_notice, "儿童¥" + MathUtils.subZero(String.valueOf(item.internationalPriceInfo.cPrice)) + "婴儿不可订商务舱退改¥111>");
                } else {
                    helper.setText(R.id.luggage_change_notice, "儿童¥" + MathUtils.subZero(String.valueOf(item.internationalPriceInfo.cPrice)) + "婴儿不可订未配置舱位退改¥111>");
                }
            }
        } else {
            setSpannableString(MathUtils.subZero(String.valueOf(item.goBackVendors.barePrice)), helper.getView(R.id.price));
            helper.setText(R.id.cabinCount, "");
            helper.setGone(R.id.cabinCount, false);
            helper.setText(R.id.luggage_change_notice, item.goBackVendors.cabinDesc + "  退改详情>");
            helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.goBackVendors.zk, item.goBackVendors.barePrice))));
            helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.goBackVendors.zk, item.goBackVendors.barePrice))));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
