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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/13
 * time: 17:45
 * email: 694125155@qq.com
 */
public class SetMealListAdapter extends BaseQuickAdapter<SetMealInfo, BaseViewHolder> {
    public SetMealListAdapter(@Nullable List<SetMealInfo> data) {
        super(R.layout.layout_setmeal_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SetMealInfo item) {
        helper.setText(R.id.setMealName, item.getSmName());
        setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getPrice()), helper.getView(R.id.setMealPrice));
        if (item.getIsShelf() == 0) {
            helper.setText(R.id.tvStatus, "上架");
        } else {
            helper.setText(R.id.tvStatus, "下架");
        }
        String[] imgs = item.getImgs().split(",");
        if (item.getImgs().contains(",")) {
            Glide.with(mContext).load(imgs[0]).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.imageView));
        } else {
            Glide.with(mContext).load(item.getImgs()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.imageView));
        }
        helper.addOnClickListener(R.id.tvStatus);
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(14, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
