package com.feitianzhu.huangliwo.pushshop.adapter;

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
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;

import java.util.List;
import java.util.Locale;


/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/3
 * time: 17:02
 * email: 694125155@qq.com
 */
public class SelfMerchantsOrderAdapter extends BaseMultiItemQuickAdapter<SelfMerchantsModel, BaseViewHolder> {
    public SelfMerchantsOrderAdapter(@Nullable List<SelfMerchantsModel> data) {
        super(data);
        addItemType(SelfMerchantsModel.RULES_TYPE, R.layout.layout_merchants_rules_order);
        addItemType(SelfMerchantsModel.ORDER_TYPE, R.layout.layout_merchants_setmeal_order);
        addItemType(SelfMerchantsModel.GIFT_ORDER_TYPE, R.layout.layout_merchants_setmeal_order);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelfMerchantsModel item) {
        if (helper.getItemViewType() == SelfMerchantsModel.RULES_TYPE) {
            helper.setText(R.id.setMealName, item.getMerchantsEarnRulesModel().getSmName());
            helper.setText(R.id.phone, item.getMerchantsEarnRulesModel().getPhone());
            helper.setText(R.id.name, item.getMerchantsEarnRulesModel().getNickName());
            helper.setText(R.id.date, item.getMerchantsEarnRulesModel().getCreateTime());
            setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getMerchantsEarnRulesModel().getAmount()), helper.getView(R.id.amount));
            if (item.getMerchantsEarnRulesModel().getType() == 1) {
                helper.setText(R.id.tvStatus, "到店消费");
            } else {
                helper.setText(R.id.tvStatus, "线上消费");
            }
        } else if (helper.getItemViewType() == SelfMerchantsModel.ORDER_TYPE) {
            helper.setText(R.id.setMealName, item.getMerchantsEarnRulesModel().getSmName());
            helper.setText(R.id.phone, item.getMerchantsEarnRulesModel().getPhone());
            helper.setText(R.id.name, item.getMerchantsEarnRulesModel().getNickName());
            helper.setText(R.id.date, item.getMerchantsEarnRulesModel().getCreateTime());
            setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getMerchantsEarnRulesModel().getAmount()), helper.getView(R.id.amount));
            if (item.getMerchantsEarnRulesModel().getStatus() == 1) {
                helper.setText(R.id.tvStatus, "未支付");
            } else if (item.getMerchantsEarnRulesModel().getStatus() == 2) {
                helper.setText(R.id.tvStatus, "待录单");
            } else if (item.getMerchantsEarnRulesModel().getStatus() == 3) {
                helper.setText(R.id.tvStatus, "退款中");
            } else if (item.getMerchantsEarnRulesModel().getStatus() == 4) {
                helper.setText(R.id.tvStatus, "已退款");
            } else if (item.getMerchantsEarnRulesModel().getStatus() == 5) {
                helper.setText(R.id.tvStatus, "已取消");
            } else if (item.getMerchantsEarnRulesModel().getStatus() == 6) {
                helper.setText(R.id.tvStatus, "已到账");
            }
        } else {
            helper.setText(R.id.setMealName, item.getMerchantGiftOrderModel().giftName);
            helper.setText(R.id.phone, item.getMerchantGiftOrderModel().phone);
            helper.setText(R.id.name, item.getMerchantGiftOrderModel().nickName);
            helper.setText(R.id.date, item.getMerchantGiftOrderModel().createTime);
            setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getMerchantGiftOrderModel().price), helper.getView(R.id.amount));
            if (item.getMerchantGiftOrderModel().isUse == 1) {
                helper.setText(R.id.tvStatus, "已录单");
            } else {
                helper.setText(R.id.tvStatus, "待录单");
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(12, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
