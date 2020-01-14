package com.feitianzhu.huangliwo.pushshop;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.base.BaseTakePhotoActivity;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.pushshop.bean.EditMerchantInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.BusinessHoursDialog;
import com.feitianzhu.huangliwo.view.BusinessWeekDayDialog;
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
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
 * date: 2019/12/13
 * time: 18:17
 * email: 694125155@qq.com
 * <p>
 * 商铺详情
 */
public class MerchantsDetailActivity extends BaseTakePhotoActivity implements BusinessHoursDialog.OnTimePickListener, BusinessWeekDayDialog.OnWeekPickListener, OnGetGeoCoderResultListener {
    public static final String IS_MY_MERCHANTS = "is_my_merchants";
    public static final String MERCHANTS_DETAIL_DATA = "merchants_detail_data";
    private String userId;
    private String token;
    private int clsId;
    private String clsName;
    private MerchantsModel merchantsBean;
    private boolean isMySelfMerchants;
    private List<MerchantsClassifyModel.ListBean> listBean;
    private int imgType;
    private boolean isTimes = false;
    private boolean isWeek = false;
    private String photo1 = "";
    private String photo2 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photo5 = "";
    private String photo6 = "";
    private String photo7 = "";
    private double latitude;
    private double longitude;
    private GeoCoder geoCoder;
    @BindView(R.id.right_img)
    ImageView imageView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.edit_merchants_name)
    EditText editMerchantsName;
    @BindView(R.id.edit_merchants_phone)
    EditText editMerchantsPhone;
    @BindView(R.id.edit_merchants_code)
    EditText editMerchantsCode;
    @BindView(R.id.edit_merchants_address)
    EditText editMerchantsAddress;
    @BindView(R.id.edit_merchants_email)
    EditText editMerchantsEmail;
    @BindView(R.id.edit_merchants_discount)
    EditText editMerchantsDiscount;
    @BindView(R.id.edit_merchants_introduction)
    EditText editMerchantsIntroduction;
    @BindView(R.id.select_merchants_type)
    RelativeLayout selectMerchantsType;
    @BindView(R.id.select_merchants_area)
    RelativeLayout selectMerchantsArea;
    @BindView(R.id.merchants_clsName)
    TextView merchantsClsName;
    @BindView(R.id.edit_business_license)
    EditText editBusinessLicenseNo;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.imageView1)
    RoundedImageView logoImg;
    @BindView(R.id.imageView2)
    RoundedImageView shopFrontImg;
    @BindView(R.id.imageView3)
    RoundedImageView shopInsideImg;
    @BindView(R.id.imageView4)
    RoundedImageView cardFrontImg;
    @BindView(R.id.imageView5)
    RoundedImageView cardBackImg;
    @BindView(R.id.imageView6)
    RoundedImageView businessLicenseImg;
    @BindView(R.id.imageView7)
    RoundedImageView permitImg;
    @BindView(R.id.ll_code)
    RelativeLayout llCode;
    @BindView(R.id.tv_business_hours)
    TextView tvBusinessHours;
    @BindView(R.id.tv_business_day)
    TextView tvBusinessDay;
    @BindView(R.id.select_business_hours)
    RelativeLayout selectBusinessHours;
    @BindView(R.id.endLine)
    View endLine;
    @BindView(R.id.submit)
    TextView btnSubmit;

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
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_detail;
    }

    @Override
    protected void initView() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsBean = (MerchantsModel) getIntent().getSerializableExtra(MERCHANTS_DETAIL_DATA);
        isMySelfMerchants = getIntent().getBooleanExtra(IS_MY_MERCHANTS, false);
        imageView.setBackgroundResource(R.mipmap.g06_01bianji);
        rightText.setText("编辑");
        if (isMySelfMerchants) {
            setEditable(false);
            selectBusinessHours.setVisibility(View.VISIBLE);
            endLine.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            setEditable(true);
            selectBusinessHours.setVisibility(View.GONE);
            endLine.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        }
        if (merchantsBean != null) {
            clsId = merchantsBean.getClsId();
            clsName = merchantsBean.getClsName();
            editMerchantsName.setText(merchantsBean.getMerchantName());
            merchantsClsName.setText(merchantsBean.getClsName());
            editBusinessLicenseNo.setText(merchantsBean.getRegisterNo());
            editMerchantsPhone.setText(merchantsBean.getPhone());
            tvArea.setText(merchantsBean.getProvinceName() + merchantsBean.getCityName() + merchantsBean.getAreaName());
            editMerchantsAddress.setText(merchantsBean.getDtlAddr());
            editMerchantsEmail.setText(merchantsBean.getEmail());
            editMerchantsDiscount.setText(String.valueOf(merchantsBean.getDiscount() * 100));
            editMerchantsIntroduction.setText(merchantsBean.getIntroduce());
            if (merchantsBean.getBusinessTime() != null && !TextUtils.isEmpty(merchantsBean.getBusinessTime())) {
                isWeek = true;
                isTimes = true;
                String[] businessTimes = merchantsBean.getBusinessTime().split(",");
                tvBusinessDay.setText(businessTimes[0]);
                tvBusinessHours.setText(businessTimes[1]);
            }
            Glide.with(mContext).load(merchantsBean.getLogo()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(logoImg);
            Glide.with(mContext).load(merchantsBean.getShopFrontImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(shopFrontImg);
            Glide.with(mContext).load(merchantsBean.getShopInsideImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(shopInsideImg);
            Glide.with(mContext).load(merchantsBean.getCardFrontImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(cardFrontImg);
            Glide.with(mContext).load(merchantsBean.getCardBackImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(cardBackImg);
            Glide.with(mContext).load(merchantsBean.getBusinessLicenseImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(businessLicenseImg);
            Glide.with(mContext).load(merchantsBean.getPermitImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(permitImg);
        }
        initListener();
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_SHOPS_TYPE)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .build()
                .execute(new Callback<MerchantsClassifyModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(MerchantsClassifyModel response, int id) {
                        listBean = response.getList();
                    }
                });
    }

    public void initListener() {
        editMerchantsPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(merchantsBean.getPhone())) {
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.tvCode, R.id.tv_business_hours, R.id.tv_business_day, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7
            , R.id.select_merchants_area, R.id.select_merchants_type, R.id.submit, R.id.ll_discount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                setEditable(true);
                btnSubmit.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCode:
                String phone = editMerchantsPhone.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvCode.setEnabled(false);
                tvCode.setBackgroundResource(R.drawable.shape_666666_r14);
                mTimer.start();
                getSmsCode(phone);
                break;
            case R.id.tv_business_hours:
                showTimeDialog();
                break;
            case R.id.tv_business_day:
                showDayDialog();
                break;
            case R.id.imageView1:
                imgType = 1;
                showDialog();
                break;
            case R.id.imageView2:
                imgType = 2;
                showDialog();
                break;
            case R.id.imageView3:
                imgType = 3;
                showDialog();
                break;
            case R.id.imageView4:
                imgType = 4;
                showDialog();
                break;
            case R.id.imageView5:
                imgType = 5;
                showDialog();
                break;
            case R.id.imageView6:
                imgType = 6;
                showDialog();
                break;
            case R.id.imageView7:
                imgType = 7;
                showDialog();
                break;
            case R.id.select_merchants_area:
                setSelectAddress();
                break;
            case R.id.select_merchants_type:
                List<String> strings = new ArrayList<>();
                if (listBean != null && listBean.size() > 0) {
                    for (int i = 0; i < listBean.size(); i++) {
                        strings.add(listBean.get(i).getClsName());
                    }
                    new XPopup.Builder(this)
                            .asCustom(new CustomRefundView(MerchantsDetailActivity.this)
                                    .setData(strings)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            clsId = listBean.get(position).getClsId();
                                            clsName = listBean.get(position).getClsName();
                                            merchantsClsName.setText(clsName);
                                        }
                                    }))
                            .show();
                } else {
                    ToastUtils.showShortToast("商铺类型获取失败");
                }
                break;
            case R.id.ll_discount:
                  //折扣比例说明
                String content = "折扣比例由商家决定折扣部分全额返现给会员消费者，例如：折扣比例为80%，会员消费100元，其中80元直接进入店家账户，随时提现，20元进入会员待释放额度，45天平均返现。";
                new XPopup.Builder(this)
                        .asConfirm("折扣比例说明", content, "", "确定", null, null, true)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.submit:
                submit();
                break;
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
                tvArea.setText("");
                merchantsBean.setProvinceId(province.getId());
                merchantsBean.setProvinceName(province.getName());
                merchantsBean.setCityId(city.getId());
                merchantsBean.setCityName(city.getName());
                merchantsBean.setAreaId(district.getId());
                merchantsBean.setAreaName(district.getName());
                tvArea.setText(province.getName() + city.getName() + district.getName());
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(MerchantsDetailActivity.this)
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

    public void showDayDialog() {
        BusinessWeekDayDialog weekDayDialog = BusinessWeekDayDialog.newInstance();
        weekDayDialog.setBusinessTime("星期三", "星期三");
        weekDayDialog.setOnWeekPickListener(this);
        weekDayDialog.show(getSupportFragmentManager());
    }

    public void showTimeDialog() {
        BusinessHoursDialog hoursDialog = BusinessHoursDialog.newInstance();
        hoursDialog.setBusinessTime("12", "30", "12", "30");
        hoursDialog.setOnTimePickListener(this);
        hoursDialog.show(getSupportFragmentManager());
    }

    /**
     * 获取验证码
     */
    private void getSmsCode(String phone) {
        NetworkDao.getSmsCode(this, phone, "0", new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                KLog.i("response:%s", result.toString());
            }

            @Override
            public void onFail(int code, String result) {
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submit() {
        String merchantsName = editMerchantsName.getText().toString().trim();
        String phone = editMerchantsPhone.getText().toString().trim();
        String smsCode = editMerchantsCode.getText().toString().trim();
        String address = editMerchantsAddress.getText().toString().trim();
        String email = editMerchantsEmail.getText().toString().trim();
        String percentage = editMerchantsDiscount.getText().toString().trim();
        String merchantsIntroduce = editMerchantsIntroduction.getText().toString().trim();
        String businessLicenseNo = editBusinessLicenseNo.getText().toString().trim();
        String businessTimes = tvBusinessDay.getText().toString() + "," + tvBusinessHours.getText().toString();

        if (TextUtils.isEmpty(merchantsName)) {
            ToastUtils.showShortToast("请填写商铺名称");
            return;
        }
        if (TextUtils.isEmpty(businessLicenseNo)) {
            ToastUtils.showShortToast("请填写营业执照号码");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShortToast("请填写商家手机号码");
            return;
        }
        if (llCode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(smsCode)) {
            ToastUtils.showShortToast("请填写验证码");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShortToast("请填写商铺地址");
            return;
        }
        geoCoder.geocode(new GeoCodeOption()
                .city(merchantsBean.getCityName())
                .address(merchantsBean.getCityName() + merchantsBean.getAreaName() + address));

        if (TextUtils.isEmpty(email)) {
            ToastUtils.showShortToast("请填写邮箱地址");
            return;
        }
        if (TextUtils.isEmpty(percentage)) {
            ToastUtils.showShortToast("请填写折扣比例");
            return;
        }
        if (TextUtils.isEmpty(merchantsIntroduce)) {
            ToastUtils.showShortToast("请填写商铺说明");
            return;
        }
        if (isMySelfMerchants) {
            if (!isTimes || !isWeek) {
                ToastUtils.showShortToast("请选择营业日期和时间");
                return;
            }
        }
        EditMerchantInfo merchantInfo = new EditMerchantInfo();
        if (isMySelfMerchants) {
            merchantInfo.setStatus(1);
        } else {
            merchantInfo.setStatus(0);
        }
        merchantInfo.setMerchantId(String.valueOf(merchantsBean.getMerchantId()));
        merchantInfo.setMerchantName(merchantsName);
        merchantInfo.setRegisterNo(businessLicenseNo);
        merchantInfo.setClsId(clsId);
        merchantInfo.setClsName(clsName);
        merchantInfo.setPhone(phone);
        if (llCode.getVisibility() == View.VISIBLE) {
            merchantInfo.setSmsCode(Integer.valueOf(smsCode));
        }
        merchantInfo.setProvinceId(merchantsBean.getProvinceId());
        merchantInfo.setProvinceName(merchantsBean.getProvinceName());
        merchantInfo.setCityName(merchantsBean.getCityName());
        merchantInfo.setCityId(merchantsBean.getCityId());
        merchantInfo.setAreaName(merchantsBean.getAreaName());
        merchantInfo.setAreaId(merchantsBean.getAreaId());
        merchantInfo.setDtlAddr(address);
        merchantInfo.setEmail(email);
        merchantInfo.setDiscount(Double.valueOf(percentage) / 100);
        merchantInfo.setIntroduce(merchantsIntroduce);
        merchantInfo.setLongitude(String.valueOf(longitude));
        merchantInfo.setLatitude(String.valueOf(latitude));
        merchantInfo.setInviteCode(Integer.valueOf(userId));
        if (isMySelfMerchants) {
            merchantInfo.setBusinessTime(businessTimes);
        }
        String json = new Gson().toJson(merchantInfo);
        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(Urls.UPDATA_MERCHANTS);
        if (!TextUtils.isEmpty(photo1)) {
            postFormBuilder.addFile("logo", "logo.png", new File(photo1));
        }
        if (!TextUtils.isEmpty(photo2)) {
            postFormBuilder.addFile("shopFrontImg", "shopFrontImg.png", new File(photo2));
        }
        if (!TextUtils.isEmpty(photo3)) {
            postFormBuilder.addFile("cardFrontImg", "cardFrontImg.png", new File(photo3));
        }
        if (!TextUtils.isEmpty(photo4)) {
            postFormBuilder.addFile("cardFrontImg", "cardFrontImg.png", new File(photo4));
        }
        if (!TextUtils.isEmpty(photo5)) {
            postFormBuilder.addFile("cardBackImg", "cardBackImg.png", new File(photo5));
        }
        if (!TextUtils.isEmpty(photo6)) {
            postFormBuilder.addFile("businessLicenseImg", "businessLicenseImg.png", new File(photo6));
        }
        if (!TextUtils.isEmpty(photo7)) {
            postFormBuilder.addFile("permitImg", "permitImg.png", new File(photo7));
        }
        if (TextUtils.isEmpty(photo1) && TextUtils.isEmpty(photo2) && TextUtils.isEmpty(photo3) && TextUtils.isEmpty(photo4) && TextUtils.isEmpty(photo5) && TextUtils.isEmpty(photo6) && TextUtils.isEmpty(photo7)) {
            postFormBuilder.setMultipart(true);
        }
        postFormBuilder.addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("merchantInfo", json)
                .build()
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
                        goneloadDialog();
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goneloadDialog();
                        EventBus.getDefault().post(UpdataMechantsEvent.SUCCESS);
                        if (isMySelfMerchants) {
                            ToastUtils.showShortToast("修改成功");
                        } else {
                            ToastUtils.showShortToast("提交成功等待审核");
                        }
                        finish();
                    }
                });
    }

    /*
     * 是否可编辑
     * */
    public void setEditable(boolean editable) {
        if (editable) {
            editMerchantsName.setFocusable(true);
            editMerchantsName.setFocusableInTouchMode(true);
            editMerchantsName.requestFocus();
            editMerchantsPhone.setFocusable(true);
            editMerchantsPhone.setFocusableInTouchMode(true);
            editMerchantsCode.setFocusable(true);
            editMerchantsCode.setFocusableInTouchMode(true);
            editMerchantsAddress.setFocusable(true);
            editMerchantsAddress.setFocusableInTouchMode(true);
            editMerchantsEmail.setFocusable(true);
            editMerchantsEmail.setFocusableInTouchMode(true);
            editMerchantsDiscount.setFocusable(true);
            editMerchantsDiscount.setFocusableInTouchMode(true);
            editMerchantsIntroduction.setFocusable(true);
            editMerchantsIntroduction.setFocusableInTouchMode(true);
            selectMerchantsType.setEnabled(true);
            selectMerchantsArea.setEnabled(true);
            selectBusinessHours.setEnabled(true);
        } else {
            editMerchantsName.setFocusable(false);
            editMerchantsName.setFocusableInTouchMode(false);
            editMerchantsPhone.setFocusable(false);
            editMerchantsPhone.setFocusableInTouchMode(false);
            editMerchantsCode.setFocusable(false);
            editMerchantsCode.setFocusableInTouchMode(false);
            editMerchantsAddress.setFocusable(false);
            editMerchantsAddress.setFocusableInTouchMode(false);
            editMerchantsEmail.setFocusable(false);
            editMerchantsEmail.setFocusableInTouchMode(false);
            editMerchantsDiscount.setFocusable(false);
            editMerchantsDiscount.setFocusableInTouchMode(false);
            editMerchantsIntroduction.setFocusable(false);
            editMerchantsIntroduction.setFocusableInTouchMode(false);
            editBusinessLicenseNo.setFocusable(false);
            editBusinessLicenseNo.setFocusableInTouchMode(false);
            selectMerchantsType.setEnabled(false);
            selectMerchantsArea.setEnabled(false);
            selectBusinessHours.setEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onWheelFinish(String startHours, String startMinutes, String endHours, String endMinutes) {
        isTimes = true;
        tvBusinessHours.setText(startHours + ":" + startMinutes + "-" + endHours + ":" + endMinutes);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onWheelFinish(String startWeek, String endWeek) {
        isWeek = true;
        tvBusinessDay.setText(startWeek + "至" + endWeek);
    }

    @Override
    protected void onWheelSelect(int num, List<String> mList) {

    }

    @Override
    public void takeSuccess(TResult result) {
        switch (imgType) {
            case 1:
                photo1 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo1).into(logoImg);
                break;
            case 2:
                photo2 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo2).into(shopFrontImg);
                break;
            case 3:
                photo3 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo3).into(shopInsideImg);
                break;
            case 4:
                photo4 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo4).into(cardFrontImg);
                break;
            case 5:
                photo5 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo5).into(cardBackImg);
                break;
            case 6:
                photo6 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo6).into(businessLicenseImg);
                break;
            case 7:
                photo7 = result.getImage().getCompressPath();
                Glide.with(MerchantsDetailActivity.this).load(photo7).into(permitImg);
                break;
        }
        KLog.i("takeSuccess：" + result.getImage().getCompressPath() + "photo_type" + imgType);
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

}
