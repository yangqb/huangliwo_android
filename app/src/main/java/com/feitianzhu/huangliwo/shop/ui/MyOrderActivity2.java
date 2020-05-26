package com.feitianzhu.huangliwo.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.EvaluateMode;
import com.feitianzhu.huangliwo.model.GoodOrderCountMode;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.LogisticsModel;
import com.feitianzhu.huangliwo.model.MultipleItemOrderModel;
import com.feitianzhu.huangliwo.model.PlaneOrderTableEntity;
import com.feitianzhu.huangliwo.model.SetMealOrderInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.shop.SelectPayActivity;
import com.feitianzhu.huangliwo.shop.SetMealOrderDetailActivity;
import com.feitianzhu.huangliwo.shop.SetMealPayActivity;
import com.feitianzhu.huangliwo.shop.adapter.OrderAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class MyOrderActivity2 extends BaseActivity {
    private static final int PAY_REQUEST_CODE = 1001;
    private static final int COMMENTS_REQUEST_CODE = 1002;
    public static final String ORDER_TYPE = "order_type";
    public static final String ORDER_STATUS = "order_status";
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
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private static final int REFUND_REQUEST_CODE = 1000;
    private String[] strings = new String[]{"商品订单", "套餐订单"};
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;
    @BindView(R.id.tabLayout2)
    CommonTabLayout tabLayout2;
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
        type = getIntent().getIntExtra(ORDER_TYPE, 0);
        status = getIntent().getIntExtra(ORDER_STATUS, -1);
        butType = getIntent().getIntExtra(ORDER_STATUS, 0);
        if (type == 0) {
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout2.setVisibility(View.GONE);
            titleName.setText(strings[0]);
            tabs.add(new PlaneOrderTableEntity("全部"));
            tabs.add(new PlaneOrderTableEntity("待付款(0)"));
            tabs.add(new PlaneOrderTableEntity("待发货(0)"));
            tabs.add(new PlaneOrderTableEntity("待收货(0)"));
            tabs.add(new PlaneOrderTableEntity("待评价(0)"));
            tabLayout.setTabData(tabs);
            if (status == GoodsOrderInfo.TYPE_All) {
                tabLayout.setCurrentTab(0);
            } else if (status == GoodsOrderInfo.TYPE_WAIT_COMMENTS) {
                tabLayout.setCurrentTab(4);
            } else if (status == GoodsOrderInfo.TYPE_NO_PAY) {
                tabLayout.setCurrentTab(1);
            } else if (status == GoodsOrderInfo.TYPE_WAIT_DELIVERY) {
                tabLayout.setCurrentTab(2);
            } else if (status == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                tabLayout.setCurrentTab(3);
            }
        } else {
            tabLayout.setVisibility(View.GONE);
            tabLayout2.setVisibility(View.VISIBLE);
            titleName.setText(strings[1]);
            tabs.add(new PlaneOrderTableEntity("全部"));
            tabs.add(new PlaneOrderTableEntity("待付款(0)"));
            tabs.add(new PlaneOrderTableEntity("待使用(0)"));
            tabs.add(new PlaneOrderTableEntity("待评价(0)"));
            tabLayout2.setTabData(tabs);
            if (butType == 0) {
                tabLayout2.setCurrentTab(0);
            } else if (butType == 1) {
                tabLayout2.setCurrentTab(1);
            } else if (butType == 2) {
                tabLayout2.setCurrentTab(2);
            } else if (butType == 4) {
                tabLayout2.setCurrentTab(3);
            }
        }


        mAdapter = new OrderAdapter(multipleItemOrderModels);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        TextView tvNoData = mEmptyView.findViewById(R.id.no_data);
        tvNoData.setText("没有相关订单，下拉刷新试试");
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        refreshLayout.setEnableLoadMore(false);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getEmptyView().setVisibility(View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        initListener();
    }

    public void initListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    butType = 0;
                    status = GoodsOrderInfo.TYPE_All;
                    getOrderList(status);
                } else if (position == 1) {
                    butType = 1;
                    status = GoodsOrderInfo.TYPE_NO_PAY;
                    getOrderList(status);
                } else if (position == 2) {
                    butType = 2;
                    status = GoodsOrderInfo.TYPE_WAIT_DELIVERY;
                    getOrderList(status);
                } else if (position == 3) {
                    status = GoodsOrderInfo.TYPE_WAIT_RECEIVING;
                    getOrderList(status);
                } else if (position == 4) {
                    butType = 4;
                    status = GoodsOrderInfo.TYPE_WAIT_COMMENTS;
                    getOrderList(status);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        tabLayout2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    butType = 0;
                    multipleItemOrderModels.clear();
                    currSetMealOder = allSetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                } else if (position == 1) {
                    butType = 1;
                    multipleItemOrderModels.clear();
                    currSetMealOder = waitPaySetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                } else if (position == 2) {
                    butType = 2;
                    multipleItemOrderModels.clear();
                    currSetMealOder = waitUseSetMealOder;
                    for (int i = 0; i < currSetMealOder.size(); i++) {
                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                        multipleItemOrderModel.setSetMealOrderModel(currSetMealOder.get(i));
                        multipleItemOrderModels.add(multipleItemOrderModel);
                    }
                    mAdapter.setNewData(multipleItemOrderModels);
                    mAdapter.notifyDataSetChanged();
                } else if (position == 3) {
                    butType = 4;
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
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            @SingleClick()
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    //商品订单详情，不包含399订单
                    if (goodsOrderList.get(position).getIsVipOrder() == 0) {
                        intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                        intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                        startActivity(intent);
                    }
                } else {
                    //商铺订单详情
                    intent = new Intent(MyOrderActivity2.this, SetMealOrderDetailActivity.class);
                    intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, currSetMealOder.get(position).getOrderNo());
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            @SingleClick()
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //根据订单类型来跳转1.等待付款(下单成功未支付)2.等待发货(付款成功)3.等待收货(已发货)4.交易成功(已收货,已评价,未评价)5.退款中6.退款成功7.订单失效(支付超时)
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    switch (view.getId()) {
                        case R.id.btn_refund:
                            //退货
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                                intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                                intent.putExtra(EditApplyRefundActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                intent.putExtra(EditApplyRefundActivity.ORDER_AMOUNT, goodsOrderList.get(position).getAmount());
                                intent.putExtra(EditApplyRefundActivity.ORDER_TYPE, type);
                                startActivityForResult(intent, REFUND_REQUEST_CODE);
                               /* intent = new Intent(MyOrderActivity2.this, ApplyReturnGoodsActivity.class);
                                startActivity(intent);*/
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED ||
                                    goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                                //删除订单，
                                new XPopup.Builder(MyOrderActivity2.this)
                                        .asConfirm("确定要删除该订单？", "", "关闭", "确定", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                delete(goodsOrderList.get(position).getOrderNo());
                                            }
                                        }, null, false)
                                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                        .show();
                            }
                            break;
                        case R.id.btn_logistics:
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
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
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_COMPLETED) {
                                //查看物流
                                checkLogisticsInfo(goodsOrderList.get(position).getExpressNo(), goodsOrderList.get(position).getLogisticCpName());
                                /*intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_COMPANY, goodsOrderList.get(position).getLogisticCpName());
                                intent.putExtra(LogisticsInfoActivity.LOGISTICS_NO, goodsOrderList.get(position).getExpressNo());
                                startActivity(intent);*/
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
                                //查看详情
                                intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                                intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                                startActivity(intent);
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_WAIT_RECEIVING) {
                                //确认收货
                                new XPopup.Builder(MyOrderActivity2.this)
                                        .asConfirm("", "请检查货品无破损后点击\"确认收货\"，确认收货后将不能退货", "关闭", "确定", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                confirm(goodsOrderList.get(position).getOrderNo());
                                            }
                                        }, null, false)
                                        .bindLayout(R.layout.layout_dialog_login) //绑定已有布局
                                        .show();
                            } else if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                    || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
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
                                new XPopup.Builder(MyOrderActivity2.this)
                                        .asConfirm("确定要取消该订单？", "", "关闭", "确定", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                cancelSetMealOrder(currSetMealOder.get(position).getOrderNo(), "5");
                                            }
                                        }, null, false)
                                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                        .show();
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
                    getGoodsOrderList();
                } else {
                    getSetMealOrderList();
                }
            }
        });
    }


    @OnClick({R.id.left_button, R.id.title_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_name:
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(MyOrderActivity2.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        titleName.setText(strings[position]);
                                        type = position;
                                        tabs.clear();
                                        if (position == 0) {
                                            tabLayout.setVisibility(View.VISIBLE);
                                            tabLayout2.setVisibility(View.GONE);
                                            tabs.add(new PlaneOrderTableEntity("全部"));
                                            tabs.add(new PlaneOrderTableEntity("待付款(0)"));
                                            tabs.add(new PlaneOrderTableEntity("待发货(0)"));
                                            tabs.add(new PlaneOrderTableEntity("待收货(0)"));
                                            tabs.add(new PlaneOrderTableEntity("待评价(0)"));
                                            tabLayout.setTabData(tabs);
                                            getGoodsOrderList();
                                        } else {
                                            tabLayout.setVisibility(View.GONE);
                                            tabLayout2.setVisibility(View.VISIBLE);
                                            tabs.add(new PlaneOrderTableEntity("全部"));
                                            tabs.add(new PlaneOrderTableEntity("待付款(0)"));
                                            tabs.add(new PlaneOrderTableEntity("待使用(0)"));
                                            tabs.add(new PlaneOrderTableEntity("待评价(0)"));
                                            tabLayout2.setTabData(tabs);
                                            getSetMealOrderList();
                                        }

                                    }
                                }))
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
        }

    }

    @Override
    protected void initData() {
        if (type == 0) {
            getGoodsOrderList();
        } else {
            getSetMealOrderList();
        }
    }


    public void getGoodsOrderList() {
        OkGo.<LzyResponse<GoodOrderCountMode>>get(Urls.GTE_ORDER_COUNT)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<GoodOrderCountMode>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<GoodOrderCountMode>> response) {
                        //super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            GoodOrderCountMode orderCountMode = response.body().data;
                            tabs.clear();
                            tabs.add(new PlaneOrderTableEntity("全部"));
                            tabs.add(new PlaneOrderTableEntity("待付款(" + orderCountMode.getWaitPay() + ")"));
                            tabs.add(new PlaneOrderTableEntity("待发货(" + orderCountMode.getWaitDeliver() + ")"));
                            tabs.add(new PlaneOrderTableEntity("待收货(" + orderCountMode.getWaitReceiving() + ")"));
                            tabs.add(new PlaneOrderTableEntity("待评价(" + orderCountMode.getWaitEval() + ")"));
                            tabLayout.setTabData(tabs);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<GoodOrderCountMode>> response) {
                        super.onError(response);
                    }
                });
        getOrderList(status);
    }

    public void getSetMealOrderList() {

        OkGo.<LzyResponse<SetMealOrderInfo>>get(Urls.SETMEAL_ORDER_LIST)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<SetMealOrderInfo>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<SetMealOrderInfo>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<SetMealOrderInfo>> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            tabs.clear();
                            tabs.add(new PlaneOrderTableEntity("全部"));
                            tabs.add(new PlaneOrderTableEntity("待付款(" + response.body().data.getWaitPayCount() + ")"));
                            tabs.add(new PlaneOrderTableEntity("待使用(" + response.body().data.getWaitUseCount() + ")"));
                            tabs.add(new PlaneOrderTableEntity("待评价(" + response.body().data.getWaitEvalCount() + ")"));
                            tabLayout2.setTabData(tabs);
                            allSetMealOder = response.body().data.getAll();
                            waitEvalSetMealOder = response.body().data.getWaitEval();
                            waitPaySetMealOder = response.body().data.getWaitPay();
                            waitUseSetMealOder = response.body().data.getWaitUse();
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
                        mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Response<LzyResponse<SetMealOrderInfo>> response) {
                        super.onError(response);
                        goneloadDialog();
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    public void getOrderList(int status) {
        /*
        status --- -1代表全部,0代表待评价-2代表售后的单子（退款中和已退款） 其他的按照订单的状态传值
        * */

        OkGo.<LzyResponse<GoodsOrderInfo>>post(Urls.GET_ORDER_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("status", status + "")
                .execute(new JsonCallback<LzyResponse<GoodsOrderInfo>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<GoodsOrderInfo>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo>> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().data != null) {
                            goodsOrderInfo = response.body().data;
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
                        mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });
    }

    public void delete(String orderNo) {
        OkGo.<LzyResponse>get(Urls.DELETE_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("删除成功");
                            getGoodsOrderList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void cancel(String orderNo) {

        OkGo.<LzyResponse>get(Urls.CANCEL_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("取消成功");
                            getGoodsOrderList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void cancelSetMealOrder(String orderNo, String status) {

        OkGo.<LzyResponse>post(Urls.CANCEL_SETMEAL_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .params("status", status)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("取消成功");
                            getSetMealOrderList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void confirm(String orderNo) {
        OkGo.<LzyResponse>get(Urls.CONFIRM_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("确认收货");
                            getGoodsOrderList();
                        }
                    }
                });
    }

    public void checkLogisticsInfo(String logisticsNo, String logisticsName) {
        if (logisticsNo != null && !TextUtils.isEmpty(logisticsNo)) {
            OkGo.<LzyResponse<String>>get(Urls.GET_LOGISTICS_INFO)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("expressNo", logisticsNo)
                    .execute(new JsonCallback<LzyResponse<String>>() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<String>> response) {
                            super.onSuccess(MyOrderActivity2.this, response.body().msg, response.body().code);
                            if (response.body().code == 0 && response.body().data != null && !TextUtils.isEmpty(response.body().data)) {
                                String jsonStr = response.body().data;
                                LogisticsModel logisticsModel = new Gson().fromJson(jsonStr, LogisticsModel.class);
                                if (logisticsModel.getData() != null && logisticsModel.getData().size() > 0) {
                                    Intent intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                                    intent.putExtra(LogisticsInfoActivity.LOGISTICS_COMPANY, logisticsName);
                                    intent.putExtra(LogisticsInfoActivity.LOGISTICS_DATA, logisticsModel);
                                    startActivity(intent);
                                } else {
                                    ToastUtils.show("暂无物流信息");
                                }
                            } else {
                                ToastUtils.show("暂无物流信息");
                            }
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<LzyResponse<String>> response) {
                            super.onError(response);
                        }
                    });
        } else {
            ToastUtils.show("暂无物流信息");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REFUND_REQUEST_CODE || requestCode == COMMENTS_REQUEST_CODE) {
                if (type == 0) {
                    getGoodsOrderList();
                } else {
                    getSetMealOrderList();
                }
            } else if (requestCode == PAY_REQUEST_CODE) {
                getGoodsOrderList();
            }
        }
    }
}
