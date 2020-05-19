package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.os.Bundle;
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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.MerchantsInfoNew;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.shop.adapter.LeftAdapter1;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapterShopMain;
import com.feitianzhu.huangliwo.shop.model.ShopClassifyBean;
import com.feitianzhu.huangliwo.shop.request.ClassifyGoodsListRequest;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
//
//        if (mParam1 == 1) { //商家
//            line1.setVisibility(View.GONE);
//            line.setVisibility(View.VISIBLE);
////            button1.setSelected(true);
////            button2.setSelected(false);
//        } else {//商城
//            line1.setVisibility(View.VISIBLE);
//            line.setVisibility(View.GONE);
////            button1.setSelected(false);
////            button2.setSelected(true);
//        }
        getShops();
        showHeadImg();
        initListener();
        return view;
    }


    public void initListener() {
//        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                leftAdapter.setSelect(position);
//                leftAdapter.notifyDataSetChanged();
//                if (leftAdapter.getItemViewType(position) == MultiItemShopAndMerchants.SHOP_TYPE) {
//                    //获取当前分类的商品
//                    clsShopId = shopClassifyLsit.get(position).getClsId();
//                    getShops(clsShopId);
//                } else {
//                    //获取当前分类的商品
//                    clsMearchantsId = merchantsClassifyList.get(position).getClsId();
//                    getMerchants(clsMearchantsId);
//                }
//            }
//        });

//        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (rightAdapter.getItemViewType(position) == MultipleItem.MERCHANTS) {
//                    //商铺详情页
//                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
//                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsList.get(position).getMerchantId());
//                    startActivity(intent);
//                } else {
//                    //商品详情
//                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
//                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
//                    startActivity(intent);
//                }
//            }
//        });
//        rightAdapterRecomm.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (rightAdapterRecomm.getItemViewType(position) == MultipleItem.MERCHANTS) {
//                    //商铺详情页
//                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
//                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsRecommList.get(position).getMerchantId());
//                    startActivity(intent);
//                } else {
//                    //商品详情
//                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
//                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsRecommListBeans.get(position).getGoodsId());
//                    startActivity(intent);
//                }
//            }
//        });
//
//        rightAdapterBou.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (rightAdapterBou.getItemViewType(position) == MultipleItem.MERCHANTS) {
//                    //商铺详情页
//                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
//                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, merchantsBouList.get(position).getMerchantId());
//                    startActivity(intent);
//                } else {
//                    //商品详情
//                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
//                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsBouListBeans.get(position).getGoodsId());
//                    startActivity(intent);
//                }
//            }
//        });
//        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getContext(), VipActivity.class);
//                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
//                startActivity(intent);
//            }
//        });

//        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                if (mParam1 == 1) {
//                    getMerchants(clsMearchantsId);
//                } else {
//                    getShops(clsShopId);
//                }
//            }
//        });
    }


    public void getShops() {
        ClassifyGoodsListRequest goodsListRequest = new ClassifyGoodsListRequest();
        goodsListRequest.accessToken = token;
        goodsListRequest.userId = userId;
        goodsListRequest.call(new ApiCallBack<List<ShopClassifyBean>>() {
            @Override
            public void onAPIResponse(List<ShopClassifyBean> response) {
                //初始化左边列表
                if (leftAdapter == null) {
                    leftAdapter = new LeftAdapter1(ShopClassifyBean.getTitleList(response));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {

                    };
//                    linearLayoutManager.
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    leftRecyclerView.setLayoutManager(linearLayoutManager);
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
                    rightRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rightRecycle.setAdapter(rightAdapterShopMain);
                    rightRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rightRecycle.getLayoutManager();
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            leftAdapter.setSelect(firstVisibleItemPosition);
                            leftAdapter.notifyDataSetChanged();
//                        rightRecycle.getLayoutManager().getPosition()
//                        leftAdapter.setSelect();
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });
                }

                rightAdapterShopMain.notifyDataSetChanged();
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });

    }

    public void getMerchants(int clsId) {
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
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsInfoNew>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        goneloadDialog();

                        mSwipeLayout.finishRefresh();

//                        rightAdapterRecomm.setNewData(multipleRecommItemList);
//                        rightAdapterRecomm.notifyDataSetChanged();
//
//                        rightAdapterBou.setNewData(multipleBouItemList);
//                        rightAdapterBou.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsInfoNew>> response) {
                        super.onError(response);
                        mSwipeLayout.finishRefresh(false);
                        ToastUtils.show(response.body().msg);


                        goneloadDialog();
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
//                getMerchantsClass();
                break;
            case R.id.button2:
                mParam1 = 2;
                line1.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
//                button1.setSelected(false);
//                button2.setSelected(true);
//                getShopClass();
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
