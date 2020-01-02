package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.view.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/6
 * time: 15:20
 * email: 694125155@qq.com
 */
public class CommentsDetailAdapter extends BaseQuickAdapter<BaseGoodsListBean.GoodsEvaluateMode, BaseViewHolder> {
    private CommentImgAdapter adapter;
    private CommentsDetailAdapter.OnChildClickListener onItemClickListener;

    public interface OnChildClickListener {
        //成功的方法传 int 的索引
        void success(int index, int pos);
    }

    public void setOnChildPositionListener(CommentsDetailAdapter.OnChildClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
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
                .apply(new RequestOptions().placeholder(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang)).into((CircleImageView) helper.getView(R.id.iv_head));
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);

        List<String> imgs = new ArrayList<>();
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
                onItemClickListener.success(position, helper.getAdapterPosition());
            }
        });
    }
}
