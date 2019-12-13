package com.feitianzhu.fu700.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.shop.adapter.ProductParametersAdapter;
import com.feitianzhu.fu700.utils.GlideUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 2:37
 * <p>
 * 商品详情页面
 */
public class ShopsDetailActivity extends BaseActivity {
    public static final String GOODS_DETAIL_DATA = "goods_detail_data";
    private String str3 = "0.00";
    private BaseGoodsListBean goodsListBean;
    private List<ProductParameters.GoodslistBean.SkuValueListBean> skuValueListBean = new ArrayList<>();
    private ProductParametersAdapter mAdapter;
    private int valueId = -1; //规格id
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.viewpager)
    BannerViewPager<BaseGoodsListBean.GoodsImgsListBean, ShopsDetailActivity.DataViewHolder> mViewpager;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.goodsSummary)
    TextView goodsSummary;
    @BindView(R.id.detail_img)
    ImageView imgDetail;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.specifications_name)
    TextView specificationsName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shops_detail;
    }

    @Override
    protected void initView() {
        goodsListBean = (BaseGoodsListBean) getIntent().getSerializableExtra(GOODS_DETAIL_DATA);
        titleName.setText("商品详情");
        tvAmount.setText("");
        String str2 = "¥ ";

        if (goodsListBean != null) {
            str3 = String.format(Locale.getDefault(), "%.2f", goodsListBean.getPrice());
            goodsName.setText(goodsListBean.getGoodsName());
            goodsSummary.setText(goodsListBean.getSummary());

            Glide.with(this).load(goodsListBean.getGoodsIntroduceImg())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.pic_fuwutujiazaishibai)
                            .error(R.drawable.pic_fuwutujiazaishibai)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                    .into(GlideUtils.getImageView(this, goodsListBean.getGoodsIntroduceImg(), imgDetail));
        }

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


        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ProductParametersAdapter(skuValueListBean);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (goodsListBean != null && goodsListBean.getGoodsImgsList() != null) {
            List<BaseGoodsListBean.GoodsImgsListBean> bannerList = goodsListBean.getGoodsImgsList();
            mViewpager.setCanLoop(true)
                    .setAutoPlay(true)
                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                    //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                    .setIndicatorRadius(8)
                    .setIndicatorColor(Color.parseColor("#FFFFFF"), Color.parseColor("#6C6D72"))
                    .setHolderCreator(ShopsDetailActivity.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    onClickBanner(position);
                }
            }).create(bannerList);
            mViewpager.startLoop();
        }


        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelect(position);
                mAdapter.notifyDataSetChanged();
                valueId = skuValueListBean.get(position).getValueId();
            }
        });
    }


    @Override
    protected void initData() {
        OkHttpUtils.post()
                .url(Urls.GET_PRODUCT_PARAMETERS)
                .addParams("accessToken", Constant.ACCESS_TOKEN)
                .addParams("userId", Constant.LOGIN_USERID)
                .addParams("goodsId", goodsListBean.getGoodsId() + "")
                .build()
                .execute(new Callback() {

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, ProductParameters.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ProductParameters productParameters = (ProductParameters) response;
                        if (productParameters != null && productParameters.getGoodslist() != null && productParameters.getGoodslist().size() > 0) {
                            specificationsName.setText(productParameters.getGoodslist().get(0).getAttributeName());
                            skuValueListBean = productParameters.getGoodslist().get(0).getSkuValueList();
                            mAdapter.setNewData(skuValueListBean);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /*
     * banner的点击事件
     * */
    public void onClickBanner(int i) {

    }

    public class DataViewHolder implements ViewHolder<BaseGoodsListBean.GoodsImgsListBean> {
        private ImageView mImageView;

        @Override
        public View createView(ViewGroup viewGroup, Context context, int position) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, BaseGoodsListBean.GoodsImgsListBean data, final int position, final int size) {
            Glide.with(context).load(data.getGoodsImg()).apply(new RequestOptions().placeholder(R.drawable.pic_fuwutujiazaishibai).error(R.drawable.pic_fuwutujiazaishibai)).into(mImageView);
        }
    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.rl_more_evaluation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                if (skuValueListBean.size() > 0 && valueId == -1) {
                    ToastUtils.showShortToast("请选择商品规格");
                    return;
                }
                Intent intent = new Intent(ShopsDetailActivity.this, ShopPayActivity.class);
                intent.putExtra(ShopPayActivity.GOODS_VALUE_ID, valueId);
                intent.putExtra(ShopPayActivity.IS_SHOW_ADDRESS, true);
                if (goodsListBean != null) {
                    intent.putExtra(ShopPayActivity.PAY_DATA, goodsListBean);
                }
                startActivity(intent);
                break;
            case R.id.rl_more_evaluation: //更多评论
                intent = new Intent(ShopsDetailActivity.this, CommentsDetailActivity.class);
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
