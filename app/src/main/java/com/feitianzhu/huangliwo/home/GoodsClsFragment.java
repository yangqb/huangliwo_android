package com.feitianzhu.huangliwo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.home.adapter.GoodclsAdapter;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.Shops;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/5/9
 * time: 9:40
 * email: 694125155@qq.com
 */
public class GoodsClsFragment extends SFFragment {
    private static final String ARG_PARAM2 = "param2";
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    private GoodclsAdapter goodclsAdapter;
    private MineInfoModel userInfo;
    private int mParam2;
    private String token;
    private String userId;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    public GoodsClsFragment() {

    }

    public static GoodsClsFragment newInstance(int param2) {
        GoodsClsFragment fragment = new GoodsClsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_cls, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        userInfo = UserInfoUtils.getUserInfo(getActivity());
        refreshLayout.setEnableLoadMore(false);
        initView();
        initListener();
        getGoodsData();
        return view;
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getGoodsData();
            }
        });

        goodclsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = goodclsAdapter.getItemViewType(position);
                if (type == MultipleItem.GOODS) {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                    startActivity(intent);
                } else {
                    //商铺详情页
                }
            }
        });

        goodclsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
            }
        });
    }

    public void initView() {
        goodclsAdapter = new GoodclsAdapter(multipleItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(goodclsAdapter);
    }

    public void getGoodsData() {
        OkGo.<LzyResponse<Shops>>post(Urls.GET_SHOP)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("cls_id", mParam2 + "")
                .execute(new JsonCallback<LzyResponse<Shops>>() {
                    @Override
                    public void onStart(Request<LzyResponse<Shops>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<Shops>> response) {
                        super.onSuccess(getActivity(), response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().data != null && response.body().data.getGoodslist() != null) {
                            Shops shops = response.body().data;
                            multipleItemList.clear();
                            goodsListBeans.clear();
                            goodsListBeans = shops.getGoodslist();
                            for (int i = 0; i < goodsListBeans.size(); i++) {
                                MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                                multipleItem.setGoodsListBean(goodsListBeans.get(i));
                                multipleItemList.add(multipleItem);
                            }
                            goodclsAdapter.setNewData(multipleItemList);
                            goodclsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<Shops>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
