package com.feitianzhu.huangliwo.me.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.adapter.PersonInfoAdapter;
import com.feitianzhu.huangliwo.me.base.BaseFragment;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Vya on 2017/8/29 0029.
 */

public class PersonInfoFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private String otherUserId;
    private List<MineInfoModel> mList;
    private PersonInfoAdapter mAdapter;
    @Override
    protected void initView() {
        mList = new ArrayList<>();
        Bundle arguments = getArguments();
//        otherUserId = arguments.getString("otherUserId");
//        otherUserId = "13";
        MineInfoModel model = (MineInfoModel) arguments.getSerializable("shopDetailBean");
        mList.add(model);
    }

    @Override
    protected void initData() {
       // mList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PersonInfoAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

       // requestData();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personinfo_recycler;
    }
}
