package com.feitianzhu.huangliwo.shop.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class EditApplyRefundActivity extends BaseActivity {
    public static final String ORDER_NO = "order_no";
    public static final String ORDER_AMOUNT = "order_amount";
    public static final String ORDER_TYPE = "order_type";
    private String orderNo;
    private double amount;
    private int type;
    private String[] strings;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private String str3;
    private String str2;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_refund;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("申请退款");
        rightText.setText("提交");
        rightText.setVisibility(View.VISIBLE);
        orderNo = getIntent().getStringExtra(ORDER_NO);
        amount = getIntent().getDoubleExtra(ORDER_AMOUNT, 0.00);
        type = getIntent().getIntExtra(ORDER_TYPE, 0);
        if (type == 0) {
            strings = new String[]{"我不想要了", "选错规格", "填错地址或信息", "其他"};
        } else {
            strings = new String[]{"我不想要了", "点错了", "支付遇到问题", "其他"};
        }
        str2 = "¥ ";
        str3 = String.format(Locale.getDefault(), "%.2f", amount);
        setSpannableString();
    }

    @OnClick({R.id.left_button, R.id.rl_reason, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_reason:
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditApplyRefundActivity.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvReason.setText(strings[position]);
                                    }
                                }))
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                if (TextUtils.isEmpty(tvReason.getText().toString())) {
                    ToastUtils.show("请选择退款原因");
                } else {
                    if (type == 0) {
                        refundGoodsOrder(orderNo, tvReason.getText().toString());
                    } else {
                        refundSetMealOrder(orderNo, tvReason.getText().toString());
                    }

                }
                break;
        }

    }

    public void refundGoodsOrder(String orderNo, String reason) {
        OkGo.<LzyResponse>post(Urls.REFUND_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .params("reason", reason)
                .params("status", "5")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditApplyRefundActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("申请成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public void refundSetMealOrder(String orderNo, String reason) {
        OkGo.<LzyResponse>post(Urls.CANCEL_SETMEAL_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .params("returnReason", reason)
                .params("status", "3")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditApplyRefundActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("退款成功");
                            setResult(RESULT_OK);
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

    @SuppressLint("SetTextI18n")
    public void setSpannableString() {
        tvPrice.setText("");
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span2.setSpan(new AbsoluteSizeSpan(13, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        tvPrice.append(span2);
        tvPrice.append(span3);

    }
}
