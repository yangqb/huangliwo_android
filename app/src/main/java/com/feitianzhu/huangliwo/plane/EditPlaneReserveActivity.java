package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BaggageRuleInfo;
import com.feitianzhu.huangliwo.model.CreateOrderInfo;
import com.feitianzhu.huangliwo.model.CustomPlaneFlightInfo;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.model.GoBackTripInfo;
import com.feitianzhu.huangliwo.model.InternationalPriceInfo;
import com.feitianzhu.huangliwo.model.MultiPriceInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.model.PlaneDetailInfo;
import com.feitianzhu.huangliwo.model.RefundChangeInfo;
import com.feitianzhu.huangliwo.model.VenDorsInfo;
import com.feitianzhu.huangliwo.pushshop.ProblemFeedbackActivity;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomLuggageBuyTicketNoticeView;
import com.feitianzhu.huangliwo.view.CustomPlaneInfoView;
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.view.CustomTicketPriceDetailView;
import com.feitianzhu.huangliwo.view.CustomTotalPriceInfoView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPlaneReserveActivity extends BaseActivity {
    private static final int REQUEST_CODE = 100;
    public static final String PLANE_TYPE = "plane_type";
    public static final String PLANE_DETAIL_DATA = "plane_detail_data";
    public static final String PRICE_DATA = "price_data";
    private int type;
    private int count;//成人数量
    private int cCount;//儿童数量
    private RefundChangeInfo refundChangeInfo;
    private SelectPassengerAdapter mAdapter;
    private PlaneDetailInfo detailInfo;
    private GoBackTripInfo internationalDetailInfo;
    private MultiPriceInfo priceInfo;
    private VenDorsInfo venDorsInfo;
    private BaggageRuleInfo baggageRuleInfo;
    private InternationalPriceInfo internationalPriceInfo;
    private List<PassengerModel> list = new ArrayList<>();
    private CustomPlaneFlightInfo customPlaneFlightInfo = new CustomPlaneFlightInfo();
    private CustomPriceDetailInfo priceDetailInfo = new CustomPriceDetailInfo();
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.plane_title)
    LinearLayout planeTitle;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.center_img)
    ImageView centerImg;
    @BindView(R.id.ll_goPlane)
    LinearLayout llGoPlane;
    @BindView(R.id.come_back)
    TextView comeBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_come_info)
    TextView tvComeInfo;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.arfAndTof)
    TextView arfAndTof;
    @BindView(R.id.tvCabinType)
    TextView tvCabinType;
    @BindView(R.id.totalPrice)
    TextView totalPrice;
    @BindView(R.id.contact_name)
    EditText contactName;
    @BindView(R.id.contact_phone)
    EditText contactPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_plane_reserve;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        type = getIntent().getIntExtra(PLANE_TYPE, 1);
        priceInfo = (MultiPriceInfo) getIntent().getSerializableExtra(PRICE_DATA);
        setSpannableString("0", totalPrice);
        if (type == 0) {
            detailInfo = (PlaneDetailInfo) getIntent().getSerializableExtra(PLANE_DETAIL_DATA);
            venDorsInfo = priceInfo.venDorsInfo;
        } else if (type == 1) {
            internationalDetailInfo = (GoBackTripInfo) getIntent().getSerializableExtra(PLANE_DETAIL_DATA);
            internationalPriceInfo = priceInfo.internationalPriceInfo;
        } else {

        }

        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText("北京");
        endCity.setText("上海");
        if (type == 0) {
            priceDetailInfo.price = venDorsInfo.barePrice;
            priceDetailInfo.arf = detailInfo.arf;
            priceDetailInfo.tof = detailInfo.tof;
            priceDetailInfo.cPrice = 0;
            customPlaneFlightInfo.date = DateUtils.strToStr(detailInfo.date) + DateUtils.strToDate2(detailInfo.date);
            customPlaneFlightInfo.depCity = detailInfo.depCode;
            customPlaneFlightInfo.arrCity = detailInfo.arrCode;
            customPlaneFlightInfo.depAirport = detailInfo.depAirport;
            customPlaneFlightInfo.arrAirport = detailInfo.arrAirport;
            customPlaneFlightInfo.depTerminal = detailInfo.depTerminal;
            customPlaneFlightInfo.arrTerminal = detailInfo.arrTerminal;
            customPlaneFlightInfo.bTime = detailInfo.btime;
            customPlaneFlightInfo.eTime = detailInfo.etime;
            customPlaneFlightInfo.flightTime = detailInfo.flightTimes;
            customPlaneFlightInfo.meal = detailInfo.meal;
            customPlaneFlightInfo.code = detailInfo.code;
            customPlaneFlightInfo.com = detailInfo.com;
            centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
            llGoPlane.setVisibility(View.GONE);
            comeBack.setVisibility(View.GONE);
            tvComeInfo.setText(DateUtils.strToStr(detailInfo.date) + DateUtils.strToDate2(detailInfo.date) + detailInfo.btime + detailInfo.depAirport + detailInfo.depTerminal + "-" + detailInfo.arrAirport + detailInfo.arrTerminal);
            price.setText("¥" + MathUtils.subZero(String.valueOf(venDorsInfo.barePrice)));
            arfAndTof.setText("机建+燃油 ¥" + MathUtils.subZero(String.valueOf(detailInfo.tof + detailInfo.arf)));
            if (venDorsInfo.cabinType == 0) {
                tvCabinType.setText("经济舱(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == 1) {
                tvCabinType.setText("头等舱(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == 2) {
                tvCabinType.setText("商务舱(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == 3) {
                tvCabinType.setText("经济舱精选(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == 4) {
                tvCabinType.setText("经济舱y舱(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == 5) {
                tvCabinType.setText("超值头等舱(" + venDorsInfo.cabin + ")");
            } else if (venDorsInfo.cabinType == -1) {
                tvCabinType.setText("未配置");
            }
        } else if (type == 1) {
            priceDetailInfo.price = internationalPriceInfo.price;
            priceDetailInfo.arf = 0;
            priceDetailInfo.tof = 0;
            priceDetailInfo.cPrice = internationalPriceInfo.cPrice;
            customPlaneFlightInfo.date = DateUtils.strToStr(internationalDetailInfo.flightSegments.get(0).depDate) + DateUtils.strToDate2(internationalDetailInfo.flightSegments.get(0).depDate);
            customPlaneFlightInfo.depCity = internationalDetailInfo.flightSegments.get(0).depCityName;
            customPlaneFlightInfo.arrCity = internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrCityName;
            customPlaneFlightInfo.depAirport = internationalDetailInfo.flightSegments.get(0).depAirportName;
            customPlaneFlightInfo.arrAirport = internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrAirportName;
            customPlaneFlightInfo.depTerminal = internationalDetailInfo.flightSegments.get(0).depTerminal;
            customPlaneFlightInfo.arrTerminal = internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrTerminal;
            customPlaneFlightInfo.bTime = internationalDetailInfo.flightSegments.get(0).depTime;
            customPlaneFlightInfo.eTime = internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrTime;
            customPlaneFlightInfo.flightTime = DateUtils.minToHour(internationalDetailInfo.duration);
            customPlaneFlightInfo.meal = false;
            customPlaneFlightInfo.code = internationalDetailInfo.flightSegments.get(0).planeTypeCode;
            customPlaneFlightInfo.com = internationalDetailInfo.flightSegments.get(0).mainCarrierFullName;
            centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
            llGoPlane.setVisibility(View.GONE);
            comeBack.setVisibility(View.GONE);
            tvComeInfo.setText(DateUtils.strToStr(internationalDetailInfo.flightSegments.get(0).depDate) + DateUtils.strToDate2(internationalDetailInfo.flightSegments.get(0).depDate)
                    + internationalDetailInfo.flightSegments.get(0).depTime + internationalDetailInfo.flightSegments.get(0).depAirportName + internationalDetailInfo.flightSegments.get(0).depTerminal + "-"
                    + internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrAirportName + internationalDetailInfo.flightSegments.get(internationalDetailInfo.flightSegments.size() - 1).arrTerminal);
            price.setText("¥" + MathUtils.subZero(String.valueOf(internationalPriceInfo.price)));
            arfAndTof.setText("机建+燃油 ¥0");
            if ("economy".equals(internationalPriceInfo.cabinLevel)) {
                tvCabinType.setText("经济舱(" + internationalPriceInfo.cabin + ")");
            } else if ("first".equals(internationalPriceInfo.cabinLevel)) {
                tvCabinType.setText("头等舱(" + internationalPriceInfo.cabin + ")");
            } else if ("business".equals(internationalPriceInfo.cabinLevel)) {
                tvCabinType.setText("商务舱(" + internationalPriceInfo.cabin + ")");
            } else {
                tvCabinType.setText("未配置");
            }
        } else {
            centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);
            llGoPlane.setVisibility(View.VISIBLE);
            comeBack.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectPassengerAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                list.remove(position);
                mAdapter.notifyDataSetChanged();
                calculationPrice(list);
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void getRefundChange() {
        if (type == 0) {
            OkGo.<PlaneResponse<RefundChangeInfo>>get(Urls.GET_TGQNEWEXPLAIN)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("flightNum", venDorsInfo.shareShowAct ? detailInfo.actCode : detailInfo.code)
                    .params("cabin", venDorsInfo.cabin)
                    .params("dpt", detailInfo.depCode)
                    .params("arr", detailInfo.arrCode)
                    .params("dptDate", detailInfo.date)
                    .params("dptTime", detailInfo.btime)
                    .params("policyId", venDorsInfo.PolicyId)
                    .params("maxSellPrice", String.valueOf(venDorsInfo.barePrice))
                    .params("minSellPrice", String.valueOf(venDorsInfo.barePrice))
                    .params("printPrice", String.valueOf(venDorsInfo.vppr))
                    .params("tagName", venDorsInfo.prtag)
                    .params("translate", false)
                    .params("sfid", venDorsInfo.groupId)
                    .params("needPercentTgqText", false)
                    .params("businessExt", venDorsInfo.businessExt)
                    .params("client", venDorsInfo.domain)
                    //.params("childCabin")
                    //.params("childSellPrice")
                    .execute(new JsonCallback<PlaneResponse<RefundChangeInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<RefundChangeInfo>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            refundChangeInfo = response.body().result;
                            new XPopup.Builder(EditPlaneReserveActivity.this)
                                    .enableDrag(false)
                                    .asCustom(new CustomCancelChangePopView(EditPlaneReserveActivity.this
                                    ).setType(type).setData(refundChangeInfo).setLuggage(false)).show();
                        }

                        @Override
                        public void onError(Response<PlaneResponse<RefundChangeInfo>> response) {
                            super.onError(response);
                        }
                    });

        }
    }

    public void getBaggagerule() {
        OkGo.<PlaneResponse<BaggageRuleInfo>>get(Urls.GET_BAGGAGERULES)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("airlineCode", detailInfo.carrier)
                .params("cabin", venDorsInfo.cabin)
                .params("depCode", detailInfo.depCode)
                .params("arrCode", detailInfo.arrCode)
                .params("luggage", venDorsInfo.luggage)
                .params("saleDate", detailInfo.date)
                .params("depDate", detailInfo.date)
                .execute(new JsonCallback<PlaneResponse<BaggageRuleInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<BaggageRuleInfo>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        baggageRuleInfo = response.body().result;
                        new XPopup.Builder(EditPlaneReserveActivity.this)
                                .enableDrag(false)
                                .asCustom(new CustomLuggageBuyTicketNoticeView(EditPlaneReserveActivity.this
                                ).setType(type).setData(baggageRuleInfo)).show();
                    }

                    @Override
                    public void onError(Response<PlaneResponse<BaggageRuleInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    @OnClick({R.id.left_button, R.id.cancel_change, R.id.rl_plane_info, R.id.luggage_buyTicket_notice, R.id.ticketPrice_detail, R.id.selectUser, R.id.tvReserveNotice, R.id.btn_submit, R.id.priceInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.cancel_change:
                getRefundChange();
                break;
            case R.id.rl_plane_info:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPlaneInfoView(this
                        ).setType(type).setData(customPlaneFlightInfo)).show();
                break;
            case R.id.luggage_buyTicket_notice:
                getBaggagerule();
                break;
            case R.id.ticketPrice_detail:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomTicketPriceDetailView(this
                        ).setType(type).setData(priceDetailInfo)).show();
                break;
            case R.id.selectUser:
                Intent intent = new Intent(EditPlaneReserveActivity.this, PassengerListActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tvReserveNotice:
                Integer[] integers = new Integer[]{R.mipmap.k04_04yuding1, R.mipmap.k04_04yuding2, R.mipmap.k04_04yuding3};
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPlaneProtocolView(this
                        ).setTitle("机票预订须知").setData(Arrays.asList(integers))).show();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.priceInfo:
                if (list.size() == 0) {
                    ToastUtils.showShortToast("请选择乘机人");
                } else {
                    new XPopup.Builder(this)
                            .enableDrag(false)
                            .asCustom(new CustomTotalPriceInfoView(EditPlaneReserveActivity.this).setData(priceDetailInfo).setType(type)).show();
                }
                break;
        }
    }

    public void submit() {
        //BookingInfo
        String json = new Gson().toJson(venDorsInfo);
        OkGo.<PlaneResponse>post(Urls.PLANE_BOOK)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("vendorStr", json)
                .params("depCode", detailInfo.depCode)
                .params("arrCode", detailInfo.arrCode)
                .params("code", detailInfo.code)
                .params("date", detailInfo.date)
                .params("carrier", detailInfo.carrier)
                .params("btime", detailInfo.btime)
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        String json = new Gson().toJson(response.body().result);
                        createOrder(json);
                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void createOrder(String bkResult) {
        String passengerJson = new Gson().toJson(list);
        OkGo.<PlaneResponse<CreateOrderInfo>>post(Urls.CREATE_PLANE_ORDER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("contact", contactName.getText().toString().trim())
                .params("contactMob", contactPhone.getText().toString().trim())
                .params("cardNo", list.get(0).cardNo)
                .params("bookingResult", bkResult)
                .params("needXcd", false)
                //.params("address", "深圳市固戍梧桐岛6A栋")
                .params("passengerStr", passengerJson)
                .execute(new JsonCallback<PlaneResponse<CreateOrderInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<CreateOrderInfo>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0) {
                            Intent intent = new Intent(EditPlaneReserveActivity.this, PlaneOrderDetailActivity.class);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<CreateOrderInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                ArrayList<PassengerModel> selectPassenger = data.getParcelableArrayListExtra(PassengerListActivity.SELECT_PASSENGER);
                list.addAll(selectPassenger);
                removeDuplicate(list);
                mAdapter.notifyDataSetChanged();
                calculationPrice(list);
            }
        }
    }

    public void calculationPrice(List<PassengerModel> list) {
        cCount = 0;
        count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).ageType == 0) {
                count++;
            } else {
                cCount++;
            }
        }
        priceDetailInfo.num = count;
        priceDetailInfo.cnum = cCount;
        String totalAmount = MathUtils.subZero(String.valueOf((priceDetailInfo.price * priceDetailInfo.num) + (priceDetailInfo.cPrice * priceDetailInfo.cnum) + (priceDetailInfo.tof + priceDetailInfo.arf) * (count + cCount)));
        setSpannableString(totalAmount, totalPrice);

    }

    public List<PassengerModel> removeDuplicate(List<PassengerModel> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span3.setSpan(new AbsoluteSizeSpan(21, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
