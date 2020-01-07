package com.feitianzhu.huangliwo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import com.tencent.mm.opensdk.utils.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * package name: com.feitianzhu.huangliwo.utils
 * user: yangqinbo
 * date: 2020/1/7
 * time: 9:51
 * email: 694125155@qq.com
 */
public class ShareImageUtils {
    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    public static void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public static Bitmap viewToBitmap(View view) {
        Log.e("ssh", "a");
        Bitmap cacheBmp = loadBitmapFromView(view);
        view.destroyDrawingCache();
        return cacheBmp;
    }

    public static void saveImg(Bitmap cacheBmp, String child) {
        Log.i("xing", "savePicture: ------------------------");
        if (null == cacheBmp) {
            Log.i("xing", "savePicture: ------------------图片为空------");
            return;
        }
        File file = Environment.getExternalStorageDirectory();
        if (!file.exists()) {
            file.mkdirs();
        }
        File myCaptureFile = new File(file, child + ".png");
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            cacheBmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ToastUtils.showShortToast("保存成功");
    }

    private static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }
}
