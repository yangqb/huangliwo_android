package com.feitianzhu.huangliwo.me;

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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.AddressManagementAdapter;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressManagementActivity extends BaseActivity {
    private static final int REQUEST_CODE = 1000;
    public static final String ADDRESS_DATA = "address_data";
    private AddressManagementAdapter adapter;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
    public static final String IS_SELECT = "is_select";
    private boolean isSelect;//是否选择地址
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout mSwipeLayout;
    private String token;
    private String userId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("收货地址");
        rightText.setText("新增");
        rightText.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressManagementAdapter(addressInfos);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter.setEmptyView(mEmptyView);
        adapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        isSelect = getIntent().getBooleanExtra(IS_SELECT, false);
        mSwipeLayout.setEnableLoadMore(false);

        initListener();

    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isSelect) {
                    //选择收货地址
                    Intent intent = new Intent();
                    intent.putExtra(ADDRESS_DATA, addressInfos.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    //修改地址
                    Intent intent = new Intent(AddressManagementActivity.this, EditAddressActivity.class);
                    intent.putExtra(EditAddressActivity.IS_ADD_ADDRESS, false);
                    intent.putExtra(EditAddressActivity.ADDRESS_DATA, addressInfos.get(position));
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //修改地址
                Intent intent = new Intent(AddressManagementActivity.this, EditAddressActivity.class);
                intent.putExtra(EditAddressActivity.IS_ADD_ADDRESS, false);
                intent.putExtra(EditAddressActivity.ADDRESS_DATA, addressInfos.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
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
                Intent intent = new Intent(AddressManagementActivity.this, EditAddressActivity.class);
                intent.putExtra(EditAddressActivity.IS_ADD_ADDRESS, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

    }

    @Override
    protected void initData() {

        OkGo.<LzyResponse<AddressInfo>>post(Urls.GET_ADDRESS)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<AddressInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onSuccess(AddressManagementActivity.this, response.body().msg, response.body().code);
                        mSwipeLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            AddressInfo addressInfo = response.body().data;
                            addressInfos = addressInfo.getShopAddressList();
                            adapter.setNewData(addressInfos);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<AddressInfo>> response) {
                        super.onError(response);
                        super.onError(response);
                        mSwipeLayout.finishRefresh(false);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                initData();
            }
        }
    }
}
