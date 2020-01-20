package com.feitianzhu.huangliwo.pushshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsPaymentCodeModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.gyf.immersionbar.ImmersionBar;
import com.itheima.roundedimageview.RoundedImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/18
 * time: 12:48
 * email: 694125155@qq.com
 */
public class MerchantsPaymentCodeActivity extends BaseActivity {
    public static final String MERCHANTS_ID = "merchants_id";
    private Bitmap bitmap;
    private int merchantsId = -1;
    private String token;
    private String userId;
    @BindView(R.id.imgViewCode)
    ImageView imgViewCode;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.name)
    TextView merchantName;
    @BindView(R.id.logo)
    RoundedImageView logoImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_payment_code;
    }

    @Override
    protected void initView() {
        titleName.setText("我要收款");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
    }

    @Override
    protected void initData() {
        OkHttpUtils.post()
                .url(Urls.GET_MERCHANTS_PAYMENT_CODE)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<MerchantsPaymentCodeModel>() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        goneloadDialog();
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MerchantsPaymentCodeModel response, int id) {
                        goneloadDialog();
                        if (response != null) {
                            createCode(response.getQrCodeUrl());
                            tvContent.setText("扫二维码 向我付款");
                            merchantName.setText(response.getMerchantName());
                            Glide.with(MerchantsPaymentCodeActivity.this).load(response.getMerchantHeadImg()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(logoImg);
                        } else {
                            ToastUtils.showShortToast("未获取到收款码");
                        }
                    }
                });
    }

    private void createCode(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast("未获取到收款码");
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

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    private Bitmap logoBitmap;
    public Bitmap getLogoBitMap(final String url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    logoBitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return logoBitmap;
    }
}
