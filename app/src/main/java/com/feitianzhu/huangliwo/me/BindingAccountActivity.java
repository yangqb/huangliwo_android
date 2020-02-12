package com.feitianzhu.huangliwo.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.BindingAliAccountModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
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
            OkGo.<LzyResponse<BindingAliAccountModel>>get(Urls.GET_ALI_ACCOUNT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .execute(new JsonCallback<LzyResponse<BindingAliAccountModel>>() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<BindingAliAccountModel>> response) {
                            super.onSuccess(BindingAccountActivity.this, response.body().msg, response.body().code);
                            if (response.body().code == 0 && response.body().data != null) {
                                bindingAliAccountModel = response.body().data;
                                editName.setText(bindingAliAccountModel.getRealName());
                                editAccount.setText(bindingAliAccountModel.getBankCardNo());
                                editAgainAccount.setText(bindingAliAccountModel.getBankCardNo());
                            }
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<LzyResponse<BindingAliAccountModel>> response) {
                            super.onError(response);
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

        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.BING_ALI_ACCOUNT)
                .tag(this);
        if (infoModel != null && infoModel.getIsBind() == 1) {
            postRequest.params("id", bindingAliAccountModel.getBankCardId() + "");
        }
        postRequest
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("name", editName.getText().toString().trim())
                .params("account", editAgainAccount.getText().toString().trim())
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(BindingAccountActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            if (infoModel.getIsBind() == 1) {
                                ToastUtils.showShortToast("修改成功");
                            } else {
                                ToastUtils.showShortToast("绑定成功");
                            }
                            EventBus.getDefault().postSticky(LoginEvent.BINDING_ALI_ACCOUNT);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }
}
