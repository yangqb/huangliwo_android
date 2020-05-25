package com.feitianzhu.huangliwo.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;


public class GetPasswordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.password1)
    EditText mPasswordEditText1;
    @BindView(R.id.sign_in_button)
    Button mSignInButton;
    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R.id.edt_code)
    EditText mEditTextCode;
    @BindView(R.id.tv_code)
    TextView mTextViewCode;
    @BindView(R.id.rl_code)
    RelativeLayout mLayoutCode;

    @BindView(R.id.password2)
    EditText mPasswordEditText2;
    @BindView(R.id.ll_code)
    LinearLayout mCodeLayout;
    @BindView(R.id.tv_old_account)
    TextView mTvCurrentPhone;
    @BindView(R.id.tv_second_tips)
    TextView mTvSecondPwdTips;
    @BindView(R.id.title_name)
    TextView titleName;
    private int mType;
    private String userId;
    private String token;

    /**
     * 找回支付密码密码
     */
    public static final int TYPE_GET_PAY_PASSWORD_PWD = 1;
    /**
     * 设置支付密码
     */
    public static final int TYPE_SET_PAY_PASSWORD_PWD = 2;


    private CountDownTimer mTimer = new CountDownTimer(6000 * 10, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTextViewCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mLayoutCode.setEnabled(true);
            mTextViewCode.setText(R.string.resend);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_pwd;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(GetPasswordActivity.this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(GetPasswordActivity.this, Constant.SP_LOGIN_USERID);
        mLayoutCode.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String phone = SPUtils.getString(this, Constant.SP_PHONE);
        mTvCurrentPhone.setText(String.format(getString(R.string.current_phone), phone));
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(Constant.INTENT_GET_SET_PSW_TYPE, 2);
        }
        KLog.i("mType: %d", mType);
        if (mType == TYPE_GET_PAY_PASSWORD_PWD) { //找回二级密码
            mPasswordEditText1.setHint(R.string.hint_input_second_pwd);
            mTvSecondPwdTips.setVisibility(View.GONE);
            titleName.setText("找回支付密码");
        } else if (mType == TYPE_SET_PAY_PASSWORD_PWD) {//设置二级密码
            mPasswordEditText1.setHint(R.string.hint_input_second_pwd);
            mTvSecondPwdTips.setVisibility(View.VISIBLE);
            titleName.setText("设置支付密码");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_in_button:

                String smsCode = mEditTextCode.getText().toString();
                String newPassword1 = mPasswordEditText1.getText().toString();
                String newPassword2 = mPasswordEditText2.getText().toString();

                if (TextUtils.isEmpty(smsCode)) {
                    ToastUtils.show(getString(R.string.please_input_code));
                    return;
                }
                if (TextUtils.isEmpty(newPassword1)) {
                    ToastUtils.show(getString(R.string.please_input_newpassword));
                    return;
                }
                if (TextUtils.isEmpty(newPassword2)) {
                    ToastUtils.show(getString(R.string.please_input_newpassword2));
                    return;
                }
                if (!newPassword1.equals(newPassword2)) {
                    ToastUtils.show(getString(R.string.please_input_check_password));
                    return;
                }
                if (newPassword1.length() < 6) {
                    Toast.makeText(this, R.string.please_input_six_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String encryptPssword1 = EncryptUtils.encodePassword(newPassword1);
                String encryptPssword2 = EncryptUtils.encodePassword(newPassword2);
                String phone = SPUtils.getString(this, Constant.SP_PHONE, "");
                if (mType == TYPE_GET_PAY_PASSWORD_PWD) { //找回二级密码

                    OkGo.<LzyResponse>post(Urls.GET_UPAYPASS)
                            .tag(this)
                            .params("phone", phone)
                            .params("smsCode", smsCode)
                            .params("newPassword", encryptPssword1)
                            .params("confirmPassword", encryptPssword2)
                            .params("accessToken", token)
                            .params("userId", userId)
                            .execute(new JsonCallback<LzyResponse>() {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onSuccess(GetPasswordActivity.this, response.body().msg, response.body().code);
                                    if (response.body().code == 0) {
                                        if (Constant.mUserAuth != null) {
                                            Constant.mUserAuth.isPaypass = 1;
                                        }
                                        ToastUtils.show(R.string.change_ok);
                                        Intent intent = new Intent(mContext, SettingsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onError(response);
                                }
                            });

                } else if (mType == TYPE_SET_PAY_PASSWORD_PWD) {//设置二级密码

                    OkGo.<LzyResponse>post(Urls.SET_PAYPASS)
                            .tag(this)
                            .params("phone", phone)
                            .params("smsCode", smsCode)
                            .params("paypass", encryptPssword1)
                            .params("confirmPaypass", encryptPssword2)
                            .params("accessToken", token)
                            .params("userId", userId)
                            .execute(new JsonCallback<LzyResponse>() {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onSuccess(GetPasswordActivity.this, response.body().msg, response.body().code);
                                    if (response.body().code == 0) {
                                        if (Constant.mUserAuth != null) {
                                            Constant.mUserAuth.isPaypass = 1;
                                        }
                                        ToastUtils.show("设置成功");
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onError(response);
                                }
                            });
                }

                break;
            case R.id.rl_code:
                String phoneNum = SPUtils.getString(this, Constant.SP_PHONE, "");
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(this, "手机号为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                mLayoutCode.setEnabled(false);
                mTimer.start();
                getValicationCode(phoneNum);
                break;

        }
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    /**
     * 获取验证码
     */
    private void getValicationCode(String phone) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(R.string.please_input_phone);
            return;
        }

        if (mType == TYPE_GET_PAY_PASSWORD_PWD) { //找回二级密码
            getSmsCode(phone, "6");
        } else if (mType == TYPE_SET_PAY_PASSWORD_PWD) {//设置二级密码
            getSmsCode(phone, "5");
        }

    }

    private void getSmsCode(String phone, String type) {
        String token = SPUtils.getString(GetPasswordActivity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", type)
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(GetPasswordActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("验证码已发送至您的手机");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, GetPasswordActivity.class);
        intent.putExtra(Constant.INTENT_GET_SET_PSW_TYPE, type);
        context.startActivity(intent);
    }
}

