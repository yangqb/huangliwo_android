package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneDetailActivity extends BaseActivity {
    public static final String DETAIL_TYPE = "detail_type";
    private PlaneDetailAdapter mAdapter;
    private int type;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.layout_top1)
    LinearLayout layoutTop1;
    @BindView(R.id.layout_top2)
    LinearLayout layoutTop2;
    @BindView(R.id.btn_title1)
    TextView btnTitle1;
    @BindView(R.id.btn_title2)
    TextView btnTitle2;
    @BindView(R.id.prompt_content)
    TextView promptContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_flight_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("");
        type = getIntent().getIntExtra(DETAIL_TYPE, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(i + "");
        }
        mAdapter = new PlaneDetailAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        if (type == 1) {
            //单程
            layoutTop1.setVisibility(View.VISIBLE);
            promptContent.setText("【机场提示】该航班到达机场为北京大兴国际机场，距市区约46公里，搭乘地铁到市区约30分钟。");
        } else if (type == 2) {
            //往返
            layoutTop2.setVisibility(View.VISIBLE);
            btnTitle1.setText("去程");
            btnTitle2.setText("返程");
            promptContent.setText("【风险提示】此报价是组合产品，如遇其中一程航班调整，您可办理另一程退改事宜，需自行承担相应费用，详见退改详情。");
        } else if (type == 3) {
            //中转
            layoutTop2.setVisibility(View.VISIBLE);
            btnTitle1.setText("一程");
            btnTitle2.setText("二程");
            String str1 = "如遇其中一程航班调整，您需自行办理另一程退改事宜并承担相应费用。请阅读";
            String str2 = "中转预定须知";
            setSpannableString(str1, str2, type);
        } else if (type == 4) {
            //国际
            layoutTop2.setVisibility(View.VISIBLE);
            btnTitle1.setText("一程");
            btnTitle2.setText("二程");
            String str3 = "【托运行李提示】";
            String str4 = "中转深圳，需在机场重新托运行李";
            setSpannableString(str4, str3, type);
        }

        initListener();
    }

    public void initListener() {

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PlaneDetailActivity.this, EditPlaneReserveActivity.class);
                intent.putExtra(EditPlaneReserveActivity.PLANE_TYPE, type);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str1, String str2, int type) {
        promptContent.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#28B5FE"));
        span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan3, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        if (type == 3) {
            promptContent.append(span1);
            promptContent.append(span2);
        } else if (type == 4) {
            promptContent.append(span2);
            promptContent.append(span1);
        }
    }
}
