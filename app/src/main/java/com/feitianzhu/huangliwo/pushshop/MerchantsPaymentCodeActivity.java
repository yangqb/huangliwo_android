package com.feitianzhu.huangliwo.pushshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsPaymentCodeModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ShareImageUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.save_layout)
    LinearLayout saveLayout;

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

        OkGo.<LzyResponse<MerchantsPaymentCodeModel>>post(Urls.GET_MERCHANTS_PAYMENT_CODE)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<MerchantsPaymentCodeModel>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<MerchantsPaymentCodeModel>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsPaymentCodeModel>> response) {
                        super.onSuccess(MerchantsPaymentCodeActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().data != null) {
                            createCode(response.body().data.getQrCodeUrl());
                            tvContent.setText("扫二维码 向我付款");
                            merchantName.setText(response.body().data.getMerchantName());
                            Glide.with(MerchantsPaymentCodeActivity.this).load(response.body().data.getMerchantHeadImg()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(logoImg);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsPaymentCodeModel>> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    private void createCode(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.show("未获取到收款码");
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

    @OnClick({R.id.left_button, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.save:
                ShareImageUtils.saveImg(this, ShareImageUtils.viewToBitmap(saveLayout), "paycode_image");
                // 通知图库更新
               // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getPath())));
                break;
        }

    }

    private Bitmap logoBitmap;

    public Bitmap getLogoBitMap(final String url) {

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
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
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
