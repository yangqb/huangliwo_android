package com.feitianzhu.huangliwo.pushshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.MultiItemComment;
import com.feitianzhu.huangliwo.pushshop.adapter.ShopFrontAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.EditMerchantInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.MultiShopFront;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.KeyboardUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.SoftKeyBoardListener;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.BusinessHoursDialog;
import com.feitianzhu.huangliwo.view.BusinessWeekDayDialog;
import com.feitianzhu.huangliwo.view.CustomClassificationView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;;
import com.lzy.okgo.request.PostRequest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
public class MerchantsDetailActivity extends BaseActivity implements BusinessHoursDialog.OnTimePickListener, BusinessWeekDayDialog.OnWeekPickListener, OnGetGeoCoderResultListener {
    public static final String IS_MY_MERCHANTS = "is_my_merchants";
    public static final String MERCHANTS_DETAIL_DATA = "merchants_detail_data";
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAPTURE = 24;
    private boolean isConfirm = false;
    private List<MultiShopFront> multiShopFrontList = new ArrayList<>();
    private String userId;
    private String token;
    private int clsId;
    private String clsName;
    private int maxSize = 6;
    private ShopFrontAdapter shopFrontAdapter;
    private MerchantsModel merchantsBean;
    private List<MerchantsClassifyModel.ListBean> listBean;
    private MediaStoreCompat mMediaStoreCompat;
    private List<String> allSelect = new ArrayList<>();
    private int imgType;
    private boolean isTimes = false;
    private boolean isWeek = false;
    private String photo1 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photo5 = "";
    private String photo6 = "";
    private String photo7 = "";
    private double latitude;
    private double longitude;
    private GeoCoder geoCoder;
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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
    @BindView(R.id.submit)
    TextView btnSubmit;
    @BindView(R.id.idCardFront_status)
    TextView idCardFrontStatus;
    @BindView(R.id.idCardBack_status)
    TextView idCardBackStatus;
    @BindView(R.id.business_status)
    TextView businessStatus;
    @BindView(R.id.parent_view)
    LinearLayout parentView;

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
        mMediaStoreCompat = new MediaStoreCompat(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsBean = (MerchantsModel) getIntent().getSerializableExtra(MERCHANTS_DETAIL_DATA);
        //默认有一张上传的图片
        MultiShopFront multiShopFront = new MultiShopFront(MultiShopFront.upImg);
        multiShopFront.setId(R.mipmap.g01_01shangchuan);
        multiShopFrontList.add(multiShopFront);
        if (merchantsBean != null) {
            latitude = Double.valueOf(merchantsBean.getLatitude());
            longitude = Double.valueOf(merchantsBean.getLongitude());
            clsId = merchantsBean.getClsId();
            clsName = merchantsBean.getClsName();
            editMerchantsName.setText(merchantsBean.getMerchantName());
            merchantsClsName.setText(merchantsBean.getClsName());
            editBusinessLicenseNo.setText(merchantsBean.getRegisterNo());
            editMerchantsPhone.setText(merchantsBean.getPhone());
            tvArea.setText(merchantsBean.getProvinceName() + merchantsBean.getCityName() + merchantsBean.getAreaName());
            editMerchantsAddress.setText(merchantsBean.getDtlAddr());

            editMerchantsDiscount.setText(MathUtils.subZero(String.valueOf(merchantsBean.getDiscount() * 100)));
            if (merchantsBean.getIntroduce() != null) {
                editMerchantsIntroduction.setText(merchantsBean.getIntroduce());
            }

            if (merchantsBean.getBusinessTime() != null && !TextUtils.isEmpty(merchantsBean.getBusinessTime())) {
                isWeek = true;
                isTimes = true;
                String[] businessTimes = merchantsBean.getBusinessTime().split("&");
                tvBusinessDay.setText(businessTimes[0]);
                tvBusinessHours.setText(businessTimes[1]);
            }

            if (merchantsBean.getExamineModel() != null) {
                if (merchantsBean.getExamineModel().getBlStatus() == 0 || merchantsBean.getExamineModel().getCardStatus() == 0 || merchantsBean.getExamineModel().getDcStatus() == 0) {
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
            } else {
                btnSubmit.setVisibility(View.VISIBLE);
            }

            Glide.with(mContext).load(merchantsBean.getLogo()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(logoImg);
            //Glide.with(mContext).load(merchantsBean.getShopFrontImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(shopFrontImg);
            Glide.with(mContext).load(merchantsBean.getShopInsideImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(shopInsideImg);
            Glide.with(mContext).load(merchantsBean.getCardFrontImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(cardFrontImg);
            Glide.with(mContext).load(merchantsBean.getCardBackImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(cardBackImg);
            Glide.with(mContext).load(merchantsBean.getBusinessLicenseImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(businessLicenseImg);
            Glide.with(mContext).load(merchantsBean.getPermitImg()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(permitImg);

            if (merchantsBean.getShopFrontImg() != null && !TextUtils.isEmpty(merchantsBean.getShopFrontImg())) {
                if (merchantsBean.getShopFrontImg().contains(",")) {
                    allSelect.addAll(Arrays.asList(merchantsBean.getShopFrontImg().split(",")));
                } else {
                    allSelect.add(merchantsBean.getShopFrontImg());
                }
                for (int i = 0; i < allSelect.size(); i++) {
                    MultiShopFront shopFront = new MultiShopFront(MultiShopFront.LookImg);
                    shopFront.setPath(allSelect.get(i));
                    multiShopFrontList.add(multiShopFrontList.size() - 1, shopFront);
                }
                //超过或等于6张删除掉上传默认上传的那张
                maxSize = 6 - allSelect.size();
                if (maxSize <= 0) {
                    multiShopFrontList.remove(multiShopFrontList.size() - 1);
                }
            }
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        shopFrontAdapter = new ShopFrontAdapter(multiShopFrontList);
        recyclerView.setAdapter(shopFrontAdapter);
        shopFrontAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
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
                        super.onSuccess(MerchantsDetailActivity.this, response.body().msg, response.body().code);
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

        editMerchantsAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (merchantsBean.getCityName() != null && !TextUtils.isEmpty(merchantsBean.getCityName())) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(merchantsBean.getCityName())
                            .address(merchantsBean.getCityName() + merchantsBean.getAreaName() + editMerchantsAddress.getText().toString().trim()));
                }
            }
        });

        SoftKeyBoardListener.setListener(MerchantsDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //Toast.makeText(AppActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void keyBoardHide(int height) {
                if (!isConfirm && !TextUtils.isEmpty(editMerchantsDiscount.getText().toString().trim()) && Double.valueOf(editMerchantsDiscount.getText().toString().trim()) <= 40) {
                    isConfirm = true;
                    new XPopup.Builder(MerchantsDetailActivity.this)
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

        editMerchantsDiscount.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    if (!isConfirm && !TextUtils.isEmpty(editMerchantsDiscount.getText().toString().trim()) && Double.valueOf(editMerchantsDiscount.getText().toString().trim()) <= 40) {
                        isConfirm = true;
                        new XPopup.Builder(MerchantsDetailActivity.this)
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

        shopFrontAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == MultiItemComment.upImg) {
                    imgType = 2;
                    selectPhoto(maxSize);
                } else {
                    //showBigImg(allSelect, position);
                }
            }
        });

        shopFrontAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                multiShopFrontList.remove(position);
                shopFrontAdapter.setNewData(multiShopFrontList);
                shopFrontAdapter.notifyDataSetChanged();
                allSelect.remove(position);
                maxSize = 6 - allSelect.size();
                if (maxSize == 1) {
                    MultiShopFront shopFront = new MultiShopFront(MultiShopFront.upImg);
                    shopFront.setId(R.mipmap.g01_01shangchuan);
                    multiShopFrontList.add(shopFront);
                }
            }
        });

    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.tvCode, R.id.tv_business_hours, R.id.tv_business_day, R.id.imageView1, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7
            , R.id.select_merchants_area, R.id.select_merchants_type, R.id.submit, R.id.ll_discount})
    @SingleClick()
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
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
                selectPhoto(1);
                break;
            case R.id.imageView3:
                imgType = 3;
                selectPhoto(1);
                break;
            case R.id.imageView4:
                imgType = 4;
                selectPhoto(1);
                break;
            case R.id.imageView5:
                imgType = 5;
                selectPhoto(1);
                break;
            case R.id.imageView6:
                imgType = 6;
                selectPhoto(1);
                break;
            case R.id.imageView7:
                imgType = 7;
                selectPhoto(1);
                break;
            case R.id.select_merchants_area:
                KeyboardUtils.hideKeyboard(parentView);
                setSelectAddress();
                break;
            case R.id.select_merchants_type:
                KeyboardUtils.hideKeyboard(parentView);
                List<String> strings = new ArrayList<>();
                if (listBean != null && listBean.size() > 0) {
                    for (int i = 0; i < listBean.size(); i++) {
                        strings.add(listBean.get(i).getClsName());
                    }
                    new XPopup.Builder(this)
                            .asCustom(new CustomClassificationView(MerchantsDetailActivity.this)
                                    .setData(strings)
                                    .setOnItemClickListener(new CustomClassificationView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            clsId = listBean.get(position).getClsId();
                                            clsName = listBean.get(position).getClsName();
                                            merchantsClsName.setText(clsName);
                                        }
                                    }))
                            .show();
                } else {
                    ToastUtils.show("商铺类型获取失败");
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
                if (!TextUtils.isEmpty(editMerchantsAddress.getText().toString().trim())) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(merchantsBean.getCityName())
                            .address(merchantsBean.getCityName() + merchantsBean.getAreaName() + editMerchantsAddress.getText().toString().trim()));
                }
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    public void selectPhoto(int maxNum) {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(MerchantsDetailActivity.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                //TakePhoto(false, 1);
                                Matisse.from(MerchantsDetailActivity.this)
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
                                        .maxSelectable(maxNum)
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
        XXPermissions.with(MerchantsDetailActivity.this)
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
                            Matisse.from(MerchantsDetailActivity.this)
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
        String token = SPUtils.getString(MerchantsDetailActivity.this, Constant.SP_ACCESS_TOKEN, "");
        OkGo.<LzyResponse>get(Urls.GET_SMSCODE)
                .tag(this)
                .params("phone", phone)
                .params("type", "0")
                .params("accessToken", token)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(MerchantsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("验证码已发送至您的手机");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    private StringBuffer imgUrls = new StringBuffer();

    public void submit() {
        String merchantsName = editMerchantsName.getText().toString().trim();
        String phone = editMerchantsPhone.getText().toString().trim();
        String smsCode = editMerchantsCode.getText().toString().trim();
        String address = editMerchantsAddress.getText().toString().trim();
        String email = editMerchantsEmail.getText().toString().trim();
        String percentage = editMerchantsDiscount.getText().toString().trim();
        String merchantsIntroduce = editMerchantsIntroduction.getText().toString().trim();
        String businessLicenseNo = editBusinessLicenseNo.getText().toString().trim();
        String businessTimes = tvBusinessDay.getText().toString() + "&" + tvBusinessHours.getText().toString();

        if (TextUtils.isEmpty(merchantsName)) {
            editMerchantsName.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }

        if (TextUtils.isEmpty(phone)) {
            editMerchantsPhone.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        if (llCode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(smsCode)) {
            editMerchantsCode.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }

        if (TextUtils.isEmpty(address)) {
            editMerchantsAddress.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }
        /*if (TextUtils.isEmpty(email)) {
            ToastUtils.show("请填写邮箱地址");
            return;
        }*/
        /*if (!StringUtils.isEmail(email)) {
            ToastUtils.show("请填写正确的邮箱地址");
            return;
        }*/

        if (TextUtils.isEmpty(percentage)) {
            editMerchantsDiscount.setHintTextColor(getResources().getColor(R.color.color_ff0000));
        }

        if (!isTimes) {
            tvBusinessHours.setTextColor(getResources().getColor(R.color.color_ff0000));
        } else {
            tvBusinessHours.setTextColor(getResources().getColor(R.color.color_333333));
        }
        if (!isWeek) {
            tvBusinessDay.setTextColor(getResources().getColor(R.color.color_ff0000));
        } else {
            tvBusinessDay.setTextColor(getResources().getColor(R.color.color_333333));
        }


        if (TextUtils.isEmpty(merchantsName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(address) || TextUtils.isEmpty(percentage) || (!isTimes || !isWeek) || allSelect.size() <= 0) {
            ToastUtils.show("您的资料填写不完整");
            return;
        }

        if (!TextUtils.isEmpty(businessLicenseNo)) {
            if (!(businessLicenseNo.length() == 15 || businessLicenseNo.length() == 18)) {
                ToastUtils.show("请输入正确的营业执照号");
                return;
            }
        }

        if (Double.valueOf(percentage) > 100 || Double.valueOf(percentage) < 0) {
            ToastUtils.show("折扣比例不能大于100小于0");
            return;
        }

        EditMerchantInfo merchantInfo = new EditMerchantInfo();
        merchantInfo.setStatus(1);
        //merchantInfo.setStatus(0);

        merchantInfo.setMerchantId(String.valueOf(merchantsBean.getMerchantId()));
        merchantInfo.setMerchantName(merchantsName);
      /*  if (!merchantsBean.getRegisterNo().equals(businessLicenseNo)) {
            merchantInfo.setRegisterNo(businessLicenseNo);
        }*/
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
        //merchantInfo.setInviteCode(Integer.valueOf(userId));
        merchantInfo.setBusinessTime(businessTimes);
        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.UPDATA_MERCHANTS).tag(this);

        if (!TextUtils.isEmpty(photo1)) {
            postRequest.params("logo", new File(photo1), "logo.png");
        }
        List<File> fileList = new ArrayList<>();
        if (allSelect.size() > 0) {
            for (int i = 0; i < allSelect.size(); i++) {
                if (allSelect.get(i).startsWith("http") || allSelect.get(i).startsWith("https")) {
                    String url = allSelect.get(i) + ",";
                    imgUrls.append(url);
                } else {
                    fileList.add(new File(allSelect.get(i)));
                }
            }
        }
        if (fileList.size() > 0) {
            postRequest.addFileParams("shopFrontImg", fileList);
        }

        if (imgUrls.toString().endsWith(",")) {
            String substring = imgUrls.substring(0, imgUrls.lastIndexOf(","));
            merchantInfo.setShopFrontImg(substring);
        }


        if (!TextUtils.isEmpty(photo3)) {
            postRequest.params("cardFrontImg", new File(photo3), "cardFrontImg.png");
        }
        if (!TextUtils.isEmpty(photo4)) {
            postRequest.params("cardFrontImg", new File(photo4), "cardFrontImg.png");
        }
        if (!TextUtils.isEmpty(photo5)) {
            postRequest.params("cardBackImg", new File(photo5), "cardBackImg.png");
        }
        if (!TextUtils.isEmpty(photo6)) {
            postRequest.params("businessLicenseImg", new File(photo6), "businessLicenseImg.png");
        }
        if (!TextUtils.isEmpty(photo7)) {
            postRequest.params("permitImg", new File(photo7), "permitImg.png");
        }
        if (TextUtils.isEmpty(photo1) && fileList.size() <= 0 && TextUtils.isEmpty(photo3) && TextUtils.isEmpty(photo4) && TextUtils.isEmpty(photo5) && TextUtils.isEmpty(photo6) && TextUtils.isEmpty(photo7)) {
            postRequest.isMultipart(true);
        }
        String json = new Gson().toJson(merchantInfo);
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
                        super.onSuccess(MerchantsDetailActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0) {
                            EventBus.getDefault().post(UpdataMechantsEvent.SUCCESS);
                            ToastUtils.show("修改成功");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onWheelFinish(String startHours, String startMinutes, String endHours, String endMinutes) {
        isTimes = true;
        tvBusinessHours.setText(startHours + ":" + startMinutes + "-" + endHours + ":" + endMinutes);
        tvBusinessHours.setTextColor(getResources().getColor(R.color.color_333333));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onWheelFinish(String startWeek, String endWeek) {
        isWeek = true;
        tvBusinessDay.setText(startWeek + "至" + endWeek);
        tvBusinessDay.setTextColor(getResources().getColor(R.color.color_333333));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
            if (geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtils.show("没有获取到当前商铺地址坐标");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    List<Uri> uris = Matisse.obtainResult(data);
                    List<String> currSelect = Matisse.obtainPathResult(data);
                    if (imgType == 1) {
                        photo1 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo1).into(logoImg);
                    } else if (imgType == 2) {
                        allSelect.addAll(currSelect);
                        for (int i = 0; i < currSelect.size(); i++) {
                            MultiShopFront shopFront = new MultiShopFront(MultiShopFront.LookImg);
                            shopFront.setPath(currSelect.get(i));
                            multiShopFrontList.add(multiShopFrontList.size() - 1, shopFront);
                        }
                        maxSize = 6 - allSelect.size();
                        if (maxSize <= 0) {
                            multiShopFrontList.remove(multiShopFrontList.size() - 1);
                        }
                        shopFrontAdapter.setNewData(multiShopFrontList);
                        shopFrontAdapter.notifyDataSetChanged();
                        //Glide.with(MerchantsDetailActivity.this).load(photo2).into(shopFrontImg);
                    } else if (imgType == 3) {
                        photo3 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo3).into(shopInsideImg);
                    } else if (imgType == 4) {
                        photo4 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo4).into(cardFrontImg);
                    } else if (imgType == 5) {
                        photo5 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo5).into(cardBackImg);
                    } else if (imgType == 6) {
                        photo6 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo6).into(businessLicenseImg);
                    } else if (imgType == 7) {
                        photo7 = currSelect.get(0);
                        Glide.with(MerchantsDetailActivity.this).load(photo7).into(permitImg);
                    }
                    break;
                case REQUEST_CODE_CAPTURE:
                    Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
                    String path = mMediaStoreCompat.getCurrentPhotoPath();
                    if (imgType == 1) {
                        photo1 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo1).into(logoImg);
                    } else if (imgType == 2) {
                        allSelect.add(path);
                        MultiShopFront shopFront = new MultiShopFront(MultiShopFront.LookImg);
                        shopFront.setPath(path);
                        multiShopFrontList.add(multiShopFrontList.size() - 1, shopFront);

                        maxSize = 6 - allSelect.size();
                        if (maxSize <= 0) {
                            multiShopFrontList.remove(multiShopFrontList.size() - 1);
                        }
                        shopFrontAdapter.setNewData(multiShopFrontList);
                        shopFrontAdapter.notifyDataSetChanged();
                        //Glide.with(MerchantsDetailActivity.this).load(photo2).into(shopFrontImg);
                    } else if (imgType == 3) {
                        photo3 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo3).into(shopInsideImg);
                    } else if (imgType == 4) {
                        photo4 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo4).into(cardFrontImg);
                    } else if (imgType == 5) {
                        photo5 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo5).into(cardBackImg);
                    } else if (imgType == 6) {
                        photo6 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo6).into(businessLicenseImg);
                    } else if (imgType == 7) {
                        photo7 = path;
                        Glide.with(MerchantsDetailActivity.this).load(photo7).into(permitImg);
                    }
                    break;
            }
        }
    }
}
