package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/10
 * time: 20:05
 * email: 694125155@qq.com
 */
public class PushShopHomeActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop;
    }

    @Override
    protected void initView() {
        titleName.setText("推店");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.push_merchants, R.id.oneself_merchants})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.push_merchants:
                boolean isAgreed = SPUtils.getBoolean(this, Constant.SP_PUSH_SHOP_PROTOCOL);
                if (isAgreed) {
                    intent = new Intent(PushShopHomeActivity.this, PushShopListActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(PushShopHomeActivity.this, PushShopProtocolActivity.class);
                    intent.putExtra(PushShopProtocolActivity.PUSH_PROTOCOL, true);
                    startActivity(intent);
                }
                break;
            case R.id.oneself_merchants:
                intent = new Intent(PushShopHomeActivity.this, MySelfMerchantsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
