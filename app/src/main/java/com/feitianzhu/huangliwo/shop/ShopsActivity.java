package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.model.Shops;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.shop
 * user: yangqinbo
 * date: 2019/12/27
 * time: 19:35
 * email: 694125155@qq.com
 */
public class ShopsActivity extends BaseActivity {
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    public static final String CLASSES_ID = "classes_id";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout mSwipeLayout;
    @BindView(R.id.title_name)
    TextView titleName;
    private String token;
    private String userId;
    private int clsId;
    private RightAdapter rightAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_shops;
    }

    @Override
    protected void initView() {
        clsId = getIntent().getIntExtra(CLASSES_ID, -1);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        getShops(clsId);
        rightAdapter = new RightAdapter(multipleItemList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rightAdapter.setEmptyView(mEmptyView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(rightAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        mSwipeLayout.setEnableLoadMore(false);
        initListener();
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    public void initListener() {
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getShops(clsId);
            }
        });

        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = rightAdapter.getItemViewType(position);
                if (type == MultipleItem.GOODS) {
                    //商品详情
                    Intent intent = new Intent(ShopsActivity.this, ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                    startActivity(intent);
                } else {

                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void getShops(int clsId) {

        OkGo.<LzyResponse<Shops>>post(Urls.GET_SHOP)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("cls_id", clsId + "")
                .execute(new JsonCallback<LzyResponse<Shops>>() {
                    @Override
                    public void onStart(Request<LzyResponse<Shops>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<Shops>> response) {
                        super.onSuccess(ShopsActivity.this, "", response.body().code);
                        mSwipeLayout.finishRefresh();
                        goneloadDialog();
                        if (response.body().data != null) {
                            Shops shops = response.body().data;
                            multipleItemList.clear();
                            goodsListBeans.clear();
                            goodsListBeans = shops.getGoodslist();
                            for (int i = 0; i < goodsListBeans.size(); i++) {
                                MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                                multipleItem.setGoodsListBean(goodsListBeans.get(i));
                                multipleItemList.add(multipleItem);
                            }
                            rightAdapter.setNewData(multipleItemList);
                            rightAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<Shops>> response) {
                        super.onError(response);
                        mSwipeLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });
    }
}
