package com.feitianzhu.huangliwo.shop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.core.base.activity.BaseUiActivity;
import com.feitianzhu.huangliwo.databinding.ActivityShopClassfityBinding;
import com.feitianzhu.huangliwo.home.adapter.GoodclsAdapter;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.Shops;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class ShopClassfityActivity extends BaseUiActivity {

    private ActivityShopClassfityBinding binding;

    private String mParam2;
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    private GoodclsAdapter goodclsAdapter;

    public static void getInstance(Context context, String title,String id) {
        Intent intent = new Intent(context, ShopClassfityActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected View initContentView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        binding = ActivityShopClassfityBinding.inflate(layoutInflater, viewGroup, false);
        binding.setViewModel(this);
        return binding.getRoot();
    }

    @Override
    public void init() {

        mParam2 = getIntent().getStringExtra("id");
        initTitle(getIntent().getStringExtra("title"));
        setBackground(R.color.white);
        goodclsAdapter = new GoodclsAdapter(multipleItemList);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(ShopClassfityActivity.this, 2));
        binding.recyclerView.setAdapter(goodclsAdapter);
        initListener();
        getGoodsData();
    }

    public void initListener() {
        binding.backTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.backTop.setVisibility(View.GONE);
                binding.recyclerView.scrollToPosition(0);
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    binding.backTop.setVisibility(View.VISIBLE);
                } else {
                    binding.backTop.setVisibility(View.GONE);
                }
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
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
                    Intent intent = new Intent(ShopClassfityActivity.this, ShopsDetailActivity.class);
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
                Intent intent = new Intent(ShopClassfityActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, UserInfoUtils.getUserInfo(ShopClassfityActivity.this));
                startActivity(intent);
            }
        });
    }


    public void getGoodsData() {
        OkGo.<LzyResponse<Shops>>post(Urls.GET_SHOP)
                .tag(this)
                .params(ACCESSTOKEN, SPUtils.getString(ShopClassfityActivity.this, Constant.SP_ACCESS_TOKEN))
                .params(USERID, SPUtils.getString(ShopClassfityActivity.this, Constant.SP_LOGIN_USERID))
                .params("cls_id", mParam2 + "")
                .execute(new JsonCallback<LzyResponse<Shops>>() {
                    @Override
                    public void onStart(Request<LzyResponse<Shops>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<Shops>> response) {
                        super.onSuccess(ShopClassfityActivity.this, response.body().msg, response.body().code);
                        binding.refreshLayout.finishRefresh();
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
                        binding.refreshLayout.finishRefresh(false);
                    }
                });
    }
}