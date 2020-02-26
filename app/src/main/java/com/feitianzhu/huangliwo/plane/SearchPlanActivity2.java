package com.feitianzhu.huangliwo.plane;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchPlanActivity2 extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.go_recyclerView)
    RecyclerView goRecyclerView;
    @BindView(R.id.come_recyclerView)
    RecyclerView comeRecyclerView;
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
        return R.layout.activity_search_plane2;
    }

    @Override
    protected void initView() {
        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText("北京");
        endCity.setText("上海");
        centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(i + "");
        }
        SearchPlaneResultAdapter2 mAdapter = new SearchPlaneResultAdapter2(list);
        SearchPlaneResultAdapter2 mAdapter2 = new SearchPlaneResultAdapter2(list);
        goRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        comeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        comeRecyclerView.setAdapter(mAdapter2);
        mAdapter2.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
