package com.feitianzhu.fu700.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelp;
import com.feitianzhu.fu700.utils.EditTextUtils;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.StringUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
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
    private double balance;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.edit_amount)
    EditText editAmount;
    @BindView(R.id.alipayNo)
    EditText editAlipayNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw2;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("提现");
        balance = getIntent().getDoubleExtra(BALANCE, 0.00);
        EditTextUtils.afterDotTwo(editAmount);
    }

    @OnClick({R.id.submit, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.submit:
                VeriPassword(balance);
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
        OkHttpUtils.post()
                .url(Urls.WITHDRAW)
                .addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .addParams("account", editAlipayNo.getText().toString().trim())
                .addParams("amount", editAmount.getText().toString().trim())
                .addParams("payPass", passWord)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast(response.toString());
                        finish();
                    }
                });
    }

    @Override
    protected void initData() {
    }
}
