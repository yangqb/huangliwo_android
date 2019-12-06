package com.feitianzhu.fu700.shop.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.shop.adapter.LogisticsAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 物流详情
 * */
public class LogisticsInfoActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.logistics_no)
    TextView logisticsNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logistics_info;
    }

    @Override
    protected void initView() {
        titleName.setText("物流信息");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        LogisticsAdapter adapter = new LogisticsAdapter(list);
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
                ClipData clip = ClipData.newPlainText("simple text", logisticsNo.getText().toString());
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                ToastUtils.showShortToast("已复制");
                break;
        }
    }
}
