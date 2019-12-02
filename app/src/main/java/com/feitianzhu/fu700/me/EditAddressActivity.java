package com.feitianzhu.fu700.me;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.SwitchButton;
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


    @OnClick({R.id.left_button, R.id.right_button, R.id.delete_address})
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
        }
    }

    @Override
    protected void initData() {

    }
}
