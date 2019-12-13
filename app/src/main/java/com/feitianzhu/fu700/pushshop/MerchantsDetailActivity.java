package com.feitianzhu.fu700.pushshop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/13
 * time: 18:17
 * email: 694125155@qq.com
 * <p>
 * 商铺详情
 */
public class MerchantsDetailActivity extends BaseActivity {
    @BindView(R.id.right_img)
    ImageView imageView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_detail;
    }

    @Override
    protected void initView() {
        imageView.setBackgroundResource(R.mipmap.g06_01bianji);
        rightText.setText("编辑");
        rightText.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
