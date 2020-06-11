package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cretin.tools.cityselect.model.CityModel;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.AddressManagementActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.model.BaggageRuleInfo;
import com.feitianzhu.huangliwo.model.BaggageRuleReqParamModel;
import com.feitianzhu.huangliwo.model.ContactModel;
import com.feitianzhu.huangliwo.model.CreateOrderInfo;
import com.feitianzhu.huangliwo.model.CustomPlaneDetailInfo;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.model.DocBookingPriceTagInfo;
import com.feitianzhu.huangliwo.model.DocGoBackCreateOrderInfo;
import com.feitianzhu.huangliwo.model.DocPassengerInfo;
import com.feitianzhu.huangliwo.model.DocResultBookingInfo;
import com.feitianzhu.huangliwo.model.GoBackBaggageRuleInfo;
import com.feitianzhu.huangliwo.model.GoBackBookingInfo;
import com.feitianzhu.huangliwo.model.GoBackFlight;
import com.feitianzhu.huangliwo.model.GoBackTripInfo;
import com.feitianzhu.huangliwo.model.InterContactModel;
import com.feitianzhu.huangliwo.model.InterPassengerInfo;
import com.feitianzhu.huangliwo.model.InterXcdInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.RefundChangeInfo;
import com.feitianzhu.huangliwo.model.ReimbursementModel;
import com.feitianzhu.huangliwo.model.UserClientInfo;
import com.feitianzhu.huangliwo.settings.ChangeLoginPassword;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomLuggageBuyTicketNoticeView;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomPayView;
import com.feitianzhu.huangliwo.view.CustomPlaneInfoView;
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolDialog;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.view.CustomTicketPriceDetailView;
import com.feitianzhu.huangliwo.view.CustomTotalPriceInfoView;
import com.feitianzhu.huangliwo.view.SwitchButton;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPlaneReserveActivity extends BaseActivity {
    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_ADDRESS_CODE = 101;
    private static final int REQUEST_PHONE_CODE = 102;
    public static final String PLANE_TYPE = "plane_type";
    public static final String PLANE_DETAIL_DATA = "plane_detail_data";
    private CityModel cityModel;
    private int type;
    private int count;//成人数量
    private int cCount;//儿童数量
    private RefundChangeInfo refundChangeInfo;
    private List<RefundChangeInfo> refundChangeInfos;
    private SelectPassengerAdapter mAdapter;
    private BaggageRuleInfo baggageRuleInfo;
    private List<GoBackBaggageRuleInfo> baggageRuleInfos;
    private List<PassengerModel> passengerList = new ArrayList<>();
    private CustomPriceDetailInfo priceDetailInfo = new CustomPriceDetailInfo();
    private CustomPlaneDetailInfo customPlaneDetailInfo;
    private String userId;
    private String token;
    private int invoicePosition;
    private AddressInfo.ShopAddressListBean addressBean;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
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
    @BindView(R.id.ll_backPlane)
    LinearLayout llBackPlane;
    @BindView(R.id.goTitle)
    TextView goTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_go_info)
    TextView tvGoInfo;
    @BindView(R.id.tv_back_info)
    TextView tvBackInfo;
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
    @BindView(R.id.ll_child_price)
    LinearLayout llChildPrice;
    @BindView(R.id.cprice)
    TextView cprice;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.ll_reimbursement)
    LinearLayout llReimbursement;
    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.editInvoiceTitle)
    EditText editInvoiceTitle;
    @BindView(R.id.edit_num)
    EditText editNum;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView tvAddress;
    @BindView(R.id.ll_InvoiceTitle)
    LinearLayout llInvoiceTitle;
    @BindView(R.id.ll_identification_num)
    LinearLayout llIdentificationNum;
    @BindView(R.id.line1)
    View invoiceTitleLine1;
    @BindView(R.id.line2)
    View identificationNumLine2;
    @BindView(R.id.root_view)
    LinearLayout rootView;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.postagePrice)
    TextView postagePrice;
    @BindView(R.id.phoneAreaCode)
    TextView phoneAreaCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_plane_reserve;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        type = getIntent().getIntExtra(PLANE_TYPE, 1);
        setSpannableString("0", totalPrice);
        customPlaneDetailInfo = (CustomPlaneDetailInfo) getIntent().getSerializableExtra(PLANE_DETAIL_DATA);

        switchButton.setBackgroundColorChecked(getResources().getColor(R.color.bg_yellow));
        switchButton.setBackgroundColorUnchecked(getResources().getColor(R.color.color_F1EFEF));
        switchButton.setAnimateDuration(300);
        switchButton.setButtonColor(getResources().getColor(R.color.white));

        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText(customPlaneDetailInfo.customFightCityInfo.depCityName);
        endCity.setText(customPlaneDetailInfo.customFightCityInfo.arrCityName);
        if (type == 0) {
            postagePrice.setText("¥20");
            priceDetailInfo.price = customPlaneDetailInfo.customDocGoPriceInfo.barePrice;
            priceDetailInfo.arf = customPlaneDetailInfo.customDocGoFlightInfo.arf;
            priceDetailInfo.tof = customPlaneDetailInfo.customDocGoFlightInfo.tof;
            if (customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap != null) {
                if (customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChild) {
                    priceDetailInfo.cPrice = customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.childPrice;
                } else {
                    if (customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChildBuyAdult) {
                        priceDetailInfo.cPrice = customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.childByAdultPrice;
                    } else {
                        priceDetailInfo.cPrice = 0;
                    }
                }
            } else {
                priceDetailInfo.cPrice = 0;
            }
            centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
            llGoPlane.setVisibility(View.VISIBLE);
            llBackPlane.setVisibility(View.GONE);
            goTitle.setVisibility(View.GONE);
            tvGoInfo.setText(DateUtils.strToStr(customPlaneDetailInfo.customDocGoFlightInfo.date) + DateUtils.strToDate2(customPlaneDetailInfo.customDocGoFlightInfo.date) + customPlaneDetailInfo.customDocGoFlightInfo.btime + customPlaneDetailInfo.customDocGoFlightInfo.depAirport + customPlaneDetailInfo.customDocGoFlightInfo.depTerminal + "-" + customPlaneDetailInfo.customDocGoFlightInfo.arrAirport + customPlaneDetailInfo.customDocGoFlightInfo.arrTerminal);
            price.setText("¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customDocGoPriceInfo.barePrice)));
            arfAndTof.setText("机建+燃油 ¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customDocGoFlightInfo.tof + customPlaneDetailInfo.customDocGoFlightInfo.arf)));
            if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 0) {
                tvCabinType.setText("经济舱(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 1) {
                tvCabinType.setText("头等舱(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 2) {
                tvCabinType.setText("商务舱(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 3) {
                tvCabinType.setText("经济舱精选(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 4) {
                tvCabinType.setText("经济舱y舱(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == 5) {
                tvCabinType.setText("超值头等舱(" + customPlaneDetailInfo.customDocGoPriceInfo.cabin + ")");
            } else if (customPlaneDetailInfo.customDocGoPriceInfo.cabinType == -1) {
                tvCabinType.setText("未配置");
            }
        } else if (type == 1) {
            priceDetailInfo.price = customPlaneDetailInfo.customInterPriceInfo.price;
            priceDetailInfo.arf = 0;
            priceDetailInfo.tof = 0;
            priceDetailInfo.cPrice = customPlaneDetailInfo.customInterPriceInfo.cPrice;
            GoBackTripInfo interGo = customPlaneDetailInfo.customInterFlightInfo.goTrip;
            centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
            llGoPlane.setVisibility(View.VISIBLE);
            llBackPlane.setVisibility(View.GONE);
            goTitle.setVisibility(View.GONE);
            tvGoInfo.setText(DateUtils.strToStr(interGo.flightSegments.get(0).depDate) + DateUtils.strToDate2(interGo.flightSegments.get(0).depDate)
                    + interGo.flightSegments.get(0).depTime + interGo.flightSegments.get(0).depAirportName + interGo.flightSegments.get(0).depTerminal + "-"
                    + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrAirportName + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTerminal);
            price.setText("¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customInterPriceInfo.price)));
            arfAndTof.setText("机建+燃油 ¥0");
            if ("economy".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel)) {
                tvCabinType.setText("经济舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin + ")");
            } else if ("first".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel)) {
                tvCabinType.setText("头等舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin + ")");
            } else if ("business".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel)) {
                tvCabinType.setText("商务舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin + ")");
            } else {
                tvCabinType.setText("未配置");
            }
        } else {
            centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);
            llGoPlane.setVisibility(View.VISIBLE);
            llBackPlane.setVisibility(View.VISIBLE);
            goTitle.setVisibility(View.VISIBLE);
            if (type == 2) {
                postagePrice.setText("¥0");
                priceDetailInfo.price = customPlaneDetailInfo.customDocGoBackPriceInfo.barePrice;
                priceDetailInfo.arf = customPlaneDetailInfo.customDocGoBackFlightInfo.arf;
                priceDetailInfo.tof = customPlaneDetailInfo.customDocGoBackFlightInfo.tof;
                priceDetailInfo.cPrice = 0;
                GoBackFlight docGoFlight = customPlaneDetailInfo.customDocGoBackFlightInfo.go;
                GoBackFlight docBackFlight = customPlaneDetailInfo.customDocGoBackFlightInfo.back;
                tvGoInfo.setText(DateUtils.strToStr(customPlaneDetailInfo.customFightCityInfo.goDate) + DateUtils.strToDate2(customPlaneDetailInfo.customFightCityInfo.goDate) + docGoFlight.depTime + docGoFlight.depAirport + docGoFlight.depTerminal + "-" + docGoFlight.arrAirport + docGoFlight.arrTerminal);
                tvBackInfo.setText(DateUtils.strToStr(customPlaneDetailInfo.customFightCityInfo.backDate) + DateUtils.strToDate2(customPlaneDetailInfo.customFightCityInfo.backDate) + docBackFlight.depTime + docBackFlight.depAirport + docBackFlight.depTerminal + "-" + docBackFlight.arrAirport + docBackFlight.arrTerminal);
                price.setText("¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customDocGoBackPriceInfo.barePrice)));
                arfAndTof.setText("机建+燃油 ¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customDocGoBackFlightInfo.tof + customPlaneDetailInfo.customDocGoBackFlightInfo.arf)));
                tvCabinType.setText(customPlaneDetailInfo.customDocGoBackPriceInfo.cabinDesc);
            } else {
                priceDetailInfo.price = customPlaneDetailInfo.customInterPriceInfo.price;
                priceDetailInfo.arf = 0;
                priceDetailInfo.tof = 0;
                priceDetailInfo.cPrice = customPlaneDetailInfo.customInterPriceInfo.cPrice;
                GoBackTripInfo interGo = customPlaneDetailInfo.customInterFlightInfo.goTrip;
                GoBackTripInfo interBack = customPlaneDetailInfo.customInterFlightInfo.backTrip;
                tvGoInfo.setText(DateUtils.strToStr(interGo.flightSegments.get(0).depDate) + DateUtils.strToDate2(interGo.flightSegments.get(0).depDate)
                        + interGo.flightSegments.get(0).depTime + interGo.flightSegments.get(0).depAirportName + interGo.flightSegments.get(0).depTerminal + "-"
                        + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrAirportName + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTerminal);
                tvBackInfo.setText(DateUtils.strToStr(interBack.flightSegments.get(0).depDate) + DateUtils.strToDate2(interBack.flightSegments.get(0).depDate)
                        + interBack.flightSegments.get(0).depTime + interBack.flightSegments.get(0).depAirportName + interBack.flightSegments.get(0).depTerminal + "-"
                        + interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrAirportName + interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrTerminal);
                price.setText("¥" + MathUtils.subZero(String.valueOf(customPlaneDetailInfo.customInterPriceInfo.price)));
                arfAndTof.setText("机建+燃油 ¥0");


                if ("economy".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel.split("/")[0])) {
                    tvCabinType.setText("经济舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin.split("/")[0] + ")");
                } else if ("first".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel.split("/")[0])) {
                    tvCabinType.setText("头等舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin.split("/")[0] + ")");
                } else if ("business".equals(customPlaneDetailInfo.customInterPriceInfo.cabinLevel.split("/")[0])) {
                    tvCabinType.setText("商务舱(" + customPlaneDetailInfo.customInterPriceInfo.cabin.split("/")[0] + ")");
                } else {
                    tvCabinType.setText("未配置");
                }
            }
        }

        if (priceDetailInfo.cPrice == 0) {
            llChildPrice.setVisibility(View.GONE);
            cprice.setText("¥0");
        } else {
            llChildPrice.setVisibility(View.VISIBLE);
            cprice.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.cPrice)));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectPassengerAdapter(passengerList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {

        SoftKeyBoardListener.setListener(EditPlaneReserveActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(AppActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                rlBottom.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                //Toast.makeText(AppActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                rlBottom.setVisibility(View.VISIBLE);
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llReimbursement.setVisibility(View.VISIBLE);
                    if (type == 2) {
                        priceDetailInfo.postage = 0;
                    } else {
                        priceDetailInfo.postage = 20;
                    }
                } else {
                    llReimbursement.setVisibility(View.GONE);
                    priceDetailInfo.postage = 0;
                }
                if (passengerList != null && passengerList.size() > 0) {
                    calculationPrice(passengerList);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                passengerList.remove(position);
                mAdapter.notifyDataSetChanged();
                calculationPrice(passengerList);
            }
        });
    }

    @Override
    protected void initData() {
        /*
         * 获取默认收货地址
         * */
        OkGo.<LzyResponse<AddressInfo>>post(Urls.GET_ADDRESS)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<AddressInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            AddressInfo addressInfo = response.body().data;
                            addressInfos = addressInfo.getShopAddressList();
                            if (addressInfos.size() > 0) {
                                for (AddressInfo.ShopAddressListBean address : addressInfos
                                ) {
                                    if (address.getIsDefalt() == 1) {
                                        addressBean = address;
                                        name.setText(addressBean.getUserName());
                                        tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                                        phone.setText(addressBean.getPhone());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    public void getRefundChange() {
        if (type == 0) {
            OkGo.<PlaneResponse<RefundChangeInfo>>get(Urls.GET_TGQNEWEXPLAIN)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("flightNum", customPlaneDetailInfo.customDocGoPriceInfo.shareShowAct ? customPlaneDetailInfo.customDocGoFlightInfo.actCode : customPlaneDetailInfo.customDocGoFlightInfo.code)
                    .params("cabin", customPlaneDetailInfo.customDocGoPriceInfo.cabin)
                    .params("dpt", customPlaneDetailInfo.customDocGoFlightInfo.depCode)
                    .params("arr", customPlaneDetailInfo.customDocGoFlightInfo.arrCode)
                    .params("dptDate", customPlaneDetailInfo.customDocGoFlightInfo.date)
                    .params("dptTime", customPlaneDetailInfo.customDocGoFlightInfo.btime)
                    .params("policyId", customPlaneDetailInfo.customDocGoPriceInfo.PolicyId)
                    .params("maxSellPrice", String.valueOf(customPlaneDetailInfo.customDocGoPriceInfo.barePrice))
                    .params("minSellPrice", String.valueOf(customPlaneDetailInfo.customDocGoPriceInfo.barePrice))
                    .params("printPrice", String.valueOf(customPlaneDetailInfo.customDocGoPriceInfo.vppr))
                    .params("tagName", customPlaneDetailInfo.customDocGoPriceInfo.prtag)
                    .params("translate", false)
                    .params("sfid", customPlaneDetailInfo.customDocGoPriceInfo.groupId)
                    .params("needPercentTgqText", false)
                    .params("businessExt", customPlaneDetailInfo.customDocGoPriceInfo.businessExt)
                    .params("client", customPlaneDetailInfo.customDocGoPriceInfo.domain)
                    //.params("childCabin")
                    //.params("childSellPrice")
                    .execute(new JsonCallback<PlaneResponse<RefundChangeInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<RefundChangeInfo>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            refundChangeInfo = response.body().result;
                            if (response.body().code == 0 && response.body().result != null) {
                                new XPopup.Builder(EditPlaneReserveActivity.this)
                                        .asCustom(new CustomCancelChangePopView(EditPlaneReserveActivity.this
                                        ).setType(type).setGoData(refundChangeInfo).setLuggage(false)).show();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<RefundChangeInfo>> response) {
                            super.onError(response);
                        }
                    });

        } else if (type == 2) {
            OkGo.<PlaneResponse<List<RefundChangeInfo>>>post(Urls.GET_GO_BACK_TGQNEWBACK)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("client", customPlaneDetailInfo.customDocGoBackPriceInfo.domain)
                    .params("carrier", customPlaneDetailInfo.customDocGoBackFlightInfo.go.carrier)
                    .params("depCode", customPlaneDetailInfo.customDocGoBackFlightInfo.go.depAirportCode)
                    .params("arrCode", customPlaneDetailInfo.customDocGoBackFlightInfo.go.arrAirportCode)
                    .params("goDate", customPlaneDetailInfo.customFightCityInfo.goDate)
                    .params("backDate", customPlaneDetailInfo.customFightCityInfo.backDate)
                    .params("outCabin", customPlaneDetailInfo.customDocGoBackPriceInfo.outCabin)
                    .params("retCabin", customPlaneDetailInfo.customDocGoBackPriceInfo.retCabin)
                    .params("businessExts", customPlaneDetailInfo.customDocGoBackPriceInfo.businessExts)
                    .params("goFlightNum", customPlaneDetailInfo.customDocGoBackFlightInfo.go.flightCode)
                    .params("backFlightNum", customPlaneDetailInfo.customDocGoBackFlightInfo.back.flightCode)
                    .params("policyId", customPlaneDetailInfo.customDocGoBackPriceInfo.policyId)
                    .params("price", customPlaneDetailInfo.customDocGoBackPriceInfo.price)
                    .params("barePrice", customPlaneDetailInfo.customDocGoBackPriceInfo.barePrice)
                    .params("tagName", customPlaneDetailInfo.customDocGoBackPriceInfo.tagName)
                    .execute(new JsonCallback<PlaneResponse<List<RefundChangeInfo>>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<List<RefundChangeInfo>>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                refundChangeInfos = response.body().result;
                                new XPopup.Builder(EditPlaneReserveActivity.this)
                                        .asCustom(new CustomCancelChangePopView(EditPlaneReserveActivity.this
                                        ).setType(type).setGoData(refundChangeInfos.get(0)).setBackData(refundChangeInfos.get(1)).setLuggage(false)).show();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<List<RefundChangeInfo>>> response) {
                            super.onError(response);
                        }
                    });
        }
    }

    public void getBaggagerule() {
        if (type == 0) {
            OkGo.<PlaneResponse<BaggageRuleInfo>>get(Urls.GET_BAGGAGERULES)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("airlineCode", customPlaneDetailInfo.customDocGoFlightInfo.carrier)
                    .params("cabin", customPlaneDetailInfo.customDocGoPriceInfo.cabin)
                    .params("depCode", customPlaneDetailInfo.customDocGoFlightInfo.depCode)
                    .params("arrCode", customPlaneDetailInfo.customDocGoFlightInfo.arrCode)
                    .params("luggage", customPlaneDetailInfo.customDocGoPriceInfo.luggage)
                    .params("saleDate", customPlaneDetailInfo.customDocGoFlightInfo.date)
                    .params("depDate", customPlaneDetailInfo.customDocGoFlightInfo.date)
                    .execute(new JsonCallback<PlaneResponse<BaggageRuleInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<BaggageRuleInfo>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                baggageRuleInfo = response.body().result;
                                new XPopup.Builder(EditPlaneReserveActivity.this)
                                        .asCustom(new CustomLuggageBuyTicketNoticeView(EditPlaneReserveActivity.this
                                        ).setType(type).setGoData(baggageRuleInfo)).show();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<BaggageRuleInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else if (type == 2) {
            BaggageRuleReqParamModel goBaggageRuleReqParamModel = new BaggageRuleReqParamModel();
            goBaggageRuleReqParamModel.airlineCode = customPlaneDetailInfo.customDocGoBackFlightInfo.go.codeShare ? customPlaneDetailInfo.customDocGoBackFlightInfo.go.mainCarrier : customPlaneDetailInfo.customDocGoBackFlightInfo.go.carrier;
            goBaggageRuleReqParamModel.arrCode = customPlaneDetailInfo.customDocGoBackFlightInfo.go.depAirportCode;
            goBaggageRuleReqParamModel.cabin = customPlaneDetailInfo.customDocGoBackPriceInfo.cabin;
            goBaggageRuleReqParamModel.depCode = customPlaneDetailInfo.customDocGoBackFlightInfo.go.arrAirportCode;
            goBaggageRuleReqParamModel.depDate = customPlaneDetailInfo.customFightCityInfo.goDate;
            goBaggageRuleReqParamModel.saleDate = customPlaneDetailInfo.customFightCityInfo.goDate;

            BaggageRuleReqParamModel backBaggageRuleReqParamModel = new BaggageRuleReqParamModel();
            backBaggageRuleReqParamModel.airlineCode = customPlaneDetailInfo.customDocGoBackFlightInfo.back.codeShare ? customPlaneDetailInfo.customDocGoBackFlightInfo.back.mainCarrier : customPlaneDetailInfo.customDocGoBackFlightInfo.back.carrier;
            backBaggageRuleReqParamModel.arrCode = customPlaneDetailInfo.customDocGoBackFlightInfo.back.depAirportCode;
            backBaggageRuleReqParamModel.cabin = customPlaneDetailInfo.customDocGoBackPriceInfo.cabin;
            backBaggageRuleReqParamModel.depCode = customPlaneDetailInfo.customDocGoBackFlightInfo.back.arrAirportCode;
            backBaggageRuleReqParamModel.depDate = customPlaneDetailInfo.customFightCityInfo.backDate;
            backBaggageRuleReqParamModel.saleDate = customPlaneDetailInfo.customFightCityInfo.backDate;

            List<BaggageRuleReqParamModel> baggageRuleReqParamModelList = new ArrayList<>();
            baggageRuleReqParamModelList.add(goBaggageRuleReqParamModel);
            baggageRuleReqParamModelList.add(backBaggageRuleReqParamModel);

            String baggageRuleReqJson = new Gson().toJson(baggageRuleReqParamModelList);

            OkGo.<PlaneResponse<List<GoBackBaggageRuleInfo>>>post(Urls.GET_GO_BACK_BAGGAGERULES)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("listStr", baggageRuleReqJson)
                    .execute(new JsonCallback<PlaneResponse<List<GoBackBaggageRuleInfo>>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<List<GoBackBaggageRuleInfo>>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                baggageRuleInfos = response.body().result;
                                new XPopup.Builder(EditPlaneReserveActivity.this)
                                        .asCustom(new CustomLuggageBuyTicketNoticeView(EditPlaneReserveActivity.this
                                        ).setType(type).setGoData(baggageRuleInfos.get(0).baggageRuleInfo).setBackData(baggageRuleInfos.get(1).baggageRuleInfo)).show();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<List<GoBackBaggageRuleInfo>>> response) {
                            super.onError(response);
                        }
                    });

        }
    }

    @OnClick({R.id.left_button, R.id.cancel_change, R.id.rl_plane_info, R.id.luggage_buyTicket_notice, R.id.ticketPrice_detail,
            R.id.selectUser, R.id.tvReserveNotice, R.id.btn_submit, R.id.priceInfo, R.id.invoiceType, R.id.rl_address, R.id.identification_num_explain, R.id.phoneAreaCode})
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
                        ).setType(type).setData(customPlaneDetailInfo)).show();
                break;
            case R.id.luggage_buyTicket_notice:
                getBaggagerule();
                break;
            case R.id.ticketPrice_detail:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .asCustom(new CustomTicketPriceDetailView(this
                        ).setType(type).setData(priceDetailInfo)).show();
                break;
            case R.id.selectUser:
                Intent intent = new Intent(EditPlaneReserveActivity.this, PassengerListActivity.class);
                intent.putExtra(PassengerListActivity.PRICE_DATA, customPlaneDetailInfo);
                intent.putExtra(PassengerListActivity.PLANE_TYPE, type);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tvReserveNotice:
                new CustomPlaneProtocolDialog(EditPlaneReserveActivity.this)
                        .setTitle("机票预订须知")
                        .setData(Urls.BASE_URL + "fhwl/static/html/jipiaoyuding.html")
                        .show();
                break;
            case R.id.btn_submit:
                if (passengerList.size() == 0) {
                    ToastUtils.show("请选择乘机人");
                    return;
                }
                if (TextUtils.isEmpty(contactName.getText().toString().trim())) {
                    ToastUtils.show("请填写联系人姓名");
                    return;
                }
                if (TextUtils.isEmpty(contactPhone.getText().toString().trim())) {
                    ToastUtils.show("请填写联系人电话");
                    return;
                } else {
                    if (!StringUtils.isPhone(contactPhone.getText().toString().trim())) {
                        ToastUtils.show("请填写正确的联系人电话");
                        return;
                    }
                }

                if (priceDetailInfo.num == 0 && priceDetailInfo.cnum > 0) {
                    String content = "儿童乘机须由18岁以上成人陪同，\n请添加成人";
                    new XPopup.Builder(this)
                            .asConfirm("提示", content, "", "确定", null, null, true)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                    return;
                }

                if (priceDetailInfo.num > 0 && priceDetailInfo.cnum > priceDetailInfo.num * 2) {
                    ToastUtils.show("一名成人最多携带2名儿童");
                    return;
                }

                if (switchButton.isChecked()) {
                    if (invoicePosition == 0) {
                        ToastUtils.show("请选择发票类型");
                        return;
                    }
                    if ((invoicePosition == 1 || invoicePosition == 3 || invoicePosition == 4) && TextUtils.isEmpty(editInvoiceTitle.getText().toString().trim())) {
                        ToastUtils.show("请填写发票抬头");
                        return;
                    }
                    if (invoicePosition == 1 || invoicePosition == 3 && TextUtils.isEmpty(editNum.getText().toString().trim())) {
                        ToastUtils.show("请填写纳税人识别号");
                        return;
                    }
                    if (addressBean == null) {
                        ToastUtils.show("请选择收货地址");
                        return;
                    }
                    if (type == 0 || type == 2) {
                        docSubmit();
                    } else {
                        interSubmit();
                    }
                } else {
                    new XPopup.Builder(EditPlaneReserveActivity.this)
                            .asConfirm("", "确认不需要报销凭证提交订单吗?", "取消", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    if (type == 0 || type == 2) {
                                        docSubmit();
                                    } else {
                                        interSubmit();
                                    }
                                }
                            }, new OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            }, false)
                            .bindLayout(R.layout.layout_dialog_login).show();
                }
                break;
            case R.id.priceInfo:
                if (passengerList.size() == 0) {
                    ToastUtils.show("请选择乘机人");
                } else {
                    new XPopup.Builder(this)
                            .enableDrag(false)
                            .asCustom(new CustomTotalPriceInfoView(EditPlaneReserveActivity.this).setData(priceDetailInfo)).show();
                }
                break;
            case R.id.invoiceType:
                String[] strings1 = new String[]{"单位", "个人", "企业", "政府机关行政单位"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPlaneReserveActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvInvoice.setText(strings1[position]);
                                        if (position == 2 || position == 0) {
                                            invoicePosition = 3;
                                        } else {
                                            invoicePosition = position + 1;
                                        }
                                        if (invoicePosition == 1 || invoicePosition == 3 || invoicePosition == 4) {
                                            llInvoiceTitle.setVisibility(View.VISIBLE);
                                            invoiceTitleLine1.setVisibility(View.VISIBLE);
                                        } else {
                                            llInvoiceTitle.setVisibility(View.GONE);
                                            invoiceTitleLine1.setVisibility(View.GONE);
                                        }
                                        if (invoicePosition == 1 || invoicePosition == 3) {
                                            llIdentificationNum.setVisibility(View.VISIBLE);
                                            identificationNumLine2.setVisibility(View.VISIBLE);
                                        } else {
                                            llIdentificationNum.setVisibility(View.GONE);
                                            identificationNumLine2.setVisibility(View.GONE);
                                        }
                                    }
                                }))
                        .show();
                break;
            case R.id.rl_address:
                intent = new Intent(EditPlaneReserveActivity.this, AddressManagementActivity.class);
                intent.putExtra(AddressManagementActivity.IS_SELECT, true);
                startActivityForResult(intent, REQUEST_ADDRESS_CODE);
                break;
            case R.id.identification_num_explain:
                String content = "纳税人识别号是企业税务登记证上唯一识别企业的税号，由15/18或20位数码组成。根据国家税务局规定，自2017年7月1日起，开具增值税普通发票必须有纳税人识别号或统一社会信用代码，否则该发票为不符合规定的发票，不得作为税收凭证。纳税人识别号可登录纳税人信息查询网www.yibannashuiren.com 查询，或向公司财务人员咨询。";

                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;
            case R.id.phoneAreaCode:
                /*intent = new Intent(EditPlaneReserveActivity.this, AreaSelectActivity.class);
                startActivity(intent);*/
                intent = new Intent(EditPlaneReserveActivity.this, SelectPhoneCodeActivity.class);
                startActivityForResult(intent, REQUEST_PHONE_CODE);
                break;
        }
    }

    public void docSubmit() {
        if (type == 0) {
            String json = new Gson().toJson(customPlaneDetailInfo.customDocGoPriceInfo);
            String depCode = customPlaneDetailInfo.customDocGoFlightInfo.depCode;
            String arrCode = customPlaneDetailInfo.customDocGoFlightInfo.arrCode;
            String code = customPlaneDetailInfo.customDocGoFlightInfo.code;
            String date = customPlaneDetailInfo.customDocGoFlightInfo.date;
            String carrier = customPlaneDetailInfo.customDocGoFlightInfo.carrier;
            String btime = customPlaneDetailInfo.customDocGoFlightInfo.btime;
            OkGo.<PlaneResponse>post(Urls.PLANE_BOOK)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("vendorStr", json)
                    .params("depCode", depCode)
                    .params("arrCode", arrCode)
                    .params("code", code)
                    .params("date", date)
                    .params("carrier", carrier)
                    .params("btime", btime)
                    .execute(new JsonCallback<PlaneResponse>() {
                        @Override
                        public void onStart(Request<PlaneResponse, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(Response<PlaneResponse> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            //BookingInfo
                            if (response.body().code == 0 && response.body().result != null) {
                                String json = new Gson().toJson(response.body().result);
                                createOrder(json);
                            } else {
                                goneloadDialog();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse> response) {
                            super.onError(response);
                            goneloadDialog();
                        }
                    });
        } else if (type == 2) {
            OkGo.<PlaneResponse<DocResultBookingInfo>>get(Urls.PLANE_GO_BACK_BOOK)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("bookingParamKey", customPlaneDetailInfo.customDocGoBackPriceInfo.bookingParamKey)
                    .execute(new JsonCallback<PlaneResponse<DocResultBookingInfo>>() {
                        @Override
                        public void onStart(Request<PlaneResponse<DocResultBookingInfo>, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(Response<PlaneResponse<DocResultBookingInfo>> response) {
                            super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                createOrder3(response.body().result);
                            } else {
                                goneloadDialog();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<DocResultBookingInfo>> response) {
                            super.onError(response);
                            goneloadDialog();
                        }
                    });
        }

    }

    public void interSubmit() {
        OkGo.<PlaneResponse>get(Urls.INTER_PLANE_BOOK)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("source", "ICP_SELECT_open.3724")
                .params("priceKey", customPlaneDetailInfo.customInterPriceInfo.priceKey)
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onStart(Request<PlaneResponse, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0 && response.body().result != null) {
                            String json = new Gson().toJson(response.body().result);
                            createOrder2(json);
                        } else {
                            goneloadDialog();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    /*
     * 国内往返生单
     * */
    List<DocBookingPriceTagInfo> adultTagInfo;
    List<DocBookingPriceTagInfo> childrenTagInfo;
    List<DocBookingPriceTagInfo> babyTagInfo;

    public void createOrder3(DocResultBookingInfo docResultBookingInfo) {
        GoBackBookingInfo goBackBookingInfo = new GoBackBookingInfo();
        goBackBookingInfo.flightType = docResultBookingInfo.type;
        goBackBookingInfo.bookingTag = docResultBookingInfo.bookingTag;
        goBackBookingInfo.soloChild = docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).policyInfo.soloChild ? 1 : 0;
        goBackBookingInfo.orderFrom = "DOMESTIC_ROUND_WAY_PACKAGE";

        String bookJson = new Gson().toJson(goBackBookingInfo);

        List<DocPassengerInfo> docPassengerInfoList = new ArrayList<>();
        // ②成人取ADU节点，儿童取CHI，婴儿取INF；key为：ADU，CHI，INF, CBA分别代表成人，儿童，婴儿，儿童买成人
        adultTagInfo = docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("ADU");
        if (docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("CHI") != null) {
            childrenTagInfo = docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("CHI");
        } else if (docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("CBA") != null) {
            childrenTagInfo = docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("CBA");
        }
        if (docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("INF") != null) {
            babyTagInfo = docResultBookingInfo.tripInfos.get(0).clientBookingResult.get(0).priceInfo.priceTag.get("INF");
        }

        List<String> aduPriceTags = new ArrayList<>();
        for (int j = 0; j < adultTagInfo.size(); j++) {
            aduPriceTags.add(adultTagInfo.get(j).productPackageCode);
        }
        List<String> childPriceTags = new ArrayList<>();
        if (babyTagInfo != null) {
            for (int j = 0; j < babyTagInfo.size(); j++) {
                childPriceTags.add(babyTagInfo.get(j).productPackageCode);
            }
        }
        for (int i = 0; i < passengerList.size(); i++) {
            if (passengerList.get(i).ageType == 1 && babyTagInfo == null) {
                ToastUtils.show("不支持儿童生单");
                return;
            }
        }

        for (int i = 0; i < passengerList.size(); i++) {
            DocPassengerInfo docPassengerInfo = new DocPassengerInfo();
            docPassengerInfo.ageType = passengerList.get(i).ageType;
            docPassengerInfo.birthday = passengerList.get(i).birthday;
            docPassengerInfo.cardNo = passengerList.get(i).cardNo;
            docPassengerInfo.mobile = contactPhone.getText().toString().trim();
            docPassengerInfo.mobilePreNum = "86";
            docPassengerInfo.name = passengerList.get(i).name;
            docPassengerInfo.sex = passengerList.get(i).sex;
            docPassengerInfo.cardType = passengerList.get(i).cardType;
            if (passengerList.get(i).ageType == 0) {
                docPassengerInfo.priceTags = aduPriceTags;
            } else {
                docPassengerInfo.priceTags = childPriceTags;
            }
            docPassengerInfoList.add(docPassengerInfo);
        }

        String passengerJson = new Gson().toJson(docPassengerInfoList);
        ContactModel contactModel = new ContactModel();
        contactModel.mobile = contactPhone.getText().toString().trim();
        contactModel.mobilePreNum = cityModel == null ? "86" : cityModel.getExtra().toString();
        contactModel.name = contactName.getText().toString().trim();
        String contactJson = new Gson().toJson(contactModel);

        if (switchButton.isChecked()) {
            sjr = addressBean.getUserName();
            sjrPhone = addressBean.getPhone();
            address = tvAddress.getText().toString().trim();
            if (invoicePosition == 2) {
                receiverTitle = "";
                taxpayerId = "";
            } else if (invoicePosition == 1 || invoicePosition == 3) {
                receiverTitle = editInvoiceTitle.getText().toString().trim();
                taxpayerId = editNum.getText().toString().trim();
            } else {
                receiverTitle = editInvoiceTitle.getText().toString().trim();
                taxpayerId = "";
            }
        } else {
            sjr = "";
            sjrPhone = "";
            address = "";
            receiverTitle = "";
            taxpayerId = "";
        }

        ReimbursementModel reimbursementModel = new ReimbursementModel();
        reimbursementModel.xcd = switchButton.isChecked();
        if (switchButton.isChecked()) {
            reimbursementModel.receiverType = invoicePosition + "";
        }
        reimbursementModel.receiverTitle = receiverTitle;
        reimbursementModel.sjr = sjr;
        reimbursementModel.sjrPhone = sjrPhone;
        reimbursementModel.sjrAddress = address;
        reimbursementModel.taxPayerID = taxpayerId;
        reimbursementModel.xcdEmail = "";

        String reimbursementJson = new Gson().toJson(reimbursementModel);

        String tripItemsJson = new Gson().toJson(docResultBookingInfo.tripInfos.get(0).tripItems);

        UserClientInfo userClientInfo = new UserClientInfo();
        String userClientInfoJson = new Gson().toJson(userClientInfo);

        OkGo.<PlaneResponse<DocGoBackCreateOrderInfo>>post(Urls.CREATE_PLANE_GO_BACK_ORDER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                // .params("userClientInfo", userClientInfoJson) //去哪儿用户名
                .params("bookingInfo", bookJson)
                .params("passengerInfos", passengerJson)
                .params("contact", contactJson)
                .params("tripItems", tripItemsJson)
                .params("reimbursement", reimbursementJson)
                .execute(new JsonCallback<PlaneResponse<DocGoBackCreateOrderInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<DocGoBackCreateOrderInfo>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0 && response.body().result != null) {
                            payValidate(response.body().result.orders.get(0).orderNo, response.body().result.noPayAmount);
                        } else {
                            goneloadDialog();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<DocGoBackCreateOrderInfo>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });

    }

    /*
    国际单程往返生单
    * */
    String bookingTagKey;

    public void createOrder2(String bkResult) {
        InterContactModel interContactModel = new InterContactModel();
        interContactModel.email = "694125155@qq.com";
        interContactModel.mobCountryCode = "86";
        interContactModel.mobile = contactPhone.getText().toString().trim();
        interContactModel.name = "YANGQINBO";
        String contactJson = new Gson().toJson(interContactModel);
        List<InterPassengerInfo> interPassengerInfoList = new ArrayList<>();
        InterPassengerInfo passengerModel = new InterPassengerInfo();
        passengerModel.name = "YANG/QINBO";
        passengerModel.ageType = 0;
        passengerModel.birthday = "2001-08-03";
        passengerModel.gender = "M";
        passengerModel.cardNum = "E95920837";
        passengerModel.cardType = "PP";
        interPassengerInfoList.add(passengerModel);

        String passengerJson = new Gson().toJson(interPassengerInfoList);

        InterXcdInfo interXcdInfo = new InterXcdInfo();
        interXcdInfo.reimburseType = 0;
        String xcdJson = new Gson().toJson(interXcdInfo);
        try {
            JSONObject object = new JSONObject(bkResult);
            bookingTagKey = object.getString("bookingTagKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<PlaneResponse>post(Urls.NET_PLANE_ORDER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("bookingResult", bkResult)
                .params("bookingTagKey", bookingTagKey)
                .params("passengersStr", passengerJson)
                .params("contact", contactJson)
                .params("xcd", xcdJson)
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show(response.body().message);
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
                    }
                });
    }

    /*
     * 国内单程生单
     * */
    private String sjr = "";
    private String sjrPhone = "";
    private String address = "";
    private String receiverTitle = "";
    private String taxpayerId = "";

    public void createOrder(String bkResult) {
        String passengerJson = new Gson().toJson(passengerList);

        if (switchButton.isChecked()) {
            sjr = addressBean.getUserName();
            sjrPhone = addressBean.getPhone();
            address = tvAddress.getText().toString().trim();
            if (invoicePosition == 2) {
                receiverTitle = "";
                taxpayerId = "";
            } else if (invoicePosition == 3) {
                receiverTitle = editInvoiceTitle.getText().toString().trim();
                taxpayerId = editNum.getText().toString().trim();
            } else {
                receiverTitle = editInvoiceTitle.getText().toString().trim();
                taxpayerId = "";
            }
        } else {
            sjr = "";
            sjrPhone = "";
            address = "";
            receiverTitle = "";
            taxpayerId = "";
        }

        PostRequest<PlaneResponse<CreateOrderInfo>> postRequest = OkGo.<PlaneResponse<CreateOrderInfo>>post(Urls.CREATE_PLANE_ORDER)
                .tag(this);
        if (switchButton.isChecked()) {
            postRequest
                    .params("receiverType", invoicePosition);
        }
        postRequest.params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("contact", contactName.getText().toString().trim())
                .params("contactMob", contactPhone.getText().toString().trim())
                //.params("contactPreNum", cityModel == null ? "86" : cityModel.getExtra().toString())
                .params("cardNo", passengerList.get(0).cardNo)
                .params("bookingResult", bkResult)
                .params("needXcd", switchButton.isChecked())
                .params("address", address)
                .params("passengerStr", passengerJson)
                .params("receiverTitle", receiverTitle)
                .params("taxpayerId", taxpayerId)
                .params("sjr", sjr)
                .params("sjrPhone", sjrPhone)
                .execute(new JsonCallback<PlaneResponse<CreateOrderInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<CreateOrderInfo>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0 && response.body().result != null) {
                            payValidate(response.body().result.orderNo, response.body().result.noPayAmount);
                        } else {
                            goneloadDialog();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<CreateOrderInfo>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });

    }

    public void payValidate(String orderNo, String noPayAmount) {
        OkGo.<PlaneResponse>get(Urls.DOMESTI_PAY_VALIDATE)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("orderNo", orderNo)
                .params("pmCode", "OUTDAIKOU")
                .params("bankCode", "ALIPAY")
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().message, response.body().code);
                        goneloadDialog();
                        /*
                         * 1007是变价的code，需要提示用户机票变价
                         * */
                        if (response.body().code == 0 || response.body().code == 1007) {
                            new XPopup.Builder(EditPlaneReserveActivity.this)
                                    .enableDrag(false)
                                    .asCustom(new CustomPayView(EditPlaneReserveActivity.this)
                                            .setData(MathUtils.subZero(noPayAmount))
                                            .setOnConfirmClickListener(new CustomPayView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(String payChannel) {
                                                    pay(orderNo, noPayAmount);
                                                }
                                            })).show();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    public void pay(String orderNo, String noPayAmount) {
        OkGo.<LzyResponse<PayModel>>post(Urls.PLANE_PAY)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("appId", "")
                .params("orderNo", orderNo)
                .params("channel", "alipay")
                .params("amount", noPayAmount)
                .params("type", "1")
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(EditPlaneReserveActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            aliPay(response.body().data.payParam, response.body().data.orderNo);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PayModel>> response) {
                        super.onError(response);
                    }
                });
    }

    private void aliPay(String payProof, String orderNo) {

        PayUtils.aliPay(EditPlaneReserveActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.show("支付成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show("支付失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                ArrayList<PassengerModel> selectPassenger = data.getParcelableArrayListExtra(PassengerListActivity.SELECT_PASSENGER);
                passengerList.addAll(selectPassenger);
                removeDuplicate(passengerList);
                mAdapter.notifyDataSetChanged();
                calculationPrice(passengerList);
            } else if (requestCode == REQUEST_ADDRESS_CODE) {
                addressBean = (AddressInfo.ShopAddressListBean) data.getSerializableExtra(AddressManagementActivity.ADDRESS_DATA);
                if (addressBean != null) {
                    name.setText(addressBean.getUserName());
                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                    phone.setText(addressBean.getPhone());
                }
            } else if (requestCode == REQUEST_PHONE_CODE) {
                cityModel = (CityModel) data.getSerializableExtra(SelectPlaneCityActivity.CITY_DATA);
                phoneAreaCode.setText("+" + cityModel.getExtra().toString());
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
        String totalAmount = MathUtils.subZero(String.valueOf((priceDetailInfo.price * priceDetailInfo.num) + (priceDetailInfo.cPrice * priceDetailInfo.cnum) + (priceDetailInfo.tof + priceDetailInfo.arf) * count + priceDetailInfo.postage));
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
