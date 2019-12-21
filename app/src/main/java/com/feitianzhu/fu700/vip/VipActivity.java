package com.feitianzhu.fu700.vip;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.MainActivity;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.dao.NetworkDao;
import com.feitianzhu.fu700.home.WebViewActivity;
import com.feitianzhu.fu700.login.LoginActivity;
import com.feitianzhu.fu700.me.adapter.CenterAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.ui.totalScore.SelectPayActivity;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.SelectPayNeedModel;
import com.feitianzhu.fu700.model.UnionLevelModel;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
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

        titleName.setText("成为会员");
        requestData();
        mCheckBox.setChecked(true);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
    }

    boolean ismore = true;

    @OnClick({R.id.left_button, R.id.moreVip, R.id.btn_submit, R.id.tv_protocol})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.moreVip:
                if (ismore) {
                    moreVip.setText("收起会员权益");
                    parentView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } else {
                    moreVip.setText("更多会员权益");
                    parentView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                ismore = !ismore;
                break;
            case R.id.btn_submit:
                intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_protocol:
                intent = new Intent(VipActivity.this, ProtocolActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(PayForMeEvent event) {
        switch (event) {
            case PAY_FINISH:
                btnSumbit.setText("恭喜您已成为会员");
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                btnSumbit.setEnabled(false);
                mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
                mCheckBox.setEnabled(false);
                requestData();
                break;
        }
    }

    private void requestData() {
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build().execute(new Callback<MineInfoModel>() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("wangyan", "onError---->" + e.getMessage());
                ToastUtils.showShortToast(e.getMessage());
            }

            @Override
            public void onResponse(MineInfoModel response, int id) {
                if (response != null && response.getAccountType() != 0) {
                    btnSumbit.setText("恭喜您已成为会员");
                    btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                    btnSumbit.setEnabled(false);
                    mCheckBox.setButtonDrawable(getResources().getDrawable(R.mipmap.f01_06xuanzhong5));
                    mCheckBox.setEnabled(false);
                }
            }
        });
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
