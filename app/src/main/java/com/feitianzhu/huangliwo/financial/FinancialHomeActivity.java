package com.feitianzhu.huangliwo.financial;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.financial
 * user: yangqinbo
 * date: 2020/3/30
 * time: 13:59
 * email: 694125155@qq.com
 */
public class FinancialHomeActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_financial_home;
    }

    @Override
    protected void initView() {
        titleName.setText("金融");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FinancialHomeAdapter financialHomeAdapter = new FinancialHomeAdapter(list);
        recyclerView.setAdapter(financialHomeAdapter);
        financialHomeAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
