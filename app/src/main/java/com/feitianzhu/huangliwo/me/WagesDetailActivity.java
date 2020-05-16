package com.feitianzhu.huangliwo.me;

import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/4/24
 * time: 11:42
 * email: 694125155@qq.com
 */
public class WagesDetailActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wages_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("细则");
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
