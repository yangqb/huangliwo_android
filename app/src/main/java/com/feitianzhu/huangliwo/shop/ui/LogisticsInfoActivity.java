package com.feitianzhu.huangliwo.shop.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.LogisticsInfo;
import com.feitianzhu.huangliwo.model.LogisticsModel;
import com.feitianzhu.huangliwo.shop.adapter.LogisticsAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 物流详情
 * */
public class LogisticsInfoActivity extends BaseActivity {
    public static final String LOGISTICS_DATA = "logistics_data";
    public static final String LOGISTICS_COMPANY = "logistics_company";
    public static final String ORDER_CREATE_TIME = "order_create_time";
    @BindView(R.id.data)
    LinearLayout data;
    private LogisticsAdapter adapter;
    private LogisticsModel logisticsModel;
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
    @BindView(R.id.ll_logistics)
    LinearLayout llLogistics;
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
        logisticsModel = (LogisticsModel) getIntent().getSerializableExtra(LOGISTICS_DATA);

        logisticsCompany = getIntent().getStringExtra(LOGISTICS_COMPANY);
        titleName.setText("物流信息");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        if (logisticsModel == null) {
            String createTime = getIntent().getStringExtra(ORDER_CREATE_TIME);
            LogisticsInfo logisticsInfo = new LogisticsInfo();
            logisticsInfo.setContext("订单处理中：\n等待库房确认、商品打包、出库");
            logisticsInfo.setFtime(createTime);
            List<LogisticsInfo> logisticsInfos = new ArrayList<>();
            logisticsInfos.add(logisticsInfo);
            adapter = new LogisticsAdapter(logisticsInfos);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        if (logisticsModel != null && logisticsCompany != null && !StringUtils.isEmpty(logisticsModel.getNu()) && !logisticsModel.getNu().equals("null")) {
            tvLogisticsNo.setText("物流编号" + logisticsModel.getNu());
            llLogistics.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            tvCompanyName.setText(logisticsCompany);
            adapter = new LogisticsAdapter(logisticsModel.getData());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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
                ClipData clip = ClipData.newPlainText("simple text", logisticsModel.getNu());
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                ToastUtils.show("已复制");
                break;
        }
    }

}
