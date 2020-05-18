package com.feitianzhu.huangliwo.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.home.adapter.HotGoodsAdapter2;
import com.feitianzhu.huangliwo.home.adapter.OptAdapter;
import com.feitianzhu.huangliwo.home.adapter.RecommendedAdapter;
import com.feitianzhu.huangliwo.home.entity.HomeEntity;
import com.feitianzhu.huangliwo.home.entity.NoticeModel;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.HomeModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.plane.PlaneHomeActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.travel.TravelHomeActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.itheima.roundedimageview.RoundedImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorSlideMode;
import com.zhpan.bannerview.constants.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhpan.bannerview.utils.BannerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/4/27
 * time: 18:15
 * email: 694125155@qq.com
 */
@SuppressWarnings("ALL")
public class RecommendedFragment extends SFFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<HomeEntity.BannerListBean> mBanners = new ArrayList<>();
    private List<MerchantsModel> optMerchantList = new ArrayList<>();
    private List<BaseGoodsListBean> hotGoodsList = new ArrayList<>();
    private List<BaseGoodsListBean> recGoodsList = new ArrayList<>();
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private HomeModel mHomeMode;
    private MineInfoModel userInfo;
    private String token;
    private String userId;
    private OptAdapter optAdapter;
    private HotGoodsAdapter2 hotGoodsAdapter;
    private RecommendedAdapter recommendedAdapter;
    private CallbackBFragment mCallbackBFragment;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.bannerViewPager)
    BannerViewPager<HomeEntity.BannerListBean, RecommendedFragment.DataViewHolder> mViewpager;
    @BindView(R.id.activityImg)
    ImageView activityImageView;
    @BindView(R.id.hotImg)
    RoundedImageView hotImg;
    @BindView(R.id.optRecyclerView)
    RecyclerView optRecyclerView;
    @BindView(R.id.discounts_img1)
    ImageView discountsImg1;
    @BindView(R.id.discounts_img2)
    ImageView discountsImg2;
    @BindView(R.id.discounts_img3)
    ImageView discountsImg3;
    @BindView(R.id.discounts_img4)
    ImageView discountsImg4;
    @BindView(R.id.discounts_img5)
    ImageView discountsImg5;
    @BindView(R.id.hotRecyclerView)
    RecyclerView hotRecyclerView;
    @BindView(R.id.recommendedRecyclerView)
    RecyclerView recommendedRecyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;

    public RecommendedFragment() {

    }

    public interface CallbackBFragment {
        void skipToCommodityFragment(int type, View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbackBFragment = (CallbackBFragment) context;
    }

    public static RecommendedFragment newInstance(int param2) {
        RecommendedFragment fragment = new RecommendedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frangment_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        userInfo = UserInfoUtils.getUserInfo(getActivity());

        initView();
        initData();
        initListener();
        getNotice();
        return view;
    }
    public void getNotice() {
        OkGo.<LzyResponse<NoticeModel>>get(Urls.GET_HOME_NOTICE)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<NoticeModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<NoticeModel>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        if (response.body().data != null) {
                            NoticeModel noticeModel = response.body().data;
                            if (noticeModel.getPushMsg() != null && !TextUtils.isEmpty(noticeModel.getPushMsg().getPushContent()) && noticeModel.getPushMsg().getPushContent() != null) {
                                llNotice.setVisibility(View.VISIBLE);
                                tvNotice.setText(noticeModel.getPushMsg().getPushContent());
                            } else {
                                llNotice.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<NoticeModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void initView() {
        optRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        optAdapter = new OptAdapter(optMerchantList);
        optRecyclerView.setAdapter(optAdapter);
        optRecyclerView.setNestedScrollingEnabled(false);

        hotGoodsAdapter = new HotGoodsAdapter2(hotGoodsList);
        hotRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        hotRecyclerView.setAdapter(hotGoodsAdapter);
        hotRecyclerView.setNestedScrollingEnabled(false);

        recommendedAdapter = new RecommendedAdapter(recGoodsList);
        recommendedRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recommendedRecyclerView.setAdapter(recommendedAdapter);
        recommendedRecyclerView.setNestedScrollingEnabled(false);
        refreshLayout.setEnableLoadMore(false);
    }
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @SingleClick()
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        optAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @SingleClick()
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商铺详情页
                Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, optMerchantList.get(position).getMerchantId());
                startActivity(intent);
            }
        });

        optAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
            }
        });

        hotGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @SingleClick()
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, hotGoodsList.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        hotGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @SingleClick()
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
            }
        });

        recommendedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @SingleClick()
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, recGoodsList.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        recommendedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = getResources().getDisplayMetrics().heightPixels;
                if (height != 0 && scrollY > height / 2) {
                    backTop.setVisibility(View.VISIBLE);
                } else {
                    backTop.setVisibility(View.GONE);
                }
            }
        });
    }

  @SingleClick()
    @OnClick({R.id.discounts_img1, R.id.discounts_img2, R.id.discounts_img3, R.id.discounts_img4, R.id.discounts_img5, R.id.activityImg, R.id.hotImg, R.id.rl_ticket, R.id.rl_travel, R.id.rl_mall, R.id.rl_merchants, R.id.back_top})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.discounts_img1:
                jumpActivity(mHomeMode.goodClsImgs.get(0).clsId);
                break;
            case R.id.discounts_img2:
                jumpActivity(mHomeMode.goodClsImgs.get(1).clsId);
                break;
            case R.id.discounts_img3:
                jumpActivity(mHomeMode.goodClsImgs.get(2).clsId);
                break;
            case R.id.discounts_img4:
                jumpActivity(mHomeMode.goodClsImgs.get(3).clsId);
                break;
            case R.id.discounts_img5:
                jumpActivity(mHomeMode.goodClsImgs.get(4).clsId);
                break;
            case R.id.activityImg:
                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
                break;
            case R.id.hotImg:
                intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, mHomeMode.hotGood.goodId);
                startActivity(intent);
                break;
            case R.id.rl_mall:
                mCallbackBFragment.skipToCommodityFragment(2, view);
                break;
            case R.id.rl_merchants:
                mCallbackBFragment.skipToCommodityFragment(1, view);
                break;
            case R.id.rl_ticket:
                intent = new Intent(getActivity(), PlaneHomeActivity.class);
                startActivity(intent);
                break;
            /*case R.id.rl_financial:
                intent = new Intent(getActivity(), FinancialHomeActivity.class);
                startActivity(intent);
                break;*/
            case R.id.rl_travel:
                intent = new Intent(getActivity(), TravelHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.back_top:
                scrollView.fling(0);
                scrollView.smoothScrollTo(0, 0);
                break;
        }
    }


    public void jumpActivity(int clsId) {
        Intent intent = new Intent(getActivity(), ShopsActivity.class);
        intent.putExtra(ShopsActivity.CLASSES_ID, clsId);
        startActivity(intent);
    }

    public void initData() {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }
        OkGo.<LzyResponse<HomeModel>>get(Urls.GET_INDEX)
                .tag(this)
                .params("longitude", longitude + "")
                .params("latitude", latitude + "")
                .execute(new JsonCallback<LzyResponse<HomeModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HomeModel>> response) {
                        super.onSuccess(getActivity(), response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            mHomeMode = response.body().data;
                            if (mHomeMode.bannerList != null && mHomeMode.bannerList.size() > 0) {
                                showBanner();
                            }
                            Glide.with(getActivity()).load(mHomeMode.activityImg).apply(new RequestOptions().placeholder(R.mipmap.g10_01weijiazai).error(R.mipmap.g10_01weijiazai).dontAnimate()).into(activityImageView);
                            if (mHomeMode.hotGood != null) {
                                Glide.with(getActivity()).load(mHomeMode.hotGood.hotGoodImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(hotImg);
                            }
                            if (mHomeMode.merchantList != null) {
                                optMerchantList = mHomeMode.merchantList;
                                optAdapter.setNewData(optMerchantList);
                                optAdapter.notifyDataSetChanged();
                            }
                            if (mHomeMode.goodClsImgs != null && mHomeMode.goodClsImgs.size() > 0) {
                                showDiscountsImg();
                            }

                            if (mHomeMode.goodsListfove != null) {
                                hotGoodsList = mHomeMode.goodsListfove;
                                hotGoodsAdapter.setNewData(hotGoodsList);
                                hotGoodsAdapter.notifyDataSetChanged();
                            }
                            if (mHomeMode.goodsList != null) {
                                recGoodsList = mHomeMode.goodsList;
                                recommendedAdapter.setNewData(recGoodsList);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<HomeModel>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    public void showDiscountsImg() {
        Glide.with(getActivity()).load(mHomeMode.goodClsImgs.get(0).goodClsImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(discountsImg1);
        Glide.with(getActivity()).load(mHomeMode.goodClsImgs.get(1).goodClsImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(discountsImg2);
        Glide.with(getActivity()).load(mHomeMode.goodClsImgs.get(2).goodClsImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(discountsImg3);
        Glide.with(getActivity()).load(mHomeMode.goodClsImgs.get(3).goodClsImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(discountsImg4);
        Glide.with(getActivity()).load(mHomeMode.goodClsImgs.get(4).goodClsImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(discountsImg5);
    }

    public void showBanner() {
        mBanners.clear();
        mBanners.addAll(mHomeMode.bannerList);
        mViewpager.setCanLoop(true)
                .setAutoPlay(true)
                .setIndicatorStyle(IndicatorStyle.CIRCLE)
                .setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                .setIndicatorSliderRadius(BannerUtils.dp2px(2.5f))
                .setIndicatorSliderColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                .setHolderCreator(RecommendedFragment.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                onClickBanner(position);
            }
        }).create(mBanners);
        mViewpager.startLoop();
    }
    public void onClickBanner(int i) {
        //链接类型（1：VIP，2：商品详情，3：文章，4：外部链接）
        Intent intent;
        switch (mHomeMode.bannerList.get(i).linkType) {
            case 1:
                intent = new Intent(getActivity(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, mHomeMode.bannerList.get(i).idValue);
                startActivity(intent);
                break;
            case 3:
                // ToastUtils.show("敬请期待");
                //WebViewActivity.startActivity(getActivity(), mHomeEntity.bannerList.get(i).outUrl, "");
                break;
            case 4:
                intent = new Intent(getActivity(), NewYearShoppingActivity.class);
                startActivity(intent);
                break;

        }
    }

    public class DataViewHolder implements ViewHolder<HomeEntity.BannerListBean> {
        private ImageView mImageView;

        @Override
        public int getLayoutId() {
            return R.layout.detail_banner_item;
        }

        @Override
        public void onBind(View itemView, HomeEntity.BannerListBean data, int position, int size) {
            mImageView = itemView.findViewById(R.id.banner_image);
            Glide.with(mContext).load(data.imagUrl).apply(new RequestOptions().error(R.mipmap.g10_01weijiazai).placeholder(R.mipmap.g10_01weijiazai).dontAnimate()).into(mImageView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewpager != null)
            mViewpager.startLoop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackBFragment = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
        unbinder.unbind();
    }
}
