package com.feitianzhu.huangliwo.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.AddShoppingCartBody;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.CollectionBody;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.ProductParameters;
import com.feitianzhu.huangliwo.shop.adapter.ShopsDetailImgAdapter;
import com.feitianzhu.huangliwo.shop.ui.ShoppingCartActivity;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomSpecificationDialog;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorSlideMode;
import com.zhpan.bannerview.constants.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhpan.bannerview.utils.BannerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

import static com.feitianzhu.huangliwo.shop.ShareShopActivity.GOODS_DATA;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 2:37
 * <p>
 * 商品详情页面
 */
public class ShopsDetailActivity extends BaseActivity {
    @BindView(R.id.goodsStock)
    TextView goodsStock;
    @BindView(R.id.goodsSalesvolume)
    TextView goodsSalesvolume;
    private boolean isAddShoppingCart = false;
    private boolean isBuyGoods = false;
    public static final String GOODS_DETAIL_DATA = "goods_detail_data";
    private StringBuffer sb;
    private StringBuffer valueId;
    private StringBuffer speciName;
    private String str3 = "0.00";
    private int goodsId;
    private BaseGoodsListBean goodsListBean;
    private ProductParameters productParameters;
    private List<BaseGoodsListBean.GoodsEvaluateMode> evalList;
    private List<ProductParameters.GoodsSpecifications> specifications = new ArrayList<>();
    private MineInfoModel mineInfoModel;
    private List<String> imgs = new ArrayList<>();
    private String token;
    private String userId;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.viewpager)
    BannerViewPager<BaseGoodsListBean.GoodsImgsListBean, DataViewHolder> mViewpager;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.goodsSummary)
    TextView goodsSummary;
    @BindView(R.id.select_specifications)
    TextView specificationsName;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
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
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.tv_rebate)
    TextView tvRebate;
    @BindView(R.id.ll_rebate)
    LinearLayout llRebate;
    @BindView(R.id.vip_rebate)
    TextView vipRebate;
    @BindView(R.id.ll_goods_detail)
    LinearLayout llGoodsDetail;
    @BindView(R.id.imgCollect)
    ImageView imgCollect;
    @BindView(R.id.detailRecyclerView)
    RecyclerView detailRecyclerView;
    @BindView(R.id.detail_img)
    SubsamplingScaleImageView detailImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shops_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        goodsId = getIntent().getIntExtra(GOODS_DETAIL_DATA, 0);
        titleName.setText("商品详情");
        rightImg.setBackgroundResource(R.mipmap.e01_02fenxiang);
        rightImg.setVisibility(View.VISIBLE);
        initListener();
    }

    public void initListener() {
    }

    @Override
    protected void initData() {
        getDetail(goodsId + "");
        getSpecifications();
        getUserInfo();
    }

    public void getUserInfo() {
        mineInfoModel = UserInfoUtils.getUserInfo(this);
        if (mineInfoModel.getAccountType() != 0) {
            llRebate.setVisibility(View.GONE);
            vipRebate.setVisibility(View.VISIBLE);
        } else {
            llRebate.setVisibility(View.VISIBLE);
            vipRebate.setVisibility(View.GONE);
        }
    }

    public void getSpecifications() {
        OkGo.<LzyResponse<ProductParameters>>post(Urls.GET_PRODUCT_PARAMETERS)
                .tag(this)
                .params("goodsId", goodsId + "")
                .execute(new JsonCallback<LzyResponse<ProductParameters>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ProductParameters>> response) {
                        //super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            productParameters = response.body().data;
                            /*
                             * 数据处理
                             * */
                            if (productParameters != null && productParameters.getGoodslist() != null && productParameters.getGoodslist().size() > 0) {
                                llSpecifications.setVisibility(View.VISIBLE);
                                specifications = productParameters.getGoodslist();
                                /*
                                 * 默认选中第一个
                                 * */
                                for (int i = 0; i < specifications.size(); i++) {
                                    for (int j = 0; j < specifications.get(i).getSkuValueList().size(); j++) {
                                        specifications.get(i).getSkuValueList().get(0).setSelect(true);
                                    }
                                }
                            } else {
                                llSpecifications.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ProductParameters>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getDetail(String goodsId) {
        OkGo.<LzyResponse<BaseGoodsListBean>>get(Urls.GET_SHOP_DETAIL)
                .tag(this)
                .params("goodsId", goodsId)
                .execute(new JsonCallback<LzyResponse<BaseGoodsListBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<BaseGoodsListBean>> response) {
                        //super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            goodsListBean = response.body().data;
                            showView();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<BaseGoodsListBean>> response) {
                        super.onError(response);
                    }
                });
    }

    public void showView() {
        tvAmount.setText("");
        String str2 = "¥ ";
        if (goodsListBean.getIsCollect() == 0) {
            imgCollect.setSelected(false);
        } else {
            imgCollect.setSelected(true);
        }
        str3 = String.format(Locale.getDefault(), "%.2f", goodsListBean.getPrice());
        goodsName.setText(goodsListBean.getGoodsName());
        goodsSummary.setText(goodsListBean.getSummary());
        goodsStock.setText("库存 " + goodsListBean.getStockCount());
        goodsSalesvolume.setText("销量 " + goodsListBean.getSalesStr());
        String rebatePv = String.format(Locale.getDefault(), "%.2f", goodsListBean.getRebatePv());
        tvRebate.setText("奖励¥" + MathUtils.subZero(rebatePv));
        vipRebate.setText("奖励¥" + MathUtils.subZero(rebatePv));
        evalList = goodsListBean.getEvalList();
        if (evalList != null && evalList.size() > 0) {
            llEvaluate.setVisibility(View.VISIBLE);
            tvCount.setText("评价(" + evalList.size() + ")");
            Glide.with(this).load(evalList.get(0).getHeadImg()).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into(ivHead);
            userName.setText(evalList.get(0).getNickName());
            tvDate.setText(evalList.get(0).getEvalDate());
            tvContent.setText(evalList.get(0).getContent());
            if (evalList.get(0).getNorms() != null && !TextUtils.isEmpty(evalList.get(0).getNorms())) {
                evaSpecifications.setText(evalList.get(0).getNorms() + "/" + goodsListBean.getGoodsName());
            } else {
                evaSpecifications.setText(goodsListBean.getGoodsName());
            }
        } else {
            llEvaluate.setVisibility(View.GONE);
            tvCount.setText("评价(0)");
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

        if (goodsListBean.getGoodsIntroduceImgList() == null || goodsListBean.getGoodsIntroduceImgList().size() <= 0) {
            llGoodsDetail.setVisibility(View.GONE);
        } else {
            if (goodsListBean.getGoodsIntroduceImgList().size() > 1) {
                ShopsDetailImgAdapter adapter = new ShopsDetailImgAdapter(goodsListBean.getGoodsIntroduceImgList());
                detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                detailRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                detailRecyclerView.setNestedScrollingEnabled(false);
                detailRecyclerView.setVisibility(View.VISIBLE);
                detailImg.setVisibility(View.GONE);
            } else {
                detailRecyclerView.setVisibility(View.GONE);
                detailImg.setVisibility(View.VISIBLE);
                Glide.with(this).load(goodsListBean.getGoodsIntroduceImg()).apply(new RequestOptions().dontAnimate()).downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        Uri uri = Uri.fromFile(resource);
                        detailImg.setImage(ImageSource.uri(uri));
                        detailImg.setZoomEnabled(false);
                        detailImg.setPanEnabled(false);
                    }
                });
            }
        }
        //String urlLogo = goodsListBean.getGoodsImg() == null ? "" : goodsListBean.getGoodsImg();
        //Glide.with(this).load(urlLogo).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(GlideUtils.getImageView(this, urlLogo, bannerImage));
        if (goodsListBean.getGoodsImgsList() != null) {
            List<BaseGoodsListBean.GoodsImgsListBean> goodsImgsList = goodsListBean.getGoodsImgsList();
            mViewpager.setCanLoop(true)
                    .setAutoPlay(true)
                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                    .setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                    .setIndicatorSliderRadius(BannerUtils.dp2px(2.5f))
                    .setIndicatorSliderColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                    .setHolderCreator(DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                }
            }).create(goodsListBean.getGoodsImgsList());
            mViewpager.startLoop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class DataViewHolder implements ViewHolder<BaseGoodsListBean.GoodsImgsListBean> {
        private ImageView mImageView;

        @Override
        public int getLayoutId() {
            return R.layout.detail_banner_item;
        }

        @Override
        public void onBind(View itemView, BaseGoodsListBean.GoodsImgsListBean data, int position, int size) {
            mImageView = itemView.findViewById(R.id.banner_image);
            Glide.with(mContext).load(data.getGoodsImg()).apply(new RequestOptions().error(R.mipmap.g10_03weijiazai).placeholder(R.mipmap.g10_03weijiazai).dontAnimate()).into(mImageView);
          /*  mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePreview
                            .getInstance()

                            // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                            .setShowIndicator(true)
                            .setContext(mContext)
                            .setEnableDragClose(false) //下拉图片关闭
                            // 设置从第0张开始看（索引从0开始）
                            .setIndex(position)
                            .setShowErrorToast(true)//加载失败提示
                            //=================================================================================================
                            // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                            // 1：第一步生成的imageInfo List
                            //.setImageInfoList(imageInfoList)

                            // 2：直接传url List
                            .setImageList(Arrays.asList(data.getGoodsImg()))

                            // 3：只有一张图片的情况，可以直接传入这张图片的url
                            //.setImage(String image)
                            //=================================================================================================

                            // 开启预览
                            .start();
                }
            });
*/

        }
    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.rl_more_evaluation, R.id.add_shopping_cart, R.id.shopping_cart, R.id.call_phone, R.id.collect, R.id.select_specifications, R.id.right_img, R.id.ll_rebate})
    @SingleClick()
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                isBuyGoods = true;
              /*  if (goodsListBean.getStockCount() <= 0) {
                    ToastUtils.show("当前商品已售完");
                    return;
                }*/
                if (specifications.size() > 0) {
                    showSpeDialog();
                    return;
                } else {
                    intent = new Intent(ShopsDetailActivity.this, ShopPayActivity.class);
                    if (valueId != null) {
                        intent.putExtra(ShopPayActivity.GOODS_VALUE_ID, valueId.toString());
                    }
                    intent.putExtra(ShopPayActivity.IS_SHOW_ADDRESS, true);
                    if (goodsListBean != null) {
                        intent.putExtra(ShopPayActivity.PAY_DATA, goodsListBean);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.rl_more_evaluation: //更多评论
                intent = new Intent(ShopsDetailActivity.this, CommentsDetailActivity.class);
                if (goodsListBean != null) {
                    intent.putExtra(CommentsDetailActivity.COMMENTS_DATA, goodsListBean);
                }
                startActivity(intent);
                break;
            case R.id.add_shopping_cart:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                isAddShoppingCart = true;
                if (specifications.size() > 0) {
                    showSpeDialog();
                    return;
                } else {
                    addShoppingCart();
                }
                break;
            case R.id.shopping_cart:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);

                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(ShopsDetailActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.call_phone:
                if (goodsListBean != null) {
                    new XPopup.Builder(this)
                            .asConfirm("拨打商家电话", goodsListBean.getConnectPhone(), "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    requestPermission();
                                }
                            }, null, false)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
                break;
            case R.id.collect:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);

                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (imgCollect.isSelected()) {
                    deleteCollect();
                } else {
                    collectGoods();
                }
                break;
            case R.id.right_img:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);

                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(ShopsDetailActivity.this, ShareShopActivity.class);
                intent.putExtra(GOODS_DATA, goodsListBean);
                startActivity(intent);
                break;
            case R.id.select_specifications:
                /* //商品规格
                 */
                showSpeDialog();
                break;
            case R.id.ll_rebate:
                token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);

                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(ShopsDetailActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
                break;
        }

    }


    public void showSpeDialog() {
        new CustomSpecificationDialog(this).setData(specifications)
                .setNegativeButton(new CustomSpecificationDialog.OnOkClickListener() {
                    @Override
                    public void onOkClick(List<ProductParameters.GoodsSpecifications> data) {
                        sb = new StringBuffer();
                        valueId = new StringBuffer();
                        speciName = new StringBuffer();
                        List<ProductParameters.GoodsSpecifications.SkuValueListBean> selectSpec = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            for (int j = 0; j < data.get(i).getSkuValueList().size(); j++) {
                                if (data.get(i).getSkuValueList().get(j).isSelect()) {
                                    sb.append("\"" + data.get(i).getSkuValueList().get(j).getAttributeVal() + "\" ");
                                    selectSpec.add(data.get(i).getSkuValueList().get(j));
                                }
                            }
                        }
                        if (selectSpec.size() > 0) {
                            for (int i = 0; i < selectSpec.size(); i++) {
                                if (i == selectSpec.size() - 1) {
                                    valueId.append(selectSpec.get(i).getValueId());
                                    speciName.append(selectSpec.get(i).getAttributeVal());
                                } else {
                                    valueId.append(selectSpec.get(i).getValueId() + ",");
                                    speciName.append(selectSpec.get(i).getAttributeVal() + ",");
                                }
                            }
                        }
                        specificationsName.setText("选择了：" + sb.toString());
                        if (isAddShoppingCart) {
                            isAddShoppingCart = false;
                            addShoppingCart();
                        }
                        if (isBuyGoods) {
                            isBuyGoods = false;
                            Intent intent = new Intent(ShopsDetailActivity.this, ShopPayActivity.class);
                            if (valueId != null) {
                                intent.putExtra(ShopPayActivity.GOODS_VALUE_ID, valueId.toString());
                            }
                            intent.putExtra(ShopPayActivity.IS_SHOW_ADDRESS, true);
                            if (goodsListBean != null) {
                                intent.putExtra(ShopPayActivity.PAY_DATA, goodsListBean);
                            }
                            startActivity(intent);
                        }
                    }
                }).show();
    }

    public void addShoppingCart() {
        AddShoppingCartBody model = new AddShoppingCartBody();
        model.goodsId = goodsId;
        if (valueId != null) {
            model.speci = valueId.toString();
            model.speciName = speciName.toString();
        } else {
            model.speci = "";
            model.speciName = "";
        }
        String json = new Gson().toJson(model);
        OkGo.<LzyResponse>post(Urls.ADD_SHOPPING_CART)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("shopingCarBody", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("添加成功");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void collectGoods() {
        CollectionBody collectionBody = new CollectionBody();
        collectionBody.type = 2;
        collectionBody.idValue = goodsId;
        String json = new Gson().toJson(collectionBody);
        OkGo.<LzyResponse>post(Urls.ADD_COLLECTION).tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("collect", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("收藏成功");
                            imgCollect.setSelected(true);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void deleteCollect() {
        CollectionBody collectionBody = new CollectionBody();
        collectionBody.type = 2;
        collectionBody.idValue = goodsId;
        String json = new Gson().toJson(collectionBody);
        OkGo.<LzyResponse>post(Urls.DELETE_COLLECTION).tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("collect", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("取消收藏");
                            imgCollect.setSelected(false);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    private void requestPermission() {
        XXPermissions.with(ShopsDetailActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CALL_PHONE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + goodsListBean.getConnectPhone()));
                            startActivity(intent);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
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
        if (mViewpager != null) {
            mViewpager.startLoop();
        }
    }
}
