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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengerTypesInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;
import com.feitianzhu.huangliwo.model.InterOrderDetailInfo;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.model.PlaneOrderStatus;
import com.feitianzhu.huangliwo.model.RefundChangeInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
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
    private List<DocOrderDetailPassengerTypesInfo> adultPassengerTypes = new ArrayList<>();
    private List<DocOrderDetailPassengerTypesInfo> childPassengerTypes = new ArrayList<>();

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderPassengerAdapter(passengers);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (orderModel.type == 1 || orderModel.type == 3) {
            ll_go_bg.setVisibility(View.GONE);
            tv_go_tag.setVisibility(View.GONE);
            ll_go_title.setVisibility(View.VISIBLE);
            ll_come_detail.setVisibility(View.GONE);
            if (orderModel.status == PlaneOrderStatus.BOOK_OK) {
                rl_refund_change.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
            } else if (orderModel.status == PlaneOrderStatus.TICKET_OK) {
                rl_refund_change.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
            }
        } else {
            ll_go_bg.setVisibility(View.VISIBLE);
            tv_go_tag.setVisibility(View.VISIBLE);
            ll_go_title.setVisibility(View.GONE);
            ll_come_detail.setVisibility(View.VISIBLE);
            if (orderModel.status == PlaneOrderStatus.BOOK_OK) {
                rl_refund_change.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
            } else if (orderModel.status == PlaneOrderStatus.TICKET_OK) {
                rl_refund_change.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
            }
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
                                DocOrderDetailInfo docOrderDetailInfo = response.body().result;
                                goDate.setText(DateUtils.strToStr(docOrderDetailInfo.flightInfo.get(0).deptTime) + DateUtils.strToDate2(DateUtils.strToStr2(docOrderDetailInfo.flightInfo.get(0).deptTime)));
                                goCity.setText(docOrderDetailInfo.flightInfo.get(0).dptCity + "-" + docOrderDetailInfo.flightInfo.get(0).arrCity);
                                goDepTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[3]);
                                goArrTime.setText(docOrderDetailInfo.flightInfo.get(0).deptTime.split("-")[4]);
                                contactName.setText(docOrderDetailInfo.contacterInfo.name);
                                contactPhone.setText(docOrderDetailInfo.contacterInfo.mobile);

                                mAdapter.setNewData(docOrderDetailInfo.passengers);
                                mAdapter.notifyDataSetChanged();

                                for (int i = 0; i < docOrderDetailInfo.passengerTypes.size(); i++) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(i).ageType)) {
                                        adultPassengerTypes.add(docOrderDetailInfo.passengerTypes.get(i));
                                    } else {
                                        childPassengerTypes.add(docOrderDetailInfo.passengerTypes.get(i));
                                    }
                                }

                                if (adultPassengerTypes.size() > 0) {
                                    priceDetailInfo.price = adultPassengerTypes.get(0).realPrice;
                                } else {
                                    priceDetailInfo.price = 0;
                                }
                                if (childPassengerTypes.size() > 0) {
                                    priceDetailInfo.cPrice = childPassengerTypes.get(0).realPrice;
                                } else {
                                    priceDetailInfo.cPrice = 0;
                                }
                                priceDetailInfo.arf = docOrderDetailInfo.passengerTypes.get(0).constructionFee; //机建加燃油
                                priceDetailInfo.tof = 0;
                                priceDetailInfo.cnum = childPassengerTypes.size();
                                priceDetailInfo.num = adultPassengerTypes.size();

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
                                DocOrderDetailInfo docOrderDetailInfo = response.body().result;
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

                                for (int i = 0; i < docOrderDetailInfo.passengerTypes.size(); i++) {
                                    if ("成人".equals(docOrderDetailInfo.passengerTypes.get(i).ageType)) {
                                        adultPassengerTypes.add(docOrderDetailInfo.passengerTypes.get(i));
                                    } else {
                                        childPassengerTypes.add(docOrderDetailInfo.passengerTypes.get(i));
                                    }
                                }

                                if (adultPassengerTypes.size() > 0) {
                                    priceDetailInfo.price = adultPassengerTypes.get(0).realPrice;
                                } else {
                                    priceDetailInfo.price = 0;
                                }
                                if (childPassengerTypes.size() > 0) {
                                    priceDetailInfo.cPrice = childPassengerTypes.get(0).realPrice;
                                } else {
                                    priceDetailInfo.cPrice = 0;
                                }
                                priceDetailInfo.arf = docOrderDetailInfo.passengerTypes.get(0).constructionFee; //机建加燃油
                                priceDetailInfo.tof = 0;
                                priceDetailInfo.cnum = childPassengerTypes.size();
                                priceDetailInfo.num = adultPassengerTypes.size();

                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<DocOrderDetailInfo>> response) {
                            super.onError(response);
                        }
                    });

        }

    }

    @OnClick({R.id.btn_reimbursement, R.id.btn_refund, R.id.btn_change, R.id.left_button, R.id.price})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_reimbursement:
                intent = new Intent(PlaneOrderDetailActivity.this, PlaneReimbursementActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_refund:
                intent = new Intent(PlaneOrderDetailActivity.this, RefundPlaneTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_change:
                intent = new Intent(PlaneOrderDetailActivity.this, PlaneChangeActivity.class);
                intent.putExtra(PlaneChangeActivity.PLANE_TYPE, orderModel.type);
                startActivity(intent);
                break;
            case R.id.price:
                new XPopup.Builder(this)
                        .enableDrag(false)
                        .asCustom(new CustomTotalPriceInfoView(PlaneOrderDetailActivity.this).setData(priceDetailInfo)).show();
                break;
        }
    }
}
