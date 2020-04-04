package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.AddressManagementActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.model.AskForResultInfo;
import com.feitianzhu.huangliwo.model.DocOrderDetailInfo;
import com.feitianzhu.huangliwo.model.ItineraryInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.RefundAskForParams;
import com.feitianzhu.huangliwo.model.RefundServiceInfo;
import com.feitianzhu.huangliwo.shop.ShopPayActivity;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PlaneReimbursementActivity extends BaseActivity {
    public static final String ORDER_DATA = "order_data";
    private static final int REQUEST_CODE = 1000;
    private int reimbursementPosition = -1;
    private AddressInfo.ShopAddressListBean addressBean;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
    private String userId;
    private String token;
    private DocOrderDetailInfo docOrderDetailInfo;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tvReimbursement)
    TextView tvReimbursement;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_reimbursement;
    }

    @Override
    protected void initView() {
        titleName.setText("索要报销凭证");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        docOrderDetailInfo = (DocOrderDetailInfo) getIntent().getSerializableExtra(ORDER_DATA);
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
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().msg, response.body().code);
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

    @OnClick({R.id.left_button, R.id.invoiceType, R.id.rl_address, R.id.identification_num_explain, R.id.reimbursementType, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.invoiceType:
                String[] strings1 = new String[]{"企业", "个人", "政府机关行政单位"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(PlaneReimbursementActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvInvoice.setText(strings1[position]);
                                    }
                                }))
                        .show();
                break;
            case R.id.rl_address:
                Intent intent = new Intent(PlaneReimbursementActivity.this, AddressManagementActivity.class);
                intent.putExtra(AddressManagementActivity.IS_SELECT, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.identification_num_explain:
                String content = "纳税人识别号是企业税务登记证上唯一识别企业的税号，由15/18或20位数码组成。根据国家税务局规定，自2017年7月1日起，开具增值税普通发票必须有纳税人识别号或统一社会信用代码，否则该发票为不符合规定的发票，不得作为税收凭证。纳税人识别号可登录纳税人信息查询网www.yibannashuiren.com 查询，或向公司财务人员咨询。";

                new XPopup.Builder(PlaneReimbursementActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;

            case R.id.reimbursementType:
                String[] strings2 = new String[]{"行程单", "全额发票", "行程单和差额发票"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(PlaneReimbursementActivity.this)
                                .setData(Arrays.asList(strings2))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvReimbursement.setText(strings2[position]);
                                        reimbursementPosition = position;
                                    }
                                }))
                        .show();
                break;
            case R.id.submit:
                onSubmit();
                break;
        }
    }

    public void onSubmit() {
        if (TextUtils.isEmpty(tvReimbursement.getText().toString())) {
            ToastUtils.showShortToast("请选择凭证类型");
            return;
        }
        if (TextUtils.isEmpty(tvReimbursement.getText().toString())) {
            ToastUtils.showShortToast("请选择发票类型");
            return;
        }
        if (TextUtils.isEmpty(editInvoiceTitle.getText().toString())) {
            ToastUtils.showShortToast("请填写发票抬头");
            return;
        }
        if (TextUtils.isEmpty(editNum.getText().toString())) {
            ToastUtils.showShortToast("请填写纳税人识别号");
            return;
        }
        if (addressBean == null) {
            ToastUtils.showShortToast("请选择收货地址");
            return;
        }

        if (reimbursementPosition == 0) {
            itinerarySearch();
        } else if (reimbursementPosition == 1) {
            itinerarySearch();
        } else {
            refundXcd();
        }
    }

    public void itinerarySearch() {
        OkGo.<ItineraryInfo>get(Urls.ITINERARY_SEARCH)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("orderNo", docOrderDetailInfo.detail.orderNo)
                .execute(new JsonCallback<ItineraryInfo>() {
                    @Override
                    public void onSuccess(Response<ItineraryInfo> response) {
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().message, response.body().code);
                        itineraryAskFor();
                    }

                    @Override
                    public void onError(Response<ItineraryInfo> response) {
                        super.onError(response);
                    }
                });
    }

    public void itineraryAskFor() {
        OkGo.<PlaneResponse<AskForResultInfo>>post(Urls.ITINERARY_ASKFOR)
                .tag(this)
                .params("orderNo", "")
                .params("receiverName", "")
                .params("receiverPhone", "")
                .params("receiverProvince", "")
                .params("receiverCity", "")
                .params("receiverDistrict", "")
                .params("receiverStreet", "")
                .params("reimburseType", "")
                .params("receiptTitle", "")
                .params("receiptTitleTypeCode", "")
                .params("taxpayerId", "")
                .execute(new JsonCallback<PlaneResponse<AskForResultInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<AskForResultInfo>> response) {
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().message, response.body().code);
                    }

                    @Override
                    public void onError(Response<PlaneResponse<AskForResultInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    public void refundXcd() {
        OkGo.<RefundServiceInfo>get(Urls.REFUND_XCD_SEARCH)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("orderNo", docOrderDetailInfo.detail.orderNo)
                .execute(new JsonCallback<RefundServiceInfo>() {
                    @Override
                    public void onSuccess(Response<RefundServiceInfo> response) {
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().message, response.body().code);
                        refundAskFor();
                    }

                    @Override
                    public void onError(Response<RefundServiceInfo> response) {
                        super.onError(response);
                    }
                });
    }

    public void refundAskFor() {
        RefundAskForParams params = new RefundAskForParams();
        String json = new Gson().toJson(params);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        OkGo.<PlaneResponse<AskForResultInfo>>post(Urls.REFUND_ASKFOR)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .upRequestBody(body)
                .execute(new JsonCallback<PlaneResponse<AskForResultInfo>>() {
                    @Override
                    public void onSuccess(Response<PlaneResponse<AskForResultInfo>> response) {
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().message, response.body().code);
                    }

                    @Override
                    public void onError(Response<PlaneResponse<AskForResultInfo>> response) {
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
                .params("type", "3") //3行程单4退票
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(PlaneReimbursementActivity.this, response.body().msg, response.body().code);
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

        PayUtils.aliPay(PlaneReimbursementActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("报销成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                addressBean = (AddressInfo.ShopAddressListBean) data.getSerializableExtra(AddressManagementActivity.ADDRESS_DATA);
                if (addressBean != null) {
                    name.setText(addressBean.getUserName());
                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                    phone.setText(addressBean.getPhone());
                }
            }
        }
    }
}
