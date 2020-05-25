package com.feitianzhu.huangliwo.home.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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
import com.feitianzhu.huangliwo.home.entity.ShopAndMerchants;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;


/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:46
 */
public class HomeRecommendAdapter2 extends BaseMultiItemQuickAdapter<ShopAndMerchants, BaseViewHolder> {

    public HomeRecommendAdapter2(List<ShopAndMerchants> data) {
        super(data);
        addItemType(ShopAndMerchants.TYPE_GOODS, R.layout.home_categoty_item);
        addItemType(ShopAndMerchants.TYPE_MERCHANTS, R.layout.home_categoty_item);
    }

    @Override
    public void convert(@NonNull BaseViewHolder holder, ShopAndMerchants item) {
        switch (item.getItemType()) {
            case ShopAndMerchants.TYPE_GOODS:
                BaseGoodsListBean shopsList = item.getShopsList();
                holder.setText(R.id.tv_category, shopsList.getGoodsName());
                holder.setText(R.id.summary, shopsList.getSummary());
                setSpannableString(MathUtils.subZero(String.valueOf(shopsList.getPrice())), holder.getView(R.id.price));
                holder.setVisible(R.id.ll_price, true);
                String rebatePv = String.valueOf(shopsList.getRebatePv());
                holder.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(rebatePv));
                holder.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(rebatePv));
                MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
                if (userInfo.getAccountType() != 0) {
                    holder.setVisible(R.id.ll_rebate, false);
                    holder.setVisible(R.id.vip_rebate, true);
                } else {
                    holder.setVisible(R.id.ll_rebate, true);
                    holder.setVisible(R.id.vip_rebate, false);
                }
                holder.addOnClickListener(R.id.ll_rebate);
                holder.setVisible(R.id.address, false);
                if (shopsList.getGoodsImg() != null) {
                    Glide.with(mContext).load(shopsList.getGoodsImg()).apply(RequestOptions.errorOf(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) holder.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(R.mipmap.g10_04weijiazai).apply(new RequestOptions().dontAnimate()).into((RoundedImageView) holder.getView(R.id.image));
                }
                break;
            case ShopAndMerchants.TYPE_MERCHANTS:
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
