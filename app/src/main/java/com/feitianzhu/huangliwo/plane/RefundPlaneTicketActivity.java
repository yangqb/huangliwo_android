package com.feitianzhu.huangliwo.plane;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.ApplyChangeParams;
import com.feitianzhu.huangliwo.model.ApplyRefundParams;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailPassengersInfo;
import com.feitianzhu.huangliwo.model.NationalPassengerInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RefundPlaneTicketActivity extends BaseActivity {
    private RefundPlaneTicketPassengerAdapter mAdapter;
    public static final String ORDER_DATA = "order_data";
    private NationalPassengerInfo passengerInfo;
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
                    public void onSuccess(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onSuccess(RefundPlaneTicketActivity.this, response.body().message, response.body().code);
                        if (response.body().code == 0) {
                            passengerInfo = response.body().result.get(0);
                            reasonList.clear();
                            for (int i = 0; i < passengerInfo.refundSearchResult.tgqReasons.size(); i++) {
                                reasonList.add(passengerInfo.refundSearchResult.tgqReasons.get(i).msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<PlaneResponse<List<NationalPassengerInfo>>> response) {
                        super.onError(response);
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
                selectShoppingSpaceDialog();
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

    public void selectShoppingSpaceDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(RefundPlaneTicketActivity.this)
                        .setData(reasonList)
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                tvReason.setText(reasonList.get(position));
                                index = position;
                            }
                        }))
                .show();
    }

    public void apply() {

        ApplyRefundParams params = new ApplyRefundParams();
        params.applyRemarks = passengerInfo.refundSearchResult.tgqReasons.get(index).msg;
        params.cabinCode = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).cabinCode;
        params.callbackUrl = "";
        params.changeCauseId = passengerInfo.refundSearchResult.tgqReasons.get(index).code + "";
        params.childExtraPrice = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).childUpgradeFee;
        params.childUseFee = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).childGqFee;
        params.endTime = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).endTime;
        params.flightNo = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).flightNo;
        params.gqFee = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).gqFee;
        params.orderNo = docOrderDetailInfo.detail.orderNo;
        params.passengerIds = passengerInfo.id + "";
        params.startDate = startDateStr;
        params.startTime = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).startTime;
        params.uniqKey = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).uniqKey;
        params.upgradeFee = passengerInfo.refundSearchResult.tgqReasons.get(index).changeFlightSegmentList.get(0).upgradeFee;

        OkGo.<PlaneResponse>post(Urls.APPLY_REFUND)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("refundRequest", docOrderDetailInfo.detail.orderNo)
                .execute(new JsonCallback<PlaneResponse>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse> response) {
                        super.onSuccess(RefundPlaneTicketActivity.this, response.body().message, response.body().code);

                    }

                    @Override
                    public void onError(Response<PlaneResponse> response) {
                        super.onError(response);
                    }
                });
    }
}
