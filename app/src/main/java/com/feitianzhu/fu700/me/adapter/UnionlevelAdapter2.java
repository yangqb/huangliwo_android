package com.feitianzhu.fu700.me.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.FuFriendModel;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.me.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 5:55
 */
public class UnionlevelAdapter2 extends BaseQuickAdapter<FuFriendModel.ListEntity, BaseViewHolder> {
    public UnionlevelAdapter2(@Nullable List<FuFriendModel.ListEntity> data) {
        super(R.layout.activity_unionlevel_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FuFriendModel.ListEntity item) {
        helper.setText(R.id.name, item.nickName);
        helper.setText(R.id.phone, item.phone);
        helper.addOnClickListener(R.id.call_phone);
    }
}
