package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.FlightSegmentInfo;
import com.feitianzhu.huangliwo.model.InternationalPriceInfo;
import com.feitianzhu.huangliwo.model.MultiPriceInfo;
import com.feitianzhu.huangliwo.model.MultipleGoSearchFightInfo;
import com.feitianzhu.huangliwo.model.PlaneDetailInfo;
import com.feitianzhu.huangliwo.model.PlaneInternationalDetailInfo;
import com.feitianzhu.huangliwo.model.SearchFlightModel;
import com.feitianzhu.huangliwo.model.VenDorsInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolView;
import com.feitianzhu.huangliwo.view.CustomPlaneTransferView;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneDetailActivity extends BaseActivity {
    public static final String DETAIL_TYPE = "detail_type";
    public static final String FLIGHT_DATA = "flight_data";
    public static final String FLIGHT_DATE = "flight_date";
    private String date;
    private PlaneDetailAdapter mAdapter;
    private TransitAdapter transitAdapter;
    private List<MultiPriceInfo> multiPriceInfos = new ArrayList<>();
    private SearchFlightModel.FlightModel flightModel;
    private List<FlightSegmentInfo> flightSegmentInfo;
    private MultipleGoSearchFightInfo goSearchFightInfo;
    private PlaneDetailInfo planeDetailInfo;
    private String userId;
    private String token;
    private int type;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.layout_top1)
    LinearLayout layoutTop1;
    @BindView(R.id.layout_top2)
    LinearLayout layoutTop2;
    @BindView(R.id.btn_title1)
    TextView btnTitle1;
    @BindView(R.id.btn_title2)
    TextView btnTitle2;
    @BindView(R.id.prompt_content)
    TextView promptContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.one_way_startDate)
    TextView oneWayStarDate;
    @BindView(R.id.one_way_endDate)
    TextView oneWayEndDate;
    @BindView(R.id.oneWayBTime)
    TextView oneWayBTime;
    @BindView(R.id.oneWayETime)
    TextView oneWayETime;
    @BindView(R.id.oneWay_arrAirport)
    TextView oneWayArrAirport;
    @BindView(R.id.oneWay_depAirport)
    TextView oneWayDepAirport;
    @BindView(R.id.oneWay_flightTimes)
    TextView oneWayFlightTimes;
    @BindView(R.id.transit_recyclerView)
    RecyclerView transitRecyclerView;
    @BindView(R.id.one_way_crossDays)
    TextView oneWayCrossDays;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_flight_detail;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("");
        type = getIntent().getIntExtra(DETAIL_TYPE, 1);
        goSearchFightInfo = (MultipleGoSearchFightInfo) getIntent().getSerializableExtra(FLIGHT_DATA);
        date = getIntent().getStringExtra(FLIGHT_DATE);
        if (type == 0) {
            flightModel = goSearchFightInfo.flightModel;
            //国内单程直达
            layoutTop1.setVisibility(View.VISIBLE);
            oneWayCrossDays.setVisibility(View.GONE);
            transitRecyclerView.setVisibility(View.GONE);
            promptContent.setText("【机场提示】该航班到达机场为北京大兴国际机场，距市区约46公里，搭乘地铁到市区约30分钟。");
          /*  //国内中转
            layoutTop2.setVisibility(View.VISIBLE);
            btnTitle1.setText("一程");
            btnTitle2.setText("二程");
            String str1 = "如遇其中一程航班调整，您需自行办理另一程退改事宜并承担相应费用。请阅读";
            String str2 = "中转预定须知";
            setSpannableString(str1, str2, type);*/
        } else if (type == 1) {
            flightSegmentInfo = goSearchFightInfo.internationalFlightModel.goTrip.flightSegments;
            if (goSearchFightInfo.internationalFlightModel.goTrip.transitCities != null && goSearchFightInfo.internationalFlightModel.goTrip.transitCities.size() > 0) {
                //国际中转
                layoutTop2.setVisibility(View.GONE);
                layoutTop1.setVisibility(View.GONE);
                btnTitle1.setText("一程");
                btnTitle2.setText("二程");
                String str3 = "【托运行李提示】";
                String str4 = "中转深圳，需在机场重新托运行李";
                setSpannableString(str4, str3, type);
                transitRecyclerView.setVisibility(View.VISIBLE);
                transitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                transitAdapter = new TransitAdapter(flightSegmentInfo);
                transitRecyclerView.setAdapter(transitAdapter);
                transitAdapter.notifyDataSetChanged();
            } else {
                //国际直达
                transitRecyclerView.setVisibility(View.GONE);
                layoutTop1.setVisibility(View.VISIBLE);
                promptContent.setText("【机场提示】该航班到达机场为北京大兴国际机场，距市区约46公里，搭乘地铁到市区约30分钟。");

                oneWayStarDate.setText(DateUtils.strToStr(flightSegmentInfo.get(0).depDate) + DateUtils.strToDate2(flightSegmentInfo.get(0).depDate));
                oneWayEndDate.setText(DateUtils.strToStr(flightSegmentInfo.get(0).arrdate) + DateUtils.strToDate2(flightSegmentInfo.get(0).arrdate));
                oneWayBTime.setText(flightSegmentInfo.get(0).depTime);
                oneWayETime.setText(flightSegmentInfo.get(0).arrTime);
                oneWayDepAirport.setText(flightSegmentInfo.get(0).depAirportName);
                oneWayArrAirport.setText(flightSegmentInfo.get(0).arrAirportName);
                oneWayFlightTimes.setText(DateUtils.minToHour(flightSegmentInfo.get(0).duration));
                if (flightSegmentInfo.get(0).crossDays == 0) {
                    oneWayCrossDays.setVisibility(View.GONE);
                } else {
                    oneWayCrossDays.setVisibility(View.VISIBLE);
                    oneWayCrossDays.setText("+" + flightSegmentInfo.get(0).crossDays + "天");
                }
            }
        } else {

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaneDetailAdapter(multiPriceInfos);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        initListener();
    }

    public void initListener() {

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.luggage_change_notice:
                        new XPopup.Builder(PlaneDetailActivity.this)
                                .enableDrag(false)
                                .asCustom(new CustomCancelChangePopView(PlaneDetailActivity.this
                                ).setType(type).setLuggage(true)).show();
                        break;
                    case R.id.btn_reserve:
                        Intent intent = new Intent(PlaneDetailActivity.this, EditPlaneReserveActivity.class);
                        intent.putExtra(EditPlaneReserveActivity.PLANE_TYPE, type);
                        if (type == 0) {
                            intent.putExtra(EditPlaneReserveActivity.PLANE_DETAIL_DATA, planeDetailInfo);
                        } else if (type == 1) {
                            intent.putExtra(EditPlaneReserveActivity.PLANE_DETAIL_DATA, goSearchFightInfo.internationalFlightModel.goTrip);
                        }
                        intent.putExtra(EditPlaneReserveActivity.PRICE_DATA, multiPriceInfos.get(position));
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (type == 0) {
            OkGo.<PlaneResponse<PlaneDetailInfo>>get(Urls.SEARCH_PRICE_FLIGHT)
                    .tag(this)
                    .params("dpt", flightModel.dpt)
                    .params("arr", flightModel.arr)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("date", date)
                    .params("carrier", flightModel.carrier)
                    .params("flightNum", flightModel.flightNum)
                    .params("cabin", flightModel.cabin)
                    .params("ex_track", "tehui")
                    .execute(new JsonCallback<PlaneResponse<PlaneDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<PlaneDetailInfo>> response) {
                            super.onSuccess(PlaneDetailActivity.this, response.body().message, response.body().code);
                            //SearchFlightModel searchFlightModel = response.body().result;
                            planeDetailInfo = response.body().result;
                            planeDetailInfo.flightTimes = flightModel.flightTimes;
                            showView(planeDetailInfo);
                        }

                        @Override
                        public void onError(Response<PlaneResponse<PlaneDetailInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else if (type == 1) {
            OkGo.<PlaneResponse<PlaneInternationalDetailInfo>>get(Urls.SEARCH_PRICE_INTERNATIONAL_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", flightSegmentInfo.get(0).depCityCode)
                    .params("arrCity", flightSegmentInfo.get(flightSegmentInfo.size() - 1).arrCityCode)
                    .params("depDate", date)
                    //.params("retDate")
                    .params("source", "ICP_SELECT_open.3724")
                    .params("flightCode", goSearchFightInfo.internationalFlightModel.flightCode)
                    //.params("adultNum")
                    //.params("childNum")
                    //.params("cabinLevel")
                    .execute(new JsonCallback<PlaneResponse<PlaneInternationalDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<PlaneInternationalDetailInfo>> response) {
                            super.onSuccess(PlaneDetailActivity.this, response.body().message, response.body().code);
                            PlaneInternationalDetailInfo internationalDetailInfo = response.body().result;
                            multiPriceInfos.clear();
                            if (internationalDetailInfo.priceInfo != null && internationalDetailInfo.priceInfo.size() > 0) {
                                for (int i = 0; i < internationalDetailInfo.priceInfo.size(); i++) {
                                    MultiPriceInfo priceInfo = new MultiPriceInfo(MultiPriceInfo.INTERNATIONAL_TYPE);
                                    priceInfo.internationalPriceInfo = internationalDetailInfo.priceInfo.get(i);
                                    multiPriceInfos.add(priceInfo);
                                }
                                mAdapter.setNewData(multiPriceInfos);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<PlaneInternationalDetailInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else {
            //国内国际往返
            layoutTop2.setVisibility(View.VISIBLE);
            btnTitle1.setText("去程");
            btnTitle2.setText("返程");
            promptContent.setText("【风险提示】此报价是组合产品，如遇其中一程航班调整，您可办理另一程退改事宜，需自行承担相应费用，详见退改详情。");
        }
    }

    public void showView(PlaneDetailInfo planeDetailInfo) {
        oneWayStarDate.setText(planeDetailInfo.date + DateUtils.strToDate2(planeDetailInfo.date));
        oneWayEndDate.setText(planeDetailInfo.date + DateUtils.strToDate2(planeDetailInfo.date));
        oneWayBTime.setText(planeDetailInfo.btime);
        oneWayETime.setText(planeDetailInfo.etime);
        oneWayDepAirport.setText(planeDetailInfo.depAirport + planeDetailInfo.depTerminal);
        oneWayArrAirport.setText(planeDetailInfo.arrAirport + planeDetailInfo.arrTerminal);
        oneWayFlightTimes.setText(flightModel.flightTimes);
        multiPriceInfos.clear();
        if (planeDetailInfo.vendors != null && planeDetailInfo.vendors.size() > 0) {
            for (int i = 0; i < planeDetailInfo.vendors.size(); i++) {
                MultiPriceInfo priceInfo = new MultiPriceInfo(MultiPriceInfo.DOMESTIC_TYPE);
                priceInfo.venDorsInfo = planeDetailInfo.vendors.get(i);
                multiPriceInfos.add(priceInfo);
            }
            mAdapter.setNewData(multiPriceInfos);
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str1, String str2, int type) {
        promptContent.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#28B5FE"));
        span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan3, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (type == 3) {
                    new XPopup.Builder(PlaneDetailActivity.this)
                            .enableDrag(false)
                            .asCustom(new CustomPlaneTransferView(PlaneDetailActivity.this
                            )).show();
                } else {

                }
            }
        }, 0, span2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (type == 3) {
            promptContent.append(span1);
            promptContent.append(span2);
        } else if (type == 4) {
            promptContent.append(span2);
            promptContent.append(span1);
        }
        promptContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
