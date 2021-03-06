package com.feitianzhu.huangliwo.pushshop;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.MerchantGiftOrderInfo;
import com.feitianzhu.huangliwo.model.MerchantGiftOrderModel;
import com.feitianzhu.huangliwo.model.MerchantsEarnRulesInfo;
import com.feitianzhu.huangliwo.pushshop.adapter.SelfMerchantsOrderAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/1/3
 * time: 16:45
 * email: 694125155@qq.com
 */
public class MySelfMerchantsOrderActivity extends BaseActivity implements View.OnClickListener {
    public static final String MERCHANTS_ID = "merchants_id";
    private SelfMerchantsOrderAdapter selfMerchantsOrderAdapter;
    private List<SelfMerchantsModel> selfMerchantsModels = new ArrayList<>();
    private List<MerchantsEarnRulesInfo.MerchantsEarnRulesModel> earnRulesModelList = new ArrayList<>();
    private List<MerchantGiftOrderModel> merchantGiftOrderModelList = new ArrayList<>();
    String[] strings = new String[]{"套餐订单", "赠品订单"};
    private PopupWindow popupWindow;
    private View vPopupWindow;
    private String userId;
    private String token;
    private int merchantsId = -1;
    private int selectPos = 0;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_merchacts_order;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText(strings[0]);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);

        vPopupWindow = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        // View vPopupWindow = View.inflate(getActivity(), R.layout.layout_popupwindow, null);//引入弹窗布局
        popupWindow = new PopupWindow(vPopupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        vPopupWindow.findViewById(R.id.ivScanQRCode).setOnClickListener(this);
        vPopupWindow.findViewById(R.id.ivRecordOrder).setOnClickListener(this);

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
        selfMerchantsOrderAdapter.getEmptyView().setVisibility(View.INVISIBLE);
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
        /*selfMerchantsOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == SelfMerchantsModel.ORDER_TYPE) {
                    Intent intent = new Intent(MySelfMerchantsOrderActivity.this, RecordOrderActivity.class);
                    startActivity(intent);
                }
            }
        });*/
    }

    @Override
    protected void initData() {
        if (selectPos == 0) {
            OkGo.<LzyResponse<MerchantsEarnRulesInfo>>post(Urls.GET_EARNINGS_RULES)
                    .tag(this)
                    .params("accessToken", token)
                    .params("userId", userId)
                    .params("merchantId", merchantsId + "")
                    .params("type", "setMeal")
                    .params("date", "")
                    .execute(new JsonCallback<LzyResponse<MerchantsEarnRulesInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<MerchantsEarnRulesInfo>> response) {
                            super.onSuccess(MySelfMerchantsOrderActivity.this, response.body().msg, response.body().code);
                            refreshLayout.finishRefresh();
                            selfMerchantsModels.clear();
                            if (response.body().code == 0 && response.body().data != null) {
                                earnRulesModelList = response.body().data.getList();
                                for (int i = 0; i < earnRulesModelList.size(); i++) {
                                    SelfMerchantsModel merchantsModel = new SelfMerchantsModel(SelfMerchantsModel.ORDER_TYPE);
                                    merchantsModel.setMerchantsEarnRulesModel(earnRulesModelList.get(i));
                                    selfMerchantsModels.add(merchantsModel);
                                }
                                selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                            }
                            selfMerchantsOrderAdapter.notifyDataSetChanged();
                            selfMerchantsOrderAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Response<LzyResponse<MerchantsEarnRulesInfo>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                            selfMerchantsModels.clear();
                            selfMerchantsOrderAdapter.notifyDataSetChanged();

                        }
                    });
        } else {
            OkGo.<LzyResponse<MerchantGiftOrderInfo>>get(Urls.GET_GIFT_ORDER_LIST)
                    .tag(this)
                    .params("accessToken", token)
                    .params("userId", userId)
                    .params("merchantId", merchantsId + "")
                    .execute(new JsonCallback<LzyResponse<MerchantGiftOrderInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<MerchantGiftOrderInfo>> response) {
                            super.onSuccess(MySelfMerchantsOrderActivity.this, response.body().msg, response.body().code);
                            refreshLayout.finishRefresh();
                            selfMerchantsModels.clear();
                            if (response.body().code == 0 && response.body().data != null) {
                                merchantGiftOrderModelList = response.body().data.all;
                                for (int i = 0; i < merchantGiftOrderModelList.size(); i++) {
                                    SelfMerchantsModel merchantsModel = new SelfMerchantsModel(SelfMerchantsModel.GIFT_ORDER_TYPE);
                                    merchantsModel.setMerchantGiftOrderModel(merchantGiftOrderModelList.get(i));
                                    selfMerchantsModels.add(merchantsModel);
                                }
                                selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                            }
                            selfMerchantsOrderAdapter.notifyDataSetChanged();
                            selfMerchantsOrderAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Response<LzyResponse<MerchantGiftOrderInfo>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                            selfMerchantsModels.clear();
                            selfMerchantsOrderAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.title_name})
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                vPopupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popupWindow.showAsDropDown(rightText, -vPopupWindow.getMeasuredWidth() + rightText.getWidth(), 0);
                break;
            case R.id.title_name:
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(MySelfMerchantsOrderActivity.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        titleName.setText(strings[position]);
                                        selectPos = position;
                                        initData();
                                    }
                                }))
                        .show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRecordOrder:
                if (selectPos == 0) {
                    Intent intent = new Intent(MySelfMerchantsOrderActivity.this, RecordOrderActivity.class);
                    intent.putExtra(RecordOrderActivity.TYPE, "1");
                    intent.putExtra(RecordOrderActivity.MERCHANTS_ID, merchantsId + "");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MySelfMerchantsOrderActivity.this, RecordOrderActivity.class);
                    intent.putExtra(RecordOrderActivity.TYPE, "2");
                    intent.putExtra(RecordOrderActivity.MERCHANTS_ID, merchantsId + "");
                    startActivity(intent);
                }

                popupWindow.dismiss();
                break;
            case R.id.ivScanQRCode:
                requestPermission();
                popupWindow.dismiss();
                break;
        }
    }

    private void requestPermission() {

        XXPermissions.with(MySelfMerchantsOrderActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Intent intent = new Intent(MySelfMerchantsOrderActivity.this, ScannerActivity.class);
                            intent.putExtra(ScannerActivity.IS_MERCHANTS, 1);
                            startActivity(intent);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }
}
