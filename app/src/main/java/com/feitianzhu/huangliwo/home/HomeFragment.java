package com.feitianzhu.huangliwo.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.fragment.SFFragment;
import com.feitianzhu.huangliwo.home.adapter.MyInnerPagerAdapter;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.shop.ui.SearchShopActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomScrollViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.login.LoginEvent.EDITOR_INFO;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/4/27
 * time: 18:09
 * email: 694125155@qq.com
 */
public class HomeFragment extends SFFragment implements ProvinceCallBack {
    Unbinder unbinder;
    private ArrayList<Fragment> mFragments;
    private MineInfoModel userInfo;
    private List<ShopClassify.GGoodsClsListBean> shopClassifyLsit = new ArrayList<>();
    private List<String> mList = new ArrayList<>();
    private String token;
    private String userId;
    @BindView(R.id.tabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager)
    CustomScrollViewPager mViewPager;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.txt_location)
    TextView mTxtLocation;

    public HomeFragment() {

    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        showHeadImg();
        getClasses();
        initListener();
        return view;
    }


    public void showHeadImg() {
        userInfo = UserInfoUtils.getUserInfo(getActivity());
        String headImg = userInfo.getHeadImg();
        Glide.with(mContext).load(headImg)
                .apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang)
                        .error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(ivHead);
    }

    public void initListener() {
        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
            }

        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换时将所有tab字体设置为正常
                int length = mList.size();
                for (int i = 0; i < length; i++) {
                    TextView titleView = slidingTabLayout.getTitleView(i);
                    titleView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }

                //将当前选中的tab设置为粗体
                TextView currentView = slidingTabLayout.getTitleView(position);
                currentView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    public void initTab() {
        mFragments = new ArrayList<>();
        mList.add("推荐");
        mFragments.add(RecommendedFragment.newInstance(shopClassifyLsit.get(0).getClsId()));
        for (int i = 0; i < shopClassifyLsit.size(); i++) {
            mFragments.add(GoodsClsFragment.newInstance(shopClassifyLsit.get(i).getClsId()));
            mList.add(shopClassifyLsit.get(i).getClsName());
        }

//      无需编写适配器，一行代码关联TabLayout与ViewPager
        String[] strArray = new String[mList.size()];
        mList.toArray(strArray);
        mViewPager.setAdapter(new MyInnerPagerAdapter(getChildFragmentManager(), mFragments, strArray));

        slidingTabLayout.setViewPager(mViewPager);
        TextView titleView = slidingTabLayout.getTitleView(0);
        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    public void getClasses() {

        OkGo.<LzyResponse<ShopClassify>>post(Urls.GET_SHOP_CLASS)
                .tag(this)
                .execute(new JsonCallback<LzyResponse<ShopClassify>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<ShopClassify>> response) {
                        if(response.body()!=null){
                            super.onSuccess(getActivity(), "", response.body().code);
                            ShopClassify shopClassify = response.body().data;
                            if (shopClassify != null && shopClassify.getGGoodsClsList() != null) {
                                shopClassifyLsit = shopClassify.getGGoodsClsList();
                                initTab();
                            }
                        }

                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<ShopClassify>> response) {
                        super.onError(response);
                    }
                });

    }

    @OnClick({R.id.ll_location, R.id.iv_head, R.id.search, R.id.iv_home_nv_right})
    public void onClick(View view) {
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
            case R.id.iv_home_nv_right:
                requestPermission(view);
                break;
        }
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
                            intent.putExtra(ScannerActivity.IS_MERCHANTS, userInfo.getIsMerchant());
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (slidingTabLayout != null) {
                slidingTabLayout.setCurrentTab(0);
            }
        }
        if (!TextUtils.isEmpty(Constant.mCity)) {
            mTxtLocation.setText(Constant.mCity);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        if (event == EDITOR_INFO) {
            showHeadImg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        Constant.provinceId = province.id;
        Constant.cityId = city.id;
        Constant.mCity = city.name;
        mTxtLocation.setText(city.name);
    }
}
