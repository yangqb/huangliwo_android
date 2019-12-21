package com.feitianzhu.fu700.shop.adapter;

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
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ShoppingCartMode;
import com.feitianzhu.fu700.view.AmountView;
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
public class ShoppingCartAdapter extends BaseQuickAdapter<ShoppingCartMode, BaseViewHolder> {
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

    public ShoppingCartAdapter(@Nullable List<ShoppingCartMode> data) {
        super(R.layout.shopping_cart_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShoppingCartMode item) {
        str1 = "¥ ";
        amountView = ((AmountView) helper.getView(R.id.amount_view));
        amountView.setGoods_storage(50);
        amountView.setEditText(item.getCount() + "");
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAmountChange(View view, int count) {
                shopCount = count;
                if (mListener != null) {
                    mListener.getGoodsAmount(helper.getAdapterPosition(), shopCount);
                }
            }
        });

        helper.setText(R.id.summary, item.getAttributeVal());
        if (item.isSelect()) {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_02quan);
        } else {
            helper.setBackgroundRes(R.id.select_img, R.mipmap.g07_01quan);
        }
        helper.addOnClickListener(R.id.delete)
                .addOnClickListener(R.id.summary)
                .addOnClickListener(R.id.select_goods);
       /* Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));*/

        String price = String.format(Locale.getDefault(), "%.2f", item.getPrice());
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
