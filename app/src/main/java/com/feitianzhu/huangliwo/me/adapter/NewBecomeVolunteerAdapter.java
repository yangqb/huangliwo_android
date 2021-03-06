package com.feitianzhu.huangliwo.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class NewBecomeVolunteerAdapter extends FragmentPagerAdapter {
  private String[] mTitles;
  private List<Fragment> mFragments;

  public NewBecomeVolunteerAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mFragments) {
    super(fm);
    this.mTitles = mTitles;
    this.mFragments = mFragments;
  }

  public NewBecomeVolunteerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
      return mFragments.get(position);
    }
  }