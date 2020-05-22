package com.feitianzhu.huangliwo.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.core.network.ApiLifeCallBack;
import com.feitianzhu.huangliwo.home.request.GoodsListRequest;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.HomeShops;
import com.feitianzhu.huangliwo.model.NewYearGoodsModel;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends SFFragment {
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    private DiscoverAdapter mAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private boolean isLoadMore;
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private int pageNo = 1;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String token;
    private String userId;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        //商家
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        //staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new DiscoverAdapter(goodsListBeans);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.setItemAnimator(null);
        // recyclerView.addItemDecoration(new StaggeredDividerItemDecoration(getActivity(), 10));
        mAdapter.notifyDataSetChanged();
        getData();
        initListener();
        return view;
    }

    public void initListener() {
        int spanCount = 2;
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    staggeredGridLayoutManager.invalidateSpanAssignments();
                }
            }
        });*/


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                isLoadMore = true;
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                isLoadMore = false;
                getData();
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = getResources().getDisplayMetrics().heightPixels;
                if (height != 0 && scrollY > height / 2) {
                    backTop.setVisibility(View.VISIBLE);
                } else {
                    backTop.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getData() {
        GoodsListRequest goodsListRequest = new GoodsListRequest(token, userId, pageNo);
        goodsListRequest.call(new ApiLifeCallBack<HomeShops>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinsh() {

            }

            @Override
            public void onAPIResponse(HomeShops response) {
                if (!isLoadMore) {
                    goodsListBeans.clear();
                }
                if (response != null && response.getGoodsList() != null && response.getGoodsList().size() > 0) {
                    if (!isLoadMore) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                    goodsListBeans.addAll(response.getGoodsList());
                    mAdapter.setNewData(goodsListBeans);
                } else {
                    if (isLoadMore) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                if (!isLoadMore) {
                    refreshLayout.finishRefresh(false);
                } else {
                    refreshLayout.finishLoadMore(false);
                }
            }
        });
    }

    @OnClick(R.id.back_top)
    public void onViewClicked(View view) {
        scrollView.fling(0);
        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
