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

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.core.base.fragment.BaseBindingFragment;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.databinding.StrategyFragmentBinding;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyAdapter;
import com.feitianzhu.huangliwo.strategy.bean.TitileBean;
import com.feitianzhu.huangliwo.strategy.request.TitleIdRequest;

import java.util.ArrayList;
import java.util.List;

public class StrategyFragment extends BaseBindingFragment {

    private StrategyFragmentBinding binding;
    public MainActivity mainActivity;

    @Override
    protected View initBindingView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = StrategyFragmentBinding.inflate(inflater, container, false);
        binding.setViewmodel(this);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        StrategyAdapter strategyAdapter = new StrategyAdapter(getChildFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<>();
        TitleIdRequest titleIdRequest = new TitleIdRequest();
        titleIdRequest.isShowLoading = true;
        titleIdRequest.call(new ApiCallBack<List<TitileBean>>() {
            @Override
            public void onAPIResponse(List<TitileBean> response) {
                if (response != null && response.size() >= 0) {
                    strategyAdapter.mTitles = response;
                    strategyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });


        StrategyChildFragment strategyChildFragment1 = new StrategyChildFragment();
        strategyChildFragment1.strategyFragment = StrategyFragment.this;
        fragments.add(strategyChildFragment1);

        StrategyChildFragment strategyChildFragment = new StrategyChildFragment();
        strategyChildFragment.strategyFragment = StrategyFragment.this;
        strategyChildFragment.type = 1;
        fragments.add(strategyChildFragment);

        strategyAdapter.list = fragments;
        binding.tabLayout.setupWithViewPager(binding.pager);

        binding.pager.setAdapter(strategyAdapter);
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
        if (tabAt != null) {
            CharSequence text = tabAt.getText();
            SpannableString spannableString = new SpannableString(text);
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tabAt.setText(spannableString);
            tabAt.select();

        }

    }

    public void showVideo(String url) {
//        mainActivity.showVideo(url);
    }
}
