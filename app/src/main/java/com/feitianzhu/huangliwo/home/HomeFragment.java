package com.feitianzhu.huangliwo.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.home.entity.IndicatorEntity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.shop.CommodityClassificationFragment;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/4/27
 * time: 18:09
 * email: 694125155@qq.com
 */
public class HomeFragment extends SFFragment {
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
    ViewPager mViewPager;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        getUserInfo();
        getClasses();
        initListener();
        return view;
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
        mFragments.add(FirstFragment.newInstance(shopClassifyLsit.get(0).getClsId()));
        for (int i = 0; i < shopClassifyLsit.size(); i++) {
            mFragments.add(GoodsClsFragment.newInstance(shopClassifyLsit.get(i).getClsId()));
            mList.add(shopClassifyLsit.get(i).getClsName());
        }

//      无需编写适配器，一行代码关联TabLayout与ViewPager
        String[] strArray = new String[mList.size()];
        mList.toArray(strArray);
        slidingTabLayout.setViewPager(mViewPager, strArray, getActivity(), mFragments);
        TextView titleView = slidingTabLayout.getTitleView(0);
        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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
                            initTab();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<ShopClassify>> response) {
                        super.onError(response);
                    }
                });

    }


    public void getUserInfo() {
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(getActivity(), "", response.body().code);
                        if (response.body().data != null) {
                            userInfo = response.body().data;
                            String headImg = userInfo.getHeadImg();
                            Glide.with(mContext).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                                    .into(ivHead);
                            UserInfoUtils.saveUserInfo(getActivity(), userInfo);
                            SPUtils.putString(getActivity(), Constant.SP_PHONE, userInfo.getPhone());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
