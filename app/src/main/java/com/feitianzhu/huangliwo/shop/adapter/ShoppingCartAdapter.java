package com.feitianzhu.huangliwo.shop.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.ShoppingCartModel;
import com.feitianzhu.huangliwo.view.AmountView;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/17
 * time: 14:04
 * email: 694125155@qq.com
 */
public class ShoppingCartAdapter extends BaseQuickAdapter<ShoppingCartModel.CartGoodsModel, BaseViewHolder> {
    private int shopCount = 1;
    private String str1;
    private AmountView amountView;

    private OnGoodsAmountListener mListener;

    public interface OnGoodsAmountListener {
        void getGoodsAmount(int pos, int count);
    }

    public void setOnGoodsAmountListener(OnGoodsAmountListener listener) {
        this.mListener = listener;
    }

    public ShoppingCartAdapter(@Nullable List<ShoppingCartModel.CartGoodsModel> data) {
        super(R.layout.shopping_cart_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShoppingCartModel.CartGoodsModel item) {
        str1 = "¥ ";
        /*amountView = ((AmountView) helper.getView(R.id.amount_view));
        amountView.setGoods_storage(50);
        amountView.setEditText(item.goodsCount + "");
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAmountChange(View view, int count) {
                shopCount = count;
                if (mListener != null) {
                    mListener.getGoodsAmount(helper.getAdapterPosition(), shopCount);
                }
            }
        });*/
        helper.setText(R.id.etAmount, item.goodsCount + "");
        if (TextUtils.isEmpty(item.speciName)) {
            helper.setVisible(R.id.summary, false);
        } else {
            helper.setVisible(R.id.summary, true);
        }
        helper.setText(R.id.summary, item.speciName);
        helper.setText(R.id.name, item.title);
        Glide.with(mContext).load(item.goodsImg).apply(new RequestOptions()
                .placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));
        if (item.checks == 1) {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_02quan);
        } else {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_01quan);
        }
        helper.addOnClickListener(R.id.delete)
                .addOnClickListener(R.id.summary)
                .addOnClickListener(R.id.select_goods)
                .addOnClickListener(R.id.btnDecrease)
                .addOnClickListener(R.id.btnIncrease)
                .addOnClickListener(R.id.etAmount);

       /* Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));*/

        String price = String.format(Locale.getDefault(), "%.2f", item.price);
        setSpannableString(price, helper.getView(R.id.goods_price));
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str3);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
        span1.setSpan(new AbsoluteSizeSpan(12, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan2, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
        span2.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span2);
    }
}
