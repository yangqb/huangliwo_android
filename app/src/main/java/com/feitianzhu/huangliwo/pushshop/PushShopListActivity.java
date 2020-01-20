package com.feitianzhu.huangliwo.pushshop;

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
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.PushShopAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.PushMerchantsListInfo;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 11:51
 * email: 694125155@qq.com
 */
public class PushShopListActivity extends BaseActivity {
    private int type = 0;
    private static final int REQUEST_CODE = 1000;
    private PushShopAdapter mAdapter;
    private List<MerchantsModel> examineMerchants = new ArrayList<>();
    private List<MerchantsModel> passedMerchants = new ArrayList<>();
    private List<MerchantsModel> unPassedMerchants = new ArrayList<>();
    private List<MerchantsModel> merchants = new ArrayList<>();
    private String token;
    private String userId;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_toAudit)
    TextView btnToAudit;
    @BindView(R.id.btn_pass)
    TextView btnPass;
    @BindView(R.id.btn_noPass)
    TextView btnNoPass;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_img)
    ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop_list;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        titleName.setText("推店详情");
        rightText.setText("新增门店");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        imageView.setBackgroundResource(R.mipmap.g01_07xinzeng);
        rightText.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        btnToAudit.setSelected(true);
        btnPass.setSelected(false);
        btnNoPass.setSelected(false);

        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PushShopAdapter(merchants);
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type == 2) {
                    Intent intent = new Intent(PushShopListActivity.this, NoPassReasonActivity.class);
                    intent.putExtra(NoPassReasonActivity.REASON_DATA, merchants.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_PUSH_MERCHANTS_LIST)
                .addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .build()
                .execute(new Callback<PushMerchantsListInfo>() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(PushMerchantsListInfo response, int id) {
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (response != null) {
                            btnToAudit.setText("待审核(" + response.getExamineCount() + ")");
                            btnPass.setText("已通过(" + response.getPassedCount() + ")");
                            btnNoPass.setText("未通过(" + response.getUnPassedCount() + ")");
                            examineMerchants = response.examineList;
                            passedMerchants = response.passedList;
                            unPassedMerchants = response.unPassedList;
                            if (type == 0) {
                                merchants = examineMerchants;
                            } else if (type == 1) {
                                merchants = passedMerchants;
                            } else if (type == 2) {
                                merchants = unPassedMerchants;
                            }
                            mAdapter.setNewData(merchants);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.btn_toAudit, R.id.btn_pass, R.id.btn_noPass, R.id.right_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_pass:
                type = 1;
                merchants = passedMerchants;
                mAdapter.setNewData(merchants);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(true);
                btnNoPass.setSelected(false);
                break;
            case R.id.btn_noPass:
                type = 2;
                merchants = unPassedMerchants;
                mAdapter.setNewData(merchants);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(false);
                btnNoPass.setSelected(true);
                break;
            case R.id.btn_toAudit:
                type = 0;
                merchants = examineMerchants;
                mAdapter.setNewData(merchants);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(true);
                btnPass.setSelected(false);
                btnNoPass.setSelected(false);
                break;
            case R.id.right_button:
                boolean isAgreed = SPUtils.getBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS);
                if (isAgreed) {
                    intent = new Intent(PushShopListActivity.this, EditMerchantsActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(PushShopListActivity.this, PushShopProtocolActivity.class);
                    intent.putExtra(PushShopProtocolActivity.PUSH_PROTOCOL, false);
                    startActivity(intent);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdataMechantsEvent(UpdataMechantsEvent event) {
        if (event == UpdataMechantsEvent.SUCCESS) {
            initData();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
