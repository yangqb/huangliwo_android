package com.feitianzhu.huangliwo.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.dao.NetworkDao;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.entity.LoginEntity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.socks.library.KLog;

import butterknife.BindView;

import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.FailCode;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.account)
    EditText mAccountLayout;
    @BindView(R.id.password1)
    EditText mPasswordEditText1;
    @BindView(R.id.sign_in_button)
    TextView mSignInButton;

    @BindView(R.id.tv_forget_password)
    TextView mForgetLayout;
    @BindView(R.id.tv_regist)
    TextView mRegister;

    private String mAccount;
    private String mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();

        mSignInButton.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgetLayout.setOnClickListener(this);

        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForgetLayout.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void initData() {
        mAccount = SPUtils.getString(this, Constant.SP_PHONE, "");
        showLoginLayout();
    }

    private void showLoginLayout() {
        mAccountLayout.setText("");
        mPasswordEditText1.setText("");
        mForgetLayout.setVisibility(View.VISIBLE);
        mRegister.setVisibility(View.VISIBLE);
        mRegister.setText(R.string.no_account);
        mSignInButton.setText(R.string.sign_in);
        mAccountLayout.setText(mAccount);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                login();
                break;
            case R.id.tv_regist:
                RegisterActivity.startActivity(this, true);
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }


    private void login() {

        mAccount = stringTrim(mAccountLayout);
        mPassword = stringTrim(mPasswordEditText1);


        if (TextUtils.isEmpty(mAccount)) {
            Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isPhone(mAccount)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, R.string.please_input_password, Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Ps = EncryptUtils.encodePassword(mPassword);

        OkGo.<LzyResponse<LoginEntity>>post(Urls.LOGIN)
                .tag(this)
                .params("phone", mAccount)
                .params("password", base64Ps)
                .execute(new JsonCallback<LzyResponse<LoginEntity>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<LoginEntity>> response) {
                        super.onSuccess(LoginActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {

                            KLog.i("response:%s", response.toString());

                            LoginEntity loginEntity = response.body().data;
                            Constant.ACCESS_TOKEN = loginEntity.accessToken;
                            Constant.LOGIN_USERID = loginEntity.userId;
                            Constant.PHONE = mAccount;

                            SPUtils.putString(LoginActivity.this, Constant.SP_PHONE, mAccount);
                            SPUtils.putString(LoginActivity.this, Constant.SP_PASSWORD, base64Ps);
                            SPUtils.putString(LoginActivity.this, Constant.SP_LOGIN_USERID, loginEntity.userId);
                            SPUtils.putString(LoginActivity.this, Constant.SP_ACCESS_TOKEN, loginEntity.accessToken);

                            getUserInfo(loginEntity.userId, loginEntity.accessToken);

                        } else {
                            Constant.ACCESS_TOKEN = "";
                            Constant.LOGIN_USERID = "";
                            Constant.PHONE = "";

                            SPUtils.putString(LoginActivity.this, Constant.SP_PHONE, "");
                            SPUtils.putString(LoginActivity.this, Constant.SP_PASSWORD, "");
                            SPUtils.putString(LoginActivity.this, Constant.SP_LOGIN_USERID, "");
                            SPUtils.putString(LoginActivity.this, Constant.SP_ACCESS_TOKEN, "");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<LoginEntity>> response) {
                        super.onError(response);
                        Constant.ACCESS_TOKEN = "";
                        Constant.LOGIN_USERID = "";
                        Constant.PHONE = "";

                        SPUtils.putString(LoginActivity.this, Constant.SP_PHONE, "");
                        SPUtils.putString(LoginActivity.this, Constant.SP_PASSWORD, "");
                        SPUtils.putString(LoginActivity.this, Constant.SP_LOGIN_USERID, "");
                        SPUtils.putString(LoginActivity.this, Constant.SP_ACCESS_TOKEN, "");
                    }
                });
    }

    public void getUserInfo(String userId, String token) {
        OkGo.<LzyResponse<MineInfoModel>>get(Common_HEADER + POST_MINE_INFO)
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
                        if (response.body().data != null) {
                            UserInfoUtils.saveUserInfo(LoginActivity.this, response.body().data);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }

    private String stringTrim(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

