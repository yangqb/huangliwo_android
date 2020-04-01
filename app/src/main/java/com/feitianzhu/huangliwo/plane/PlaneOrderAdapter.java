package com.feitianzhu.huangliwo.plane;

import android.support.annotation.NonNull;

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
        } else if (item.status == PlaneOrderStatus.TICKET_LOCK) {
            helper.setText(R.id.tvStatus, "等待出票");
        } else if (item.status == PlaneOrderStatus.TICKET_OK) {
            helper.setText(R.id.tvStatus, "出票完成");
        } else if (item.status == PlaneOrderStatus.CANCEL_OK) {
            helper.setText(R.id.tvStatus, "订单取消");
        } else if (item.status == PlaneOrderStatus.WAIT_REFUNDMENT || item.status == PlaneOrderStatus.APPLY_RETURN_PAY) {
            helper.setText(R.id.tvStatus, "等待退款");
        } else if (item.status == PlaneOrderStatus.REFUND_OK) {
            helper.setText(R.id.tvStatus, "退款完成");
        } else if (item.status == PlaneOrderStatus.APPLY_CHANGE) {
            helper.setText(R.id.tvStatus, "改签中");
        } else if (item.status == PlaneOrderStatus.CHANGE_OK) {
            helper.setText(R.id.tvStatus, "改签完成");
        }
        helper.setText(R.id.orderDate, "下单时间" + item.createTime);
        helper.setText(R.id.price, MathUtils.subZero(String.valueOf(item.noPayAmount)));
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
    }
}
