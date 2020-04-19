package com.feitianzhu.huangliwo.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.AddressInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.SwitchButton;
import com.hjq.toast.ToastUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import butterknife.BindView;
import butterknife.OnClick;

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
                    ToastUtils.show("请填写收货人姓名");
                    return;
                }
                if (checkText(editPhone.getText().toString().trim())) {
                    ToastUtils.show("请填写正确的收货人电话");
                    return;
                } else {
                    if (!StringUtils.isPhone(editPhone.getText().toString().trim())) {
                        ToastUtils.show("请填写正确的收货人电话");
                        return;
                    }
                }
                if (checkText(tvAddress.getText().toString().trim())) {
                    ToastUtils.show("请选择收货人地址");
                    return;
                }
                if (checkText(editAddressDetail.getText().toString().trim())) {
                    ToastUtils.show("请填写收货人详细地址");
                    return;
                }
                submit();
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
        OkGo.<LzyResponse>post(Urls.DELETE_ADDRESS)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("addressId", addressId + "")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditAddressActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            setResult(RESULT_OK);
                            ToastUtils.show("删除成功");
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void submit() {
        if (switchButton.isChecked()) {
            isDefalt = 1;
        } else {
            isDefalt = 0;
        }
        String url;
        //新增地址
        if ("710000".equals(mProvince.getId()) || "810000".equals(mProvince.getId()) || "820000".equals(mProvince.getId())) {
            ToastUtils.show("暂不支持港澳台地区配送");
            return;
        }
        if (isAdd) {
            url = Urls.ADD_ADDRESS;
        } else {
            url = Urls.UPDATA_ADDRESS;
        }
        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(url).tag(this);
        if (!isAdd) {
            postRequest.params("addressId", shopAddressListBean.getAddressId() + "");
        }
        postRequest.params("accessToken", token)
                .params("userId", userId)
                .params("provinceId", mProvince.getId())
                .params("provinceName", mProvince.getName())
                .params("cityId", mCity.getId())
                .params("cityName", mCity.getName())
                .params("areaId", mDistrict.getId())
                .params("areaName", mDistrict.getName())
                .params("detailAddress", editAddressDetail.getText().toString())
                .params("userName", editName.getText().toString().trim())
                .params("phone", editPhone.getText().toString().trim())
                .params("isDefalt", isDefalt + "")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditAddressActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            if (isAdd) {
                                ToastUtils.show("添加成功");
                            } else {
                                ToastUtils.show("修改成功");
                            }
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
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
