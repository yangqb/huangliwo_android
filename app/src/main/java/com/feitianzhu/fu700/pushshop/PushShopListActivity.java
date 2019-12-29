package com.feitianzhu.fu700.pushshop;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.pushshop.adapter.PushShopAdapter;
import com.feitianzhu.fu700.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 11:51
 * email: 694125155@qq.com
 */
public class PushShopListActivity extends BaseActivity {
    private PushShopAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_toAudit)
    TextView btnToAudit;
    @BindView(R.id.btn_pass)
    TextView btnPass;
    @BindView(R.id.btn_noPass)
    TextView btnNoPass;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_img)
    ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop_list;
    }

    @Override
    protected void initView() {
        titleName.setText("推店详情");
        rightText.setText("新增门店");
        imageView.setBackgroundResource(R.mipmap.g01_07xinzeng);
        rightText.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        btnToAudit.setSelected(true);
        btnPass.setSelected(false);
        btnNoPass.setSelected(false);

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            integers.add(i);
        }
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PushShopAdapter(integers);
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.sf_blue));
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PushShopListActivity.this, NoPassReasonActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshing(false);
    }

    @OnClick({R.id.left_button, R.id.btn_toAudit, R.id.btn_pass, R.id.btn_noPass, R.id.right_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_pass:
                List<Integer> integers3 = new ArrayList<>();
                mAdapter.setNewData(integers3);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(true);
                btnNoPass.setSelected(false);
                break;
            case R.id.btn_noPass:
                List<Integer> integers2 = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    integers2.add(i);
                }
                mAdapter.setNewData(integers2);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(false);
                btnNoPass.setSelected(true);
                break;
            case R.id.btn_toAudit:
                List<Integer> integers1 = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    integers1.add(i);
                }
                mAdapter.setNewData(integers1);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(true);
                btnPass.setSelected(false);
                btnNoPass.setSelected(false);
                break;
            case R.id.right_button:
                boolean isAgreed = SPUtils.getBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS);
                if (isAgreed) {
                    intent = new Intent(PushShopListActivity.this, EditMerchantsActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(PushShopListActivity.this, PushShopProtocolActivity.class);
                    intent.putExtra(PushShopProtocolActivity.PUSH_PROTOCOL, false);
                    startActivity(intent);
                }
                break;
        }
    }
}
