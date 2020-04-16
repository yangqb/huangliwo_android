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
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
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
        addItemType(MultipleItem.MERCHANTS, R.layout.shop_right_item);
        addItemType(MultipleItem.GOODS, R.layout.shop_right_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        switch (helper.getItemViewType()) {
            case MultipleItem.MERCHANTS:
                helper.setText(R.id.merchantsName, item.getMerchantsModel().getMerchantName());
                if (item.getMerchantsModel().getIntroduce() != null) {
                    helper.setText(R.id.merchants_introduce, item.getMerchantsModel().getIntroduce());
                }
                helper.setText(R.id.distance, item.getMerchantsModel().getDistinceStr());
                Glide.with(mContext).load(item.getMerchantsModel().getLogo())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
                String discount = String.valueOf((100 - item.getMerchantsModel().getDiscount() * 100));
                helper.setText(R.id.tv_rebate, "返" + MathUtils.subZero(discount) + "%");
                helper.setText(R.id.vip_rebate, "返" + MathUtils.subZero(discount) + "%");
                if (userInfo.getAccountType() != 0) {
                    helper.setVisible(R.id.ll_rebate, false);
                    helper.setVisible(R.id.vip_rebate, true);
                } else {
                    helper.setVisible(R.id.ll_rebate, true);
                    helper.setVisible(R.id.vip_rebate, false);
                }
               /* if (item.getMerchantsModel().getDiscount() == 100) {
                    helper.setVisible(R.id.ll_rebate, false);
                } else {
                    helper.setVisible(R.id.ll_rebate, true);
                }*/
                helper.addOnClickListener(R.id.ll_rebate);
                break;
            case MultipleItem.GOODS:
                helper.setText(R.id.tv_category, item.getGoodsListBean().getGoodsName());
                setSpannableString(MathUtils.subZero(String.valueOf(item.getGoodsListBean().getPrice())), helper.getView(R.id.price));
                helper.setText(R.id.tvContent, item.getGoodsListBean().getSummary());
                String rebatePv = String.format(Locale.getDefault(), "%.2f", item.getGoodsListBean().getRebatePv());
                helper.setText(R.id.tv_rebate, "返¥" + MathUtils.subZero(rebatePv));
                helper.setText(R.id.vip_rebate, "返¥" + MathUtils.subZero(rebatePv));
                if (userInfo.getAccountType() != 0) {
                    helper.setVisible(R.id.ll_rebate, false);
                    helper.setVisible(R.id.vip_rebate, true);
                } else {
                    helper.setVisible(R.id.ll_rebate, true);
                    helper.setVisible(R.id.vip_rebate, false);
                }
               /* if (item.getGoodsListBean().getRebatePv() == 0) {
                    helper.setVisible(R.id.ll_rebate, false);
                } else {
                    helper.setVisible(R.id.ll_rebate, true);
                }*/
                helper.addOnClickListener(R.id.ll_rebate);
                Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
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
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
