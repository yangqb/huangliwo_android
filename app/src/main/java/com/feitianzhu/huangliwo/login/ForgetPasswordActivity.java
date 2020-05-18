package com.feitianzhu.huangliwo.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;


public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edt_phone)
    EditText mEtPhone;
    @BindView(R.id.password1)
    EditText mPasswordEditText1;
    @BindView(R.id.sign_in_button)
    TextView mSignInButton;
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
    @BindView(R.id.back)
    RelativeLayout back;


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
        return R.layout.activity_forget_pwd2;
    }

    @Override
    protected void initView() {
        mLayoutCode.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        back.setOnClickListener(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    protected void initData() {


    }


    @Override
    public void onClick(View v) {

        String phone = mEtPhone.getText().toString().trim();
        String smsCode = mEditTextCode.getText().toString().trim();
        String password1 = mPasswordEditText1.getText().toString().trim();
        String password2 = mPasswordEditText2.getText().toString().trim();

        switch (v.getId()) {

            case R.id.sign_in_button:

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(smsCode)) {
                    Toast.makeText(this, R.string.please_input_code, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(this, R.string.please_input_newpassword, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    Toast.makeText(this, R.string.please_input_newpassword2, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password1.equals(password2)) {
                    ToastUtils.show(getString(R.string.please_input_check_password));
                    return;
                }

                OkGo.<LzyResponse>post(Urls.GET_MYPASSWORD)
                        .tag(this)
                        .params("phone", phone)
                        .params("smsCode", smsCode)
                        .params("newPassword", EncryptUtils.encodePassword(password1))
                        .params("confirmPassword", EncryptUtils.encodePassword(password2))
                        .execute(new JsonCallback<LzyResponse>() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                if (response.body().code == 0) {
                                    ToastUtils.show(R.string.change_ok);
                                    SPUtils.putString(ForgetPasswordActivity.this, Constant.SP_PHONE, phone);
                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                                super.onError(response);
                            }
                        });
                break;
            case R.id.rl_code:

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegexUtils.isChinaPhoneLegal(phone)) {
//                    Toast.makeText(this, R.string.please_input_correct_phone, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mLayoutCode.setEnabled(false);
                mTimer.start();
                getValicationCode(phone);
                break;

            case R.id.back:
                finish();
                break;

        }
    }

    /**
     * 获取验证码
     */
    private void getValicationCode(String phone) {
        String token = SPUtils.getString(ForgetPasswordActivity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "4")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(ForgetPasswordActivity.this, response.body().msg, response.body().code);
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
}

