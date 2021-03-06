package com.feitianzhu.huangliwo.me.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.LazyBaseActivity;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ShopHelp;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * dicallc
 * 钱包充值界面
 */
public class WalletPayActivity extends LazyBaseActivity {

  @BindView(R.id.edt_money) EditText mEdtMoney;
  @BindView(R.id.iv_check1) ImageView mIvCheck1;
  @BindView(R.id.ly_one) RelativeLayout mLyOne;
  @BindView(R.id.iv_check2) ImageView mIvCheck2;
  @BindView(R.id.ly_two) RelativeLayout mLyTwo;
  @BindView(R.id.rl_bottomContainer) Button mRlBottomContainer;


  @Override
  protected int getChildLayoutId() {
    return R.layout.activity_wallet_pay;
  }

  @Override protected void initView() {
    EventBus.getDefault().register(this);
    setTitleName("充值");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }


  @Override protected void initData() {

  }

  //重置所有文本的选中状态
  public void selected() {
    mIvCheck1.setSelected(false);
    mIvCheck2.setSelected(false);
  }

  @OnClick({ R.id.ly_one, R.id.ly_two, R.id.rl_bottomContainer })
  public void onViewClicked(View view) {

    switch (view.getId()) {
      case R.id.ly_one:
        selected();
        mIvCheck1.setSelected(true);
        break;
      case R.id.ly_two:
        selected();
        mIvCheck2.setSelected(true);
        break;
      case R.id.rl_bottomContainer:
        String money = mEdtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
          ToastUtils.show("金额还没有填写");
          return;
        }
        if (mIvCheck1.isSelected()) {
          //微信支付
          ShopHelp.WxPay(money, sfContext);
        } else {
          aliPay(money);
        }

        break;
    }
  }

  private void aliPay(String mMoney) {
    ShopDao.postAliPayPay(mMoney, new onConnectionFinishLinstener() {
      @Override public void onSuccess(int code, Object result) {
        String str = result.toString();
        PayUtils.aliPay(WalletPayActivity.this, str, new onConnectionFinishLinstener() {
          @Override public void onSuccess(int code, Object result) {
            ToastUtils.show("充值成功");
            finish();
          }

          @Override public void onFail(int code, String result) {
            ToastUtils.show("充值失败");
          }
        });
      }

      @Override public void onFail(int code, String result) {
        ToastUtils.show(result);
      }
    });
  }

  @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
  public void onPayMessageCall(PayInfo msg) {
    switch (msg.getCurrentInfo()) {
      case PayInfo.MyMoneyPay:
        finish();
        break;
    }
  }
}
