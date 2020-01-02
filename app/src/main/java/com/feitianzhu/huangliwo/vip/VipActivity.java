package com.feitianzhu.huangliwo.vip;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.PresentsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    private List<PresentsModel.ShopGiftListBean> shopGiftList = new ArrayList<>();
    private VipPresentsAdapter adapter;
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_presents)
    LinearLayout llPresents;
    private String token;
    private String userId;

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
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mTempData = (MineInfoModel) getIntent().getSerializableExtra(MINE_INFO);
        if (mTempData != null && mTempData.getAccountType() != 0) {
            btnSumbit.setText("恭喜您已成为会员");
            btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            btnSumbit.setEnabled(false);
            mCheckBox.setEnabled(false);
        }
        titleName.setText("成为会员");
        mCheckBox.setChecked(true);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
        adapter = new VipPresentsAdapter(shopGiftList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
               /* UserAuth mAuth = Constant.mUserAuth;
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
                    intent.putExtra(VipUpgradeActivity.PARENT_ID, mTempData.getParentId());
                    startActivityForResult(intent, REQUEST_CODE);
                }*/
                intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                intent.putExtra(VipUpgradeActivity.PARENT_ID, mTempData.getParentId());
                startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_VIP_PRESENT)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, PresentsModel.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        PresentsModel presentsModel = (PresentsModel) response;
                        if (presentsModel.getShopGiftList() == null || presentsModel.getShopGiftList().size() <= 0) {
                            llPresents.setVisibility(View.GONE);
                        } else {
                            shopGiftList = presentsModel.getShopGiftList();
                            adapter.setNewData(shopGiftList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                btnSumbit.setText("恭喜您已成为会员");
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                btnSumbit.setEnabled(false);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
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
                mCheckBox.setBackgroundResource(R.mipmap.f01_06weixuanzhong4);
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            } else {
                btnSumbit.setEnabled(true);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
                btnSumbit.setBackgroundResource(R.drawable.shape_fed428_r5);
            }
        }
    }
}
