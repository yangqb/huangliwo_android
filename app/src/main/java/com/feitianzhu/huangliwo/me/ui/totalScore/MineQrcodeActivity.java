package com.feitianzhu.huangliwo.me.ui.totalScore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MineQRcodeModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
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
    @BindView(R.id.iv_QRCode)
    ImageView mQRcode;
    @BindView(R.id.civ_pic)
    CircleImageView mCivPic;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.title_name)
    TextView titleName;
    private Bitmap bitmap;
    private MineQRcodeModel mData;
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
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mineInfoModel = (MineInfoModel) getIntent().getSerializableExtra(MINE_DATA);
        mTvName.setText("邀请码：" + mineInfoModel.getUserId());
        Glide.with(mContext).load(mineInfoModel.getHeadImg()).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(mCivPic);
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()//
                .url(Common_HEADER + Constant.POST_MINE_QRCODE)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<MineQRcodeModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                    }

                    @Override
                    public void onResponse(MineQRcodeModel response, int id) {
                        mData = response;
                        setShowData(response);
                    }
                });
    }


    private void setShowData(MineQRcodeModel response) {

        String qrUrl = response.getLink();
        if (TextUtils.isEmpty(qrUrl)) {
            qrUrl = "http://www.baidu.com";
        }
        Log.e("Test", "-------->" + qrUrl);
        bitmap = CodeUtils.createImage(qrUrl, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
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
                saveBitmapToLocal(bitmap);
                break;
            case R.id.bt_shared:
                showShare();
                break;
        }
    }


    private void saveBitmapToLocal(Bitmap bitmap) {
        if (bitmap == null) {
            ToastUtils.showShortToast("图片不存在!!!");
            return;
        }
        File file;
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "zxing_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "mqrcode" + ".png";
        file = new File(appDir, fileName);
        if (file.exists()) {
            ToastUtils.showShortToast("保存成功!");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            ToastUtils.showShortToast("保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/zxing_image/")));


    }

    private void saveBitmapToLocal() {
        if (bitmap == null) {
            ToastUtils.showShortToast("图片不存在!!!");
            return;
        }
        File file;
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "zxing_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "mqrcode" + ".png";
        file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/zxing_image/")));


    }

    private void showShare() {
        //saveBitmapToLocal();
        if (mData == null) {
            ToastUtils.showShortToast("分享的资料信息未完善，请先完善资料");
            return;
        }
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("黄鹂窝优选");  //最顶部的Title
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mData.getLink());
        // text是分享文本，所有平台都需要这个字段
        // oks.setText(mSharedInfo.getCompany());  //第二行的小文字
        // imagePath是图片地址，Linked-In以外的平台都支持此参数
        // 如果不用本地图片，千万不要调用这个方法！！！
//        oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setImagePath(Environment.getExternalStorageDirectory() + "/zxing_image/" + "mqrcode.png");
        // oks.setImageUrl(mSharedInfo.getHeadImg());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mData.getLink());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("黄鹂窝优选");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("黄鹂窝优选");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mData.getLink());
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
