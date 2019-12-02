package com.feitianzhu.fu700.me;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.adapter.AddressManagementAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressManagementActivity extends BaseActivity {

    private AddressManagementAdapter adapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initView() {
        titleName.setText("收货地址");
        rightText.setText("新增");
        rightText.setVisibility(View.VISIBLE);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressManagementAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        initListener();

    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(AddressManagementActivity.this, EditAddressActivity.class);
                intent.putExtra(EditAddressActivity.IS_ADD_ADDRESS, false);
                startActivity(intent);
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
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void initData() {

    }
}
