package com.feitianzhu.huangliwo.me.ui;

import android.annotation.SuppressLint;
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
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.me.base.BaseTakePhotoActivity;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.model.UserVeriModel;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ui.EditApplyRefundActivity;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;
import com.feitianzhu.huangliwo.utils.IDCardValidate;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFNO;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFTYPE;
import static com.feitianzhu.huangliwo.common.Constant.CITYID;
import static com.feitianzhu.huangliwo.common.Constant.CITYNAME;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_USER_AUTH;
import static com.feitianzhu.huangliwo.common.Constant.POST_REALAUTH;
import static com.feitianzhu.huangliwo.common.Constant.PROVINCEID;
import static com.feitianzhu.huangliwo.common.Constant.PROVINCENAME;
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
public class VerificationActivity2 extends BaseTakePhotoActivity {
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
        ShopDao.loadUserVeriInfo(this, new onConnectionFinishLinstener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int code, Object result) {
                UserVeriModel veriModel = (UserVeriModel) result;
                realName.setText(veriModel.realName);
                idNum.setText(veriModel.certifNo);
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    @OnClick({R.id.left_button, R.id.take_photo_one, R.id.take_photo_two, R.id.ly_type, R.id.btn_submit})
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
        }
    }

    public void submit() {
        String id_num = mEdtIdNum.getText().toString().trim();
        if (TextUtils.isEmpty(photo_file_one)) {
            ToastUtils.showShortToast("还没有选择证件正面照");
            return;
        }
        if (TextUtils.isEmpty(photo_file_two)) {
            ToastUtils.showShortToast("还没有选择证件背面照");
            return;
        }
        String name = mEditName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast("还没有填写真实姓名");
            return;
        }
        if (index == -1) {
            ToastUtils.showShortToast("还没有选择证件类型");
            return;
        }
        if (TextUtils.isEmpty(id_num)) {
            ToastUtils.showShortToast("还没有填写证件号码");
            return;
        }
        if ((index != certificates.length - 1)) {
            if (!StringUtils.isIDCard(id_num) && !StringUtils.isPassport(id_num)) {
                ToastUtils.showShortToast("请输入正确的证件号码");
            }
            return;
        }

        String businatures;
        if (index == certificates.length - 1) {
            businatures = "10";
        } else {
            businatures = index + "";
        }

        OkHttpUtils.post()//
                .addFile("certifFile", "01.png", new File(photo_file_one))//
                .addFile("certifFile", "02.png", new File(photo_file_two))//
                .url(Common_HEADER + POST_REALAUTH)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(REALNAME, name)//
                .addParams(CERTIFTYPE, businatures)//
                .addParams(CERTIFNO, id_num)//
                .build()//
                .execute(new Callback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goneloadDialog();
                        ToastUtils.showShortToast("提交成功，请等待验证");
                        EventBus.getDefault().postSticky(AuthEvent.SUCCESS);
                        finish();
                    }
                });
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(VerificationActivity2.this)
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

    @Override
    protected void onWheelSelect(int num, List<String> mList) {
    }

    @Override
    public void takeSuccess(TResult result) {
        switch (photo_type) {
            case 0:
                photo_file_one = result.getImage().getCompressPath();
                Glide.with(VerificationActivity2.this).load(photo_file_one).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriOne);
                break;
            case 1:
                photo_file_two = result.getImage().getCompressPath();
                Glide.with(VerificationActivity2.this).load(photo_file_two).apply(new RequestOptions().placeholder(R.mipmap.g10_03weijiazai).error(R.mipmap.g10_03weijiazai)).into(mImgVeriTwo);
                break;
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
