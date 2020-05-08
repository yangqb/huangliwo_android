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
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.DoubleUtil;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.home.adapter
 * user: yangqinbo
 * date: 2020/5/8
 * time: 18:32
 * email: 694125155@qq.com
 */
public class HotGoodsAdapter2 extends BaseQuickAdapter<BaseGoodsListBean, BaseViewHolder> {
    public HotGoodsAdapter2(@Nullable List<BaseGoodsListBean> data) {
        super(R.layout.item_hot_good, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseGoodsListBean item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        Glide.with(mContext).load(item.getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((ImageView) helper.getView(R.id.goodsImg));
        helper.setText(R.id.goodsName, item.getGoodsName());
        String rebatePv = String.valueOf(item.getRebatePv());
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
        setSpannableString(MathUtils.subZero(String.valueOf(item.getPrice())), helper.getView(R.id.price));

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
