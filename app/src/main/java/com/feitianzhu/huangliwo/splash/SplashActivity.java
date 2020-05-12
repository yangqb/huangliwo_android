package com.feitianzhu.huangliwo.splash;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.LazyWebActivity;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.settings.ChangeLoginPassword;
import com.feitianzhu.huangliwo.utils.LocationUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomUserPrivateView;
import com.feitianzhu.huangliwo.view.CustomVideoView;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.videoview)
    CustomVideoView mVideoView;
    private static final int REQUEST_CODE_PERMISSION = 100;
    @BindView(R.id.btn)
    TextView mBtn;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
        //initPermision();
        showPrivateDialog();
        SPUtils.putBoolean(this, Constant.LOGIN_DIALOG, true);//重新进入APP才弹出异地登录的弹框
    }

    private void doLogin() {
        /*
         * 视频播放不要了
         * */

        /*mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        mVideoView.start();
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                KLog.e("视频播放失败");
                startMainActivity();
                return true;
            }
        });
        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                 startMainActivity();
            }
        });*/

        /*
         * 倒计时
         * */
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        handler.postDelayed(runnable, 3000);
    }


    public void showPrivateDialog() {

        new XPopup.Builder(this)
                .asCustom(new CustomUserPrivateView(this).setOnClickCancelListener(new CustomUserPrivateView.OnClickCancelListener() {
                    @Override
                    public void onCancel() {
                        finish();
                        System.exit(0);
                    }
                }).setOnClickConfirmListener(new CustomUserPrivateView.OnClickConfirmListener() {
                    @Override
                    public void onConfirm() {
                        requestPermission();
                    }
                })).show();
    }


    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    public void requestPermission() {
        XXPermissions.with(SplashActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            mBtn.setVisibility(View.VISIBLE);
                            doSomeThing();
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                            mBtn.setVisibility(View.GONE);
                            finish();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(SplashActivity.this);
                        } else {
                            ToastUtils.show("获取权限失败");
                            mBtn.setVisibility(View.GONE);
                            finish();
                        }
                    }
                });
    }

    private void doSomeThing() {
        doLogin();
        LocationUtils.getInstance().start();
    }

    @OnClick(R.id.btn)
    public void onClick() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
      /*  if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }*/
        startMainActivity();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration newConfig = new Configuration();
        //控制字体缩放 1.0为默认
        newConfig.fontScale = 1.0f;
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //7.0以上系统手机 显示大小 对APP的影响
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (displayMetrics.density < DisplayMetrics.DENSITY_DEVICE_STABLE / (float) DisplayMetrics.DENSITY_DEFAULT) {
                    displayMetrics.densityDpi = (int) (DisplayMetrics.DENSITY_DEVICE_STABLE * 0.92);
                } else {
                    displayMetrics.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
                }
                newConfig.densityDpi = displayMetrics.densityDpi;
            }
            createConfigurationContext(newConfig);
        }
        res.updateConfiguration(newConfig, displayMetrics);
        return res;
    }
}
