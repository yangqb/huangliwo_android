package com.feitianzhu.huangliwo.shop.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public RightAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.shop_right_item);
        addItemType(MultipleItem.IMG, R.layout.shop_right_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                // helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                helper.setText(R.id.tv_category, item.getGoodsListBean().getGoodsName());
                setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getGoodsListBean().getPrice()), helper.getView(R.id.price));
                helper.setText(R.id.tvContent, item.getGoodsListBean().getSummary());
                String rebatePv = String.format(Locale.getDefault(), "%.2f", item.getGoodsListBean().getRebatePv());
                helper.setText(R.id.tv_rebate, "省¥" + rebatePv);
                if (item.getGoodsListBean().getRebatePv() == 0) {
                    helper.setVisible(R.id.ll_rebate, false);
                } else {
                    helper.setVisible(R.id.ll_rebate, true);
                }
                helper.addOnClickListener(R.id.ll_rebate);
                Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));
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
