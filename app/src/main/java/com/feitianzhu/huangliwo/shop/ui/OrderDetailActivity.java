package com.feitianzhu.huangliwo.shop.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.shop.SelectPayActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class OrderDetailActivity extends BaseActivity {
    public static final String ORDER_NO = "order_no";
    private static final int PAY_REQUEST_CODE = 1000;
    private long time;
    private GoodsOrderInfo.GoodsOrderListBean goodsOrderBean;
    private String orderNo;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.orderNo)
    TextView tvOrderNo;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.phone)
    TextView tvPhone;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.specifications)
    TextView specifications;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.merchantsName)
    TextView merchantsName;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvStatusContent)
    TextView tvStatusContent;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.image)
    RoundedImageView imageView;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.remark)
    TextView remark;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("订单详情");
        orderNo = getIntent().getStringExtra(ORDER_NO);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String formatLongToTimeStr = DateUtils.formatTime(time * 1000);
                tvStatusContent.setText("剩" + formatLongToTimeStr + "自动取消订单");
            }
            super.handleMessage(msg);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                handler.sendEmptyMessage(0);
                handler.postDelayed(this, 1000);
            }
            time--;
        }
    };

    @OnClick({R.id.left_button, R.id.tv_copy, R.id.call_phone, R.id.cancel_order, R.id.shopPay, R.id.ll_order_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_copy:
                //获取剪贴版
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                //第一个参数只是一个标记，随便传入。
                //第二个参数是要复制到剪贴版的内容
                ClipData clip = ClipData.newPlainText("simple text", tvOrderNo.getText().toString());
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                ToastUtils.show("已复制");
                break;
            case R.id.call_phone:
                if (goodsOrderBean != null) {
                    new XPopup.Builder(this)
                            .asConfirm("拨打商家电话", goodsOrderBean.getConnectPhone(), "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    requestPermission();
                                }
                            }, null, false)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
                break;
            case R.id.cancel_order: //取消订单
                if (time <= 0) {
                    ToastUtils.show("订单已失效");
                } else {
                    //取消订单，
                    new XPopup.Builder(OrderDetailActivity.this)
                            .asConfirm("确定要取消该订单？", "", "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    cancel(goodsOrderBean.getOrderNo());
                                }
                            }, null, false)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
                break;
            case R.id.shopPay: //支付
                if (time <= 0) {
                    ToastUtils.show("订单已失效");
                } else {
                    Intent intent = new Intent(OrderDetailActivity.this, SelectPayActivity.class);
                    intent.putExtra(SelectPayActivity.ORDER_DATA, goodsOrderBean);
                    startActivityForResult(intent, PAY_REQUEST_CODE);
                }
                break;
            case R.id.ll_order_detail:
                //商品详情
                Intent intent = new Intent(OrderDetailActivity.this, ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, goodsOrderBean.getGoodId());
                startActivity(intent);
                break;
        }
    }


    public void cancel(String orderNo) {

        OkGo.<LzyResponse>get(Urls.CANCEL_ORDER)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(OrderDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("取消成功");
                            initData();
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
        OkGo.<LzyResponse<GoodsOrderInfo.GoodsOrderListBean>>get(Urls.GET_ORDER_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<LzyResponse<GoodsOrderInfo.GoodsOrderListBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo.GoodsOrderListBean>> response) {
                        super.onSuccess(OrderDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            goodsOrderBean = response.body().data;
                            time = (goodsOrderBean.getExpiresDate() - goodsOrderBean.getNowTimeStamp()) / 1000;
                            showView();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo.GoodsOrderListBean>> response) {
                        super.onError(response);
                    }
                });
    }

    public void showView() {
        tvOrderNo.setText(goodsOrderBean.getOrderNo());
        createTime.setText(goodsOrderBean.getCreateDate());
        merchantsName.setText(goodsOrderBean.getShopName());
        goodsName.setText(goodsOrderBean.getGoodName());
        specifications.setText(goodsOrderBean.getAttributeVal());
        count.setText("×" + goodsOrderBean.getCount());
        tvCount.setText("共" + goodsOrderBean.getCount() + "件商品");
        remark.setText(goodsOrderBean.getRemark());
        if (goodsOrderBean.getDetailAddress() != null && !TextUtils.isEmpty(goodsOrderBean.getDetailAddress())) {
            rlAddress.setVisibility(View.VISIBLE);
            address.setText(goodsOrderBean.getDetailAddress());
        } else {
            rlAddress.setVisibility(View.GONE);
        }
        userName.setText(goodsOrderBean.getBuyerName());
        tvPhone.setText(goodsOrderBean.getBuyerPhone());
        tvPrice.setText(String.format(Locale.getDefault(), "%.2f", goodsOrderBean.getPrice()));
        merchantsName.setText(goodsOrderBean.getShopName());
        Glide.with(mContext).load(goodsOrderBean.getGoodsImg())
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView);

        if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_NO_PAY) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            tvStatus.setText("等待付款");
            handler.post(runnable);
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_REFUND) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            tvStatus.setText("退款中");
            tvStatusContent.setText("等待商家处理");
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            tvStatus.setText("退款成功");
            tvStatusContent.setText("");
        } else {
            llStatus.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
        }
    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .callback(
                        new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                                AndPermission.rationaleDialog(App.getAppContext(), rationale).show();
                            }
                        }
                )
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + goodsOrderBean.getConnectPhone()));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(OrderDetailActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAY_REQUEST_CODE) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
