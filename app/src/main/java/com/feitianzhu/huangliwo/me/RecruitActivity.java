package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.totalScore.MineQrcodeActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/4/2
 * time: 14:56
 * email: 694125155@qq.com
 */
public class RecruitActivity extends BaseActivity {
    public static final String MINE_DATA = "user_info";
    private MineInfoModel mTempData;

    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recruit;
    }

    @Override
    protected void initView() {
        titleName.setText("分享");
        mTempData = (MineInfoModel) getIntent().getSerializableExtra(MINE_DATA);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.share_app, R.id.share_merchant, R.id.left_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.share_app:
                intent = new Intent(this, MineQrcodeActivity.class);
                intent.putExtra(MineQrcodeActivity.SHARE_TYPE, 1);
                intent.putExtra(MineQrcodeActivity.MINE_DATA, mTempData);
                startActivity(intent);
                break;
            case R.id.share_merchant:
                intent = new Intent(this, MineQrcodeActivity.class);
                intent.putExtra(MineQrcodeActivity.SHARE_TYPE, 2);
                intent.putExtra(MineQrcodeActivity.MINE_DATA, mTempData);
                startActivity(intent);
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }
}
