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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapter extends BaseQuickAdapter<MultipleItem, BaseViewHolder> {
    public RightAdapter(@Nullable List<MultipleItem> data) {
        super(R.layout.item_recommended_goods, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultipleItem item) {
        BaseGoodsListBean goodsListBean = item.getGoodsListBean();
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        Glide.with(mContext).load(goodsListBean.getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((ImageView) helper.getView(R.id.goodsImg));
        helper.setText(R.id.goodsName, goodsListBean.getGoodsName());
        helper.setText(R.id.goodsSummary, goodsListBean.getSummary());
        String rebatePv = String.valueOf(goodsListBean.getRebatePv());
        helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(rebatePv));
        helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(rebatePv));
        if (userInfo.getAccountType() != 0) {
            helper.setGone(R.id.ll_rebate, false);
            helper.setGone(R.id.vip_rebate, true);
        } else {
            helper.setGone(R.id.ll_rebate, true);
            helper.setGone(R.id.vip_rebate, false);
        }
        helper.addOnClickListener(R.id.ll_rebate);
        setSpannableString(MathUtils.subZero(String.valueOf(goodsListBean.getPrice())), helper.getView(R.id.price));
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#E63411"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#E63411"));
        span3.setSpan(new AbsoluteSizeSpan(16, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}