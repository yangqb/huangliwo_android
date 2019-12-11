package com.feitianzhu.fu700.pushshop;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.pushshop.adapter.ReasonAdapter;

import java.util.Arrays;

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
        String[] strings = new String[]{"身份证正面不清晰", "身份证正面不清晰", "身份证正面不清晰", "身份证正面不清晰"};
        adapter = new ReasonAdapter(Arrays.asList(strings));
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
                startActivity(intent);
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
