package com.feitianzhu.huangliwo.me.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.me.helper.ImageUtil;
import com.feitianzhu.huangliwo.pushshop.MyPaymentActivity;
import com.feitianzhu.huangliwo.pushshop.RecordOrderActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/8/31 0031.
 */

public class ScannerActivity extends BaseActivity {
    public static final String IS_MERCHANTS = "isMerchants";
    private int isMerchants;
    @BindView(R.id.textview)
    TextView mTextview;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.right_container)
    FrameLayout rightContainer;
    private static final int REQUEST_IMAGE = 0x00011;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scanner;
    }

    @Override
    protected void initTitle() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
        isMerchants = getIntent().getIntExtra(IS_MERCHANTS, 0);
        title.setText("扫一扫");
        tvRight.setText("相册");
        title.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        rightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void initView() {

        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.scanner_view);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

//            1.判断是否是当前服务器地址的前缀
//            a 是的话判断type 1 就去商户付款页面  2就去用户个人资料
//            b 不是的话 判断是否是Url 是 直接跳 web就提示无法识别
            Log.e("Test", "-------Url----->" + result);
            checkUrl(result);

           /* Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScannerActivity.this.setResult(RESULT_OK, resultIntent);
            ScannerActivity.this.finish();*/
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScannerActivity.this.setResult(RESULT_OK, resultIntent);
            ScannerActivity.this.finish();
        }
    };

    private void checkUrl(String result) {
        Intent intent = null;
        if (isUrl(result)) {
            intent = new Intent(ScannerActivity.this, LazyWebActivity.class);
            intent.putExtra(Constant.URL, result);
            intent.putExtra(Constant.H5_TITLE, "扫描二维码");
            startActivity(intent);
            finish();
        } else {
            if (result.contains("merchantId") && result.contains("receivables")) {
                 /*
               收款页面
              * */
                intent = new Intent(ScannerActivity.this, MyPaymentActivity.class);
                intent.putExtra(MyPaymentActivity.PAYMENT_INFO, result);
                startActivity(intent);
                finish();

            } else if (result.contains("record")) {
               /*
               赠品录单
              * */
                if (isMerchants != 1) {
                    ToastUtils.show("您不是商户不可录单");
                    finish();
                } else {
                    intent = new Intent(ScannerActivity.this, RecordOrderActivity.class);
                    intent.putExtra(RecordOrderActivity.URL_CODE, result);
                    startActivity(intent);
                    finish();
                }
            } else {
                ToastUtils.show("无法识别此二维码!!");
            }
        }
    }

    public boolean isUrl(String result) {
        if (result.startsWith("http://") || result.startsWith("https://")) {
            return true;
        }
        return false;
    }

    private boolean isURL(String result) {
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";

        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(result);
        return matcher.find();
    }

    @Override
    protected void initData() {


    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
*/

    @OnClick({R.id.textview, R.id.fl_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview:
                break;
            case R.id.fl_back:
                finish();
                break;
        }

    }

    public String getStringByUrl(String url, String typeName) {
        String[] arr = url.split("[?]");
        String temp = arr[1];
        String[] arr2 = temp.split("&");
        for (String item : arr2) {
            if (item.contains(typeName)) {
                String[] str = item.split("=");
                return str[1];
            }
        }
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 选择系统图片并解析
         */
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            checkUrl(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(ScannerActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
