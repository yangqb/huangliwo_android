package com.feitianzhu.fu700.me;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.adapter.AddressManagementAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.AddressInfo;
import com.feitianzhu.fu700.shop.ShopPayActivity;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    SwipeRefreshLayout mSwipeLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initView() {
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
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        isSelect = getIntent().getBooleanExtra(IS_SELECT, false);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.sf_blue));

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

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

    }

    @OnClick({R.id.left_button, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_text:
                Intent intent = new Intent(AddressManagementActivity.this, EditAddressActivity.class);
                intent.putExtra(EditAddressActivity.IS_ADD_ADDRESS, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

    }

    @Override
    protected void initData() {
        OkHttpUtils.post()
                .url(Urls.GET_ADDRESS)
                .addParams("accessToken", Constant.ACCESS_TOKEN)
                .addParams("userId", Constant.LOGIN_USERID)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, AddressInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        AddressInfo addressInfo = (AddressInfo) response;
                        addressInfos = addressInfo.getShopAddressList();
                        adapter.setNewData(addressInfos);
                        adapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
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
