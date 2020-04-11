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
import com.feitianzhu.huangliwo.common.base.WebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.PushShopAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.MultiMerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.PushMerchantsListInfo;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    private List<MultiMerchantsModel> muitlMerchantsModels = new ArrayList<>();
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
        mAdapter = new PushShopAdapter(muitlMerchantsModels);
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
                if (mAdapter.getItemViewType(position) == MultiMerchantsModel.UNPASSED_TYPE) {
                    Intent intent = new Intent(PushShopListActivity.this, NoPassReasonActivity.class);
                    intent.putExtra(NoPassReasonActivity.REASON_DATA, muitlMerchantsModels.get(position).merchants);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItemViewType(position) == MultiMerchantsModel.PASSED_TYPE) {
                    Intent intent = new Intent(PushShopListActivity.this, ShareMerchantActivity.class);
                    intent.putExtra(ShareMerchantActivity.MERCHANT_DATA, muitlMerchantsModels.get(position).merchants);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<PushMerchantsListInfo>>get(Urls.GET_PUSH_MERCHANTS_LIST)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<PushMerchantsListInfo>>() {

                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<PushMerchantsListInfo>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<PushMerchantsListInfo>> response) {
                        super.onSuccess(PushShopListActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        muitlMerchantsModels.clear();
                        if (response.body().code == 0 && response.body().data != null) {
                            btnToAudit.setText("待审核(" + response.body().data.getExamineCount() + ")");
                            btnPass.setText("已通过(" + response.body().data.getPassedCount() + ")");
                            btnNoPass.setText("未通过(" + response.body().data.getUnPassedCount() + ")");
                            examineMerchants = response.body().data.examineList;
                            passedMerchants = response.body().data.passedList;
                            unPassedMerchants = response.body().data.unPassedList;
                            if (type == 0) {
                                if (examineMerchants != null && examineMerchants.size() > 0) {
                                    for (int i = 0; i < examineMerchants.size(); i++) {
                                        MultiMerchantsModel examineMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.EXAMINE_TYPE);
                                        examineMerchantsModel.merchants = examineMerchants.get(i);
                                        muitlMerchantsModels.add(examineMerchantsModel);
                                    }
                                }
                                merchants = examineMerchants;
                            } else if (type == 1) {
                                if (passedMerchants != null && passedMerchants.size() > 0) {
                                    for (int i = 0; i < passedMerchants.size(); i++) {
                                        MultiMerchantsModel passedMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.PASSED_TYPE);
                                        passedMerchantsModel.merchants = passedMerchants.get(i);
                                        muitlMerchantsModels.add(passedMerchantsModel);
                                    }
                                }
                                merchants = passedMerchants;
                            } else if (type == 2) {
                                if (unPassedMerchants != null && unPassedMerchants.size() > 0) {
                                    for (int i = 0; i < unPassedMerchants.size(); i++) {
                                        MultiMerchantsModel unPassedMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.UNPASSED_TYPE);
                                        unPassedMerchantsModel.merchants = unPassedMerchants.get(i);
                                        muitlMerchantsModels.add(unPassedMerchantsModel);
                                    }
                                }
                                merchants = unPassedMerchants;
                            }
                            mAdapter.setNewData(muitlMerchantsModels);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PushMerchantsListInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
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
                muitlMerchantsModels.clear();
                if (passedMerchants != null && passedMerchants.size() > 0) {
                    for (int i = 0; i < passedMerchants.size(); i++) {
                        MultiMerchantsModel passedMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.PASSED_TYPE);
                        passedMerchantsModel.merchants = passedMerchants.get(i);
                        muitlMerchantsModels.add(passedMerchantsModel);
                    }
                }
                mAdapter.setNewData(muitlMerchantsModels);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(true);
                btnNoPass.setSelected(false);
                break;
            case R.id.btn_noPass:
                type = 2;
                muitlMerchantsModels.clear();
                if (unPassedMerchants != null && unPassedMerchants.size() > 0) {
                    for (int i = 0; i < unPassedMerchants.size(); i++) {
                        MultiMerchantsModel unPassedMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.UNPASSED_TYPE);
                        unPassedMerchantsModel.merchants = unPassedMerchants.get(i);
                        muitlMerchantsModels.add(unPassedMerchantsModel);
                    }
                }
                mAdapter.setNewData(muitlMerchantsModels);
                mAdapter.notifyDataSetChanged();
                btnToAudit.setSelected(false);
                btnPass.setSelected(false);
                btnNoPass.setSelected(true);
                break;
            case R.id.btn_toAudit:
                type = 0;
                muitlMerchantsModels.clear();
                for (int i = 0; i < examineMerchants.size(); i++) {
                    MultiMerchantsModel examineMerchantsModel = new MultiMerchantsModel(MultiMerchantsModel.EXAMINE_TYPE);
                    examineMerchantsModel.merchants = examineMerchants.get(i);
                    muitlMerchantsModels.add(examineMerchantsModel);
                }
                mAdapter.setNewData(muitlMerchantsModels);
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
                    intent = new Intent(PushShopListActivity.this, WebActivity.class);
                    intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/tuidianguize.html");
                    intent.putExtra(Constant.H5_TITLE, "推店规则和收益说明");
                    intent.putExtra(WebActivity.PUSH_PROTOCOL, false);
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
