package com.feitianzhu.huangliwo.shop.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.ShoppingCartModel;

import java.util.List;
import java.util.Locale;

public class SettlementShoppingAdapter extends BaseQuickAdapter<ShoppingCartModel.CartGoodsModel, BaseViewHolder> {
    private String str2 = "¥";
    private OnEditListener listener;

    public interface OnEditListener {
        void edit(String text, int postion);
    }

    public void setOnEditListener(OnEditListener mListener) {
        this.listener = mListener;
    }

    public SettlementShoppingAdapter(@Nullable List<ShoppingCartModel.CartGoodsModel> data) {
        super(R.layout.layout_settlement_shop, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShoppingCartModel.CartGoodsModel item) {
        helper.setIsRecyclable(false);
        Glide.with(mContext).load(item.goodsImg).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai))
                .into((ImageView) helper.getView(R.id.image));
        helper.setText(R.id.name, item.title);
        helper.setText(R.id.summary, item.speciName);
        String price = String.format(Locale.getDefault(), "%.2f", item.price);
        helper.setText(R.id.count, "×" + item.goodsCount);
        helper.setText(R.id.postage, "¥" + String.format(Locale.getDefault(), "%.2f", item.postage));
        helper.setText(R.id.total_amount, String.format(Locale.getDefault(), "%.2f", item.price * item.goodsCount + item.postage));
        setSpan(helper.getView(R.id.tv_price), price);

        EditText editText = helper.getView(R.id.editRemark);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null) {
                    listener.edit(s.toString(), helper.getAdapterPosition());
                }
            }
        });
    }


    public void setSpan(TextView textView, String price) {
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(price);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.append(span4);
        textView.append(span5);
    }
}
