package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
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
    SmartRefreshLayout refreshLayout;

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
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build().execute(new Callback<MineInfoModel>() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                Log.e("wangyan", "onError---->" + e.getMessage());
                ToastUtils.showShortToast(e.getMessage());
            }

            @Override
            public void onResponse(MineInfoModel response, int id) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (response != null) {
                    userInfo = response;
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
                        intent = new Intent(PushShopHomeActivity.this, PushShopProtocolActivity.class);
                        intent.putExtra(PushShopProtocolActivity.PUSH_PROTOCOL, true);
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showShortToast("您还不是会员无法推荐店铺");
                }
                break;
            case R.id.oneself_merchants:
                if (userInfo.getIsMerchant() == 1) {
                    intent = new Intent(PushShopHomeActivity.this, MySelfMerchantsActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("您的商铺还未被推荐至平台请联系会员推荐");
                }
                break;
        }
    }
}
