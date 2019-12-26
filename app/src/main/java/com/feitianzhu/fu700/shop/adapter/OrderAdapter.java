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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends BaseQuickAdapter<GoodsOrderInfo.GoodsOrderListBean, BaseViewHolder> {
    private String str1;
    private String str2;
    private String amount;
    private String price;

    public OrderAdapter(@Nullable List<GoodsOrderInfo.GoodsOrderListBean> data) {
        super(R.layout.layout_order_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GoodsOrderInfo.GoodsOrderListBean item) {
        str1 = "合计：";
        str2 = "¥ ";
        amount = String.format(Locale.getDefault(), "%.2f", item.getAmount());
        price = String.format(Locale.getDefault(), "%.2f", item.getPrice());
        setSpannableString(amount, helper.getView(R.id.amount));
        setSpannableString2(price, helper.getView(R.id.tv_amount));
        helper.setText(R.id.specifications, item.getAttributeVal());
        helper.setText(R.id.summary, item.getGoodName());
        helper.setText(R.id.count, "×" + item.getCount());
        helper.setText(R.id.tvCount, "共" + item.getCount() + "件商品");
        helper.setText(R.id.merchantsName, item.getShopName());
        Glide.with(mContext).load(item.getGoodsImg()).apply(new RequestOptions()
                .placeholder(R.mipmap.g10_04weijiazai)
                .error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));

        helper.addOnClickListener(R.id.btn_refund);
        helper.addOnClickListener(R.id.btn_logistics);
        helper.addOnClickListener(R.id.btn_confirm_goods);
        if (item.getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
            if (item.getIsEval() == 1) { //是否评价
                helper.setText(R.id.btn_confirm_goods, "查看详情");
            } else {
                helper.setText(R.id.btn_confirm_goods, "评价");
            }
            helper.setText(R.id.tvStatus, "交易成功");
            helper.setText(R.id.btn_logistics, "删除订单");
            helper.setVisible(R.id.btn_logistics, true);
            helper.setVisible(R.id.btn_refund, false);
        } else if (item.getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
            helper.setText(R.id.tvStatus, "待付款");
            helper.setText(R.id.btn_confirm_goods, "付款");
            helper.setText(R.id.btn_logistics, " 取消订单");
            helper.setVisible(R.id.btn_confirm_goods, true);
            helper.setVisible(R.id.btn_logistics, true);
            helper.setVisible(R.id.btn_refund, false);
        } else if (item.getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
            helper.setText(R.id.tvStatus, "待发货");
            helper.setText(R.id.btn_confirm_goods, "查看物流");
            helper.setText(R.id.btn_logistics, "退款 ");
            helper.setVisible(R.id.btn_confirm_goods, true);
            helper.setVisible(R.id.btn_logistics, true);
            helper.setVisible(R.id.btn_refund, false);
        } else if (item.getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
            helper.setText(R.id.tvStatus, "待收货");
            helper.setText(R.id.btn_confirm_goods, "确认收货");
            helper.setText(R.id.btn_logistics, " 查看物流");
            helper.setText(R.id.btn_refund, "退款");
            helper.setVisible(R.id.btn_confirm_goods, true);
            helper.setVisible(R.id.btn_logistics, true);
            helper.setVisible(R.id.btn_refund, true);
        } else if (item.getStatus() == GoodsOrderInfo.TYPE_REFUND || item.getStatus() == GoodsOrderInfo.TYPE_REFUNDED || item.getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
            if (item.getStatus() == GoodsOrderInfo.TYPE_REFUND) {
                helper.setText(R.id.tvStatus, "退款中");
            } else if (item.getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
                helper.setText(R.id.tvStatus, "已退款");
            } else {
                helper.setText(R.id.tvStatus, "订单关闭");
            }
            helper.setText(R.id.btn_confirm_goods, "查看详情");
            helper.setVisible(R.id.btn_confirm_goods, true);
            helper.setVisible(R.id.btn_logistics, false);
            helper.setVisible(R.id.btn_refund, false);
        } else {
            helper.setText(R.id.tvStatus, "");
            helper.setVisible(R.id.btn_confirm_goods, false);
            helper.setVisible(R.id.btn_logistics, false);
            helper.setVisible(R.id.btn_refund, false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        span1.setSpan(new AbsoluteSizeSpan(14, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span2.setSpan(new AbsoluteSizeSpan(13, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span2);
        view.append(span3);

    }

    public void setSpannableString2(String str3, TextView view) {
        view.setText("");
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(10, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(14, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span4);
        view.append(span5);
    }
}
