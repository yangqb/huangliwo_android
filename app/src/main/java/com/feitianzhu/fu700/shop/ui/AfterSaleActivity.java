package com.feitianzhu.fu700.shop.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.shop.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.shop.ui
 * user: yangqinbo
 * date: 2019/12/16
 * time: 17:05
 * email: 694125155@qq.com
 */
public class AfterSaleActivity extends BaseActivity {
    private OrderAdapter adapter;
    public static final String ORDER_LIST_DATA = "order_data";
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderClassList = new ArrayList<>();
    private List<GoodsOrderInfo.GoodsOrderListBean> goodsOrderCurrentList = new ArrayList<>();

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_after_sale;
    }

    @Override
    protected void initView() {
        titleName.setText("售后订单");
        GoodsOrderInfo goodsOrderInfo = (GoodsOrderInfo) getIntent().getSerializableExtra(ORDER_LIST_DATA);
        if (goodsOrderInfo != null) {
            goodsOrderList = goodsOrderInfo.getGoodsOrderList();
            goodsOrderCurrentList.addAll(getGoodsOrderClassList(GoodsOrderInfo.TYPE_REFUND));
            goodsOrderCurrentList.addAll(getGoodsOrderClassList(GoodsOrderInfo.TYPE_REFUNDED));
            goodsOrderCurrentList.addAll(getGoodsOrderClassList(GoodsOrderInfo.TYPE_CANCEL));
        }
        adapter = new OrderAdapter(goodsOrderCurrentList);
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
                Intent intent = new Intent(AfterSaleActivity.this, OrderDetailActivity.class);
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
                        break;
                    case R.id.btn_logistics:
                        break;
                    case R.id.btn_confirm_goods:
                        if (goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUND
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_REFUNDED
                                || goodsOrderCurrentList.get(position).getStatus() == GoodsOrderInfo.TYPE_CANCEL) {
                            //查看详情
                            intent = new Intent(AfterSaleActivity.this, OrderDetailActivity.class);
                            intent.putExtra(OrderDetailActivity.ORDER_NO, goodsOrderCurrentList.get(position).getOrderNo());
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    public List<GoodsOrderInfo.GoodsOrderListBean> getGoodsOrderClassList(int goodsOrderType) {
        goodsOrderClassList.clear();
        for (int i = 0; i < goodsOrderList.size(); i++) {
            if (goodsOrderType == goodsOrderList.get(i).getStatus()) {
                goodsOrderClassList.add(goodsOrderList.get(i));
            }
        }
        return goodsOrderClassList;
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
