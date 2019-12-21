package com.feitianzhu.fu700.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.App;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.base.SFFragment;
import com.feitianzhu.fu700.home.adapter.HAdapter;
import com.feitianzhu.fu700.home.adapter.HomeRecommendAdapter2;
import com.feitianzhu.fu700.home.entity.HomeEntity;
import com.feitianzhu.fu700.home.entity.ShopAndMerchants;
import com.feitianzhu.fu700.me.ui.PersonalCenterActivity2;
import com.feitianzhu.fu700.me.ui.PushServiceActivity;
import com.feitianzhu.fu700.me.ui.ScannerActivity;
import com.feitianzhu.fu700.me.ui.ServiceDetailActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.Province;
import com.feitianzhu.fu700.shop.ShopHelp;
import com.feitianzhu.fu700.shop.ShopSetMealActivity;
import com.feitianzhu.fu700.shop.ShopsDetailActivity;
import com.feitianzhu.fu700.shop.ui.ShopSearchActivity;
import com.feitianzhu.fu700.shop.ui.ShopsActivity;
import com.feitianzhu.fu700.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.fu700.shop.ui.dialog.ProvincehDialog;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.view.CircleImageView;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.ISADMIN;
import static com.feitianzhu.fu700.common.Constant.MERCHANTID;
import static com.feitianzhu.fu700.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.USERID;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;

/**
 * @class name：com.feitianzhu.fu700.home
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:35
 */
public class HomeFragment2 extends SFFragment implements ProvinceCallBack, View.OnClickListener {
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
    @BindView(R.id.rl_merchants)
    RelativeLayout rlMerchants;
    @BindView(R.id.rl_mall)
    RelativeLayout rlMall;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    private List<ShopAndMerchants> shopAndMerchants = new ArrayList<>();
    private List<HomeEntity.RecommendListBean> recommendListBeanList = new ArrayList<>();
    private List<HomeEntity.ServiceRecommendListBean> serviceRecommendList = new ArrayList<>();
    private List<BaseGoodsListBean> shopsLists = new ArrayList<>();
    private View mHeader;
    private HomeRecommendAdapter2 mAdapter;
    private HAdapter hAdapter;
    private HomeEntity mHomeEntity;
    private PopupWindow popupWindow;
    private View vPopupWindow;
    private List<HomeEntity.BannerListBean> mBanners = new ArrayList<>();
    private boolean isLoadMore;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        vPopupWindow = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        // View vPopupWindow = View.inflate(getActivity(), R.layout.layout_popupwindow, null);//引入弹窗布局
        popupWindow = new PopupWindow(vPopupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        vPopupWindow.findViewById(R.id.iv_saoyisao).setOnClickListener(this);
        vPopupWindow.findViewById(R.id.iv_shoukuan).setOnClickListener(this);
        vPopupWindow.findViewById(R.id.iv_ludan).setOnClickListener(this);
        vPopupWindow.findViewById(R.id.iv_fabufuwu).setOnClickListener(this);
        rlMerchants.setOnClickListener(this);
        rlMall.setOnClickListener(this);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        hAdapter = new HAdapter(serviceRecommendList);
        hList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        hList.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();

        //商品
        mAdapter = new HomeRecommendAdapter2(shopAndMerchants);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
        hList.setNestedScrollingEnabled(false);
        //商家

        ivRight.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);
       mSwipeLayout.setEnableLoadMore(false);
        getData();
        requestData();
        initListener();
        return view;
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == ShopAndMerchants.TYPE_SERIES) {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, shopsLists.get(position));
                    startActivity(intent);
                } else {
                    //套餐详情页
                    Intent intent = new Intent(getActivity(), ShopSetMealActivity.class);
                    intent.putExtra(ShopSetMealActivity.MERCHANT_DATA, recommendListBeanList.get(position - shopsLists.size()));
                    startActivity(intent);
                }
                //startShopsActivity(mHomeEntity.recommendList.get(position).merchantId);
            }
        });

        hAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mHomeEntity != null && mHomeEntity.serviceRecommendList != null && position < mHomeEntity.serviceRecommendList.size()) {
                    HomeEntity.ServiceRecommendListBean serviceRecommendBean = mHomeEntity.serviceRecommendList.get(position);
                    Intent intent = new Intent(getActivity(), ShopSetMealActivity.class);
                    intent.putExtra(ShopSetMealActivity.SERVICE_RECOMMEND_BEAN, serviceRecommendBean);
                    startActivity(intent);
                }
            }
        });

        mSwipeLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = false;
                requestData();
                getData();
            }
        });
    }

    @OnClick({R.id.ll_location, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                ProvincehDialog branchDialog = ProvincehDialog.newInstance(getActivity());
                branchDialog.setAddress("北京市", "北京市");
                branchDialog.setSelectOnListener(this);
                branchDialog.show(getChildFragmentManager());
                break;
            case R.id.iv_head: //
                Intent intent = new Intent(getActivity(), PersonalCenterActivity2.class);
                startActivity(intent);
                break;
        }

    }

    public void getData() {
        OkHttpUtils
                .post()
                .url(Urls.GET_INDEX)
                .addParams("accessToken", Constant.ACCESS_TOKEN)
                .addParams("userId", Constant.LOGIN_USERID)
                .build()
                .execute(new Callback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        goneloadDialog();
                        return new Gson().fromJson(mData, HomeEntity.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        goneloadDialog();
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh(false);
                        } else {
                            mSwipeLayout.finishLoadMore(false);
                        }
                        Toast.makeText(getActivity(), TextUtils.isEmpty(e.getMessage()) ? "加载失败，请重试" : e.getMessage(), Toast.LENGTH_SHORT).show();
                        KLog.e(e);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goneloadDialog();
                        if (!isLoadMore) {
                            mSwipeLayout.finishRefresh();
                        } else {
                            mSwipeLayout.finishLoadMore();
                        }

                        mHomeEntity = (HomeEntity) response;

                        //1.set banner
                        if (mHomeEntity.bannerList != null && !mHomeEntity.bannerList.isEmpty()) {
                            mBanners.clear();
                            mBanners.addAll(mHomeEntity.bannerList);
                            mViewpager.setCanLoop(true)
                                    .setAutoPlay(true)
                                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                                    //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                                    .setRoundCorner(20)
                                    .setIndicatorRadius(8)
                                    .setIndicatorColor(Color.parseColor("#FFFFFF"), Color.parseColor("#6C6D72"))
                                    .setHolderCreator(DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                                @Override
                                public void onPageClick(int position) {
                                    onClickBanner(position);
                                }
                            }).create(mBanners);

                            mViewpager.startLoop();

                        }


                        shopAndMerchants.clear();
                        //商品
                        if (mHomeEntity.goodsList != null) {
                            shopsLists = mHomeEntity.goodsList;
                            for (int i = 0; i < mHomeEntity.goodsList.size(); i++) {
                                ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_SERIES);
                                entity.setShopsList(mHomeEntity.goodsList.get(i));
                                shopAndMerchants.add(entity);
                            }
                        }

                        //商家
                        if (mHomeEntity.recommendList != null) {
                            recommendListBeanList = mHomeEntity.recommendList;
                            for (int i = 0; i < mHomeEntity.recommendList.size(); i++) {
                                ShopAndMerchants entity = new ShopAndMerchants(ShopAndMerchants.TYPE_PESALE);
                                entity.setRecommendListBean(mHomeEntity.recommendList.get(i));
                                shopAndMerchants.add(entity);
                            }
                        }
                        mAdapter.setNewData(shopAndMerchants);
                        mAdapter.notifyDataSetChanged();


                        //热门服务
                        if (mHomeEntity.serviceRecommendList != null && !mHomeEntity.serviceRecommendList.isEmpty()) {
                            serviceRecommendList.clear();
                            serviceRecommendList.addAll(mHomeEntity.serviceRecommendList);
                            hAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mViewpager != null) {
            mViewpager.stopLoop();
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
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
        if (mViewpager != null)
            mViewpager.startLoop();
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
        //链接类型（1：商户，2：服务，3：文章，4：外部链接）
        switch (mHomeEntity.bannerList.get(i).linkType) {
            case 1: //商家详情
                startShopsActivity(mHomeEntity.bannerList.get(i).idValue);
                break;
            case 2: //商品详情（以前的服务详情）
                JumpActivity(getContext(), ServiceDetailActivity.class, mHomeEntity.bannerList.get(i).idValue);
                break;
            case 3:
                ToastUtils.showShortToast("待开发");
                //WebViewActivity.startActivity(getActivity(), mHomeEntity.bannerList.get(i).outUrl, "");
                break;
            case 4:
                ToastUtils.showShortToast("待开发");
                // WebViewActivity.startActivity(getActivity(), mHomeEntity.bannerList.get(i).outUrl, "");
                break;

        }
    }

    private void startShopsActivity(int id) {
        Intent intent = new Intent(getActivity(), ShopsActivity.class);
        intent.putExtra(ISADMIN, false);
        intent.putExtra(MERCHANTID, id + "");
        startActivity(intent);
    }

    private void JumpActivity(Context context, Class<ServiceDetailActivity> clazz, int mId) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("serviceid", mId + "");
        context.startActivity(intent);
    }

    private void JumpActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.search:
                JumpActivity(getActivity(), ShopSearchActivity.class);
                break;
            case R.id.iv_home_nv_right:
                vPopupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popupWindow.showAsDropDown(ivRight, -vPopupWindow.getMeasuredWidth() + ivRight.getWidth() - 3, 0);
                break;
            case R.id.iv_saoyisao:
                requestPermission();
                popupWindow.dismiss();
                break;
            case R.id.iv_shoukuan:
                popupWindow.dismiss();
                ToastUtils.showShortToast("待开发");
                /*Intent collIntent = new Intent(getActivity(), CollectMoneyActivity.class);
                ShopHelp.veriUserShopJumpActivity(getActivity(), collIntent);*/
                break;
            case R.id.iv_ludan:
                popupWindow.dismiss();
                ToastUtils.showShortToast("待开发");
               /* intent = new Intent(getActivity(), ShopRecordActivity.class);
                ShopHelpTwo.veriUserShopJumpActivity(getActivity(), intent);*/
                break;
            case R.id.iv_fabufuwu:
                popupWindow.dismiss();
                //ToastUtils.showShortToast("待开发");
                Intent pushIntent = new Intent(getActivity(), PushServiceActivity.class);
                ShopHelp.veriUserShopJumpActivity(getActivity(), pushIntent);
                break;
            case R.id.rl_merchants: //商家
                mCallbackBFragment.skipToCommodityFragment(1, v);
                break;
            case R.id.rl_mall: //商城
                mCallbackBFragment.skipToCommodityFragment(2, v);
                break;
            case R.id.rl_ticket:
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.rl_financial:
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.rl_travel:
                ToastUtils.showShortToast("待开发");
                break;
        }
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
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback<MineInfoModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MineInfoModel response, int id) {
                        String headImg = response.getHeadImg();
                        Glide.with(mContext).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).dontAnimate())
                                .into(ivHead);
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackBFragment = null;
    }
}

