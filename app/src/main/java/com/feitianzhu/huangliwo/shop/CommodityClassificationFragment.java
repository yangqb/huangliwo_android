package com.feitianzhu.huangliwo.shop;

import android.Manifest;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MerchantsInfo;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiItemShopAndMerchants;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.model.Shops;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.adapter.LeftAdapter;
import com.feitianzhu.huangliwo.shop.adapter.RightAdapter;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.socks.library.KLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
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
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 2:22
 * <p>
 * 商品分类页面
 */


public class CommodityClassificationFragment extends SFFragment implements ProvinceCallBack {
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
    @BindView(R.id.right_recyclerView)
    RecyclerView rightRecyclerView;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1 = 2;
    private String mParam2;
    Unbinder unbinder;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<MerchantsClassifyModel.ListBean> merchantsClassifyList = new ArrayList<>();
    private List<MultiItemShopAndMerchants> multiItemShopAndMerchantsClass = new ArrayList<>();
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<MerchantsModel> merchantsList = new ArrayList<>();
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    private int clsShopId;
    private int clsMearchantsId;
    private String token;
    private String userId;
    private double longitude;
    private double latitude;
    private MineInfoModel mineInfoModel = new MineInfoModel();

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
        View view = inflater.inflate(R.layout.fragment_commodity_classification, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);

        leftAdapter = new LeftAdapter(multiItemShopAndMerchantsClass);
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        leftRecyclerView.setAdapter(leftAdapter);
        leftAdapter.notifyDataSetChanged();

        rightAdapter = new RightAdapter(multipleItemList);
        View mEmptyView = View.inflate(getActivity(), R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rightAdapter.setEmptyView(mEmptyView);
        rightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rightRecyclerView.setAdapter(rightAdapter);
        rightAdapter.notifyDataSetChanged();
        mSwipeLayout.setEnableLoadMore(false);

        if (mParam1 == 1) { //商家
            button1.setSelected(true);
            button2.setSelected(false);
            getMerchantsClass();
        } else {//商城
            button1.setSelected(false);
            button2.setSelected(true);
            getShopClass();
        }
        requestData();
        initListener();
        return view;
    }

    public void getMerchantsClass() {
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS_TYPE)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<MerchantsClassifyModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MerchantsClassifyModel response, int id) {
                        merchantsClassifyList = response.getList();
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
                        getMerchants(clsMearchantsId);
                    }
                });
    }

    public void getShopClass() {
        OkHttpUtils.post()
                .url(Urls.GET_SHOP_CLASS)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, ShopClassify.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        goneloadDialog();
                        Toast.makeText(getActivity(), TextUtils.isEmpty(e.getMessage()) ? "加载失败，请重试" : e.getMessage(), Toast.LENGTH_SHORT).show();
                        KLog.e(e);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ShopClassify shopClassify = (ShopClassify) response;
                        shopClassifyLsit = shopClassify.getGGoodsClsList();
                        multiItemShopAndMerchantsClass.clear();
                        for (int i = 0; i < shopClassifyLsit.size(); i++) {
                            MultiItemShopAndMerchants multiItemShopAndMerchants = new MultiItemShopAndMerchants(MultiItemShopAndMerchants.SHOP_TYPE);
                            multiItemShopAndMerchants.setShopClassifyModel(shopClassifyLsit.get(i));
                            multiItemShopAndMerchantsClass.add(multiItemShopAndMerchants);
                        }
                        leftAdapter.setSelect(0);
                        leftAdapter.setNewData(multiItemShopAndMerchantsClass);
                        leftAdapter.notifyDataSetChanged();
                        clsShopId = shopClassifyLsit.get(0).getClsId();
                        getShops(clsShopId);
                    }
                });
    }

    public void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                leftAdapter.setSelect(position);
                leftAdapter.notifyDataSetChanged();
                if (leftAdapter.getItemViewType(position) == MultiItemShopAndMerchants.SHOP_TYPE) {
                    //获取当前分类的商品
                    clsShopId = shopClassifyLsit.get(position).getClsId();
                    getShops(clsShopId);
                } else {
                    //获取当前分类的商品
                    clsMearchantsId = merchantsClassifyList.get(position).getClsId();
                    getMerchants(clsMearchantsId);
                }
            }
        });

        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (rightAdapter.getItemViewType(position) == MultipleItem.MERCHANTS) {
                    //套餐详情页
                    Intent intent = new Intent(getActivity(), ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANT_DATA, merchantsList.get(position));
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position).getGoodsId());
                    startActivity(intent);
                }
            }
        });

        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
            }
        });

        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mParam1 == 1) {
                    getMerchants(clsMearchantsId);
                } else {
                    getShops(clsShopId);
                }
            }
        });
    }


    public void getShops(int clsId) {
        OkHttpUtils.post()
                .url(Urls.GET_SHOP)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("cls_id", clsId + "")
                .build()
                .execute(new Callback<Shops>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSwipeLayout.finishRefresh(false);
                        ToastUtils.showShortToast(e.getMessage());
                        multipleItemList.clear();
                        goodsListBeans.clear();
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(Shops response, int id) {
                        mSwipeLayout.finishRefresh();
                        goneloadDialog();
                        multipleItemList.clear();
                        goodsListBeans.clear();
                        goodsListBeans = response.getGoodslist();
                        for (int i = 0; i < goodsListBeans.size(); i++) {
                            MultipleItem multipleItem = new MultipleItem(MultipleItem.GOODS);
                            multipleItem.setGoodsListBean(goodsListBeans.get(i));
                            multipleItemList.add(multipleItem);
                        }
                        rightRecyclerView.smoothScrollToPosition(0);
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getMerchants(int clsId) {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("clsId", clsId + "")
                .addParams("longitude", longitude + "")
                .addParams("latitude", latitude + "")
                .build()
                .execute(new Callback<MerchantsInfo>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSwipeLayout.finishRefresh(false);
                        ToastUtils.showShortToast(e.getMessage());
                        multipleItemList.clear();
                        merchantsList.clear();
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(MerchantsInfo response, int id) {
                        goneloadDialog();
                        mSwipeLayout.finishRefresh();
                        multipleItemList.clear();
                        merchantsList.clear();
                        merchantsList = response.getList();
                        for (int i = 0; i < merchantsList.size(); i++) {
                            MultipleItem multipleItem = new MultipleItem(MultipleItem.MERCHANTS);
                            multipleItem.setMerchantsModel(merchantsList.get(i));
                            multipleItemList.add(multipleItem);
                        }
                        rightRecyclerView.smoothScrollToPosition(0);
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick({R.id.ll_location, R.id.iv_head, R.id.search, R.id.button1, R.id.button2, R.id.iv_home_nv_right})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_location:
                ProvinceDialog2 branchDialog = ProvinceDialog2.newInstance();
                branchDialog.setAddress("北京市", "东城区", "东华门街道");
                branchDialog.setSelectOnListener(this);
                branchDialog.show(getChildFragmentManager());
                break;
            case R.id.iv_head: //
                intent = new Intent(getActivity(), PersonalCenterActivity2.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getActivity(), SearchShopActivity.class);
                startActivity(intent);
                break;
            case R.id.button1:
                mParam1 = 1;
                button1.setSelected(true);
                button2.setSelected(false);
                getMerchantsClass();
                break;
            case R.id.button2:
                mParam1 = 2;
                button1.setSelected(false);
                button2.setSelected(true);
                getShopClass();
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

    /*
     * 获取头像
     * */
    private void requestData() {
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
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
                            String headImg = response.getHeadImg();
                            Glide.with(mContext).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                                    .into(ivHead);
                        }
                    }
                });
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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        if (event == EDITOR_INFO) {
            /*
              启动App就去个人中心编辑个人信息，这里会收到信息，需要先获取userID，token不然会报错
            * */
            requestData();
        }
    }
}
