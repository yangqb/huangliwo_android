package com.feitianzhu.huangliwo.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.FailCode;


public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.old_password)
    EditText mOldPassword;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.password1)
    EditText mPassword1;
    @BindView(R.id.password2)
    EditText mPassword2;
    @BindView(R.id.sign_in_button)
    Button mSignInButton;
    @BindView(R.id.title_name)
    TextView titleName;
    private boolean mIsChangeLoginPwd;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        token = SPUtils.getString(ChangePasswordActivity.this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(ChangePasswordActivity.this, Constant.SP_LOGIN_USERID);
        if (intent != null) {
            mIsChangeLoginPwd = intent.getBooleanExtra(Constant.INTENT_IS_CHANGE_LOGIN_PWD, true);
        }
        if (mIsChangeLoginPwd) {
            titleName.setText("修改登录密码");
        } else {
            titleName.setText("修改支付密码");
        }

    }

    @Override
    protected void initData() {
        if (mIsChangeLoginPwd) { //修改登录密码
            mPassword1.setHint(R.string.hint_input_login_pwd);
        } else { //修改二级密码
            mPassword1.setHint(R.string.hint_input_second_pwd);
        }

    }


    @OnClick({R.id.sign_in_button, R.id.tv_forget_password, R.id.left_button})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_in_button:

                String oldPassWord = mOldPassword.getText().toString();
                String newPassword1 = mPassword1.getText().toString();
                String newPassword2 = mPassword2.getText().toString();

                if (TextUtils.isEmpty(oldPassWord)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_oldpassword));
                    return;
                }
                if (TextUtils.isEmpty(newPassword1)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_newpassword));
                    return;
                }
                if (TextUtils.isEmpty(newPassword2)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_newpassword2));
                    return;
                }
                if (!newPassword1.equals(newPassword2)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_check_password));
                    return;
                }
                if (newPassword1.length() < 6) {
                    Toast.makeText(this, R.string.please_input_six_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                oldPassWord = EncryptUtils.encodePassword(oldPassWord);
                newPassword1 = EncryptUtils.encodePassword(newPassword1);
                newPassword2 = EncryptUtils.encodePassword(newPassword2);

                final String finalNewPassword = newPassword1;

                if (mIsChangeLoginPwd) {

                    OkGo.<LzyResponse>post(Urls.UPDATE_ULPASS)
                            .tag(this)
                            .params("oldPassword", oldPassWord)
                            .params("newPassword", newPassword1)
                            .params("confirmPassword", newPassword2)
                            .params("userId", userId)
                            .execute(new JsonCallback<LzyResponse>() {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onSuccess(ChangePasswordActivity.this, response.body().msg, response.body().code);
                                    if (response.body().code == 0) {
                                        ToastUtils.showShortToast(mContext, R.string.change_ok);
                                        SPUtils.putString(mContext, Constant.SP_PASSWORD, finalNewPassword);
                                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
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
                } else {

                    OkGo.<LzyResponse>post(Urls.UPDATE_UPAYPASS)
                            .tag(this)
                            .params("oldPassword", oldPassWord)
                            .params("newPassword", newPassword1)
                            .params("confirmPassword", newPassword2)
                            .params("accessToken", token)
                            .params("userId", userId)
                            .execute(new JsonCallback<LzyResponse>() {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                    super.onSuccess(ChangePasswordActivity.this, response.body().msg, response.body().code);
                                    if (response.body().code == 0) {
                                        ToastUtils.showShortToast(mContext, R.string.change_ok);
                                        startActivity(new Intent(mContext, MainActivity.class));
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
            case R.id.tv_forget_password:
                GetPasswordActivity.startActivity(this, mIsChangeLoginPwd ? GetPasswordActivity.TYPE_GET_LOGIN_PWD : GetPasswordActivity.TYPE_GET_PAY_PASSWORD_PWD);
                break;
            case R.id.left_button:
                finish();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startActivity(Context context, boolean isChangeLoginPwd) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.putExtra(Constant.INTENT_IS_CHANGE_LOGIN_PWD, isChangeLoginPwd);
        context.startActivity(intent);
    }
}

