package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.CollectionAdapter;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CollectionBody;
import com.feitianzhu.huangliwo.model.CollectionInfo;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectionActivity extends BaseActivity {
    private String userId;
    private String token;
    private int curPage = 1;
    private boolean isLoad;
    //private List<MultiCollectionModel> multiCollectionModels = new ArrayList<>();
    private List<CollectionInfo.CollectionModel> collectionModelList = new ArrayList<>();
    private List<CollectionInfo.CollectionModel> locModelList = new ArrayList<>();
    private CollectionAdapter mAdapter;
    private MineInfoModel mineInfoModel;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("我的收藏");
        mineInfoModel = UserInfoUtils.getUserInfo(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectionAdapter(collectionModelList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoad = true;
                curPage++;
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoad = false;
                curPage = 1;
                initData();
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyCollectionActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (collectionModelList.get(position).type == 1) {
                    //套餐详情页
                    Intent intent = new Intent(MyCollectionActivity.this, ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, collectionModelList.get(position).idValue);
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(MyCollectionActivity.this, ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, collectionModelList.get(position).idValue);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("onItemSwipeStart", pos + "");
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("clearView", pos + "");
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("onItemSwiped", pos + "");
                deleteCollect(locModelList.get(pos).type, locModelList.get(pos).idValue);
                locModelList.remove(pos);
            }


            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                Log.e("onItemSwipeMoving", "dX=" + dX + ";dY=" + dY + isCurrentlyActive);
            }
        });
    }

    public void deleteCollect(int type, int idValue) {
        CollectionBody collectionBody = new CollectionBody();
        collectionBody.type = type;
        collectionBody.idValue = idValue;
        String json = new Gson().toJson(collectionBody);
        OkGo.<LzyResponse>post(Urls.DELETE_COLLECTION).tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("collect", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(MyCollectionActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("取消收藏");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<CollectionInfo>>post(Urls.GET_COLLECTION_LIST).tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("curPage", curPage + "")
                .params("limitNum", "10")
                .execute(new JsonCallback<LzyResponse<CollectionInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CollectionInfo>> response) {
                        super.onSuccess(MyCollectionActivity.this, response.body().msg, response.body().code);
                        if (isLoad) {
                            refreshLayout.finishLoadMore();
                        } else {
                            refreshLayout.finishRefresh();
                        }
                        if (response.body().code == 0 && response.body().data != null) {
                            if (!isLoad) {
                                collectionModelList.clear();
                                locModelList.clear();
                            }
                            if (response.body().data.collectList != null && response.body().data.collectList.size() > 0) {
                                locModelList.addAll(response.body().data.collectList);
                                collectionModelList.addAll(response.body().data.collectList);
                            }
                            mAdapter.setNewData(collectionModelList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<CollectionInfo>> response) {
                        super.onError(response);
                        if (isLoad) {
                            refreshLayout.finishLoadMore(false);
                        } else {
                            refreshLayout.finishRefresh(false);
                        }
                    }
                });

    }
}
