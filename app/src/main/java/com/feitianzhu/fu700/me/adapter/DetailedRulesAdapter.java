package com.feitianzhu.fu700.me.adapter;

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
import com.feitianzhu.fu700.R;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.me.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 18:50
 * email: 694125155@qq.com
 */
public class DetailedRulesAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    private String str2;
    private String amount;

    public DetailedRulesAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_detailed_rules, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        str2 = "¥ ";
        setSpannableString("333.00", helper.getView(R.id.amount));
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        view.setText("");
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span2.setSpan(new AbsoluteSizeSpan(12, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span2);
        view.append(span3);

    }
}
