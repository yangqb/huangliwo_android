package com.feitianzhu.huangliwo.settings;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.ForgetPasswordActivity;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.settings
 * user: yangqinbo
 * date: 2020/4/18
 * time: 11:49
 * email: 694125155@qq.com
 */
public class ChangeLoginPassword extends BaseActivity {
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
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_password;
    }

    @Override
    protected void initView() {
        titleName.setText("修改登录密码");
        token = SPUtils.getString(ChangeLoginPassword.this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(ChangeLoginPassword.this, Constant.SP_LOGIN_USERID);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.sign_in_button, R.id.tv_forget_password, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                finish();
                break;
            case R.id.sign_in_button:
                String oldPassWord = mOldPassword.getText().toString();
                String newPassword1 = mPassword1.getText().toString();
                String newPassword2 = mPassword2.getText().toString();

                if (TextUtils.isEmpty(oldPassWord)) {
                    ToastUtils.show(getString(R.string.please_input_oldpassword));
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
                if (newPassword1.length() < 6 || newPassword1.length() > 20) {
                    Toast.makeText(this, R.string.hint_input_login_pwd, Toast.LENGTH_SHORT).show();
                    return;
                }
                oldPassWord = EncryptUtils.encodePassword(oldPassWord);
                newPassword1 = EncryptUtils.encodePassword(newPassword1);
                newPassword2 = EncryptUtils.encodePassword(newPassword2);

                final String finalNewPassword = newPassword1;

                OkGo.<LzyResponse>post(Urls.UPDATE_ULPASS)
                        .tag(this)
                        .params("oldPassword", oldPassWord)
                        .params("newPassword", newPassword1)
                        .params("confirmPassword", newPassword2)
                        .params("userId", userId)
                        .execute(new JsonCallback<LzyResponse>() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                                super.onSuccess(ChangeLoginPassword.this, response.body().msg, response.body().code);
                                if (response.body().code == 0) {
                                    ToastUtils.show(R.string.change_ok);
                                    SPUtils.putString(mContext, Constant.SP_PASSWORD, finalNewPassword);
                                    Intent intent = new Intent(ChangeLoginPassword.this, LoginActivity.class);
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
        }

    }
}
