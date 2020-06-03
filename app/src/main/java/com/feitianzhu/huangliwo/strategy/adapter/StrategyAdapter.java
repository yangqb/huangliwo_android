package com.feitianzhu.huangliwo.strategy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feitianzhu.huangliwo.strategy.bean.TitileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bch on 2020/5/25
 */
public class StrategyAdapter extends FragmentPagerAdapter {
    public List<TitileBean> mTitles;
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
        if (mTitles != null && mTitles.size() >= 0) {
            return mTitles.get(position).getColumnName();
        } else {
            return "..";
        }
    }
}
