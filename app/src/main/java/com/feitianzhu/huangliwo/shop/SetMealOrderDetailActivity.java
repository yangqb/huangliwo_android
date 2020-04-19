package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.SetMealOrderDetailInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.shop
 * user: yangqinbo
 * date: 2020/1/15
 * time: 18:23
 * email: 694125155@qq.com
 * <p>
 * 套餐支付成功详情
 */
public class SetMealOrderDetailActivity extends BaseActivity {
    private Bitmap bitmap;
    public static final String ORDER_NO = "order_no";
    private SetMealOrderDetailInfo orderDetailInfo;
    private String orderNo;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.name)
    TextView setMealName;
    @BindView(R.id.summary)
    TextView tvSummary;
    @BindView(R.id.tvSetMealCode)
    TextView tvSetMealCode;
    @BindView(R.id.orderNo)
    TextView tvOrderNo;
    @BindView(R.id.buyDate)
    TextView buyDate;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.ll_QRCode)
    LinearLayout llQRCode;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvStatusContent)
    TextView tvStatusContent;
    @BindView(R.id.btn_bottom)
    TextView btnBottom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setmeal_order_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("订单详情");
        orderNo = getIntent().getStringExtra(ORDER_NO);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<SetMealOrderDetailInfo>>get(Urls.SETMEAL_ORDER_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse<SetMealOrderDetailInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<SetMealOrderDetailInfo>> response) {
                        super.onSuccess(SetMealOrderDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            orderDetailInfo = response.body().data;
                            showView(orderDetailInfo);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<SetMealOrderDetailInfo>> response) {
                        super.onError(response);
                    }
                });
    }

    public void showView(SetMealOrderDetailInfo orderDetailInfo) {
        if (orderDetailInfo.getStatus() != 1 && orderDetailInfo.getStatus() != 5) {
            createQrcode(orderDetailInfo.getNumMid());
            llQRCode.setVisibility(View.VISIBLE);
        }
        if (orderDetailInfo.getStatus() == 3) {
            llStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("退款中");
            tvStatusContent.setText("等待商家处理");
        } else if (orderDetailInfo.getStatus() == 4) {
            llStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("退款成功");
            tvStatusContent.setText("");
        } else if (orderDetailInfo.getStatus() == 1) {
            llStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("等待付款");
        }
        if (orderDetailInfo.getStatus() == 1) {
            btnBottom.setText("付款");
            btnBottom.setVisibility(View.VISIBLE);
        }

        setMealName.setText(orderDetailInfo.getSmName());
        tvSummary.setText(orderDetailInfo.getRemark());
        tvSetMealCode.setText(orderDetailInfo.getNum());
        tvOrderNo.setText("订单号：" + orderDetailInfo.getOrderNo());
        buyDate.setText("购买时间：" + orderDetailInfo.getCreateTime());
        tvPrice.setText("套餐价格：¥" + String.format(Locale.getDefault(), "%.2f", orderDetailInfo.getAmount()));
        if (orderDetailInfo.getSmImg().contains(",")) {
            String[] imgUrls = orderDetailInfo.getSmImg().split(",");
            Glide.with(SetMealOrderDetailActivity.this).load(imgUrls[0]).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(imageView);
        } else {
            Glide.with(SetMealOrderDetailActivity.this).load(orderDetailInfo.getSmImg()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(imageView);
        }
    }

    public void createQrcode(String setMealCode) {
        if (TextUtils.isEmpty(setMealCode)) {
            ToastUtils.show("未获取到套餐码");
            return;
        }
        Log.e("Test", "-------->" + setMealCode);
        //bitmap = CodeUtils.createImage(qrUrl, 200, 200, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        bitmap = CodeUtils.createImage(setMealCode, 340, 340, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(ivQRCode);
        }
    }

    @OnClick({R.id.left_button, R.id.btn_bottom})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_bottom:
                if (orderDetailInfo.getStatus() == 1) {
                    //支付
                    SetMealInfo setMealInfo = new SetMealInfo();
                    setMealInfo.setImgs(orderDetailInfo.getSmImg());
                    setMealInfo.setSmName(orderDetailInfo.getSmName());
                    setMealInfo.setRemark(orderDetailInfo.getRemark());
                    setMealInfo.setPrice(orderDetailInfo.getAmount());
                    setMealInfo.setSmId(orderDetailInfo.getSmId());
                    setMealInfo.setMerchantId(orderDetailInfo.getMerchantId());
                    setMealInfo.setUserId(orderDetailInfo.getUserId());
                    intent = new Intent(SetMealOrderDetailActivity.this, SetMealPayActivity.class);
                    intent.putExtra(SetMealPayActivity.ORDER_NO, orderDetailInfo.getOrderNo());
                    intent.putExtra(SetMealPayActivity.SETMEAL_ORDERI_NFO, setMealInfo);
                    startActivity(intent);
                }
                break;
        }
    }
}
