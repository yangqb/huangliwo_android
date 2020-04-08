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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CollectionBody;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleMerchantsItem;
import com.feitianzhu.huangliwo.model.SetMealEvalDetailInfo;
import com.feitianzhu.huangliwo.model.VipGifListInfo;
import com.feitianzhu.huangliwo.pushshop.MyPaymentActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealListInfo;
import com.feitianzhu.huangliwo.shop.adapter.ShopDetailAdapter;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.feitianzhu.huangliwo.vip.VipGiftDetailActivity;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.enums.IndicatorStyle;
import com.zhpan.bannerview.holder.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.vip.VipGiftDetailActivity.GIFT_ID;

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
    private MerchantsModel merchantsBean;
    ;
    private MineInfoModel mineInfoModel = new MineInfoModel();
    private List<MultipleMerchantsItem> multipleItemList = new ArrayList<>();
    private List<SetMealInfo> setMealInfoList = new ArrayList<>();
    private List<VipGifListInfo.VipGifModel> gifModelList = new ArrayList<>();
    private List<SetMealEvalDetailInfo.SetMealEvalDetailModel> setMealEvalDetailModelList = new ArrayList<>();
    private List<String> imgs = new ArrayList<>();
    @BindView(R.id.right_img)
    ImageView shareImg;
    @BindView(R.id.img_collect)
    ImageView collectImg;
    @BindView(R.id.button1)
    TextView button1;
    @BindView(R.id.button2)
    TextView button2;
    @BindView(R.id.button3)
    TextView button3;
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
    @BindView(R.id.vip_rebate)
    TextView vipRebate;
    private int merchantsId;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_rebate)
    LinearLayout llRebate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_setmeal2;
    }

    @Override
    protected void initView() {
        titleName.setText("商铺详情");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        //shareImg.setVisibility(View.VISIBLE);
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        collectImg.setVisibility(View.VISIBLE);

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

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.left_button, R.id.img_collect, R.id.call_phone, R.id.address, R.id.ll_rebate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                button1.setSelected(true);
                button2.setSelected(false);
                button3.setSelected(false);
                getSetMealList(merchantsId);
                /*multipleItemList.clear();
                mAdapter.notifyDataSetChanged();*/
                break;
            case R.id.button2:
                button1.setSelected(false);
                button2.setSelected(true);
                button3.setSelected(false);
                getSetMealEvaList();
                break;
            case R.id.button3:
                button1.setSelected(false);
                button2.setSelected(false);
                button3.setSelected(true);
                getSetMealList(merchantsId);
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.img_collect:
                if (collectImg.isSelected()) { //如果已经是收藏就删除收藏
                    deleteCollect();
                } else {  //收藏
                    addCollect();
                }
                break;
            case R.id.call_phone:
                new XPopup.Builder(this)
                        .asConfirm("拨打商家电话", merchantsBean.getPhone(), "关闭", "确定", new OnConfirmListener() {
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
                                                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=" + merchantsBean.getCityName() + merchantsBean.getAreaName() + merchantsBean.getDtlAddr()));
                                                startActivity(i1);
                                            } else if ("高德地图".equals(mapList.get(position))) {
                                                Intent intent_gdmap = new Intent();
                                                intent_gdmap.setAction("android.intent.action.VIEW");
                                                intent_gdmap.setPackage("com.autonavi.minimap");
                                                intent_gdmap.addCategory("android.intent.category.DEFAULT");
                                                intent_gdmap.setData(Uri.parse("androidamap://poi?sourceApplication=com.feitianzhu.huangliwo&keywords=" + merchantsBean.getCityName() + merchantsBean.getAreaName() + merchantsBean.getDtlAddr() + "&dev=0"));
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
                intent.setData(Uri.parse("tel:" + merchantsBean.getPhone()));
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

    public void deleteCollect() {
        CollectionBody collectionBody = new CollectionBody();
        collectionBody.type = 1;
        collectionBody.idValue = merchantsId;
        String json = new Gson().toJson(collectionBody);
        OkGo.<LzyResponse>post(Urls.DELETE_COLLECTION).tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("collect", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("取消收藏");
                            collectImg.setSelected(false);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void addCollect() {
        CollectionBody collectionBody = new CollectionBody();
        collectionBody.type = 1;
        collectionBody.idValue = merchantsId;
        String json = new Gson().toJson(collectionBody);
        OkGo.<LzyResponse>post(Urls.ADD_COLLECTION).tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("collect", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("收藏成功");
                            collectImg.setSelected(true);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
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
                } else if (mAdapter.getItemViewType(position) == MultipleMerchantsItem.GIFT_TYPE) {
                    //礼品详情页
                    if (multipleItemList.get(position).getGifModel().isGet == 1) {
                        Intent intent = new Intent(ShopMerchantsDetailActivity.this, VipGiftDetailActivity.class);
                        intent.putExtra(GIFT_ID, multipleItemList.get(position).getGifModel().giftId);
                        startActivity(intent);
                    }
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItemViewType(position) == MultipleMerchantsItem.GIFT_TYPE) {
                    if (mineInfoModel.getAccountType() != 0) {
                        if (multipleItemList.get(position).getGifModel().isGet == 0) {
                            receiveGif(multipleItemList.get(position).getGifModel().giftId, multipleItemList.get(position).getGifModel().merchantId, position);
                        }
                    } else {
                        ToastUtils.showShortToast("您还不是会员");
                    }
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

    public void receiveGif(int giftId, int merchantId, int position) {
        OkGo.<LzyResponse>get(Urls.GET_GIFT)
                .tag(this)
                .params("giftId", "" + giftId)
                .params("merchantId", "" + merchantId)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("领取成功");
                            multipleItemList.get(position).getGifModel().isGet = 1;
                            mAdapter.setNewData(multipleItemList);
                            mAdapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void getSetMealEvaList() {

        OkGo.<LzyResponse<SetMealEvalDetailInfo>>get(Urls.GET_SETMEAL_EVALIST)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<SetMealEvalDetailInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<SetMealEvalDetailInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<SetMealEvalDetailInfo>> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().data != null) {
                            setMealEvalDetailModelList = response.body().data.getList();
                            multipleItemList.clear();
                            for (int i = 0; i < setMealEvalDetailModelList.size(); i++) {
                                MultipleMerchantsItem multipleMerchantsItem = new MultipleMerchantsItem(MultipleMerchantsItem.COMMENTS_TYPE);
                                multipleMerchantsItem.setEvalDetailModel(setMealEvalDetailModelList.get(i));
                                multipleItemList.add(multipleMerchantsItem);
                            }
                            mAdapter.setNewData(multipleItemList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<SetMealEvalDetailInfo>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    public void getSetMealList(int merchantsId) {

        OkGo.<LzyResponse<SetMealListInfo>>get(Urls.GET_SETMEAL_LIST)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params("type", "2")
                .params(USERID, userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<SetMealListInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<SetMealListInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<SetMealListInfo>> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        multipleItemList.clear();
                        if (response.body().code == 0 && response.body().data != null) {
                            if (button3.isSelected()) {
                                if (response.body().data.getMerchantGiftList() != null) {
                                    gifModelList = response.body().data.getMerchantGiftList();
                                    for (int i = 0; i < gifModelList.size(); i++) {
                                        MultipleMerchantsItem multipleMerchantsItem = new MultipleMerchantsItem(MultipleMerchantsItem.GIFT_TYPE);
                                        multipleMerchantsItem.setGifModel(gifModelList.get(i));
                                        multipleItemList.add(multipleMerchantsItem);
                                    }
                                }
                            } else {
                                if (response.body().data.getList() != null) {
                                    setMealInfoList = response.body().data.getList();
                                    for (int i = 0; i < setMealInfoList.size(); i++) {
                                        MultipleMerchantsItem multipleMerchantsItem = new MultipleMerchantsItem(MultipleMerchantsItem.SETMEAL_TYPE);
                                        multipleMerchantsItem.setSetMealInfo(setMealInfoList.get(i));
                                        multipleItemList.add(multipleMerchantsItem);
                                    }
                                }
                            }
                            mAdapter.setNewData(multipleItemList);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<SetMealListInfo>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    @Override
    protected void initData() {
        getUserInfo();
        getMerchantInfo();
    }

    public void getMerchantInfo() {
        OkGo.<LzyResponse<MerchantsModel>>get(Urls.GET_MERCHANTS_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<MerchantsModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MerchantsModel>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            merchantsBean = response.body().data;
                            if (merchantsBean.getIsCollect() == 0) {
                                collectImg.setSelected(false);
                            } else {
                                collectImg.setSelected(true);
                            }
                            shopName.setText(merchantsBean.getMerchantName());
                            address.setText(merchantsBean.getCityName() + merchantsBean.getAreaName() + merchantsBean.getDtlAddr());
                            String discount = String.valueOf((100 - merchantsBean.getDiscount() * 100));
                            tvRebate.setText("返" + MathUtils.subZero(discount) + "%");
                            vipRebate.setText("返" + MathUtils.subZero(discount) + "%");
                            getSetMealList(merchantsId);
                            String urlLogo = merchantsBean.getShopFrontImg() == null ? "" : merchantsBean.getShopFrontImg();
                            imgs.add(urlLogo);
                            mViewpager.setCanLoop(true)
                                    .setAutoPlay(true)
                                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                                    //.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
                                    // .setRoundCorner(10)
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
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MerchantsModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getUserInfo() {
        OkGo.<LzyResponse<MineInfoModel>>get(Common_HEADER + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(ShopMerchantsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            mineInfoModel = response.body().data;
                            if (mineInfoModel.getAccountType() != 0) {
                                llRebate.setVisibility(View.GONE);
                                vipRebate.setVisibility(View.VISIBLE);
                            } else {
                                llRebate.setVisibility(View.VISIBLE);
                                vipRebate.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
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
            View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_merchants, viewGroup, false);
            mImageView = view.findViewById(R.id.image);
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
