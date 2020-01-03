package com.feitianzhu.huangliwo.pushshop;

import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/3
 * time: 18:29
 * email: 694125155@qq.com
 */
public class RecordOrderActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_order;
    }

    @Override
    protected void initView() {
        titleName.setText("商家录单");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
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

    @Override
    protected void initData() {

    }
}
