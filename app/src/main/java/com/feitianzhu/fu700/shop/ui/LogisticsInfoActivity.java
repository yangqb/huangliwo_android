package com.feitianzhu.fu700.shop.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.LogisticsModel;
import com.feitianzhu.fu700.shop.adapter.LogisticsAdapter;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
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

/*
 * 物流详情
 * */
public class LogisticsInfoActivity extends BaseActivity {
    public static final String LOGISTICS_NO = "logistics_no";
    public static final String LOGISTICS_COMPANY = "logistics_company";
    private List<LogisticsModel.DataBean> logisticsModes = new ArrayList<>();
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
        logisticsNo = getIntent().getStringExtra(LOGISTICS_NO);
        logisticsCompany = getIntent().getStringExtra(LOGISTICS_COMPANY);
        titleName.setText("物流信息");
        if (logisticsNo != null) {
            tvLogisticsNo.setText("物流编号" + logisticsNo);
            tvCopy.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        } else {
            tvLogisticsNo.setText("暂无物流信息");
            tvCopy.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if (logisticsCompany != null) {
            tvCompanyName.setText(logisticsCompany);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new LogisticsAdapter(logisticsModes);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        if (logisticsNo != null) {
            OkHttpUtils.get()
                    .url(Urls.GET_LOGISTICS_INFO)
                    .addParams(Constant.ACCESSTOKEN, token)
                    .addParams(Constant.USERID,userId)
                    .addParams("expressNo", logisticsNo)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                            return new Gson().fromJson(mData, LogisticsModel.class);
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(e.getMessage());
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            LogisticsModel logisticsModel = (LogisticsModel) response;
                            if (logisticsModel != null) {
                                logisticsModes.clear();
                                logisticsModes.addAll(logisticsModel.getData());
                                adapter.setNewData(logisticsModes);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
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
                ToastUtils.showShortToast("已复制");
                break;
        }
    }
}
