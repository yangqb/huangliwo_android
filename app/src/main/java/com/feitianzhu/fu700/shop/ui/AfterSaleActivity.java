package com.feitianzhu.fu700.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.shop.adapter.OrderAdapter;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

/**
 * package name: com.feitianzhu.fu700.shop.ui
 * user: yangqinbo
 * date: 2019/12/16
 * time: 17:05
 * email: 694125155@qq.com
 */
public class AfterSaleActivity extends BaseActivity {
    private OrderAdapter adapter;
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
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
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(AfterSaleActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderList.get(position).getOrderNo());
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
        /*
        status --- -1代表全部,0代表待评价-2代表售后的单子（退款中和已退款） 其他的按照订单的状态传值
        * */
        OkHttpUtils.post()
                .url(Urls.GET_ORDER_INFO)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("status", "-2")
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
                        GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo) response;
                        goodsOrderList = goodsOrderInfo.getGoodsOrderList();
                        adapter.setNewData(goodsOrderList);
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
