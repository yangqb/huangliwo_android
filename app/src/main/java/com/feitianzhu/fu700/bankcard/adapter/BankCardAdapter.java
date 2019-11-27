package com.feitianzhu.fu700.bankcard.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.bankcard.entity.UserBankCardEntity;

import java.util.List;


/**
 * Created by Lee on 2016/6/24.
 */
public class BankCardAdapter extends BaseQuickAdapter<UserBankCardEntity, BaseViewHolder> {

    public BankCardAdapter(List<UserBankCardEntity> data) {
        super(R.layout.item_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, UserBankCardEntity item) {

        holder.setText(R.id.tv_name, item.bankName);
//        holder.setText(R.id.tv_detail, item.detail);
        holder.setText(R.id.tv_number, item.bankCardNo);
//        holder.setText(R.id.tv_type, item.type);
        Glide.with(mContext).load(item.icon)
                .apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai))
                .into(((ImageView) holder.getView(R.id.imageview)));
    }


}
