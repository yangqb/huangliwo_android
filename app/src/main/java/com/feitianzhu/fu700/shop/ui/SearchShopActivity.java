package com.feitianzhu.fu700.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.home.adapter.HomeRecommendAdapter2;
import com.feitianzhu.fu700.home.entity.ShopAndMerchants;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.SearchGoodsMode;
import com.feitianzhu.fu700.shop.ShopSetMealActivity;
import com.feitianzhu.fu700.shop.ShopsDetailActivity;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.util.KeyboardUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * package name: com.feitianzhu.fu700.shop.ui
 * user: yangqinbo
 * date: 2019/12/25
 * time: 20:48
 * email: 694125155@qq.com
 */
public class SearchShopActivity extends BaseActivity {
    @BindView(R.id.et_keyword)
    EditText editText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSwipeLayout;
    private String token;
    private String userId;
    private int pageNo = 1;
    private String searchText;
    private List<ShopAndMerchants> shopAndMerchants = new ArrayList<>();
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private HomeRecommendAdapter2 mAdapter;
    private boolean isLoadMore;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_shop;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        TextView noData = mEmptyView.findViewById(R.id.no_data);
        noData.setText("暂无搜索结果");
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeRecommendAdapter2(shopAndMerchants);
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListener();
    }

    @Override
    protected void initData() {

    }

    public void searchData(String searchText) {
        OkHttpUtils.post()
                .url(Urls.GET_SEARCH_LIST)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("limitNum", Constant.PAGE_SIZE)
                .addParams("curPage", pageNo + "")
                .addParams("searchName", searchText)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, SearchGoodsMode.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh(false);
                        } else {
                            mSwipeLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh();
                        } else {
                            mSwipeLayout.finishLoadMore();
                        }
                        SearchGoodsMode goodsMode = (SearchGoodsMode) response;
                        goodsListBeans = goodsMode.getList();
                        if (!isLoadMore) {
                            shopAndMerchants.clear();
                        }
                        //商品
                        if (goodsListBeans != null && goodsListBeans.size() > 0) {
                            for (int i = 0; i < goodsListBeans.size(); i++) {
                                ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_GOODS);
                                entity.setShopsList(goodsListBeans.get(i));
                                shopAndMerchants.add(entity);
                            }
                            mAdapter.setNewData(shopAndMerchants);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void initListener() {
        mSwipeLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                isLoadMore = true;
                searchData(searchText);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                isLoadMore = false;
                searchData(searchText);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = adapter.getItemViewType(position);
                if (type == ShopAndMerchants.TYPE_GOODS) {
                    //商品详情
                    Intent intent = new Intent(SearchShopActivity.this, ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, shopAndMerchants.get(position).getShopsList().getGoodsId());
                    startActivity(intent);
                } else {
                    //套餐详情页
                    Intent intent = new Intent(SearchShopActivity.this, ShopSetMealActivity.class);
                    startActivity(intent);

                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(searchText)) {
                        ToastUtils.showShortToast("请输入关键字查询");
                        return true;
                    }
                    //  下面就是业务逻辑
                    searchData(searchText);
                    //  这里记得一定要将键盘隐藏了
                    KeyboardUtils.hideSoftInput(editText);
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.back, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_search:
                searchText = editText.getText().toString().trim();
                searchData(searchText);
                break;
        }
    }
}