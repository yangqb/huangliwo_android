package com.feitianzhu.huangliwo.me;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.DetailedRulesAdapter;
import com.feitianzhu.huangliwo.model.UserGoodVo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    RefreshLayout refreshLayout;
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
        adapter.getEmptyView().setVisibility(View.INVISIBLE);
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
                resultBeans.clear();
                adapter.notifyDataSetChanged();
                initData();
                break;
            case R.id.btn_discount:
                type = 2;
                btnBonus.setSelected(false);
                btnDiscount.setSelected(true);
                btnEarnings.setSelected(false);
                resultBeans.clear();
                adapter.notifyDataSetChanged();
                initData();
                break;
            case R.id.btn_earnings:
                ToastUtils.show("敬请期待");
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
            OkGo.<LzyResponse<UserGoodVo>>get(Urls.GET_DETAIL_RULES)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("type", type + "")
                    .execute(new JsonCallback<LzyResponse<UserGoodVo>>() {
                        @Override
                        public void onStart(Request<LzyResponse<UserGoodVo>, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<UserGoodVo>> response) {
                            super.onSuccess(DetailedRulesActivity.this, response.body().msg, response.body().code);
                            goneloadDialog();
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().data != null) {
                                UserGoodVo userGoodVo = response.body().data;
                                if (userGoodVo.getReslut() != null) {
                                    resultBeans = userGoodVo.getReslut();
                                    adapter.setNewData(resultBeans);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            adapter.getEmptyView().setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<LzyResponse<UserGoodVo>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                            goneloadDialog();
                        }
                    });
        }
    }
}
