package com.feitianzhu.huangliwo.travel;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.utils.GlideUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.travel
 * user: yangqinbo
 * date: 2020/4/9
 * time: 14:45
 * email: 694125155@qq.com
 */
public class TravelHomeActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travel_home;
    }

    @Override
    protected void initView() {
        titleName.setText("旅游");
        Glide.with(this).load(R.mipmap.lvyou).into(GlideUtils.getImageView2(this, R.mipmap.lvyou, imageView));
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }
}
