package com.feitianzhu.fu700.shop.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.App;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.ui.ServiceDetailActivity;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {
    public static final String ORDER_DATA = "order_data";
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("订单详情");
        GoodsOrderInfo.GoodsOrderListBean goodsOrderBean = (GoodsOrderInfo.GoodsOrderListBean) getIntent().getSerializableExtra(ORDER_DATA);
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
        }
    }

    @OnClick({R.id.left_button, R.id.tv_copy, R.id.call_phone})
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
        }
    }


    @Override
    protected void initData() {

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
}
