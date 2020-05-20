package com.feitianzhu.huangliwo.splash;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.splash.Bean.AdvertisementBean;
import com.feitianzhu.huangliwo.utils.GlideUtils;
import com.feitianzhu.huangliwo.utils.LocationUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomUserPrivateView;
import com.feitianzhu.huangliwo.view.CustomVideoView;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends BaseActivity {

    @BindView(R.id.videoview)
    CustomVideoView mVideoView;
    @BindView(R.id.image)
    ImageView image;
    private static final int REQUEST_CODE_PERMISSION = 100;
    @BindView(R.id.btn)
    TextView mBtn;
    private Handler handler;
    private Runnable runnable;
    private boolean b = false;
    private String strVal;
    boolean service = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initPermision();
        //请求广告数据
        ImageCancheok();
        GlideUtils.getImageView2(this, R.mipmap.dingbu, image);
        service = SPUtils.getBoolean(this, "service");
        if (service != true) {
            showPrivateDialog();
        } else {
            requestPermission();
        }

        SPUtils.putBoolean(this, Constant.LOGIN_DIALOG, true);//重新进入APP才弹出异地登录的弹框
        //获取本地图片
        //判断本地时候存在动画
    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void ImageCancheok() {
        OkGo.<LzyResponse<AdvertisementBean>>get(Urls.ADVERTISEMENT)
                .tag(this)
                .execute(new JsonCallback<LzyResponse<AdvertisementBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<AdvertisementBean>> response) {
                        super.onSuccess(SplashActivity.this, response.body().msg == null ? "" : response.body().msg, response.body().code);
                        if (response.body() != null) {
                            AdvertisementBean data = response.body().data;
                            strVal = data.getStrVal();
                            // b = ImageCancheUtil.hasImageCached(strVal);
                            if (StringUtils.isEmpty(strVal)) {
                                return;
                            }
                            b = ImageCancheUtil.hasImageCached(strVal);
                            if (b == false) {
                                ImageCancheUtil.cacheImage(SplashActivity.this, strVal, new ApiCallBack<String>() {
                                    @Override
                                    public void onAPIResponse(String response) {
                                    }

                                    @Override
                                    public void onAPIError(int errorCode, String errorMsg) {
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<AdvertisementBean>> response) {
                        super.onError(response);
                    }
                });
       /* .execute(new JsonCallback<LzyResponse<AdvertisementBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<AdvertisementBean>> response) {
                AdvertisementBean data = response.body().data;
                strVal = data.getStrVal();
                b = ImageCancheUtil.hasImageCached(strVal);
            }
        });*/
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

                if (b) {
                    //跳转广告页面
                    starAdvertiseActivity();
                } else {
                    //跳转进入MainActivity
                    startMainActivity();
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }


    public void showPrivateDialog() {
        new XPopup.Builder(this)
                .autoDismiss(false)
                .dismissOnTouchOutside(false)
                .enableDrag(false)
                .asCustom(new CustomUserPrivateView(this).setOnClickCancelListener(new CustomUserPrivateView.OnClickCancelListener() {
                    @Override
                    public void onCancel() {
                        finish();
                        System.exit(0);
                    }
                }).setOnClickConfirmListener(new CustomUserPrivateView.OnClickConfirmListener() {
                    @Override
                    public void onConfirm() {
                        SPUtils.putBoolean(SplashActivity.this, "service", true);
                        requestPermission();
                    }
                })).show();
    }


    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void starAdvertiseActivity() {
        Intent intent = new Intent(SplashActivity.this, AdvertisementActivity.class);
        intent.putExtra("strVal", strVal);
        startActivity(intent);
        //startActivity(new Intent(SplashActivity.this, AdvertisementActivity.class));
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
                            // mBtn.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
