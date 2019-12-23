package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.shinichi.library.ImagePreview;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/6
 * time: 15:20
 * email: 694125155@qq.com
 */
public class CommentsDetailAdapter extends BaseQuickAdapter<BaseGoodsListBean.GoodsEvaluateMode, BaseViewHolder> {
    private CommentImgAdapter adapter;
    private List<String> imgs = new ArrayList<>();

    public CommentsDetailAdapter(@Nullable List<BaseGoodsListBean.GoodsEvaluateMode> data) {
        super(R.layout.commodity_valuate_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseGoodsListBean.GoodsEvaluateMode item) {
        helper.setText(R.id.userName, item.getNickName());
        helper.setText(R.id.tvContent, item.getContent());
        helper.setText(R.id.tvDate, item.getEvalDate());
        helper.setText(R.id.specifications, item.getNorms() + "/" + item.getGoodsName());
        Glide.with(mContext).load(item.getHeadImg())
                .apply(new RequestOptions().placeholder(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang)).into((RoundedImageView) helper.getView(R.id.iv_head));
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);


        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setNestedScrollingEnabled(false);
        if (item.getEvalImgs() != null) {
            String[] strings = item.getEvalImgs().split(",");
            imgs = Arrays.asList(strings);
        }
        adapter = new CommentImgAdapter(imgs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                        .setIndex(position)
                        .setShowErrorToast(true)//加载失败提示
                        //=================================================================================================
                        // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                        // 1：第一步生成的imageInfo List
                        //.setImageInfoList(imageInfoList)

                        // 2：直接传url List
                        .setImageList(imgs)

                        // 3：只有一张图片的情况，可以直接传入这张图片的url
                        //.setImage(String image)
                        //=================================================================================================

                        // 开启预览
                        .start();
            }
        });
    }
}
