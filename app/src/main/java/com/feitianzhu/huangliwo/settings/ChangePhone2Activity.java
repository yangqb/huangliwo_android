package com.feitianzhu.huangliwo.settings;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.dao.NetworkDao;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhone2Activity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.edt_code)
    EditText mEdtCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.rl_code)
    RelativeLayout mRlCode;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.title_name)
    TextView titleName;
    private String mOldPhone;
    private String mOldSmsCode;


    private CountDownTimer mTimer = new CountDownTimer(6000 * 10, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTvCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mRlCode.setEnabled(true);
            mTvCode.setText(R.string.resend);
        }
    };

    @Override
    protected void initTitle() {
        titleName.setText("更换手机号码");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone2;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            mOldPhone = intent.getStringExtra(Constant.INTENT_OLD_PHONE);
            mOldSmsCode = intent.getStringExtra(Constant.INTENT_OLD_SMSCODE);
        }

    }

    @OnClick({R.id.button, R.id.rl_code, R.id.left_button})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:

                final String newPhone = mEtPhone.getText().toString();
                String newSmsCode = mEdtCode.getText().toString();

                if (TextUtils.isEmpty(newPhone)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_phone));
                    return;
                }

                if (TextUtils.isEmpty(newSmsCode)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_code));
                    return;
                }

                NetworkDao.updatePhone(this, mOldPhone, mOldSmsCode, newPhone, newSmsCode, new onConnectionFinishLinstener() {
                    @Override
                    public void onSuccess(int code, Object result) {
                        ToastUtils.showShortToast("修改成功");
                        Constant.PHONE = newPhone;
                        SPUtils.putString(ChangePhone2Activity.this, Constant.SP_PHONE, newPhone);
                        KLog.i("new Constant.PHONE : " + Constant.PHONE);
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFail(int code, String result) {

                        ToastUtils.showShortToast(result);
                    }
                });
                break;
            case R.id.rl_code:
                String phone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast(getString(R.string.please_input_phone));
                    return;
                }

                mRlCode.setEnabled(false);
                mTimer.start();

                getSmsCode(phone);
                break;
            case R.id.left_button:
                finish();
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getSmsCode(String phone) {

        NetworkDao.getSmsCode(this, phone, "3", new onConnectionFinishLinstener() {

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


    public static void startActivity(Context context, String oldPhone, String oldSmsCode) {
        Intent intent = new Intent(context, ChangePhone2Activity.class);
        intent.putExtra(Constant.INTENT_OLD_PHONE, oldPhone);
        intent.putExtra(Constant.INTENT_OLD_SMSCODE, oldSmsCode);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
