package com.feitianzhu.huangliwo.common.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * Created by dicallc on 2017/9/8 0008.
 */

public class SFFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    LoadingPopupView loadingPopup;

    protected void showloadDialog(String title) {
        loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.NoAnimation)
                .asLoading()
                .bindLayout(R.layout.layout_loading_view)
                .show();
    }

    protected void goneloadDialog() {
        if (null != loadingPopup) {
            loadingPopup.delayDismissWith(600, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingPopup = null;
    }
}
