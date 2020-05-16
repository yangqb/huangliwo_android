package com.feitianzhu.huangliwo.login;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String ext_register = "REGISTER";
    private int loginViewtoBottom;
    private ObjectAnimator animatorUp, animatorDown;
    @BindView(R.id.account)
    EditText mAccountLayout;
    @BindView(R.id.password1)
    EditText mPasswordEditText1;
    @BindView(R.id.sign_in_button)
    TextView mSignInButton;
    @BindView(R.id.edt_code)
    EditText mEditTextCode;
    @BindView(R.id.et_parentId)
    EditText mEditTextParentId;
    @BindView(R.id.tv_code)
    TextView mTextViewCode;
    @BindView(R.id.rl_code)
    RelativeLayout mLayoutCode;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.ll_code)
    LinearLayout mCodeLayout;
    @BindView(R.id.tv_forget_password)
    TextView mForgetLayout;
    @BindView(R.id.tv_regist)
    TextView mRegister;
    @BindView(R.id.ll_protocol)
    LinearLayout mProtocolLayout;
    @BindView(R.id.tv_protocol)
    TextView mTvProtocol;
    @BindView(R.id.cb_protocol)
    CheckBox mCheckBox;
    @BindView(R.id.registerLayout)
    LinearLayout registerLayout;

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
    private String mAccount;
    private String mPassword;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();

        registerLayout.post(new Runnable() {
            @Override
            public void run() {
                //不可以直接获取控件位置，放在这个里面获取；
                int[] viewLocation = new int[2];
                registerLayout.getLocationOnScreen(viewLocation); //获取该控价在屏幕中的位置（左上角的点）
//              loginViewtoBottom = UiUtils.getScreenHeight(mContext) - layoutLogin.getBottom(); //getBottom是该控件最底部距离父控件顶部的距离
                WindowManager manager = getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenHeight = outMetrics.heightPixels;
                loginViewtoBottom = screenHeight - viewLocation[1] - registerLayout.getHeight(); //屏幕高度-控件距离顶部高度-控件高度
            }
        });


        boolean isShow = getIntent().getBooleanExtra(ext_register, false);
        if (isShow) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(this);
        mLayoutCode.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgetLayout.setOnClickListener(this);
        mTvProtocol.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForgetLayout.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        initListener();

    }

    public void initListener() {
        SoftKeyBoardListener.setListener(RegisterActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(AppActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                //if (animatorUp == null) { //如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
                int translationY = height - loginViewtoBottom - 140;
                animatorUp = ObjectAnimator.ofFloat(registerLayout, "translationY", 0, -translationY);
                animatorUp.setDuration(360);
                animatorUp.setInterpolator(new AccelerateDecelerateInterpolator());
                //}
                animatorUp.start();
            }

            @Override
            public void keyBoardHide(int height) {
                //if (animatorDown == null) {//如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
                int translationY = height - loginViewtoBottom - 140;
                animatorDown = ObjectAnimator.ofFloat(registerLayout, "translationY", -translationY, 0);
                animatorDown.setDuration(360);
                animatorDown.setInterpolator(new AccelerateDecelerateInterpolator());
                //}
                animatorDown.start();
            }
        });
    }


    @Override
    protected void initData() {
        showRegisterLayout();
    }

    private void showRegisterLayout() {

        resetEditText();
        mForgetLayout.setVisibility(View.GONE);
        mCodeLayout.setVisibility(View.VISIBLE);
        mEditTextParentId.setVisibility(View.VISIBLE);
        mRegister.setVisibility(View.GONE);
        mSignInButton.setText(R.string.no_account);
        mProtocolLayout.setVisibility(View.VISIBLE);
        mCheckBox.setChecked(true);
    }

    private void resetEditText() {
        mAccountLayout.setText("");
        mEditTextCode.setText("");
        mPasswordEditText1.setText("");
        mEditTextParentId.setText("");
    }

    private void register(final String account, final String password, String code, String parentId) {

        OkGo.<LzyResponse>post(Urls.REGISTER)
                .tag(this)
                .params("phone", account)
                .params("password", password)
                .params("parentId", parentId)
                .params("smsCode", code)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(RegisterActivity.this, response.body().msg == null ? "" : response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                            SPUtils.putString(RegisterActivity.this, Constant.SP_PHONE, mAccount);
                            LoginActivity.startActivity(mContext);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_code:
                String phone = mAccountLayout.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
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
            case R.id.sign_in_button:
                register();
                break;
//            case R.id.tv_regist:
//                    showRegisterLayout();
//                break;
//            case R.id.tv_forget_password:
//                startActivity(new Intent(this, ForgetPasswordActivity.class));
//                break;
            case R.id.tv_protocol:
                Intent intent = new Intent(RegisterActivity.this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/yonghuxieyi.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营用户注册协议");
                startActivity(intent);
                break;

            case R.id.back:
                finish();
                break;

        }
    }


    private void register() {

        mAccount = stringTrim(mAccountLayout);
        mPassword = stringTrim(mPasswordEditText1);

        String code = stringTrim(mEditTextCode);
        String parentId = stringTrim(mEditTextParentId);

        if (TextUtils.isEmpty(mAccount)) {
            Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isPhone(mAccount)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, R.string.please_input_code, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, R.string.please_input_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPassword.length() < 6) {
            Toast.makeText(this, R.string.please_input_six_password, Toast.LENGTH_SHORT).show();
            return;
        }
        String base64Ps = EncryptUtils.encodePassword(mPassword);
        register(mAccount, base64Ps, code, parentId);
    }

    private String stringTrim(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 获取验证码
     */
    private void getValicationCode(String phone) {
        String token = SPUtils.getString(RegisterActivity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "1")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(RegisterActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("验证码已发送至您的手机");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public static void startActivity(Context context, boolean isShow) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(ext_register, isShow);
        context.startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.cb_protocol) {
            if (!isChecked) {
                mSignInButton.setEnabled(false);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06weixuanzhong4);
                mSignInButton.setBackgroundResource(R.drawable.button_shape_gray);
            } else {
                mSignInButton.setEnabled(true);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
                mSignInButton.setBackgroundResource(R.drawable.button_shape_blue);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

