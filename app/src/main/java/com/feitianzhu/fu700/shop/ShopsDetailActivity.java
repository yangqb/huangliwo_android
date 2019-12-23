package com.feitianzhu.fu700.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.shop.ui.ShoppingCartActivity;
import com.feitianzhu.fu700.utils.GlideUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.view.CustomSpecificationDialog;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
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
    private StringBuffer sb;
    private StringBuffer valueId;
    private String str3 = "0.00";
    private BaseGoodsListBean goodsListBean;
    private ProductParameters productParameters;
    private List<BaseGoodsListBean.GoodsEvaluateMode> evalList = new ArrayList<>();
    private List<ProductParameters.GoodsSpecifications> specifications = new ArrayList<>();
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
    @BindView(R.id.select_specifications)
    TextView specificationsName;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R.id.ll_specifications)
    LinearLayout llSpecifications;
    @BindView(R.id.specifications)
    TextView evaSpecifications;


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

            evalList = goodsListBean.getEvalList();
            if (evalList != null && evalList.size() > 0) {
                llEvaluate.setVisibility(View.VISIBLE);
                tvCount.setText("评价(" + evalList.size() + ")");
                Glide.with(this).load(evalList.get(0).getHeadImg()).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into(ivHead);
                userName.setText(evalList.get(0).getNickName());
                tvDate.setText(evalList.get(0).getEvalDate());
                tvContent.setText(evalList.get(0).getContent());
                evaSpecifications.setText(evalList.get(0).getNorms() + "/" + goodsListBean.getGoodsName());
            } else {
                llEvaluate.setVisibility(View.GONE);
                tvCount.setText("评价(0)");
            }


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
                        productParameters = (ProductParameters) response;
                        /*
                         * 数据处理
                         * */
                        if (productParameters != null && productParameters.getGoodslist() != null && productParameters.getGoodslist().size() > 0) {
                            llSpecifications.setVisibility(View.VISIBLE);
                            specifications = productParameters.getGoodslist();
                        } else {
                            llSpecifications.setVisibility(View.GONE);
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
            Glide.with(context).load(data.getGoodsImg()).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImageView);
        }
    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.rl_more_evaluation, R.id.add_shopping_cart, R.id.shopping_cart, R.id.share, R.id.collect, R.id.select_specifications})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                if (specifications.size() > 0 && (sb == null || TextUtils.isEmpty(sb.toString()))) {
                    ToastUtils.showShortToast("请选择商品规格");
                    return;
                }
                Intent intent = new Intent(ShopsDetailActivity.this, ShopPayActivity.class);
                intent.putExtra(ShopPayActivity.GOODS_VALUE_ID, valueId.toString());
                intent.putExtra(ShopPayActivity.IS_SHOW_ADDRESS, true);
                if (goodsListBean != null) {
                    intent.putExtra(ShopPayActivity.PAY_DATA, goodsListBean);
                }
                startActivity(intent);
                break;
            case R.id.rl_more_evaluation: //更多评论
                intent = new Intent(ShopsDetailActivity.this, CommentsDetailActivity.class);
                if (goodsListBean != null) {
                    intent.putExtra(CommentsDetailActivity.COMMENTS_DATA, goodsListBean);
                }
                startActivity(intent);
                break;
            case R.id.add_shopping_cart:
                //TODO:添加到购物车
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.shopping_cart:
                intent = new Intent(ShopsDetailActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.collect:
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.select_specifications:
                /* //商品规格
                 */
                new CustomSpecificationDialog(this).setData(specifications)
                        .setNegativeButton(new CustomSpecificationDialog.OnOkClickListener() {
                            @Override
                            public void onOkClick(List<ProductParameters.GoodsSpecifications> data) {
                                sb = new StringBuffer();
                                valueId = new StringBuffer();
                                List<Integer> ids = new ArrayList<>();
                                for (int i = 0; i < data.size(); i++) {
                                    for (int j = 0; j < data.get(i).getSkuValueList().size(); j++) {
                                        if (data.get(i).getSkuValueList().get(j).isSelect()) {
                                            sb.append("\"" + data.get(i).getSkuValueList().get(j).getAttributeVal() + "\" ");
                                            ids.add(data.get(i).getSkuValueList().get(j).getValueId());
                                        }
                                    }
                                }
                                if (ids.size() > 0) {
                                    for (int i = 0; i < ids.size(); i++) {
                                        if (i == ids.size() - 1) {
                                            valueId.append(ids.get(i));
                                        } else {
                                            valueId.append(ids.get(i) + ",");
                                        }
                                    }
                                }
                                specificationsName.setText("选择了：" + sb.toString());
                            }
                        }).show();
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
