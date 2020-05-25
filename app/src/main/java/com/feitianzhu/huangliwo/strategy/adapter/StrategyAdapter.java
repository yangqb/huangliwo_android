package com.feitianzhu.huangliwo.strategy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by bch on 2020/5/25
 */
public class StrategyAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"会员须知", "正品保障"};
    public ArrayList<Fragment> list = new ArrayList<>();

    public StrategyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
