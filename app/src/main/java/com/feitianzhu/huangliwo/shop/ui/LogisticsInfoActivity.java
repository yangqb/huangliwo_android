package com.feitianzhu.huangliwo.shop.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.LogisticsInfo;
import com.feitianzhu.huangliwo.model.LogisticsModel;
import com.feitianzhu.huangliwo.shop.adapter.LogisticsAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 物流详情
 * */
public class LogisticsInfoActivity extends BaseActivity {
    public static final String LOGISTICS_DATA = "logistics_data";
    public static final String LOGISTICS_COMPANY = "logistics_company";
    private LogisticsAdapter adapter;
    private String logisticsNo = "";
    private String logisticsCompany = "";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.logistics_no)
    TextView tvLogisticsNo;
    @BindView(R.id.companyName)
    TextView tvCompanyName;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.line)
    View line;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logistics_info;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        LogisticsModel logisticsModel = (LogisticsModel) getIntent().getSerializableExtra(LOGISTICS_DATA);

        logisticsCompany = getIntent().getStringExtra(LOGISTICS_COMPANY);
        titleName.setText("物流信息");
        tvLogisticsNo.setText("物流编号" + logisticsModel.getNu());
        tvCopy.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        if (logisticsCompany != null) {
            tvCompanyName.setText(logisticsCompany);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new LogisticsAdapter(logisticsModel.getData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.tv_copy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_copy:
                //获取剪贴版
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                //第一个参数只是一个标记，随便传入。
                //第二个参数是要复制到剪贴版的内容
                ClipData clip = ClipData.newPlainText("simple text", logisticsNo);
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                ToastUtils.show("已复制");
                break;
        }
    }
}
