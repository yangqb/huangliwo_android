package com.feitianzhu.huangliwo.settings;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhone2Activity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.edt_code)
    EditText mEdtCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.rl_code)
    RelativeLayout mRlCode;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.title_name)
    TextView titleName;
    private String mOldPhone;
    private String mOldSmsCode;


    private CountDownTimer mTimer = new CountDownTimer(6000 * 10, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTvCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mRlCode.setEnabled(true);
            mTvCode.setText(R.string.resend);
        }
    };

    @Override
    protected void initTitle() {
        titleName.setText("更换手机号码");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone2;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            mOldPhone = intent.getStringExtra(Constant.INTENT_OLD_PHONE);
            mOldSmsCode = intent.getStringExtra(Constant.INTENT_OLD_SMSCODE);
        }

    }

    @OnClick({R.id.button, R.id.rl_code, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:

                final String newPhone = mEtPhone.getText().toString();
                String newSmsCode = mEdtCode.getText().toString();

                if (TextUtils.isEmpty(newPhone)) {
                    ToastUtils.show(getString(R.string.please_input_phone));
                    return;
                }

                if (TextUtils.isEmpty(newSmsCode)) {
                    ToastUtils.show(getString(R.string.please_input_code));
                    return;
                }

                String token = SPUtils.getString(ChangePhone2Activity.this, Constant.SP_ACCESS_TOKEN);
                String userId = SPUtils.getString(ChangePhone2Activity.this, Constant.SP_LOGIN_USERID);

                OkGo.<LzyResponse>post(Urls.UPDATE_PHONE)
                        .tag(this)
                        .params("oldPhone", mOldPhone)
                        .params("oldSmsCode", mOldSmsCode)
                        .params("newPhone", newPhone)
                        .params("newSmsCode", newSmsCode)
                        .params("accessToken", token)
                        .params("userId", userId)
                        .execute(new JsonCallback<LzyResponse>() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                super.onSuccess(ChangePhone2Activity.this, response.body().msg, response.body().code);
                                if (response.body().code == 0) {
                                    ToastUtils.show("修改成功");
                                    Constant.PHONE = newPhone;
                                    SPUtils.putString(ChangePhone2Activity.this, Constant.SP_PHONE, newPhone);
                                    KLog.i("new Constant.PHONE : " + Constant.PHONE);
                                    Intent intent = new Intent(ChangePhone2Activity.this, MainActivity.class);
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
                String phone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(getString(R.string.please_input_phone));
                    return;
                }

                mRlCode.setEnabled(false);
                mTimer.start();

                getSmsCode(phone);
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getSmsCode(String phone) {

        String token = SPUtils.getString(ChangePhone2Activity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "3")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(ChangePhone2Activity.this, response.body().msg, response.body().code);
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


    public static void startActivity(Context context, String oldPhone, String oldSmsCode) {
        Intent intent = new Intent(context, ChangePhone2Activity.class);
        intent.putExtra(Constant.INTENT_OLD_PHONE, oldPhone);
        intent.putExtra(Constant.INTENT_OLD_SMSCODE, oldSmsCode);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
