package com.feitianzhu.huangliwo.login;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;

import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetworkConnectChangedReceiver;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.entity.LoginEntity;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.login.model.UserInfo;
import com.feitianzhu.huangliwo.login.request.LoginRequest;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.WXLoginInfo;
import com.feitianzhu.huangliwo.model.WXLoginModel;
import com.feitianzhu.huangliwo.model.WXUserInfo;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private int loginViewtoBottom;
    private ObjectAnimator animatorUp, animatorDown;
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
    @BindView(R.id.wx_login)
    ImageView wxLogin;
    private String mAccount;
    private String mPassword;
    @BindView(R.id.layoutLogin)
    LinearLayout layoutLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mSignInButton.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgetLayout.setOnClickListener(this);
        wxLogin.setOnClickListener(this);

        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForgetLayout.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        layoutLogin.post(new Runnable() {
            @Override
            public void run() {
                //不可以直接获取控件位置，放在这个里面获取；
                int[] viewLocation = new int[2];
                layoutLogin.getLocationOnScreen(viewLocation); //获取该控价在屏幕中的位置（左上角的点）
//              loginViewtoBottom = UiUtils.getScreenHeight(mContext) - layoutLogin.getBottom(); //getBottom是该控件最底部距离父控件顶部的距离
                WindowManager manager = getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenHeight = outMetrics.heightPixels;
                if (ImmersionBar.hasNavigationBar(LoginActivity.this)) {
                    loginViewtoBottom = screenHeight - viewLocation[1] - layoutLogin.getHeight();//屏幕高度-控件距离顶部高度-控件高度
                } else {
                    loginViewtoBottom = screenHeight - viewLocation[1] - layoutLogin.getHeight() + 80;//屏幕高度-控件距离顶部高度-控件高度
                }
            }
        });

        initListener();
    }

    public void initListener() {
        SoftKeyBoardListener.setListener(LoginActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(AppActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                //if (animatorUp == null) { //如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
                int translationY = height - loginViewtoBottom;
                animatorUp = ObjectAnimator.ofFloat(layoutLogin, "translationY", 0, -translationY);
                animatorUp.setDuration(360);
                animatorUp.setInterpolator(new AccelerateDecelerateInterpolator());
                //}
                animatorUp.start();
            }

            @Override
            public void keyBoardHide(int height) {
                //if (animatorDown == null) {//如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
                int translationY = height - loginViewtoBottom;
                animatorDown = ObjectAnimator.ofFloat(layoutLogin, "translationY", -translationY, 0);
                animatorDown.setDuration(360);
                animatorDown.setInterpolator(new AccelerateDecelerateInterpolator());
                //}
                animatorDown.start();
            }
        });
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
        mAccountLayout.requestFocus();
        mAccountLayout.setSelection(mAccount.length());
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
            case R.id.wx_login:
                reqWeiXin();
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }

    public void reqWeiXin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        IWXAPI api = WXAPIFactory.createWXAPI(LoginActivity.this, Constant.WX_APP_ID);
        api.sendReq(req);
    }


    private void login() {

        mAccount = stringTrim(mAccountLayout);
        mPassword = stringTrim(mPasswordEditText1);
        NetWorkState networkStatus = NetworkConnectChangedReceiver.getNetworkStatus(getApplicationContext());
        if (networkStatus == NetWorkState.NONE) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            return;
        }
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
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.password = base64Ps;
//        loginRequest.phone = mAccount;
//        loginRequest.call(new ApiCallBack<UserInfo>() {
//            @Override
//            public void onAPIResponse(UserInfo response) {
//
//            }
//
//            @Override
//            public void onAPIError(int errorCode, String errorMsg) {
//
//            }
//        });

        OkGo.<LzyResponse<LoginEntity>>post(Urls.LOGIN)
                .tag(this)
                .params("phone", mAccount)
                .params("password", base64Ps)
                .execute(new JsonCallback<LzyResponse<LoginEntity>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<LoginEntity>> response) {
                        super.onSuccess(LoginActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
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
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onStart(Request<LzyResponse<MineInfoModel>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MineInfoModel>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            MineInfoModel userInfo = response.body().data;
                            UserInfoUtils.saveUserInfo(LoginActivity.this, userInfo);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXLogin(String code) {
        OkGo.<WXLoginModel>get("https://api.weixin.qq.com/sns/oauth2/access_token")
                .tag(this)
                .params("appid", Constant.WX_APP_ID)
                .params("secret", "9e4574f38b2b81b24f5305105626bd03")
//                .params("secret", "dfd64a9483d48766f13cf3b2fa70fbe0")
                .params("code", code)
                .params("grant_type", "authorization_code")
                .execute(new JsonCallback<WXLoginModel>() {
                    @Override
                    public void onSuccess(Response<WXLoginModel> response) {
                        if (response.body().errcode == null) {
                            getWXUserInfo(response.body());
                        } else {
                            ToastUtils.show(response.body().errmsg);
                        }
                    }

                    @Override
                    public void onError(Response<WXLoginModel> response) {
                        super.onError(response);
                    }
                });

    }

    public void getWXUserInfo(WXLoginModel wxLoginModel) {
        OkGo.<WXUserInfo>get("https://api.weixin.qq.com/sns/userinfo")
                .tag(this)
                .params("access_token", wxLoginModel.access_token)
                .params("openid", wxLoginModel.openid)
                // .params("lang")//国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为 zh-CN
                .execute(new JsonCallback<WXUserInfo>() {
                    @Override
                    public void onSuccess(Response<WXUserInfo> response) {
                        if (response.body().errcode == null) {
                            wxLogin(response.body());
                        } else {
                            ToastUtils.show(response.body().errmsg);
                        }
                    }

                    @Override
                    public void onError(Response<WXUserInfo> response) {
                        super.onError(response);
                    }
                });
    }

    public void wxLogin(WXUserInfo userInfo) {
        String json = new Gson().toJson(userInfo);
        OkGo.<LzyResponse<WXLoginInfo>>post(Urls.WX_LOGIN)
                .tag(this)
                .params("loginUserInfo", json)
                .execute(new JsonCallback<LzyResponse<WXLoginInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<WXLoginInfo>> response) {
                        super.onSuccess(LoginActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            if (response.body().data.isBindPhone == 0) {
                                Intent intent = new Intent(LoginActivity.this, WXBindingActivity.class);
                                intent.putExtra(WXBindingActivity.WX_DATA, response.body().data);
                                startActivity(intent);
                            } else {
                                getUserInfo(response.body().data.userId, response.body().data.accessToken);
                                Constant.ACCESS_TOKEN = response.body().data.accessToken;
                                Constant.LOGIN_USERID = response.body().data.userId;
                                SPUtils.putString(LoginActivity.this, Constant.SP_LOGIN_USERID, response.body().data.userId);
                                SPUtils.putString(LoginActivity.this, Constant.SP_ACCESS_TOKEN, response.body().data.accessToken);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<WXLoginInfo>> response) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

