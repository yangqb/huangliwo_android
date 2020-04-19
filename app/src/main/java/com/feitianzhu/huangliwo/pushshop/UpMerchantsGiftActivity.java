package com.feitianzhu.huangliwo.pushshop;

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
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MerchantGiftInfo;
import com.feitianzhu.huangliwo.model.MerchantGitModel;
import com.feitianzhu.huangliwo.pushshop.adapter.MerchantGitfAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
/*
 *
 * 商铺赠品列表
 * */

public class UpMerchantsGiftActivity extends BaseActivity {
    public static final int REQUEST_CODE = 1000;
    public static final int EDIT_REQUEST_CODE = 999;
    public static final String MERCHANTS_ID = "merchants_id";
    private int merchantsId;
    private List<MerchantGitModel> merchantGitModelList = new ArrayList<>();
    private List<MerchantGitModel> locgitModelList = new ArrayList<>();
    private MerchantGitfAdapter adapter;
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_gift;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("上传赠品");
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        rightText.setText("新增");
        rightImg.setBackgroundResource(R.mipmap.a01_04jia);
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MerchantGitfAdapter(merchantGitModelList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter.setEmptyView(mEmptyView);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // 开启滑动删除
        adapter.enableSwipeItem();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);

        initListener();

    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                Intent intent = new Intent(UpMerchantsGiftActivity.this, EditMerchantGiftActivity.class);
                intent.putExtra(EditMerchantGiftActivity.MERCHANTS_ID, merchantsId);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MerchantGiftInfo>>get(Urls.GET_MERCHANT_GIFT_LIST).tag(this)
                .params("merchantId", merchantsId)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MerchantGiftInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantGiftInfo>> response) {
                        super.onSuccess(UpMerchantsGiftActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        locgitModelList.clear();
                        if (response.body().code == 0 && response.body().data != null) {
                            if (response.body().data.list != null) {
                                merchantGitModelList = response.body().data.list;
                                locgitModelList.addAll(response.body().data.list);
                                adapter.setNewData(merchantGitModelList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantGiftInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });


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
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(UpMerchantsGiftActivity.this, EditMerchantGiftActivity.class);
                intent.putExtra(EditMerchantGiftActivity.GIFT_DATA, merchantGitModelList.get(i));
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
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
                deleteGift(locgitModelList.get(pos).id);
                locgitModelList.remove(pos);
            }


            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                Log.e("onItemSwipeMoving", "dX=" + dX + ";dY=" + dY + isCurrentlyActive);
            }
        });
    }

    public void deleteGift(String giftId) {
        OkGo.<LzyResponse>post(Urls.DELETE_GIFT)
                .tag(this)
                .params("giftId", giftId)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(UpMerchantsGiftActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("删除成功");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE || requestCode == EDIT_REQUEST_CODE) {
                initData();
            }
        }
    }
}
