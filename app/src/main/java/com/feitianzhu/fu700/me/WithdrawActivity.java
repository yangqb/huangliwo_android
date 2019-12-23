package com.feitianzhu.fu700.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelp;
import com.feitianzhu.fu700.utils.EditTextUtils;
import com.feitianzhu.fu700.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.me
 * user: yangqinbo
 * date: 2019/12/23
 * time: 10:07
 * email: 694125155@qq.com
 * 余额提现
 */
public class WithdrawActivity extends BaseActivity {
    public static final String BALANCE = "balance";
    private double balance;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.edit_amount)
    EditText editAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw2;
    }

    @Override
    protected void initView() {
        titleName.setText("提现");
        balance = getIntent().getDoubleExtra(BALANCE, 0.00);
        EditTextUtils.afterDotTwo(editAmount);
    }

    @OnClick({R.id.submit, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.submit:
                VeriPassword(balance);
                break;
        }
    }

    private void VeriPassword(double balance) {
        double amount = Integer.valueOf(editAmount.getText().toString());
        if (amount > balance) {
            ToastUtils.showShortToast("当前余额不足");
            return;
        }
        ShopHelp.veriPassword(this, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                /*
                 * 提现
                 * */
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast(result);
            }
        });
    }

    @Override
    protected void initData() {
    }
}
