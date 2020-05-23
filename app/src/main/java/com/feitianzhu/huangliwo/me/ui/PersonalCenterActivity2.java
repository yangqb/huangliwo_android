package com.feitianzhu.huangliwo.me.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.SharedInfoModel;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.POST_UPLOAD_PIC;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.me.ui
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 上午 10:13
 */
public class PersonalCenterActivity2 extends BaseActivity {
    private static final int NICK_REQUEST_CODE = 1000;
    private static final int SIGN_REQUEST_CODE = 999;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAPTURE = 24;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_SETTING = 300;
    private MediaStoreCompat mMediaStoreCompat;
    private SharedInfoModel mSharedInfo;
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
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_personId)
    TextView tvPersonId;
    private String userId;
    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center2;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mMediaStoreCompat = new MediaStoreCompat(this);
        //rightImg.setVisibility(View.VISIBLE);
        titleName.setText("个人信息");
        getSharedInfo();
        requestData();
    }

    @Override
    protected void initData() {

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
                intent.putExtra(EditNickActivity.NICE_NAME, tvNick.getText().toString());
                startActivityForResult(intent, NICK_REQUEST_CODE);
                break;
            case R.id.rl_sign: //修改个性签名
                intent = new Intent(PersonalCenterActivity2.this, EditSignActivity.class);
                intent.putExtra(EditSignActivity.SIGN, tvSign.getText().toString());
                startActivityForResult(intent, SIGN_REQUEST_CODE);
                break;
            case R.id.right_button: //分享名片
                // showShare();
                ToastUtils.show("敬请期待");
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }

    private void requestData() {
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(PersonalCenterActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            setShowData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
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

        tvNick.setText(response.getNickName() == null ? "" : response.getNickName().toString());
        tvSign.setText(response.getPersonSign() == null ? "" : response.getPersonSign().toString());
        tvPersonId.setText(String.valueOf(response.getUserId()));
        if (response.getAccountType() == 0) {
            tvVip.setText("消费者");
        } else if (response.getAccountType() == 1) {
            tvVip.setText("市代理");
        } else if (response.getAccountType() == 2) {
            tvVip.setText("区代理");
        } else if (response.getAccountType() == 3) {
            tvVip.setText("合伙人");
        } else if (response.getAccountType() == 4) {
            tvVip.setText("超级会员");
        } else if (response.getAccountType() == 5) {
            tvVip.setText("优选会员");
        } else if (response.getAccountType() == 7) {
            tvVip.setText("省代理");
        }
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(PersonalCenterActivity2.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                //TakePhoto(false, 1);
                                Matisse.from(PersonalCenterActivity2.this)
                                        .choose(MimeType.ofImage())
                                        //自定义选择选择的类型
                                        //.choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
                                        //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                        .showSingleMediaType(true)
                                        /*.capture(true)  // 使用相机，和 captureStrategy 一起使用
                                        .captureStrategy(new CaptureStrategy(true, "com.feitianzhu.fu700.fileprovider"))*/
                                        //有序选择图片 123456...
                                        .countable(true)
                                        //最大选择数量为6
                                        .maxSelectable(1)
                                        //选择方向
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        //图片过滤
                                        //.addFilter()
                                        //界面中缩略图的质量
                                        .thumbnailScale(0.85f)
                                        //蓝色主题
                                        .theme(R.style.Matisse_Zhihu)
                                        //黑色主题
                                        //.theme(R.style.Matisse_Dracula)
                                        //Picasso加载方式
                                        //.imageEngine(new PicassoEngine())
                                        //Glide加载方式
                                        .originalEnable(true)
                                        .maxOriginalSize(10)
                                        .imageEngine(new Glide4Engine())
                                        .forResult(REQUEST_CODE_CHOOSE);
                            }
                        })
                        .setSelectCameraListener(new CustomSelectPhotoView.OnSelectCameraListener() {
                            @Override
                            public void onCameraClick() {
                                //TakeCamera(false);
                                requestPermission();
                            }
                        }))
                .show();
    }

    private void requestPermission() {
        XXPermissions.with(PersonalCenterActivity2.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Matisse.from(PersonalCenterActivity2.this)
                                    .capture()
                                    .captureStrategy(new CaptureStrategy(true, "com.feitianzhu.huangliwo.fileprovider", "bldby"))
                                    .forResult(REQUEST_CODE_CAPTURE, mMediaStoreCompat);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }

    /**
     * 获取分享的信息
     */
    private void getSharedInfo() {

        OkGo.<LzyResponse<SharedInfoModel>>post(Urls.BASE_URL + Constant.GET_SHARED_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<SharedInfoModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<SharedInfoModel>> response) {
                        //super.onSuccess(PersonalCenterActivity2.this, "", response.body().code);
                        if (response.body().data != null && response.body().code == 0) {
                            mSharedInfo = response.body().data;
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<SharedInfoModel>> response) {
                        super.onError(response);
                    }
                });
    }


    private void uploadPic(String compressPath) {
        OkGo.<LzyResponse>post(Urls.BASE_URL + POST_UPLOAD_PIC)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("avatar", new File(compressPath), "touxiang.png")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(PersonalCenterActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("上传成功!");
                            EventBus.getDefault().postSticky(LoginEvent.EDITOR_INFO);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NICK_REQUEST_CODE:
                    String nick = data.getStringExtra(EditNickActivity.NICE_NAME);
                    tvNick.setText(nick);
                    break;
                case SIGN_REQUEST_CODE:
                    String sign = data.getStringExtra(EditSignActivity.SIGN);
                    tvSign.setText(sign);
                    break;
                case REQUEST_CODE_CHOOSE:
                    List<Uri> uris = Matisse.obtainResult(data);
                    List<String> strings = Matisse.obtainPathResult(data);
                    Glide.with(mContext).load(strings.get(0)).into(ivHead);
                    uploadPic(strings.get(0));
                    /*mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
                    Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(data)));*/
                    break;
                case REQUEST_CODE_CAPTURE:
                    Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
                    String path = mMediaStoreCompat.getCurrentPhotoPath();
                    Glide.with(mContext).load(path).into(ivHead);
                    uploadPic(path);
                         /*ArrayList<String> selectedPath = new ArrayList<>();
                    selectedPath.add(path);
                    ArrayList<Uri> selected = new ArrayList<>();
                    selected.add(contentUri);
                    mAdapter.setData(selected, selectedPath);*/
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
