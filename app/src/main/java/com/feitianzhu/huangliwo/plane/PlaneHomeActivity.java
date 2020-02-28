package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.vip.VipActivity;
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

public class PlaneHomeActivity extends BaseActivity {
    private List<HotCity> hotCities;
    private String tvStartCity = "北京";
    private String tvEndCity = "上海";
    private int anim;
    private boolean enable;
    private boolean reversal;
    private boolean isChildren;
    private boolean isBaby;
    private boolean isComeAndGo;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.btn_domestic)
    TextView btnDomestic;
    @BindView(R.id.btn_international)
    TextView btnInternational;
    @BindView(R.id.btn_come_go)
    TextView btnComeAndGo;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.ll_comeDate)
    LinearLayout llComeDate;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.tvShoppingSpace)
    TextView tvShoppingSpace;
    @BindView(R.id.select_children)
    ImageView selectChildren;
    @BindView(R.id.select_baby)
    ImageView selectBaby;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_home;
    }

    @Override
    protected void initView() {
        titleName.setText("机票");
    }

    @Override
    protected void initData() {
        hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
    }

    @OnClick({R.id.left_button, R.id.btn_domestic, R.id.btn_international, R.id.btn_come_go, R.id.startCity, R.id.endCity, R.id.reversalCity,
            R.id.select_shipping_space, R.id.select_children, R.id.select_baby, R.id.reserve_explain, R.id.search, R.id.ll_order, R.id.select_seat})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_domestic:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_333333));
                btnInternational.setTextColor(getResources().getColor(R.color.color_666666));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_666666));
                line1.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                line3.setBackgroundColor(getResources().getColor(R.color.white));
                llComeDate.setVisibility(View.INVISIBLE);
                isComeAndGo = false;
                break;
            case R.id.btn_international:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_666666));
                btnInternational.setTextColor(getResources().getColor(R.color.color_333333));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_666666));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                line3.setBackgroundColor(getResources().getColor(R.color.white));
                llComeDate.setVisibility(View.INVISIBLE);
                isComeAndGo = false;
                break;
            case R.id.btn_come_go:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_666666));
                btnInternational.setTextColor(getResources().getColor(R.color.color_666666));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_333333));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                line3.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                llComeDate.setVisibility(View.VISIBLE);
                isComeAndGo = true;
                break;
            case R.id.startCity:
                selectCity(1);
                break;
            case R.id.endCity:
                selectCity(2);
                break;
            case R.id.reversalCity:
                if (reversal) {
                    endCity.setText(tvEndCity);
                    startCity.setText(tvStartCity);
                } else {
                    startCity.setText(tvEndCity);
                    endCity.setText(tvStartCity);
                }
                reversal = !reversal;
                break;
            case R.id.select_shipping_space:
                selectShoppingSpaceDialog();
                break;
            case R.id.select_children:
                isChildren = !isChildren;
                if (isChildren) {
                    selectChildren.setBackgroundResource(R.mipmap.k01_04xuanzhong);
                } else {
                    selectChildren.setBackgroundResource(R.mipmap.k01_03weixuanzhong);
                }
                break;
            case R.id.select_baby:
                isBaby = !isBaby;
                if (isBaby) {
                    selectBaby.setBackgroundResource(R.mipmap.k01_04xuanzhong);
                } else {
                    selectBaby.setBackgroundResource(R.mipmap.k01_03weixuanzhong);
                }
                break;
            case R.id.reserve_explain:
                break;
            case R.id.search:
                if (isComeAndGo) {
                    intent = new Intent(this, SearchPlanActivity2.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, SearchPlanActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_order:
                intent = new Intent(PlaneHomeActivity.this, PlaneOrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.select_seat:
                intent = new Intent(PlaneHomeActivity.this, PlaneSeatSelectionActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void selectShoppingSpaceDialog() {
        String[] strings = new String[]{"舱位不限", "经济舱", "头等/商务舱"};
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(PlaneHomeActivity.this)
                        .setData(Arrays.asList(strings))
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                tvShoppingSpace.setText(strings[position]);
                            }
                        }))
                .show();
    }

    public void selectCity(int type) {
        CityPicker.from(PlaneHomeActivity.this)
                .enableAnimation(enable)
                .setAnimationStyle(anim)
                .setLocatedCity(null)
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        //startCity.setText(String.format("当前城市：%s，%s", data.getName(), data.getCode()));
                        if (type == 1) {
                            tvStartCity = data.getName();
                            startCity.setText(data.getName());
                        } else {
                            tvEndCity = data.getName();
                            endCity.setText(data.getName());
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
                                CityPicker.from(PlaneHomeActivity.this).locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 3000);
                    }
                })
                .show();

    }
}
