package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.WebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/10
 * time: 20:05
 * email: 694125155@qq.com
 */
public class PushShopHomeActivity extends BaseActivity {
    public static final String MINE_INFO = "mine_info";
    private MineInfoModel userInfo;
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop;
    }

    @Override
    protected void initView() {
        titleName.setText("推店");
        userInfo = (MineInfoModel) getIntent().getSerializableExtra(MINE_INFO);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
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
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(PushShopHomeActivity.this, response.message(), response.body().code);
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                        if (response.body().code == 0 && response.body().data != null) {
                            userInfo = response.body().data;
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.push_merchants, R.id.oneself_merchants})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.push_merchants:
                if (userInfo.getAccountType() != 0) {
                    boolean isAgreed = SPUtils.getBoolean(PushShopHomeActivity.this, Constant.SP_PUSH_SHOP_PROTOCOL);
                    if (isAgreed) {
                        intent = new Intent(PushShopHomeActivity.this, PushShopListActivity.class);
                        startActivity(intent);
                    } else {
                        //推店协议
                        intent = new Intent(PushShopHomeActivity.this, WebActivity.class);
                        intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/tuidianxieyi.html");
                        intent.putExtra(Constant.H5_TITLE, "便利大本营推店协议");
                        intent.putExtra(WebActivity.PUSH_PROTOCOL, true);
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.show("您还不是会员无法推荐店铺");
                }
                break;
            case R.id.oneself_merchants:
                if (userInfo.getIsMerchant() == 1) {
                    intent = new Intent(PushShopHomeActivity.this, MySelfMerchantsActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.show("您的商铺还未被推荐至平台请联系会员推荐");
                }
                break;
        }
    }
}
