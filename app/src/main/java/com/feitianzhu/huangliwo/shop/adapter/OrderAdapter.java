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
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.MultipleItemOrderModel;
import com.feitianzhu.huangliwo.model.SetMealOrderInfo;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends BaseMultiItemQuickAdapter<MultipleItemOrderModel, BaseViewHolder> {
    private String str1;
    private String str2;
    private String amount;
    private String price;

    public OrderAdapter(@Nullable List<MultipleItemOrderModel> data) {
        super(data);
        addItemType(MultipleItemOrderModel.GOODS_ORDER, R.layout.layout_order_item);
        addItemType(MultipleItemOrderModel.SETMEAL_ORDER, R.layout.layout_setmeal_order_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultipleItemOrderModel item) {
        str1 = "合计：";
        str2 = "¥ ";
        helper.addOnClickListener(R.id.btn_refund);
        helper.addOnClickListener(R.id.btn_logistics);
        helper.addOnClickListener(R.id.btn_confirm_goods);
        if (helper.getItemViewType() == MultipleItemOrderModel.GOODS_ORDER) {
            amount = String.format(Locale.getDefault(), "%.2f", item.getGoodsOrderListBean().getAmount());
            price = String.format(Locale.getDefault(), "%.2f", item.getGoodsOrderListBean().getPrice());
            setSpannableString(amount, helper.getView(R.id.amount));
            setSpannableString2(price, helper.getView(R.id.tv_amount));
            helper.setText(R.id.specifications, item.getGoodsOrderListBean().getAttributeVal());
            helper.setText(R.id.summary, item.getGoodsOrderListBean().getGoodName());
            helper.setText(R.id.count, "×" + item.getGoodsOrderListBean().getCount());
            helper.setText(R.id.tvCount, "共" + item.getGoodsOrderListBean().getCount() + "件商品");
            helper.setText(R.id.merchantsName, item.getGoodsOrderListBean().getShopName());
            Glide.with(mContext).load(item.getGoodsOrderListBean().getGoodsImg()).apply(new RequestOptions()
                    .placeholder(R.mipmap.g10_04weijiazai)
                    .error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));
            if (item.getGoodsOrderListBean().getIsVipOrder() == 1) {
                if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                    helper.setText(R.id.tvStatus, "等待发货");
                    helper.setGone(R.id.btn_logistics, false);
                    helper.setGone(R.id.btn_refund, false);
                    helper.setGone(R.id.btn_confirm_goods, false);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                    helper.setText(R.id.tvStatus, "等待收货");
                    helper.setText(R.id.btn_logistics, "查看物流");
                    helper.setGone(R.id.btn_logistics, true);
                    helper.setGone(R.id.btn_refund, false);
                    helper.setGone(R.id.btn_confirm_goods, false);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                    helper.setText(R.id.tvStatus, "交易完成");
                    helper.setGone(R.id.btn_logistics, false);
                    helper.setGone(R.id.btn_refund, false);
                    helper.setGone(R.id.btn_confirm_goods, false);
                } else {
                    helper.setText(R.id.tvStatus, "");
                    helper.setGone(R.id.btn_confirm_goods, false);
                    helper.setGone(R.id.btn_logistics, false);
                    helper.setGone(R.id.btn_refund, false);
                }

            } else {
                if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                    if (item.getGoodsOrderListBean().getIsEval() == 1) { //是否评价
                        helper.setText(R.id.btn_confirm_goods, "查看详情");
                    } else {
                        helper.setText(R.id.btn_confirm_goods, "晒评价");
                    }
                    helper.setText(R.id.btn_refund, "删除订单");
                    helper.setText(R.id.tvStatus, "交易完成");
                    helper.setText(R.id.btn_logistics, "查看物流");
                    helper.setGone(R.id.btn_logistics, true);
                    helper.setGone(R.id.btn_refund, true);
                    helper.setGone(R.id.btn_confirm_goods, true);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                    helper.setText(R.id.tvStatus, "等待付款");
                    helper.setText(R.id.btn_confirm_goods, "去付款");
                    helper.setText(R.id.btn_logistics, " 取消订单");
                    helper.setGone(R.id.btn_confirm_goods, true);
                    helper.setGone(R.id.btn_logistics, true);
                    helper.setGone(R.id.btn_refund, false);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                    helper.setText(R.id.tvStatus, "等待发货");
                    helper.setText(R.id.btn_logistics, "申请退款 ");
                    helper.setGone(R.id.btn_confirm_goods, false);
                    helper.setGone(R.id.btn_logistics, true);
                    helper.setGone(R.id.btn_refund, false);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                    helper.setText(R.id.tvStatus, "等待收货");
                    helper.setText(R.id.btn_confirm_goods, "确认收货");
                    helper.setText(R.id.btn_logistics, " 查看物流");
                    helper.setText(R.id.btn_refund, "申请退款");
                    helper.setGone(R.id.btn_confirm_goods, true);
                    helper.setGone(R.id.btn_logistics, true);
                    helper.setGone(R.id.btn_refund, true);
                } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_REFUND || item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_REFUNDED || item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                    if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_REFUND) {
                        helper.setText(R.id.tvStatus, "退款中");
                        helper.setText(R.id.btn_confirm_goods, "退款进度");
                        helper.setGone(R.id.btn_refund, false);
                        helper.setGone(R.id.btn_confirm_goods, true);
                    } else if (item.getGoodsOrderListBean().getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
                        helper.setText(R.id.tvStatus, "退款完成");
                        helper.setText(R.id.btn_refund, "删除订单");
                        helper.setText(R.id.btn_confirm_goods, "查看订单");
                        helper.setGone(R.id.btn_refund, true);
                        helper.setGone(R.id.btn_confirm_goods, true);
                    } else {
                        helper.setText(R.id.tvStatus, "交易关闭");
                        helper.setText(R.id.btn_refund, "删除订单");
                        helper.setGone(R.id.btn_refund, true);
                        helper.setGone(R.id.btn_confirm_goods, false);
                    }
                    helper.setGone(R.id.btn_logistics, false);
                } else {
                    helper.setText(R.id.tvStatus, "");
                    helper.setGone(R.id.btn_confirm_goods, false);
                    helper.setGone(R.id.btn_logistics, false);
                    helper.setGone(R.id.btn_refund, false);
                }
            }
        } else {
            helper.setText(R.id.count, "×1");
            helper.setText(R.id.tvCount, "共1件商品");
            helper.setText(R.id.merchantsName, item.getSetMealOrderModel().getMerchantName());
            helper.setText(R.id.summary, item.getSetMealOrderModel().getSmName());
            helper.setText(R.id.specifications, item.getSetMealOrderModel().getRemark());
            amount = String.format(Locale.getDefault(), "%.2f", item.getSetMealOrderModel().getAmount());
            price = String.format(Locale.getDefault(), "%.2f", item.getSetMealOrderModel().getAmount());
            setSpannableString(amount, helper.getView(R.id.amount));
            setSpannableString2(price, helper.getView(R.id.tv_amount));
            if (item.getSetMealOrderModel().getSmImg() != null && item.getSetMealOrderModel().getSmImg().contains(",")) {
                String[] imgUrls = item.getSetMealOrderModel().getSmImg().split(",");
                Glide.with(mContext).load(imgUrls[0]).apply(new RequestOptions()
                        .placeholder(R.mipmap.g10_04weijiazai)
                        .error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));
            } else {
                Glide.with(mContext).load(item.getSetMealOrderModel().getSmImg()).apply(new RequestOptions()
                        .placeholder(R.mipmap.g10_04weijiazai)
                        .error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.image));
            }

            helper.setVisible(R.id.btn_refund, false);
            if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.WAIT_PAY) {
                helper.setGone(R.id.btn_logistics, true);
                helper.setGone(R.id.btn_confirm_goods, true);
                helper.setText(R.id.tvStatus, "等待付款");
                helper.setText(R.id.btn_logistics, "取消订单");
                helper.setText(R.id.btn_confirm_goods, "去付款");
            } else if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.WAIT_USE) {
                //未使用
                helper.setText(R.id.btn_logistics, "申请退款");
                helper.setText(R.id.tvStatus, "等待使用");
                helper.setGone(R.id.btn_logistics, true);
                helper.setGone(R.id.btn_confirm_goods, true);
                helper.setText(R.id.btn_confirm_goods, "查看订单");
            } else if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.REFUNDING) {
                helper.setText(R.id.tvStatus, "退款中");
                helper.setGone(R.id.btn_logistics, false);
                helper.setGone(R.id.btn_confirm_goods, true);
                helper.setText(R.id.btn_confirm_goods, "查看订单");
            } else if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.REFUNDED) {
                helper.setText(R.id.tvStatus, "退款完成");
                helper.setGone(R.id.btn_logistics, false);
                helper.setGone(R.id.btn_confirm_goods, true);
                helper.setText(R.id.btn_confirm_goods, "查看详情");
            } else if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.CANCEL) {
                helper.setText(R.id.tvStatus, "交易关闭");
                helper.setText(R.id.btn_confirm_goods, "查看订单");
                helper.setGone(R.id.btn_confirm_goods, true);
                helper.setGone(R.id.btn_logistics, false);
            } else if (item.getSetMealOrderModel().getStatus() == SetMealOrderInfo.HAVE_USED) {
                if (item.getSetMealOrderModel().getIsConsume() == 1) {
                    //已使用
                    if (item.getSetMealOrderModel().getIsEval() == 1) {
                        helper.setGone(R.id.btn_logistics, false);
                    } else {
                        helper.setGone(R.id.btn_logistics, true);
                        helper.setText(R.id.btn_logistics, "晒评价");
                    }
                    helper.setText(R.id.tvStatus, "交易完成");
                }
            } else {
                helper.setText(R.id.tvStatus, "");
                helper.setGone(R.id.btn_confirm_goods, false);
                helper.setGone(R.id.btn_logistics, false);
            }
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

