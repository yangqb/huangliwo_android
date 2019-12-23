package com.feitianzhu.fu700.shop;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
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
    public static final String COMMENTS_DATA = "comments_data";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CommentsDetailAdapter adapter;
    private BaseGoodsListBean goodsListBean;
    private List<BaseGoodsListBean.GoodsEvaluateMode> evaluateModeList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("评价");
        goodsListBean = (BaseGoodsListBean) getIntent().getSerializableExtra(COMMENTS_DATA);
        if (goodsListBean != null && goodsListBean.getEvalList() != null) {
            evaluateModeList.addAll(goodsListBean.getEvalList());
            if(evaluateModeList.size() >0) { //服务端没法把商品名字放在列表
                for (BaseGoodsListBean.GoodsEvaluateMode evaluateMode : evaluateModeList
                ) {
                    evaluateMode.setGoodsName(goodsListBean.getGoodsName());
                }
            }
        }
        View mEmptyView = View.inflate(mContext, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter = new CommentsDetailAdapter(evaluateModeList);
        adapter.setEmptyView(mEmptyView);
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
