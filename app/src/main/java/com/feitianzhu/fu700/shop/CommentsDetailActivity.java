package com.feitianzhu.fu700.shop;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.MultipleItem;
import com.feitianzhu.fu700.shop.adapter.CommentsDetailAdapter;
import com.feitianzhu.fu700.shop.adapter.ShopDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.shop
 * user: yangqinbo
 * date: 2019/12/6
 * time: 15:12
 * email: 694125155@qq.com
 */
public class CommentsDetailActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CommentsDetailAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("评价");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        adapter = new CommentsDetailAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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
