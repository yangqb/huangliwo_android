package com.feitianzhu.huangliwo.strategy;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.BaseBindingFragment;
import com.feitianzhu.huangliwo.databinding.FragmentStrategyChildListBinding;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyItem1Adapter;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyItemAdapter;

import java.util.ArrayList;


/**
 *
 */
public class StrategyChildFragment extends BaseBindingFragment {


    private FragmentStrategyChildListBinding binding;
    //0 会员须知   1  正品保障
    public int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStrategyChildListBinding.inflate(inflater, container, false);
        binding.setViewModel(this);
        return binding.getRoot();
    }


    @Override
    protected void init() {

        if (type == 0) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            strings.add("fsdfsdfsdf");
            binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            StrategyItem1Adapter strategyItemAdapter = new StrategyItem1Adapter(strings);
            binding.list.setAdapter(strategyItemAdapter);
        } else {
            ArrayList<String> strings1 = new ArrayList<>();
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            strings1.add("fsdfsdfsdf");
            binding.list.setLayoutManager(new GridLayoutManager(getContext(), 2));
            StrategyItemAdapter strategyItemAdapter = new StrategyItemAdapter(strings1);
            binding.list.setAdapter(strategyItemAdapter);
        }

    }
}
