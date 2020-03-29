package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BindingAliAccountModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.WithdrawModel;
import com.feitianzhu.huangliwo.pushshop.MySelfMerchantsActivity;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ShopHelp;
import com.feitianzhu.huangliwo.shop.ui.EditApplyRefundActivity;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.me
 * user: yangqinbo
 * date: 2019/12/23
 * time: 10:07
 * email: 694125155@qq.com
 * 余额提现
 */
public class WithdrawActivity extends BaseActivity {
    public static final int REQUEST_CODE = 111;
    public static final String BALANCE = "balance";
    public static final String MERCHANT_ID = "merchantId";
    private String channel = "alipay";
    private String alipayNo;
    private MineInfoModel infoModel;
    private double balance;
    private int merchantId = -1;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.edit_amount)
    EditText editAmount;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw2;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("提现");
        rightText.setText("明细");
        rightText.setVisibility(View.VISIBLE);
        balance = getIntent().getDoubleExtra(BALANCE, 0.00);
        tvBalance.setText("可提现余额" + MathUtils.subZero(String.valueOf(balance)) + "元");
        merchantId = getIntent().getIntExtra(MERCHANT_ID, -1);
        EditTextUtils.afterDotTwo(editAmount);
    }

    @OnClick({R.id.submit, R.id.left_button, R.id.right_button, R.id.rl_pay, R.id.allWithdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.submit:
                VeriPassword(balance);
                break;
            case R.id.allWithdraw:
                editAmount.setText(MathUtils.subZero(String.valueOf(balance)));
                break;
            case R.id.right_button:
                Intent intent = new Intent(WithdrawActivity.this, WithdrawRecordActivity.class);
                intent.putExtra(WithdrawRecordActivity.MERCHANT_ID, merchantId);
                startActivity(intent);
                break;
            case R.id.rl_pay:
                String[] strings = new String[]{"微信", "支付宝"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(WithdrawActivity.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvPayType.setText(strings[position]);
                                        if (position == 0) {
                                            channel = "wx";
                                            imageView.setBackgroundResource(R.mipmap.icon_weixinzhifu);
                                        } else {
                                            imageView.setBackgroundResource(R.mipmap.icon_zhifubaozhifu);
                                            channel = "alipay";
                                        }
                                    }
                                }))
                        .show();
                break;
        }
    }

    private void VeriPassword(double balance) {
        if (TextUtils.isEmpty(editAmount.getText().toString())) {
            ToastUtils.showShortToast("请输入正确金额");
            return;
        }
        if (editAmount.getText().toString().endsWith(".")) {
            ToastUtils.showShortToast("请输入正确金额");
            return;
        }
        double amount = Double.valueOf(editAmount.getText().toString());
       /* if (amount < 10) {
            ToastUtils.showShortToast("提现金额必须大于10元");
            return;
        }*/
        if (amount > balance) {
            ToastUtils.showShortToast("当前余额不足");
            return;
        }


        ShopHelp.veriPassword(this, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                /*
                 * 提现
                 * */
                if (infoModel != null && infoModel.getIsBind() == 1) {
                    submit(result.toString());
                } else {
                    new XPopup.Builder(WithdrawActivity.this)
                            .asConfirm("提示", "您未绑定支付宝账号是否现在去绑定？", "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Intent intent = new Intent(WithdrawActivity.this, BindingAccountActivity.class);
                                    intent.putExtra(BindingAccountActivity.MINE_INFO, infoModel);
                                    startActivityForResult(intent, REQUEST_CODE);
                                }
                            }, null, false)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    public void submit(String passWord) {
        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.WITHDRAW)
                .tag(this);
        if (merchantId != -1) {
            postRequest.params("merchantId", merchantId + "");
            postRequest.params("type", 2);
        } else {
            postRequest.params("type", 1);
        }
        postRequest.params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("channel", channel)
                .params("amount", editAmount.getText().toString().trim())
                .params("payPass", passWord)
                .params("device", "android")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(WithdrawActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            new XPopup.Builder(WithdrawActivity.this)
                                    .asConfirm("提示", response.body().msg, "关闭", "确定", new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }, null, false)
                                    .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                    .show();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

            /*PostRequest<LzyResponse<WithdrawModel>> postRequest = OkGo.<LzyResponse<WithdrawModel>>post(Urls.WITHDRAWAL)
                    .tag(this);
            if (merchantId != -1) {
                postRequest.params("merchantId", merchantId + "");
            }
            postRequest.params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("account", alipayNo)
                    .params("amount", editAmount.getText().toString().trim())
                    .params("payPass", passWord)
                    .execute(new JsonCallback<LzyResponse<WithdrawModel>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<WithdrawModel>> response) {
                            super.onSuccess(WithdrawActivity.this, response.body().msg, response.body().code);
                            if (response.body().data != null && response.body().code == 0) {
                                new XPopup.Builder(WithdrawActivity.this)
                                        .asConfirm("提示", response.body().data.getMsg(), "关闭", "确定", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                finish();
                                            }
                                        }, null, false)
                                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                        .show();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<WithdrawModel>> response) {
                            super.onError(response);
                        }
                    });*/
    }

    @Override
    protected void initData() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);

        OkGo.<LzyResponse<MineInfoModel>>get(Common_HEADER + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<MineInfoModel>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(WithdrawActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().data != null && response.body().code == 0) {
                            infoModel = response.body().data;
                            if (response.body().data.getIsBind() == 1) {
                                getAccount();
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });

    }

    public void getAccount() {

        OkGo.<LzyResponse<BindingAliAccountModel>>get(Urls.GET_ALI_ACCOUNT)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<BindingAliAccountModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<BindingAliAccountModel>> response) {
                        super.onSuccess(WithdrawActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            alipayNo = response.body().data.getBankCardNo();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<BindingAliAccountModel>> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                initData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
