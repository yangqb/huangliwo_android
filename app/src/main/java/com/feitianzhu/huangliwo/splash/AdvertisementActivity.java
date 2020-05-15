package com.feitianzhu.huangliwo.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertisementActivity extends AppCompatActivity {

    @BindView(R.id.btnAdv)
    TextView btnAdv;
    @BindView(R.id.imageadvertise)
    ImageView imageadvertise;
    private String strVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();
        strVal = getIntent().getStringExtra("strVal");
        //加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap img = BitmapFactory.decodeFile(ImageCancheUtil.getFilePath(strVal)
                , options);
        imageadvertise.setImageBitmap(img);
//        Glide.with(this)
//                .load(img)
//                .into(imageadvertise);
        btnAdv.setText("跳过 " + index);
        handler.sendEmptyMessageDelayed(index, 1000);


    }

    // 倒计时五秒
    int index = 3;
    //启用一个Handler
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            index--;
            btnAdv.setText("跳过 " + index);

            switch (msg.what) {
                case 1:
                    startMainActivity();
                    break;
                case 2:
                    handler.sendEmptyMessageDelayed(index, 1000);

                    break;
                case 3:
                    handler.sendEmptyMessageDelayed(index, 1000);

                    break;
                default:
                    break;
            }
        }
    };

    //点击跳转到主页面
    @OnClick(R.id.btnAdv)
    public void onViewClicked(View view) {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(AdvertisementActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
