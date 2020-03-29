package com.feitianzhu.huangliwo.shop.adapter;

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
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.huangliwo.shop.adapter
 * user: yangqinbo
 * date: 2020/1/6
 * time: 15:34
 * email: 694125155@qq.com
 */
public class NewYearShoppingAdapter extends BaseQuickAdapter<BaseGoodsListBean, BaseViewHolder> {
    public NewYearShoppingAdapter(@Nullable List<BaseGoodsListBean> data) {
        super(R.layout.layout_new_year_shopping, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseGoodsListBean item) {
        Glide.with(mContext).load(item.getGoodsImg()).apply(new RequestOptions()
                .error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.imageView));
        helper.setText(R.id.goodsName, item.getGoodsName());
        helper.setText(R.id.tv_introduce, item.getSummary());
        setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getPrice()), helper.getView(R.id.tv_price));
        String rebatePv = String.format(Locale.getDefault(), "%.2f", item.getRebatePv());
        helper.setText(R.id.tv_rebate, "返¥" + rebatePv);
        helper.setText(R.id.vip_rebate,"返¥" + rebatePv);
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        if (userInfo.getAccountType() != 0) {
            helper.setVisible(R.id.ll_rebate, false);
            helper.setVisible(R.id.vip_rebate, true);
        } else {
            helper.setVisible(R.id.ll_rebate, true);
            helper.setVisible(R.id.vip_rebate, false);
        }
        helper.addOnClickListener(R.id.ll_rebate);
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥ ";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#B71D23"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#B71D23"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
