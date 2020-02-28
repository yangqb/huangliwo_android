package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MultiPlaneOrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneOrderListActivity extends BaseActivity {
    private List<MultiPlaneOrderModel> multiPlaneOrderModels = new ArrayList<>();
    private MultiPlaneOrderModel multiPlaneOrderModel;
    private PlaneOrderAdapter mAdapter;
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
        titleName.setText("机票订单");
        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                multiPlaneOrderModel = new MultiPlaneOrderModel(MultiPlaneOrderModel.GO_TYPE);
            } else {
                multiPlaneOrderModel = new MultiPlaneOrderModel(MultiPlaneOrderModel.GO_COME_TYPE);
            }
            multiPlaneOrderModels.add(multiPlaneOrderModel);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaneOrderAdapter(multiPlaneOrderModels);
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

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
