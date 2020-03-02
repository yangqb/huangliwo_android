package com.feitianzhu.huangliwo.plane;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneChangeDetailActivity extends BaseActivity {
    public static final String PLANE_TYPE = "plane_type";
    private int planeType;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_go_come)
    LinearLayout ll_go_come;
    @BindView(R.id.ll_go)
    LinearLayout ll_go;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_change_detail;
    }

    @Override
    protected void initView() {
        planeType = getIntent().getIntExtra(PLANE_TYPE, 1);
        if (planeType == 1) {
            ll_go.setVisibility(View.VISIBLE);
            ll_go_come.setVisibility(View.GONE);
        } else {
            ll_go_come.setVisibility(View.VISIBLE);
            ll_go.setVisibility(View.GONE);
        }

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        PlaneChangeDetailAdapter mAdapter = new PlaneChangeDetailAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
