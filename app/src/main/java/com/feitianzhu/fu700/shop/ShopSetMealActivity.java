package com.feitianzhu.fu700.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.fu700.home.entity.HomeEntity;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.MultipleItem;
import com.feitianzhu.fu700.model.ServiceDetailModel;
import com.feitianzhu.fu700.shop.adapter.ShopDetailAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 5:45
 * <p>
 * 套餐详情页
 */
public class ShopSetMealActivity extends BaseActivity {
    public static final String MERCHANT_DATA = "data";
    public static final String SERVICE_RECOMMEND_BEAN = "serviceRecommendBean";
    private ShopDetailAdapter adapter;
    private List<MultipleItem> multipleItemList = new ArrayList<>();
    @BindView(R.id.right_img)
    ImageView shareImg;
    @BindView(R.id.img_collect)
    ImageView collectImg;
    @BindView(R.id.button1)
    TextView button1;
    @BindView(R.id.button2)
    TextView button2;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewpager)
    BannerViewPager<Integer, ShopSetMealActivity.DataViewHolder> mViewpager;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.address)
    TextView address;
    private ServiceDetailModel model;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_setmeal;
    }

    @Override
    protected void initView() {
        shareImg.setVisibility(View.VISIBLE);
        collectImg.setVisibility(View.VISIBLE);
        button1.setSelected(true);
        button2.setSelected(false);

        HomeEntity.RecommendListBean recommendListBean = (HomeEntity.RecommendListBean) getIntent().getSerializableExtra(MERCHANT_DATA);

        HomeEntity.ServiceRecommendListBean serviceRecommendBean = (HomeEntity.ServiceRecommendListBean) getIntent().getSerializableExtra(SERVICE_RECOMMEND_BEAN);
        if (serviceRecommendBean != null) {
            shopName.setText(serviceRecommendBean.serviceName);
            address.setText(serviceRecommendBean.serviceAddr);
        }
        if (recommendListBean != null) {
            shopName.setText(recommendListBean.merchantName);
        }
        for (int i = 0; i < 20; i++) {
            MultipleItem item = new MultipleItem(1);
            multipleItemList.add(item);
        }

        adapter = new ShopDetailAdapter(multipleItemList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (serviceRecommendBean != null) {
            requestShowData(2 + "");
        }
        if (recommendListBean != null) {
            requestShowData(2 + "");
        }
        initListener();
    }

    private void requestShowData(String serviceId) {
        serviceId = "2";
        Log.e("wangyan", "----serviceId----" + serviceId);
        OkHttpUtils.post()//
                .url(Common_HEADER + Constant.POST_SERVICE_DETAIL_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("serviceId", serviceId)
                .build()
                .execute(new Callback<ServiceDetailModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(ServiceDetailModel response, int id) {
                        model = response;
                        if (response.getCollectId() <= 0) {
                            collectImg.setSelected(false);
                        } else {
                            collectImg.setSelected(true);
                        }
                    }

                });
    }

    @OnClick({R.id.button1, R.id.button2, R.id.left_button, R.id.img_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                button1.setSelected(true);
                button2.setSelected(false);
                multipleItemList.clear();
                for (int i = 0; i < 20; i++) {
                    MultipleItem item = new MultipleItem(1);
                    multipleItemList.add(item);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.button2:
                button1.setSelected(false);
                button2.setSelected(true);
                multipleItemList.clear();
                for (int i = 0; i < 20; i++) {
                    MultipleItem item = new MultipleItem(2);
                    multipleItemList.add(item);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.img_collect:
                /*if (collectImg.isSelected()) { //如果已经是收藏就删除收藏
                    deleteShops();
                } else {  //收藏
                    doCelectShops();
                }*/
                break;
        }
    }

    private void deleteShops() {
        ShopDao.DeleteCollect(this,model.getCollectId() + "", new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("取消收藏");
                collectImg.setSelected(false);
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    private void doCelectShops() {
        ShopDao.PostCollect(this, 2, model.getServiceId(), new onNetFinishLinstenerT() {
            @Override
            public void onSuccess(int code, Object result) {
                model.setCollectId(Integer.parseInt(result.toString()));
                ToastUtils.showShortToast("收藏成功");
                collectImg.setSelected(true);
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = adapter.getItemViewType(position);
                if (type == 1) {
                    //套餐详情页
                    Intent intent = new Intent(ShopSetMealActivity.this, ShopInfoDetailActivity.class);
                    startActivity(intent);
                } else {
                    //评论
                }
            }
        });
    }

    @Override
    protected void initData() {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            integers.add(i);
        }
        //1.set banner
        //mBanners.clear();
        //mBanners.addAll(mHomeEntity.bannerList);
        mViewpager.setCanLoop(true)
                .setAutoPlay(true)
                .setIndicatorStyle(IndicatorStyle.CIRCLE)
                //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                .setRoundCorner(10)
                .setIndicatorRadius(8)
                .setIndicatorColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                .setHolderCreator(ShopSetMealActivity.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                onClickBanner(position);
            }
        }).create(integers);//.create(mBanners);
        mViewpager.startLoop();
    }

    /*
     * banner的点击事件
     * */
    public void onClickBanner(int i) {

    }

    public class DataViewHolder implements ViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(ViewGroup viewGroup, Context context, int position) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, Integer data, final int position, final int size) {
            Glide.with(context).load(R.mipmap.g10_02weijiazai).into(mImageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mViewpager != null) {
            mViewpager.stopLoop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewpager != null)
            mViewpager.startLoop();
    }
}
