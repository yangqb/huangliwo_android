package com.feitianzhu.huangliwo.pushshop;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.dao.NetworkDao;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.ForgetPasswordActivity;
import com.feitianzhu.huangliwo.me.EditAddressActivity;
import com.feitianzhu.huangliwo.me.base.BaseTakePhotoActivity;
import com.feitianzhu.huangliwo.plane.EditPlaneReserveActivity;
import com.feitianzhu.huangliwo.pushshop.bean.EditMerchantInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.shop.ui.MyOrderActivity2;
import com.feitianzhu.huangliwo.utils.KeyboardUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/10
 * time: 20:29
 * email: 694125155@qq.com
 * 编辑商铺
 */
public class EditMerchantsActivity extends BaseTakePhotoActivity implements OnGetGeoCoderResultListener {
    private boolean isConfirm = false;
    public static final String MERCHANTS_DETAIL_DATA = "merchants_detail_data";
    private MerchantsModel merchantsModel;
    private String mProvinceId;
    private String mProvinceName;
    private String mCityId;
    private String mCityName;
    private String mAreaId;
    private String mAreaName;
    private GeoCoder geoCoder;
    private int clsId;
    private String clsName;
    private int imgType;
    private String photo1 = "";
    private String photo2 = "";
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.imageView1)
    RoundedImageView imageView1;
    @BindView(R.id.imageView2)
    RoundedImageView imageView2;
    @BindView(R.id.edit_merchants_name)
    EditText editMerchantsName;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.edit_address)
    EditText editAddress;
    @BindView(R.id.edit_percentage)
    EditText editPercentage;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvAreaAddress)
    TextView tvAreaAddress;
    @BindView(R.id.merchants_clsName)
    TextView merchantsClsName;
    @BindView(R.id.llCode)
    RelativeLayout llCode;
    @BindView(R.id.parent_view)
    LinearLayout parentView;
    private List<MerchantsClassifyModel.ListBean> listBean;
    private double latitude;
    private double longitude;
    private CountDownTimer mTimer = new CountDownTimer(6000 * 10, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            tvCode.setEnabled(true);
            tvCode.setBackgroundResource(R.drawable.shape_fed428_r14);
            tvCode.setText(R.string.resend);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_merchants;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsModel = (MerchantsModel) getIntent().getSerializableExtra(MERCHANTS_DETAIL_DATA);
        titleName.setText("新增门店");
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        if (merchantsModel != null) {
            latitude = Double.valueOf(merchantsModel.getLatitude());
            longitude = Double.valueOf(merchantsModel.getLongitude());
            llCode.setVisibility(View.GONE);
            clsId = merchantsModel.getClsId();
            clsName = merchantsModel.getClsName();
            editMerchantsName.setText(merchantsModel.getMerchantName());
            merchantsClsName.setText(merchantsModel.getClsName());
            editPhone.setText(merchantsModel.getPhone());
            tvAreaAddress.setText(merchantsModel.getProvinceName() + merchantsModel.getCityName() + merchantsModel.getAreaName());
            editAddress.setText(merchantsModel.getDtlAddr());
            editPercentage.setText(MathUtils.subZero(String.valueOf(merchantsModel.getDiscount() * 100)));
            mProvinceName = merchantsModel.getProvinceName();
            mProvinceId = merchantsModel.getProvinceId();
            mCityName = merchantsModel.getCityName();
            mCityId = merchantsModel.getCityId();
            mAreaName = merchantsModel.getAreaName();
            mAreaId = merchantsModel.getAreaId();

            Glide.with(mContext).load(merchantsModel.getLogo()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView1);
            Glide.with(mContext).load(merchantsModel.getShopFrontImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView2);
        }
        initListener();
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MerchantsClassifyModel>>get(Urls.GET_MERCHANTS_TYPE)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MerchantsClassifyModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        super.onSuccess(EditMerchantsActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            listBean = response.body().data.getList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void initListener() {
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (merchantsModel != null) {
                    if (s.toString().equals(merchantsModel.getPhone())) {
                        tvCode.setBackgroundResource(R.drawable.shape_666666_r14);
                        llCode.setVisibility(View.GONE);
                        tvCode.setEnabled(false);
                    } else {
                        llCode.setVisibility(View.VISIBLE);
                        tvCode.setBackgroundResource(R.drawable.shape_fed428_r14);
                        tvCode.setText("验证码");
                        tvCode.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCityName != null && !TextUtils.isEmpty(mCityName)) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(mCityName)
                            .address(mCityName + mAreaName + editAddress.getText().toString().trim()));
                }
            }
        });

        SoftKeyBoardListener.setListener(EditMerchantsActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(AppActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void keyBoardHide(int height) {
                if (!isConfirm && !TextUtils.isEmpty(editPercentage.getText().toString().trim()) && Double.valueOf(editPercentage.getText().toString().trim()) <= 40) {
                    isConfirm = true;
                    new XPopup.Builder(EditMerchantsActivity.this)
                            .asConfirm("", "当前商品折扣过低，请认真查看商家折扣说明再做填写", "", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {

                                }
                            }, null, true)
                            .bindLayout(R.layout.layout_dialog_login) //绑定已有布局
                            .show();
                }
            }
        });

        editPercentage.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    if (!isConfirm && !TextUtils.isEmpty(editPercentage.getText().toString().trim()) && Double.valueOf(editPercentage.getText().toString().trim()) <= 40) {
                        isConfirm = true;
                        new XPopup.Builder(EditMerchantsActivity.this)
                                .asConfirm("", "当前商品折扣过低，请认真查看商家折扣说明再做填写", "", "确定", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {

                                    }
                                }, null, true)
                                .bindLayout(R.layout.layout_dialog_login) //绑定已有布局
                                .show();
                    }
                }
            }
        });
    }

    @OnClick({R.id.left_button, R.id.imageView1, R.id.imageView2, R.id.ll_discount, R.id.submit, R.id.tvCode, R.id.rl_merchants_type, R.id.rl_merchants_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_discount:
                //折扣比例说明
                String content = "折扣比例由商家决定折扣部分全额返现给会员消费者，例如：折扣比例为80%，会员消费100元，其中80元直接进入店家账户，随时提现，20元进入会员待释放额度，45天平均返现。";
                new XPopup.Builder(this)
                        .asConfirm("折扣比例说明", content, "", "确定", null, null, true)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.imageView1:
                imgType = 1;
                showDialog();
                break;
            case R.id.imageView2:
                imgType = 2;
                showDialog();
                break;
            case R.id.submit:
                submit();
                break;
            case R.id.tvCode:
                String phone = editPhone.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvCode.setEnabled(false);
                tvCode.setBackgroundResource(R.drawable.shape_666666_r14);
                mTimer.start();
                getSmsCode(phone);
                break;
            case R.id.rl_merchants_type:
                KeyboardUtils.hideKeyboard(parentView);
                List<String> strings = new ArrayList<>();
                if (listBean != null && listBean.size() > 0) {
                    for (int i = 0; i < listBean.size(); i++) {
                        strings.add(listBean.get(i).getClsName());
                    }
                    new XPopup.Builder(this)
                            .asCustom(new CustomRefundView(EditMerchantsActivity.this)
                                    .setData(strings)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            clsId = listBean.get(position).getClsId();
                                            clsName = listBean.get(position).getClsName();
                                            merchantsClsName.setText(clsName);
                                            merchantsClsName.setTextColor(getResources().getColor(R.color.color_333333));
                                        }
                                    }))
                            .show();
                } else {
                    ToastUtils.showShortToast("商铺类型获取失败");
                }

                break;
            case R.id.rl_merchants_area:
                KeyboardUtils.hideKeyboard(parentView);
                setSelectAddress();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getSmsCode(String phone) {
        String token = SPUtils.getString(EditMerchantsActivity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "0")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditMerchantsActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("验证码已发送至您的手机");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void submit() {
        String merchantsName = editMerchantsName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String smsCode = editCode.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String percentage = editPercentage.getText().toString().trim();
        if (TextUtils.isEmpty(merchantsName)) {
            editMerchantsName.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        if (clsName == null) {
            merchantsClsName.setTextColor(getResources().getColor(R.color.color_ff0000));
        } else {
            merchantsClsName.setTextColor(getResources().getColor(R.color.color_333333));
        }
        if (TextUtils.isEmpty(phone)) {
            editPhone.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        if (llCode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(smsCode)) {
            editCode.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        if (mProvinceName == null || mCityName == null || mAreaName == null) {
            tvAreaAddress.setTextColor(getResources().getColor(R.color.color_ff0000));
        } else {
            tvAreaAddress.setTextColor(getResources().getColor(R.color.color_333333));
        }
        if (TextUtils.isEmpty(address)) {
            editAddress.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        if (TextUtils.isEmpty(percentage)) {
            editPercentage.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }

        if (merchantsModel != null) {
            if (TextUtils.isEmpty(merchantsName) || clsName == null || TextUtils.isEmpty(phone) || (llCode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(smsCode))
                    || mProvinceName == null || mCityName == null || mAreaName == null || TextUtils.isEmpty(address) || TextUtils.isEmpty(percentage)) {
                ToastUtils.showShortToast("您的资料填写不完整");
                return;
            }
        } else {
            if (TextUtils.isEmpty(merchantsName) || clsName == null || TextUtils.isEmpty(phone) || TextUtils.isEmpty(smsCode)
                    || mProvinceName == null || mCityName == null || mAreaName == null || TextUtils.isEmpty(address) || TextUtils.isEmpty(percentage) || TextUtils.isEmpty(photo1) || TextUtils.isEmpty(photo2)) {
                ToastUtils.showShortToast("您的资料填写不完整");
                return;
            }
        }

        if (Double.valueOf(percentage) > 100 || Double.valueOf(percentage) < 0) {
            ToastUtils.showShortToast("折扣比例不能大于100小于0");
            return;
        }
        EditMerchantInfo merchantInfo = new EditMerchantInfo();
        if (merchantsModel != null) {
            merchantInfo.setStatus(0);
            merchantInfo.setMerchantId(String.valueOf(merchantsModel.getMerchantId()));
            merchantInfo.setInviteCode(Integer.valueOf(userId));
        }
        merchantInfo.setMerchantName(merchantsName);
        merchantInfo.setClsId(clsId);
        merchantInfo.setClsName(clsName);
        merchantInfo.setPhone(phone);
        merchantInfo.setProvinceId(mProvinceId);
        merchantInfo.setProvinceName(mProvinceName);
        merchantInfo.setCityName(mCityName);
        merchantInfo.setCityId(mCityId);
        merchantInfo.setAreaName(mAreaName);
        merchantInfo.setAreaId(mAreaId);
        merchantInfo.setDtlAddr(address);
        if (llCode.getVisibility() == View.VISIBLE) {
            merchantInfo.setSmsCode(Integer.valueOf(smsCode));
        }
        merchantInfo.setDiscount(Double.valueOf(percentage) / 100);
        merchantInfo.setLongitude(String.valueOf(longitude));
        merchantInfo.setLatitude(String.valueOf(latitude));
        String json = new Gson().toJson(merchantInfo);

        if (merchantsModel != null) {
            PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.UPDATA_MERCHANTS).tag(this);
            if (!TextUtils.isEmpty(photo1)) {
                postRequest.params("logo", new File(photo1), "logo.png");
            }
            if (!TextUtils.isEmpty(photo2)) {
                postRequest.params("shopFrontImg", new File(photo2), "shopFrontImg.png");
            }
            if (TextUtils.isEmpty(photo1) && TextUtils.isEmpty(photo2)) {
                postRequest.isMultipart(true);
            }
            postRequest.params("accessToken", token)
                    .params("userId", userId)
                    .params("merchantInfo", json)
                    .execute(new JsonCallback<LzyResponse>() {
                        @Override
                        public void onStart(com.lzy.okgo.request.base.Request<LzyResponse, ? extends com.lzy.okgo.request.base.Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                            super.onSuccess(EditMerchantsActivity.this, response.body().msg, response.body().code);
                            goneloadDialog();
                            if (response.body().code == 0) {
                                EventBus.getDefault().post(UpdataMechantsEvent.SUCCESS);
                                ToastUtils.showShortToast("修改成功");
                                finish();
                            }
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                            super.onError(response);
                            goneloadDialog();
                        }
                    });
        } else {
            OkGo.<LzyResponse>post(Urls.CREATE_MERCHANTS)
                    .tag(this).params("logo", new File(photo1), "logo.png")
                    .params("shopFrontImg", new File(photo2), "shopFrontImg.png")
                    .params("accessToken", token)
                    .params("userId", userId)
                    .params("merchantInfo", json)
                    .execute(new JsonCallback<LzyResponse>() {
                        @Override
                        public void onStart(com.lzy.okgo.request.base.Request<LzyResponse, ? extends com.lzy.okgo.request.base.Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                            super.onSuccess(EditMerchantsActivity.this, response.body().msg, response.body().code);
                            goneloadDialog();
                            if (response.body().code == 0) {
                                ToastUtils.showShortToast("创建成功等待审核");
                                EventBus.getDefault().post(UpdataMechantsEvent.SUCCESS);
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
    }

    /*
     * 选择区域
     * */
    public void setSelectAddress() {
        JDCityPicker cityPicker = new JDCityPicker();
        JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();

        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                tvAreaAddress.setText("");
                mProvinceId = province.getId();
                mProvinceName = province.getName();
                mCityId = city.getId();
                mCityName = city.getName();
                mAreaId = district.getId();
                mAreaName = district.getName();
                tvAreaAddress.setText(province.getName() + city.getName() + district.getName());
                tvAreaAddress.setTextColor(getResources().getColor(R.color.color_333333));
                if (!TextUtils.isEmpty(editAddress.getText().toString().trim())) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(mCityName)
                            .address(mCityName + mAreaName + editAddress.getText().toString().trim()));
                }
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(EditMerchantsActivity.this)
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
        switch (imgType) {
            case 1:
                photo1 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo1).into(imageView1);
                break;
            case 2:
                photo2 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo2).into(imageView2);
                break;
        }
        KLog.i("takeSuccess：" + result.getImage().getCompressPath() + "photo_type" + imgType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
            if (geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtils.showShortToast("没有获取到当前商铺地址坐标");
                return;
            } else {
                latitude = geoCodeResult.getLocation().latitude;
                longitude = geoCodeResult.getLocation().longitude;
                Log.e("latitude", latitude + "");
                Log.e("longitude", longitude + "");
            }
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    public File drawableToFile(Context mContext, int drawableId, String fileName) {
//        InputStream is = view.getContext().getResources().openRawResource(R.drawable.logo);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
//        Bitmap bitmap = BitmapFactory.decodeStream(is);

        String defaultPath = mContext.getFilesDir()
                .getAbsolutePath() + "/defaultGoodInfo";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String defaultImgPath = defaultPath + "/" + fileName;
        file = new File(defaultImgPath);
        try {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
//            is.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
