package com.feitianzhu.huangliwo.travel;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.databinding.ActivityTraveFormBinding;
import com.feitianzhu.huangliwo.travel.adapter.TraveFormAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class TraveFormActivity extends BaseBindingActivity {

    private ActivityTraveFormBinding dataBinding;


    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(TraveFormActivity.this, R.layout.activity_trave_form);
        dataBinding.setViewModel(this);
    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white);
    }

    @Override
    public void init() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        strings.add("dfsdfds");
        TraveFormAdapter traveFormAdapter = new TraveFormAdapter(strings);
        dataBinding.recyclerView.setAdapter(traveFormAdapter);
    }

}
