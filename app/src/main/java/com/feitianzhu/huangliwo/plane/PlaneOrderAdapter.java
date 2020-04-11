package com.feitianzhu.huangliwo.plane;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.model.PlaneOrderStatus;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

public class PlaneOrderAdapter extends BaseQuickAdapter<PlaneOrderModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PlaneOrderAdapter(List<PlaneOrderModel> data) {
        super(R.layout.item_go_come_plane_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlaneOrderModel item) {
        if (item.status == PlaneOrderStatus.BOOK_OK) {
            helper.setText(R.id.tvStatus, "等待付款");
            helper.setVisible(R.id.btn_pay, true);
        } else if (item.status == PlaneOrderStatus.PAY_OK || item.status == PlaneOrderStatus.TICKET_LOCK) {
            helper.setText(R.id.tvStatus, "等待出票");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.TICKET_OK) {
            helper.setText(R.id.tvStatus, "出票完成");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.CANCEL_OK) {
            helper.setText(R.id.tvStatus, "订单取消");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.WAIT_REFUNDMENT || item.status == PlaneOrderStatus.APPLY_RETURN_PAY || item.status == PlaneOrderStatus.APPLY_REFUNDMENT) {
            helper.setText(R.id.tvStatus, "等待退款");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.REFUND_OK) {
            helper.setText(R.id.tvStatus, "退款完成");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.APPLY_CHANGE) {
            helper.setText(R.id.tvStatus, "改签中");
            helper.setVisible(R.id.btn_pay, false);
        } else if (item.status == PlaneOrderStatus.CHANGE_OK) {
            helper.setText(R.id.tvStatus, "改签完成");
            helper.setVisible(R.id.btn_pay, false);
        }
        helper.setText(R.id.orderDate, "下单时间" + item.createTime);
        setSpannableString(MathUtils.subZero(String.valueOf(item.noPayAmount)), helper.getView(R.id.price));
        helper.setText(R.id.goFlightCode, item.flightNum);
        helper.setText(R.id.goFlightDate, item.goFlyTime);
        helper.setText(R.id.cityName, item.goCityInfo);
        if (item.isGoBack == 0) {
            helper.setGone(R.id.ll_back, false);
            helper.setGone(R.id.goTitle, false);
        } else {
            helper.setGone(R.id.ll_back, true);
            helper.setGone(R.id.goTitle, true);
            helper.setText(R.id.backFlightDate, item.backFlyTime);
            helper.setText(R.id.backFlightCode, item.backFlightNum);
            helper.setText(R.id.cityName, item.backCityInfo);
        }

        // helper.addOnClickListener(R.id.btn_pay);

    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
