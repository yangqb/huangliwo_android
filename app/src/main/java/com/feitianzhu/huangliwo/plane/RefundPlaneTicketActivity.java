package com.feitianzhu.huangliwo.plane;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.ApplyRefundParams;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;
import com.feitianzhu.huangliwo.model.NationalPassengerInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RefundPlaneTicketActivity extends BaseActivity {
    private RefundPlaneTicketPassengerAdapter mAdapter;
    public static final String ORDER_DATA = "order_data";
    private NationalPassengerInfo passengerInfo;
    private List<NationalPassengerInfo> passengerInfos;
    private List<String> reasonList = new ArrayList<>();
    private DocOrderDetailInfo docOrderDetailInfo;
    private String startDateStr;
    private String userId;
    private String token;
    private int index = -1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.serviceAmount)
    TextView serviceAmount;
    @BindView(R.id.refundAmount)
    TextView refundAmount;
    @BindView(R.id.totalAmount)
    TextView totalAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund_plane_ticket;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        docOrderDetailInfo = (DocOrderDetailInfo) getIntent().getSerializableExtra(ORDER_DATA);
        String[] strings = docOrderDetailInfo.flightInfo.get(0).deptTime.split("-");
        startDateStr = strings[0] + "-" + strings[1] + "-" + strings[2];
        List<DocOrderDetailPassengersInfo> passengerTypes = docOrderDetailInfo.passengers;
        mAdapter = new RefundPlaneTicketPassengerAdapter(passengerTypes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        OkGo.<PlaneResponse<List<NationalPassengerInfo>>>get(Urls.REFUND_SEARCH)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("orderNo", docOrderDetailInfo.detail.orderNo)
                .execute(new JsonCallback<PlaneResponse<List<NationalPassengerInfo>>>() {
                    @Override
                    public void onStart(Request<PlaneResponse<List<NationalPassengerInfo>>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onSuccess(RefundPlaneTicketActivity.this, response.body().message, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().result != null) {
                            passengerInfos = response.body().result;
                            passengerInfo = response.body().result.get(0);
                            if (passengerInfo.refundSearchResult.canRefund) {
                                reasonList.clear();
                                if (passengerInfo.refundSearchResult.tgqReasons != null) {
                                    for (int i = 0; i < passengerInfo.refundSearchResult.tgqReasons.size(); i++) {
                                        if (passengerInfo.refundSearchResult.tgqReasons.get(i).code != 19) { //去掉身体原因且有二级甲等医院<含>以上的医院证明）
                                            reasonList.add(passengerInfo.refundSearchResult.tgqReasons.get(i).msg);
                                        }
                                    }
                                }
                            } else {
                                ToastUtils.show(passengerInfo.refundSearchResult.reason);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.refund_reason, R.id.invoiceType, R.id.identification_num_explain, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.refund_reason:
                if (reasonList != null && reasonList.size() > 0) {
                    selectShoppingSpaceDialog();
                }
                break;
            case R.id.invoiceType:
                String[] strings1 = new String[]{"企业", "个人", "政府机关行政单位"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(RefundPlaneTicketActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                    }
                                }))
                        .show();
                break;
            case R.id.identification_num_explain:
                String content = "纳税人识别号是企业税务登记证上唯一识别企业的税号，由15/18或20位数码组成。根据国家税务局规定，自2017年7月1日起，开具增值税普通发票必须有纳税人识别号或统一社会信用代码，否则该发票为不符合规定的发票，不得作为税收凭证。纳税人识别号可登录纳税人信息查询网www.yibannashuiren.com 查询，或向公司财务人员咨询。";

                new XPopup.Builder(RefundPlaneTicketActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;

            case R.id.submit:
                apply();
                break;
        }
    }

    private int totalServiceAmount;
    private int totalRefundAmount;

    public void selectShoppingSpaceDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(RefundPlaneTicketActivity.this)
                        .setData(reasonList)
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                tvReason.setText(reasonList.get(position));
                                index = position;
                                for (int i = 0; i < passengerInfos.size(); i++) {
                                    totalRefundAmount += passengerInfos.get(i).refundSearchResult.tgqReasons.get(index).refundPassengerPriceInfoList.get(0).refundFeeInfo.returnRefundFee;
                                    totalServiceAmount += passengerInfos.get(i).refundSearchResult.tgqReasons.get(index).refundPassengerPriceInfoList.get(0).refundFeeInfo.refundFee;
                                }
                                serviceAmount.setText("¥" + MathUtils.subZero(String.valueOf(totalServiceAmount)));
                                refundAmount.setText("¥" + MathUtils.subZero(String.valueOf(totalRefundAmount)));
                                totalAmount.setText("¥" + MathUtils.subZero(String.valueOf(docOrderDetailInfo.passengerTypes.get(0).allPrices)));
                            }
                        }))
                .show();
    }

    public void apply() {
        if (TextUtils.isEmpty(tvReason.getText().toString())) {
            ToastUtils.show("请选择退票原因");
            return;
        }
        if (!passengerInfo.refundSearchResult.canRefund) {
            ToastUtils.show(passengerInfo.refundSearchResult.reason);
            return;
        }

        ApplyRefundParams params = new ApplyRefundParams();
        params.callbackUrl = "";
        params.orderNo = docOrderDetailInfo.detail.orderNo;
        params.passengerIds = passengerInfo.id + "";
        params.refundCause = passengerInfo.refundSearchResult.tgqReasons.get(index).msg;
        params.refundCauseId = passengerInfo.refundSearchResult.tgqReasons.get(index).code + "";
        params.amount = String.valueOf(totalRefundAmount);
        String json = new Gson().toJson(params);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        OkGo.<PlaneResponse<List<NationalPassengerInfo>>>post(Urls.APPLY_REFUND)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .upRequestBody(body)
                .execute(new JsonCallback<PlaneResponse<List<NationalPassengerInfo>>>() {
                    @Override
                    public void onStart(Request<PlaneResponse<List<NationalPassengerInfo>>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onSuccess(RefundPlaneTicketActivity.this, response.body().message, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().result != null && response.body().result.get(0).refundApplyResult.success) {
                            ToastUtils.show("申请成功");
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }
}
