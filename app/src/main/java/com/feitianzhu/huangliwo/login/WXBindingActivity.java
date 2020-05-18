package com.feitianzhu.huangliwo.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.WXLoginInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;

/**
 * package name: com.feitianzhu.huangliwo.login
 * user: yangqinbo
 * date: 2020/3/6
 * time: 16:38
 * email: 694125155@qq.com
 */
public class WXBindingActivity extends BaseActivity {
    public static final String WX_DATA = "wx_data";
    private WXLoginInfo loginInfo;
    private String token;
    private String userId;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.rl_code)
    RelativeLayout mLayoutCode;
    @BindView(R.id.tv_code)
    TextView mTextViewCode;

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
    protected int getLayoutId() {
        return R.layout.activity_wx_binding;
    }

    @Override
    protected void initView() {
        loginInfo = (WXLoginInfo) getIntent().getSerializableExtra(WX_DATA);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.back, R.id.submit, R.id.rl_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_code:
                if (!StringUtils.isPhone(account.getText().toString().trim())) {
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegexUtils.isChinaPhoneLegal(phone)) {
//                    Toast.makeText(this, R.string.please_input_correct_phone, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mLayoutCode.setEnabled(false);
                mTimer.start();
                getValicationCode(account.getText().toString().trim());
                break;
            case R.id.submit:
                String phone = account.getText().toString().trim();
                String code = edtCode.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, R.string.please_input_code, Toast.LENGTH_SHORT).show();
                    return;
                }

                binding(phone, code);

                break;
        }
    }

    public void binding(String phone, String code) {
        OkGo.<LzyResponse<WXLoginInfo>>get(Urls.BINDING_PHONE)
                .tag(this)
                .params("accessToken", loginInfo.accessToken)
                .params("userId", loginInfo.userId)
                .params("phone", phone)
                .params("smsCode", code)
                .execute(new JsonCallback<LzyResponse<WXLoginInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<WXLoginInfo>> response) {
                        super.onSuccess(WXBindingActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            Constant.PHONE = phone;
                            SPUtils.putString(WXBindingActivity.this, Constant.SP_PHONE, phone);
                            Constant.ACCESS_TOKEN = response.body().data.accessToken;
                            Constant.LOGIN_USERID = response.body().data.userId;
                            SPUtils.putString(WXBindingActivity.this, Constant.SP_LOGIN_USERID, response.body().data.userId);
                            SPUtils.putString(WXBindingActivity.this, Constant.SP_ACCESS_TOKEN, response.body().data.accessToken);
                            getUserInfo(response.body().data.accessToken, response.body().data.userId);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<WXLoginInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    public void getUserInfo(String token, String userId) {
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<MineInfoModel>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MineInfoModel>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            UserInfoUtils.saveUserInfo(WXBindingActivity.this, response.body().data);
                            Intent intent = new Intent(WXBindingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getValicationCode(String phone) {
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "7")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(WXBindingActivity.this, response.body().msg, response.body().code);
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
