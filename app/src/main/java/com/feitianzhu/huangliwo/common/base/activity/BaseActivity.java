package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/3/22.
 * updata by bch on 2020/5/6.
 * * 修改 ButterKnife  下沉到最底层
 * * 带header的Activity
 * * 取消状态栏适配 ,下沉
 */
public abstract class BaseActivity extends SFActivity {
    protected TreeMap<String, String> maps;
    /**
     * 代替Context
     */
    protected Context mContext;



    @Override
    protected void initBind() {
        super.initBind();
        mContext = BaseActivity.this;
        maps = new TreeMap<String, String>();
        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

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


}
