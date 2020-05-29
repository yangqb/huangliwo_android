package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.databinding.ActivityBaseUiBinding;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.hjq.toast.ToastUtils;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/3/22.
 * updata by bch on 2020/5/6.
 * * 修改 ButterKnife  下沉到最底层
 * * 带header的Activity
 * * 取消状态栏适配 ,下沉
 */
public abstract class BaseUiActivity extends AbsActivity {
    /**
     * 代替Context
     */
    protected Context mContext;
    private ActivityBaseUiBinding viewDataBinding;


    @Override
    public void initBase() {
        super.initBase();
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_ui);
        viewDataBinding.setViewmodel(this);
//        viewDataBinding.llContent.addView();
        mContext = BaseUiActivity.this;
        // 初始化头部
        initTitle("");

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

    /**
     * 创建一个titlebar
     */
    protected void initTitle(String title) {
        if (!StringUtils.isEmpty(title)) {
            viewDataBinding.titleName.setText(title);
        } else {
            viewDataBinding.titleName.setText("");
        }
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
