package com.feitianzhu.fu700.shop.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 6:48
 */
public class ShopDetailAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    public ShopDetailAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.shop_detail_between_item);
        addItemType(MultipleItem.IMG, R.layout.commodity_valuate_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                // helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                //helper.setImageUrl(R.id.iv, item.getContent());
                List<Integer> integers = new ArrayList<>();
                RecyclerView recyclerView = helper.getView(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                for (int i = 0; i < 6; i++) {
                    integers.add(i);
                }
                List<String> imageList = new ArrayList<>();
                imageList.add("http://g.hiphotos.baidu.com/image/pic/item/6d81800a19d8bc3e770bd00d868ba61ea9d345f2.jpg");
                imageList.add("http://a.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a292d2472199d25bc315d607c7c.jpg");
                imageList.add("http://h.hiphotos.baidu.com/image/pic/item/902397dda144ad340668b847d4a20cf430ad851e.jpg");
                imageList.add("http://d.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f119945cbe2fe9925bc317d2a.jpg");
                imageList.add("http://a.hiphotos.baidu.com/image/pic/item/e824b899a9014c087eb617650e7b02087af4f464.jpg");
                imageList.add("http://b.hiphotos.baidu.com/image/pic/item/e824b899a9014c08878b2c4c0e7b02087af4f4a3.jpg");
                recyclerView.setNestedScrollingEnabled(false);
                CommentImgAdapter adapter = new CommentImgAdapter(integers);
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
                                .setImageList(imageList)

                                // 3：只有一张图片的情况，可以直接传入这张图片的url
                                //.setImage(String image)
                                //=================================================================================================

                                // 开启预览
                                .start();
                    }
                });

                break;
        }
    }
}
