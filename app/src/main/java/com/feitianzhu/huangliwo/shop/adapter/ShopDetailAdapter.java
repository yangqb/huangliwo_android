package com.feitianzhu.huangliwo.shop.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.MultipleMerchantsItem;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cc.shinichi.library.ImagePreview;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 6:48
 */
public class ShopDetailAdapter extends BaseMultiItemQuickAdapter<MultipleMerchantsItem, BaseViewHolder> {

    private ShopDetailAdapter.OnChildClickListener onItemClickListener;

    public interface OnChildClickListener {
        //成功的方法传 int 的索引
        void success(int index, int pos);
    }

    public void setOnChildPositionListener(ShopDetailAdapter.OnChildClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ShopDetailAdapter(List<MultipleMerchantsItem> data) {
        super(data);
        addItemType(MultipleMerchantsItem.SETMEAL_TYPE, R.layout.shop_detail_between_item);
        addItemType(MultipleMerchantsItem.COMMENTS_TYPE, R.layout.commodity_valuate_item);
        addItemType(MultipleMerchantsItem.GIFT_TYPE, R.layout.commodity_gift_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleMerchantsItem item) {
        switch (helper.getItemViewType()) {
            case MultipleMerchantsItem.SETMEAL_TYPE:
                helper.setText(R.id.tvSetMealName, item.getSetMealInfo().getSmName());
                helper.setText(R.id.setMealDescription, item.getSetMealInfo().getRemark());
                setSpannableString(String.format(Locale.getDefault(), "%.2f", item.getSetMealInfo().getPrice()), helper.getView(R.id.setMealPrice));
                String[] imgUrls = item.getSetMealInfo().getImgs().split(",");
                if (item.getSetMealInfo().getImgs().contains(",")) {
                    Glide.with(mContext).load(imgUrls[0])
                            .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(item.getSetMealInfo().getImgs())
                            .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
                }

                break;
            case MultipleMerchantsItem.COMMENTS_TYPE:
                Glide.with(mContext).load(item.getEvalDetailModel().getHeadImg())
                        .apply(new RequestOptions().placeholder(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate()).into((CircleImageView) helper.getView(R.id.iv_head));
                helper.setText(R.id.userName, item.getEvalDetailModel().getNickName());
                helper.setText(R.id.tvContent, item.getEvalDetailModel().getContent());
                helper.setText(R.id.tvDate, item.getEvalDetailModel().getEvalDate());
                RecyclerView recyclerView = helper.getView(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                recyclerView.setNestedScrollingEnabled(false);
                List<String> imgs = new ArrayList<>();
                if (item.getEvalDetailModel().getImgs() != null) {
                    String[] strings = (item.getEvalDetailModel().getImgs().split(","));
                    imgs = Arrays.asList(strings);
                }
                CommentImgAdapter adapter = new CommentImgAdapter(imgs);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        onItemClickListener.success(position, helper.getAdapterPosition());
                    }
                });
                break;
            case MultipleMerchantsItem.GIFT_TYPE:
                helper.addOnClickListener(R.id.button);
                helper.setText(R.id.giftName, item.getGifModel().giftName);
                helper.setText(R.id.giftPrice, MathUtils.subZero(String.valueOf(item.getGifModel().price)));
                if (item.getGifModel().isGet == 0) {
                    helper.setBackgroundRes(R.id.button, R.drawable.shape_fed428_r5);
                    helper.setText(R.id.button, "领取");
                    helper.setTextColor(R.id.button, mContext.getResources().getColor(R.color.color_333333));
                } else {
                    helper.setText(R.id.button, "已领取");
                    helper.setBackgroundRes(R.id.button, R.drawable.shape_999999_r5);
                    helper.setTextColor(R.id.button, mContext.getResources().getColor(R.color.white));
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(16, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
