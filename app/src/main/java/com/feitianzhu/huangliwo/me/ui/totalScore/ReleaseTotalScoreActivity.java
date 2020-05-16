package com.feitianzhu.huangliwo.me.ui.totalScore;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.adapter.ReleaseTotalDetailAdapter;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.ReleaseTotalDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Vya on 2017/11/12.
 */

public class ReleaseTotalScoreActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.text_big)
    TextView mTextBig;
    @BindView(R.id.recyclerview_release)
    RecyclerView mRecyclerView;

    private ReleaseTotalDetailAdapter mAdapter;
    private String PageRows = "6";
    private final static int LOAD_NORMAL = 0;
    private final static int LOAD_MORE = 1;
    private boolean isHasNextPage = false;
    private View mEmptyView;
    private int currPage = 1;
    private   String flag; //传过来的标记
    private String totalScore; //传过来的总积分
    private List<ReleaseTotalDetailModel.ListBean> mDatas;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_view;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mDatas = new ArrayList<>();
        flag = intent.getStringExtra("releaseFlag");
        totalScore = intent.getStringExtra("releaseTotalScore");
        if(TextUtils.isEmpty(flag)){
            flag = "-1";
        }
        if(!TextUtils.isEmpty(totalScore)){
            mTextBig.setText(totalScore);
        }else{
            mTextBig.setText("0");
        }
        mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
    }

    private void requestData(final int state) {
        showloadDialog("正在加载");
    }

    @Override
    protected void initData() {
        mAdapter = new ReleaseTotalDetailAdapter(mDatas);
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestData(LOAD_NORMAL);
    }

    @Override
    public void onLoadMoreRequested() {
        if (!isHasNextPage){
            mAdapter.loadMoreEnd();
        }else{
            requestData(LOAD_MORE);
        }
    }
}
