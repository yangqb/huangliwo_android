package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MineQRcodeModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ShareImageUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.socks.library.KLog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/3/18
 * time: 13:53
 * email: 694125155@qq.com
 */
public class ShareMerchantActivity extends BaseActivity {
    public static final String MERCHANT_DATA = "merchant_data";
    private MerchantsModel merchantsModel;
    private Bitmap bitmap;
    private String userId;
    private String token;
    @BindView(R.id.merchantsName)
    TextView merchantsName;
    @BindView(R.id.merchants_logo)
    CircleImageView merchantsLogo;
    @BindView(R.id.merchants_ShopFront)
    ImageView merchantsShopFront;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.shareImg)
    ImageView shareImg;
    @BindView(R.id.share_layout)
    LinearLayout shareLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_merchant;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsModel = (MerchantsModel) getIntent().getSerializableExtra(MERCHANT_DATA);
        merchantsName.setText(merchantsModel.getMerchantName());
        Glide.with(ShareMerchantActivity.this).load(merchantsModel.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(merchantsLogo);
        Glide.with(ShareMerchantActivity.this).load(merchantsModel.getShopFrontImg()).apply(new RequestOptions().error(R.mipmap.g10_02weijiazai).error(R.mipmap.g10_02weijiazai)).into(merchantsShopFront);
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(ShareMerchantActivity.this);

        Glide.with(ShareMerchantActivity.this).load(userInfo.getHeadImg()).apply(new RequestOptions().placeholder(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang)).into(head);
        nickName.setText(userInfo.getNickName());
        tvUserId.setText("邀请id：" + userInfo.getUserId());

    }

    @OnClick({R.id.left_button, R.id.bt_shared, R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.bt_save:
                ShareImageUtils.saveImg(this,ShareImageUtils.viewToBitmap(shareLayout), "zxing_merchant_image");
                // 通知图库更新
                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getPath())));
                break;
            case R.id.bt_shared:
                showShare();
                break;
        }

    }

    private void showShare() {
        Bitmap bitmap = ShareImageUtils.viewToBitmap(shareLayout);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        // oks.disableSSOWhenAuthorize();
        oks.setImageData(bitmap);
        oks.setTitle("便利大本营");  //最顶部的Title
        //oks.setImagePath(imgPath);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                KLog.i("share onComplete...");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                KLog.i("share onError..." + throwable);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                KLog.i("share onCancel...");
            }
        });
        // 启动分享GUI
        oks.show(ShareMerchantActivity.this);
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MineQRcodeModel>>get(Urls.BASE_URL + Constant.POST_MINE_QRCODE)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("type", "1")
                .execute(new JsonCallback<LzyResponse<MineQRcodeModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onSuccess(ShareMerchantActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            setShowData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onError(response);
                    }
                });
    }

    private void setShowData(MineQRcodeModel response) {
        String qrUrl = response.getLink();
        if (TextUtils.isEmpty(qrUrl)) {
            ToastUtils.show("未获取到分享地址");
            return;
        }
        Log.e("Test", "-------->" + qrUrl);
        //bitmap = CodeUtils.createImage(qrUrl, 200, 200, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        bitmap = CodeUtils.createImage(qrUrl, 200, 200, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(shareImg);
        }
    }
}
