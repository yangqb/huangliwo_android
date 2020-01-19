package com.feitianzhu.huangliwo.me;

import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/1/19
 * time: 14:26
 * email: 694125155@qq.com
 */
public class BindingAccountActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_withdraw;
    }

    @Override
    protected void initView() {
        titleName.setText("绑定账号");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                break;
        }
    }

}
