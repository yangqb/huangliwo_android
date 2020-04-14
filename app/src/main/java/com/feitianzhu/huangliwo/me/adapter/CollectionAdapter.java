package com.feitianzhu.huangliwo.me.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CollectionInfo;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiCollectionModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

public class CollectionAdapter extends BaseItemDraggableAdapter<CollectionInfo.CollectionModel, BaseViewHolder> {
    public CollectionAdapter(List<CollectionInfo.CollectionModel> data) {
        super(R.layout.layout_collection_goods, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CollectionInfo.CollectionModel item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        Glide.with(mContext).load(item.goodsImg)
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.goodsImg));
        helper.setText(R.id.goodsName, item.title);
        helper.setText(R.id.describe, item.goodsName);
        if (userInfo.getAccountType() != 0) {
            helper.setVisible(R.id.ll_rebate, false);
            helper.setVisible(R.id.vip_rebate, true);
        } else {
            helper.setVisible(R.id.ll_rebate, true);
            helper.setVisible(R.id.vip_rebate, false);
        }
        if (item.type == 1) {
            String discount = String.valueOf((100 - item.rebatePv * 100));
            helper.setText(R.id.tv_rebate, "返" + MathUtils.subZero(discount) + "%");
            helper.setText(R.id.vip_rebate, "返" + MathUtils.subZero(discount) + "%");
            helper.setGone(R.id.price,false);
        } else {
            String rebatePv = String.format(Locale.getDefault(), "%.2f", item.rebatePv);
            helper.setText(R.id.tv_rebate, "返¥" + MathUtils.subZero(rebatePv));
            helper.setText(R.id.vip_rebate, "返" + MathUtils.subZero(rebatePv));
            setSpannableString(MathUtils.subZero(String.valueOf(item.price)), helper.getView(R.id.price));
            helper.setGone(R.id.price,true);
        }
        helper.addOnClickListener(R.id.ll_rebate);
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
