/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.feitianzhu.huangliwo.wxapi;
import android.os.Bundle;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;


/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends WXCallbackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    /*@Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp instanceof SendAuth.Resp) {
                   *//* String code = ((SendAuth.Resp) baseResp).code;
                    EventBus.getDefault().post(code);*//*
                } else {
                    if (this.mWxHandler != null) {
                        try {
                            this.mWxHandler.getWXEventHandler().onResp(baseResp);
                        } catch (Exception var3) {
                            SLog.error(var3);
                        }
                    }
                    //ToastUtils.show("分享成功");
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                ToastUtils.show("授权失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                if (baseResp.getType() == 1) {
                    ToastUtils.show("取消登录");
                } else if (baseResp.getType() == 2) {
                    ToastUtils.show("取消分享");
                } else {

                }
                finish();
                break;
            default:
                finish();
                break;
        }
    }*/
}
