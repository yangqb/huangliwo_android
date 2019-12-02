package com.feitianzhu.fu700.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 7:51
 */
public class CommentImgAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public CommentImgAdapter(List<Integer> list) {
        super(R.layout.comment_img_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {

    }


}
