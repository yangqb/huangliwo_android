package com.feitianzhu.huangliwo.me.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.model.UserVeriModel;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFNO;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFTYPE;
import static com.feitianzhu.huangliwo.common.Constant.LOADER_VERI_USER_INFO;
import static com.feitianzhu.huangliwo.common.Constant.POST_REALAUTH;
import static com.feitianzhu.huangliwo.common.Constant.REALNAME;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.me.ui
 * user: yangqinbo
 * date: 2019/12/23
 * time: 11:28
 * email: 694125155@qq.com
 * <p>
 * 实名认证
 */
public class VerificationActivity2 extends BaseActivity implements ProvinceCallBack {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_SETTING = 300;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAPTURE = 24;
    private Province province;
    private Province.CityListBean city;
    private Province.AreaListBean area;
    private MediaStoreCompat mMediaStoreCompat;
    public static final String AUTH_INFO = "auth_info";
    private String[] certificates = new String[]{"身份证", "护照", "其他"};
    private int photo_type;
    private String photo_file_one = "";
    private String photo_file_two = "";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.take_photo_one)
    ImageView mImgVeriOne;
    @BindView(R.id.take_photo_two)
    ImageView mImgVeriTwo;
    @BindView(R.id.txt_idcard_type)
    TextView mTxtIdcardType;
    @BindView(R.id.ly_shiming_success)
    LinearLayout mLyShimingSuccess;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edt_id_num)
    EditText mEdtIdNum;
    @BindView(R.id.ly_shiming)
    LinearLayout lyShiming;
    @BindView(R.id.ly_wait)
    LinearLayout lyWait;
    @BindView(R.id.txt_person_name)
    TextView realName;
    @BindView(R.id.txt_person_idnum)
    TextView idNum;
    private String token;
    private String userId;
    private UserAuth mAuth;
    private int index = -1;
    @BindView(R.id.txt_permanent_address)
    TextView txtPermanentAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verification2;
    }

    @Override
    protected void initView() {
        titleName.setText("实名认证");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mAuth = (UserAuth) getIntent().getSerializableExtra(AUTH_INFO);
        mMediaStoreCompat = new MediaStoreCompat(this);
        if (mAuth != null) {
            if (mAuth.isRnAuth == 0) {
                //未实名
                lyShiming.setVisibility(View.VISIBLE);
            } else if (mAuth.isRnAuth == -1) {
                //审核被拒绝
                lyShiming.setVisibility(View.VISIBLE);
            } else if (mAuth.isRnAuth == 2) {
                //审核中
                lyShiming.setVisibility(View.GONE);
                lyWait.setVisibility(View.VISIBLE);
            } else {
                //通过
                mLyShimingSuccess.setVisibility(View.VISIBLE);
                lyShiming.setVisibility(View.GONE);
                getAuthInfo();
            }
        } else {
            lyShiming.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    public void getAuthInfo() {

        OkGo.<LzyResponse<UserVeriModel>>get(Urls.BASE_URL + LOADER_VERI_USER_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<UserVeriModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<UserVeriModel>> response) {
                        super.onSuccess(VerificationActivity2.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            UserVeriModel veriModel = response.body().data;
                            realName.setText(veriModel.realName);
                            idNum.setText(veriModel.certifNo);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<UserVeriModel>> response) {
                        super.onError(response);
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.take_photo_one, R.id.take_photo_two, R.id.ly_type, R.id.btn_submit, R.id.rl_permanent_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.take_photo_one:
                photo_type = 0;
                showDialog();
                break;
            case R.id.take_photo_two:
                photo_type = 1;
                showDialog();
                break;
            case R.id.ly_type: //证件类型
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(VerificationActivity2.this)
                                .setData(Arrays.asList(certificates))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        index = position;
                                        mTxtIdcardType.setText(certificates[position]);
                                        mTxtIdcardType.setTextColor(getResources().getColor(R.color.color_333333));
                                    }
                                }))
                        .show();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.rl_permanent_address:
                ProvinceDialog2 branchDialog = ProvinceDialog2.newInstance();
                branchDialog.setCityLevel(ProvinceDialog2.PROVINCE_CITY_AREA);
                branchDialog.setAddress("北京市", "东城区", "东华门街道");
                branchDialog.setSelectOnListener(this);
                branchDialog.show(getSupportFragmentManager());
                break;
        }
    }

    public void submit() {
        String id_num = mEdtIdNum.getText().toString().trim();
        if (TextUtils.isEmpty(photo_file_one)) {
            ToastUtils.show("还没有选择证件正面照");
            return;
        }
        if (TextUtils.isEmpty(photo_file_two)) {
            ToastUtils.show("还没有选择证件背面照");
            return;
        }
        String name = mEditName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show("还没有填写真实姓名");
            return;
        }
        if (index == -1) {
            ToastUtils.show("还没有选择证件类型");
            return;
        }
        if (TextUtils.isEmpty(id_num)) {
            ToastUtils.show("还没有填写证件号码");
            return;
        }
        if ((index != certificates.length - 1)) {
            if (!StringUtils.isIDCard(id_num) && !StringUtils.isPassport(id_num)) {
                ToastUtils.show("请输入正确的证件号码");
                return;
            }
        }

        if (province == null || city == null || area == null) {
            ToastUtils.show("请选择地址");
            return;
        }

        String businatures;
        if (index == certificates.length - 1) {
            businatures = "10";
        } else {
            businatures = (index + 1) + "";
        }

        OkGo.<LzyResponse>post(Urls.BASE_URL + POST_REALAUTH)
                .tag(this)
                .params("certifFile", new File(photo_file_one), "01.png")//
                .params("certifFile", new File(photo_file_two), "02.png")//
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params(REALNAME, name)//
                .params(CERTIFTYPE, businatures)//
                .params(CERTIFNO, id_num)//
                .params("provinceId", province.id)
                .params("provinceName", province.name)
                .params("cityId", city.id)
                .params("cityName", city.name)
                .params("areaId", area.id)
                .params("areaName", area.name)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(VerificationActivity2.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0) {
                            ToastUtils.show("提交成功，请等待验证");
                            EventBus.getDefault().postSticky(AuthEvent.SUCCESS);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(VerificationActivity2.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                //TakePhoto(false, 1);
                                Matisse.from(VerificationActivity2.this)
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
        XXPermissions.with(VerificationActivity2.this)
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
                            Matisse.from(VerificationActivity2.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    List<Uri> uris = Matisse.obtainResult(data);
                    List<String> strings = Matisse.obtainPathResult(data);
                    if (photo_type == 0) {
                        photo_file_one = strings.get(0);
                        Glide.with(VerificationActivity2.this).load(photo_file_one).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriOne);
                    } else {
                        photo_file_two = strings.get(0);
                        Glide.with(VerificationActivity2.this).load(photo_file_two).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriTwo);
                    }
                    break;
                case REQUEST_CODE_CAPTURE:
                    Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
                    String path = mMediaStoreCompat.getCurrentPhotoPath();
                    if (photo_type == 0) {
                        photo_file_one = path;
                        Glide.with(VerificationActivity2.this).load(photo_file_one).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriOne);
                    } else {
                        photo_file_two = path;
                        Glide.with(VerificationActivity2.this).load(photo_file_two).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriTwo);
                    }
                    break;
            }
        }
    }

    @Override
    public void onWhellFinish(Province province, Province.CityListBean city, Province.AreaListBean mAreaListBean) {
        this.province = province;
        this.city = city;
        this.area = mAreaListBean;
        txtPermanentAddress.setText(province.name + city.name + mAreaListBean.name);
    }
}
