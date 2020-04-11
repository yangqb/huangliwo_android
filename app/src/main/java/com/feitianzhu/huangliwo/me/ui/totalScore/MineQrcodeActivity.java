package com.feitianzhu.huangliwo.me.ui.totalScore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.feitianzhu.huangliwo.utils.GlideUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ShareImageUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.socks.library.KLog;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * Created by Vya on 2017/9/5 0005.
 */

public class MineQrcodeActivity extends BaseActivity {
    public static final String MINE_DATA = "mine_data";
    public static final String SHARE_TYPE = "share_type";
    private int shareType = 1;
    @BindView(R.id.iv_QRCode)
    ImageView mQRcode;
    @BindView(R.id.civ_pic)
    CircleImageView mCivPic;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.shareLayout)
    LinearLayout shareLayout;
    @BindView(R.id.yearImg)
    ImageView imageView;
    @BindView(R.id.tips)
    TextView tips;
    private Bitmap bitmap;
    private String token;
    private String userId;
    private MineInfoModel mineInfoModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_qrcode;
    }

    @Override
    protected void initTitle() {
        titleName.setText("我的二维码");
    }

    @Override
    protected void initView() {
        /*View v = LayoutInflater.from(this).inflate(R.layout.layout_share, null, false);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        ShareImageUtils.layoutView(v, width, height);*/
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mineInfoModel = (MineInfoModel) getIntent().getSerializableExtra(MINE_DATA);
        shareType = getIntent().getIntExtra(SHARE_TYPE, 1);
        tvUserId.setText("邀请码：" + mineInfoModel.getUserId());
        tvName.setText(mineInfoModel.getNickName() == null ? "" : mineInfoModel.getNickName());
        Glide.with(mContext).load(mineInfoModel.getHeadImg()).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(mCivPic);
        if (shareType == 1) {
            tips.setText("扫一扫 下载便利大本营APP 更多优惠等你来！");
        } else {
            tips.setText("扫一扫 分享推店给身边的商家！");
        }
    }

    @Override
    protected void initData() {

        OkGo.<LzyResponse<MineQRcodeModel>>get(Common_HEADER + Constant.POST_MINE_QRCODE)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("type", shareType + "")
                .execute(new JsonCallback<LzyResponse<MineQRcodeModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onSuccess(MineQrcodeActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            setShowData(response.body().data);
                        } else {
                            imageView.setBackgroundResource(R.mipmap.g10_03weijiazai);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onError(response);
                    }
                });
    }


    private void setShowData(MineQRcodeModel response) {
        //Glide.with(this).load(response.getYearImg()).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(GlideUtils.getImageView(this, response.getYearImg(), imageView));
        Glide.with(this).load(response.getYearImg()).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(imageView);
        String qrUrl = response.getLink();
        if (TextUtils.isEmpty(qrUrl)) {
            ToastUtils.showShortToast("未获取到分享地址");
            return;
        }
        Log.e("Test", "-------->" + qrUrl);
        //bitmap = CodeUtils.createImage(qrUrl, 200, 200, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        bitmap = CodeUtils.createImage(qrUrl, 200, 200, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(mQRcode);
        }
    }

    @OnClick({R.id.bt_save, R.id.bt_shared, R.id.left_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.bt_save:
                if (shareType == 1) {
                    ShareImageUtils.saveImg(ShareImageUtils.viewToBitmap(shareLayout), "zxing_image");
                } else {
                    ShareImageUtils.saveImg(ShareImageUtils.viewToBitmap(shareLayout), "zxing_recruit_image");
                }
                // 通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getPath())));
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
        oks.show(MineQrcodeActivity.this);
    }

}
