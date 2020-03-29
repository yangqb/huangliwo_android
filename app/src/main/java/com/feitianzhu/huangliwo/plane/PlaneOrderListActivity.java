package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.PlaneOrderInfo;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneOrderListActivity extends BaseActivity {
    private PlaneOrderAdapter mAdapter;
    private List<PlaneOrderModel> allOrder = new ArrayList<>();
    private List<PlaneOrderModel> refundOrUpdateList = new ArrayList<>();
    private List<PlaneOrderModel> waiPayList = new ArrayList<>();
    private List<PlaneOrderModel> waitTicketedList = new ArrayList<>();
    private List<PlaneOrderModel> ticketedList = new ArrayList<>();
    private String userId;
    private String token;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_order;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("机票订单");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaneOrderAdapter(allOrder);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PlaneOrderListActivity.this, PlaneOrderDetailActivity.class);
                if (position % 4 == 0) {
                    //单程未付款
                    intent.putExtra(PlaneOrderDetailActivity.PLANE_TYPE, 1);
                    intent.putExtra(PlaneOrderDetailActivity.ORDER_TYPE, 1);
                } else if (position % 4 == 1) {
                    //单程已付款
                    intent.putExtra(PlaneOrderDetailActivity.PLANE_TYPE, 1);
                    intent.putExtra(PlaneOrderDetailActivity.ORDER_TYPE, 2);
                } else if (position % 4 == 2) {
                    //往返未付款
                    intent.putExtra(PlaneOrderDetailActivity.PLANE_TYPE, 2);
                    intent.putExtra(PlaneOrderDetailActivity.ORDER_TYPE, 1);
                } else if (position % 4 == 3) {
                    //往返未付款
                    intent.putExtra(PlaneOrderDetailActivity.PLANE_TYPE, 2);
                    intent.putExtra(PlaneOrderDetailActivity.ORDER_TYPE, 2);
                }

                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {

        OkGo.<LzyResponse<PlaneOrderInfo>>get(Urls.GET_PLANE_ORDER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<PlaneOrderInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PlaneOrderInfo>> response) {
                        super.onSuccess(PlaneOrderListActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            PlaneOrderInfo planeOrderInfo = response.body().data;
                            allOrder = planeOrderInfo.all;
                            refundOrUpdateList = planeOrderInfo.refundOrUpdateList;
                            ticketedList = planeOrderInfo.ticketedList;
                            waiPayList = planeOrderInfo.waiPayList;
                            waitTicketedList = planeOrderInfo.waitTicketedList;
                            mAdapter.setNewData(allOrder);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PlaneOrderInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
