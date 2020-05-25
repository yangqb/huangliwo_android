package com.feitianzhu.huangliwo.shop.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.SearchGoodsMode;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.shop.adapter.GoodsHistoryKeyAdapter;
import com.feitianzhu.huangliwo.shop.adapter.SearchGoodsAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    public static final String HISTORY_KEY = "history_key";
    @BindView(R.id.et_keyword)
    EditText editText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mSwipeLayout;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;
    @BindView(R.id.historyLayout)
    LinearLayout historyLayout;
    @BindView(R.id.edit_parent_view)
    LinearLayout editParentView;
    private String token;
    private String userId;
    private GoodsHistoryKeyAdapter historyKeyAdapter;
    private MineInfoModel userInfo;
    private int pageNo = 1;
    private String searchText;
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<String> historyKey = new ArrayList<>();
    private SearchGoodsAdapter mAdapter;
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
        userInfo = UserInfoUtils.getUserInfo(this);
        String json = SPUtils.getString(this, HISTORY_KEY);

        if (json != null) {
            List<String> ps = new Gson().fromJson(json, new TypeToken<List<String>>() {
            }.getType());
            historyKey.addAll(ps);
            newList.addAll(ps);
        }
        historyKeyAdapter = new GoodsHistoryKeyAdapter(historyKey);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(historyKeyAdapter);
        historyKeyAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new SearchGoodsAdapter(goodsListBeans);
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

    private List<String> newList = new ArrayList<>();

    public void searchData(String searchText) {
        if (searchText == null || TextUtils.isEmpty(searchText)) {
            ToastUtils.show("请输入关键字进行搜索");
            mSwipeLayout.finishRefresh(false);
            return;
        }
        historyKey.add(0, searchText);
        newList.clear();
        for (String s : historyKey) {
            if (!newList.contains(s)) {
                newList.add(s);
            }
        }
        if (newList.size() > 20) {
            newList.remove(newList.size() - 1);
        }
        String historyKeyJson = new Gson().toJson(newList);
        SPUtils.putString(SearchShopActivity.this, HISTORY_KEY, historyKeyJson);
        mSwipeLayout.getLayout().setVisibility(View.VISIBLE);
        historyLayout.setVisibility(View.GONE);
        OkGo.<LzyResponse<SearchGoodsMode>>post(Urls.GET_SEARCH_LIST)
                .tag(this)
                .params("limitNum", Constant.PAGE_SIZE)
                .params("curPage", pageNo + "")
                .params("searchName", searchText)
                .execute(new JsonCallback<LzyResponse<SearchGoodsMode>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<SearchGoodsMode>> response) {
                        super.onSuccess(SearchShopActivity.this, "", response.body().code);
                        if (!isLoadMore) {
                            goodsListBeans.clear();
                        }
                        if (response.body().data != null && response.body().data.getList() != null && response.body().data.getList().size() > 0) {
                            if (!isLoadMore) {
                                mSwipeLayout.finishRefresh();
                            } else {
                                mSwipeLayout.finishLoadMore();
                            }
                            emptyView.setVisibility(View.GONE);
                            goodsListBeans.addAll(response.body().data.getList());
                            mAdapter.setNewData(goodsListBeans);
                        } else {
                            if (!isLoadMore) {
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                mSwipeLayout.finishLoadMoreWithNoMoreData();
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
                Intent intent = new Intent(SearchShopActivity.this, ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchShopActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
            }
        });

        historyKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                searchText = newList.get(position);
                editText.setText(newList.get(position));
                editText.setSelection(editText.getText().toString().length());
                searchData(newList.get(position));
                KeyboardUtils.hideSoftInput(editText);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mSwipeLayout.getLayout().setVisibility(View.GONE);
                    historyLayout.setVisibility(View.VISIBLE);
                    historyKeyAdapter.setNewData(newList);
                    historyKeyAdapter.notifyDataSetChanged();
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

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * 防止返回本页面弹出输入框
         * */
        editParentView.setFocusable(true);
        editParentView.setFocusableInTouchMode(true);
        editParentView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.back, R.id.btn_search, R.id.cleanHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                KeyboardUtils.hideSoftInput(editText);
                finish();
                break;
            case R.id.btn_search:
                searchText = editText.getText().toString().trim();
                searchData(searchText);
                //  这里记得一定要将键盘隐藏了
                KeyboardUtils.hideSoftInput(editText);
                break;
            case R.id.cleanHistory:
                newList.clear();
                String historyKeyJson = new Gson().toJson(newList);
                SPUtils.putString(SearchShopActivity.this, HISTORY_KEY, historyKeyJson);
                historyKeyAdapter.setNewData(newList);
                historyKeyAdapter.notifyDataSetChanged();
                break;
        }
    }
}