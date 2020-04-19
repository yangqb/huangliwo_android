package com.feitianzhu.huangliwo.me.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.navigationbar.DefaultNavigationBar;
import com.feitianzhu.huangliwo.view.CustomPopWindow;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.util.TreeMap;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/22.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public DefaultNavigationBar defaultNavigationBar;
    protected TreeMap<String, String> maps;
    /**
     * 代替Context
     */
    protected Context mContext;
    LoadingPopupView loadingPopup;
    protected CustomPopWindow mCustomPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        maps = new TreeMap<String, String>();
        setContentView(getLayoutId());

        //Log.e("TAG", viewRoot + "");

        // 一些特定的算法，子类基本都会使用的
        ButterKnife.bind(BaseActivity.this);

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();


        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        onBaseResume();
    }

    protected void onBaseResume() {

    }

    protected void showPopMenu(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_personview, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .create();
        int width = mCustomPopWindow.getWidth();
        mCustomPopWindow.showAsDropDown(v, -width + v.getWidth(), 0);


    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }

                switch (v.getId()) {
                    case R.id.menu1:
                        onMenuOneClick();
                        break;
                    case R.id.menu2:
                        onMenuTwoClick();
                        break;
                }

            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2).setOnClickListener(listener);
    }

    public void onMenuOneClick() {
    }

    public void onMenuTwoClick() {
    }


    protected void showloadDialog(String title) {
        loadingPopup = (LoadingPopupView) new XPopup.Builder(this)
                .asLoading("正在加载中")
                .show();
    }

    protected void goneloadDialog() {
        if (null != loadingPopup) {
            loadingPopup.delayDismissWith(1000, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }


    /**
     * 子类传入一个布局,父类创建View
     */
    protected abstract int getLayoutId();

    /**
     * 创建一个titlebar
     */
    protected void initTitle() {
     /*   defaultNavigationBar = new DefaultNavigationBar
                .Builder(BaseActivity.this, (ViewGroup)findViewById(R.id.ll_Container))
                .setTitle(setTitleText())
                // .setRightText("发布")
                .setTitleColor(titlebar_home)
                .setStatusColor(titlebar_home)
                .setStatusHeight()
                .setLeftIcon(R.mipmap.icon_left)
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BaseActivity.this,"点击了发布",Toast.LENGTH_SHORT).show();
                    }
                })

                .builder();*/
    }

    /**
     * 子类去实现更改头部的名字
     *
     * @return
     */
    protected String setTitleText() {
        return "";
    }

    /**
     * 初始化界面,比如创建dialog，创建其他的view
     */
    protected abstract void initView();

    /**
     * 初始化ViewData
     */
    protected abstract void initData();

    protected final boolean checkEditText(EditText editText, String tips) {

        if (editText == null) return true;

        String busName = editText.getText().toString().trim();
        if (TextUtils.isEmpty(busName)) {
            ToastUtils.show(tips);
            return true;
        }
        return false;
    }

    protected final boolean checkTextView(TextView textView, String tips) {

        if (textView == null) return true;

        String busName = textView.getText().toString().trim();
        if (TextUtils.isEmpty(busName)) {
            ToastUtils.show(tips);
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingPopup = null;
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
