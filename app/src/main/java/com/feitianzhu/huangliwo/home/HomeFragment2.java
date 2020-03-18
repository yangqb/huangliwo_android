package com.feitianzhu.huangliwo.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.home.adapter.HAdapter;
import com.feitianzhu.huangliwo.home.adapter.HomeRecommendAdapter2;
import com.feitianzhu.huangliwo.home.entity.HomeEntity;
import com.feitianzhu.huangliwo.home.entity.NoticeModel;
import com.feitianzhu.huangliwo.home.entity.ShopAndMerchants;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.HomePopModel;
import com.feitianzhu.huangliwo.model.HomeShops;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.plane.PlaneHomeActivity;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.shop.ShopsActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomNerYearPopView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.socks.library.KLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.login.LoginEvent.EDITOR_INFO;

/**
 * @class name：com.feitianzhu.fu700.home
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:35
 */
public class HomeFragment2 extends SFFragment implements ProvinceCallBack {
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
    private List<ShopAndMerchants> shopAndMerchants = new ArrayList<>();
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<BaseGoodsListBean> shopsLists = new ArrayList<>();
    private HomeRecommendAdapter2 mAdapter;
    private HAdapter hAdapter;
    private HomeEntity mHomeEntity;
    private List<HomeEntity.BannerListBean> mBanners = new ArrayList<>();
    private boolean isLoadMore;
    private String token;
    private String userId;
    private int pageNo = 1;
    private MineInfoModel mineInfoModel = new MineInfoModel();

    public HomeFragment2() {

    }

    public static HomeFragment2 newInstance() {
        return new HomeFragment2();
    }

    private CallbackBFragment mCallbackBFragment;

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
        hList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        hList.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();

        //商品
        mAdapter = new HomeRecommendAdapter2(shopAndMerchants);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
        hList.setNestedScrollingEnabled(false);
        //商家
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
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
                intent.putExtra(ShopsActivity.CLASSES_DATA, goodsClsListBean);
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
                getData();
                getNotice();
                requestData();
                getClasses();
                getGoodsData();
            }
        });
    }

    @OnClick({R.id.ll_location, R.id.iv_head, R.id.rl_ticket, R.id.rl_financial, R.id.rl_travel, R.id.rl_mall, R.id.rl_merchants, R.id.search, R.id.iv_home_nv_right})
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
                ToastUtils.showShortToast("敬请期待");
               /* intent = new Intent(getActivity(), PlaneHomeActivity.class);
                startActivity(intent);*/
                break;
            case R.id.rl_financial:
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.rl_travel:
                ToastUtils.showShortToast("敬请期待");
                break;
            case R.id.search:
                JumpActivity(getActivity(), SearchShopActivity.class);
                break;
            case R.id.iv_home_nv_right:
                requestPermission();
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
        OkGo.<LzyResponse<HomeEntity>>get(Urls.GET_INDEX)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
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

        OkGo.<LzyResponse<HomeShops>>get(Urls.GET_HOME_GOODS_LIST)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("limitNum", Constant.PAGE_SIZE)
                .params("curPage", pageNo + "")
                .execute(new JsonCallback<LzyResponse<HomeShops>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<HomeShops>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
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
                });
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
            ToastUtils.showShortToast("数据加载失败，请重新获取");
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
                // ToastUtils.showShortToast("敬请期待");
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

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CAMERA
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
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                intent.putExtra(ScannerActivity.IS_MERCHANTS, mineInfoModel.getIsMerchant());
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(getActivity(), "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class DataViewHolder implements ViewHolder<HomeEntity.BannerListBean> {
        private RoundedImageView mImageView;

        @Override
        public View createView(ViewGroup viewGroup, Context context, int position) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, HomeEntity.BannerListBean data, final int position, final int size) {
            Glide.with(context).load(data.imagUrl).apply(new RequestOptions().error(R.mipmap.g10_01weijiazai).placeholder(R.mipmap.g10_01weijiazai)).into(mImageView);
        }
    }

    /*
     * 获取头像
     * */
    private void requestData() {
        OkGo.<LzyResponse<MineInfoModel>>get(Common_HEADER + POST_MINE_INFO)
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

