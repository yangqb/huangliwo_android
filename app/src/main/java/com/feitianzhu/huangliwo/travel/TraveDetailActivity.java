package com.feitianzhu.huangliwo.travel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.databinding.ActivityTraveDetailBinding;
import com.feitianzhu.huangliwo.travel.adapter.Distance1Adapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceAdapter;
import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.ArrayList;

public class TraveDetailActivity extends BaseBindingActivity {

    private ActivityTraveDetailBinding dataBinding;

    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(TraveDetailActivity.this, R.layout.activity_trave_detail);
        dataBinding.setViewModel(this);
    }

    public String n = "", n1 = "", n2 = "";

    @Override
    public void init() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("汽油");
        strings.add("柴油");
        Distance1Adapter distanceAdapter = new Distance1Adapter(strings);
        dataBinding.oilClass.setLayoutManager(new GridLayoutManager(this, 4));

        dataBinding.oilClass.setAdapter(distanceAdapter);
        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("#92");
        strings1.add("#95");
        Distance1Adapter distanceAdapter1 = new Distance1Adapter(strings1);
        dataBinding.oilLevel.setLayoutManager(new GridLayoutManager(this, 4));
        dataBinding.oilLevel.setAdapter(distanceAdapter1);
        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("1号");
        strings2.add("2号");
        strings2.add("3号");
        strings2.add("4号");
        strings2.add("5号");
        strings2.add("6号");
        strings2.add("7号");
        strings2.add("8号");
        strings2.add("9号");
        strings2.add("10号");
        Distance1Adapter distanceAdapter2 = new Distance1Adapter(strings2);
        dataBinding.gun.setLayoutManager(new GridLayoutManager(this, 4));

        dataBinding.gun.setAdapter(distanceAdapter2);

        distanceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                n = strings.get(position);
                distanceAdapter.chengtextcolor(position);
                distanceAdapter.notifyDataSetChanged();
                distanceAdapter1.chengtextcolor(-1);
                distanceAdapter2.chengtextcolor(-1);
                distanceAdapter1.notifyDataSetChanged();
                distanceAdapter2.notifyDataSetChanged();
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        distanceAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n)) {
                    n1 = strings1.get(position);
                    distanceAdapter1.chengtextcolor(position);
                    distanceAdapter1.notifyDataSetChanged();
                    distanceAdapter2.chengtextcolor(-1);
                    distanceAdapter2.notifyDataSetChanged();

                }
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        distanceAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n1)) {
                    n2 = strings2.get(position);
                    distanceAdapter2.chengtextcolor(position);
                    distanceAdapter2.notifyDataSetChanged();
                }
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
    }
}