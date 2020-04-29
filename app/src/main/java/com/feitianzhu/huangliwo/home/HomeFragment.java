package com.feitianzhu.huangliwo.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.shop.CommodityClassificationFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/4/27
 * time: 18:09
 * email: 694125155@qq.com
 */
public class HomeFragment extends SFFragment {
    Unbinder unbinder;
    private ArrayList<Fragment> mFragments;
    private List<String> mList = new ArrayList<>();
    private String[] titles = new String[]{"000", "111", "222", "333", "444", "555", "666", "777", "888"};
    @BindView(R.id.tabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    public void initListener() {
        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

       mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换时将所有tab字体设置为正常
                int length = titles.length;
                for (int i = 0; i < length; i++) {
                    TextView titleView = slidingTabLayout.getTitleView(i);
                    titleView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }

                //将当前选中的tab设置为粗体
                TextView currentView = slidingTabLayout.getTitleView(position);
                currentView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(FirstFragment.newInstance(0,titles[0]));
        mFragments.add(FirstFragment.newInstance(0,titles[1]));
        mFragments.add(FirstFragment.newInstance(0,titles[2]));
        mFragments.add(FirstFragment.newInstance(0,titles[3]));
        mFragments.add(FirstFragment.newInstance(0,titles[4]));
        mFragments.add(FirstFragment.newInstance(0,titles[5]));
        mFragments.add(FirstFragment.newInstance(0,titles[6]));
        mFragments.add(FirstFragment.newInstance(0,titles[7]));
        mFragments.add(FirstFragment.newInstance(0,titles[8]));
//      无需编写适配器，一行代码关联TabLayout与ViewPager
        slidingTabLayout.setViewPager(mViewPager, titles, getActivity(), mFragments);
        TextView titleView = slidingTabLayout.getTitleView(0);
        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
