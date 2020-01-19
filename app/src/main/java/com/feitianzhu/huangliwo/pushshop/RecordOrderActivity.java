package com.feitianzhu.huangliwo.pushshop;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
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
    private String mealCode;
    private String merchantsId;
    private String userId;
    private String token;
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
        mealCode = getIntent().getStringExtra(SET_MEAL_CODE);
        merchantsId = getIntent().getStringExtra(MERCHANTS_ID);
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
        OkHttpUtils.post()
                .url(Urls.RECORD_ORDER)
                .addParams("num", mealCode)
                .addParams("merchantId", merchantsId)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
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
                });
    }

    @Override
    protected void initData() {

    }
}
