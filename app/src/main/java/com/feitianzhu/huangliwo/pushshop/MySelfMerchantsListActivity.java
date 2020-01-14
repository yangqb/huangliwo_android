package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/10
 * time: 18:28
 * email: 694125155@qq.com
 */
public class MySelfMerchantsListActivity extends BaseActivity {
    public static final String MERCHANTS_ID = "merchants_id";
    private MerchantsModel merchantsBean;
    private int merchantsId = -1;
    private String userId;
    private String token;
    @BindView(R.id.merchants_name)
    TextView merchantsName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_merchants_list;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        titleName.setText("我的商铺");
    }

    @OnClick({R.id.merchants_detail, R.id.upSetMeal, R.id.left_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.merchants_detail:
                intent = new Intent(MySelfMerchantsListActivity.this, MerchantsDetailActivity.class);
                intent.putExtra(MerchantsDetailActivity.IS_MY_MERCHANTS, true);
                intent.putExtra(MerchantsDetailActivity.MERCHANTS_DETAIL_DATA, merchantsBean);
                startActivity(intent);
                break;
            case R.id.upSetMeal:
                intent = new Intent(MySelfMerchantsListActivity.this, SetMealListActivity.class);
                intent.putExtra(SetMealListActivity.MERCHANTS_ID, merchantsId);
                startActivity(intent);
                break;
            case R.id.left_button:
                finish();
                break;
        }

    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS_DETAIL)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<MerchantsModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MerchantsModel response, int id) {
                        if (response != null) {
                            merchantsBean = response;
                            merchantsName.setText(merchantsBean.getMerchantName());
                            tvDate.setText("创建日期：" + merchantsBean.getCreateDate());
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdataMechantsEvent(UpdataMechantsEvent event) {
        if (event == UpdataMechantsEvent.SUCCESS) {
            initData();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
