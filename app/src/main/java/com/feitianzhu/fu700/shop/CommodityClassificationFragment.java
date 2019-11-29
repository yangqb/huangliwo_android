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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.App;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.base.SFFragment;
import com.feitianzhu.fu700.me.ui.ScannerActivity;
import com.feitianzhu.fu700.model.MultipleItem;
import com.feitianzhu.fu700.model.Province;
import com.feitianzhu.fu700.shop.adapter.LeftAdapter;
import com.feitianzhu.fu700.shop.adapter.RightAdapter;
import com.feitianzhu.fu700.shop.ui.ShopSearchActivity;
import com.feitianzhu.fu700.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.fu700.shop.ui.dialog.ProvincehDialog;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.socks.library.KLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 2:22
 * <p>
 * 商品分类页面
 */


public class CommodityClassificationFragment extends SFFragment implements View.OnClickListener, ProvinceCallBack, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1 = 1;
    private String mParam2;
    Unbinder unbinder;
    private String provinceId;
    private String cityId;
    private PopupWindow popupWindow;
    private View vPopupWindow;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<String> list;
    private List<MultipleItem> multipleItemList = new ArrayList<>();

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
        initData();
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
            for (int i = 0; i < 20; i++) {
                MultipleItem item = new MultipleItem(1);
                multipleItemList.add(item);
            }
            list = new ArrayList<>();
            list.add("休闲娱乐");
            list.add("健身/游泳");
            list.add("丽人/美发");
            list.add("医疗/牙科");
            list.add("结婚摄影");
            list.add("信用卡");

        } else {//商城
            button1.setSelected(false);
            button2.setSelected(true);
            for (int i = 0; i < 20; i++) {
                MultipleItem item = new MultipleItem(2);
                multipleItemList.add(item);
            }
            list = new ArrayList<>();
            list.add("水果");
            list.add("蔬菜");
            list.add("肉蛋");
            list.add("粮油");
            list.add("休闲食品");
            list.add("服饰");
            list.add("电器");
            list.add("海外甄选");
            list.add("乳品");
            list.add("家居");
            list.add("个护");
            list.add("美妆");
            list.add("餐厨");
            list.add("母婴");
            list.add("宠物");
        }

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


        leftAdapter = new LeftAdapter(list);
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        leftRecyclerView.setAdapter(leftAdapter);
        leftAdapter.notifyDataSetChanged();

        rightAdapter = new RightAdapter(multipleItemList);
        rightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rightRecyclerView.setAdapter(rightAdapter);
        rightAdapter.notifyDataSetChanged();

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.sf_blue));


        initListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(Constant.mCity)) mTxtLocation.setText(Constant.mCity);
    }

    public void initData() {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    public void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                leftAdapter.setSelect(position);
                leftAdapter.notifyDataSetChanged();
            }
        });

        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = adapter.getItemViewType(position);
                if (type == 1) {
                    //商家
                    Intent intent = new Intent(getActivity(), MerchantsDetailActivity.class);
                    startActivity(intent);
                } else {
                    //商品详情
                    Intent intent = new Intent(getActivity(), ShopsDetailActivity.class);
                    startActivity(intent);
                }

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
                list = new ArrayList<>();
                list.add("休闲娱乐");
                list.add("健身/游泳");
                list.add("丽人/美发");
                list.add("医疗/牙科");
                list.add("结婚摄影");
                list.add("信用卡");
                leftAdapter.setSelect(0);
                leftAdapter.setNewData(list);
                leftAdapter.notifyDataSetChanged();

                multipleItemList.clear();
                for (int i = 0; i < 20; i++) {
                    MultipleItem item = new MultipleItem(1);
                    multipleItemList.add(item);
                }
                rightAdapter.notifyDataSetChanged();
                break;
            case R.id.button2:
                mParam1 = 2;
                button1.setSelected(false);
                button2.setSelected(true);
                list = new ArrayList<>();
                list.add("水果");
                list.add("蔬菜");
                list.add("肉蛋");
                list.add("粮油");
                list.add("休闲食品");
                list.add("服饰");
                list.add("电器");
                list.add("海外甄选");
                list.add("乳品");
                list.add("家居");
                list.add("个护");
                list.add("美妆");
                list.add("餐厨");
                list.add("母婴");
                list.add("宠物");
                leftAdapter.setSelect(0);
                leftAdapter.setNewData(list);
                leftAdapter.notifyDataSetChanged();

                multipleItemList.clear();
                for (int i = 0; i < 20; i++) {
                    MultipleItem item = new MultipleItem(2);
                    multipleItemList.add(item);
                }
                rightAdapter.notifyDataSetChanged();
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

    private void JumpActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = provinceId = province.id;
        Constant.cityId = cityId = city.id;
        mTxtLocation.setText(city.name);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
