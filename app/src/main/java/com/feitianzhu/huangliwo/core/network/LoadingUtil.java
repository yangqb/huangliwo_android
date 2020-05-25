package com.feitianzhu.huangliwo.core.network;

import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.util.HashMap;

/**
 * Created by bch on 2020/5/15
 */
public class LoadingUtil {
    /**
     * 用到的地方的数量
     */
    private static int usefulCount = 0;
    private static LoadingPopupView loadingPopup;
    private static HashMap<String, BaseApiRequest> map = new HashMap<>();

    public static void setLoadingViewShow(Boolean show) {
        setLoadingViewShowWithContent(null, show, "加载中");
    }

    synchronized public static void setLoadingViewShowWithContent(BaseApiRequest baseApiRequest, Boolean show, String content) {
        if (show) {
            usefulCount++;
            loadingPopup = (LoadingPopupView) new XPopup.Builder(GlobalUtil.getCurrentActivity())
                    .hasShadowBg(false)
                    .popupAnimation(PopupAnimation.NoAnimation)
                    .asLoading()
                    .bindLayout(R.layout.layout_loading_view)
                    .show();
            if (baseApiRequest != null) {
                if (!map.containsKey(baseApiRequest.requestTag)) {
                    map.put(baseApiRequest.requestTag, baseApiRequest);
                }
            }
        } else {
            usefulCount--;
            if (usefulCount <= 0 && loadingPopup != null) {
                loadingPopup.delayDismissWith(600, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                //取消进度条取消展示,清空
                if (baseApiRequest != null) {
                    if (!map.isEmpty()) {
                        map.clear();
                    }
                }
            } else {
                if (baseApiRequest != null) {
                    if (map.containsKey(baseApiRequest.requestTag)) {
                        map.remove(baseApiRequest.requestTag);
                    }
//        loadingPopup
                    //TODO 加载条 添加退出按钮 ,点击取消所有请求 2020-5-15
                }
            }

        }


    }

    public static void setLoadingViewShow(BaseApiRequest baseApiRequest, Boolean show) {
        setLoadingViewShowWithContent(baseApiRequest, show, "加载中");
    }

}
