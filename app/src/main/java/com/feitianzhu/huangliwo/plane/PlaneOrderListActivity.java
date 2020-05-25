package com.feitianzhu.huangliwo.plane;

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
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.PlaneOrderInfo;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.model.PlaneOrderTableEntity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneOrderListActivity extends BaseActivity {
    private PlaneOrderAdapter mAdapter;
    private List<PlaneOrderModel> currOrder = new ArrayList<>();
    private List<PlaneOrderModel> allOrder = new ArrayList<>();
    private List<PlaneOrderModel> refundOrUpdateList = new ArrayList<>();
    private List<PlaneOrderModel> waiPayList = new ArrayList<>();
    private List<PlaneOrderModel> waitTicketedList = new ArrayList<>();
    private List<PlaneOrderModel> ticketedList = new ArrayList<>();
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private int btnType;
    private String userId;
    private String token;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;

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
        mAdapter = new PlaneOrderAdapter(currOrder);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        TextView tvNoData = mEmptyView.findViewById(R.id.no_data);
        tvNoData.setText("没有相关订单，下拉刷新试试");
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        tabs.add(new PlaneOrderTableEntity("全部"));
        tabs.add(new PlaneOrderTableEntity("待付款"));
        tabs.add(new PlaneOrderTableEntity("待出票"));
        tabs.add(new PlaneOrderTableEntity("已出票"));
        tabs.add(new PlaneOrderTableEntity("退改单"));
        tabLayout.setTabData(tabs);
        refreshLayout.setEnableLoadMore(false);
        initListener();
    }

    public void initListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    btnType = 0;
                    currOrder = allOrder;
                } else if (position == 1) {
                    btnType = 1;
                    currOrder = waiPayList;
                } else if (position == 2) {
                    btnType = 2;
                    currOrder = waitTicketedList;
                } else if (position == 3) {
                    btnType = 3;
                    currOrder = ticketedList;
                } else if (position == 4) {
                    btnType = 4;
                    currOrder = refundOrUpdateList;
                }
                mAdapter.setNewData(currOrder);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PlaneOrderListActivity.this, PlaneOrderDetailActivity.class);
                intent.putExtra(PlaneOrderDetailActivity.ORDER_DATA, currOrder.get(position));
                startActivity(intent);
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
        OkGo.<LzyResponse<PlaneOrderInfo>>get(Urls.GET_PLANE_ORDER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<PlaneOrderInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<PlaneOrderInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<PlaneOrderInfo>> response) {
                        super.onSuccess(PlaneOrderListActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (currOrder != null) {
                            currOrder.clear();
                        }
                        if (response.body().code == 0 && response.body().data != null) {
                            PlaneOrderInfo planeOrderInfo = response.body().data;
                            tabs.clear();
                            tabs.add(new PlaneOrderTableEntity("全部"));
                            tabs.add(new PlaneOrderTableEntity("待付款(" + planeOrderInfo.waiPayCount + ")"));
                            tabs.add(new PlaneOrderTableEntity("待出票(" + planeOrderInfo.waitTicketedCount + ")"));
                            tabs.add(new PlaneOrderTableEntity("已出票(" + planeOrderInfo.ticketedCount + ")"));
                            tabs.add(new PlaneOrderTableEntity("退改单(" + planeOrderInfo.refundOrUpdateCount + ")"));
                            tabLayout.setTabData(tabs);
                            if (planeOrderInfo.all != null) {
                                allOrder = planeOrderInfo.all;
                            }
                            if (planeOrderInfo.refundOrUpdateList != null) {
                                refundOrUpdateList = planeOrderInfo.refundOrUpdateList;
                            }

                            if (planeOrderInfo.ticketedList != null) {
                                ticketedList = planeOrderInfo.ticketedList;
                            }

                            if (planeOrderInfo.waiPayList != null) {
                                waiPayList = planeOrderInfo.waiPayList;
                            }

                            if (planeOrderInfo.waitTicketedList != null) {
                                waitTicketedList = planeOrderInfo.waitTicketedList;
                            }
                            if (btnType == 0) {
                                currOrder = allOrder;
                            } else if (btnType == 1) {
                                currOrder = waiPayList;
                            } else if (btnType == 2) {
                                currOrder = waitTicketedList;
                            } else if (btnType == 3) {
                                currOrder = ticketedList;
                            } else {
                                currOrder = refundOrUpdateList;
                            }
                            if (currOrder != null) {
                                mAdapter.setNewData(currOrder);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Response<LzyResponse<PlaneOrderInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });

    }

    @OnClick({R.id.left_button})
    public void onClick(View view) {
        finish();
    }
}
