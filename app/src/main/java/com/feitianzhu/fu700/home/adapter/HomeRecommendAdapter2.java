package com.feitianzhu.fu700.home.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.home.entity.HomeEntity;
import com.feitianzhu.fu700.home.entity.ShopAndMerchants;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;


/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:46
 */
public class HomeRecommendAdapter2 extends BaseMultiItemQuickAdapter<ShopAndMerchants, BaseViewHolder> {

    public HomeRecommendAdapter2(List<ShopAndMerchants> data) {
        super(data);
        addItemType(ShopAndMerchants.TYPE_SERIES, R.layout.home_categoty_item);
        addItemType(ShopAndMerchants.TYPE_PESALE, R.layout.home_categoty_item);
    }

    @Override
    public void convert(@NonNull BaseViewHolder holder, ShopAndMerchants item) {
        switch (item.getItemType()) {
            case ShopAndMerchants.TYPE_SERIES:
                BaseGoodsListBean shopsList = item.getShopsList();
                holder.setText(R.id.tv_category, shopsList.getGoodsName());
                holder.setText(R.id.summary, shopsList.getSummary());
                setSpannableString(String.format(Locale.getDefault(), "%.2f", shopsList.getPrice()), holder.getView(R.id.price));
                holder.setVisible(R.id.ll_price, true);
                holder.setText(R.id.tv_benefit, "让利" + shopsList.getRebatePv() + "元");
                holder.setVisible(R.id.address, false);
                if (shopsList.getGoodsImg() != null) {
                    Glide.with(mContext).load(shopsList.getGoodsImg()).apply(RequestOptions.errorOf(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) holder.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(R.mipmap.g10_04weijiazai).into((RoundedImageView) holder.getView(R.id.image));
                }
                break;
            case ShopAndMerchants.TYPE_PESALE:
                HomeEntity.RecommendListBean recommendListBean = item.getRecommendListBean();
                holder.setText(R.id.tv_category, recommendListBean.merchantName);
                holder.setText(R.id.address, "");
                holder.setVisible(R.id.ll_price, false);
                holder.setVisible(R.id.address, true);
                if (recommendListBean.merchantHeadImg != null) {
                    Glide.with(mContext).load(recommendListBean.merchantHeadImg).apply(RequestOptions.errorOf(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) holder.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(R.mipmap.g10_04weijiazai).into((RoundedImageView) holder.getView(R.id.image));
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(16, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
