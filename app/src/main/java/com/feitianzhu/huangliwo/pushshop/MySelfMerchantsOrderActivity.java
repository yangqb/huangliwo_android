package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.SelfMerchantsOrderAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/3
 * time: 16:45
 * email: 694125155@qq.com
 */
public class MySelfMerchantsOrderActivity extends BaseActivity {
    private SelfMerchantsOrderAdapter selfMerchantsOrderAdapter;
    private List<SelfMerchantsModel> selfMerchantsModels = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_online)
    TextView btnOnline;
    @BindView(R.id.btn_offline)
    TextView btnOffline;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_merchacts_order;
    }

    @Override
    protected void initView() {
        titleName.setText("订单列表");
        btnOffline.setSelected(true);

        for (int i = 0; i < 5; i++) {
            SelfMerchantsModel selfMerchantsModel = new SelfMerchantsModel(SelfMerchantsModel.OFFLINE_TYPE);
            selfMerchantsModel.setOrderInfo(i + "");
            selfMerchantsModels.add(selfMerchantsModel);
        }

        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selfMerchantsOrderAdapter = new SelfMerchantsOrderAdapter(selfMerchantsModels);
        selfMerchantsOrderAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(selfMerchantsOrderAdapter);
        selfMerchantsOrderAdapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
            }
        });
        selfMerchantsOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == SelfMerchantsModel.ONLINE_TYPE) {
                    Intent intent = new Intent(MySelfMerchantsOrderActivity.this, RecordOrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.btn_online, R.id.btn_offline})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_offline:
                selfMerchantsModels.clear();
                for (int i = 0; i < 5; i++) {
                    SelfMerchantsModel selfMerchantsModel = new SelfMerchantsModel(SelfMerchantsModel.OFFLINE_TYPE);
                    selfMerchantsModel.setOrderInfo(i + "");
                    selfMerchantsModels.add(selfMerchantsModel);
                }
                selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                selfMerchantsOrderAdapter.notifyDataSetChanged();
                btnOnline.setSelected(false);
                btnOffline.setSelected(true);
                break;
            case R.id.btn_online:
                selfMerchantsModels.clear();
                for (int i = 0; i < 5; i++) {
                    SelfMerchantsModel selfMerchantsModel = new SelfMerchantsModel(SelfMerchantsModel.ONLINE_TYPE);
                    selfMerchantsModel.setOrderInfo(i + "");
                    selfMerchantsModels.add(selfMerchantsModel);
                }
                selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                selfMerchantsOrderAdapter.notifyDataSetChanged();
                btnOnline.setSelected(true);
                btnOffline.setSelected(false);
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }
}
