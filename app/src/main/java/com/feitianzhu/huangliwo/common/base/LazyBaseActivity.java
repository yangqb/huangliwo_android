package com.feitianzhu.huangliwo.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.feitianzhu.huangliwo.R;
import com.gyf.immersionbar.ImmersionBar;

/**
 * Created by dicallc on 2017/9/11 0011.
 */

public abstract class LazyBaseActivity extends SFActivity {
    protected LinearLayout mContent;
    @BindView(R.id.title_name)
    TextView mTitle;
    private TextView mRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_a_lazy_view);
        mContent = (LinearLayout) findViewById(R.id.ll_content);
        View.inflate(this, setView(), mContent);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();

        initLocal();
        initView();
        initData();
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    protected void setTitleName(String name) {
        mTitle.setText(name);
    }

    protected abstract int setView();

    protected abstract void initView();

    protected abstract void initLocal();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
