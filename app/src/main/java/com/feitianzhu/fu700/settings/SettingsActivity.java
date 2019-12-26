package com.feitianzhu.fu700.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.fu700.login.LoginActivity;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.UpdateAppModel;
import com.feitianzhu.fu700.model.UserAuth;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.utils.DataCleanUtils;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.UpdateAppHttpUtil;
import com.feitianzhu.fu700.utils.VersionManagementUtil;
import com.google.gson.Gson;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.AppUpdateUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.UAPDATE;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.rl_change_phone)
    RelativeLayout mRlChangePhone;
    @BindView(R.id.rl_change_password)
    RelativeLayout mRlChangePassword;
    @BindView(R.id.rl_change_second_password)
    RelativeLayout mRlChangeSecondPassword;
    @BindView(R.id.rl_about)
    RelativeLayout mRlAbout;
    @BindView(R.id.rl_feedback)
    RelativeLayout mRlFeedback;
    @BindView(R.id.rl_help)
    RelativeLayout mRlHelp;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.rl_update)
    RelativeLayout mRlUpdate;
    @BindView(R.id.tv_clear_cache)
    TextView mTvClearCache;
    @BindView(R.id.tv_pay_password)
    TextView mTvPayPassword;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout mRlClearCache;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.title_name)
    TextView titleName;

    private boolean isPayPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTitle() {
        titleName.setText("设置");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mTvVersion.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ShopDao.loadUserAuth(this, new onNetFinishLinstenerT<UserAuth>() {
            @Override
            public void onSuccess(int code, UserAuth result) {

                if (result != null && result.isPaypass == 1) {
                    isPayPassword = true;
                    mTvPayPassword.setText("重设支付密码");
                } else {
                    isPayPassword = false;
                    mTvPayPassword.setText("设置支付密码");
                }

            }

            @Override
            public void onFail(int code, String result) {
                isPayPassword = false;
                mTvPayPassword.setText("设置支付密码");
            }
        });
    }


    @OnClick({R.id.rl_change_phone, R.id.rl_change_password, R.id.rl_change_second_password, R.id.rl_about,
            R.id.rl_feedback, R.id.rl_help, R.id.rl_clear_cache, R.id.button, R.id.rl_update, R.id.left_button})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.rl_about:
               /* intent = new Intent(this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.H5_ABOUT_ME);
                intent.putExtra(Constant.H5_TITLE, "关于我们");
                startActivity(intent);*/
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.rl_help:
                /*intent = new Intent(this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.H5_HELPER);
                intent.putExtra(Constant.H5_TITLE, "帮助");
                startActivity(intent);*/
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.rl_update:
                updateDiy();
                break;
            case R.id.rl_change_phone:
                startActivity(new Intent(this, ChangePhone1Activity.class));
                break;
            case R.id.rl_change_password:
                ChangePasswordActivity.startActivity(this, true);
                break;
            case R.id.rl_change_second_password:

                if (isPayPassword) {
                    ChangePasswordActivity.startActivity(this, false);
                } else {
                    GetPasswordActivity.startActivity(mContext, GetPasswordActivity.TYPE_SET_PAY_PASSWORD_PWD);
                }

                break;
            case R.id.rl_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.rl_clear_cache:
                DataCleanUtils.cleanApplicationData(this);
                Toast.makeText(this, R.string.clean_cache_successfully, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button:
                SPUtils.putString(this, Constant.SP_PASSWORD, "");
                SPUtils.putString(this, Constant.SP_LOGIN_USERID, "");
                SPUtils.putString(this, Constant.SP_ACCESS_TOKEN, "");
                Constant.ACCESS_TOKEN = "";
                Constant.LOGIN_USERID = "";
                Constant.PHONE = "";
                EventBus.getDefault().post(LoginEvent.LOGOUT);
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void updateDiy() {
        final String versionName = AppUpdateUtils.getVersionName(this);
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil(this))
                .setUpdateUrl(Common_HEADER + UAPDATE)
                .setPost(false)
                .setThemeColor(0xffffac5d)
                .build()
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        UpdateAppModel updateAppModel = new Gson().fromJson(json, UpdateAppModel.class);
                        boolean constraint = false;
                        String update = "No";
                        if (VersionManagementUtil.VersionComparison(updateAppModel.versionName + "", versionName) == 1) {
                            update = "Yes";
                            if ("1".equals(updateAppModel.isForceUpdate)) {
                                constraint = true;
                            } else {
                                constraint = false;
                            }
                        } else {
                            update = "No";
                        }
                        updateAppBean
                                //（必须）是否更新Yes,No
                                .setUpdate(update)
                                //（必须）新版本号，
                                .setNewVersion(updateAppModel.versionName + "")
//                                .setNewVer、sion("1.1.0"+ "")
                                //（必须）下载地址
                                .setApkFileUrl(updateAppModel.downloadUrl)
//                                    .setUpdateLog("测试")
                                .setUpdateLog(updateAppModel.updateDesc)
//                                    .setUpdateLog("今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说\r\n")
                                //大小，不设置不显示大小，可以不设置
                                .setTargetSize(updateAppModel.packSize + "Mb")
                                //是否强制更新，可以不设置
                                .setConstraint(constraint);
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                       // showloadDialog("");
//                CProgressDialogUtils.showProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                     //   goneloadDialog();
//                CProgressDialogUtils.cancelProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                       // goneloadDialog();
                        ToastUtils.showShortToast("没有新版本");
                    }
                });

    }
}
