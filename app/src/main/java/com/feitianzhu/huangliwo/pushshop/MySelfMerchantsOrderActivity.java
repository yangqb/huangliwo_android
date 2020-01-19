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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.MerchantsEarnRulesInfo;
import com.feitianzhu.huangliwo.pushshop.adapter.SelfMerchantsOrderAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.just.agentweb.ActionActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

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
    private PopupWindow popupWindow;
    private View vPopupWindow;
    private String userId;
    private String token;
    private int merchantsId = -1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_merchacts_order;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("订单列表");
        rightText.setText("快速录单");
        rightImg.setBackgroundResource(R.mipmap.a01_04jia);
        rightImg.setVisibility(View.VISIBLE);
        rightText.setVisibility(View.VISIBLE);
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
        OkHttpUtils.post()
                .url(Urls.GET_EARNINGS_RULES)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("merchantId", merchantsId + "")
                .addParams("type", "setMeal")
                .addParams("date", "")
                .build()
                .execute(new Callback<MerchantsEarnRulesInfo>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                    }

                    @Override
                    public void onResponse(MerchantsEarnRulesInfo response, int id) {
                        refreshLayout.finishRefresh();
                        if (response != null) {
                            earnRulesModelList = response.getList();
                            selfMerchantsModels.clear();
                            for (int i = 0; i < earnRulesModelList.size(); i++) {
                                SelfMerchantsModel merchantsModel = new SelfMerchantsModel(SelfMerchantsModel.ORDER_TYPE);
                                merchantsModel.setMerchantsEarnRulesModel(earnRulesModelList.get(i));
                                selfMerchantsModels.add(merchantsModel);
                            }
                            selfMerchantsOrderAdapter.setNewData(selfMerchantsModels);
                            selfMerchantsOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                vPopupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popupWindow.showAsDropDown(rightText, -vPopupWindow.getMeasuredWidth() + rightText.getWidth(), 0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRecordOrder:
                Intent intent = new Intent(MySelfMerchantsOrderActivity.this, RecordOrderActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.ivScanQRCode:
                requestPermission();
                popupWindow.dismiss();
                break;
        }
    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CAMERA
                )
                .callback(
                        new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                                AndPermission.rationaleDialog(App.getAppContext(), rationale).show();
                            }
                        }
                )
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent(MySelfMerchantsOrderActivity.this, ScannerActivity.class);
                intent.putExtra(ScannerActivity.IS_MERCHANTS, 2);
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(MySelfMerchantsOrderActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
