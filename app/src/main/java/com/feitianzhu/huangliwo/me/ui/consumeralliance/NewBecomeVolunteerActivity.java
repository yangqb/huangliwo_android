package com.feitianzhu.huangliwo.me.ui.consumeralliance;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.me.adapter.NewBecomeVolunteerAdapter;
import com.feitianzhu.huangliwo.me.fragment.ApplyCityAgentFragment;
import com.feitianzhu.huangliwo.me.fragment.BecomVolunteerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Vya on 2017/9/4 0004.
 * 新的成为志愿者界面
 */

public class NewBecomeVolunteerActivity extends BaseActivity {
    private String[] titles = {"申请志愿者","申请市区代"};

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private List<Fragment> list;
    @Override
    protected int getLayoutId() {
        return R.layout.new_become_volunteer;
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        list.add(new BecomVolunteerFragment());
        list.add(new ApplyCityAgentFragment());
        mViewPager.setAdapter(new NewBecomeVolunteerAdapter(getSupportFragmentManager(),titles,list));
    }

    @Override
    protected void initTitle() {

    }

    private List<String> getTestData(){
        List<String> mList = new ArrayList<>();
        mList.add("AAa");
        return mList;
    }

    @Override
    protected void initData() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        mViewPager.setCurrentItem(0);
    }
}
