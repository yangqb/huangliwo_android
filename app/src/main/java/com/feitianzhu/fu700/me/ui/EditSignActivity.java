package com.feitianzhu.fu700.me.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.EDIT_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.me.ui
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 4:58
 */
public class EditSignActivity extends BaseActivity {
    public static final String sign = "SIGN";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.edit_sign)
    EditText editText;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_edit_sign;
    }

    @Override
    protected void initView() {
        titleName.setText("修改个性签名");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        editText.setText(getIntent().getStringExtra(sign));
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtils.showShortToast("请输入新的个性签名");
                    return;
                }
                //提交信息操作
                sendSaveRequest();
                break;
        }
    }

    private void sendSaveRequest() {
        OkHttpUtils.post()//
                .url(Common_HEADER + EDIT_MINE_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .addParams("personSign", TextUtils.isEmpty(editText.getText().toString()) ? "" : editText.getText().toString())
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        /*
                         * 不知道这里修改成功为啥要返回错误，之前的人写的
                         * */
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        if ("数据为空".equals(e.getMessage())) {
                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(LoginEvent.EDITORINFO);
                            finish();
                        } else {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("wangyan", "onResponse---->" + response);
                    }
                });
    }

    @Override
    protected void initData() {

    }
}
