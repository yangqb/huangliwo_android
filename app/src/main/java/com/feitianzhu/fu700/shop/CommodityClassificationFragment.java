package com.feitianzhu.fu700.shop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.App;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.base.SFFragment;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.ui.ScannerActivity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.MultipleItem;
import com.feitianzhu.fu700.model.Province;
import com.feitianzhu.fu700.model.ShopClassify;
import com.feitianzhu.fu700.model.Shops;
import com.feitianzhu.fu700.shop.adapter.LeftAdapter;
import com.feitianzhu.fu700.shop.adapter.RightAdapter;
import com.feitianzhu.fu700.shop.ui.ShopSearchActivity;
import com.feitianzhu.fu700.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.fu700.shop.ui.dialog.ProvincehDialog;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.view.CircleImageView;
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

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 2:22
 * <p>
 * 商品分类页面
 */


public class CommodityClassificationFragment extends SFFragment implements View.OnClickListener, ProvinceCallBack {
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
    private int mParam1 = 1;
    private String mParam2;
    Unbinder unbinder;
    private PopupWindow popupWindow;
    private View vPopupWindow;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<BaseGoodsListBean> goodsListBeans = new ArrayList<>();
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    private int clsId;

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

        ivRight.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);

        if (mParam1 == 1) { //商家
            button1.setSelected(true);
            button2.setSelected(false);
        } else {//商城
            button1.setSelected(false);
            button2.setSelected(true);
        }

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


        leftAdapter = new LeftAdapter(shopClassifyLsit);
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
        requestData();
        initListener();
        return view;
    }

    public void initData() {
        OkHttpUtils.post()
                .url(Urls.GET_SHOP_CLASS)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
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
                        leftAdapter.setSelect(0);
                        leftAdapter.setNewData(shopClassifyLsit);
                        leftAdapter.notifyDataSetChanged();
                        clsId = shopClassifyLsit.get(0).getClsId();
                        getShops(clsId);
                    }
                });


    }

    public void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                leftAdapter.setSelect(position);
                leftAdapter.notifyDataSetChanged();
                //获取当前分类的商品
                clsId = shopClassifyLsit.get(position).getClsId();
                getShops(clsId);
            }
        });

        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = adapter.getItemViewType(position);
                if (mParam1 == 1) {
                    //套餐详情页
                    Intent intent = new Intent(getActivity(), ShopSetMealActivity.class);
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position));
                    startActivity(intent);
                }
                /*if (type == 1) {
                    //商家
                    Intent intent = new Intent(getActivity(), ShopSetMealActivity.class);
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsListBeans.get(position));
                    startActivity(intent);
                }*/

            }
        });

        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getShops(clsId);
            }
        });
    }


    public void getShops(int clsId) {
        OkHttpUtils.post()
                .url(Urls.GET_SHOP)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("cls_id", clsId + "")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        goneloadDialog();
                        return new Gson().fromJson(mData, Shops.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSwipeLayout.finishRefresh(false);
                        ToastUtils.showShortToast(e.getMessage());
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        mSwipeLayout.finishRefresh();
                        goneloadDialog();
                        Shops shops = (Shops) response;
                        multipleItemList.clear();
                        goodsListBeans.clear();
                        goodsListBeans = shops.getGoodslist();
                        for (int i = 0; i < goodsListBeans.size(); i++) {
                            MultipleItem multipleItem = new MultipleItem(MultipleItem.IMG);
                            multipleItem.setGoodsListBean(goodsListBeans.get(i));
                            multipleItemList.add(multipleItem);
                        }
                        rightAdapter.setNewData(multipleItemList);
                        rightAdapter.notifyDataSetChanged();
                    }
                });

    }

    @OnClick(R.id.ll_location)
    public void onViewClicked() {
        ProvincehDialog branchDialog = ProvincehDialog.newInstance(getActivity());
        branchDialog.setAddress("北京市", "北京市");
        branchDialog.setSelectOnListener(this);
        branchDialog.show(getChildFragmentManager());
    }

    @Override
    public void onStart() {
        super.onStart();
        KLog.e("onStart");
        ShopDao.loadUserAuthImpl();
    }

    @Override
    public void onResume() {
        super.onResume();
        KLog.e("onResume");
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                JumpActivity(getActivity(), ShopSearchActivity.class);
                break;
            case R.id.iv_home_nv_right:
                // popupWindow.showAtLocation(ivRight, Gravity.BOTTOM, 0, 0);
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
               /* Intent intent = new Intent(getActivity(), ShopRecordActivity.class);
                ShopHelpTwo.veriUserShopJumpActivity(getActivity(), intent);*/
                break;
            case R.id.iv_fabufuwu:
                popupWindow.dismiss();
                ToastUtils.showShortToast("待开发");
                /*Intent pushIntent = new Intent(getActivity(), PushServiceActivity.class);
                ShopHelp.veriUserShopJumpActivity(getActivity(), pushIntent);*/
                break;
            case R.id.button1:
                mParam1 = 1;
                button1.setSelected(true);
                button2.setSelected(false);
                shopClassifyLsit.clear();
                multipleItemList.clear();
                rightAdapter.notifyDataSetChanged();
                leftAdapter.notifyDataSetChanged();
                //initData();
                break;
            case R.id.button2:
                mParam1 = 2;
                button1.setSelected(false);
                button2.setSelected(true);
                initData();
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

    private void JumpActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = province.id;
        Constant.cityId = city.id;
        Constant.mCity = city.name;
        mTxtLocation.setText(city.name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        switch (event) {
            case LOGIN_SUCCESS:
                requestData();
                break;
        }
    }
}
