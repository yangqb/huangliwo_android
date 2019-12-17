package com.feitianzhu.fu700.shop.ui;

import android.content.Intent;
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
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.model.MultiItemGoodsOrder;
import com.feitianzhu.fu700.shop.ShopPayActivity;
import com.feitianzhu.fu700.shop.adapter.OrderAdapter;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.USERID;

public class MyOrderActivity2 extends BaseActivity {
    private OrderAdapter adapter;
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderClassList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderCurrentList = new ArrayList<>();
    private GoodsOrderInfo goodsOrderInfo;
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
        adapter.setEmptyView(mEmptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_DATA, goodsOrderCurrentList.get(position));
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
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                            //取消订单，
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                            //查看物流
                            intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                            //退款
                            intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.btn_confirm_goods:
                        if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                            //评价
                            intent = new Intent(MyOrderActivity2.this, EditCommentsActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                            //付款
                            intent = new Intent(MyOrderActivity2.this, ShopPayActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                            //查看物流
                            intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                            startActivity(intent);
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                            //确认收货
                        } else if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                            //查看详情
                            intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                            intent.putExtra(OrderDetailActivity.ORDER_DATA, goodsOrderCurrentList.get(position));
                            startActivity(intent);
                        }
                        break;
                }
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

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goodsOrderInfo = (GoodsOrderInfo) response;
                        goodsOrderList = goodsOrderInfo.getGoodsOrderList();
                        goodsOrderCurrentList = goodsOrderList;
                        adapter.setNewData(goodsOrderCurrentList);
                        adapter.notifyDataSetChanged();
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
}
