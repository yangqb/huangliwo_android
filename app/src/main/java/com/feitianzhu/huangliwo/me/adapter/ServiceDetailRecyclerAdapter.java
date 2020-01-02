package com.feitianzhu.huangliwo.me.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class ServiceDetailRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
  public ServiceDetailRecyclerAdapter(@Nullable List<String> data) {
    super(R.layout.service_detail_recycler_item, data);
  }

  @Override protected void convert(BaseViewHolder holder, String item) {
       ImageView iv =  holder.getView(R.id.iv_photos);
    Glide.with(mContext).load(item).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
            .into(iv);
  }
}
