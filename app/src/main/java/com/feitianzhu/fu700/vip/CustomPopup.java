package com.feitianzhu.fu700.vip;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.feitianzhu.fu700.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/23 0023 下午 7:17
 */
public class CustomPopup extends CenterPopupView {
    private Context context;

    public CustomPopup(@NonNull Context context) {
        super(context);
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runnable != null) {
                    dismiss();
                    runnable.run();
                }
            }
        });
    }

    private Runnable runnable;

    public CustomPopup onClose(Runnable runnable) {
        this.runnable = runnable;
        return this;
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
