package com.feitianzhu.huangliwo.login;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetworkConnectChangedReceiver;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.im.IMContent;
import com.feitianzhu.huangliwo.login.entity.LoginEntity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.WXLoginInfo;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.socks.library.KLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_button)
    RelativeLayout leftButton;
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

        //EventBus.getDefault().register(this);
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
                if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    reqWeiXin();
                } else {
                    ToastUtils.show("请先安装微信");
                }
                break;

        }
    }

    public void reqWeiXin() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                WXUserInfo wxUserInfo = new WXUserInfo();
                wxUserInfo.city = map.get("city");
                wxUserInfo.country = map.get("country");
                wxUserInfo.headimgurl = map.get("iconurl");
                wxUserInfo.nickname = map.get("name");
                wxUserInfo.openid = map.get("openid");
                //wxUserInfo.privilege
                wxUserInfo.province = map.get("province");
                //wxUserInfo.sex = 1;
                wxUserInfo.unionid = map.get("unionid");
                wxLogin(wxUserInfo);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtils.show("授权失败：" + throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtils.show("取消登录");
            }
        });
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
                        LoadingUtil.setLoadingViewShow(true);
                        if (response.body().code == 0 && response.body().data != null) {

                            EMClient.getInstance().login(userId + IMContent.IMTAG, "123456", new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    //startActivity(new Intent(Customerservice.this,ImActivity.class));
                                    MineInfoModel userInfo = response.body().data;
                                    SPUtils.putString(LoginActivity.this, Constant.SP_PHONE, userInfo.getPhone());
                                    UserInfoUtils.saveUserInfo(LoginActivity.this, userInfo);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Log.d("main", "登录聊天服务器成功!");
                                    LoadingUtil.setLoadingViewShow(false);
                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }

                                @Override
                                public void onError(int code, String message) {
                                    LoadingUtil.setLoadingViewShow(false);
                                    Log.i("onError", "onError: " + code + message);
                                    Log.d("main", "登录聊天服务器失败！");
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
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

    }*/

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
        // EventBus.getDefault().unregister(this);
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


    @OnClick(R.id.left_button)
    public void onViewClicked() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}

