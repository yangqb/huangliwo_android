package com.feitianzhu.fu700.me.ui.consumeralliance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.fragment.UnionLevelApplicationFragment;
import com.feitianzhu.fu700.me.fragment.UnionNumberFragment;
import com.feitianzhu.fu700.me.navigationbar.DefaultNavigationBar;
import com.feitianzhu.fu700.me.ui.UnionApplyRecordActivity;
import com.feitianzhu.fu700.shop.adapter.MyPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/9/4 0004.
 */

public class UnionlevelActivity extends BaseActivity {
    @BindView(R.id.tl_2)
    SlidingTabLayout mTl_2;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    private String[] mTitles = {"联盟人数", "联盟级别申请"};
    private List<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unionlevel_application;
    }

    @Override
    protected void initTitle() {
        titleName.setText("联盟");
        rightText.setText("记录");
        rightText.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                Intent intent = new Intent(UnionlevelActivity.this, UnionApplyRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void initView() {
        String agentName = (String) getIntent().getSerializableExtra("AgentName");
        String mRate = (String) getIntent().getSerializableExtra("Rate");
        mFragments = new ArrayList<>();
        UnionLevelApplicationFragment unFragment = new UnionLevelApplicationFragment();
        Bundle mBudle = new Bundle();
        mBudle.putString("AgentName", agentName);
        mBudle.putString("Rate", mRate);
        unFragment.setArguments(mBudle);
        mFragments.add(new UnionNumberFragment());
        mFragments.add(unFragment);
        mTl_2.setDividerWidth(0);

    }

    @Override
    protected void initData() {
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
        mTl_2.setViewPager(mViewPager, mTitles);
    }
}
