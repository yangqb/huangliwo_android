package com.feitianzhu.huangliwo.vip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.GiftDetailModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class VipGiftDetailActivity extends BaseActivity {
    public static final String GIFT_ID = "gift_id";
    private List<String> mapList = new ArrayList<>();
    //1.百度地图包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //2.高德地图包名
    public static final String AUTONAVI_PACKAGENAME = "com.autonavi.minimap";
    //3.腾讯地图包名
    public static final String QQMAP_PACKAGENAME = "com.tencent.map";
    private int giftId;
    private String token;
    private String userId;
    private Bitmap bitmap;
    private GiftDetailModel detailModel;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.imgViewCode)
    ImageView imgViewCode;
    @BindView(R.id.merchants_name)
    TextView merchantsName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.giftName)
    TextView giftName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvCode)
    TextView tvCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("赠品详情");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        appHasMap(this);
    }

    @Override
    protected void initData() {
        giftId = getIntent().getIntExtra(GIFT_ID, 0);
        OkGo.<LzyResponse<GiftDetailModel>>get(Urls.GET_GIFT_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("giftId", giftId + "")
                .execute(new JsonCallback<LzyResponse<GiftDetailModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<GiftDetailModel>> response) {
                        super.onSuccess(VipGiftDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            detailModel = response.body().data;
                            merchantsName.setText(detailModel.merchantName);
                            address.setText(detailModel.address);
                            giftName.setText(detailModel.giftName);
                            tvDesc.setText(detailModel.desc);
                            setSpannableString(String.format(Locale.getDefault(), "%.2f", detailModel.price), price);
                            if (detailModel.isUse == 0) {
                                tvUser.setText("未使用");
                            } else {
                                tvUser.setText("已使用");
                            }
                            createCode(detailModel.url);
                            tvCode.setText(detailModel.num);

                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<GiftDetailModel>> response) {
                        super.onError(response);
                    }
                });

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

    private void createCode(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.show("未获取到二维码");
            return;
        }
        Log.e("Test", "-------->" + url);
        //bitmap = CodeUtils.createImage(url, 400, 400, getLogoBitMap(logoUrl));
        bitmap = CodeUtils.createImage(url, 400, 400, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imgViewCode);
        }
    }

    @OnClick({R.id.left_button, R.id.address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.address:
                if (mapList.size() <= 0) {
                    ToastUtils.show("请先安装百度地图或高德地图");
                    return;
                } else {
                    new XPopup.Builder(this)
                            .asCustom(new CustomRefundView(VipGiftDetailActivity.this)
                                    .setData(mapList)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            if ("百度地图".equals(mapList.get(position))) {
                                                Intent i1 = new Intent();
                                                i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=" + detailModel.address));
                                                startActivity(i1);
                                            } else if ("高德地图".equals(mapList.get(position))) {
                                                Intent intent_gdmap = new Intent();
                                                intent_gdmap.setAction("android.intent.action.VIEW");
                                                intent_gdmap.setPackage("com.autonavi.minimap");
                                                intent_gdmap.addCategory("android.intent.category.DEFAULT");
                                                intent_gdmap.setData(Uri.parse("androidamap://poi?sourceApplication=com.feitianzhu.huangliwo&keywords=" + detailModel.address + "&dev=0"));
                                                startActivity(intent_gdmap);
                                            }
                                        }
                                    }))
                            .show();
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(16, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(26, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
