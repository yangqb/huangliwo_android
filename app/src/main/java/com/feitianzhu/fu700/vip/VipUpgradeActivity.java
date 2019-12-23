package com.feitianzhu.fu700.vip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.AddressManagementActivity;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.helper.CityModel;
import com.feitianzhu.fu700.me.ui.ShopRecordDetailActivity;
import com.feitianzhu.fu700.model.AddressInfo;
import com.feitianzhu.fu700.model.PayInfo;
import com.feitianzhu.fu700.model.ShopRecordWxModel;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.payforme.PayForMeRecordActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.utils.PayUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.utils.doubleclick.SingleClick;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.FailCode;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/23 0023 上午 11:44
 */
public class VipUpgradeActivity extends BaseActivity {
    private String appId = "";
    private static final int REQUEST_CODE = 1000;
    private AddressInfo.ShopAddressListBean addressBean;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
    @BindView(R.id.amount)
    TextView bottomAmount;
    @BindView(R.id.tv_amount)
    TextView topAmount;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.no_address)
    LinearLayout noAddress;
    private String payType = "wx"; //支付方式
    private CityModel mCityModel;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_upgrade;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl();
    }

    @Override
    protected void initView() {
        titleName.setText("确认升级");
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        /**
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
         */
        bottomAmount.setText("");
        topAmount.setText("");
        String str1 = "合计：";
        String str2 = "¥ ";
        String str3 = "399.00";
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        bottomAmount.append(span1);
        bottomAmount.append(span2);
        bottomAmount.append(span3);

        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        topAmount.append(span4);
        topAmount.append(span5);

        //showDialog();

    }

    @SingleClick
    @OnClick({R.id.left_button, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon, R.id.tv_pay, R.id.rl_address, R.id.no_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.weixinPay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payType = "wx";
                break;
            case R.id.alipay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payType = "alipay";
                break;
            case R.id.balancePay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                payType = "";
                break;
            case R.id.tv_pay:
                if (TextUtils.isEmpty(tvAddress.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择收货地址");
                } else {
                    pay();
                }
                break;
            case R.id.no_address:
            case R.id.rl_address:
                Intent intent = new Intent(this, AddressManagementActivity.class);
                intent.putExtra(AddressManagementActivity.IS_SELECT, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    public void pay() {
        if (payType.equals("wx")) {
            appId = Constant.WX_APP_ID;
        } else {
            appId = "";
        }
        OkHttpUtils.post().url(Common_HEADER + Constant.POST_UNION_LEVEL_PAY)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("addressId", addressBean.getAddressId() + "")
                .addParams("appId", appId)//这个是微信才需要的，
                .addParams("payChannel", payType)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                        ToastUtils.showShortToast("提交订单失败");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if ("alipay".equals(payType)) {
                            try {
                                JSONObject object = new JSONObject(response.toString());
                                String orderInfo = object.getString("payParam");
                                aliPay(orderInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if ("wx".equals(payType)) {
                            Constant.PayFlag = PayInfo.UNIONLEVEL;
                            CallWxPay(response.toString());
                        }
                    }
                });

    }

    public void aliPay(String orderInfo) {
        PayUtils.aliPay(VipUpgradeActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                setResult(RESULT_OK);
                ToastUtils.showShortToast("支付成功");
                EventBus.getDefault().post(LoginEvent.EDITOR_INFO);
                //弹框
                showDialog();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        switch (msg.getCurrentInfo()) {
            case PayInfo.UNIONLEVEL:
                if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                    setResult(RESULT_OK);
                    EventBus.getDefault().post(LoginEvent.EDITOR_INFO);
                    showDialog();
                }
                break;
        }
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .enableDrag(false)
                .asCustom(new CustomPopup(this).onClose(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }))
                .show();
    }

    private void CallWxPay(String result) {
        Log.e("Test", "--------result----->" + result);
        Gson gson = new Gson();
        ShopRecordWxModel mResult = gson.fromJson(result, ShopRecordWxModel.class);
        IWXAPI api = WXAPIFactory.createWXAPI(VipUpgradeActivity.this, mResult.getAppid());
        api.registerApp(mResult.getAppid());
        PayReq mPayReq = new PayReq();
        mPayReq.appId = Constant.WX_APP_ID;
        mPayReq.partnerId = mResult.getPartnerid();
        mPayReq.prepayId = mResult.getPrepayid();
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = mResult.getNoncestr();
        mPayReq.timeStamp = mResult.getTimestamp() + "";
        mPayReq.sign = mResult.getSign();
        api.sendReq(mPayReq);
        ToastUtils.showShortToast("正在打开微信中");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        /*
         * 获取默认收货地址
         * */
        OkHttpUtils.post()
                .url(Urls.GET_ADDRESS)
                .addParams("accessToken", Constant.ACCESS_TOKEN)
                .addParams("userId", Constant.LOGIN_USERID)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, AddressInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        AddressInfo addressInfo = (AddressInfo) response;
                        addressInfos = addressInfo.getShopAddressList();
                        if (addressInfos.size() > 0) {
                            for (AddressInfo.ShopAddressListBean address : addressInfos
                            ) {
                                if (address.getIsDefalt() == 1) {
                                    noAddress.setVisibility(View.GONE);
                                    rlAddress.setVisibility(View.VISIBLE);
                                    addressBean = address;
                                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                                    name.setText(addressBean.getUserName());
                                    phone.setText(addressBean.getPhone());
                                    break;
                                } else {
                                    noAddress.setVisibility(View.VISIBLE);
                                    rlAddress.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            noAddress.setVisibility(View.VISIBLE);
                            rlAddress.setVisibility(View.GONE);
                        }

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
                    noAddress.setVisibility(View.GONE);
                    rlAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                    name.setText(addressBean.getUserName());
                    phone.setText(addressBean.getPhone());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
