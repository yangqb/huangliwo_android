package com.feitianzhu.fu700.shop.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.AddressInfo;
import com.feitianzhu.fu700.model.GoodOrderCountMode;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.model.MultiItemGoodsOrder;
import com.feitianzhu.fu700.shop.SelectPayActivity;
import com.feitianzhu.fu700.shop.ShopPayActivity;
import com.feitianzhu.fu700.shop.adapter.OrderAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.USERID;

public class MyOrderActivity2 extends BaseActivity {
    private static final int PAY_REQUEST_CODE = 1001;
    private OrderAdapter adapter;
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderClassList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderCurrentList = new ArrayList<>();
    private GoodsOrderInfo goodsOrderInfo;
    private static final int REQUEST_CODE = 1000;
    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.wait_pay_order)
    TextView waitPayOrder;
    @BindView(R.id.wait_sending_order)
    TextView waitSendingOrder;
    @BindView(R.id.wait_receive_order)
    TextView waitReceiveOrder;
    @BindView(R.id.wait_evaluate_order)
    TextView waitEvaluateOrder;
    @BindView(R.id.line_all)
    View lineAll;
    @BindView(R.id.line_wait_pay)
    View lineWaitPay;
    @BindView(R.id.line_wait_sending)
    View lineWaitSending;
    @BindView(R.id.line_wait_receive)
    View lineWaitReceive;
    @BindView(R.id.line_wait_evaluate)
    View lineWaitEvaluate;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_collect)
    ImageView search;
    @BindView(R.id.right_img)
    ImageView afterSale;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        titleName.setText("我的订单");
        afterSale.setBackgroundResource(R.mipmap.a01_03shouhou);
        search.setBackgroundResource(R.mipmap.a01_03sousuo);
        afterSale.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        allOrder.setSelected(true);
        waitPayOrder.setSelected(false);
        waitSendingOrder.setSelected(false);
        waitReceiveOrder.setSelected(false);
        waitEvaluateOrder.setSelected(false);
        lineAll.setSelected(true);
        lineWaitPay.setSelected(false);
        lineWaitSending.setSelected(false);
        lineWaitReceive.setSelected(false);
        lineWaitEvaluate.setSelected(false);

        adapter = new OrderAdapter(goodsOrderList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        refreshLayout.setEnableLoadMore(false);
        adapter.setEmptyView(mEmptyView);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        initListener();
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderCurrentList.get(position).getOrderNo());
                startActivity(intent);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //根据订单类型来跳转1.交易成功(已收货)2.等待付款(下单成功)3.等待发货(付款成功)4.已发货5.等待收货6.退款中7.退款成功8.订单失效
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_refund:
                        //退款
                        if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                            intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.btn_logistics:
                        if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                            //删除订单，
                            delete(goodsOrderCurrentList.get(position).getOrderNo());
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                            //取消订单，
                            new XPopup.Builder(MyOrderActivity2.this)
                                    .asConfirm("确定要取消该订单？", "", "关闭", "确定", new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            cancel(goodsOrderCurrentList.get(position).getOrderNo());
                                        }
                                    }, null, false)
                                    .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                    .show();
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                            //查看物流
                            intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                            //退款
                            intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                            intent.putExtra(EditApplyRefundActivity.ORDER_DATA, goodsOrderCurrentList.get(position));
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_confirm_goods:
                        if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                            //评价
                            intent = new Intent(MyOrderActivity2.this, EditCommentsActivity.class);
                            intent.putExtra(EditCommentsActivity.ORDER_DATA, goodsOrderCurrentList.get(position));
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                            //付款
                            intent = new Intent(MyOrderActivity2.this, SelectPayActivity.class);
                            intent.putExtra(SelectPayActivity.ORDER_DATA, goodsOrderCurrentList.get(position));
                            startActivityForResult(intent, PAY_REQUEST_CODE);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                            //查看物流
                            intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                            //确认收货
                            confirm(goodsOrderCurrentList.get(position).getOrderNo());
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                            //查看详情
                            intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                            intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderCurrentList.get(position).getOrderNo());
                            startActivity(intent);
                        }
                        break;
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }


    @OnClick({R.id.left_button, R.id.all_order, R.id.wait_pay_order, R.id.wait_sending_order, R.id.wait_receive_order, R.id.wait_evaluate_order, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                //售后(退款)
                Intent intent = new Intent(MyOrderActivity2.this, AfterSaleActivity.class);
                intent.putExtra(AfterSaleActivity.ORDER_LIST_DATA, goodsOrderInfo);
                startActivity(intent);
                break;
            case R.id.all_order:
                setSelect(allOrder, true);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, true);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                goodsOrderCurrentList = goodsOrderList;
                adapter.setNewData(goodsOrderCurrentList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.wait_pay_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, true);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, true);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                adapter.setNewData(getGoodsOrderClassList(GoodsOrderInfo.TYPE_NO_PAY));
                adapter.notifyDataSetChanged();
                break;
            case R.id.wait_sending_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, true);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, true);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                adapter.setNewData(getGoodsOrderClassList(GoodsOrderInfo.TYPE_WAIT_DELIVERY));
                adapter.notifyDataSetChanged();
                break;
            case R.id.wait_receive_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, true);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, true);
                setSelect(lineWaitEvaluate, false);
                adapter.setNewData(getGoodsOrderClassList(GoodsOrderInfo.TYPE_WAIT_RECEIVING));
                adapter.notifyDataSetChanged();
                break;
            case R.id.wait_evaluate_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, true);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, true);
                adapter.setNewData(getGoodsOrderClassList(GoodsOrderInfo.TYPE_COMPLETED));
                adapter.notifyDataSetChanged();
                break;
        }

    }

    public void setSelect(View view, boolean select) {
        view.setSelected(select);
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GTE_ORDER_COUNT)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, GoodOrderCountMode.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Object response, int id) {
                        GoodOrderCountMode orderCountMode = (GoodOrderCountMode) response;
                        if (orderCountMode != null) {
                            waitPayOrder.setText("待付款(" + orderCountMode.getWaitPay() + ")");
                            waitSendingOrder.setText("待发货(" + orderCountMode.getWaitDeliver() + ")");
                            waitReceiveOrder.setText("待收货(" + orderCountMode.getWaitReceiving() + ")");
                            waitEvaluateOrder.setText("待评价(" + orderCountMode.getWaitEval() + ")");
                        }
                    }
                });

        OkHttpUtils.post()
                .url(Urls.GET_ORDER_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback() {

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, GoodsOrderInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        refreshLayout.finishRefresh();
                        goodsOrderInfo = (GoodsOrderInfo) response;
                        goodsOrderList = goodsOrderInfo.getGoodsOrderList();
                        goodsOrderCurrentList = goodsOrderList;
                        adapter.setNewData(goodsOrderCurrentList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void delete(String orderNo) {
        OkHttpUtils.get()
                .url(Urls.DELETE_ORDER)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("orderNo", orderNo)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast("删除成功");
                        initData();
                    }
                });
    }

    public void cancel(String orderNo) {
        OkHttpUtils.get()
                .url(Urls.CANCEL_ORDER)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("orderNo", orderNo)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast("取消成功");
                        initData();
                    }
                });

    }

    public void confirm(String orderNo) {
        OkHttpUtils.get()
                .url(Urls.CONFIRM_ORDER)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("orderNo", orderNo)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast("确认收货");
                        initData();
                    }
                });
    }

    public List<GoodsOrderInfo.GoodsOrderListBean> getGoodsOrderClassList(int goodsOrderType) {
        goodsOrderClassList.clear();
        for (int i = 0; i < goodsOrderList.size(); i++) {
            if (goodsOrderType == goodsOrderList.get(i).getStatus()) {
                goodsOrderClassList.add(goodsOrderList.get(i));
            }
        }
        goodsOrderCurrentList = goodsOrderClassList;
        return goodsOrderCurrentList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE || requestCode == PAY_REQUEST_CODE) {
                initData();
            }
        }
    }
}
