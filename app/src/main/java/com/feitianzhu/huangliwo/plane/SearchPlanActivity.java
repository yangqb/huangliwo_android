package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchPlanActivity extends BaseActivity {
    private SearchPlaneResultAdapter mAdapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.plane_title)
    LinearLayout planeTitle;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.center_img)
    ImageView centerImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_plan;
    }

    @Override
    protected void initView() {
        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText("北京");
        endCity.setText("上海");
        centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
        rightImg.setBackgroundResource(R.mipmap.k01_10gengduo);
        rightText.setText("更多日期");
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(i + "");
        }
        mAdapter = new SearchPlaneResultAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchPlanActivity.this, PlaneDetailActivity.class);
                if (position % 4 == 0) {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, 1);
                } else if (position % 4 == 1) {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, 2);
                } else if (position % 4 == 2) {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, 3);
                } else {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, 4);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                break;
        }
    }
}
