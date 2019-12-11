package com.feitianzhu.fu700.me;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.adapter.DetailedRulesAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 3:24
 */
public class DetailedRulesActivity extends BaseActivity {
    private DetailedRulesAdapter adapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_bonus)
    TextView btnBonus;
    @BindView(R.id.btn_discount)
    TextView btnDiscount;
    @BindView(R.id.btn_earnings)
    TextView btnEarnings;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed_rules;
    }

    @Override
    protected void initView() {
        titleName.setText("细则");
        btnBonus.setSelected(true);
        btnDiscount.setSelected(false);
        btnEarnings.setSelected(false);
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            integers.add(i);
        }
        adapter = new DetailedRulesAdapter(integers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @OnClick({R.id.left_button, R.id.btn_bonus, R.id.btn_discount, R.id.btn_earnings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_bonus:
                btnBonus.setSelected(true);
                btnDiscount.setSelected(false);
                btnEarnings.setSelected(false);
                break;
            case R.id.btn_discount:
                btnBonus.setSelected(false);
                btnDiscount.setSelected(true);
                btnEarnings.setSelected(false);
                break;
            case R.id.btn_earnings:
                btnBonus.setSelected(false);
                btnDiscount.setSelected(false);
                btnEarnings.setSelected(true);
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
