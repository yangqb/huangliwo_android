package com.feitianzhu.huangliwo.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.SwitchButton;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class EditAddressActivity extends BaseActivity {
    public static final String ADDRESS_DATA = "address_data";
    public static final String IS_ADD_ADDRESS = "is_add_address";
    private int isDefalt;
    private boolean isAdd;
    private ProvinceBean mProvince = new ProvinceBean();
    private CityBean mCity = new CityBean();
    private DistrictBean mDistrict = new DistrictBean();
    private AddressInfo.ShopAddressListBean shopAddressListBean;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.delete_address)
    RelativeLayout deleteAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editPhone)
    EditText editPhone;
    @BindView(R.id.editAddressDetail)
    EditText editAddressDetail;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        isAdd = getIntent().getBooleanExtra(IS_ADD_ADDRESS, false);
        shopAddressListBean = (AddressInfo.ShopAddressListBean) getIntent().getSerializableExtra(ADDRESS_DATA);
        if (shopAddressListBean != null) {
            editName.setText(shopAddressListBean.getUserName());
            editPhone.setText(shopAddressListBean.getPhone());
            editAddressDetail.setText(shopAddressListBean.getDetailAddress());
            tvAddress.setText(shopAddressListBean.getProvinceName() + "\n"
                    + shopAddressListBean.getCityName() + "\n"
                    + shopAddressListBean.getAreaName());
            mProvince.setId(shopAddressListBean.getProvinceId());
            mProvince.setName(shopAddressListBean.getProvinceName());
            mCity.setId(shopAddressListBean.getCityId());
            mCity.setName(shopAddressListBean.getCityName());
            mDistrict.setId(shopAddressListBean.getAreaId());
            mDistrict.setName(shopAddressListBean.getAreaName());
            if (shopAddressListBean.getIsDefalt() == 1) {
                switchButton.setChecked(true);
            }
        }
        if (isAdd) {
            titleName.setText("新建地址");
            deleteAddress.setVisibility(View.GONE);
        } else {
            titleName.setText("编辑收货地址");
            deleteAddress.setVisibility(View.VISIBLE);
        }
        rightText.setText("保存");
        rightText.setVisibility(View.VISIBLE);

        switchButton.setBackgroundColorChecked(getResources().getColor(R.color.bg_yellow));
        switchButton.setBackgroundColorUnchecked(getResources().getColor(R.color.color_F1EFEF));
        switchButton.setAnimateDuration(300);
        switchButton.setButtonColor(getResources().getColor(R.color.white));
    }


    @OnClick({R.id.left_button, R.id.right_button, R.id.delete_address, R.id.select_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button: //保存地址
                if (checkText(editName.getText().toString().trim())) {
                    ToastUtils.showShortToast("请填写收货人姓名");
                } else if (checkText(editPhone.getText().toString().trim())) {
                    ToastUtils.showShortToast("请填写收货人电话");
                } else if (checkText(tvAddress.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择收货人地址");
                } else if (checkText(editAddressDetail.getText().toString().trim())) {
                    ToastUtils.showShortToast("请填写收货人详细地址");
                } else {
                    submit();
                }
                break;
            case R.id.delete_address:
                new XPopup.Builder(this)
                        .asConfirm("确定要删除该地址？", "", "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                deleteAddress(shopAddressListBean.getAddressId());
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.select_address:
                setSelectAddress();
                break;
        }
    }

    /*
     * 删除地址
     * */
    public void deleteAddress(int addressId) {
        OkHttpUtils.post()
                .url(Urls.DELETE_ADDRESS)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .addParams("addressId", addressId + "")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("删除失败");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        setResult(RESULT_OK);
                        ToastUtils.showShortToast("删除成功");
                        finish();
                    }
                });

    }

    public void submit() {
        if (switchButton.isChecked()) {
            isDefalt = 1;
        } else {
            isDefalt = 0;
        }

        if (isAdd) {
            //新增地址
            if ("710000".equals(mProvince.getId()) || "810000".equals(mProvince.getId()) || "820000".equals(mProvince.getId())) {
                ToastUtils.showShortToast("暂不支持港澳台地区配送");
                return;
            }
            OkHttpUtils.post()
                    .url(Urls.ADD_ADDRESS)
                    .addParams("accessToken", token)
                    .addParams("userId", userId)
                    .addParams("provinceId", mProvince.getId())
                    .addParams("provinceName", mProvince.getName())
                    .addParams("cityId", mCity.getId())
                    .addParams("cityName", mCity.getName())
                    .addParams("areaId", mDistrict.getId())
                    .addParams("areaName", mDistrict.getName())
                    .addParams("detailAddress", editAddressDetail.getText().toString())
                    .addParams("userName", editName.getText().toString().trim())
                    .addParams("phone", editPhone.getText().toString().trim())
                    .addParams("isDefalt", isDefalt + "")
                    .build()
                    .execute(new Callback() {

                        @Override
                        public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                            return mData;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast("添加失败");
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            ToastUtils.showShortToast("添加成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
        } else {
            //修改地址
            OkHttpUtils.post()
                    .url(Urls.UPDATA_ADDRESS)
                    .addParams("accessToken", token)
                    .addParams("userId", userId)
                    .addParams("provinceId", mProvince.getId())
                    .addParams("provinceName", mProvince.getName())
                    .addParams("cityId", mCity.getId())
                    .addParams("cityName", mCity.getName())
                    .addParams("areaId", mDistrict.getId())
                    .addParams("areaName", mDistrict.getName())
                    .addParams("detailAddress", editAddressDetail.getText().toString())
                    .addParams("userName", editName.getText().toString().trim())
                    .addParams("phone", editPhone.getText().toString().trim())
                    .addParams("addressId", shopAddressListBean.getAddressId() + "")
                    .addParams("isDefalt", isDefalt + "")
                    .build()
                    .execute(new Callback() {

                        @Override
                        public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                            return mData;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast("修改失败");
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            ToastUtils.showShortToast("修改成功");
                            setResult(RESULT_OK);
                            finish();
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
                tvAddress.setText("");
                mProvince = province;
                mCity = city;
                mDistrict = district;
                tvAddress.setText(province.getName() + "\n"
                        + city.getName() + "\n"
                        + district.getName());
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }


    public boolean checkText(String string) {
        if (TextUtils.isEmpty(string)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initData() {

    }
}
