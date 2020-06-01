package com.feitianzhu.huangliwo.strategy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.core.base.fragment.BaseBindingFragment;
import com.feitianzhu.huangliwo.core.base.BaseWebviewActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.databinding.FragmentStrategyChildListBinding;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyItem1Adapter;
import com.feitianzhu.huangliwo.strategy.adapter.StrategyItemAdapter;
import com.feitianzhu.huangliwo.strategy.bean.ListPageBean;
import com.feitianzhu.huangliwo.strategy.request.ListPageRequest;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;


/**
 *
 */
public class StrategyChildFragment extends BaseBindingFragment {


    private FragmentStrategyChildListBinding binding;
    //0 会员须知   1  正品保障
    public int type = 0;
    private StrategyItem1Adapter strategyItemAdapter;
    private StrategyItemAdapter strategyItemAdapter1;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStrategyChildListBinding.inflate(inflater, container, false);
        binding.setViewModel(this);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        binding.refreshLayout.setEnableRefresh(true);
        binding.refreshLayout.setEnableLoadMore(true);
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                request(0);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;

                request(1);
            }
        });


        if (type == 0) {
            View mEmptyView = View.inflate(getActivity(), R.layout.view_common_nodata, null);
            ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
            img_empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            strategyItemAdapter = new StrategyItem1Adapter(null);
            strategyItemAdapter.setEmptyView(mEmptyView);
            binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.list.setAdapter(strategyItemAdapter);
            strategyItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BaseWebviewActivity.toBaseWebviewActivity(getActivity(), strategyItemAdapter.getData().get(position).getH5Url());
                }
            });
        } else {
            View mEmptyView1 = View.inflate(getActivity(), R.layout.view_common_nodata, null);
            ImageView img_empty1 = (ImageView) mEmptyView1.findViewById(R.id.img_empty);
            img_empty1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            strategyItemAdapter1 = new StrategyItemAdapter(null);
            strategyItemAdapter1.setEmptyView(mEmptyView1);
            binding.list.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.list.setAdapter(strategyItemAdapter1);
            strategyItemAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BaseWebviewActivity.toBaseWebviewActivity(getActivity(), strategyItemAdapter1.getData().get(position).getH5Url());
                }
            });
        }

        request(-1);

    }

    private void request(int i) {
        ListPageRequest listPageRequest = new ListPageRequest();
        listPageRequest.isShowLoading = true;
        if (type == 0) {
            listPageRequest.columnId = 1;
            listPageRequest.currentPage = currentPage;
            listPageRequest.pageSize = 10;
            listPageRequest.call(new ApiCallBack<ListPageBean>() {
                @Override
                public void onAPIResponse(ListPageBean response) {
                    List<ListPageBean.ListBean> list = response.getList();

                    if (list != null && list.size() > 0) {
                        if (currentPage == 1) {
                            strategyItemAdapter.getData().clear();
                            strategyItemAdapter.setNewData(list);
                        } else {
                            strategyItemAdapter.addData(list);
                        }
                    } else if (currentPage == 1) {
                        strategyItemAdapter.setNewData(null);
                    }
                    if (i == 1) {
                        binding.refreshLayout.finishRefresh();
                    } else if (i == 0) {
                        binding.refreshLayout.finishLoadMore();
                    }
                    strategyItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onAPIError(int errorCode, String errorMsg) {
                    strategyItemAdapter.notifyDataSetChanged();
                    if (i == 1) {
                        binding.refreshLayout.finishRefresh();
                    } else if (i == 0) {
                        binding.refreshLayout.finishLoadMore();
                    }
                }
            });

        } else {
            listPageRequest.columnId = 2;
            listPageRequest.currentPage = currentPage;
            listPageRequest.pageSize = 10;
            listPageRequest.call(new ApiCallBack<ListPageBean>() {
                @Override
                public void onAPIResponse(ListPageBean response) {
                    List<ListPageBean.ListBean> list = response.getList();
                    if (list != null && list.size() > 0) {
                        if (currentPage == 1) {
                            strategyItemAdapter1.getData().clear();
                            strategyItemAdapter1.setNewData(list);
                        } else {
                            strategyItemAdapter1.addData(list);
                        }
                    } else if (currentPage == 1) {
                        strategyItemAdapter1.setNewData(null);
                    }
                    if (i == 1) {
                        binding.refreshLayout.finishRefresh();
                    } else if (i == 0) {
                        binding.refreshLayout.finishLoadMore();
                    }
                    strategyItemAdapter1.notifyDataSetChanged();
                }

                @Override
                public void onAPIError(int errorCode, String errorMsg) {
                    strategyItemAdapter1.notifyDataSetChanged();
                    if (i == 1) {
                        binding.refreshLayout.finishRefresh();
                    } else if (i == 0) {
                        binding.refreshLayout.finishLoadMore();
                    }
                }
            });

        }
    }
}
