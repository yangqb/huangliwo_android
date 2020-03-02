package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneChangeActivity extends BaseActivity {
    private int anim;
    private boolean enable;
    private List<HotCity> hotCities;
    public static final String PLANE_TYPE = "plane_type";
    private RefundPlaneTicketPassengerAdapter mAdapter;
    private PlaneChangeServiceAdapter serviceAdapter;
    private int planeType;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.serviceRecyclerView)
    RecyclerView serviceRecyclerView;
    @BindView(R.id.tvStartCityName)
    TextView tvStartCityName;
    @BindView(R.id.tvEndCityName)
    TextView tvEndCityName;
    @BindView(R.id.tvShippingSpace)
    TextView tvShippingSpace;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_plane_change;
    }

    @Override
    protected void initView() {
        hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        titleName.setText("申请改签");
        planeType = getIntent().getIntExtra(PLANE_TYPE, 1);
        if (planeType == 1) {

        } else {

        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        mAdapter = new RefundPlaneTicketPassengerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        serviceAdapter = new PlaneChangeServiceAdapter(list);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceAdapter.notifyDataSetChanged();

        recyclerView.setNestedScrollingEnabled(false);
        serviceRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.btn_bottom, R.id.rl_startCity, R.id.rl_endCity, R.id.rl_shippingSpace})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.rl_startCity:
                selectCity(1);
                break;
            case R.id.rl_endCity:
                selectCity(2);
                break;
            case R.id.rl_shippingSpace:
                selectShoppingSpaceDialog();
                break;
            case R.id.btn_bottom:
                Intent intent = new Intent(PlaneChangeActivity.this, PlaneChangeDetailActivity.class);
                intent.putExtra(PlaneChangeDetailActivity.PLANE_TYPE, planeType);
                startActivity(intent);
                break;
        }
    }

    public void selectCity(int type) {
        CityPicker.from(PlaneChangeActivity.this)
                .enableAnimation(enable)
                .setAnimationStyle(anim)
                .setLocatedCity(null)
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        //startCity.setText(String.format("当前城市：%s，%s", data.getName(), data.getCode()));
                        if (type == 1) {
                            tvStartCityName.setText(data.getName());
                        } else {
                            tvEndCityName.setText(data.getName());
                        }
                        Toast.makeText(
                                getApplicationContext(),
                                String.format("点击的数据：%s，%s", data.getName(), data.getCode()),
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CityPicker.from(PlaneChangeActivity.this).locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 3000);
                    }
                })
                .show();

    }

    public void selectShoppingSpaceDialog() {
        String[] strings = new String[]{"舱位不限", "经济舱", "头等/商务舱"};
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(PlaneChangeActivity.this)
                        .setData(Arrays.asList(strings))
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                tvShippingSpace.setText(strings[position]);
                            }
                        }))
                .show();
    }
}
