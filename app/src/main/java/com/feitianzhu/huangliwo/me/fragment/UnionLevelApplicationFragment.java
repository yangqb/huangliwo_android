package com.feitianzhu.huangliwo.me.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.BaseFragment;
import com.feitianzhu.huangliwo.me.adapter.UnionLevelAdapter;
import com.feitianzhu.huangliwo.me.ui.totalScore.SelectPayActivity;
import com.feitianzhu.huangliwo.model.SelectPayNeedModel;
import com.feitianzhu.huangliwo.model.UnionLevelModel;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/8/29 0029.
 */

public class UnionLevelApplicationFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_LevelTxt)
    TextView mCurrentLevel;
    @BindView(R.id.tv_returnNum)
    TextView mTvReturnNum;
    private List<UnionLevelModel> mList;
    private UnionLevelAdapter mAdapter;
    private SelectPayNeedModel model;
    private String agentName;
    private String mRate;

    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        agentName = arguments.getString("AgentName");
        mRate = arguments.getString("Rate");
        if (agentName != null) {
            mCurrentLevel.setText(agentName);
        }
        if (!TextUtils.isEmpty(mRate)) {
            mTvReturnNum.setText(mRate);
        }
        model = new SelectPayNeedModel();
        model.setType(SelectPayNeedModel.TYPE_UNION_LEVEL);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UnionLevelAdapter(mList);
        mAdapter.setOnItemImageClickListener(new UnionLevelAdapter.OnItemImageClickListener() {
            @Override
            public void onItemImageClick(int position) {
//                ToastUtils.show("点击----》"+position);
                //进入付款页面
                model.gradeId = mList.get(position).getGradeId() + "";
                model.setHandleFee(mList.get(position).getPoints());
                model.agentName = mList.get(position).getName();
                model.agentType = mList.get(position).getAgentType();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        requestData();
    }

    private void requestData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_union_level;
    }

    @OnClick(R.id.bt_send)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                Log.e("Test", "------>SelectPayActivity");
                if (model.gradeId != null && !TextUtils.isEmpty(model.gradeId)) {
                    Intent intent = new Intent(getContext(), SelectPayActivity.class);
                    intent.putExtra(Constant.INTENT_SELECTET_PAY_MODEL, model);
                    startActivity(intent);
                } else {
                    ToastUtils.show("请选择会员级别");
                }

                break;
        }
    }
}
