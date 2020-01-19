package com.feitianzhu.huangliwo.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.ProductParameters;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomSpecificationDialog;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

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
    private int goodsId;
    private BaseGoodsListBean goodsListBean;
    private ProductParameters productParameters;
    private List<BaseGoodsListBean.GoodsEvaluateMode> evalList;
    private List<ProductParameters.GoodsSpecifications> specifications = new ArrayList<>();
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private String token;
    private String userId;
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
    SubsamplingScaleImageView imgDetail;
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
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.tv_rebate)
    TextView tvRebate;
    @BindView(R.id.ll_rebate)
    LinearLayout llRebate;
    @BindView(R.id.ll_goods_detail)
    LinearLayout llGoodsDetail;


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
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<MineInfoModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MineInfoModel response, int id) {
                        if (response != null) {
                            mineInfoModel = response;
                        }
                    }
                });
    }

    public void getSpecifications() {
        OkHttpUtils.post()
                .url(Urls.GET_PRODUCT_PARAMETERS)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("goodsId", goodsId + "")
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
                });
    }

    public void getDetail(String goodsId) {
        OkHttpUtils.get()
                .url(Urls.GET_SHOP_DETAIL)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("goodsId", goodsId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, BaseGoodsListBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goodsListBean = (BaseGoodsListBean) response;
                        showView();
                    }
                });
    }

    public void showView() {
        tvAmount.setText("");
        String str2 = "¥ ";
        if (goodsListBean != null) {
            str3 = String.format(Locale.getDefault(), "%.2f", goodsListBean.getPrice());
            goodsName.setText(goodsListBean.getGoodsName());
            goodsSummary.setText(goodsListBean.getSummary());
            String rebatePv = String.format(Locale.getDefault(), "%.2f", goodsListBean.getRebatePv());
            if (goodsListBean.getRebatePv() == 0) {
                llRebate.setVisibility(View.GONE);
            }
            tvRebate.setText("赚¥" + rebatePv);
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

            if (goodsListBean.getGoodsIntroduceImg() == null || TextUtils.isEmpty(goodsListBean.getGoodsIntroduceImg())) {
                llGoodsDetail.setVisibility(View.GONE);
            } else {
                Glide.with(this).load(goodsListBean.getGoodsIntroduceImg()).apply(new RequestOptions().dontAnimate()).downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        Uri uri = Uri.fromFile(resource);
                        imgDetail.setImage(ImageSource.uri(uri));
                        imgDetail.setZoomEnabled(false);
                        imgDetail.setPanEnabled(false);
                    }
                });
            }
            if (goodsListBean.getGoodsImgsList() != null) {
                List<BaseGoodsListBean.GoodsImgsListBean> bannerList = goodsListBean.getGoodsImgsList();
                mViewpager.setCanLoop(true)
                        .setAutoPlay(true)
                        .setIndicatorStyle(IndicatorStyle.CIRCLE)
                        //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                        .setIndicatorRadius(8)
                        .setIndicatorColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                        .setHolderCreator(ShopsDetailActivity.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                    @Override
                    public void onPageClick(int position) {
                        onClickBanner(position);
                    }
                }).create(bannerList);
                mViewpager.startLoop();
            }
        }
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
            View view = LayoutInflater.from(context).inflate(R.layout.detail_banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, BaseGoodsListBean.GoodsImgsListBean data, final int position, final int size) {
            Glide.with(context).load(data.getGoodsImg())
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .placeholder(R.mipmap.g10_03weijiazai)
                            .error(R.mipmap.g10_03weijiazai))
                    .into(mImageView);
        }

    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.rl_more_evaluation, R.id.add_shopping_cart, R.id.shopping_cart, R.id.call_phone, R.id.collect, R.id.select_specifications, R.id.right_img, R.id.ll_rebate})
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
                if (valueId != null) {
                    intent.putExtra(ShopPayActivity.GOODS_VALUE_ID, valueId.toString());
                }
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
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.shopping_cart:
                ToastUtils.showShortToast("敬请期待");
               /* intent = new Intent(ShopsDetailActivity.this, ShoppingCartActivity.class);
                startActivity(intent);*/
                break;
            case R.id.call_phone:
                new XPopup.Builder(this)
                        .asConfirm("拨打商家电话", Constant.CUSTOMER_SERVICE_TELEPHONE, "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                requestPermission();
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.collect:
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.right_img:
                ToastUtils.showShortToast("敬请期待");
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
            case R.id.ll_rebate:
                intent = new Intent(ShopsDetailActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
                break;
        }

    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .callback(
                        new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                                AndPermission.rationaleDialog(App.getAppContext(), rationale).show();
                            }
                        }
                )
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Constant.CUSTOMER_SERVICE_TELEPHONE));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(ShopsDetailActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

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