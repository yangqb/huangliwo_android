package com.feitianzhu.huangliwo.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.entity.LoginEntity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.LocationUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CustomVideoView;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.socks.library.KLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;


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
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
        initPermision();
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
                realLogin();
                return true;
            }
        });
        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                realLogin();
            }
        });*/

        /*
         * 倒计时
         * */
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                realLogin();
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void realLogin() {
        final String token = SPUtils.getString(SplashActivity.this, Constant.SP_ACCESS_TOKEN);
        final String userId = SPUtils.getString(SplashActivity.this, Constant.SP_LOGIN_USERID);

        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId)) {
            startMainActivity();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void initPermision() {
        AndPermission.with(SplashActivity.this)
                .requestCode(REQUEST_CODE_PERMISSION)
                .permission(Permission.LOCATION, Permission.STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
    }

    private void doSomeThing() {
        doLogin();
        LocationUtils.getInstance().start();
    }

    private static final int REQUEST_CODE_SETTING = 300;

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    mBtn.setVisibility(View.VISIBLE);
                    doSomeThing();
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    mBtn.setVisibility(View.GONE);
                    finish();
                    return;
                }
            }
            if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(SplashActivity.this, REQUEST_CODE_SETTING).show();
            }
        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
            AndPermission.rationaleDialog(SplashActivity.this, rationale).show();

        }
    };

    @OnClick(R.id.btn)
    public void onClick() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
      /*  if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }*/
        realLogin();
    }
}
