package com.feitianzhu.fu700.pushshop;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.pushshop.adapter.PushShopProtocolAdapter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 20:03
 * email: 694125155@qq.com
 */
public class PushShopProtocolActivity extends BaseActivity {
    private PushShopProtocolAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop_protocol;
    }

    @Override
    protected void initView() {
        titleName.setText("黄鹂窝优选推店协议");
        Integer[] integers = new Integer[]{R.mipmap.g05_01xieyi, R.mipmap.g05_02xieyi, R.mipmap.g05_03xieyi};
        adapter = new PushShopProtocolAdapter(Arrays.asList(integers));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick(View view) {
        finish();
    }

}
