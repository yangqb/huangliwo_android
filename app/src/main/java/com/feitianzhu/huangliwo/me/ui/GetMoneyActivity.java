package com.feitianzhu.huangliwo.me.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.GetMoneyModel;
import com.hjq.toast.ToastUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.POST_SCORE_GET;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * Created by Vya on 2017/9/24.
 * 转入余额界面
 */

public class GetMoneyActivity extends BaseActivity {
    private GetMoneyModel mData;
    @BindView(R.id.et_inputMoney)
    EditText mEditText;
    @BindView(R.id.tv_TotalMoney)
    TextView mLeftName;
    @BindView(R.id.tv_formatTxt)
    TextView mFormatTxt;
    private DecimalFormat mFormatter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_money;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mFormatter = new DecimalFormat();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mData = (GetMoneyModel) intent.getSerializableExtra("getMoneyBean");
        mLeftName.setText(mData.points + "");
        //  double rate = 0;
    /*    try {
            Log.e("Test","Point==="+Double.valueOf(mData.points));
//            rate = mData.points * mData.rate / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        mFormatTxt.setText(String.format(getString(R.string.withdraw_tips), mFormatter.format(mData.points), mData.rate + "%"));
    }

    @OnClick(R.id.bt_sure)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    ToastUtils.show("请输入转出金额");
                    return;
                }
                sendParams();
                break;
        }
    }

    /**
     * 提交
     */
    private void sendParams() {
    }

}
