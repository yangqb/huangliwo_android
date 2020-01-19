package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.WithdrawModel;
import com.feitianzhu.huangliwo.shop.ShopHelp;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * package name: com.feitianzhu.fu700.me
 * user: yangqinbo
 * date: 2019/12/23
 * time: 10:07
 * email: 694125155@qq.com
 * 余额提现
 */
public class WithdrawActivity extends BaseActivity {
    public static final String BALANCE = "balance";
    public static final String MERCHANT_ID = "merchantId";
    private double balance;
    private int merchantId = -1;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.edit_amount)
    EditText editAmount;
    @BindView(R.id.alipayNo)
    EditText editAlipayNo;
    @BindView(R.id.right_text)
    TextView rightText;

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
        merchantId = getIntent().getIntExtra(MERCHANT_ID, -1);
        EditTextUtils.afterDotTwo(editAmount);
    }

    @OnClick({R.id.submit, R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.submit:
                VeriPassword(balance);
                break;
            case R.id.right_button:
                Intent intent = new Intent(WithdrawActivity.this, WithdrawRecordActivity.class);
                intent.putExtra(WithdrawRecordActivity.MERCHANT_ID, merchantId);
                startActivity(intent);
                break;
        }
    }

    private void VeriPassword(double balance) {
        if (!StringUtils.isPhone(editAlipayNo.getText().toString().trim())) {
            if (!StringUtils.isEmail(editAlipayNo.getText().toString().trim())) {
                ToastUtils.showShortToast("请输入正确的支付宝账号");
                return;
            }
        }

        if (TextUtils.isEmpty(editAmount.getText().toString())) {
            ToastUtils.showShortToast("请输入正确金额");
            return;
        }
        if (editAmount.getText().toString().endsWith(".")) {
            ToastUtils.showShortToast("请输入正确金额");
            return;
        }
        double amount = Double.valueOf(editAmount.getText().toString());
        if (amount < 10) {
            ToastUtils.showShortToast("提现金额必须大于10元");
            return;
        }
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
                submit(result.toString());
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    public void submit(String passWord) {
        PostFormBuilder postForm = OkHttpUtils.post()
                .url(Urls.WITHDRAW);
        if (merchantId != -1) {
            postForm.addParams("merchantId", merchantId + "");
        }
        postForm.addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .addParams("account", editAlipayNo.getText().toString().trim())
                .addParams("amount", editAmount.getText().toString().trim())
                .addParams("payPass", passWord)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, WithdrawModel.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        new XPopup.Builder(WithdrawActivity.this)
                                .asConfirm("提示", ((WithdrawModel) response).getMsg(), "关闭", "确定", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                }, null, false)
                                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                .show();
                    }
                });
    }

    @Override
    protected void initData() {
    }
}
