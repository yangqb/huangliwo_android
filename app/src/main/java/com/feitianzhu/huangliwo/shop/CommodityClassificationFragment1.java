package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.adapter.LeftAdapter1;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapterMerchantMain;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapterShopMain;
import com.feitianzhu.huangliwo.shop.model.MerchantBean;
import com.feitianzhu.huangliwo.shop.model.ShopClassifyBean;
import com.feitianzhu.huangliwo.shop.request.ClassifyGoodsListRequest;
import com.feitianzhu.huangliwo.shop.request.MerchantListRequest;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.login.LoginEvent.EDITOR_INFO;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 2:22
 * <p>
 * 商品分类页面
 */


public class CommodityClassificationFragment1 extends SFFragment implements ProvinceCallBack {
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout mSwipeLayout;
    @BindView(R.id.search)
    LinearLayout mSearchLayout;
    @BindView(R.id.txt_location)
    TextView mTxtLocation;
    @BindView(R.id.iv_home_nv_right)
    ImageView ivRight;
    @BindView(R.id.button1)
    TextView button1;
    @BindView(R.id.button2)
    TextView button2;
    @BindView(R.id.left_recyclerView)
    RecyclerView leftRecyclerView;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.rightRecycle)
    RecyclerView rightRecycle;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;


    private int mParam1 = 2;
    private String mParam2;
    Unbinder unbinder;
    private LeftAdapter1 leftAdapter;
    private String token;
    private String userId;
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private RightAdapterShopMain rightAdapterShopMain;


    public CommodityClassificationFragment1() {

    }

    public static CommodityClassificationFragment1 newInstance(int param1, String param2) {
        CommodityClassificationFragment1 fragment = new CommodityClassificationFragment1();
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
        View view = inflater.inflate(R.layout.fragment_commodity_classification, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);


        mSwipeLayout.setEnableLoadMore(false);

        if (mParam1 == 1) { //商家
            line1.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
//            button1.setSelected(false);
//            button2.setSelected(true);
            getMerchants();
        } else {//商城
            line1.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);

//            button1.setSelected(true);
//            button2.setSelected(false);
            getShops();

        }

        showHeadImg();
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mParam1 == 1) { //商家
                    getMerchants();
                } else {//商城
                    getShops();
                }
            }
        });
        rightRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        leftRecyclerView.setLayoutManager(linearLayoutManager);
        rightRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rightRecycle.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
//                if (firstCompletelyVisibleItemPosition == 0) {
//                    leftAdapter.setSelect(0);
//                    leftRecyclerView.scrollToPosition(0);
////顶部
//                } else {
//                    leftAdapter.setSelect(firstVisibleItemPosition);
//                    leftRecyclerView.scrollToPosition(firstVisibleItemPosition);
//                }


                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
//底部
                    leftAdapter.setSelect(lastCompletelyVisibleItemPosition);
                    leftRecyclerView.scrollToPosition(lastCompletelyVisibleItemPosition);
                } else {
                    leftAdapter.setSelect(firstVisibleItemPosition);
                    leftRecyclerView.scrollToPosition(firstVisibleItemPosition);
                }
                leftAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    public void getShops() {
        Log.e("TAG", "getShops: ");
        ClassifyGoodsListRequest goodsListRequest = new ClassifyGoodsListRequest();
        goodsListRequest.isShowLoading = true;
        goodsListRequest.accessToken = token;
        goodsListRequest.userId = userId;
        goodsListRequest.call(new ApiCallBack<List<ShopClassifyBean>>() {
            @Override
            public void onAPIResponse(List<ShopClassifyBean> response) {
                //初始化左边列表
                if (leftAdapter == null) {
                    leftAdapter = new LeftAdapter1(ShopClassifyBean.getTitleList(response));
                    leftRecyclerView.setAdapter(leftAdapter);
                    leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            leftAdapter.setSelect(position);
                            leftAdapter.notifyDataSetChanged();
//点击左边滚动右边
                            rightRecycle.stopScroll();
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rightRecycle.getLayoutManager();
                            linearLayoutManager.scrollToPositionWithOffset(position, 0);

                        }
                    });
                } else {
                    leftAdapter.setNewData(ShopClassifyBean.getTitleList(response));
                }
                //初始化分类
                leftAdapter.notifyDataSetChanged();

                if (rightAdapterShopMain != null) {
                    rightAdapterShopMain.setNewData(response);
                } else {
                    rightAdapterShopMain = new RightAdapterShopMain(response);
                    rightAdapterShopMain.callback = new RightAdapterShopMain.Callback() {
                        @Override
                        public void callBack(BaseGoodsListBean baseGoodsListBean) {
                            Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                            intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, baseGoodsListBean.getGoodsId());
                            startActivity(intent);
                        }
                    };

                }
                rightRecycle.setAdapter(rightAdapterShopMain);
                rightAdapterShopMain.notifyDataSetChanged();
                emptyView.setVisibility(View.GONE);
                mSwipeLayout.finishRefresh();

            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                emptyView.setVisibility(View.VISIBLE);
                mSwipeLayout.finishRefresh();

            }
        });

    }

    public void getMerchants() {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }
        MerchantListRequest merchantListRequest = new MerchantListRequest();
        merchantListRequest.latitude = latitude + "";
        merchantListRequest.longitude = longitude + "";
        merchantListRequest.isShowLoading = true;
        merchantListRequest.call(new ApiCallBack<List<MerchantBean>>() {

            private RightAdapterMerchantMain rightAdapterMerchantMain;

            @Override
            public void onAPIResponse(List<MerchantBean> response) {
                //初始化左边列表
                if (leftAdapter == null) {
                    leftAdapter = new LeftAdapter1(MerchantBean.getTitleList(response));
                    leftRecyclerView.setAdapter(leftAdapter);
                    leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                            leftAdapter.setSelect(position);
//                            leftAdapter.notifyDataSetChanged();
//点击左边滚动右边
                            rightRecycle.stopScroll();
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rightRecycle.getLayoutManager();
                            linearLayoutManager.scrollToPositionWithOffset(position, 0);

                        }
                    });
                } else {
                    leftAdapter.setNewData(MerchantBean.getTitleList(response));
                }
                leftAdapter.notifyDataSetChanged();
                //初始化分类

                if (rightAdapterMerchantMain != null) {
                    rightAdapterMerchantMain.setNewData(response);
                } else {
                    rightAdapterMerchantMain = new RightAdapterMerchantMain(response);
                    rightAdapterMerchantMain.callback = new RightAdapterMerchantMain.Callback() {
                        @Override
                        public void callBack(MerchantsModel baseGoodsListBean) {
                            Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                            intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, baseGoodsListBean.getMerchantId());
                            startActivity(intent);
                        }
                    };
                }

                rightRecycle.setAdapter(rightAdapterMerchantMain);
                rightAdapterMerchantMain.notifyDataSetChanged();
                emptyView.setVisibility(View.GONE);
                mSwipeLayout.finishRefresh();
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                emptyView.setVisibility(View.VISIBLE);
                mSwipeLayout.finishRefresh();

            }
        });
    }

    @OnClick({R.id.ll_location, R.id.iv_head, R.id.search, R.id.button1, R.id.button2, R.id.iv_home_nv_right})
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
                token = SPUtils.getString(getContext(), Constant.SP_ACCESS_TOKEN);
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
            case R.id.button1:
                mParam1 = 1;
//                button1.setSelected(true);
//                button2.setSelected(false);
                line1.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
                getMerchants();
                break;
            case R.id.button2:
                mParam1 = 2;
                line1.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
//                button1.setSelected(false);
//                button2.setSelected(true);
                getShops();
                break;
            case R.id.iv_home_nv_right:
                requestPermission();
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                .into(ivHead);
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = province.id;
        Constant.cityId = city.id;
        Constant.mCity = city.name;
        mTxtLocation.setText(city.name);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
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
