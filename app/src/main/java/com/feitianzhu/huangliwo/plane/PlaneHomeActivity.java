package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.SearchFlightModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.necer.enumeration.SelectedModel;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneHomeActivity extends BaseActivity {
    //private List<HotCity> hotCities;
    private int startCityType;
    private int endCityType;
    private static final int DATE_REQUEST_CODE = 100;
    private static final int REQUEST_START_CODE = 111;
    private static final int REQUEST_END_CODE = 222;
    private String tvStartCity = "北京";
    private String tvEndCity = "上海";
    private String startDateStr = "";
    private String endDateStr = "";

    private int anim;
    private boolean enable;
    private boolean reversal;
    private boolean isChildren;
    private boolean isBaby;
    private int searchType; //0国内1国际2国内往返3国际往返
    private String userId;
    private String token;
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
    @BindView(R.id.startCityName)
    TextView startCityName;
    @BindView(R.id.endCityName)
    TextView endCityName;
    @BindView(R.id.tvShoppingSpace)
    TextView tvShoppingSpace;
    @BindView(R.id.select_children)
    ImageView selectChildren;
    @BindView(R.id.select_baby)
    ImageView selectBaby;
    @BindView(R.id.planeSelect)
    ImageView planeSelect;
    @BindView(R.id.trainSelect)
    ImageView trainSelect;
    @BindView(R.id.startDate)
    TextView startDate;
    @BindView(R.id.startWeek)
    TextView startWeek;
    @BindView(R.id.endDate)
    TextView endDate;
    @BindView(R.id.endWeek)
    TextView endWeek;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_home;
    }

    @Override
    protected void initView() {
        titleName.setText("机票");
        planeSelect.setSelected(true);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// HH:mm:ss
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        startDateStr = simpleDateFormat.format(curDate);

        Calendar rightNow = Calendar.getInstance();
        //当前时间 加10天
        rightNow.add(Calendar.DAY_OF_YEAR, 2);
        //new SimgpleDateFormat 进行格式化
        //利用Calendar的getTime方法，将时间转化为Date对象
        //利用SimpleDateFormat对象 把Date对象格式化
        endDateStr = simpleDateFormat.format(rightNow.getTime());
        endDate.setText(endDateStr);
        endWeek.setText(DateUtils.strToDate2(endDateStr));
        startDate.setText(startDateStr);
        startWeek.setText(DateUtils.strToDate2(startDateStr));
    }

    @Override
    protected void initData() {
       /* hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));*/

    }

    @OnClick({R.id.left_button, R.id.btn_domestic, R.id.btn_international, R.id.btn_come_go, R.id.startCityName, R.id.endCityName, R.id.reversalCity,
            R.id.select_shipping_space, R.id.select_children, R.id.select_baby, R.id.reserve_explain, R.id.search, R.id.ll_order, R.id.select_seat, R.id.planeSelect, R.id.trainSelect
            , R.id.ll_goDate, R.id.ll_comeDate})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.planeSelect:
                planeSelect.setSelected(true);
                trainSelect.setSelected(false);
                break;
            case R.id.trainSelect:
                planeSelect.setSelected(false);
                trainSelect.setSelected(true);
                break;
            case R.id.btn_domestic:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_333333));
                btnInternational.setTextColor(getResources().getColor(R.color.color_666666));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_666666));
                line1.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                line3.setBackgroundColor(getResources().getColor(R.color.white));
                llComeDate.setVisibility(View.INVISIBLE);
                searchType = 0;
                break;
            case R.id.btn_international:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_666666));
                btnInternational.setTextColor(getResources().getColor(R.color.color_333333));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_666666));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                line3.setBackgroundColor(getResources().getColor(R.color.white));
                llComeDate.setVisibility(View.INVISIBLE);
                searchType = 1;
                break;
            case R.id.btn_come_go:
                btnDomestic.setTextColor(getResources().getColor(R.color.color_666666));
                btnInternational.setTextColor(getResources().getColor(R.color.color_666666));
                btnComeAndGo.setTextColor(getResources().getColor(R.color.color_333333));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                line3.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                llComeDate.setVisibility(View.VISIBLE);
                searchType = 2;
                break;
            case R.id.startCityName:
                //selectCity(1);
                intent = new Intent(PlaneHomeActivity.this, SelectPlaneCityActivity.class);
                startActivityForResult(intent, REQUEST_START_CODE);
                break;
            case R.id.endCityName:
                //selectCity(2);
                intent = new Intent(PlaneHomeActivity.this, SelectPlaneCityActivity.class);
                startActivityForResult(intent, REQUEST_END_CODE);
                break;
            case R.id.reversalCity:
                if (reversal) {
                    endCityName.setText(tvEndCity);
                    startCityName.setText(tvStartCity);
                } else {
                    startCityName.setText(tvEndCity);
                    endCityName.setText(tvStartCity);
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
                Integer[] integers = new Integer[]{R.mipmap.k01_07yuding};
                new XPopup.Builder(PlaneHomeActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPlaneProtocolView(this
                        ).setTitle("儿童/婴儿票预订说明").setData(Arrays.asList(integers))).show();

                break;
            case R.id.search:
                if (searchType == 2 || searchType == 3) {
                    intent = new Intent(this, SearchPlanActivity2.class);
                    intent.putExtra(SearchPlanActivity2.SEARCH_TYPE, searchType);
                    intent.putExtra(SearchPlanActivity2.FLIGHT_START_DATE, startDateStr);
                    intent.putExtra(SearchPlanActivity2.FLIGHT_END_DATE, endDateStr);
                } else {
                    intent = new Intent(this, SearchPlanActivity.class);
                    intent.putExtra(SearchPlanActivity.SEARCH_TYPE, searchType);
                    intent.putExtra(SearchPlanActivity.FLIGHT_DATE, startDateStr);
                }
                startActivity(intent);
                break;
            case R.id.ll_order:
                intent = new Intent(PlaneHomeActivity.this, PlaneOrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.select_seat:
                intent = new Intent(PlaneHomeActivity.this, PlaneSeatSelectionActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_goDate:
            case R.id.ll_comeDate:
                intent = new Intent(PlaneHomeActivity.this, PlaneCalendarActivity.class);
                intent.putExtra(PlaneCalendarActivity.SELECT_MODEL, searchType);
                startActivityForResult(intent, DATE_REQUEST_CODE);
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

   /* public void selectCity(int type) {
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
                            startCityName.setText(data.getName());
                        } else {
                            tvEndCity = data.getName();
                            startCityName.setText(data.getName());
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

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DATE_REQUEST_CODE) {
                if (searchType == 2 || searchType == 3) {
                    startDateStr = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE).split("=")[0];
                    endDateStr = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE).split("=")[1];
                    startDate.setText(startDateStr);
                    startWeek.setText(DateUtils.strToDate2(startDateStr));
                    endDate.setText(endDateStr);
                    endWeek.setText(DateUtils.strToDate2(endDateStr));
                } else {
                    startDateStr = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE);
                    startDate.setText(startDateStr);
                    startWeek.setText(DateUtils.strToDate2(startDateStr));
                }
            } else if (requestCode == REQUEST_END_CODE) {
                startCityType = data.getIntExtra(SelectPlaneCityActivity.CITY_TYPE, 0);
                if (startCityType == 0 && endCityType == 0) { //国内城市
                    if (searchType == 3) {
                        searchType = 2;
                    } else if (searchType == 1) {
                        searchType = 0;
                    }
                } else {
                    if (searchType == 2) {
                        searchType = 3;
                    } else if (searchType == 0) {
                        searchType = 1;
                    }
                }
            } else if (requestCode == REQUEST_START_CODE) {
                endCityType = data.getIntExtra(SelectPlaneCityActivity.CITY_TYPE, 0);
                if (startCityType == 0 && endCityType == 0) { //国内城市
                    if (searchType == 3) {
                        searchType = 2;
                    } else if (searchType == 1) {
                        searchType = 0;
                    }
                } else {
                    if (searchType == 2) {
                        searchType = 3;
                    } else if (searchType == 0) {
                        searchType = 1;
                    }
                }
            }
        }
    }
}
