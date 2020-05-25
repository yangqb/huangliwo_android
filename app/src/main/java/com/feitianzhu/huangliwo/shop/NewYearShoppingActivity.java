package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.NewYearGoodsModel;
import com.feitianzhu.huangliwo.shop.adapter.NewYearShoppingAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.shop
 * user: yangqinbo
 * date: 2020/1/6
 * time: 15:10
 * email: 694125155@qq.com
 */
public class NewYearShoppingActivity extends BaseActivity {
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private NewYearShoppingAdapter adapter;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private String token;
    private String userId;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_year_shopping;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();
        mineInfoModel = UserInfoUtils.getUserInfo(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        adapter = new NewYearShoppingAdapter(goodsListBeans);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);

        Glide.with(this).asGif().load(R.drawable.holiday)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.color.color_1F9DFF).error(R.color.color_1F9DFF)).into(imageView);
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(NewYearShoppingActivity.this, ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(NewYearShoppingActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

        OkGo.<LzyResponse<NewYearGoodsModel>>get(Urls.GET_NEW_YEAR)
                .tag(this)
                .execute(new JsonCallback<LzyResponse<NewYearGoodsModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<NewYearGoodsModel>> response) {
                        super.onSuccess(NewYearShoppingActivity.this, "", response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().data != null) {
                            goodsListBeans = response.body().data.getActivityList();
                            adapter.setNewData(goodsListBeans);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<NewYearGoodsModel>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
