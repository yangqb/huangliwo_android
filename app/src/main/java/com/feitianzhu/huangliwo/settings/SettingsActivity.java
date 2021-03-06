package com.feitianzhu.huangliwo.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.pushshop.ProblemFeedbackActivity;
import com.feitianzhu.huangliwo.update.UpdateMyDialogFragment;
import com.feitianzhu.huangliwo.utils.DataCleanUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UpdateAppHttpUtil;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.VersionManagementUtil;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.AppUpdateUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_USER_AUTH;
import static com.feitianzhu.huangliwo.common.Constant.UAPDATE;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class SettingsActivity extends BaseActivity {
    boolean constraint = false;
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
    @BindView(R.id.btn_fuwu)
    TextView btnFuwu;
    @BindView(R.id.btn_yinsi)
    TextView btnYinsi;

    private boolean isPayPassword;
    private UpdateAppModel updateAppModel;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

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
        btnFuwu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btnFuwu.getPaint().setAntiAlias(true);//抗锯齿
        btnYinsi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        btnYinsi.getPaint().setAntiAlias(true);//抗锯齿
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
        String token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        OkGo.<LzyResponse<UserAuth>>post(Urls.BASE_URL + LOAD_USER_AUTH)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .execute(new JsonCallback<LzyResponse<UserAuth>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<UserAuth>> response) {
                        super.onSuccess(SettingsActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 100021105 || response.body().code == 100010100) {
                            mButton.setText("登陆");
                        } else {
                            mButton.setText("退出当前账号");
                        }
                        if (response.body().data != null && response.body().code == 0) {
                            if (response.body().data.isPaypass == 1) {
                                isPayPassword = true;
                                mTvPayPassword.setText("重设支付密码");
                            } else {
                                isPayPassword = false;
                                mTvPayPassword.setText("设置支付密码");
                            }
                        } else {
                            isPayPassword = false;
                            mTvPayPassword.setText("设置支付密码");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<UserAuth>> response) {
                        super.onError(response);
                        isPayPassword = false;
                        mTvPayPassword.setText("设置支付密码");
                    }
                });
    }

    @OnClick({R.id.rl_change_phone, R.id.rl_change_password, R.id.rl_change_second_password, R.id.rl_about,
            R.id.rl_feedback, R.id.rl_help, R.id.rl_clear_cache, R.id.button, R.id.rl_update, R.id.left_button, R.id.btn_fuwu, R.id.btn_yinsi})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_fuwu:
                intent = new Intent(this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/fuwuxieyi.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营用户服务协议");
                startActivity(intent);
                break;
            case R.id.btn_yinsi:
                intent = new Intent(this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/yinsishuoming.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营用户隐私说明");
                startActivity(intent);
                break;
            case R.id.rl_about:
               /* intent = new Intent(this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.H5_ABOUT_ME);
                intent.putExtra(Constant.H5_TITLE, "关于我们");
                startActivity(intent);*/
                ToastUtils.show("敬请期待");
                break;
            case R.id.rl_help:
                intent = new Intent(SettingsActivity.this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/bangzhu.html");
                intent.putExtra(Constant.H5_TITLE, "帮助中心");
                startActivity(intent);
                break;
            case R.id.rl_update:
                updateDiy();
                break;
            case R.id.rl_change_phone:
                startActivity(new Intent(this, ChangePhone1Activity.class));
                break;
            case R.id.rl_change_password:
                intent = new Intent(SettingsActivity.this, ChangeLoginPassword.class);
                startActivity(intent);
                //ChangePasswordActivity.startActivity(this, true);
                break;
            case R.id.rl_change_second_password:
                if (isPayPassword) {
                    ChangePasswordActivity.startActivity(this);
                } else {
                    GetPasswordActivity.startActivity(mContext, GetPasswordActivity.TYPE_SET_PAY_PASSWORD_PWD);
                }

                break;
            case R.id.rl_feedback:
                startActivity(new Intent(SettingsActivity.this, ProblemFeedbackActivity.class));
                break;
            case R.id.rl_clear_cache:
                DataCleanUtils.cleanApplicationData(this);
                Toast.makeText(this, R.string.clean_cache_successfully, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button:
                //退出登录，清空存储数据
                preferences = getSharedPreferences(SPUtils.PREFERENCE_NAME, MODE_PRIVATE);
                edit = preferences.edit();
                //删除存储文件
                File[] files = new File("/data/data/" + this.getPackageName() + "/shared_prefs").listFiles();
                deleteCache(files);
                Constant.ACCESS_TOKEN = "";
                Constant.LOGIN_USERID = "";
                Constant.PHONE = "";
                //跳转到登录页面
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 删除SharedPreferences数据文件
     *
     * @param files
     */
    public void deleteCache(File[] files) {

        boolean flag;
        for (File itemFile : files) {
            flag = itemFile.delete();
            if (flag == false) {
                deleteCache(itemFile.listFiles());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空数据
        if (edit != null) {
            edit.clear();
            edit.commit();
        }
    }

    public void updateDiy() {
//        final String versionName = "1.2.5";
        final String versionName = AppUpdateUtils.getVersionName(this);
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil(this))
                .setUpdateUrl(Urls.BASE_URL + UAPDATE)
                .setPost(false)
                .setThemeColor(0xfffed428)
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
                        updateAppModel = new Gson().fromJson(json, UpdateAppModel.class);
                        String update = "No";
                        if (VersionManagementUtil.VersionComparison(updateAppModel.data.versionName + "", versionName) == 1) {
                            update = "Yes";
                            if ("1".equals(updateAppModel.data.isForceUpdate)) {
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
                                .setNewVersion(updateAppModel.data.versionName + "")
//                                .setNewVer、sion("1.1.0"+ "")
                                //（必须）下载地址
                                .setApkFileUrl(updateAppModel.data.downloadUrl)
//                                    .setUpdateLog("测试")
                                .setUpdateLog(updateAppModel.data.updateDesc)
//                                    .setUpdateLog("今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说\r\n")
                                //大小，不设置不显示大小，可以不设置
                                .setTargetSize(updateAppModel.data.packSize + "Mb")
                                //是否强制更新，可以不设置
                                .setConstraint(constraint);
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                      /*
                        自定义对话框
                        * */
//                        updateAppManager.showDialogFragment();
                        UpdateAppBean updateAppBean = updateAppManager.fillUpdateAppData();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("update_dialog_values", updateAppBean);
                        bundle.putInt("theme_color", 0xfffed428);
                        UpdateMyDialogFragment updateMyDialogFragment = UpdateMyDialogFragment.newInstance(bundle);
                        updateMyDialogFragment.show(getSupportFragmentManager(), "dialog");
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
                        ToastUtils.show("没有新版本");
                    }
                });
    }
}
