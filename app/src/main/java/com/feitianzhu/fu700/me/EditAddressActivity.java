package com.feitianzhu.fu700.me;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.SwitchButton;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import butterknife.BindView;
import butterknife.OnClick;

public class EditAddressActivity extends BaseActivity {

    public static final String IS_ADD_ADDRESS = "is_add_address";

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void initView() {
        boolean isAdd = getIntent().getBooleanExtra(IS_ADD_ADDRESS, false);
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


    @OnClick({R.id.left_button, R.id.right_button, R.id.delete_address, R.id.add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button: //保存地址

                break;
            case R.id.delete_address:
                new XPopup.Builder(this)
                        .asConfirm("确定要删除该地址？", "", "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                ToastUtils.showShortToast("删除成功");
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.add_address:
                setSelectAddress();
                break;
        }
    }

    public void setSelectAddress() {
        JDCityPicker cityPicker = new JDCityPicker();
        JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();

        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                tvAddress.setText(province.getName() + "(" + province.getId() + ")\n"
                        + city.getName() + "(" + city.getId() + ")\n"
                        + district.getName() + "(" + district.getId() + ")");
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    @Override
    protected void initData() {

    }
}
