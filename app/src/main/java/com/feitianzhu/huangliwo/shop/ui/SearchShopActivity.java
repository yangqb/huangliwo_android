package com.feitianzhu.huangliwo.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.home.adapter.HomeRecommendAdapter2;
import com.feitianzhu.huangliwo.home.entity.ShopAndMerchants;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.SearchGoodsMode;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    RefreshLayout mSwipeLayout;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeRecommendAdapter2(shopAndMerchants);
        recyclerView.setAdapter(mAdapter);
        initListener();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        SearchShopActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void initData() {

    }

    public void searchData(String searchText) {
        if (searchText == null || TextUtils.isEmpty(searchText)) {
            ToastUtils.show("请输入关键字进行搜索");
            mSwipeLayout.finishRefresh(false);
            return;
        }

        OkGo.<LzyResponse<SearchGoodsMode>>post(Urls.GET_SEARCH_LIST)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("limitNum", Constant.PAGE_SIZE)
                .params("curPage", pageNo + "")
                .params("searchName", searchText)
                .execute(new JsonCallback<LzyResponse<SearchGoodsMode>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<SearchGoodsMode>> response) {
                        super.onSuccess(SearchShopActivity.this, "", response.body().code);
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh();
                        } else {
                            mSwipeLayout.finishLoadMore();
                        }
                        if (!isLoadMore) {
                            shopAndMerchants.clear();
                        }
                        if (response.body().data != null) {
                            emptyView.setVisibility(View.GONE);
                            goodsListBeans = response.body().data.getList();
                            //商品
                            if (goodsListBeans != null && goodsListBeans.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                for (int i = 0; i < goodsListBeans.size(); i++) {
                                    ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_GOODS);
                                    entity.setShopsList(goodsListBeans.get(i));
                                    shopAndMerchants.add(entity);
                                }
                                mAdapter.setNewData(shopAndMerchants);
                            } else {
                                if (!isLoadMore) {
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    ToastUtils.show("没有更多数据了");
                                }
                            }
                        } else {
                            if (!isLoadMore) {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<SearchGoodsMode>> response) {
                        super.onError(response);
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh(false);
                        } else {
                            mSwipeLayout.finishLoadMore(false);
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
                    Intent intent = new Intent(SearchShopActivity.this, ShopMerchantsDetailActivity.class);
                    startActivity(intent);

                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    shopAndMerchants.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(searchText)) {
                        ToastUtils.show("请输入关键字查询");
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
                //  这里记得一定要将键盘隐藏了
                KeyboardUtils.hideSoftInput(editText);
                break;
        }
    }
}