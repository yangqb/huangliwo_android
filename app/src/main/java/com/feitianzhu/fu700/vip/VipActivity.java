package com.feitianzhu.fu700.vip;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.MainActivity;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.dao.NetworkDao;
import com.feitianzhu.fu700.login.LoginActivity;
import com.feitianzhu.fu700.me.adapter.CenterAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.ui.totalScore.SelectPayActivity;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.model.SelectPayNeedModel;
import com.feitianzhu.fu700.model.UnionLevelModel;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.utils.ToastUtils;
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
public class VipActivity extends BaseActivity {
    private List<UnionLevelModel> mList = new ArrayList<>();
    private SelectPayNeedModel model;
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
    }

    boolean ismore = true;

    @OnClick({R.id.left_button, R.id.moreVip, R.id.btn_submit})
    public void onClick(View view) {
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
                if (model.gradeId != null && !TextUtils.isEmpty(model.gradeId)) {
                    Intent intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                    intent.putExtra(Constant.INTENT_SELECTET_PAY_MODEL, model);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("无会员级别数据");
                }
                break;
        }

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        OkHttpUtils.post()//会员等级数据
                .url(Common_HEADER + POST_UNION_LEVEL)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build().execute(new Callback<List<UnionLevelModel>>() {
            @Override
            public List<UnionLevelModel> parseNetworkResponse(String mData, Response response, int id)
                    throws Exception {
                Type type = new TypeToken<List<UnionLevelModel>>() {
                }.getType();
                List<UnionLevelModel> bean = new Gson().fromJson(mData, type);
                return bean;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("wangyan", "onError---->" + e.getMessage());
            }

            @Override
            public void onResponse(List<UnionLevelModel> response, int id) {
                //setShowData(response);
                mList.addAll(response);
                //现在只返回399的会员级别
                model = new SelectPayNeedModel();
                model.setType(SelectPayNeedModel.TYPE_UNION_LEVEL);
                if (mList.size() > 0) {
                    model.gradeId = mList.get(0).getGradeId() + "";
                    model.setHandleFee(mList.get(0).getPoints());
                    model.agentName = mList.get(0).getName();
                    model.agentType = mList.get(0).getAgentType();
                }
            }
        });

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
                requestData();
                break;
        }
    }

    private void requestData() {
        OkHttpUtils.post()//
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
                if (response == null) {
                    return;
                }
                if (response.getGradeId() >= 1) {
                    btnSumbit.setText("恭喜您已成为会员");
                    btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                    btnSumbit.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
