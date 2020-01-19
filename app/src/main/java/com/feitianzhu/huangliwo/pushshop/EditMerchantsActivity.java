package com.feitianzhu.huangliwo.pushshop;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.feitianzhu.huangliwo.me.base.BaseTakePhotoActivity;
import com.feitianzhu.huangliwo.pushshop.bean.EditMerchantInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
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
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.devio.takephoto.model.TResult;

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
 * date: 2019/12/10
 * time: 20:29
 * email: 694125155@qq.com
 * 编辑商铺
 */
public class EditMerchantsActivity extends BaseTakePhotoActivity implements OnGetGeoCoderResultListener {
    private ProvinceBean mProvince;
    private CityBean mCity;
    private DistrictBean mDistrict;
    private GeoCoder geoCoder;
    private int clsId;
    private String clsName;
    private int imgType;
    private String photo1 = "";
    private String photo2 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photo5 = "";
    private String photo6 = "";
    private String photo7 = "";
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.imageView1)
    RoundedImageView imageView1;
    @BindView(R.id.imageView2)
    RoundedImageView imageView2;
    @BindView(R.id.imageView3)
    RoundedImageView imageView3;
    @BindView(R.id.imageView4)
    RoundedImageView imageView4;
    @BindView(R.id.imageView5)
    RoundedImageView imageView5;
    @BindView(R.id.imageView6)
    RoundedImageView imageView6;
    @BindView(R.id.imageView7)
    RoundedImageView imageView7;
    @BindView(R.id.edit_merchants_name)
    EditText editMerchantsName;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.edit_address)
    EditText editAddress;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_percentage)
    EditText editPercentage;
    @BindView(R.id.edit_merchants_introduce)
    EditText editMerchantsIntroduce;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvAreaAddress)
    TextView tvAreaAddress;
    @BindView(R.id.merchants_clsName)
    TextView merchantsClsName;
    @BindView(R.id.edit_business_license)
    EditText editBusinessLicenseNo;
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
        titleName.setText("新增门店");
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        initListener();
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS_TYPE)
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
        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCity != null && !TextUtils.isEmpty(mCity.getName())) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(mCity.getName())
                            .address(mCity.getName() + mDistrict.getName() + editAddress.getText().toString().trim()));
                }
            }
        });

    }

    @OnClick({R.id.left_button, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.ll_discount, R.id.submit, R.id.tvCode, R.id.rl_merchants_type, R.id.rl_merchants_area})
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
                                        }
                                    }))
                            .show();
                } else {
                    ToastUtils.showShortToast("商铺类型获取失败");
                }

                break;
            case R.id.rl_merchants_area:
                setSelectAddress();
                break;
        }
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
        String phone = editPhone.getText().toString().trim();
        String smsCode = editCode.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String percentage = editPercentage.getText().toString().trim();
        String merchantsIntroduce = editMerchantsIntroduce.getText().toString().trim();
        String businessLicenseNo = editBusinessLicenseNo.getText().toString().trim();

        if (TextUtils.isEmpty(merchantsName)) {
            ToastUtils.showShortToast("请填写商铺名称");
            return;
        }
        if (clsName == null) {
            ToastUtils.showShortToast("请选择商铺类型");
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
        if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.showShortToast("请填写验证码");
            return;
        }
        if (mProvince == null || mCity == null || mDistrict == null) {
            ToastUtils.showShortToast("请选择商铺所在地区");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShortToast("请填写商铺地址");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showShortToast("请填写邮箱地址");
            return;
        }
        if (!StringUtils.isEmail(email)) {
            ToastUtils.showShortToast("请填写正确的邮箱地址");
            return;
        }

        if (TextUtils.isEmpty(percentage)) {
            ToastUtils.showShortToast("请填写折扣比例");
            return;
        }
        if (Integer.valueOf(percentage) > 100) {
            ToastUtils.showShortToast("折扣比例不能大于100%");
            return;
        }
        if (TextUtils.isEmpty(merchantsIntroduce)) {
            ToastUtils.showShortToast("请填写商铺说明");
            return;
        }
        if (TextUtils.isEmpty(photo1)) {
            ToastUtils.showShortToast("请上传商品logo");
            return;
        }
        if (TextUtils.isEmpty(photo2)) {
            ToastUtils.showShortToast("请上传店铺门面照片");
            return;
        }
        if (TextUtils.isEmpty(photo3)) {
            ToastUtils.showShortToast("请上传店内环境照片");
            return;
        }
        if (TextUtils.isEmpty(photo4)) {
            ToastUtils.showShortToast("请上传身份证正面照片");
            return;
        }
        if (TextUtils.isEmpty(photo5)) {
            ToastUtils.showShortToast("请上传身份证反面照片");
            return;
        }
        if (TextUtils.isEmpty(photo6)) {
            ToastUtils.showShortToast("请上传店铺营业执照");
            return;
        }
        if (TextUtils.isEmpty(photo7)) {
            ToastUtils.showShortToast("请上传经营许可证");
            return;
        }

        EditMerchantInfo merchantInfo = new EditMerchantInfo();
        merchantInfo.setMerchantName(merchantsName);
        merchantInfo.setRegisterNo(businessLicenseNo);
        merchantInfo.setClsId(clsId);
        merchantInfo.setClsName(clsName);
        merchantInfo.setPhone(phone);
        merchantInfo.setSmsCode(Integer.valueOf(smsCode));
        merchantInfo.setProvinceId(mProvince.getId());
        merchantInfo.setProvinceName(mProvince.getName());
        merchantInfo.setCityName(mCity.getName());
        merchantInfo.setCityId(mCity.getId());
        merchantInfo.setAreaName(mDistrict.getName());
        merchantInfo.setAreaId(mDistrict.getId());
        merchantInfo.setDtlAddr(address);
        merchantInfo.setEmail(email);
        merchantInfo.setDiscount(Double.valueOf(percentage) / 100);
        merchantInfo.setIntroduce(merchantsIntroduce);
        merchantInfo.setLongitude(String.valueOf(longitude));
        merchantInfo.setLatitude(String.valueOf(latitude));
        merchantInfo.setInviteCode(Integer.valueOf(userId));
        String json = new Gson().toJson(merchantInfo);
        Map<String, File> files = new HashMap<>();
        files.put("logo.png", new File(photo1));
        files.put("shopFrontImg.png", new File(photo2));
        files.put("shopInsideImg.png", new File(photo3));
        files.put("cardFrontImg.png", new File(photo4));
        files.put("cardBackImg.png", new File(photo5));
        files.put("businessLicenseImg.png", new File(photo6));
        files.put("permitImg.png", new File(photo7));
        OkHttpUtils.post().url(Urls.CREATE_MERCHANTS)
                .addFiles("files", files)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("merchantInfo", json)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        goneloadDialog();
                        ToastUtils.showShortToast("创建成功等待审核");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
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
                mProvince = province;
                mCity = city;
                mDistrict = district;
                tvAreaAddress.setText(province.getName() + city.getName() + district.getName());
                if (!TextUtils.isEmpty(editAddress.getText().toString().trim())) {
                    geoCoder.geocode(new GeoCodeOption()
                            .city(mCity.getName())
                            .address(mCity.getName() + mDistrict.getName() + editAddress.getText().toString().trim()));
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
            case 3:
                photo3 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo3).into(imageView3);
                break;
            case 4:
                photo4 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo4).into(imageView4);
                break;
            case 5:
                photo5 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo5).into(imageView5);
                break;
            case 6:
                photo6 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo6).into(imageView6);
                break;
            case 7:
                photo7 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo7).into(imageView7);
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
}
