package com.feitianzhu.huangliwo.me.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.BaseFragment;
import com.feitianzhu.huangliwo.me.adapter.OrderUnUseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Vya on 2017/8/29 0029.
 * 我的订单下未使用的Fragment
 */

public class OrderUnUseFragment extends BaseFragment {
    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerView;
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        List<String> mList = new ArrayList<>();
        mList.add("AAAAAAAA");
        mList.add("AAAAAAAA");
        mList.add("AAAAAAAA");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new OrderUnUseAdapter(mList));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }
}
