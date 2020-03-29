package com.feitianzhu.huangliwo.me.adapter;

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
import com.feitianzhu.huangliwo.model.WithdrawRecordInfo;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.huangliwo.me.adapter
 * user: yangqinbo
 * date: 2020/1/18
 * time: 11:46
 * email: 694125155@qq.com
 */
public class WithdrawRecordAdapter extends BaseQuickAdapter<WithdrawRecordInfo.WithdrawRecordModel, BaseViewHolder> {
    public WithdrawRecordAdapter(@Nullable List<WithdrawRecordInfo.WithdrawRecordModel> data) {
        super(R.layout.layout_withdraw_record_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WithdrawRecordInfo.WithdrawRecordModel item) {
        helper.setText(R.id.date, item.getApplyDate());
        if (item.getIsTrans() == -1) {
            helper.setTextColor(R.id.status, mContext.getResources().getColor(R.color.color_666666));
            helper.setText(R.id.status, "已取消");
            helper.setText(R.id.tvTime, "");
        } else if (item.getIsTrans() == 1) {
            helper.setText(R.id.status, "已到账");
            helper.setText(R.id.tvTime, "");
            helper.setTextColor(R.id.status, mContext.getResources().getColor(R.color.color_666666));
        } else {
            if (item.getRefuseReason() != null) {
                helper.setText(R.id.status, "提现失败");
                helper.setText(R.id.tvTime, item.getRefuseReason());
            } else {
                helper.setText(R.id.status, "取消提现");
                helper.setText(R.id.tvTime, item.getTimeDesc());
            }
            helper.setTextColor(R.id.status, mContext.getResources().getColor(R.color.color_F88D03));
        }

        if ("wx".equals(item.getChannel())) {
            helper.setText(R.id.tvType, "提现-微信");
        } else {
            helper.setText(R.id.tvType, "提现-支付宝");
        }


        setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getAmount()), helper.getView(R.id.amount));
        helper.addOnClickListener(R.id.btn_cancel);
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(14, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
