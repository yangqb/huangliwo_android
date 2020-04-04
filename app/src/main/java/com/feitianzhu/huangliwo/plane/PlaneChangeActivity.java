package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.ApplyChangeParams;
import com.feitianzhu.huangliwo.model.CustomCityModel;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengerTypesInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;
import com.feitianzhu.huangliwo.model.NationalPassengerInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.PlaneOrderModel;
import com.feitianzhu.huangliwo.model.TimePointChargsInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PlaneChangeActivity extends BaseActivity {
    public static final int DATE_REQUEST_CODE = 100;
    private DocOrderDetailInfo docOrderDetailInfo;
    private String userId;
    private String token;
    private String startDateStr;
    private String changeDate;
    private int index = -1;
    public static final String ORDER_DATA = "order_data";
    private RefundPlaneTicketPassengerAdapter mAdapter;
    private NationalPassengerInfo passengerInfo;
    private List<String> reasonList = new ArrayList<>();
    private PlaneChangeServiceAdapter serviceAdapter;
    private List<TimePointChargsInfo> timePointChargesList = new ArrayList<>();
    private String changePrice = "0";
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
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.price)
    TextView tvPrice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_plane_change;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("申请改签");
        docOrderDetailInfo = (DocOrderDetailInfo) getIntent().getSerializableExtra(ORDER_DATA);
        String[] strings = docOrderDetailInfo.flightInfo.get(0).deptTime.split("-");
        startDateStr = strings[0] + "-" + strings[1] + "-" + strings[2];
        List<DocOrderDetailPassengersInfo> passengerTypes = docOrderDetailInfo.passengers;
        mAdapter = new RefundPlaneTicketPassengerAdapter(passengerTypes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        serviceAdapter = new PlaneChangeServiceAdapter(timePointChargesList);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceAdapter.notifyDataSetChanged();

        recyclerView.setNestedScrollingEnabled(false);
        serviceRecyclerView.setNestedScrollingEnabled(false);

        setSpannableString(changePrice, tvPrice);
    }

    public void checkChange() {
        OkGo.<PlaneResponse<List<NationalPassengerInfo>>>get(Urls.CHANGE_SEARCH)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("orderNo", docOrderDetailInfo.detail.orderNo)
                .params("changeDate", changeDate)
                .execute(new JsonCallback<PlaneResponse<List<NationalPassengerInfo>>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onSuccess(PlaneChangeActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0 && response.body().result != null) {
                            if (response.body().result.get(0).changeSearchResult.changeRuleInfo.timePointChargesList != null) {
                                timePointChargesList = response.body().result.get(0).changeSearchResult.changeRuleInfo.timePointChargesList;
                                passengerInfo = response.body().result.get(0);
                                serviceAdapter.setNewData(timePointChargesList);
                                serviceAdapter.notifyDataSetChanged();
                            }
                           /* String json = new Gson().toJson(response.body().result);
                            Intent intent = new Intent(PlaneChangeActivity.this, PlaneChangeDetailActivity.class);
                            intent.putExtra(PlaneChangeDetailActivity.PLANE_TYPE, planeType);
                            startActivity(intent);*/
                            reasonList.clear();
                            tvShippingSpace.setText("");
                            if (passengerInfo.changeSearchResult.tgqReasons != null) {
                                for (int i = 0; i < passengerInfo.changeSearchResult.tgqReasons.size(); i++) {
                                    reasonList.add(passengerInfo.changeSearchResult.tgqReasons.get(i).msg);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onError(response);
                    }
                });
    }


    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.btn_bottom, R.id.rl_shippingSpace, R.id.select_date})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.rl_shippingSpace:
                selectShoppingSpaceDialog();
                break;
            case R.id.btn_bottom:
                apply();
                break;
            case R.id.select_date:
                intent = new Intent(PlaneChangeActivity.this, PlaneCalendarActivity.class);
                intent.putExtra(PlaneCalendarActivity.SELECT_MODEL, 0);
                startActivityForResult(intent, DATE_REQUEST_CODE);
                break;
        }
    }


    public void apply() {
        if (TextUtils.isEmpty(tvDate.getText().toString())) {
            ToastUtils.showShortToast("请选择改签日期");
            return;
        }
        if (startDateStr.equals(tvDate.getText().toString())) {
            ToastUtils.showShortToast("不能改签同一天");
            return;
        }

        if (TextUtils.isEmpty(tvShippingSpace.getText().toString())) {
            ToastUtils.showShortToast("请选择改签原因");
            return;
        }
        if (!passengerInfo.changeSearchResult.canChange) {
            ToastUtils.showShortToast(passengerInfo.changeSearchResult.reason);
            return;
        }

        ApplyChangeParams params = new ApplyChangeParams();
        params.applyRemarks = passengerInfo.changeSearchResult.tgqReasons.get(index).msg;
        params.cabinCode = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).cabinCode;
        params.callbackUrl = "";
        params.changeCauseId = passengerInfo.changeSearchResult.tgqReasons.get(index).code + "";
        params.childExtraPrice = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).childUpgradeFee;
        params.childUseFee = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).childGqFee;
        params.endTime = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).endTime;
        params.flightNo = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).flightNo;
        params.gqFee = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).gqFee;
        params.orderNo = docOrderDetailInfo.detail.orderNo;
        params.passengerIds = passengerInfo.id + "";
        params.startDate = changeDate;
        params.startTime = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).startTime;
        params.uniqKey = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).uniqKey;
        params.upgradeFee = passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).upgradeFee;

        String json = new Gson().toJson(params);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        OkGo.<PlaneResponse<List<NationalPassengerInfo>>>post(Urls.APPLY_CHANGE)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .upRequestBody(body)
                .execute(new JsonCallback<PlaneResponse<List<NationalPassengerInfo>>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onSuccess(PlaneChangeActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0 && response.body().result != null && response.body().result.get(0).changeApplyResult.success) {
                            if (passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).allFee == 0) {
                                ToastUtils.showShortToast("申请成功");
                            } else {
                                pay(docOrderDetailInfo.detail.orderNo, MathUtils.subZero(String.valueOf(passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).allFee)));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
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
                .params("type", "2")
                .params("gqId", passengerInfo.changeApplyResult.gqId)
                .params("passengerIds", passengerInfo.id + "")
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(PlaneChangeActivity.this, response.body().msg, response.body().code);
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

        PayUtils.aliPay(PlaneChangeActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("申请成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });
    }

    public void selectShoppingSpaceDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(PlaneChangeActivity.this)
                        .setData(reasonList)
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                tvShippingSpace.setText(reasonList.get(position));
                                index = position;
                                changePrice = MathUtils.subZero(String.valueOf(passengerInfo.changeSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).allFee));
                                setSpannableString(changePrice, tvPrice);
                            }
                        }))
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DATE_REQUEST_CODE) {
                changeDate = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE);
                tvDate.setText(changeDate);
                checkChange();
            }
        }
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
