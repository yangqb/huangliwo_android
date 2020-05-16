package com.feitianzhu.huangliwo.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.MultipleItemOrderModel;
import com.feitianzhu.huangliwo.model.SetMealOrderInfo;
import com.feitianzhu.huangliwo.shop.SetMealOrderDetailActivity;
import com.feitianzhu.huangliwo.shop.adapter.OrderAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.shop.ui
 * user: yangqinbo
 * date: 2019/12/16
 * time: 17:05
 * email: 694125155@qq.com
 */
public class AfterSaleActivity extends BaseActivity {
    public static final String ORDER_TYPE = "order_type";
    private OrderAdapter mAdapter;
    private int type = 0;
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    private List<MultipleItemOrderModel> multipleItemOrderModels = new ArrayList<>();
    private List<SetMealOrderInfo.SetMealOrderModel> refundingSetMealOder = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_after_sale;
    }

    @Override
    protected void initView() {
        titleName.setText("售后订单");
        type = getIntent().getIntExtra(ORDER_TYPE, 0);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    intent = new Intent(AfterSaleActivity.this, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
                    startActivity(intent);
                } else {
                    intent = new Intent(AfterSaleActivity.this, SetMealOrderDetailActivity.class);
                    intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, refundingSetMealOder.get(position).getOrderNo());
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //根据订单类型来跳转1.交易成功(已收货)2.等待付款(下单成功)3.等待发货(付款成功)4.已发货5.等待收货6.退款中7.退款成功8.订单失效
                Intent intent;
                if (mAdapter.getItemViewType(position) == MultipleItemOrderModel.GOODS_ORDER) {
                    switch (view.getId()) {
                        case R.id.btn_refund:
                            break;
                        case R.id.btn_logistics:
                            break;
                        case R.id.btn_confirm_goods:
                            if (goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                    || goodsOrderList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
                                //查看详情
                                intent = new Intent(AfterSaleActivity.this, OrderDetailActivity.class);
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
                            break;
                        case R.id.btn_confirm_goods:
                            if (refundingSetMealOder.get(position).getStatus() == 3
                                    || refundingSetMealOder.get(position).getStatus() == 4) {
                                //查看详情
                                intent = new Intent(AfterSaleActivity.this, SetMealOrderDetailActivity.class);
                                intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, refundingSetMealOder.get(position).getOrderNo());
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
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        if (type == 0) {
/*
        status --- -1代表全部,0代表待评价-2代表售后的单子（退款中和已退款） 其他的按照订单的状态传值
        * */

            OkGo.<LzyResponse<GoodsOrderInfo>>post(Urls.GET_ORDER_INFO)
                    .tag(this)
                    .params(ACCESSTOKEN, token)
                    .params(USERID, userId)
                    .params("status", "-2")
                    .execute(new JsonCallback<LzyResponse<GoodsOrderInfo>>() {
                        @Override
                        public void onStart(Request<LzyResponse<GoodsOrderInfo>, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo>> response) {
                            super.onSuccess(AfterSaleActivity.this, response.body().msg, response.body().code);
                            goneloadDialog();
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().data != null) {
                                GoodsOrderInfo goodsOrderInfo = response.body().data;
                                goodsOrderList = goodsOrderInfo.getGoodsOrderList();
                                multipleItemOrderModels.clear();
                                for (int i = 0; i < goodsOrderList.size(); i++) {
                                    MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.GOODS_ORDER);
                                    multipleItemOrderModel.setGoodsOrderListBean(goodsOrderList.get(i));
                                    multipleItemOrderModels.add(multipleItemOrderModel);
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
        } else {

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
                            super.onSuccess(AfterSaleActivity.this, response.body().msg, response.body().code);
                            goneloadDialog();
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().data != null) {
                                if (response != null) {
                                    refundingSetMealOder = response.body().data.getRefunding();
                                    multipleItemOrderModels.clear();
                                    for (int i = 0; i < refundingSetMealOder.size(); i++) {
                                        MultipleItemOrderModel multipleItemOrderModel = new MultipleItemOrderModel(MultipleItemOrderModel.SETMEAL_ORDER);
                                        multipleItemOrderModel.setSetMealOrderModel(refundingSetMealOder.get(i));
                                        multipleItemOrderModels.add(multipleItemOrderModel);
                                    }
                                    mAdapter.setNewData(multipleItemOrderModels);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                            mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Response<LzyResponse<SetMealOrderInfo>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                            goneloadDialog();
                        }
                    });
        }


    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
