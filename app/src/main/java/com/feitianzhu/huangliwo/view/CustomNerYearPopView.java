package com.feitianzhu.huangliwo.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.HomePopModel;
import com.feitianzhu.huangliwo.utils.GlideUtils;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/1/7
 * time: 17:19
 * email: 694125155@qq.com
 */
public class CustomNerYearPopView extends CenterPopupView {
    private HomePopModel.PopupBean popupBea;

    public CustomNerYearPopView(@NonNull Context context) {
        super(context);
    }

    private OnConfirmClickListener listener;

    public interface OnConfirmClickListener {
        void onConfirm();
    }

    public CustomNerYearPopView setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.listener = onConfirmClickListener;
        return this;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_new_year_pop;
    }

    public CustomNerYearPopView setImgUrl(HomePopModel.PopupBean bean) {
        this.popupBea = bean;
        return this;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ImageView btnConfirm = findViewById(R.id.confirm);
        if (popupBea.getLikeType() == 4) {
            btnConfirm.setVisibility(VISIBLE);
        }
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                    dismiss();
                }
            }
        });
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(getContext()).load(popupBea.getImgUrl()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai)).into(GlideUtils.getImageView((Activity) getContext(), popupBea.getImgUrl(), imageView));
    }

    // 设置最大宽度，看需要而定
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }
}
