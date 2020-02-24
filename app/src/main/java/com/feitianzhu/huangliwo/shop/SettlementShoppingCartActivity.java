package com.feitianzhu.huangliwo.shop;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.AddressManagementActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.ShoppingCartModel;
import com.feitianzhu.huangliwo.model.ShoppingCartPayInfo;
import com.feitianzhu.huangliwo.shop.adapter.SettlementShoppingAdapter;
import com.feitianzhu.huangliwo.shop.ui.MyOrderActivity2;
import com.feitianzhu.huangliwo.shop.ui.OrderDetailActivity;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class SettlementShoppingCartActivity extends BaseActivity {
    public static final String CART_DATA = "cart_data";
    private static final int REQUEST_CODE = 1000;
    private SettlementShoppingAdapter mAdapter;
    private ArrayList<ShoppingCartModel.CartGoodsModel> selectCartModels;
    private AddressInfo.ShopAddressListBean addressBean;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
    private List<ShoppingCartPayInfo> shoppingCartPayInfos = new ArrayList<>();
    private String appId = "";
    private String orderNo = "";//订单号
    private String orderInfo = "";
    private String str1 = "合计：";
    private String str2 = "¥ ";
    private String payChannel = "wx";
    private double p = 0.00;
    private String totalAmount = "0.00";
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.no_address)
    LinearLayout noAddress;
    @BindView(R.id.consignee_name)
    TextView consigneeName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @BindView(R.id.pay_amount)
    TextView payAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement_shoppingcart;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        titleName.setText("确认订单");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        selectCartModels = getIntent().getParcelableArrayListExtra(CART_DATA);
        mAdapter = new SettlementShoppingAdapter(selectCartModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter.notifyDataSetChanged();
        calculationAmount();
        initListener();
    }

    public void initListener() {
        mAdapter.setOnEditListener(new SettlementShoppingAdapter.OnEditListener() {
            @Override
            public void edit(String text, int position) {
                selectCartModels.get(position).remark = text;
            }
        });

        SoftKeyBoardListener.setListener(SettlementShoppingCartActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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

    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<AddressInfo>>post(Urls.GET_ADDRESS)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<AddressInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onSuccess(SettlementShoppingCartActivity.this, response.body().msg, response.body().code);
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
                                        consigneeName.setText(addressBean.getUserName());
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

    @OnClick({R.id.left_button, R.id.rl_address, R.id.no_address, R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.no_address:
            case R.id.rl_address:
                Intent intent = new Intent(SettlementShoppingCartActivity.this, AddressManagementActivity.class);
                intent.putExtra(AddressManagementActivity.IS_SELECT, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tv_pay:
                if (addressBean == null) {
                    ToastUtils.showShortToast("请添加收货地址");
                    return;
                }
                pay2();
                break;
            case R.id.weixinPay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payChannel = "wx";
                break;
            case R.id.alipay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payChannel = "alipay";
                break;
            case R.id.balancePay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                payChannel = "balance";
                break;
        }
    }

    /*
     * 生成订单
     * */
    private void pay2() {
        if (payChannel.equals("wx")) {
            appId = Constant.WX_APP_ID;
        } else {
            appId = "";
        }
        StringBuffer carId = new StringBuffer();
        for (int i = 0; i < selectCartModels.size(); i++) {
            ShoppingCartPayInfo cartPayInfo = new ShoppingCartPayInfo();
            if (selectCartModels.get(i).remark == null) {
                cartPayInfo.remark = "";
            } else {
                cartPayInfo.remark = selectCartModels.get(i).remark;
            }

            cartPayInfo.goodId = selectCartModels.get(i).goodsId;
            cartPayInfo.goodsQty = selectCartModels.get(i).goodsCount;   //数量
            cartPayInfo.valueId = selectCartModels.get(i).speci;
            shoppingCartPayInfos.add(cartPayInfo);

            if (i == selectCartModels.size() - 1) {
                carId.append(selectCartModels.get(i).carId);
            } else {
                carId.append(selectCartModels.get(i).carId + ",");
            }
        }
        String json = new Gson().toJson(shoppingCartPayInfos);
        OkGo.<LzyResponse<PayModel>>post(Urls.PAY_SHOPPING_CART)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params("appId", appId)  //这个是微信才需要的，
                .params("channel", payChannel)
                .params("addressId", addressBean.getAddressId())
                .params("carId", carId.toString())
                //.params("orderNo")
                .params("goodOrders", json)
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(SettlementShoppingCartActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            if ("wx".equals(payChannel)) {
                                orderNo = response.body().data.orderNo;
                                wexinPay(response.body().data);
                            } else if ("alipay".equals(payChannel)) {
                                orderInfo = response.body().data.payParam;
                                orderNo = response.body().data.orderNo;
                                aliPay(orderInfo, orderNo);
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onError(response);
                    }
                });
    }

    private void wexinPay(PayModel result) {
        Constant.PayFlag = PayInfo.SHOPPING_CART_PAY;
        IWXAPI api = WXAPIFactory.createWXAPI(SettlementShoppingCartActivity.this, result.appid);
        api.registerApp(Constant.WX_APP_ID);
        PayReq mPayReq = new PayReq();
        mPayReq.appId = Constant.WX_APP_ID;
        mPayReq.partnerId = result.partnerid;
        mPayReq.prepayId = result.prepayid;
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = result.noncestr;
        mPayReq.timeStamp = result.timestamp + "";
        mPayReq.sign = result.sign;
        api.sendReq(mPayReq);
        ToastUtils.showShortToast("正在打开微信中");
    }


    private void aliPay(String payProof, String orderNo) {

        PayUtils.aliPay(SettlementShoppingCartActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
                Intent intent = new Intent(SettlementShoppingCartActivity.this, MyOrderActivity2.class);
                startActivity(intent);
                finish();
                //跳到订单详情
                /*Intent intent = new Intent(ShopPayActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();*/
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.SHOPPING_CART_PAY) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.showShortToast("支付成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.showShortToast("支付失败");
                Intent intent = new Intent(SettlementShoppingCartActivity.this, MyOrderActivity2.class);
                startActivity(intent);
                finish();
                //跳到订单详情
                /*Intent intent = new Intent(SettlementShoppingCartActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();*/
            }
        }
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
                    consigneeName.setText(addressBean.getUserName());
                    phone.setText(addressBean.getPhone());
                }
            }
        }
    }

    public void calculationAmount() {
        p = 0.00;
        for (ShoppingCartModel.CartGoodsModel shoppingCartModel : selectCartModels
        ) {
            p += (shoppingCartModel.goodsCount * shoppingCartModel.price + shoppingCartModel.postage);

            totalAmount = String.format(Locale.getDefault(), "%.2f", p);
        }
        setSpannableString(totalAmount);
    }

    @SuppressLint("SetTextI18n")
    public void setSpannableString(String str3) {
        payAmount.setText("");
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

        payAmount.append(span1);
        payAmount.append(span2);
        payAmount.append(span3);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
