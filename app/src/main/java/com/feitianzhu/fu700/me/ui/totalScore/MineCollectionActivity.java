package com.feitianzhu.fu700.me.ui.totalScore;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.fragment.MerchantsFragment;
import com.feitianzhu.fu700.me.fragment.ServiceFragment;
import com.feitianzhu.fu700.me.navigationbar.DefaultNavigationBar;
import com.feitianzhu.fu700.shop.adapter.MyPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/9/5 0005.
 */

public class MineCollectionActivity extends BaseActivity {
    @BindView(R.id.tl_2)
    SlidingTabLayout mTl_2;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.title_name)
    TextView titleName;
    private String[] mTitles = {"商户", "服务"};
    private List<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_collection;
    }

    @Override
    protected void initTitle() {
        titleName.setText("我的收藏");
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new MerchantsFragment());
        mFragments.add(new ServiceFragment());
//        mFragments.add(new GoodsFragment());
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void onBaseResume() {
        Log.e("onResume", "-------------->>>>");
    }

    @Override
    protected void initData() {
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
        mTl_2.setViewPager(mViewPager, mTitles);
    }
}
