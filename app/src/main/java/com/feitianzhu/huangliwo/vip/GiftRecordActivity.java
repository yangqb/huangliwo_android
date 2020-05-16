package com.feitianzhu.huangliwo.vip;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.GiftRecordModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.vip.VipGiftDetailActivity.GIFT_ID;

public class GiftRecordActivity extends BaseActivity {
    private String userId;
    private String token;
    private List<GiftRecordModel> giftRecordModelList = new ArrayList<>();
    private GiftRecordAdapter adapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift_record;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("领取记录");
        refreshLayout.setEnableLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GiftRecordAdapter(giftRecordModelList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter.setEmptyView(mEmptyView);
        adapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<List<GiftRecordModel>>>get(Urls.GET_GIFT_RECORD).tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<List<GiftRecordModel>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<GiftRecordModel>>> response) {
                        super.onSuccess(GiftRecordActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            giftRecordModelList = response.body().data;
                            adapter.setNewData(giftRecordModelList);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Response<LzyResponse<List<GiftRecordModel>>> response) {
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
                //礼品详情页
                Intent intent = new Intent(GiftRecordActivity.this, VipGiftDetailActivity.class);
                intent.putExtra(GIFT_ID, giftRecordModelList.get(i).giftId);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
