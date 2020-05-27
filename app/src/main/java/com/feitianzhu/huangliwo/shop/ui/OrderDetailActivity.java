package com.feitianzhu.huangliwo.shop.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiLifeCallBack;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.LogisticsModel;
import com.feitianzhu.huangliwo.shop.SelectPayActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.shop.adapter.CommentImgAdapter;
import com.feitianzhu.huangliwo.shop.adapter.RefundImgAdapter;
import com.feitianzhu.huangliwo.shop.request.ExpressInfoRequest;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class OrderDetailActivity extends BaseActivity {
    public static final String ORDER_NO = "order_no";
    private static final int PAY_REQUEST_CODE = 1000;
    private LogisticsModel logisticsModel;
    private RefundImgAdapter refundImgAdapter;
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
    @BindView(R.id.ll_recipient_info)
    LinearLayout llRecipientInfo;
    @BindView(R.id.rl_logistics_info)
    RelativeLayout rlLogisticsInfo;
    @BindView(R.id.ll_refund_img)
    LinearLayout llRefundImg;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.supplierName)
    TextView tvSupplierName;
    @BindView(R.id.supplierPhone)
    TextView tvSupplierPhone;
    @BindView(R.id.supplierAddress)
    TextView tvSupplierAddress;
    @BindView(R.id.edit_express_name)
    EditText editExpressName;
    @BindView(R.id.edit_express_no)
    EditText editExpressNo;
    @BindView(R.id.logistics_name)
    TextView logisticsName;
    @BindView(R.id.itemInfo)
    TextView itemInfo;
    @BindView(R.id.time)
    TextView logisticsTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.return_reason)
    TextView returnReason;
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
        rightText.setText("提交");
        orderNo = getIntent().getStringExtra(ORDER_NO);
    }

    @OnClick({R.id.left_button, R.id.tv_copy, R.id.call_phone, R.id.cancel_order, R.id.shopPay, R.id.ll_order_detail, R.id.tvStatusContent, R.id.right_button, R.id.rl_logistics_info})
    @SingleClick()
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
            case R.id.tvStatusContent:
                if (goodsOrderBean != null && goodsOrderBean.getRefuseReason() != null && !TextUtils.isEmpty(goodsOrderBean.getRefuseReason())) {
                    String content = goodsOrderBean.getRefuseReason();
                    new XPopup.Builder(this)
                            .asConfirm("", content, "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                }
                            }, new OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            }, true)
                            .bindLayout(R.layout.layout_dialog_login)
                            .show();//绑定已有布局
                }


                break;

            case R.id.right_button:
                submit();
                break;
            case R.id.rl_logistics_info:
                if (logisticsModel != null) {
                    intent = new Intent(OrderDetailActivity.this, LogisticsInfoActivity.class);
                    intent.putExtra(LogisticsInfoActivity.LOGISTICS_COMPANY, goodsOrderBean.getRefundExpressCom());
                    intent.putExtra(LogisticsInfoActivity.LOGISTICS_DATA, logisticsModel);
                    startActivity(intent);
                }
                break;


        }
    }

    public void submit() {
        String expressNum = editExpressNo.getText().toString().trim();
        String expressName = editExpressName.getText().toString().trim();
        if (TextUtils.isEmpty(expressName)) {
            ToastUtils.show("请填写快递公司名称");
            return;
        }
        if (TextUtils.isEmpty(expressNum)) {
            ToastUtils.show("请填写快递单号");
            return;
        }
        ExpressInfoRequest request = new ExpressInfoRequest();
        request.userId = userId;
        request.token = token;
        request.expressName = expressName;
        request.expressNum = expressNum;
        request.orderNo = orderNo;
        request.call(new ApiLifeCallBack<Boolean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinsh() {

            }

            @Override
            public void onAPIResponse(Boolean response) {
                ToastUtils.show("提交成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                ToastUtils.show(errorMsg);
            }
        });
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
                            if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_WAIT_MERCHANT_RECEIVING || goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_WAIT_MERCHANT_REFUND || goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_COMPLETED_REFUND_GOODS) {
                                if (goodsOrderBean.getRefundExpressNum() != null && !TextUtils.isEmpty(goodsOrderBean.getRefundExpressNum())) {
                                    getLogisticsInfo(goodsOrderBean.getRefundExpressNum(), goodsOrderBean.getRefundExpressCom());
                                }
                            }
                            showView();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<GoodsOrderInfo.GoodsOrderListBean>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getLogisticsInfo(String expressNo, String expressCom) {
        OkGo.<LzyResponse<String>>get(Urls.GET_LOGISTICS_INFO)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("expressNo", expressNo)
                .execute(new JsonCallback<LzyResponse<String>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<String>> response) {
                        if (response.body().code == 0 && response.body().data != null && !TextUtils.isEmpty(response.body().data)) {
                            String jsonStr = response.body().data;
                            logisticsModel = new Gson().fromJson(jsonStr, LogisticsModel.class);
                            if (logisticsModel.getData() != null && logisticsModel.getData().size() > 0) {
                                logisticsName.setText("退货物流：" + expressCom + "(" + expressNo + ")");
                                itemInfo.setText(logisticsModel.getData().get(0).getContext());
                                logisticsTime.setText(logisticsModel.getData().get(0).getFtime());
                            } else {
                                itemInfo.setText("暂无物流信息");
                            }
                        } else {
                            itemInfo.setText("暂无物流信息");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<String>> response) {
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
            rightText.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.GONE);
            tvStatus.setText("等待付款");
            countDownTimer();
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_REFUND) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.GONE);
            tvStatus.setText("退款中");
            tvStatusContent.setText("等待商家处理");
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_REFUNDED) {
            llStatus.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.GONE);
            tvStatus.setText("退款成功");
            tvStatusContent.setText("");
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_REFUNDING_GOODS) {
            llStatus.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.GONE);
            tvStatus.setText("退货中");
            tvStatusContent.setText("等待商家处理");
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_AGREE_REFUND_GOODS) {
            llStatus.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.VISIBLE);
            tvStatus.setText("退货中");
            llRecipientInfo.setVisibility(View.VISIBLE);
            showSupplierInfo();
            showRefundImg();
            tvStatusContent.setText("商家已同意，请尽快将商品发回");
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_WAIT_MERCHANT_RECEIVING) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.VISIBLE);
            llRefundImg.setVisibility(View.VISIBLE);
            tvStatus.setText("退货中");
            tvStatusContent.setText("等待商家收货");
            showRefundImg();
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_WAIT_MERCHANT_REFUND) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.VISIBLE);
            llRefundImg.setVisibility(View.VISIBLE);
            tvStatus.setText("退货中");
            tvStatusContent.setText("等待商家退款");
            showRefundImg();
        } else if (goodsOrderBean.getStatus() == GoodsOrderInfo.TYPE_COMPLETED_REFUND_GOODS) {
            llStatus.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.VISIBLE);
            llRecipientInfo.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.VISIBLE);
            tvStatus.setText("退货完成");
            showRefundImg();
            tvStatusContent.setText("");
        } else {
            llStatus.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
            rlLogisticsInfo.setVisibility(View.GONE);
            llRecipientInfo.setVisibility(View.GONE);
            rightText.setVisibility(View.GONE);
            llRefundImg.setVisibility(View.GONE);
        }
        if (goodsOrderBean.getRefuseReason() != null && !TextUtils.isEmpty(goodsOrderBean.getRefuseReason())) {
            tvStatusContent.setText(goodsOrderBean.getRefuseReason());
        }

        if (goodsOrderBean.getReturnReason() != null && !TextUtils.isEmpty(goodsOrderBean.getReturnReason())) {
            returnReason.setText(goodsOrderBean.getRefuseReason());
        }
    }


    public void showSupplierInfo() {
        tvSupplierName.setText(goodsOrderBean.getSupplierName());
        tvSupplierPhone.setText(goodsOrderBean.getSupplierPhone());
        tvSupplierAddress.setText(goodsOrderBean.getSupplierAddress());
    }

    public void showRefundImg() {
        returnReason.setText(goodsOrderBean.getReturnReason());
        List<String> imgs = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setNestedScrollingEnabled(false);
        if (goodsOrderBean.getRefundImg() != null && !TextUtils.isEmpty(goodsOrderBean.getRefundImg())) {
            String[] strings = goodsOrderBean.getRefundImg().split(",");
            imgs = Arrays.asList(strings);
        }
        refundImgAdapter = new RefundImgAdapter(imgs);
        recyclerView.setAdapter(refundImgAdapter);
        refundImgAdapter.notifyDataSetChanged();
    }

    private void requestPermission() {
        XXPermissions.with(OrderDetailActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CALL_PHONE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + goodsOrderBean.getConnectPhone()));
                            startActivity(intent);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAY_REQUEST_CODE) {
                finish();
            }
        }
    }

    private CountDownTimer countDownTimer;

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!OrderDetailActivity.this.isFinishing()) {
                    time = millisUntilFinished / 1000;
                    String formatLongToTimeStr = DateUtils.formatTime(millisUntilFinished);
                    tvStatusContent.setText("剩" + formatLongToTimeStr + "自动取消订单");
                }
            }

            /**
             *倒计时结束后调用的
             */
            @Override
            public void onFinish() {
                ToastUtils.show("订单已取消");
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
