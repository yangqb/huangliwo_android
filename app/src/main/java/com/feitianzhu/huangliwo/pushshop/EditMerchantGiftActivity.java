package com.feitianzhu.huangliwo.pushshop;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.MerchantGitModel;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/*
 * 修改和新增赠品
 * */

public class EditMerchantGiftActivity extends BaseActivity {
    public static final String MERCHANTS_ID = "merchants_id";
    public static final String GIFT_DATA = "gift_data";
    private int merchantsId;
    private boolean isAdd = true;
    private String token;
    private String userId;
    private MerchantGitModel merchantGitModel;
    private String giftId;

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.editRemark)
    EditText editRemark;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editPrice)
    EditText editPrice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_gift;
    }

    @Override
    protected void initView() {
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("新增赠品");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        EditTextUtils.afterDotTwo(editPrice);
        merchantGitModel = (MerchantGitModel) getIntent().getSerializableExtra(GIFT_DATA);
        if (merchantGitModel != null) {
            isAdd = false;
            merchantsId = merchantGitModel.merchantId;
            giftId = merchantGitModel.id;
            editName.setText(merchantGitModel.giftName);
            editPrice.setText(String.valueOf(merchantGitModel.price));
            if (merchantGitModel.desc != null && !TextUtils.isEmpty(merchantGitModel.desc)) {
                editRemark.setText(merchantGitModel.desc);
            }
        }
    }

    @Override
    protected void initData() {

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
        String name = editName.getText().toString().trim();
        String price = editPrice.getText().toString().trim();
        String remark = editRemark.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show("请输入赠品名称");
            return;
        }

        if (TextUtils.isEmpty(price)) {
            ToastUtils.show("请输入赠品价格");
            return;
        }

        MerchantGitModel model = new MerchantGitModel();
        model.desc = remark;
        model.giftName = name;
        model.merchantId = merchantsId;
        model.price = Double.valueOf(price);
        if (!isAdd) {
            model.id = giftId;
        }
        String json = new Gson().toJson(model);

        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.ADD_GIFT).tag(this);
        postRequest.params(ACCESSTOKEN, token)
                .params(USERID, userId);
        postRequest.params("giftBody", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(EditMerchantGiftActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            if (isAdd) {
                                ToastUtils.show("添加成功");
                            } else {
                                ToastUtils.show("修改成功");
                            }
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }
}
