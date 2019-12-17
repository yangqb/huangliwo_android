package com.feitianzhu.fu700.shop.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.App;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.model.PayInfo;
import com.feitianzhu.fu700.model.WXModel;
import com.feitianzhu.fu700.utils.DateUtils;
import com.feitianzhu.fu700.utils.PayUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {
    private GoodsOrderInfo.GoodsOrderListBean goodsOrderBean;
    public static final String ORDER_DATA = "order_data";
    public static final String ORDER_NO = "order_no";
    private long time = 60;
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
    @BindView(R.id.summary)
    TextView summary;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("订单详情");
        goodsOrderBean = (GoodsOrderInfo.GoodsOrderListBean) getIntent().getSerializableExtra(ORDER_DATA);
        if (goodsOrderBean != null) {
            tvOrderNo.setText(goodsOrderBean.getOrderNo());
            createTime.setText(goodsOrderBean.getCreateDate());
            goodsName.setText(goodsOrderBean.getGoodsName());
            summary.setText(goodsOrderBean.getSummary());
            count.setText("×" + goodsOrderBean.getGoodsQTY());
            tvCount.setText("共" + goodsOrderBean.getGoodsQTY() + "件商品");
            address.setText(goodsOrderBean.getShopAddress().getDetailAddress());
            userName.setText(goodsOrderBean.getShopAddress().getUserName());
            tvPhone.setText(goodsOrderBean.getShopAddress().getPhone());
            tvPrice.setText(String.format(Locale.getDefault(), "%.2f", goodsOrderBean.getPrice()));
            merchantsName.setText(goodsOrderBean.getShopName());

            if (1 == GoodsOrderInfo.TYPE_NO_PAY) {
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
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String formatLongToTimeStr = DateUtils.formatTime(time*1000);
                tvStatusContent.setText(formatLongToTimeStr);
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


    @OnClick({R.id.left_button, R.id.tv_copy, R.id.call_phone, R.id.cancel_order, R.id.shopPay})
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
                ToastUtils.showShortToast("已复制");
                break;
            case R.id.call_phone:
                new XPopup.Builder(this)
                        .asConfirm("拨打商家电话", "18866668888", "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                requestPermission();
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.cancel_order: //取消订单
                break;
            case R.id.shopPay: //支付
                if ("wx".equals(goodsOrderBean.getChannel())) {
                    //aliPay();
                } else if ("alipay".equals(goodsOrderBean.getChannel())) {
                    //wexinPay();
                }
                break;
        }
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    private void wexinPay(WXModel result) {
        Constant.PayFlag = PayInfo.ShopPay;
        IWXAPI api = WXAPIFactory.createWXAPI(OrderDetailActivity.this, result.appid);
        api.registerApp(result.appid);
        PayReq mPayReq = new PayReq();
        Constant.WX_APP_ID = result.appid + "";
        mPayReq.appId = result.appid;
        mPayReq.partnerId = result.partnerid;
        mPayReq.prepayId = result.prepayid;
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = result.noncestr;
        mPayReq.timeStamp = result.timestamp + "";
        mPayReq.sign = result.sign;
        api.sendReq(mPayReq);
        ToastUtils.showShortToast("正在打开微信中");
    }

    private void aliPay(String orderInfo, String orderNo) {

        PayUtils.aliPay(OrderDetailActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.ShopPay) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.showShortToast("支付成功");
                finish();
            } else {
                ToastUtils.showShortToast("支付失败");
            }
        }
    }

    String telNum = "18866668888";

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
                intent.setData(Uri.parse("tel:" + telNum));
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
