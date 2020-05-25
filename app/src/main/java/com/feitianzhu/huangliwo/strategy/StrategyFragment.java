package com.feitianzhu.huangliwo.strategy;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.BaseBindingFragment;
import com.feitianzhu.huangliwo.common.base.fragment.SFFragment;
import com.feitianzhu.huangliwo.databinding.StrategyFragmentBinding;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyAdapter;

import java.util.ArrayList;

public class StrategyFragment extends BaseBindingFragment {

    private StrategyFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = StrategyFragmentBinding.inflate(inflater, container, false);
        binding.setViewmodel(this);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        StrategyAdapter strategyAdapter = new StrategyAdapter(getChildFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new StrategyChildFragment());
        StrategyChildFragment strategyChildFragment = new StrategyChildFragment();
        strategyChildFragment.type = 1;
        fragments.add(strategyChildFragment);
        strategyAdapter.list = fragments;
        binding.pager.setAdapter(strategyAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
                SpannableString spannableString = new SpannableString(text);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spannableString);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
                String s = text.toString();
                tab.setText(s);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.Tab tabAt = binding.tabLayout.getTabAt(0);
        CharSequence text = tabAt.getText();
        SpannableString spannableString = new SpannableString(text);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tabAt.setText(spannableString);
        tabAt.select();

    }
}
