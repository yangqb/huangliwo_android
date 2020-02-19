package com.feitianzhu.huangliwo.pushshop;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.uuzuche.lib_zxing.view.ViewfinderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/3
 * time: 18:29
 * email: 694125155@qq.com
 */
public class RecordOrderActivity extends BaseActivity {
    public static final String SET_MEAL_CODE = "set_meal_code";
    public static final String MERCHANTS_ID = "merchants_id";
    public static final String TYPE = "type";
    public static final String URL_CODE = "url_code";
    private String mealCode;
    private String merchantsId;
    private String userId;
    private String token;
    private String result;
    private String type;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.editCode)
    EditText editCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_order;
    }

    @Override
    protected void initView() {
        titleName.setText("商家录单");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        type = getIntent().getStringExtra(TYPE);
        result = getIntent().getStringExtra(URL_CODE);
        if ("1".equals(type)) {
            String[] strings = result.split("-");
            mealCode = strings[0];
            merchantsId = strings[1];
        } else {
            String[] aa = result.split("\\?");
            String[] bb = aa[1].split("\\&");
            merchantsId = bb[0].split("=")[1];
            mealCode = bb[2].split("=")[1];
        }

        if (mealCode != null) {
            editCode.setText(mealCode);
        }
    }

    @OnClick({R.id.left_button, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.confirm:
                recordOrder();
                break;
        }
    }

    public void recordOrder() {
        if (TextUtils.isEmpty(editCode.getText().toString())) {
            ToastUtils.showShortToast("请输入套餐码");
            return;
        }

        OkGo.<LzyResponse>post(Urls.RECORD_ORDER)
                .tag(this)
                .params("num", mealCode)
                .params("type", type)
                .params("merchantId", merchantsId)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(RecordOrderActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            new XPopup.Builder(RecordOrderActivity.this)
                                    .asConfirm("录单成功！是否继续录单？", "", "取消", "确定", new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            editCode.setHint("请输入套餐码");
                                        }
                                    }, new OnCancelListener() {
                                        @Override
                                        public void onCancel() {
                                            finish();
                                        }
                                    }, false)
                                    .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                    .show();
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
