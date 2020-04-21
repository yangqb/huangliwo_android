package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.ReasonAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 17:33
 * email: 694125155@qq.com
 */
public class NoPassReasonActivity extends BaseActivity {
    public static final String REASON_DATA = "reason_data";
    private MerchantsModel merchantsModel;
    private ReasonAdapter adapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nopass_reason;
    }

    @Override
    protected void initView() {
        titleName.setText("详情");
        rightImg.setBackgroundResource(R.mipmap.g01_08fankui);
        rightImg.setVisibility(View.VISIBLE);
        merchantsModel = (MerchantsModel) getIntent().getSerializableExtra(REASON_DATA);
        List<String> strings = new ArrayList<>();
        strings.add(merchantsModel.getRefuseReason());
        adapter = new ReasonAdapter(strings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.reSubmit, R.id.right_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.reSubmit:
                intent = new Intent(NoPassReasonActivity.this, EditMerchantsActivity.class);
                intent.putExtra(EditMerchantsActivity.MERCHANTS_DETAIL_DATA, merchantsModel);
                startActivity(intent);
                finish();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                intent = new Intent(NoPassReasonActivity.this, ProblemFeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}
