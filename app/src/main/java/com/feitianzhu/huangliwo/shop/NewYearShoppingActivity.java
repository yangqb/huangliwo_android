package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.NewYearGoodsModel;
import com.feitianzhu.huangliwo.shop.adapter.NewYearShoppingAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

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
    SmartRefreshLayout refreshLayout;

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
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        adapter = new NewYearShoppingAdapter(goodsListBeans);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);
        requestData();
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
        OkHttpUtils.get().url(Urls.GET_NEW_YEAR)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback<NewYearGoodsModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(NewYearGoodsModel response, int id) {
                        refreshLayout.finishRefresh();
                        goodsListBeans = response.getActivityList();
                        adapter.setNewData(goodsListBeans);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void requestData() {
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<MineInfoModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MineInfoModel response, int id) {
                        if (response != null) {
                            mineInfoModel = response;
                        }
                    }
                });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
