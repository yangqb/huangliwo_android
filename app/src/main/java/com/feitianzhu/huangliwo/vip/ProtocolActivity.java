package com.feitianzhu.huangliwo.vip;

import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/29 0029 下午 6:24
 */
public class ProtocolActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initView() {
        titleName.setText("便利大本营会员服务协议");
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }
}
