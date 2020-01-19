package com.feitianzhu.huangliwo.shop.ui;

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
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.EvaluateMode;
import com.feitianzhu.huangliwo.model.GoodOrderCountMode;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.MultipleItemOrderModel;
import com.feitianzhu.huangliwo.model.SetMealOrderInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.shop.SelectPayActivity;
import com.feitianzhu.huangliwo.shop.SetMealOrderDetailActivity;
import com.feitianzhu.huangliwo.shop.SetMealPayActivity;
import com.feitianzhu.huangliwo.shop.adapter.OrderAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class MyOrderActivity2 extends BaseActivity {
    private static final int PAY_REQUEST_CODE = 1001;
    private static final int COMMENTS_REQUEST_CODE = 1002;
    private int type = 0; //0，商品订单1，商铺订单
    private int butType = 0;//0全部1代付款2待发货（待使用）3待收货4待评价5售后
    private OrderAdapter mAdapter;
    private int status = -1;
    private List<MultipleItemOrderModel> multipleItemOrderModels = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    private GoodsOrderInfo goodsOrderInfo;
    private List<SetMealOrderInfo.SetMealOrderModel> allSetMealOder = new ArrayList<>();
    private List<SetMealOrderInfo.SetMealOrderModel> waitEvalSetMealOder = new ArrayList<>();
    private List<SetMealOrderInfo.SetMealOrderModel> waitPaySetMealOder = new ArrayList<>();
    private List<SetMealOrderInfo.SetMealOrderModel> waitUseSetMealOder = new ArrayList<>();
    private List<SetMealOrderInfo.SetMealOrderModel> currSetMealOder = new ArrayList<>();
    private static final int REFUND_REQUEST_CODE = 1000;
    private String[] strings = new String[]{"商品订单", "套餐订单"};
    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.wait_pay_order)
    TextView waitPayOrder;
    @BindView(R.id.wait_sending_order)
    TextView waitSendingOrder;
    @BindView(R.id.wait_receive_order)
    TextView waitReceiveOrder;
    @BindView(R.id.ll_wait_receive_order)
    LinearLayout llWaitReceiveOrder;
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
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("商品订单");
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

        mAdapter = new OrderAdapter(multipleItemOrderModels);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        refreshLayout.setEnableLoadMore(false);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                    startActivity(intent);
                } else {
                    //详情
                    intent = new Intent(MyOrderActivity2.this, SetMealOrderDetailActivity.class);
                    intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, currSetMealOder.get(position).getOrderNo());
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //根据订单类型来跳转1.等待付款(下单成功未支付)2.等待发货(付款成功)3.等待收货(已发货)4.交易成功(已收货,已评价,未评价)5.退款中6.退款成功7.订单失效(支付超时)
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    switch (view.getId()) {
                        case R.id.btn_refund:
                            //退款
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                                intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                                intent.putExtra(EditApplyRefundActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                intent.putExtra(EditApplyRefundActivity.ORDER_AMOUNT, goodsOrderList.get(position).getAmount());
                                intent.putExtra(EditApplyRefundActivity.ORDER_TYPE, type);
                                startActivityForResult(intent, REFUND_REQUEST_CODE);
                            }
                            break;
                        case R.id.btn_logistics:
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                                //删除订单，
                                delete(goodsOrderList.get(position).getOrderNo());
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                                //取消订单，
                                new XPopup.Builder(MyOrderActivity2.this)
                                        .asConfirm("确定要取消该订单？", "", "关闭", "确定", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                cancel(goodsOrderList.get(position).getOrderNo());
                                            }
                                        }, null, false)
                                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                        .show();
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                                //查看物流
                                intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_COMPANY, goodsOrderList.get(position).getLogisticCpName());
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_NO, goodsOrderList.get(position).getExpressNo());
                                startActivity(intent);
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                                //退款
                                intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                                intent.putExtra(EditApplyRefundActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                intent.putExtra(EditApplyRefundActivity.ORDER_AMOUNT, goodsOrderList.get(position).getAmount());
                                intent.putExtra(EditApplyRefundActivity.ORDER_TYPE, type);
                                startActivityForResult(intent, REFUND_REQUEST_CODE);
                            }
                            break;
                        case R.id.btn_confirm_goods:
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                                if (goodsOrderList.get(position).getIsEval() == 1) {
                                    //查看详情(已评价)
                                    intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                                    intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                    startActivity(intent);
                                } else {
                                    //评价
                                    EvaluateMode evaluateMode = new EvaluateMode();
                                    evaluateMode.setGoodId(goodsOrderList.get(position).getGoodId());
                                    evaluateMode.setOrderNo(goodsOrderList.get(position).getOrderNo());
                                    evaluateMode.setUserId(userId);
                                    intent = new Intent(MyOrderActivity2.this, EditCommentsActivity.class);
                                    intent.putExtra(EditCommentsActivity.EVALUATE_DATA, evaluateMode);
                                    startActivityForResult(intent, COMMENTS_REQUEST_CODE);
                                }
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
                                //付款
                                intent = new Intent(MyOrderActivity2.this, SelectPayActivity.class);
                                intent.putExtra(SelectPayActivity.ORDER_DATA, goodsOrderList.get(position));
                                startActivityForResult(intent, PAY_REQUEST_CODE);
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                                //查看物流
                                intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_COMPANY, goodsOrderList.get(position).getLogisticCpName());
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_NO, goodsOrderList.get(position).getExpressNo());
                                startActivity(intent);
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                                //确认收货
                                confirm(goodsOrderList.get(position).getOrderNo());
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                    || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED
                                    || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                                //查看详情
                                intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                                intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                startActivity(intent);
                            }
                            break;
                    }
                } else {
                    switch (view.getId()) {
                        case R.id.btn_refund:
                            break;
                        case R.id.btn_logistics:
                            if (currSetMealOder.get(position).getStatus() == SetMealOrderInfo.WAIT_PAY) {
                                //取消订单
                                cancelSetMealOrder(currSetMealOder.get(position).getOrderNo(), "5");
                            } else if (currSetMealOder.get(position).getStatus() == SetMealOrderInfo.HAVE_USED) {
                                if (currSetMealOder.get(position).getIsConsume() == 1) {
                                    if (currSetMealOder.get(position).getIsEval() == 0) {
                                        //评价
                                        EvaluateMode evaluateMode = new EvaluateMode();
                                        evaluateMode.setSmId(currSetMealOder.get(position).getSmId());
                                        evaluateMode.setOrderNo(currSetMealOder.get(position).getOrderNo());
                                        evaluateMode.setMerchantId(currSetMealOder.get(position).getMerchantId());
                                        evaluateMode.setUserId(userId);
                                        intent = new Intent(MyOrderActivity2.this, EditCommentsActivity.class);
                                        intent.putExtra(EditCommentsActivity.EVALUATE_DATA, evaluateMode);
                                        startActivityForResult(intent, COMMENTS_REQUEST_CODE);
                                    }
                                }
                            } else if (currSetMealOder.get(position).getStatus() == SetMealOrderInfo.WAIT_USE) {
                                //申请退款
                                intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                                intent.putExtra(EditApplyRefundActivity.ORDER_NO, currSetMealOder.get(position).getOrderNo());
                                intent.putExtra(EditApplyRefundActivity.ORDER_AMOUNT, currSetMealOder.get(position).getAmount());
                                intent.putExtra(EditApplyRefundActivity.ORDER_TYPE, type);
                                startActivityForResult(intent, REFUND_REQUEST_CODE);
                            }
                            break;
                        case R.id.btn_confirm_goods:
                            if (currSetMealOder.get(position).getStatus() == SetMealOrderInfo.WAIT_PAY) {
                                //支付
                                SetMealInfo setMealInfo = new SetMealInfo();
                                setMealInfo.setImgs(currSetMealOder.get(position).getSmImg());
                                setMealInfo.setSmName(currSetMealOder.get(position).getSmName());
                                setMealInfo.setRemark(currSetMealOder.get(position).getRemark());
                                setMealInfo.setPrice(currSetMealOder.get(position).getAmount());
                                setMealInfo.setSmId(currSetMealOder.get(position).getSmId());
                                setMealInfo.setMerchantId(currSetMealOder.get(position).getMerchantId());
                                setMealInfo.setUserId(currSetMealOder.get(position).getUserId());
                                intent = new Intent(MyOrderActivity2.this, SetMealPayActivity.class);
                                intent.putExtra(SetMealPayActivity.SETMEAL_ORDERI_NFO, setMealInfo);
                                intent.putExtra(SetMealPayActivity.ORDER_NO, currSetMealOder.get(position).getOrderNo());
                                startActivity(intent);
                            } else {
                                //详情
                                intent = new Intent(MyOrderActivity2.this, SetMealOrderDetailActivity.class);
                                intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, currSetMealOder.get(position).getOrderNo());
                                startActivity(intent);
                            }
                            break;
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (type == 0) {
                    initData();
                } else {
                    getSetMealOrderList();
                }
            }
        });
    }


    @OnClick({R.id.left_button, R.id.all_order, R.id.wait_pay_order, R.id.wait_sending_order, R.id.wait_receive_order, R.id.wait_evaluate_order, R.id.right_button, R.id.title_name, R.id.img_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_collect:
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.title_name:
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(MyOrderActivity2.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        titleName.setText(strings[position]);
                                        type = position;
                                        if (position == 0) {
                                            llWaitReceiveOrder.setVisibility(View.VISIBLE);
                                            waitPayOrder.setText("待付款(0)");
                                            waitSendingOrder.setText("待发货(0)");
                                            waitReceiveOrder.setText("待收货(0)");
                                            waitEvaluateOrder.setText("待评价(0)");
                                            initData();
                                        } else {
                                            llWaitReceiveOrder.setVisibility(View.GONE);
                                            waitPayOrder.setText("待付款(0)");
                                            waitSendingOrder.setText("待使用(0)");
                                            waitEvaluateOrder.setText("待评价(0)");
                                            getSetMealOrderList();
                                        }
                                    }
                                }))
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                //售后(退款)
                Intent intent = new Intent(MyOrderActivity2.this, AfterSaleActivity.class);
                intent.putExtra(AfterSaleActivity.ORDER_TYPE, type);
                startActivity(intent);
                break;
            case R.id.all_order:
                butType = 0;
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
                if (type == 0) {
                    getOrderList(-1);
                } else {
                    multipleItemOrderModels.clear();
                    currSetMealOder = allSetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.wait_pay_order:
                butType = 1;
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
                if (type == 0) {
                    status = GoodsOrderInfo.TYPE_NO_PAY;
                    getOrderList(status);
                } else {
                    multipleItemOrderModels.clear();
                    currSetMealOder = waitPaySetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.wait_sending_order:
                butType = 2;
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
                if (type == 0) {
                    status = GoodsOrderInfo.TYPE_WAIT_DELIVERY;
                    getOrderList(status);
                } else {
                    multipleItemOrderModels.clear();
                    currSetMealOder = waitUseSetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                }
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
                status = GoodsOrderInfo.TYPE_WAIT_RECEIVING;
                getOrderList(status);
                break;
            case R.id.wait_evaluate_order:
                butType = 4;
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
                if (type == 0) {
                    status = 0;
                    getOrderList(status);
                } else {
                    multipleItemOrderModels.clear();
                    currSetMealOder = waitEvalSetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                }

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
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
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
        getOrderList(status);
    }

    public void getSetMealOrderList() {
        OkHttpUtils.get()
                .url(Urls.SETMEAL_ORDER_LIST)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<SetMealOrderInfo>() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        goneloadDialog();
                        refreshLayout.finishRefresh(false);
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(SetMealOrderInfo response, int id) {
                        goneloadDialog();
                        refreshLayout.finishRefresh();
                        if (response != null) {
                            waitPayOrder.setText("待付款(" + response.getWaitPayCount() + ")");
                            waitSendingOrder.setText("待使用(" + response.getWaitUseCount() + ")");
                            waitEvaluateOrder.setText("待评价(" + response.getWaitEvalCount() + ")");
                            allSetMealOder = response.getAll();
                            waitEvalSetMealOder = response.getWaitEval();
                            waitPaySetMealOder = response.getWaitPay();
                            waitUseSetMealOder = response.getWaitUse();
                            if (butType == 0) {
                                currSetMealOder = allSetMealOder;
                            } else if (butType == 1) {
                                currSetMealOder = waitPaySetMealOder;
                            } else if (butType == 2) {
                                currSetMealOder = waitUseSetMealOder;
                            } else if (butType == 4) {
                                currSetMealOder = waitEvalSetMealOder;
                            }
                            multipleItemOrderModels.clear();
                            for (int i = 0; i < currSetMealOder.size(); i++) {
                                MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                                multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                                multipleItemOrderModels.add(multipleItemOrderModel);
                            }
                            mAdapter.setNewData(multipleItemOrderModels);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void getOrderList(int status) {
        /*
        status --- -1代表全部,0代表待评价-2代表售后的单子（退款中和已退款） 其他的按照订单的状态传值
        * */
        OkHttpUtils.post()
                .url(Urls.GET_ORDER_INFO)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("status", status + "")
                .build()
                .execute(new Callback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, GoodsOrderInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        goneloadDialog();
                        refreshLayout.finishRefresh(false);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goneloadDialog();
                        refreshLayout.finishRefresh();
                        goodsOrderInfo = (GoodsOrderInfo) response;
                        multipleItemOrderModels.clear();
                        if (goodsOrderInfo != null && goodsOrderInfo.getGoodsOrderList() != null) {
                            goodsOrderList = goodsOrderInfo.getGoodsOrderList();
                            for (int i = 0; i < goodsOrderList.size(); i++) {
                                MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.GOODS_ORDER);
                                multipleItemOrderModel.setGoodsOrderListBean(goodsOrderList.get(i));
                                multipleItemOrderModels.add(multipleItemOrderModel);
                            }
                        }
                        mAdapter.setNewData(multipleItemOrderModels);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void delete(String orderNo) {
        OkHttpUtils.get()
                .url(Urls.DELETE_ORDER)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
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
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
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

    public void cancelSetMealOrder(String orderNo, String status) {

        OkHttpUtils.post()
                .url(Urls.CANCEL_SETMEAL_ORDER)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("orderNo", orderNo)
                .addParams("status", status)
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
                        getSetMealOrderList();
                    }
                });
    }

    public void confirm(String orderNo) {
        OkHttpUtils.get()
                .url(Urls.CONFIRM_ORDER)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REFUND_REQUEST_CODE || requestCode == COMMENTS_REQUEST_CODE) {
                if (type == 0) {
                    initData();
                } else {
                    getSetMealOrderList();
                }
            } else if (requestCode == PAY_REQUEST_CODE) {
                initData();
            }
        }
    }
}
