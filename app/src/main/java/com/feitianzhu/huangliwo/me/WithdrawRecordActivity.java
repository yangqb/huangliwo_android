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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.WithdrawRecordAdapter;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.WithdrawRecordInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
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

        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.WITHDRAW_CANCEL)
                .tag(this);
        if (merchantId != -1) {
            postRequest.params("merchantId", merchantId + "");
        }
        postRequest.params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(WithdrawRecordActivity.this, response.body().msg, response.body().code);
                        if(response.body().code==0){
                            ToastUtils.showShortToast("取消成功");
                            initData();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    @Override
    protected void initData() {
        GetRequest<LzyResponse<WithdrawRecordInfo>> getRequest = OkGo.<LzyResponse<WithdrawRecordInfo>>get(Urls.GET_WITHDRAW_RECORD)
                .tag(this);
        if (merchantId != -1) {
            getRequest.params("merchantId", merchantId + "");
            getRequest.params("type", "2");
        } else {
            getRequest.params("type", "1");
        }
        getRequest.params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<WithdrawRecordInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<WithdrawRecordInfo>> response) {
                        super.onSuccess(WithdrawRecordActivity.this, response.body().msg, response.body().code);
                        withdrawRecordModelList.clear();
                        refreshLayout.finishRefresh();
                        if (response.body().data != null && response.body().code == 0) {
                            withdrawRecordModelList = response.body().data.getList();
                            mAdapter.setNewData(withdrawRecordModelList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<WithdrawRecordInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
