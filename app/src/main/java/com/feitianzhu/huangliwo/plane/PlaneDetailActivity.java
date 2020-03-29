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
import com.feitianzhu.huangliwo.model.CustomPlaneDetailInfo;
import com.feitianzhu.huangliwo.model.FlightSegmentInfo;
import com.feitianzhu.huangliwo.model.InternationalPriceInfo;
import com.feitianzhu.huangliwo.model.MultiGoBackFlightInfo;
import com.feitianzhu.huangliwo.model.MultiPriceInfo;
import com.feitianzhu.huangliwo.model.MultipleGoSearchFightInfo;
import com.feitianzhu.huangliwo.model.PlaneDetailInfo;
import com.feitianzhu.huangliwo.model.PlaneGoBackDetailInfo;
import com.feitianzhu.huangliwo.model.PlaneInternationalDetailInfo;
import com.feitianzhu.huangliwo.model.SearchFlightModel;
import com.feitianzhu.huangliwo.model.VenDorsInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolView;
import com.feitianzhu.huangliwo.view.CustomPlaneTransferView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneDetailActivity extends BaseActivity {
    public static final String DETAIL_TYPE = "detail_type";
    public static final String FLIGHT_DATA = "flight_data";
    public static final String FLIGHT_START_DATE = "flight_start_date";
    public static final String FLIGHT_END_DATE = "flight_end_date";
    private String depDate;
    private String retDate;
    private PlaneDetailAdapter mAdapter;
    private TransitAdapter transitAdapter;
    private List<MultiPriceInfo> multiPriceInfos = new ArrayList<>();
    private SearchFlightModel.FlightModel flightModel;
    private List<FlightSegmentInfo> flightSegmentInfo;
    private MultipleGoSearchFightInfo goSearchFightInfo;
    private MultiGoBackFlightInfo goBackSearchFlightInfo;
    private PlaneDetailInfo planeDetailInfo;
    private PlaneInternationalDetailInfo internationalDetailInfo;
    private CustomPlaneDetailInfo customPlaneDetailInfo = new CustomPlaneDetailInfo();
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
    @BindView(R.id.one_way_companyName)
    TextView oneWayCompanyName;
    @BindView(R.id.twoWay_go_depTime)
    TextView twoWayGoDepTime;
    @BindView(R.id.twoWay_go_arrTime)
    TextView twoWayGoArrTime;
    @BindView(R.id.twoWay_go_depAirportName)
    TextView twoWayGoDepAirportName;
    @BindView(R.id.twoWay_go_arrAirportName)
    TextView twoWayGoArrAirportName;
    @BindView(R.id.twoWay_go_flightTime)
    TextView twoWayGoFlightTime;
    @BindView(R.id.twoWay_go_date)
    TextView twoWayGoDate;
    @BindView(R.id.twoWay_go_cityName)
    TextView twoWayGoCityName;
    @BindView(R.id.twoWay_back_date)
    TextView twoWayBackDate;
    @BindView(R.id.twoWay_back_cityName)
    TextView twoWayBackCityName;
    @BindView(R.id.twoWay_back_depTime)
    TextView twoWayBackDepTime;
    @BindView(R.id.twoWay_back_arrTime)
    TextView twoWayBackArrTime;
    @BindView(R.id.twoWay_back_flightTime)
    TextView twoWayBackFlightTime;
    @BindView(R.id.twoWay_back_depAirportName)
    TextView twoWayBackDepAirportName;
    @BindView(R.id.twoWay_back_arrAirportName)
    TextView twoWayBackArrAirportName;
    @BindView(R.id.twoWay_go_CrossDays)
    TextView twoWayGoCrossDays;
    @BindView(R.id.twoWay_back_CrossDays)
    TextView twoWayBackCrossDays;
    @BindView(R.id.twoWay_back_com)
    TextView twoWayBackCom;
    @BindView(R.id.twoWay_go_com)
    TextView twoWayGoCom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_flight_detail;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("");
        type = getIntent().getIntExtra(DETAIL_TYPE, 0);
        if (type == 0 || type == 1) {
            //单程
            goSearchFightInfo = (MultipleGoSearchFightInfo) getIntent().getSerializableExtra(FLIGHT_DATA);
        } else {
            //往返
            goBackSearchFlightInfo = (MultiGoBackFlightInfo) getIntent().getSerializableExtra(FLIGHT_DATA);
        }

        depDate = getIntent().getStringExtra(FLIGHT_START_DATE);
        retDate = getIntent().getStringExtra(FLIGHT_END_DATE);
        if (type == 0) {
            flightModel = goSearchFightInfo.flightModel;
            //国内单程直达
            layoutTop1.setVisibility(View.VISIBLE);
            layoutTop2.setVisibility(View.GONE);
            oneWayCrossDays.setVisibility(View.GONE);
            transitRecyclerView.setVisibility(View.GONE);
            promptContent.setText("【机场提示】该航班到达机场为北京大兴国际机场，距市区约46公里，搭乘地铁到市区约30分钟。");
            oneWayStarDate.setText(depDate + DateUtils.strToDate2(depDate));
            oneWayEndDate.setText(depDate + DateUtils.strToDate2(depDate));
            oneWayBTime.setText(flightModel.dptTime);
            oneWayETime.setText(flightModel.arrTime);
            oneWayDepAirport.setText(flightModel.dptAirport + flightModel.dptTerminal);
            oneWayArrAirport.setText(flightModel.arrAirport + flightModel.arrTerminal);
            oneWayFlightTimes.setText(flightModel.flightTimes);
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
                transitRecyclerView.setNestedScrollingEnabled(false);
            } else {
                //国际直达
                transitRecyclerView.setVisibility(View.GONE);
                layoutTop1.setVisibility(View.VISIBLE);
                layoutTop2.setVisibility(View.GONE);
                promptContent.setText("【机场提示】该航班到达机场为北京大兴国际机场，距市区约46公里，搭乘地铁到市区约30分钟。");
                oneWayCompanyName.setText(flightSegmentInfo.get(0).mainCarrierShortName + flightSegmentInfo.get(0).mainCode + flightSegmentInfo.get(0).planeTypeName + "无餐\n以下价格含机建燃油");
                oneWayStarDate.setText(DateUtils.strToStr(flightSegmentInfo.get(0).depDate) + DateUtils.strToDate2(flightSegmentInfo.get(0).depDate));
                oneWayEndDate.setText(DateUtils.strToStr(flightSegmentInfo.get(0).arrdate) + DateUtils.strToDate2(flightSegmentInfo.get(0).arrdate));
                oneWayBTime.setText(flightSegmentInfo.get(0).depTime);
                oneWayETime.setText(flightSegmentInfo.get(0).arrTime);
                oneWayDepAirport.setText(flightSegmentInfo.get(0).depAirportName + flightSegmentInfo.get(0).depTerminal);
                oneWayArrAirport.setText(flightSegmentInfo.get(0).arrAirportName + flightSegmentInfo.get(0).arrTerminal);
                oneWayFlightTimes.setText(DateUtils.minToHour(flightSegmentInfo.get(0).duration));
                if (flightSegmentInfo.get(0).crossDays == 0) {
                    oneWayCrossDays.setVisibility(View.GONE);
                } else {
                    oneWayCrossDays.setVisibility(View.VISIBLE);
                    oneWayCrossDays.setText("+" + flightSegmentInfo.get(0).crossDays + "天");
                }
            }
        } else if (type == 2) {
            //国内往返
            btnTitle1.setText("去程");
            btnTitle2.setText("返程");
            promptContent.setText("【风险提示】此报价是组合产品，如遇其中一程航班调整，您可办理另一程退改事宜，需自行承担相应费用，详见退改详情。");
            layoutTop2.setVisibility(View.VISIBLE);
            layoutTop1.setVisibility(View.GONE);
            transitRecyclerView.setVisibility(View.GONE);
            twoWayGoDepTime.setText(goBackSearchFlightInfo.domesticFlight.go.depTime);
            twoWayGoArrTime.setText(goBackSearchFlightInfo.domesticFlight.go.arrTime);
            twoWayGoDepAirportName.setText(goBackSearchFlightInfo.domesticFlight.go.depAirport + goBackSearchFlightInfo.domesticFlight.go.depTerminal);
            twoWayGoArrAirportName.setText(goBackSearchFlightInfo.domesticFlight.go.arrAirport + goBackSearchFlightInfo.domesticFlight.go.arrTerminal);
            twoWayGoFlightTime.setText(goBackSearchFlightInfo.domesticFlight.go.flightTimes);
            twoWayGoDate.setText(DateUtils.strToStr(depDate) + DateUtils.strToDate2(depDate));
            twoWayGoCityName.setText(goBackSearchFlightInfo.domesticFlight.go.depAirportCode + "-" + goBackSearchFlightInfo.domesticFlight.go.arrAirportCode);
            twoWayBackDate.setText(DateUtils.strToStr(retDate) + DateUtils.strToDate2(retDate));
            twoWayBackCityName.setText(goBackSearchFlightInfo.domesticFlight.back.depAirportCode + "-" + goBackSearchFlightInfo.domesticFlight.back.arrAirportCode);
            twoWayBackDepTime.setText(goBackSearchFlightInfo.domesticFlight.back.depTime);
            twoWayBackArrTime.setText(goBackSearchFlightInfo.domesticFlight.back.arrTime);
            twoWayBackFlightTime.setText(goBackSearchFlightInfo.domesticFlight.back.flightTimes);
            twoWayBackDepAirportName.setText(goBackSearchFlightInfo.domesticFlight.back.depAirport + goBackSearchFlightInfo.domesticFlight.back.depTerminal);
            twoWayBackArrAirportName.setText(goBackSearchFlightInfo.domesticFlight.back.arrAirport + goBackSearchFlightInfo.domesticFlight.back.arrTerminal);
            twoWayGoCrossDays.setVisibility(View.GONE);
            twoWayBackCrossDays.setVisibility(View.GONE);
            twoWayGoCom.setText(goBackSearchFlightInfo.domesticFlight.go.carrierName + goBackSearchFlightInfo.domesticFlight.go.flightCode + goBackSearchFlightInfo.domesticFlight.go.flightTypeFullName + "无餐\n以下价格不含机建燃油");
            twoWayBackCom.setText(goBackSearchFlightInfo.domesticFlight.back.carrierName + goBackSearchFlightInfo.domesticFlight.back.flightCode + goBackSearchFlightInfo.domesticFlight.back.flightTypeFullName + "无餐\n以下价格不含机建燃油");
        } else {
            //国际往返
            btnTitle1.setText("去程");
            btnTitle2.setText("返程");
            promptContent.setText("【风险提示】此报价是组合产品，如遇其中一程航班调整，您可办理另一程退改事宜，需自行承担相应费用，详见退改详情。");
            layoutTop2.setVisibility(View.VISIBLE);
            layoutTop1.setVisibility(View.GONE);
            transitRecyclerView.setVisibility(View.GONE);
            twoWayGoDepTime.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).depTime);
            twoWayGoArrTime.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.size() - 1).arrTime);
            twoWayGoDepAirportName.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).depAirportName);
            twoWayGoArrAirportName.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.size() - 1).arrAirportName);
            twoWayGoFlightTime.setText(DateUtils.minToHour(goBackSearchFlightInfo.internationalFlight.goTrip.duration));
            twoWayGoDate.setText(DateUtils.strToStr(depDate) + DateUtils.strToDate2(depDate));
            twoWayGoCityName.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).depCityName + "-" + goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.size() - 1).arrCityName);
            twoWayBackDate.setText(DateUtils.strToStr(retDate) + DateUtils.strToDate2(retDate));
            twoWayBackCityName.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).depCityName + "-" + goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.size() - 1).arrCityName);
            twoWayBackDepTime.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).depTime);
            twoWayBackArrTime.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.size() - 1).arrTime);
            twoWayBackFlightTime.setText(DateUtils.minToHour(goBackSearchFlightInfo.internationalFlight.backTrip.duration));
            twoWayBackDepAirportName.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).depAirportName);
            twoWayBackArrAirportName.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.size() - 1).arrAirportName);
            twoWayGoCom.setText(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).mainCarrierShortName + goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).mainCode + goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).planeTypeName + "无餐\n以下价格含机建燃油");
            twoWayBackCom.setText(goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).mainCarrierShortName + goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).mainCode + goBackSearchFlightInfo.internationalFlight.backTrip.flightSegments.get(0).planeTypeName + "无餐\n以下价格含机建燃油");

            if (goBackSearchFlightInfo.internationalFlight.goTrip.crossDays == 0) {
                twoWayGoCrossDays.setVisibility(View.GONE);
            } else {
                twoWayGoCrossDays.setVisibility(View.VISIBLE);
                twoWayGoCrossDays.setText("+" + goBackSearchFlightInfo.internationalFlight.goTrip.crossDays + "天");
            }
            if (goBackSearchFlightInfo.internationalFlight.backTrip.crossDays == 0) {
                twoWayBackCrossDays.setVisibility(View.GONE);
            } else {
                twoWayBackCrossDays.setVisibility(View.VISIBLE);
                twoWayBackCrossDays.setText("+" + goBackSearchFlightInfo.internationalFlight.backTrip.crossDays + "天");
            }
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
                            customPlaneDetailInfo.customDocGoFlightInfo = planeDetailInfo;
                            customPlaneDetailInfo.customDocGoPriceInfo = multiPriceInfos.get(position).venDorsInfo;
                            customPlaneDetailInfo.goDate = depDate;
                        } else if (type == 1) {
                            customPlaneDetailInfo.customInterFlightInfo = goSearchFightInfo.internationalFlightModel;
                            customPlaneDetailInfo.customInterPriceInfo = multiPriceInfos.get(position).internationalPriceInfo;
                            customPlaneDetailInfo.goDate = depDate;
                        } else if (type == 2) {
                            customPlaneDetailInfo.customDocGoBackFlightInfo = goBackSearchFlightInfo.domesticFlight;
                            customPlaneDetailInfo.customDocGoBackPriceInfo = multiPriceInfos.get(position).goBackVendors;
                            customPlaneDetailInfo.goDate = depDate;
                            customPlaneDetailInfo.backDate = retDate;
                        } else {
                            customPlaneDetailInfo.customInterFlightInfo = goBackSearchFlightInfo.internationalFlight;
                            customPlaneDetailInfo.customInterPriceInfo = multiPriceInfos.get(position).internationalPriceInfo;
                            customPlaneDetailInfo.goDate = depDate;
                            customPlaneDetailInfo.backDate = retDate;
                        }
                        intent.putExtra(EditPlaneReserveActivity.PLANE_DETAIL_DATA, customPlaneDetailInfo);
                        // intent.putExtra(EditPlaneReserveActivity.PRICE_DATA, multiPriceInfos.get(position));
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
                    .params("date", depDate)
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
                            oneWayCompanyName.setText(planeDetailInfo.com + planeDetailInfo.code + flightModel.flightTypeFullName + (planeDetailInfo.meal ? "有餐" : "无餐") + "\n以下价格不含机建燃油");
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

                        @Override
                        public void onError(Response<PlaneResponse<PlaneDetailInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else if (type == 2) {
            OkGo.<PlaneResponse<PlaneGoBackDetailInfo>>get(Urls.SEARCH_GO_BACK_PRICE_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", goBackSearchFlightInfo.domesticFlight.go.depAirportCode)
                    .params("arrCity", goBackSearchFlightInfo.domesticFlight.go.arrAirportCode)
                    .params("goDate", depDate)
                    .params("backDate", retDate)
                    .params("flightCodes", goBackSearchFlightInfo.domesticFlight.flightCodes)
                    .params("exTrack", "retehui")
                    .execute(new JsonCallback<PlaneResponse<PlaneGoBackDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<PlaneGoBackDetailInfo>> response) {
                            super.onSuccess(PlaneDetailActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                PlaneGoBackDetailInfo goBackDetailInfo = response.body().result;
                                if (goBackDetailInfo.packVendors != null && goBackDetailInfo.packVendors.size() > 0) {
                                    for (int i = 0; i < goBackDetailInfo.packVendors.size(); i++) {
                                        MultiPriceInfo priceInfo = new MultiPriceInfo(MultiPriceInfo.DOMESTIC_GO_BACK_TYPE);
                                        priceInfo.goBackVendors = goBackDetailInfo.packVendors.get(i);
                                        multiPriceInfos.add(priceInfo);
                                    }
                                    mAdapter.setNewData(multiPriceInfos);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<PlaneGoBackDetailInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else {
            GetRequest<PlaneResponse<PlaneInternationalDetailInfo>> getRequest = OkGo.<PlaneResponse<PlaneInternationalDetailInfo>>get(Urls.SEARCH_PRICE_INTERNATIONAL_FLIGHT)
                    .tag(this);
            if (type != 1) {
                //国际往返
                getRequest.params("retDate", retDate)
                        .params("depCity", goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(0).depCityCode)
                        .params("arrCity", goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.get(goBackSearchFlightInfo.internationalFlight.goTrip.flightSegments.size() - 1).arrCityCode)
                        .params("flightCode", goBackSearchFlightInfo.internationalFlight.flightCode);
            } else {
                //国际单程
                getRequest.params("depCity", goSearchFightInfo.internationalFlightModel.goTrip.flightSegments.get(0).depCityCode)
                        .params("arrCity", goSearchFightInfo.internationalFlightModel.goTrip.flightSegments.get(goSearchFightInfo.internationalFlightModel.goTrip.flightSegments.size() - 1).arrCityCode)
                        .params("flightCode", goSearchFightInfo.internationalFlightModel.flightCode);
            }
            getRequest
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depDate", depDate)
                    .params("source", "ICP_SELECT_open.3724")
                    //.params("adultNum")
                    //.params("childNum")
                    //.params("cabinLevel")
                    .execute(new JsonCallback<PlaneResponse<PlaneInternationalDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<PlaneInternationalDetailInfo>> response) {
                            super.onSuccess(PlaneDetailActivity.this, response.body().message, response.body().code);
                            internationalDetailInfo = response.body().result;
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
