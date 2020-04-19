package com.feitianzhu.huangliwo.home.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.utils.DoubleUtil;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.home.adapter
 * user: yangqinbo
 * date: 2020/4/1
 * time: 19:22
 * email: 694125155@qq.com
 */
public class HotGoodsAdapter extends BaseQuickAdapter<BaseGoodsListBean, BaseViewHolder> {
    public HotGoodsAdapter(@Nullable List<BaseGoodsListBean> data) {
        super(R.layout.layout_hot_goods, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseGoodsListBean item) {
        Glide.with(mContext).load(item.getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((ImageView) helper.getView(R.id.goodsImg));
        helper.setText(R.id.goodsName, item.getGoodsName());
        setSpannableString(MathUtils.subZero(String.valueOf(DoubleUtil.sub(item.getPrice(), item.getRebatePv()))), helper.getView(R.id.vip_price));
        if (helper.getAdapterPosition() == 0) {
            helper.setBackgroundRes(R.id.item, R.drawable.shape_hot_one);
        } else if (helper.getAdapterPosition() == 1) {
            helper.setBackgroundRes(R.id.item, R.drawable.shape_hot_two);
        } else if (helper.getAdapterPosition() == 2) {
            helper.setBackgroundRes(R.id.item, R.drawable.shape_hot_three);
        } else {
            helper.setBackgroundRes(R.id.item, R.drawable.shape_hot_four);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FE522B"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FE522B"));
        span3.setSpan(new AbsoluteSizeSpan(14, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
