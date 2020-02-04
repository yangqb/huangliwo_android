package com.feitianzhu.huangliwo.shop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleMerchantsItem;
import com.feitianzhu.huangliwo.model.SetMealEvalDetailInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealListInfo;
import com.feitianzhu.huangliwo.shop.adapter.ShopDetailAdapter;
import com.feitianzhu.huangliwo.shop.ui.EditApplyRefundActivity;
import com.feitianzhu.huangliwo.shop.ui.OrderDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 5:45
 * <p>
 * 商铺详情页
 */
public class ShopMerchantsDetailActivity extends BaseActivity {
    public static final String MERCHANTS_ID = "merchants_id";
    public static final String MERCHANT_DATA = "data";
    private List<String> mapList = new ArrayList<>();
    //1.百度地图包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //2.高德地图包名
    public static final String AUTONAVI_PACKAGENAME = "com.autonavi.minimap";
    //3.腾讯地图包名
    public static final String QQMAP_PACKAGENAME = "com.tencent.map";
    private ShopDetailAdapter mAdapter;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private List<MultipleMerchantsItem> multipleItemList = new ArrayList<>();
    private List<SetMealInfo> setMealInfoList = new ArrayList<>();
    private List<SetMealEvalDetailInfo.SetMealEvalDetailModel> setMealEvalDetailModelList = new ArrayList<>();
    private MerchantsModel merchantsDetail;
    private List<String> imgs = new ArrayList<>();
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
    BannerViewPager<String, ShopMerchantsDetailActivity.DataViewHolder> mViewpager;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_rebate)
    TextView tvRebate;
    private int merchantsId;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_setmeal;
    }

    @Override
    protected void initView() {
        titleName.setText("商铺详情");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        shareImg.setVisibility(View.VISIBLE);
        collectImg.setVisibility(View.VISIBLE);
        button1.setSelected(true);
        button2.setSelected(false);

        merchantsDetail = (MerchantsModel) getIntent().getSerializableExtra(MERCHANT_DATA);

        if (merchantsDetail != null) {
            merchantsId = merchantsDetail.getMerchantId();
            shopName.setText(merchantsDetail.getMerchantName());
            address.setText(merchantsDetail.getCityName() + merchantsDetail.getAreaName() + merchantsDetail.getDtlAddr());
            String discount = String.valueOf((100 - merchantsDetail.getDiscount() * 100));
            tvRebate.setText("返" + discount + "%");
            getSetMealList(merchantsId);
            String urlLogo = merchantsDetail.getLogo()==null?"":merchantsDetail.getLogo();
            imgs.add(urlLogo);
            mViewpager.setCanLoop(true)
                    .setAutoPlay(true)
                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                    //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                    .setRoundCorner(10)
                    .setIndicatorRadius(8)
                    .setIndicatorColor(Color.parseColor("#CCCCCC"), Color.parseColor("#6C6D72"))
                    .setHolderCreator(ShopMerchantsDetailActivity.DataViewHolder::new).setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    onClickBanner(position);
                }
            }).create(imgs);//.create(mBanners);
            mViewpager.startLoop();
        }
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new ShopDetailAdapter(multipleItemList);
        mAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        appHasMap(this);
        initListener();
    }

    @OnClick({R.id.button1, R.id.button2, R.id.left_button, R.id.img_collect, R.id.call_phone, R.id.address, R.id.ll_rebate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                button1.setSelected(true);
                button2.setSelected(false);
                getSetMealList(merchantsId);
                /*multipleItemList.clear();
                mAdapter.notifyDataSetChanged();*/
                break;
            case R.id.button2:
                button1.setSelected(false);
                button2.setSelected(true);
                getSetMealEvaList();
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
            case R.id.call_phone:
                new XPopup.Builder(this)
                        .asConfirm("拨打商家电话", merchantsDetail.getPhone(), "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                requestPermission();
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.address:
                if (mapList.size() <= 0) {
                    ToastUtils.showShortToast("请先安装百度地图或高德地图");
                    return;
                } else {
                    new XPopup.Builder(this)
                            .asCustom(new CustomRefundView(ShopMerchantsDetailActivity.this)
                                    .setData(mapList)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            if ("百度地图".equals(mapList.get(position))) {
                                                Intent i1 = new Intent();
                                                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=" + merchantsDetail.getCityName() + merchantsDetail.getAreaName() + merchantsDetail.getDtlAddr()));
                                                startActivity(i1);
                                            } else if ("高德地图".equals(mapList.get(position))) {
                                                Intent intent_gdmap = new Intent();
                                                intent_gdmap.setAction("android.intent.action.VIEW");
                                                intent_gdmap.setPackage("com.autonavi.minimap");
                                                intent_gdmap.addCategory("android.intent.category.DEFAULT");
                                                intent_gdmap.setData(Uri.parse("androidamap://poi?sourceApplication=com.feitianzhu.huangliwo&keywords=" + merchantsDetail.getCityName() + merchantsDetail.getAreaName() + merchantsDetail.getDtlAddr() + "&dev=0"));
                                                startActivity(intent_gdmap);
                                            }
                                        }
                                    }))
                            .show();
                }
                break;
            case R.id.ll_rebate:
                Intent intent = new Intent(ShopMerchantsDetailActivity.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, mineInfoModel);
                startActivity(intent);
                break;
        }
    }

    public List<String> appHasMap(Context context) {
        if (isAvilible(context, BAIDUMAP_PACKAGENAME)) {
            mapList.add("百度地图");
        }
        if (isAvilible(context, AUTONAVI_PACKAGENAME)) {
            mapList.add("高德地图");
        }
        return mapList;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + merchantsDetail.getPhone()));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(ShopMerchantsDetailActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void deleteShops() {
        /*ShopDao.DeleteCollect(this, model.getCollectId() + "", new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("取消收藏");
                collectImg.setSelected(false);
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });*/
    }

    private void doCelectShops() {
        /*ShopDao.PostCollect(this, 2, model.getServiceId(), new onNetFinishLinstenerT() {
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
        });*/
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItemViewType(position) == MultipleMerchantsItem.SETMEAL_TYPE) {
                    //套餐详情页
                    Intent intent = new Intent(ShopMerchantsDetailActivity.this, ShopSetMealDetailActivity.class);
                    intent.putExtra(ShopSetMealDetailActivity.SETMEAL_INFO, setMealInfoList.get(position));
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnChildPositionListener(new ShopDetailAdapter.OnChildClickListener() {
            @Override
            public void success(int index, int pos) {
                if (setMealEvalDetailModelList.get(pos).getImgs() != null) {
                    String[] strings = setMealEvalDetailModelList.get(pos).getImgs().split(",");
                    // 仅需一行代码,默认配置为：
                    //      显示顶部进度指示器、
                    //      显示右侧下载按钮、
                    //      隐藏左侧关闭按钮、
                    //      开启点击图片关闭、
                    //      关闭下拉图片关闭、
                    //      加载方式为手动模式
                    //      加载原图的百分比在底部
                    ImagePreview
                            .getInstance()
                            // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                            .setContext(mContext)
                            .setEnableDragClose(true) //下拉图片关闭
                            // 设置从第几张开始看（索引从0开始）
                            .setIndex(index)
                            .setShowErrorToast(true)//加载失败提示
                            //=================================================================================================
                            // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                            // 1：第一步生成的imageInfo List
                            //.setImageInfoList(imageInfoList)

                            // 2：直接传url List
                            .setImageList(Arrays.asList(strings))

                            // 3：只有一张图片的情况，可以直接传入这张图片的url
                            //.setImage(String image)
                            //=================================================================================================

                            // 开启预览
                            .start();
                }
            }
        });
    }

    public void getSetMealEvaList() {
        OkHttpUtils.get()
                .url(Urls.GET_SETMEAL_EVALIST)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<SetMealEvalDetailInfo>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(SetMealEvalDetailInfo response, int id) {
                        setMealEvalDetailModelList = response.getList();
                        multipleItemList.clear();
                        for (int i = 0; i < setMealEvalDetailModelList.size(); i++) {
                            MultipleMerchantsItem multipleMerchantsItem = new MultipleMerchantsItem(MultipleMerchantsItem.COMMENTS_TYPE);
                            multipleMerchantsItem.setEvalDetailModel(setMealEvalDetailModelList.get(i));
                            multipleItemList.add(multipleMerchantsItem);
                        }
                        mAdapter.setNewData(multipleItemList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getSetMealList(int merchantsId) {
        OkHttpUtils.get()
                .url(Urls.GET_SETMEAL_LIST)
                .addParams(ACCESSTOKEN, token)
                .addParams("type","2")
                .addParams(USERID, userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<SetMealListInfo>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(SetMealListInfo response, int id) {
                        setMealInfoList = response.getList();
                        multipleItemList.clear();
                        for (int i = 0; i < setMealInfoList.size(); i++) {
                            MultipleMerchantsItem multipleMerchantsItem = new MultipleMerchantsItem(MultipleMerchantsItem.SETMEAL_TYPE);
                            multipleMerchantsItem.setSetMealInfo(setMealInfoList.get(i));
                            multipleItemList.add(multipleMerchantsItem);
                        }
                        mAdapter.setNewData(multipleItemList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initData() {
        getUserInfo();
    }

    public void getUserInfo() {
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
                        }
                    }
                });
    }

    /*
     * banner的点击事件
     * */
    public void onClickBanner(int i) {

    }

    public class DataViewHolder implements ViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(ViewGroup viewGroup, Context context, int position) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, viewGroup, false);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(final Context context, String data, final int position, final int size) {
            Glide.with(context).load(data).apply(new RequestOptions().placeholder(R.mipmap.g10_02weijiazai).error(R.mipmap.g10_02weijiazai)).into(mImageView);
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
