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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;

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
        handler.post(waitSendsRunnable);
        strVal = getIntent().getStringExtra("strVal");
        //加载图片
        // Glide.with(AdvertisementActivity.this).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageadvertise);
        Log.e("TAG", "onCreate: ");

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap img = BitmapFactory.decodeFile(ImageCancheUtil.getFilePath(strVal)
                , options);
        imageadvertise.setImageBitmap(img);
//        Glide.with(this)
//                .load(img)
//                .into(imageadvertise);
    }

    //启用一个Handler
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startMainActivity();
                    break;
                case 1:
                    btnAdv.setText("跳过 " + index);
                    break;
                default:
                    break;
            }
        }
    };

    //点击跳转到主页面
    @OnClick(R.id.btnAdv)
    public void onViewClicked() {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(AdvertisementActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 倒计时五秒
    int index = 3;
    Runnable waitSendsRunnable = new Runnable() {
        public void run() {
            if (index > 0) {
                index--;
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(waitSendsRunnable);
            } else {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    };


}
