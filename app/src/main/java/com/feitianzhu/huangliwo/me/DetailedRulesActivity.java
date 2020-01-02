package com.feitianzhu.huangliwo.me;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.adapter.DetailedRulesAdapter;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.UserGoodVo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 3:24
 */
public class DetailedRulesActivity extends BaseActivity {
    private DetailedRulesAdapter adapter;
    private List<UserGoodVo.ReslutBean> resultBeans = new ArrayList<>();
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_bonus)
    TextView btnBonus;
    @BindView(R.id.btn_discount)
    TextView btnDiscount;
    @BindView(R.id.btn_earnings)
    TextView btnEarnings;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private String userId;
    private int type = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed_rules;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("细则");
        btnBonus.setSelected(true);
        btnDiscount.setSelected(false);
        btnEarnings.setSelected(false);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter = new DetailedRulesAdapter(resultBeans);
        adapter.setEmptyView(mEmptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @OnClick({R.id.left_button, R.id.btn_bonus, R.id.btn_discount, R.id.btn_earnings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_bonus:
                type = 1;
                btnBonus.setSelected(true);
                btnDiscount.setSelected(false);
                btnEarnings.setSelected(false);
                initData();
                break;
            case R.id.btn_discount:
                type = 2;
                btnBonus.setSelected(false);
                btnDiscount.setSelected(true);
                btnEarnings.setSelected(false);
                initData();
                break;
            case R.id.btn_earnings:
                ToastUtils.showShortToast("敬请期待");
                btnBonus.setSelected(false);
                btnDiscount.setSelected(false);
                btnEarnings.setSelected(true);
                type = 3;
                resultBeans.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void initData() {
        if (type == 3) {
            resultBeans.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        } else {
            OkHttpUtils.get()
                    .url(Urls.GET_DETAIL_RULES)
                    .addParams(Constant.ACCESSTOKEN, token)
                    .addParams(Constant.USERID, userId)
                    .addParams("type", type + "")
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                            return new Gson().fromJson(mData, UserGoodVo.class);
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            refreshLayout.finishRefresh(false);
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            refreshLayout.finishRefresh();
                            UserGoodVo userGoodVo = (UserGoodVo) response;
                            if (userGoodVo.getReslut() != null) {
                                resultBeans = userGoodVo.getReslut();
                                adapter.setNewData(resultBeans);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}
