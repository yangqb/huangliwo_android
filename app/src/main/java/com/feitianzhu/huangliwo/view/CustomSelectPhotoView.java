package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.BottomPopupView;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/10
 * time: 17:07
 * email: 694125155@qq.com
 */
public class CustomSelectPhotoView extends BottomPopupView {

    private TextView btn_cancel;
    private TextView btn_open_camera;
    private TextView btn_choose_img;

    public interface OnSelectCameraListener {
        void onCameraClick();
    }

    public interface OnSelectTakePhotoListener {
        void onTakePhotoClick();
    }

    private CustomSelectPhotoView.OnSelectCameraListener mOnSelectCameraListener;

    public CustomSelectPhotoView setSelectCameraListener(CustomSelectPhotoView.OnSelectCameraListener onSelectCameraListener) {
        this.mOnSelectCameraListener = onSelectCameraListener;
        return this;
    }

    private CustomSelectPhotoView.OnSelectTakePhotoListener mOnSelectTakePhotoListener;

    public CustomSelectPhotoView setOnSelectTakePhotoListener(CustomSelectPhotoView.OnSelectTakePhotoListener onSelectTakePhotoListener) {
        this.mOnSelectTakePhotoListener = onSelectTakePhotoListener;
        return this;
    }

    public CustomSelectPhotoView(@NonNull Context context) {
        super(context);
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_view_takephoto;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_open_camera = findViewById(R.id.btn_open_camera);
        btn_choose_img = findViewById(R.id.btn_choose_img);
        listener();
    }

    public void listener() {
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_open_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSelectCameraListener != null) {
                    mOnSelectCameraListener.onCameraClick();
                    dismiss();
                }
            }
        });

        btn_choose_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSelectTakePhotoListener != null) {
                    mOnSelectTakePhotoListener.onTakePhotoClick();
                    dismiss();
                }
            }
        });
    }
}
