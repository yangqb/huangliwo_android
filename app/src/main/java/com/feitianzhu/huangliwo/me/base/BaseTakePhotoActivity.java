package com.feitianzhu.huangliwo.me.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.SelectPhotoActivity2;
import com.feitianzhu.huangliwo.me.navigationbar.DefaultNavigationBar;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.view.CustomPopWindow;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;

/**
 * Created by Vya on 2017/9/12 0012.
 */

public abstract class BaseTakePhotoActivity extends SelectPhotoActivity2 {
    protected DefaultNavigationBar defaultNavigationBar;
    protected MaterialDialog mDialog;
    protected CustomPopWindow mCustomPopWindow;

   /* @Override
    protected void onWheelSelect(int num, ArrayList<String> mList) {

    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(BaseTakePhotoActivity.this);
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

    protected void showloadDialog(String title) {
        mDialog = new MaterialDialog.Builder(this)
                .content("加载中,请稍等")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    protected void goneloadDialog() {
        if (null != mDialog && mDialog.isShowing()) if (mDialog.isShowing()) mDialog.dismiss();
    }

    /**
     * 子类传入一个布局,父类创建View
     */
    protected abstract int getLayoutId();

    /**
     * 创建一个titlebar
     */
    protected void initTitle() {

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
            ToastUtils.showShortToast(tips);
            return true;
        }
        return false;
    }
}
