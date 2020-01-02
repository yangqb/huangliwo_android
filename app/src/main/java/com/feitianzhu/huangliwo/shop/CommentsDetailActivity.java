package com.feitianzhu.huangliwo.shop;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.shop.adapter.CommentsDetailAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

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
            if (evaluateModeList.size() > 0) { //服务端没法把商品名字放在列表
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

        initListener();

    }

    public void initListener() {
        adapter.setOnChildPositionListener(new CommentsDetailAdapter.OnChildClickListener() {
            @Override
            public void success(int index, int pos) {
                if (evaluateModeList.get(pos).getEvalImgs() != null) {
                    String[] strings = evaluateModeList.get(pos).getEvalImgs().split(",");
                    // 仅需一行代码,默认配置为：
                    //      显示顶部进度指示器、
                    //      显示右侧下载按钮、
                    //      隐藏左侧关闭按钮、
                    //      开启点击图片关闭、
                    //      关闭下拉图片关闭、
                    //      加载方式为手动模式
                    //      加载原图的百分比在底部
                    ImagePreview
                            .getInstance()
                            // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                            .setContext(mContext)
                            .setEnableDragClose(true) //下拉图片关闭
                            // 设置从第几张开始看（索引从0开始）
                            .setIndex(index)
                            .setShowErrorToast(true)//加载失败提示
                            //=================================================================================================
                            // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                            // 1：第一步生成的imageInfo List
                            //.setImageInfoList(imageInfoList)

                            // 2：直接传url List
                            .setImageList(Arrays.asList(strings))

                            // 3：只有一张图片的情况，可以直接传入这张图片的url
                            //.setImage(String image)
                            //=================================================================================================

                            // 开启预览
                            .start();
                }
            }
        });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }
}
