package com.feitianzhu.huangliwo.vip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.AddressManagementActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.helper.CityModel;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CustomInputView;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

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
    public static final String PARENT_ID = "parent_id";
    public static final String PRESENT_ID = "present_id";
    public String presentId;
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
    private String token;
    private String userId;
    private int parentId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_upgrade;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl(this);
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        parentId = getIntent().getIntExtra(PARENT_ID, 0);
        presentId = getIntent().getStringExtra(PRESENT_ID);
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
    @OnClick({R.id.left_button, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon, R.id.tv_pay, R.id.no_address, R.id.rl_address})
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
                if (addressBean == null) {
                    ToastUtils.show("请选择收货地址");
                } else {
                    showInputDialog();
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

    /*
     * 输入邀请人id
     * */
    public void showInputDialog() {
        String titleText;
        if (parentId == 0) {
            titleText = "请输入邀请人ID";
        } else {
            titleText = "请确认邀请人ID";
        }
        new XPopup.Builder(VipUpgradeActivity.this)
                .asCustom(new CustomInputView(VipUpgradeActivity.this)
                        .setTitle(titleText)
                        .setEditHintText("请输入")
                        .setText(parentId)
                        .setOnConfirmClickListener(new CustomInputView.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(String account) {
                                if (TextUtils.isEmpty(account)) {
                                    ToastUtils.show("请输入邀请人ID");
                                    return;
                                }
                                pay(account);
                            }
                        }))
                .show();
    }

    public void pay(String account) {
        if (payType.equals("wx")) {
            appId = Constant.WX_APP_ID;
        } else {
            appId = "";
        }

        OkGo.<LzyResponse<PayModel>>post(Common_HEADER + Constant.POST_UNION_LEVEL_PAY)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("appId", appId)//这个是微信才需要的，
                .params("payChannel", payType)
                .params("parentId", account)
                .params("giftId", presentId)
                .params("addressId", addressBean.getAddressId() + "")
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(VipUpgradeActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null && response.body().code == 0) {
                            if ("alipay".equals(payType)) {
                                String orderInfo = response.body().data.payParam;
                                aliPay(orderInfo);

                            } else if ("wx".equals(payType)) {
                                Constant.PayFlag = PayInfo.UNIONLEVEL;
                                CallWxPay(response.body().data);
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onError(response);
                    }
                });

    }

    public void aliPay(String orderInfo) {
        PayUtils.aliPay(VipUpgradeActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                setResult(RESULT_OK);
                ToastUtils.show("支付成功");
                EventBus.getDefault().postSticky(LoginEvent.BUY_VIP);
                //弹框
                showDialog();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show("支付失败");
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        switch (msg.getCurrentInfo()) {
            case PayInfo.UNIONLEVEL:
                if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                    setResult(RESULT_OK);
                    EventBus.getDefault().postSticky(LoginEvent.BUY_VIP);
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

    private void CallWxPay(PayModel mResult) {
        IWXAPI api = WXAPIFactory.createWXAPI(VipUpgradeActivity.this, mResult.appid);
        api.registerApp(mResult.appid);
        PayReq mPayReq = new PayReq();
        mPayReq.appId = Constant.WX_APP_ID;
        mPayReq.partnerId = mResult.partnerid;
        mPayReq.prepayId = mResult.prepayid;
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = mResult.noncestr;
        mPayReq.timeStamp = mResult.timestamp + "";
        mPayReq.sign = mResult.sign;
        api.sendReq(mPayReq);
        ToastUtils.show("正在打开微信中");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
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
                        super.onSuccess(VipUpgradeActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            AddressInfo addressInfo = response.body().data;
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
                        } else {
                            noAddress.setVisibility(View.VISIBLE);
                            rlAddress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onError(response);
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
