package com.feitianzhu.fu700.me.ui;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.base.BaseTakePhotoActivity;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.SharedInfoModel;
import com.feitianzhu.fu700.shop.ui.EditApplyRefundActivity;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.CircleImageView;
import com.feitianzhu.fu700.view.CustomRefundView;
import com.feitianzhu.fu700.view.CustomSelectPhotoView;
import com.lxj.xpopup.XPopup;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.POST_UPLOAD_PIC;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.me.ui
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 上午 10:13
 */
public class PersonalCenterActivity2 extends BaseTakePhotoActivity {
    private SharedInfoModel mSharedInfo;
    private MineInfoModel mData;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.rl_nick)
    RelativeLayout rlNick;
    @BindView(R.id.rl_vip)
    RelativeLayout rlVip;
    @BindView(R.id.rl_sign)
    RelativeLayout rlSign;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.right_button)
    RelativeLayout rlShare;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_personId)
    TextView tvPersonId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center2;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        rightImg.setVisibility(View.VISIBLE);
        titleName.setText("个人信息");
        getSharedInfo();
        requestData();
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        switch (event) {
            case TAKEPHOTO:
            case EDITORINFO:
            case LOGIN_SUCCESS:
                requestData();
                break;
        }
    }

    @OnClick({R.id.rl_head, R.id.rl_nick, R.id.rl_vip, R.id.rl_sign, R.id.right_button, R.id.left_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_head:  //修改头像
                showDialog();
                break;
            case R.id.rl_nick:  //修改昵称
                intent = new Intent(PersonalCenterActivity2.this, EditNickActivity.class);
                intent.putExtra(EditNickActivity.nick_name, tvNick.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_sign: //修改个性签名
                intent = new Intent(PersonalCenterActivity2.this, EditSignActivity.class);
                intent.putExtra(EditSignActivity.sign, tvSign.getText().toString());
                startActivity(intent);
                break;
            case R.id.right_button: //分享名片
                // showShare();
                ToastUtils.showShortToast("待开发");
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }

    private void requestData() {
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback<MineInfoModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MineInfoModel response, int id) {
                        mData = response;
                        setShowData(response);
                    }
                });
    }

    private void setShowData(MineInfoModel response) {
        String headImg = "";
        if (ivHead != null) {
            if (TextUtils.isEmpty(response.getHeadImg())) {
                headImg = "";
            } else {
                headImg = response.getHeadImg();
            }
            Glide.with(this).load(headImg).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).dontAnimate())
                    .into(ivHead);
        }

        tvNick.setText(response.getNickName() == null ? "小黄鹂" : response.getNickName().toString());
        tvSign.setText(response.getPersonSign() == null ? "" : response.getPersonSign().toString());
        tvPersonId.setText(String.valueOf(response.getUserId()));
        tvVip.setText(response.getGradeName() == null ? "" : response.getGradeName());

    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(PersonalCenterActivity2.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                TakePhoto(false, 1);
                            }
                        })
                        .setSelectCameraListener(new CustomSelectPhotoView.OnSelectCameraListener() {
                            @Override
                            public void onCameraClick() {
                                TakeCamera(false);
                            }
                        }))
                .show();
    }

    /**
     * 获取分享的信息
     */
    private void getSharedInfo() {
        OkHttpUtils.post()//
                .url(Common_HEADER + Constant.GET_SHARED_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback<SharedInfoModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                    }

                    @Override
                    public void onResponse(SharedInfoModel response, int id) {
                        mSharedInfo = response;
                    }

                });
    }

    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (mSharedInfo == null) {
            ToastUtils.showShortToast("分享的资料信息未完善，无法分享");
            return;
        }
        if (TextUtils.isEmpty(mSharedInfo.getNickName()) || TextUtils.isEmpty(mSharedInfo.getLink()) || TextUtils.isEmpty(mSharedInfo.getCompany())) {
            ToastUtils.showShortToast("分享的资料信息未完善，无法分享");
            return;
        }
        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mSharedInfo.getNickName());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mSharedInfo.getLink());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mSharedInfo.getCompany());
        // imagePath是图片地址，Linked-In以外的平台都支持此参数
        // 如果不用本地图片，千万不要调用这个方法！！！
//        oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setImageUrl(mSharedInfo.getHeadImg());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mSharedInfo.getLink());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mSharedInfo.getJob());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mSharedInfo.getLink());
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                KLog.i("share onComplete...");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                KLog.i("share onError..." + throwable);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                KLog.i("share onCancel...");
            }
        });
        // 启动分享GUI
        oks.show(PersonalCenterActivity2.this);
    }

    @Override
    public void takeSuccess(TResult result) {
        String compressPath = result.getImage().getCompressPath();
        Glide.with(mContext).load(compressPath).into(ivHead);
        uploadPic(compressPath);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    private void uploadPic(String compressPath) {
        OkHttpUtils.post()//
                .url(Common_HEADER + POST_UPLOAD_PIC)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .addFile("avatar", "touxiang.png", new File(compressPath))
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {


                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("wangyan", "response====" + response);
                        ToastUtils.showShortToast("上传成功!");
                        requestData();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onWheelSelect(int num, ArrayList<String> mList) {

    }
}
