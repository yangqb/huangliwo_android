package com.feitianzhu.huangliwo.me.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.PurchaseInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class PurchaseInfoAdapter extends BaseQuickAdapter<PurchaseInfoModel, BaseViewHolder> {
  public PurchaseInfoAdapter(@Nullable List<PurchaseInfoModel> data) {
    super(R.layout.purchaseinfo_recycler_item, data);
  }

  @Override protected void convert(BaseViewHolder mBaseViewHolder, PurchaseInfoModel item) {
    mBaseViewHolder.setText(R.id.tv_content,item.getContent());
  }
}
