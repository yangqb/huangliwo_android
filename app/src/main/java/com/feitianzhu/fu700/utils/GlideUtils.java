package com.feitianzhu.fu700.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.utils.Log;

public class GlideUtils {
    /*
     * 根据图片尺寸等比缩放到屏幕尺寸
     * */
    public static ImageView getImageView(Activity mContext, String imgUrl, ImageView imgDetail) {
        Glide.with(mContext).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                WindowManager manager = mContext.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenWidth = outMetrics.widthPixels;
                Log.e("====", bWidth + " " + bHeight + " " + bHeight);
                int height = screenWidth * bHeight / bWidth;
                ViewGroup.LayoutParams para = imgDetail.getLayoutParams();
                para.height = height;
                imgDetail.setLayoutParams(para);
            }
        });

        return imgDetail;
    }

    public static ImageView getImageView2(Activity mContext, int res, ImageView imgDetail) {
        Glide.with(mContext).asBitmap().load(res).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                WindowManager manager = mContext.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenWidth = outMetrics.widthPixels;
                Log.e("====", bWidth + " " + bHeight + " " + bHeight);
                int height = (screenWidth - 150) * bHeight / bWidth;
                ViewGroup.LayoutParams para = imgDetail.getLayoutParams();
                para.height = height;
                imgDetail.setLayoutParams(para);
            }
        });

        return imgDetail;
    }
}
