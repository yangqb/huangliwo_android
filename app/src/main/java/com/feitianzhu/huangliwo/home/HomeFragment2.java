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
import com.feitianzhu.huangliwo.common.base.fragment.SFFragment;
import com.feitianzhu.huangliwo.financial.FinancialHomeActivity;
import com.feitianzhu.huangliwo.home.adapter.HAdapter;
import com.feitianzhu.huangliwo.home.adapter.HomeRecommendAdapter2;
import com.feitianzhu.huangliwo.home.adapter.HotGoodsAdapter;
import com.feitianzhu.huangliwo.home.adapter.IndicatorAdapter;
import com.feitianzhu.huangliwo.home.adapter.OptMerchantsAdapter;
import com.feitianzhu.huangliwo.home.entity.HomeEntity;
import com.feitianzhu.huangliwo.home.entity.IndicatorEntity;
import com.feitianzhu.huangliwo.home.entity.NoticeModel;
import com.feitianzhu.huangliwo.home.entity.ShopAndMerchants;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.plane.PlaneHomeActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.travel.TravelHomeActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.login.LoginEvent.EDITOR_INFO;

/**
 * @class name：com.feitianzhu.fu700.home
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:35
 */
public class HomeFragment2 extends SFFragment implements ProvinceCallBack, PagerGridLayoutManager.PageListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout mSwipeLayout;
    @BindView(R.id.search)
    LinearLayout mSearchLayout;
    @BindView(R.id.txt_location)
    TextView mTxtLocation;
    @BindView(R.id.iv_home_nv_right)
    ImageView ivRight;
    @BindView(R.id.hList)
    RecyclerView hList;
    @BindView(R.id.viewpager)
    BannerViewPager<HomeEntity.BannerListBean, DataViewHolder> mViewpager;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.indicatorRecyclerView)
    RecyclerView indicatorRecyclerView;
    @BindView(R.id.optRecyclerView)
    RecyclerView optRecyclerView;
    @BindView(R.id.hotRecyclerView)
    RecyclerView hotRecyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    private List<ShopAndMerchants> shopAndMerchants = new ArrayList<>();
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<BaseGoodsListBean> shopsLists = new ArrayList<>();
    private HomeRecommendAdapter2 mAdapter;
    private IndicatorAdapter indicatorAdapter;
    private List<IndicatorEntity> indicatorEntityList = new ArrayList<>();
    private HAdapter hAdapter;
    private HomeEntity mHomeEntity;
    private List<HomeEntity.BannerListBean> mBanners = new ArrayList<>();
    private boolean isLoadMore;
    private String token;
    private String userId;
    private int pageNo = 1;
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private PagerGridLayoutManager layoutManager;
    private OptMerchantsAdapter optMerchantsAdapter;
    private HotGoodsAdapter hotGoodsAdapter;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private List<MerchantsModel> optMerchantList = new ArrayList<>();
    private List<BaseGoodsListBean> hotGoodsList = new ArrayList<>();

    public HomeFragment2() {

    }

    public static HomeFragment2 newInstance() {
        return new HomeFragment2();
    }

    private CallbackBFragment mCallbackBFragment;

    @Override
    public void onPageSizeChanged(int pageSize) {

    }

    @Override
    public void onPageSelect(int pageIndex) {
        for (int i = 0; i < indicatorEntityList.size(); i++) {
            if (i == pageIndex) {
                indicatorEntityList.get(i).isSelect = true;
            } else {
                indicatorEntityList.get(i).isSelect = false;
            }
        }
        indicatorAdapter.notifyDataSetChanged();
    }

    public interface CallbackBFragment {
        void skipToCommodityFragment(int type, View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbackBFragment = (CallbackBFragment) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        hAdapter = new HAdapter(shopClassifyLsit);
        // 1.水平分页布局管理器
        layoutManager = new PagerGridLayoutManager(
                1, 5, PagerGridLayoutManager.HORIZONTAL);
        hList.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(hList);
        /*// 平滑滚动使用示例
        hList.smoothScrollToPosition(0);// 平滑滚动到指定条目
        layoutManager.smoothScrollToPage(0);// 平滑滚动到指定页
        layoutManager.smoothPrePage();// 平滑滚动到上一页
        layoutManager.smoothNextPage();// 平滑滚动到下一页*/
        // 使用示例
        //layoutManager.isAllowContinuousScroll();//设置是否允许连续滚动
        layoutManager.setAllowContinuousScroll(false);//设置是否允许连续滚动
        hList.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();

        //hList.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        //new GridPagerSnapHelper(2, 3).attachToRecyclerView(hList);

        // 使用示例
        //layoutManager.setChangeSelectInScrolling(false);// 设置是否在滚动过程中回调页码变化
        layoutManager.setPageListener(this);    // 设置页面变化监听器
        // 使用示例滚动方向
        //layoutManager.setOrientationType(PagerGridLayoutManager.HORIZONTAL);
        //设置滚动速度
        //PagerConfig.setMillisecondsPreInch(60f);
        //  //直接滚动使用示例
       /* hList.scrollToPosition(0);
        layoutManager.scrollToPage(0);
        layoutManager.prePage();
        layoutManager.nextPage();*/

        //商品
        mAdapter = new HomeRecommendAdapter2(shopAndMerchants);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
        hList.setNestedScrollingEnabled(false);
        //商家
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);

        indicatorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        indicatorAdapter = new IndicatorAdapter(indicatorEntityList);
        indicatorRecyclerView.setAdapter(indicatorAdapter);
        indicatorAdapter.notifyDataSetChanged();

        optRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        optMerchantsAdapter = new OptMerchantsAdapter(optMerchantList);
        optRecyclerView.setAdapter(optMerchantsAdapter);
        optMerchantsAdapter.notifyDataSetChanged();
        optRecyclerView.setNestedScrollingEnabled(false);

        hotRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        hotGoodsAdapter = new HotGoodsAdapter(hotGoodsList);
        hotRecyclerView.setAdapter(hotGoodsAdapter);
        hotGoodsAdapter.notifyDataSetChanged();
        hotRecyclerView.setNestedScrollingEnabled(false);


        getData();
        requestData();
        getGoodsData();
        getClasses(); //商品类别
        getNotice();
        initListener();
        return view;
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == ShopAndMerchants.TYPE_GOODS) {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, shopAndMerchants.get(position).getShopsList().getGoodsId());
                    startActivity(intent);
                } else {
                    //套餐详情页
                   /* Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANT_DATA, recommendListBeanList.get(position - shopsLists.size()));
                    startActivity(intent);*/
                }
                //startShopsActivity(mHomeEntity.recommendList.get(position).merchantId);
            }
        });


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
            }
        });

        hAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopClassify.GGoodsClsListBean goodsClsListBean = shopClassifyLsit.get(position);
                Intent intent = new Intent(getActivity(), ShopsActivity.class);
                intent.putExtra(ShopsActivity.CLASSES_ID, goodsClsListBean.getClsId());
                startActivity(intent);
            }
        });

        optMerchantsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
            }
        });

        optMerchantsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商铺详情页
                Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, optMerchantList.get(position).getMerchantId());
                startActivity(intent);
            }
        });

        hotGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, hotGoodsList.get(position).getGoodsId());
                startActivity(intent);
            }
        });

        mSwipeLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                pageNo++;
                getGoodsData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = false;
                pageNo = 1;
                token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
                userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
                getData();
                getNotice();
                requestData();
                getClasses();
                getGoodsData();
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

    @OnClick({R.id.ll_location, R.id.iv_head, R.id.rl_ticket, R.id.rl_financial, R.id.rl_travel, R.id.rl_mall, R.id.rl_merchants, R.id.search, R.id.iv_home_nv_right, R.id.back_top})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_location:
                ProvinceDialog2 branchDialog = ProvinceDialog2.newInstance();
                branchDialog.setCityLevel(ProvinceDialog2.PROVINCE_CITY);
                branchDialog.setAddress("北京市", "东城区", "东华门街道");
                branchDialog.setSelectOnListener(this);
                branchDialog.show(getChildFragmentManager());
                break;
            case R.id.iv_head: //
                intent = new Intent(getActivity(), PersonalCenterActivity2.class);
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
            case R.id.rl_financial:
                intent = new Intent(getActivity(), FinancialHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_travel:
                intent = new Intent(getActivity(), TravelHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                JumpActivity(getActivity(), SearchShopActivity.class);
                break;
            case R.id.iv_home_nv_right:
                requestPermission(view);
                break;
            case R.id.back_top:
                mRecyclerview.smoothScrollToPosition(0);
                scrollView.fling(0);
                scrollView.smoothScrollTo(0, 0);
                break;
        }

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

    public void getData() {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }
        OkGo.<LzyResponse<HomeEntity>>get(Urls.GET_INDEX)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("longitude", longitude + "")
                .params("latitude", latitude + "")
                .execute(new JsonCallback<LzyResponse<HomeEntity>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<HomeEntity>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh();
                        } else {
                            mSwipeLayout.finishLoadMore();
                        }

                        mHomeEntity = response.body().data;

                        //1.set banner
                        if (mHomeEntity != null && mHomeEntity.bannerList != null && !mHomeEntity.bannerList.isEmpty()) {
                            mBanners.clear();
                            mBanners.addAll(mHomeEntity.bannerList);
                            mViewpager.setCanLoop(true)
                                    .setAutoPlay(true)
                                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                                    //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                                    .setRoundCorner(20)
                                    .setIndicatorRadius(8)
                                    .setIndicatorColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                                    .setHolderCreator(DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                                @Override
                                public void onPageClick(int position) {
                                    onClickBanner(position);
                                }
                            }).create(mBanners);
                            mViewpager.startLoop();
                        }

                        if (mHomeEntity != null && mHomeEntity.merchantList != null) {
                            optMerchantList = mHomeEntity.merchantList;
                            optMerchantsAdapter.setNewData(optMerchantList);
                            optMerchantsAdapter.notifyDataSetChanged();
                        }

                        if (mHomeEntity != null && mHomeEntity.goodsListfove != null) {
                            hotGoodsList = mHomeEntity.goodsListfove;
                            hotGoodsAdapter.setNewData(hotGoodsList);
                            hotGoodsAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<HomeEntity>> response) {
                        super.onError(response);
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh(false);
                        } else {
                            mSwipeLayout.finishLoadMore(false);
                        }

                    }
                });
    }

    public void getGoodsData() {
        /*GoodsListRequest goodsListRequest = new GoodsListRequest(token, userId, pageNo);
        goodsListRequest.token = token;
        goodsListRequest.userId = userId;
        goodsListRequest.pageNo = pageNo;
        goodsListRequest.call(new ApiLifeCallBack<HomeShops>() {
            @Override
            public void onStart() {
                if (isLoadMore) {
                    if (shopsLists.size() == 10) {
                        showloadDialog("");
                    }
                } else {
                    showloadDialog("");
                }
            }

            @Override
            public void onFinsh() {

            }

            @Override
            public void onAPIResponse(HomeShops response) {
                goneloadDialog();
                if (!isLoadMore) {
                    mSwipeLayout.finishRefresh();
                } else {
                    mSwipeLayout.finishLoadMore();
                }
                if (!isLoadMore) {
                    shopAndMerchants.clear();
                    indicatorEntityList.clear();
                }
                //商品
                if (response != null) {
                    shopsLists = response.getGoodsList();
                    if (shopsLists != null && shopsLists.size() > 0) {
                        for (int i = 0; i < shopsLists.size(); i++) {
                            ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_GOODS);
                            entity.setShopsList(shopsLists.get(i));
                            shopAndMerchants.add(entity);
                        }
                        mAdapter.setNewData(shopAndMerchants);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (isLoadMore) {
                    if (shopsLists != null && shopsLists.size() == 0) {
                        ToastUtils.show("没有更多数据了");
                    }
                }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                goneloadDialog();
                if (!isLoadMore) {
                    mSwipeLayout.finishRefresh(false);
                } else {
                    mSwipeLayout.finishLoadMore(false);
                }
            }
        });*/
    /*    OkGo.<LzyResponse<HomeShops>>get(Urls.GET_HOME_GOODS_LIST)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("limitNum", Constant.PAGE_SIZE)
                .params("curPage", pageNo + "")
                .execute(new JsonCallback<LzyResponse<HomeShops>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<HomeShops>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        if (isLoadMore) {
                            if (shopsLists.size() == 10) {
                                showloadDialog("");
                            }
                        } else {
                            showloadDialog("");
                        }
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<HomeShops>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        goneloadDialog();
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh();
                        } else {
                            mSwipeLayout.finishLoadMore();
                        }
                        HomeShops homeShops = response.body().data;
                        if (!isLoadMore) {
                            shopAndMerchants.clear();
                            indicatorEntityList.clear();
                        }
                        //商品
                        if (homeShops != null) {
                            shopsLists = homeShops.getGoodsList();
                            if (shopsLists != null && shopsLists.size() > 0) {
                                for (int i = 0; i < shopsLists.size(); i++) {
                                    ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_GOODS);
                                    entity.setShopsList(shopsLists.get(i));
                                    shopAndMerchants.add(entity);
                                }
                                mAdapter.setNewData(shopAndMerchants);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        if (isLoadMore) {
                            if (shopsLists != null && shopsLists.size() == 0) {
                                ToastUtils.show("没有更多数据了");
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<HomeShops>> response) {
                        super.onError(response);
                        goneloadDialog();
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh(false);
                        } else {
                            mSwipeLayout.finishLoadMore(false);
                        }
                    }
                });*/
    }

    public void getClasses() {
        OkGo.<LzyResponse<ShopClassify>>post(Urls.GET_SHOP_CLASS)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<ShopClassify>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<ShopClassify>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        ShopClassify shopClassify = response.body().data;
                        if (shopClassify != null && shopClassify.getGGoodsClsList() != null) {
                            shopClassifyLsit = shopClassify.getGGoodsClsList();
                            hAdapter.setNewData(shopClassifyLsit);
                            hAdapter.notifyDataSetChanged();

                            int indicatorSize = shopClassifyLsit.size() % 5 == 0 ? (shopClassifyLsit.size() / 5) : (shopClassifyLsit.size() / 5) + 1;
                            if (!isLoadMore) {
                                layoutManager.scrollToPage(0);
                            }
                            for (int i = 0; i < indicatorSize; i++) {
                                IndicatorEntity indicatorEntity = new IndicatorEntity();
                                if (i == 0) {
                                    indicatorEntity.isSelect = true;
                                } else {
                                    indicatorEntity.isSelect = false;
                                }
                                indicatorEntityList.add(indicatorEntity);
                            }
                            indicatorAdapter.setNewData(indicatorEntityList);
                            indicatorAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<ShopClassify>> response) {
                        super.onError(response);
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
        unbinder.unbind();
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean
            city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = province.id;
        Constant.cityId = city.id;
        Constant.mCity = city.name;
        mTxtLocation.setText(city.name);
    }

    public void onClickBanner(int i) {
        if (mHomeEntity == null || mHomeEntity.bannerList == null || mHomeEntity.bannerList.isEmpty()) {
            ToastUtils.show("数据加载失败，请重新获取");
            return;
        }
        //链接类型（1：VIP，2：商品详情，3：文章，4：外部链接）
        Intent intent;
        switch (mHomeEntity.bannerList.get(i).linkType) {
            case 1:
                intent = new Intent(getActivity(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, mHomeEntity.bannerList.get(i).idValue);
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

    private void JumpActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public void requestPermission(View view) {
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Intent intent = new Intent(getActivity(), ScannerActivity.class);
                            intent.putExtra(ScannerActivity.IS_MERCHANTS, mineInfoModel.getIsMerchant());
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

    public class DataViewHolder implements ViewHolder<HomeEntity.BannerListBean> {
        private RoundedImageView mImageView;

        @Override
        public int getLayoutId() {
            return R.layout.banner_item;
        }

        @Override
        public void onBind(View itemView, HomeEntity.BannerListBean data, int position, int size) {
            mImageView = itemView.findViewById(R.id.banner_image);
            Glide.with(mContext).load(data.imagUrl).apply(new RequestOptions().error(R.mipmap.g10_01weijiazai).placeholder(R.mipmap.g10_01weijiazai).dontAnimate()).into(mImageView);
        }
    }

    /*
     * 获取头像
     * */
    private void requestData() {
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        if (response.body().data != null) {
                            mineInfoModel = response.body().data;
                            String headImg = mineInfoModel.getHeadImg();
                            Glide.with(mContext).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                                    .into(ivHead);
                            UserInfoUtils.saveUserInfo(getActivity(), mineInfoModel);
                            SPUtils.putString(getActivity(), Constant.SP_PHONE, mineInfoModel.getPhone());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        if (event == EDITOR_INFO) {
            requestData();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackBFragment = null;
    }
}

