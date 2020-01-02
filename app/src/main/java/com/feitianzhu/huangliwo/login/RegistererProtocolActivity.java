package com.feitianzhu.huangliwo.login;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.PushShopProtocolAdapter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.login
 * user: yangqinbo
 * date: 2019/12/23
 * time: 17:53
 * email: 694125155@qq.com
 */
public class RegistererProtocolActivity extends BaseActivity {
    private PushShopProtocolAdapter adapter;
    Integer[] integers = new Integer[]{R.mipmap.g11_02xieyi, R.mipmap.g11_03xieyi,R.mipmap.g11_04xieyi};
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_protocol;
    }

    @Override
    protected void initView() {
        titleName.setText("《黄鹂窝优选用户注册协议》");
        adapter = new PushShopProtocolAdapter(Arrays.asList(integers));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }
}
