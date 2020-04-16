package com.feitianzhu.huangliwo.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.DensityUtil;
import com.feitianzhu.huangliwo.utils.RoundedCornersTransform;
import com.itheima.roundedimageview.RoundedImageView;

import java.io.File;

import butterknife.BindView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/4/11
 * time: 18:04
 * email: 694125155@qq.com
 */
public class CustomImgViewDialog extends Dialog {
    private Context context;
    protected MaterialDialog mDialog;
    private String url;
    private String title = "";

    public CustomImgViewDialog(Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    public CustomImgViewDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomImgViewDialog setData(String url) {
        this.url = url;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_imgview);
        SubsamplingScaleImageView imageView = findViewById(R.id.imageView);

       /* RoundedCornersTransform transform = new RoundedCornersTransform(context, DensityUtil.dp2px(context, 10));
        transform.setNeedCorner(true, true, true, true);
        RequestOptions options = new RequestOptions().transform(transform);*/
        Glide.with(context).load(url).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                Uri uri = Uri.fromFile(resource);
                imageView.setImage(ImageSource.uri(uri));
                imageView.setZoomEnabled(false);
                imageView.setPanEnabled(false);
            }
        });

        ImageView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
