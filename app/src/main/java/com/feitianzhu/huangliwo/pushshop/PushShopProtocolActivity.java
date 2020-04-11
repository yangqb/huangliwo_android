package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.PushShopProtocolAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static final String PUSH_PROTOCOL = "push_protocol";
    public static final String CHECK_PROTOCOL = "check_protocol";
    private boolean isPushProtocol;
    private boolean isChecked;
    private PushShopProtocolAdapter adapter;
    private List<Integer> integers = new ArrayList<>();
    Integer[] integers1 = new Integer[]{R.mipmap.g05_01xieyi, R.mipmap.g05_02xieyi, R.mipmap.g05_03xieyi, R.mipmap.g05_04xieyi};
    Integer[] integers2 = new Integer[]{R.mipmap.g05_07xieyi, R.mipmap.g05_08xieyi};
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_shop_protocol;
    }

    @Override
    protected void initView() {
        isPushProtocol = getIntent().getBooleanExtra(PUSH_PROTOCOL, false);
        isChecked = getIntent().getBooleanExtra(CHECK_PROTOCOL, false);
        if (isPushProtocol) {
            titleName.setText("便利大本营推店协议");
            integers = Arrays.asList(integers1);
        } else {
            titleName.setText("推店规则和收益说明");
            integers = Arrays.asList(integers2);
            if (isChecked) {
                ll_bottom.setVisibility(View.GONE);
            } else {
                ll_bottom.setVisibility(View.VISIBLE);
            }
        }
        adapter = new PushShopProtocolAdapter(integers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.not_agreed, R.id.agreed})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.not_agreed:
                if (isPushProtocol) {
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_PROTOCOL, false);
                } else {
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS, true);
                }
                finish();
                break;
            case R.id.agreed:
                if (isPushProtocol) {
                    intent = new Intent(PushShopProtocolActivity.this, PushShopListActivity.class);
                    startActivity(intent);
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_PROTOCOL, true);
                    finish();
                } else {
                    intent = new Intent(PushShopProtocolActivity.this, EditMerchantsActivity.class);
                    startActivity(intent);
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS, true);
                    finish();
                }
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }
}
