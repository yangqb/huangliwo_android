package com.feitianzhu.huangliwo.me.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.LazyFragment;
import com.feitianzhu.huangliwo.me.adapter.ShopRecordDetailAdapter;
import com.feitianzhu.huangliwo.model.ShopRecordDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by Vya on 2017/9/13 0013.
 * 商家录单详情,审查页
 */

public class ShopRecordDetailReviewFragment extends LazyFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<ShopRecordDetailModel.ListBean> mDatas;
    private ShopRecordDetailAdapter mAdapter;


    private int pageRows=6;
    private final static int LOAD_NORMAL = 0;
    private final static int LOAD_MORE = 1;
    private int currPage = 0;
    private int totalPage = 0;
    private boolean hasNextPage = false;
    private View v;
    private View mEmptyView;
    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEmptyView = View.inflate(getActivity(), R.layout.view_common_nodata, null);
       v = inflater.inflate(R.layout.fragment_shop_recodedetail,null,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ShopRecordDetailAdapter(mDatas);
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        mAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        requestData(LOAD_NORMAL);
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    private void requestData(final int state) {

    }



    @Override
    public void onLoadMoreRequested() {
        if(hasNextPage){
            requestData(LOAD_MORE);
        }else{
            mAdapter.loadMoreEnd(true);
        }
    }
}
