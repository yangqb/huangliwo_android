package com.feitianzhu.fu700.vip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.MainActivity;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.dao.NetworkDao;
import com.feitianzhu.fu700.home.WebViewActivity;
import com.feitianzhu.fu700.login.LoginActivity;
import com.feitianzhu.fu700.me.WithdrawActivity;
import com.feitianzhu.fu700.me.adapter.CenterAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.ui.AuthEvent;
import com.feitianzhu.fu700.me.ui.VerificationActivity2;
import com.feitianzhu.fu700.me.ui.totalScore.SelectPayActivity;
import com.feitianzhu.fu700.model.LocationPost;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.MyPoint;
import com.feitianzhu.fu700.model.SelectPayNeedModel;
import com.feitianzhu.fu700.model.UnionLevelModel;
import com.feitianzhu.fu700.model.UserAuth;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelp;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.view.CustomInputView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.POST_UNION_LEVEL;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/22 0022 下午 6:53
 */
public class VipActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String MINE_INFO = "mine_info";
    private static final int REQUEST_CODE = 1000;
    private MineInfoModel mTempData = new MineInfoModel();
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.parent_view)
    LinearLayout parentView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.moreVip)
    TextView moreVip;
    @BindView(R.id.btn_submit)
    TextView btnSumbit;
    @BindView(R.id.cb_protocol)
    CheckBox mCheckBox;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();

        mTempData = (MineInfoModel) getIntent().getSerializableExtra(MINE_INFO);
        if (mTempData != null && mTempData.getAccountType() != 0) {
            btnSumbit.setText("恭喜您已成为会员");
            btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            btnSumbit.setEnabled(false);
            mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
            mCheckBox.setEnabled(false);
        }
        titleName.setText("成为会员");
        mCheckBox.setChecked(true);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));

    }

    boolean isMore = true;

    @OnClick({R.id.left_button, R.id.moreVip, R.id.btn_submit, R.id.tv_protocol})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.moreVip:
                if (isMore) {
                    moreVip.setText("收起会员权益");
                    parentView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } else {
                    moreVip.setText("更多会员权益");
                    parentView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                isMore = !isMore;
                break;
            case R.id.btn_submit:
                UserAuth mAuth = Constant.mUserAuth;
                if (null == mAuth || 0 == mAuth.isRnAuth) {
                    //未实名 审核被拒
                    showDialog("你还没有进行实名认证，请先进行实名认证再进行该操作", true);
                } else if (-1 == mAuth.isRnAuth) {
                    showDialog("审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证", true);
                } else if (mAuth.isRnAuth == 2) {
                    showDialog("你的实名认证正在审核中，请等审核通过后再进行该操作", false);
                } else {
                    //验证用户审核通过
                    intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                    intent.putExtra(VipUpgradeActivity.PARENT_ID, mTempData.getGradeId());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.tv_protocol:
                intent = new Intent(VipActivity.this, ProtocolActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void showDialog(String result, boolean isGoAuth) {
        new XPopup.Builder(VipActivity.this)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        if (isGoAuth) {
                            Intent mIntent = new Intent(VipActivity.this, VerificationActivity2.class);
                            startActivity(mIntent);
                        }
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAuthEvent(AuthEvent mAuth) {
        if (mAuth == AuthEvent.SUCCESS) {
            initData();
        }
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ShopDao.loadUserAuthImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                btnSumbit.setText("恭喜您已成为会员");
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                btnSumbit.setEnabled(false);
                mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
                mCheckBox.setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_protocol) {
            if (!isChecked) {
                btnSumbit.setEnabled(false);
                mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06weixuanzhong4));
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            } else {
                btnSumbit.setEnabled(true);
                mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
                btnSumbit.setBackgroundResource(R.drawable.shape_fed428_r5);
            }
        }
    }
}
