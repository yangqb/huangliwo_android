package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.databinding.FragmentCommodityClassification1Binding;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MerchantsInfoNew;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiItemShopAndMerchants;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.model.ShopsNew;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.adapter.LeftAdapter;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapter1;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.login.LoginEvent.EDITOR_INFO;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 2:22
 * <p>
 * 商品分类页面
 */


public class CommodityClassificationFragment extends SFFragment implements ProvinceCallBack {




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mParam1 = 2;
    private String mParam2;
    private LeftAdapter leftAdapter;
    private RightAdapter1 rightAdapter;
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<MerchantsClassifyModel.ListBean> merchantsClassifyList = new ArrayList<>();
    private List<MultiItemShopAndMerchants> multiItemShopAndMerchantsClass = new ArrayList<>();


    private List<MultipleItem> multipleItemList = new ArrayList<>();
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();

    private List<MerchantsModel> merchantsList = new ArrayList<>();

    private List<MultipleItem> multipleRecommItemList = new ArrayList<>();
    private List<BaseGoodsListBean> goodsRecommListBeans = new ArrayList<>();

    private List<MerchantsModel> merchantsRecommList = new ArrayList<>();

    private List<MultipleItem> multipleBouItemList = new ArrayList<>();
    private List<BaseGoodsListBean> goodsBouListBeans = new ArrayList<>();

    private List<MerchantsModel> merchantsBouList = new ArrayList<>();

    private int clsShopId;
    private int clsMearchantsId;
    private String token;
    private String userId;
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private RightAdapter1 rightAdapterRecomm;
    private RightAdapter1 rightAdapterBou;
    private FragmentCommodityClassification1Binding binding;

    public CommodityClassificationFragment() {

    }

    public static CommodityClassificationFragment newInstance(int param1, String param2) {
        CommodityClassificationFragment fragment = new CommodityClassificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommodityClassification1Binding.inflate(inflater, container, false);
        binding.setViewModel(this);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            binding.txtLocation.setText(Constant.mCity);
        }
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);

        leftAdapter = new LeftAdapter(multiItemShopAndMerchantsClass);

        binding.leftRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.leftRecyclerView.setAdapter(leftAdapter);
        leftAdapter.notifyDataSetChanged();

        rightAdapter = new RightAdapter1(multipleItemList);

//        commodityStringVm = new CommodityStringVm();
//
//        rightAdapter.setHeaderView(commodityStringVm.getView(getLayoutInflater()));
        View mEmptyView = View.inflate(getActivity(), R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        binding.rightRecyclerView.setLayoutManager(gridLayoutManager);
        binding.rightRecyclerView.setAdapter(rightAdapter);
        rightAdapter.notifyDataSetChanged();


        rightAdapterRecomm = new RightAdapter1(multipleRecommItemList);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 3);
        binding.recyclerView2.setLayoutManager(gridLayoutManager1);
        binding.recyclerView2.setAdapter(rightAdapterRecomm);
        rightAdapterRecomm.notifyDataSetChanged();

        rightAdapterBou = new RightAdapter1(multipleBouItemList);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 3);
        binding.recyclerView1.setLayoutManager(gridLayoutManager2);
        binding.recyclerView1.setAdapter(rightAdapterBou);
        rightAdapterBou.notifyDataSetChanged();

        binding.swipeLayout.setEnableLoadMore(true);
        binding.swipeLayout.setEnableRefresh(true);
        binding.swipeLayout.setEnableLoadMoreWhenContentNotFull(true);//在内容不满一页的时候，是否可以上拉加载更多

        if (mParam1 == 1) { //商家
            binding.tabLayout.getTabAt(1).select();
            getMerchantsClass();
        } else {//商城
            binding.tabLayout.getTabAt(0).select();

            getShopClass();
        }
        binding.swipeLayout.setEnableLoadMoreWhenContentNotFull(true);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1) { //商家
                    mParam1 = 1;
                    getMerchantsClass();
                } else {//商城
                    mParam1 = 2;
                    getShopClass();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        showHeadImg();
        initListener();
        return binding.getRoot();
    }

    public void getMerchantsClass() {
        binding.title1.setText("热门商铺");
        binding.title2.setText("优质商家");
        binding.title3.setText("为您推荐");
        binding.nescro.scrollTo(0, 0);
        binding.leftRecyclerView.scrollToPosition(0);
        OkGo.<LzyResponse<MerchantsClassifyModel>>get(Urls.GET_MERCHANTS_TYPE)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MerchantsClassifyModel>>() {
                    @Override
                    public void onStart(Request<LzyResponse<MerchantsClassifyModel>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsClassifyModel>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        super.onSuccess(getActivity(), response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            merchantsClassifyList = response.body().data.getList();
                            multiItemShopAndMerchantsClass.clear();
                            for (int i = 0; i < merchantsClassifyList.size(); i++) {
                                MultiItemShopAndMerchants multiItemShopAndMerchants = new MultiItemShopAndMerchants(MultiItemShopAndMerchants.MERCHANTS_TYPE);
                                multiItemShopAndMerchants.setMerchantsClassifyModel(merchantsClassifyList.get(i));
                                multiItemShopAndMerchantsClass.add(multiItemShopAndMerchants);
                            }
                            leftAdapter.setSelect(0);
                            leftAdapter.setNewData(multiItemShopAndMerchantsClass);
                            leftAdapter.notifyDataSetChanged();
                            clsMearchantsId = merchantsClassifyList.get(0).getClsId();
                            getMerchants(clsMearchantsId, -1);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsClassifyModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getShopClass() {
        binding.nescro.scrollTo(0, 0);
        binding.leftRecyclerView.scrollToPosition(0);
        binding.title1.setText("热门商品");
        binding.title2.setText("精品推荐");
        binding.title3.setText("为您推荐");
        OkGo.<LzyResponse<ShopClassify>>post(Urls.GET_SHOP_CLASS)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<ShopClassify>>() {
                    @Override
                    public void onStart(Request<LzyResponse<ShopClassify>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<ShopClassify>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        super.onSuccess(getActivity(), "", response.body().code);
                        if (response.body().data != null) {
                            ShopClassify shopClassify = response.body().data;
                            shopClassifyLsit = shopClassify.getGGoodsClsList();
                            multiItemShopAndMerchantsClass.clear();
                            if (shopClassifyLsit != null && shopClassifyLsit.size() > 0) {
                                for (int i = 0; i < shopClassifyLsit.size(); i++) {
                                    MultiItemShopAndMerchants multiItemShopAndMerchants = new MultiItemShopAndMerchants(MultiItemShopAndMerchants.SHOP_TYPE);
                                    multiItemShopAndMerchants.setShopClassifyModel(shopClassifyLsit.get(i));
                                    multiItemShopAndMerchantsClass.add(multiItemShopAndMerchants);
                                }
                                leftAdapter.setSelect(0);
                                leftAdapter.setNewData(multiItemShopAndMerchantsClass);
                                leftAdapter.notifyDataSetChanged();
                                clsShopId = shopClassifyLsit.get(0).getClsId();
                                getShops(clsShopId, -1);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ShopClassify>> response) {
                        super.onError(response);
                    }
                });
    }

    public void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)   binding.leftRecyclerView.getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(position, 0);
                binding.nescro.scrollTo(0, 0);
                leftAdapter.setSelect(position);
                leftAdapter.notifyDataSetChanged();
                if (leftAdapter.getItemViewType(position) == MultiItemShopAndMerchants.SHOP_TYPE) {
                    //获取当前分类的商品
                    clsShopId = shopClassifyLsit.get(position).getClsId();
                    getShops(clsShopId, -1);
                } else {
                    //获取当前分类的商品
                    clsMearchantsId = merchantsClassifyList.get(position).getClsId();
                    getMerchants(clsMearchantsId, -1);
                }
            }
        });

        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (rightAdapter.getItemViewType(position) == MultipleItem.MERCHANTS) {
                    //商铺详情页
                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsList.get(position).getMerchantId());
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                    startActivity(intent);
                }
            }
        });
        rightAdapterRecomm.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (rightAdapterRecomm.getItemViewType(position) == MultipleItem.MERCHANTS) {
                    //商铺详情页
                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsRecommList.get(position).getMerchantId());
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsRecommListBeans.get(position).getGoodsId());
                    startActivity(intent);
                }
            }
        });

        rightAdapterBou.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (rightAdapterBou.getItemViewType(position) == MultipleItem.MERCHANTS) {
                    //商铺详情页
                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsBouList.get(position).getMerchantId());
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsBouListBeans.get(position).getGoodsId());
                    startActivity(intent);
                }
            }
        });
//        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getContext(), VipActivity.class);
//                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
//                startActivity(intent);
//            }
//        });

        binding.swipeLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int i = leftAdapter.getPos();

                int pos = i;
                if (pos >= leftAdapter.getData().size() - 1) {
                    leftAdapter.setSelect(pos);

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)   binding.leftRecyclerView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(pos, 0);
//                    binding.leftRecyclerView.scrollToPosition(pos);
                } else {
                    pos = pos + 1;
                    leftAdapter.setSelect(pos);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)   binding.leftRecyclerView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(pos, 0);
//                    binding.leftRecyclerView.scrollToPosition(pos);
                }
                leftAdapter.notifyDataSetChanged();
                if (mParam1 == 1) {
                    //获取当前分类的商品
                    clsMearchantsId = merchantsClassifyList.get(pos).getClsId();
                    getMerchants(clsMearchantsId, 0);
                } else {
                    //获取当前分类的商品
                    clsShopId = shopClassifyLsit.get(pos).getClsId();
                    getShops(clsShopId, 0);
                }

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {


                int pos = leftAdapter.getPos();

                if (pos <= 0) {
                    leftAdapter.setSelect(0);
//                    binding.leftRecyclerView.scrollToPosition(pos);
                } else {
                    pos = pos - 1;
                    leftAdapter.setSelect(pos);
//                    binding.leftRecyclerView.scrollToPosition(pos);
                }
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)   binding.leftRecyclerView.getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(pos, 0);
                leftAdapter.notifyDataSetChanged();

                if (mParam1 == 1) {
                    //获取当前分类的商品
                    clsMearchantsId = merchantsClassifyList.get(pos).getClsId();
                    getMerchants(clsMearchantsId, 1);
                } else {
                    //获取当前分类的商品
                    clsShopId = shopClassifyLsit.get(pos).getClsId();
                    getShops(clsShopId, 1);
                }
            }
        });
    }


    public void getShops(int clsId, int type) {

        OkGo.<LzyResponse<ShopsNew>>post(Urls.GET_SHOP1)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("clsId", clsId + "")
                .execute(new JsonCallback<LzyResponse<ShopsNew>>() {
                    @Override
                    public void onStart(Request<LzyResponse<ShopsNew>, ? extends Request> request) {
                        super.onStart(request);
                        LoadingUtil.setLoadingViewShow(true);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<ShopsNew>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        super.onSuccess(getActivity(), "", response.body().code);
                        binding.backgroundImg.setVisibility(View.GONE);
                        if (type == 0) {
                            binding.swipeLayout.finishLoadMore();
                        } else if (type == 1) {
                            binding.swipeLayout.finishRefresh();
                        }
                        multipleItemList.clear();
                        goodsListBeans.clear();

                        multipleRecommItemList.clear();
                        goodsRecommListBeans.clear();

                        goodsBouListBeans.clear();
                        multipleBouItemList.clear();

                        if (response.body().data != null) {
                            ShopsNew data = response.body().data;
                            if (data.getBoutique() == null || data.getBoutique().size() == 0) {
                                binding.boutique.setVisibility(View.GONE);
                            } else {
                                binding.boutique.setVisibility(View.VISIBLE);

                                goodsBouListBeans = data.getBoutique();
                                for (int i = 0; i < goodsBouListBeans.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                                    multipleItem.setGoodsListBean(goodsBouListBeans.get(i));
                                    multipleBouItemList.add(multipleItem);
                                }
                            }

                            if (data.getHot() == null || data.getHot().size() == 0) {
                                binding.hot.setVisibility(View.GONE);
                            } else {
                                binding.hot.setVisibility(View.VISIBLE);

                                goodsListBeans = data.getHot();
                                for (int i = 0; i < goodsListBeans.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                                    multipleItem.setGoodsListBean(goodsListBeans.get(i));
                                    multipleItemList.add(multipleItem);
                                }
                            }

                            if (data.getRecommendFor() == null || data.getRecommendFor().size() == 0) {
                                binding.recommend.setVisibility(View.GONE);
                            } else {
                                binding.recommend.setVisibility(View.VISIBLE);

                                goodsRecommListBeans = data.getRecommendFor();
                                for (int i = 0; i < goodsRecommListBeans.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                                    multipleItem.setGoodsListBean(goodsRecommListBeans.get(i));
                                    multipleRecommItemList.add(multipleItem);
                                }
                            }


                        }

                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();

                        rightAdapterRecomm.setNewData(multipleRecommItemList);
                        rightAdapterRecomm.notifyDataSetChanged();

                        rightAdapterBou.setNewData(multipleBouItemList);
                        rightAdapterBou.notifyDataSetChanged();

                        if (binding.recommend.getVisibility() == View.GONE &&
                                binding.hot.getVisibility() == View.GONE &&
                                binding.boutique.getVisibility() == View.GONE) {
                            binding.backgroundImg.setVisibility(View.VISIBLE);
//                            rightAdapter.setNewData(null);
                        }
                        if (type == 0) {
                            binding.nescro.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.nescro.scrollTo(0, 0);
                                    LoadingUtil.setLoadingViewShow(false);
                                }
                            }, 1000);
                        } else {
                            LoadingUtil.setLoadingViewShow(false);
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<ShopsNew>> response) {
                        super.onError(response);
                        if (type == 0) {
                            binding.swipeLayout.finishLoadMore();
                        } else if (type == 1) {
                            binding.swipeLayout.finishRefresh();
                        }
                        if (response.body()!=null){
                            ToastUtils.show(response.body().msg);
                        }


                        multipleItemList.clear();
                        goodsListBeans.clear();
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();


                        multipleRecommItemList.clear();
                        goodsRecommListBeans.clear();
                        rightAdapterRecomm.setNewData(multipleRecommItemList);
                        rightAdapterRecomm.notifyDataSetChanged();

                        goodsBouListBeans.clear();
                        multipleBouItemList.clear();
                        rightAdapterBou.setNewData(multipleBouItemList);
                        rightAdapterBou.notifyDataSetChanged();
                        LoadingUtil.setLoadingViewShow(false);
                    }
                });
    }

    public void getMerchants(int clsId, int type) {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }

        OkGo.<LzyResponse<MerchantsInfoNew>>get(Urls.GET_MERCHANTS1)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("clsId", clsId + "")
                .params("longitude", longitude + "")
                .params("latitude", latitude + "")
                .execute(new JsonCallback<LzyResponse<MerchantsInfoNew>>() {
                    @Override
                    public void onStart(Request<LzyResponse<MerchantsInfoNew>, ? extends Request> request) {
                        super.onStart(request);
                        LoadingUtil.setLoadingViewShow(true);
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsInfoNew>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        super.onSuccess(getActivity(), "", response.body().code);
                        binding.backgroundImg.setVisibility(View.GONE);
                        if (type == 0) {
                            binding.swipeLayout.finishLoadMore();
                        } else if (type == 1) {
                            binding.swipeLayout.finishRefresh();
                        }
                        multipleItemList.clear();
                        merchantsList.clear();

                        merchantsRecommList.clear();
                        multipleRecommItemList.clear();

                        merchantsBouList.clear();
                        multipleBouItemList.clear();
                        if (response.body().data != null) {
                            MerchantsInfoNew data = response.body().data;
                            if (data.getVeryGood() == null || data.getVeryGood().size() == 0) {
                                binding.boutique.setVisibility(View.GONE);
                            } else {
                                binding.boutique.setVisibility(View.VISIBLE);

                                merchantsBouList = data.getVeryGood();
                                for (int i = 0; i < merchantsBouList.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.MERCHANTS);
                                    multipleItem.setMerchantsModel(merchantsBouList.get(i));
                                    multipleBouItemList.add(multipleItem);
                                }
                            }

                            if (data.getHot() == null || data.getHot().size() == 0) {
                                binding.hot.setVisibility(View.GONE);
                            } else {
                                binding.hot.setVisibility(View.VISIBLE);
                                merchantsList = data.getHot();
                                for (int i = 0; i < merchantsList.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.MERCHANTS);
                                    multipleItem.setMerchantsModel(merchantsList.get(i));
                                    multipleItemList.add(multipleItem);
                                }
                            }

                            if (data.getRecommendFor() == null || data.getRecommendFor().size() == 0) {
                                binding.recommend.setVisibility(View.GONE);
                            } else {
                                binding.recommend.setVisibility(View.VISIBLE);

                                merchantsRecommList = data.getRecommendFor();
                                for (int i = 0; i < merchantsRecommList.size(); i++) {
                                    MultipleItem multipleItem = new MultipleItem(MultipleItem.MERCHANTS);
                                    multipleItem.setMerchantsModel(merchantsRecommList.get(i));
                                    multipleRecommItemList.add(multipleItem);
                                }
                            }

                        }
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();

                        rightAdapterRecomm.setNewData(multipleRecommItemList);
                        rightAdapterRecomm.notifyDataSetChanged();

                        rightAdapterBou.setNewData(multipleBouItemList);
                        rightAdapterBou.notifyDataSetChanged();
                        if (binding.recommend.getVisibility() == View.GONE &&
                                binding.hot.getVisibility() == View.GONE &&
                                binding.boutique.getVisibility() == View.GONE) {
                            binding.backgroundImg.setVisibility(View.VISIBLE);
//                            rightAdapter.setNewData(null);
                        }
                        if (type == 0) {
                            binding.nescro.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TAG", "run: ");
                                    binding.nescro.scrollTo(0, 0);
                                    LoadingUtil.setLoadingViewShow(false);

                                }
                            }, 500);
                        } else {
                            LoadingUtil.setLoadingViewShow(false);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsInfoNew>> response) {
                        super.onError(response);
                        if (type == 0) {
                            binding.swipeLayout.finishLoadMore();
                        } else if (type == 1) {
                            binding.swipeLayout.finishRefresh();
                        }
                        if (response.body()!=null){
                            ToastUtils.show(response.body().msg);
                        }

                        multipleItemList.clear();
                        merchantsList.clear();
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();

                        multipleRecommItemList.clear();
                        merchantsRecommList.clear();
                        rightAdapterRecomm.setNewData(multipleRecommItemList);
                        rightAdapterRecomm.notifyDataSetChanged();

                        merchantsBouList.clear();
                        multipleBouItemList.clear();
                        rightAdapterBou.setNewData(multipleBouItemList);
                        rightAdapterBou.notifyDataSetChanged();
                        LoadingUtil.setLoadingViewShow(false);
                    }
                });
    }

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
                if (token == null || TextUtils.isEmpty(token)) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), PersonalCenterActivity2.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getActivity(), SearchShopActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_home_nv_right:
                requestPermission();
                break;
        }

    }

    private void requestPermission() {
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

    /*
     * 获取头像
     * */
    private void showHeadImg() {
        mineInfoModel = UserInfoUtils.getUserInfo(getActivity());
        String headImg = mineInfoModel.getHeadImg();
        Glide.with(mContext).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(binding.ivHead);
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = province.id;
        Constant.cityId = city.id;
        Constant.mCity = city.name;
        binding.txtLocation.setText(city.name);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            binding.txtLocation.setText(Constant.mCity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        if (event == EDITOR_INFO) {
            showHeadImg();
        }
    }
}
