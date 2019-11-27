package com.feitianzhu.fu700.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 2:37
 */
public class CommodityDetailActivity extends BaseActivity {
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.viewpager)
    BannerViewPager<Integer, CommodityDetailActivity.DataViewHolder> mViewpager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commodity_detail;
    }

    @Override
    protected void initView() {

        titleName.setText("商品详情");
        tvAmount.setText("");
        String str2 = "¥ ";
        String str3 = "188.00";

        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
        span2.setSpan(new AbsoluteSizeSpan(12, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
        span3.setSpan(new AbsoluteSizeSpan(17, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvAmount.append(span2);
        tvAmount.append(span3);

    }

    @Override
    protected void initData() {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            integers.add(i);
        }
        mViewpager.setCanLoop(true)
                .setAutoPlay(true)
                .setIndicatorStyle(IndicatorStyle.CIRCLE)
                //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                .setIndicatorRadius(8)
                .setIndicatorColor(Color.parseColor("#FFFFFF"), Color.parseColor("#6C6D72"))
                .setHolderCreator(CommodityDetailActivity.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                onClickBanner(position);
            }
        }).create(integers);//.create(mBanners);
        mViewpager.startLoop();
    }

    /*
     * banner的点击事件
     * */
    public void onClickBanner(int i) {

    }

    public class DataViewHolder implements ViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(ViewGroup viewGroup, Context context, int position) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, Integer data, final int position, final int size) {
            Glide.with(context).load(R.mipmap.e01_25shangping).into(mImageView);
        }
    }

    @OnClick({R.id.left_button, R.id.tv_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                Intent intent = new Intent(CommodityDetailActivity.this, ShopPayActivity.class);
                intent.putExtra(ShopPayActivity.IS_SHOW_ADDRESS, true);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewpager != null)
            mViewpager.startLoop();
    }
}
