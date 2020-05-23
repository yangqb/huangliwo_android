package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.share.ShareUtils;
import com.feitianzhu.huangliwo.utils.ShareImageUtils;
import com.socks.library.KLog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/4/22
 * time: 18:32
 * email: 694125155@qq.com
 */
public class CustomerQrcodeActivity extends BaseActivity {
    public static final String QRCODE_URL = "qrcode_url";
    @BindView(R.id.imgCode)
    ImageView imgCode;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.shareLayout)
    LinearLayout shareLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_code;
    }

    @Override
    protected void initView() {
        titleName.setText("联系二维码");
        String qrcodeUrl = getIntent().getStringExtra(QRCODE_URL);
        Glide.with(this).load(qrcodeUrl).into(imgCode);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.bt_shared, R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.bt_shared:
                showShare();
                break;
            case R.id.bt_save:
                ShareImageUtils.saveImg(this, ShareImageUtils.viewToBitmap(shareLayout), "zxing_kefu_image");
                break;
        }
    }

    private void showShare() {
        Bitmap bitmap = ShareImageUtils.viewToBitmap(shareLayout);
        ShareUtils.shareImg(this, bitmap, "便利大本营");
       /* OnekeyShare oks = new OnekeyShare();
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
        oks.show(CustomerQrcodeActivity.this);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
