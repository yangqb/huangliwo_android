package com.feitianzhu.huangliwo.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BindingAliAccountModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/1/19
 * time: 14:26
 * email: 694125155@qq.com
 */
public class BindingAccountActivity extends BaseActivity {
    public static final String MINE_INFO = "mine_info";
    private MineInfoModel infoModel = new MineInfoModel();
    private BindingAliAccountModel bindingAliAccountModel;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_again_account)
    EditText editAgainAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_withdraw;
    }

    @Override
    protected void initView() {
        titleName.setText("绑定账号");
        infoModel = (MineInfoModel) getIntent().getSerializableExtra(MINE_INFO);
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
    }

    @Override
    protected void initData() {
        if (infoModel.getIsBind() == 1) {
            OkHttpUtils.get()
                    .url(Urls.GET_ALI_ACCOUNT)
                    .addParams(Constant.ACCESSTOKEN, token)
                    .addParams(Constant.USERID, userId)
                    .build()
                    .execute(new Callback<BindingAliAccountModel>() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(BindingAliAccountModel response, int id) {
                            bindingAliAccountModel = response;
                            editName.setText(response.getRealName());
                            editAccount.setText(response.getBankCardNo());
                            editAgainAccount.setText(response.getBankCardNo());
                        }
                    });
        }
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                submit();
                break;
        }
    }

    public void submit() {
        if (TextUtils.isEmpty(editName.getText().toString().trim())) {
            ToastUtils.showShortToast("请输入真实姓名");
            return;
        }
        if (!StringUtils.isPhone(editAccount.getText().toString().trim())) {
            if (!StringUtils.isEmail(editAccount.getText().toString().trim())) {
                ToastUtils.showShortToast("请输入正确的支付宝账号");
                return;
            }
        }
        if (!editAccount.getText().toString().trim().equals(editAgainAccount.getText().toString().trim())) {
            ToastUtils.showShortToast("两次输入的账号不一致");
            return;
        }
        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(Urls.BING_ALI_ACCOUNT);
        if (infoModel != null && infoModel.getIsBind() == 1) {
            postFormBuilder.addParams("id", bindingAliAccountModel.getBankCardId() + "");
        }
        postFormBuilder
                .addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .addParams("name", editName.getText().toString().trim())
                .addParams("account", editAgainAccount.getText().toString().trim())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (infoModel.getIsBind() == 1) {
                            ToastUtils.showShortToast("修改成功");
                        } else {
                            ToastUtils.showShortToast("绑定成功");
                        }
                        EventBus.getDefault().postSticky(LoginEvent.BINDING_ALI_ACCOUNT);
                        finish();
                    }
                });
    }
}
