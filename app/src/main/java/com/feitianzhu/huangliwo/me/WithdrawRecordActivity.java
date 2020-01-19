package com.feitianzhu.huangliwo.me;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.adapter.WithdrawRecordAdapter;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.WithdrawRecordInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/1/18
 * time: 11:39
 * email: 694125155@qq.com
 */
public class WithdrawRecordActivity extends BaseActivity {
    public static final String MERCHANT_ID = "merchantId";
    private WithdrawRecordAdapter mAdapter;
    private List<WithdrawRecordInfo.WithdrawRecordModel> withdrawRecordModelList = new ArrayList<>();
    private String token;
    private String userId;
    private int merchantId = -1;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_record;
    }

    @Override
    protected void initView() {
        titleName.setText("提现明细");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantId = getIntent().getIntExtra(MERCHANT_ID, -1);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WithdrawRecordAdapter(withdrawRecordModelList);
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (withdrawRecordModelList.get(position).getIsTrans() == 0) {
                    showDialog();
                }
            }
        });
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asConfirm("温馨提示", "确定取消提现吗", "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        cancelWithdraw();
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    public void cancelWithdraw() {
        PostFormBuilder postForm = OkHttpUtils.post()
                .url(Urls.WITHDRAW_CANCEL);
        if (merchantId != -1) {
            postForm.addParams("merchantId", merchantId + "");
        }
        postForm.addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast("取消成功");
                        initData();
                    }
                });
    }

    @Override
    protected void initData() {
        GetBuilder postForm = OkHttpUtils.get().url(Urls.GET_WITHDRAW_RECORD);
        if (merchantId != -1) {
            postForm.addParams("merchantId", merchantId + "");
            postForm.addParams("type", "2");
        } else {
            postForm.addParams("type", "1");
        }
        postForm.addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .build()
                .execute(new Callback<WithdrawRecordInfo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                    }

                    @Override
                    public void onResponse(WithdrawRecordInfo response, int id) {
                        refreshLayout.finishRefresh();
                        withdrawRecordModelList.clear();
                        if (response != null) {
                            withdrawRecordModelList = response.getList();
                            mAdapter.setNewData(withdrawRecordModelList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
