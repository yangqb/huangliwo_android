package com.feitianzhu.huangliwo.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.home.adapter.OptAdapter;
import com.feitianzhu.huangliwo.home.entity.HomeEntity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.HomeModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.CommodityClassificationFragment;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * package name: com.feitianzhu.huangliwo.home
 * user: yangqinbo
 * date: 2020/4/27
 * time: 18:15
 * email: 694125155@qq.com
 */
@SuppressWarnings("ALL")
public class FirstFragment extends SFFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<HomeEntity.BannerListBean> mBanners = new ArrayList<>();
    private List<MerchantsModel> optMerchantList = new ArrayList<>();
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private HomeModel mHomeMode;
    private MineInfoModel userInfo;
    private String token;
    private String userId;
    private OptAdapter optAdapter;
    Unbinder unbinder;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bannerViewPager)
    BannerViewPager<HomeEntity.BannerListBean, FirstFragment.DataViewHolder> mViewpager;
    @BindView(R.id.activityImg)
    ImageView activityImageView;
    @BindView(R.id.hotImg)
    RoundedImageView hotImg;
    @BindView(R.id.optRecyclerView)
    RecyclerView optRecyclerView;

    public FirstFragment() {

    }

    public static FirstFragment newInstance(int param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frangment_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        userInfo = UserInfoUtils.getUserInfo(getActivity());

        optRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        optAdapter = new OptAdapter(optMerchantList);
        optRecyclerView.setAdapter(optAdapter);
        initView();
        initData();
        initListener();

        return view;
    }

    public void initView() {
        content.setText(getArguments().getString(ARG_PARAM2));
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }


    public void initData() {
        OkGo.<LzyResponse<HomeModel>>get(Urls.GET_INDEX)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("longitude", longitude + "")
                .params("latitude", latitude + "")
                .execute(new JsonCallback<LzyResponse<HomeModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HomeModel>> response) {
                        super.onSuccess(getActivity(), response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            mHomeMode = response.body().data;
                            if (mHomeMode.bannerList != null && !mHomeMode.bannerList.isEmpty()) {
                                showBanner();
                            }
                            Glide.with(getActivity()).load(mHomeMode.activityImg).apply(new RequestOptions().placeholder(R.mipmap.g10_01weijiazai).error(R.mipmap.g10_01weijiazai).dontAnimate()).into(activityImageView);
                            if (mHomeMode.hotGood != null) {
                                Glide.with(getActivity()).load(mHomeMode.hotGood.hotGoodImg).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into(hotImg);
                            }
                            if (mHomeMode.merchantList != null) {
                                optMerchantList = mHomeMode.merchantList;
                                optAdapter.setNewData(optMerchantList);
                                optAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<HomeModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void showBanner() {
        mBanners.clear();
        mBanners.addAll(mHomeMode.bannerList);
        mViewpager.setCanLoop(true)
                .setAutoPlay(true)
                .setIndicatorStyle(IndicatorStyle.CIRCLE)
                //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                .setIndicatorRadius(8)
                .setIndicatorColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                .setHolderCreator(FirstFragment.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                onClickBanner(position);
            }
        }).create(mBanners);
        mViewpager.startLoop();
    }

    public void onClickBanner(int i) {
        //链接类型（1：VIP，2：商品详情，3：文章，4：外部链接）
        Intent intent;
        switch (mHomeMode.bannerList.get(i).linkType) {
            case 1:
                intent = new Intent(getActivity(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, mHomeMode.bannerList.get(i).idValue);
                startActivity(intent);
                break;
            case 3:
                // ToastUtils.show("敬请期待");
                //WebViewActivity.startActivity(getActivity(), mHomeEntity.bannerList.get(i).outUrl, "");
                break;
            case 4:
                intent = new Intent(getActivity(), NewYearShoppingActivity.class);
                startActivity(intent);
                break;

        }
    }

    public class DataViewHolder implements ViewHolder<HomeEntity.BannerListBean> {
        private ImageView mImageView;

        @Override
        public int getLayoutId() {
            return R.layout.detail_banner_item;
        }

        @Override
        public void onBind(View itemView, HomeEntity.BannerListBean data, int position, int size) {
            mImageView = itemView.findViewById(R.id.banner_image);
            Glide.with(mContext).load(data.imagUrl).apply(new RequestOptions().error(R.mipmap.g10_01weijiazai).placeholder(R.mipmap.g10_01weijiazai).dontAnimate()).into(mImageView);
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
        if (mViewpager != null)
            mViewpager.startLoop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
        unbinder.unbind();
    }
}
