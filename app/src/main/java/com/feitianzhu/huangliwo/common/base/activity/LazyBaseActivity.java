package com.feitianzhu.huangliwo.common.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dicallc on 2017/9/11 0011.
 * <p>
 * updata by bch on 2020/5/6.
 * 修改 ButterKnife  下沉到最底层
 * 带header的Activity
 * 取消状态栏适配 ,下沉
 */
//此类废弃,适配以前代码,不删,禁止使用
@Deprecated
public abstract class LazyBaseActivity extends BaseActivity {
    protected LinearLayout mContent;
    @BindView(R.id.title_name)
    TextView mTitle;
    private TextView mRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = (LinearLayout) findViewById(R.id.ll_content);
        View.inflate(this, getChildLayoutId(), mContent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_a_lazy_view;
    }

    /**
     * 子类传入一个布局,父类创建View
     */
    protected abstract int getChildLayoutId();

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    protected void setTitleName(String name) {
        mTitle.setText(name);
    }


}
