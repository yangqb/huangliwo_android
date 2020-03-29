package com.feitianzhu.huangliwo.me.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.EDIT_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.me.ui
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 2:33
 */
public class EditNickActivity extends BaseActivity {
    public static final String NICE_NAME = "nice_name";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.edit_nick)
    EditText editText;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_edit_nick;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("修改昵称");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        editText.setText(getIntent().getStringExtra(NICE_NAME));
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtils.showShortToast("请输入新的昵称");
                    return;
                }
                //提交信息操作
                sendSaveRequest();
                break;
        }
    }

    private void sendSaveRequest() {

        OkGo.<LzyResponse>post(Common_HEADER + EDIT_MINE_INFO)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("nickName", TextUtils.isEmpty(editText.getText().toString()) ? "" : editText.getText().toString())
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditNickActivity.this,response.body().msg,response.body().code);
                        if(response.body().code==0){
                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().postSticky(LoginEvent.EDITOR_INFO);
                            Intent intent = new Intent();
                            intent.putExtra(NICE_NAME, editText.getText().toString().trim());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void initData() {

    }
}
