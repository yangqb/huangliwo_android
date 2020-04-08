package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengerTypesInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;
import com.feitianzhu.huangliwo.model.InterOrderDetailInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.model.PlaneOrderStatus;
import com.feitianzhu.huangliwo.model.RefundChangeInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomPayView;
import com.feitianzhu.huangliwo.view.CustomTotalPriceInfoView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneOrderDetailActivity extends BaseActivity {
    public static final String ORDER_DATA = "order_data";
    private PlaneOrderModel orderModel;
    private OrderPassengerAdapter mAdapter;
    private List<DocOrderDetailPassengersInfo> passengers = new ArrayList<>();
    private CustomPriceDetailInfo priceDetailInfo = new CustomPriceDetailInfo();
    private DocOrderDetailPassengerTypesInfo adultPassengerType;
    private DocOrderDetailPassengerTypesInfo childPassengerType;
    private DocOrderDetailInfo docOrderDetailInfo;

    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_go_bg)
    LinearLayout ll_go_bg;
    @BindView(R.id.tv_go_tag)
    TextView tv_go_tag;
    @BindView(R.id.ll_go_title)
    LinearLayout ll_go_title;
    @BindView(R.id.ll_come_detail)
    LinearLayout ll_come_detail;
    @BindView(R.id.rl_refund_change)
    RelativeLayout rl_refund_change;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.go_depDate)
    TextView goDepDate;
    @BindView(R.id.go_cityName)
    TextView goCityName;
    @BindView(R.id.back_depDate)
    TextView backDepDate;
    @BindView(R.id.back_cityName)
    TextView backCityName;
    @BindView(R.id.go_depTime)
    TextView goDepTime;
    @BindView(R.id.go_arrTime)
    TextView goArrTime;
    @BindView(R.id.back_depTime)
    TextView backDepTime;
    @BindView(R.id.back_arrTime)
    TextView backArrTime;
    @BindView(R.id.go_date)
    TextView goDate;
    @BindView(R.id.go_city)
    TextView goCity;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.contactName)
    TextView contactName;
    @BindView(R.id.contactPhone)
    TextView contactPhone;
    @BindView(R.id.go_depAirportName)
    TextView goDepAirportName;
    @BindView(R.id.go_arrAirportName)
    TextView goArrAirportName;
    @BindView(R.id.back_depAirportName)
    TextView backDepAirportName;
    @BindView(R.id.back_arrAirportName)
    TextView backArrAirportName;
    @BindView(R.id.backFlightNum)
    TextView backFlightNum;
    @BindView(R.id.goFlightNum)
    TextView goFlightNum;
    @BindView(R.id.rl_luggage_change_notice)
    RelativeLayout rlLuggageChangeNotice;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_order_detail;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        orderModel = (PlaneOrderModel) getIntent().getSerializableExtra(ORDER_DATA);
        price.setText("¥" + MathUtils.subZero(String.valueOf(orderModel.noPayAmount)));
        if (orderModel.status == PlaneOrderStatus.BOOK_OK) {
            tvStatus.setText("等待付款");
        } else if (orderModel.status == PlaneOrderStatus.TICKET_LOCK) {
            tvStatus.setText("等待出票");
        } else if (orderModel.status == PlaneOrderStatus.TICKET_OK) {
            tvStatus.setText("出票完成");
        } else if (orderModel.status == PlaneOrderStatus.CANCEL_OK) {
            tvStatus.setText("订单取消");
        } else if (orderModel.status == PlaneOrderStatus.WAIT_REFUNDMENT || orderModel.status == PlaneOrderStatus.APPLY_RETURN_PAY) {
            tvStatus.setText("等待退款");
        } else if (orderModel.status == PlaneOrderStatus.REFUND_OK) {
            tvStatus.setText("退款完成");
        } else if (orderModel.status == PlaneOrderStatus.APPLY_CHANGE) {
            tvStatus.setText("改签中");
        } else if (orderModel.status == PlaneOrderStatus.CHANGE_OK) {
            tvStatus.setText("改签完成");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderPassengerAdapter(passengers);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        goDepAirportName.setText(orderModel.dptAirport + orderModel.dptTerminal);
        goArrAirportName.setText(orderModel.arrAirport + orderModel.arrTerminal);
        goFlightNum.setText(orderModel.flightNum);
        if (orderModel.type == 1 || orderModel.type == 3) {
            ll_go_bg.setVisibility(View.GONE);
            tv_go_tag.setVisibility(View.GONE);
            ll_go_title.setVisibility(View.VISIBLE);
            ll_come_detail.setVisibility(View.GONE);
            if (orderModel.status == PlaneOrderStatus.BOOK_OK || orderModel.status == PlaneOrderStatus.CANCEL_OK) {
                rl_refund_change.setVisibility(View.GONE);
            } else {
                rl_refund_change.setVisibility(View.VISIBLE);
            }
            if (orderModel.status == PlaneOrderStatus.CHANGE_OK || orderModel.status == PlaneOrderStatus.APPLY_CHANGE) {
                rlLuggageChangeNotice.setVisibility(View.VISIBLE);
            } else {
                rlLuggageChangeNotice.setVisibility(View.GONE);
            }

            if (orderModel.status == PlaneOrderStatus.BOOK_OK) {
                rl_bottom.setVisibility(View.VISIBLE);
            } else {
                rl_bottom.setVisibility(View.GONE);
            }
        } else {
            ll_go_bg.setVisibility(View.VISIBLE);
            tv_go_tag.setVisibility(View.VISIBLE);
            ll_go_title.setVisibility(View.GONE);
            ll_come_detail.setVisibility(View.VISIBLE);
            if (orderModel.status == PlaneOrderStatus.BOOK_OK || orderModel.status == PlaneOrderStatus.CANCEL_OK) {
                rl_refund_change.setVisibility(View.GONE);
            } else {
                rl_refund_change.setVisibility(View.VISIBLE);
            }
            if (orderModel.status == PlaneOrderStatus.CHANGE_OK || orderModel.status == PlaneOrderStatus.APPLY_CHANGE) {
                rlLuggageChangeNotice.setVisibility(View.VISIBLE);
            } else {
                rlLuggageChangeNotice.setVisibility(View.GONE);
            }

            if (orderModel.status == PlaneOrderStatus.BOOK_OK) {
                rl_bottom.setVisibility(View.VISIBLE);
            } else {
                rl_bottom.setVisibility(View.GONE);
            }
            backDepAirportName.setText(orderModel.backDptAirport + orderModel.backDptTerminal);
            backArrAirportName.setText(orderModel.backArrAirport + orderModel.backArrTerminal);
            backFlightNum.setText(orderModel.backFlightNum);
        }
    }

    @Override
    protected void initData() {
        if (orderModel.type == 3 || orderModel.type == 4) {
            //国际单程和往返
            OkGo.<PlaneResponse<InterOrderDetailInfo>>get(Urls.GET_INTERNATIONAl_ORDER_DETAIL)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("orderNo", orderModel.sourceOrderNo)
                    .execute(new JsonCallback<PlaneResponse<InterOrderDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<InterOrderDetailInfo>> response) {
                            super.onSuccess(PlaneOrderDetailActivity.this, response.body().message, response.body().code);
                            String json = new Gson().toJson(response.body().result);
                           /* if (response.body().code == 0) {
                                InterOrderDetailInfo interOrderDetailInfo = response.body().result;

                                goDepDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(0).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(0).deptTime)));
                                goCityName.setText(docOrderDetailInfo.flightInfo.get(0).dptCity + "-" + docOrderDetailInfo.flightInfo.get(0).arrCity);
                                backDepDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(1).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(1).deptTime)));
                                backCityName.setText(docOrderDetailInfo.flightInfo.get(1).dptCity + "-" + docOrderDetailInfo.flightInfo.get(1).arrCity);
                                goDepTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[3]);
                                goArrTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[4]);
                                backDepTime.setText(docOrderDetailInfo.flightInfo.get(1).deptTime.split("-")[3]);
                                backArrTime.setText(docOrderDetailInfo.flightInfo.get(1).deptTime.split("-")[4]);
                                contactName.setText(docOrderDetailInfo.contacterInfo.name);
                                contactPhone.setText(docOrderDetailInfo.contacterInfo.mobile);

                                mAdapter.setNewData(docOrderDetailInfo.passengers);
                                mAdapter.notifyDataSetChanged();

                            }*/
                        }

                        @Override
                        public void onError(Response<PlaneResponse<InterOrderDetailInfo>> response) {
                            super.onError(response);
                        }
                    });
        } else if (orderModel.type == 1) {
            //国内单程
            OkGo.<PlaneResponse<DocOrderDetailInfo>>get(Urls.DOMESTIC_ORDER_DETAIL)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("orderNo", orderModel.sourceOrderNo)
                    .execute(new JsonCallback<PlaneResponse<DocOrderDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<DocOrderDetailInfo>> response) {
                            super.onSuccess(PlaneOrderDetailActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0) {
                                docOrderDetailInfo = response.body().result;
                                goDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(0).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(0).deptTime)));
                                goCity.setText(docOrderDetailInfo.flightInfo.get(0).dptCity + "-" + docOrderDetailInfo.flightInfo.get(0).arrCity);
                                goDepTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[3]);
                                goArrTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[4]);
                                contactName.setText(docOrderDetailInfo.contacterInfo.name);
                                contactPhone.setText(docOrderDetailInfo.contacterInfo.mobile.substring(0, 3) + "****" + docOrderDetailInfo.contacterInfo.mobile.substring(8, 11));

                                mAdapter.setNewData(docOrderDetailInfo.passengers);
                                mAdapter.notifyDataSetChanged();

                                if (docOrderDetailInfo.passengerTypes.size() > 0) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(0).ageType)) {
                                        adultPassengerType = docOrderDetailInfo.passengerTypes.get(0);
                                    } else {
                                        childPassengerType = docOrderDetailInfo.passengerTypes.get(0);
                                    }
                                }

                                if (docOrderDetailInfo.passengerTypes.size() > 1) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(1).ageType)) {
                                        adultPassengerType = docOrderDetailInfo.passengerTypes.get(1);
                                    } else {
                                        childPassengerType = docOrderDetailInfo.passengerTypes.get(1);
                                    }
                                }
                                if (adultPassengerType != null) {
                                    priceDetailInfo.price = adultPassengerType.realPrice;
                                    priceDetailInfo.arf = adultPassengerType.constructionFee; //机建加燃油
                                    priceDetailInfo.num = adultPassengerType.Count;
                                } else {
                                    priceDetailInfo.price = 0;
                                    priceDetailInfo.arf = 0;
                                    priceDetailInfo.num = 0;

                                }
                                if (childPassengerType != null) {
                                    priceDetailInfo.cPrice = childPassengerType.realPrice;
                                    priceDetailInfo.cnum = childPassengerType.Count;
                                } else {
                                    priceDetailInfo.cPrice = 0;
                                    priceDetailInfo.cnum = 0;
                                }
                                priceDetailInfo.tof = 0;
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<DocOrderDetailInfo>> response) {
                            super.onError(response);
                        }
                    });

        } else {
            //国内往返
            OkGo.<PlaneResponse<DocOrderDetailInfo>>get(Urls.GO_BACK_ORDER_DETAIL)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("orderNo", orderModel.sourceOrderNo)
                    .execute(new JsonCallback<PlaneResponse<DocOrderDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<DocOrderDetailInfo>> response) {
                            super.onSuccess(PlaneOrderDetailActivity.this, response.body().message, response.body().code);
                            if (response.body().code == 0) {
                                docOrderDetailInfo = response.body().result;
                                goDepDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(0).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(0).deptTime)));
                                goCityName.setText(docOrderDetailInfo.flightInfo.get(0).dptCity + "-" + docOrderDetailInfo.flightInfo.get(0).arrCity);
                                backDepDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(1).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(1).deptTime)));
                                backCityName.setText(docOrderDetailInfo.flightInfo.get(1).dptCity + "-" + docOrderDetailInfo.flightInfo.get(1).arrCity);
                                goDepTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[3]);
                                goArrTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[4]);
                                backDepTime.setText(docOrderDetailInfo.flightInfo.get(1).deptTime.split("-")[3]);
                                backArrTime.setText(docOrderDetailInfo.flightInfo.get(1).deptTime.split("-")[4]);
                                contactName.setText(docOrderDetailInfo.contacterInfo.name);
                                contactPhone.setText(docOrderDetailInfo.contacterInfo.mobile);

                                mAdapter.setNewData(docOrderDetailInfo.passengers);
                                mAdapter.notifyDataSetChanged();

                                if (docOrderDetailInfo.passengerTypes.size() > 0) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(0).ageType)) {
                                        adultPassengerType = docOrderDetailInfo.passengerTypes.get(0);
                                    } else {
                                        childPassengerType = docOrderDetailInfo.passengerTypes.get(0);
                                    }
                                }

                                if (docOrderDetailInfo.passengerTypes.size() > 1) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(1).ageType)) {
                                        adultPassengerType = docOrderDetailInfo.passengerTypes.get(1);
                                    } else {
                                        childPassengerType = docOrderDetailInfo.passengerTypes.get(1);
                                    }
                                }
                                if (adultPassengerType != null) {
                                    priceDetailInfo.price = adultPassengerType.realPrice;
                                    priceDetailInfo.arf = adultPassengerType.constructionFee; //机建加燃油
                                    priceDetailInfo.num = adultPassengerType.Count;
                                } else {
                                    priceDetailInfo.price = 0;
                                    priceDetailInfo.arf = 0;
                                    priceDetailInfo.num = 0;

                                }
                                if (childPassengerType != null) {
                                    priceDetailInfo.cPrice = childPassengerType.realPrice;
                                    priceDetailInfo.cnum = childPassengerType.Count;
                                } else {
                                    priceDetailInfo.cPrice = 0;
                                    priceDetailInfo.cnum = 0;
                                }
                                priceDetailInfo.tof = 0;

                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<DocOrderDetailInfo>> response) {
                            super.onError(response);
                        }
                    });

        }

    }

    @OnClick({R.id.btn_reimbursement, R.id.btn_refund, R.id.btn_change, R.id.left_button, R.id.price, R.id.pay})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_reimbursement:
               /* if (orderModel.status == PlaneOrderStatus.PAY_OK ||
                        orderModel.status == PlaneOrderStatus.TICKET_OK || orderModel.status == PlaneOrderStatus.TICKET_LOCK ||
                        orderModel.status == PlaneOrderStatus.WAIT_CONFIRM || orderModel.status == PlaneOrderStatus.CHANGE_OK) {
                    intent = new Intent(PlaneOrderDetailActivity.this, PlaneReimbursementActivity.class);
                    intent.putExtra(PlaneReimbursementActivity.ORDER_DATA, docOrderDetailInfo);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("当前订单状态不可报销");
                }*/
                intent = new Intent(PlaneOrderDetailActivity.this, PlaneReimbursementActivity.class);
                intent.putExtra(PlaneReimbursementActivity.ORDER_DATA, docOrderDetailInfo);
                startActivity(intent);
                break;
            case R.id.btn_refund:
                if (orderModel.status == PlaneOrderStatus.PAY_OK || orderModel.status == PlaneOrderStatus.TICKET_OK ||
                        orderModel.status == PlaneOrderStatus.TICKET_LOCK || orderModel.status == PlaneOrderStatus.WAIT_CONFIRM ||
                        orderModel.status == PlaneOrderStatus.CHANGE_OK || orderModel.status == PlaneOrderStatus.APPLY_RETURN_PAY) {
                    intent = new Intent(PlaneOrderDetailActivity.this, RefundPlaneTicketActivity.class);
                    intent.putExtra(RefundPlaneTicketActivity.ORDER_DATA, docOrderDetailInfo);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("当前订单状态不可退票");
                }
                break;
            case R.id.btn_change:
                if (orderModel.status == PlaneOrderStatus.TICKET_OK || orderModel.status == PlaneOrderStatus.WAIT_CONFIRM || orderModel.status == PlaneOrderStatus.CHANGE_OK) {
                    intent = new Intent(PlaneOrderDetailActivity.this, PlaneChangeActivity.class);
                    intent.putExtra(PlaneChangeActivity.ORDER_DATA, docOrderDetailInfo);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("当前订单状态不可改签");
                }

                break;
            case R.id.price:
                new XPopup.Builder(this)
                        .enableDrag(false)
                        .asCustom(new CustomTotalPriceInfoView(PlaneOrderDetailActivity.this).setData(priceDetailInfo)).show();
                break;
            case R.id.pay:
                new XPopup.Builder(this)
                        .enableDrag(false)
                        .asCustom(new CustomPayView(PlaneOrderDetailActivity.this)
                                .setData(MathUtils.subZero(String.valueOf(orderModel.noPayAmount)))
                                .setOnConfirmClickListener(new CustomPayView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String payChannel) {
                                        payValidate();
                                    }
                                })).show();
                break;
        }
    }

    public void payValidate() {
        OkGo.<PlaneResponse>get(Urls.DOMESTI_PAY_VALIDATE)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("orderNo", orderModel.sourceOrderNo)
                .params("pmCode", "OUTDAIKOU")
                .params("bankCode", "ALIPAY")
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(PlaneOrderDetailActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0) {
                            new XPopup.Builder(PlaneOrderDetailActivity.this)
                                    .enableDrag(false)
                                    .asCustom(new CustomPayView(PlaneOrderDetailActivity.this)
                                            .setData(MathUtils.subZero(String.valueOf(orderModel.noPayAmount)))
                                            .setOnConfirmClickListener(new CustomPayView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(String payChannel) {
                                                    pay(orderModel.sourceOrderNo, MathUtils.subZero(String.valueOf(orderModel.noPayAmount)));
                                                }
                                            })).show();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
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
                        super.onSuccess(PlaneOrderDetailActivity.this, response.body().msg, response.body().code);
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

        PayUtils.aliPay(PlaneOrderDetailActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });
    }
}
