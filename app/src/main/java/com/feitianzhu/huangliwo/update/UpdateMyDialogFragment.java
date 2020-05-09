package com.feitianzhu.huangliwo.update;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.listener.ExceptionHandler;
import com.vector.update_app.listener.ExceptionHandlerHelper;
import com.vector.update_app.listener.IUpdateDialogFragmentListener;
import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;
import com.vector.update_app.utils.ColorUtil;
import com.vector.update_app.utils.DrawableUtil;
import com.vector.update_app.view.NumberProgressBar;

import java.io.File;

/**
 * on 2020/5/9
 */

public class UpdateMyDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static boolean isShow = false;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private UpdateAppBean mUpdateApp;
    private NumberProgressBar mNumberProgressBar;
    private TextView mTitleTextView;
//    private UpdateAppModel updateAppModel;

    /**
     * 回调
     */
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            startDownloadApp((DownloadService.DownloadBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.mipmap.banben;
    private ImageView mTopIv;
    private Button mIgnore;
    private IUpdateDialogFragmentListener mUpdateDialogFragmentListener;
    private DownloadService.DownloadBinder mDownloadBinder;
    private Activity mActivity;
    private TextView mTvTitleTextView;
    //    private Button mClose;
    private LinearLayout mBottom;
    private LinearLayout mRoot;

    public UpdateMyDialogFragment setUpdateDialogFragmentListener(IUpdateDialogFragmentListener updateDialogFragmentListener) {
        this.mUpdateDialogFragmentListener = updateDialogFragmentListener;
        return this;
    }


    public static UpdateMyDialogFragment newInstance(Bundle args) {
        UpdateMyDialogFragment fragment = new UpdateMyDialogFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
//        setStyle(DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);


        mActivity = getActivity();


    }

    @Override
    public void onStart() {
        super.onStart();

        //是否可以取消,会影响上面那条属性
//        setCancelable(false);
//        //window外可以点击,不拦截窗口外的事件
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (mUpdateApp != null && mUpdateApp.isConstraint()) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_updata, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = view.findViewById(R.id.title);
        //副标题
        mTvTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //取消
        mIgnore = view.findViewById(R.id.cancel);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);

//        mClose = view.findViewById(R.id.iv_close);

        mBottom = view.findViewById(R.id.bottom);
        mRoot = view.findViewById(R.id.root);
        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUpdateApp.isConstraint()){
                    dismiss();

                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mUpdateApp = (UpdateAppBean) getArguments().getSerializable("update_dialog_values");
        //设置主题色
        initTheme();

        //点击window外的区域 是否消失
        if (mUpdateApp.isConstraint()) {
            getDialog().setCanceledOnTouchOutside(false);

        } else {
            getDialog().setCanceledOnTouchOutside(true);

        }
        if (mUpdateApp != null) {
            //弹出对话框
//            final String dialogTitle = mUpdateApp.getUpdateDefDialogTitle();
            final String newVersion = mUpdateApp.getNewVersion();
//            final String newVersion = updateAppModel.data.versionName;
//            final String targetSize = mUpdateApp.getTargetSize();
            final String updateLog = mUpdateApp.getUpdateLog();
//            final String updateLog = updateAppModel.data.updateDesc;

            //更新内容
            mContentTextView.setText(updateLog);
            //标题
            mTvTitleTextView.setText(String.format("黄鹂窝全新版本%s上线", newVersion));
            //强制更新
            if (mUpdateApp.isConstraint()) {
                mIgnore.setVisibility(View.GONE);
            } else {
                //不是强制更新时，才生效
//                if (mUpdateApp.isShowIgnoreVersion()) {
                mIgnore.setVisibility(View.VISIBLE);
//                }
            }

            initEvents();
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {


        final int color = getArguments().getInt("theme_color", -1);

//        final int topResId = getArguments().getInt("top_resId", -1);
        final int topResId = -1;

        setDialogTheme(mDefaultColor, mDefaultPicResId);
//        if (-1 == topResId) {
//            if (-1 == color) {
//                //默认红色
//                setDialogTheme(mDefaultColor, mDefaultPicResId);
//            } else {
//                setDialogTheme(color, mDefaultPicResId);
//            }
//
//        } else {
//            if (-1 == color) {
//                //自动提色
////                Palette.from(AppUpdateUtils.drawableToBitmap(this.getResources().getDrawable(topResId))).generate(new Palette.PaletteAsyncListener() {
////                    @Override
////                    public void onGenerated(Palette palette) {
////                        int mDominantColor = palette.getDominantColor(mDefaultColor);
////                        setDialogTheme(mDominantColor, topResId);
////                    }
////                });
//                setDialogTheme(mDefaultColor, topResId);
//            } else {
//                //更加指定的上色
//                setDialogTheme(color, topResId);
//            }
//        }


    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIgnore.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {

            //权限判断是否有访问外部存储
            // 空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                    Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                } else {
                    // 申请授权。
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

            } else {
                installApp();

            }

        } else if (i == R.id.iv_close) {
            // TODO @WVector 这里是否要对UpdateAppBean的强制更新做处理？不会重合，当强制更新时，就不会显示这个按钮，也不会调这个方法。
            if (mNumberProgressBar.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), "后台更新app", Toast.LENGTH_LONG).show();
            }
            cancelDownloadService();
            if (mUpdateDialogFragmentListener != null) {
                // 通知用户
                mUpdateDialogFragmentListener.onUpdateNotifyDialogCancel(mUpdateApp);
            }
            if (mUpdateApp.isConstraint()) {
                mIgnore.setVisibility(View.GONE);
            } else {
                mIgnore.setVisibility(View.VISIBLE);
            }
            dismiss();
        } else if (i == R.id.cancel) {
            AppUpdateUtils.saveIgnoreVersion(getActivity(), mUpdateApp.getNewVersion());
            dismiss();
        }
    }

    public void cancelDownloadService() {
        if (mDownloadBinder != null) {
            // 标识用户已经点击了更新，之后点击取消
            mDownloadBinder.stop("取消下载");
        }
    }

    private void installApp() {
        if (AppUpdateUtils.appIsDownloaded(mUpdateApp)) {
            AppUpdateUtils.installApp(UpdateMyDialogFragment.this, AppUpdateUtils.getAppFile(mUpdateApp));
            //安装完自杀
            //如果上次是强制更新，但是用户在下载完，强制杀掉后台，重新启动app后，则会走到这一步，所以要进行强制更新的判断。
            if (!mUpdateApp.isConstraint()) {
                dismiss();
            } else {
                showInstallBtn(AppUpdateUtils.getAppFile(mUpdateApp));
            }
        } else {
            downloadApp();
            //这里的隐藏对话框会和强制更新冲突，导致强制更新失效，所以当强制更新时，不隐藏对话框。
            if (mUpdateApp.isHideDialog() && !mUpdateApp.isConstraint()) {
                dismiss();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //升级
                installApp();
            } else {
                //提示，并且关闭
                Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                dismiss();

            }
        }

    }

    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        //使用ApplicationContext延长他的生命周期
        DownloadService.bindService(getActivity().getApplicationContext(), conn);
    }

    /**
     * 回调监听下载
     */
    private void startDownloadApp(DownloadService.DownloadBinder binder) {
        // 开始下载，监听下载进度，可以用对话框显示
        if (mUpdateApp != null) {

            this.mDownloadBinder = binder;

            binder.start(mUpdateApp, new DownloadService.DownloadCallback() {
                @Override
                public void onStart() {
                    if (!UpdateMyDialogFragment.this.isRemoving()) {
                        mNumberProgressBar.setVisibility(View.VISIBLE);
//                        mClose.setVisibility(View.VISIBLE);
                        mBottom.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onProgress(float progress, long totalSize) {
                    if (!UpdateMyDialogFragment.this.isRemoving()) {
                        mNumberProgressBar.setProgress(Math.round(progress * 100));
                        mNumberProgressBar.setMax(100);
                    }
                }

                @Override
                public void setMax(long total) {

                }

                //TODO 这里的 onFinish 和 onInstallAppAndAppOnForeground 会有功能上的重合，后期考虑合并优化。
                @Override
                public boolean onFinish(final File file) {
                    dismiss();

                    if (!UpdateMyDialogFragment.this.isRemoving()) {
                        if (mUpdateApp.isConstraint()) {
                            showInstallBtn(file);
                        } else {
                            dismissAllowingStateLoss();
                        }
                    }
                    return true;
                }

                @Override
                public void onError(String msg) {
                    if (!UpdateMyDialogFragment.this.isRemoving()) {
                        dismissAllowingStateLoss();
                    }
                }

                @Override
                public boolean onInstallAppAndAppOnForeground(File file) {
                    if (!mUpdateApp.isConstraint()) {
                        dismiss();
                    }
                    if (mActivity != null) {
                        AppUpdateUtils.installApp(mActivity, file);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    private void showInstallBtn(final File file) {
        mNumberProgressBar.setVisibility(View.GONE);
        mUpdateOkButton.setText("安装");
        mUpdateOkButton.setVisibility(View.VISIBLE);
        mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdateUtils.installApp(UpdateMyDialogFragment.this, file);
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("", "对话框 requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
//        switch (resultCode) {
//            case Activity.RESULT_CANCELED:
//                switch (requestCode){
//                    // 得到通过UpdateDialogFragment默认dialog方式安装，用户取消安装的回调通知，以便用户自己去判断，比如这个更新如果是强制的，但是用户下载之后取消了，在这里发起相应的操作
//                    case AppUpdateUtils.REQ_CODE_INSTALL_APP:
//                        if (mUpdateApp.isConstraint()) {
//                            if (AppUpdateUtils.appIsDownloaded(mUpdateApp)) {
//                                AppUpdateUtils.installApp(UpdateDialogFragment.this, AppUpdateUtils.getAppFile(mUpdateApp));
//                            }
//                        }
//                        break;
//                }
//                break;
//
//            default:
//        }
//    }

    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }

        try {
            super.show(manager, tag);
        } catch (Exception e) {
            ExceptionHandler exceptionHandler = ExceptionHandlerHelper.getInstance();
            if (exceptionHandler != null) {
                exceptionHandler.onException(e);
            }
        }
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }
}


