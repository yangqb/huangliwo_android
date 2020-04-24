package com.feitianzhu.huangliwo.settings;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhone1Activity extends BaseActivity {

    @BindView(R.id.tv_current_phone)
    TextView mTvCurrentPhone;
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
    private String phone;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initTitle() {
        titleName.setText("更换手机号码");
        phone = SPUtils.getString(this, Constant.SP_PHONE, "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone1;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mTvCurrentPhone.setText(String.format(getString(R.string.current_phone), phone));
    }

    @OnClick({R.id.button, R.id.rl_code, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(mEdtCode.getText().toString().trim())) {
                    ToastUtils.show("验证码不能为空");
                    return;
                }

                ChangePhone2Activity.startActivity(this, phone, mEdtCode.getText().toString().trim());
                finish();
                break;
            case R.id.rl_code:

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

        String token = SPUtils.getString(ChangePhone1Activity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "2")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(ChangePhone1Activity.this, response.body().msg, response.body().code);
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
