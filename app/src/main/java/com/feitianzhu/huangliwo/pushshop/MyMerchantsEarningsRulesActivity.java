package com.feitianzhu.huangliwo.pushshop;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MerchantsEarnRulesInfo;
import com.feitianzhu.huangliwo.pushshop.adapter.SelfMerchantsOrderAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.DatePickDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/17
 * time: 11:27
 * email: 694125155@qq.com
 * 商铺收益细则
 */
public class MyMerchantsEarningsRulesActivity extends BaseActivity implements DatePickDialog.OnPickListener {
    public static final String MERCHANTS_ID = "merchants_id";
    private SelfMerchantsOrderAdapter selfMerchantsOrderAdapter;
    private List<SelfMerchantsModel> selfMerchantsModels = new ArrayList<>();
    private List<MerchantsEarnRulesInfo.MerchantsEarnRulesModel> earnRulesModelList = new ArrayList<>();
    private String userId;
    private String token;
    private String strDate = "";
    private int merchantsId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_earnings_rules;
    }

    @Override
    protected void initView() {
        titleName.setText("收益细则");
        rightText.setText("选择时间");
        rightText.setVisibility(View.VISIBLE);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selfMerchantsOrderAdapter = new SelfMerchantsOrderAdapter(selfMerchantsModels);
        selfMerchantsOrderAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(selfMerchantsOrderAdapter);
        selfMerchantsOrderAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MerchantsEarnRulesInfo>>post(Urls.GET_EARNINGS_RULES)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("merchantId", merchantsId + "")
                .params("type", "")
                .params("date", strDate)
                .execute(new JsonCallback<LzyResponse<MerchantsEarnRulesInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsEarnRulesInfo>> response) {
                        super.onSuccess(MyMerchantsEarningsRulesActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            earnRulesModelList = response.body().data.getList();
                            selfMerchantsModels.clear();
                            for (int i = 0; i < earnRulesModelList.size(); i++) {
                                SelfMerchantsModel merchantsModel = new SelfMerchantsModel(SelfMerchantsModel.RULES_TYPE);
                                merchantsModel.setMerchantsEarnRulesModel(earnRulesModelList.get(i));
                                selfMerchantsModels.add(merchantsModel);
                            }
                            selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                            selfMerchantsOrderAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onStart(Request<LzyResponse<MerchantsEarnRulesInfo>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsEarnRulesInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                String year = DateUtils.getCurrentDateStr("yyyy");
                String month = DateUtils.getCurrentDateStr("MM");
                DatePickDialog pickDialog = DatePickDialog.newInstance();
                pickDialog.setPickTime(year, month);
                pickDialog.setOnPickListener(this);
                pickDialog.show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    public void onWheelFinish(String year, String month) {
        strDate = year + "年" + month + "月";
        initData();
    }
}
