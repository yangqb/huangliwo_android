package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.UpdataMechantsEvent;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.merchantsLogo)
    CircleImageView merchantsLogo;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

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

    @OnClick({R.id.merchants_detail, R.id.upSetMeal, R.id.left_button, R.id.upGift})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.merchants_detail:
                intent = new Intent(MySelfMerchantsListActivity.this, MerchantsDetailActivity.class);
                intent.putExtra(MerchantsDetailActivity.MERCHANTS_DETAIL_DATA, merchantsBean);
                startActivity(intent);
                break;
            case R.id.upSetMeal:
                intent = new Intent(MySelfMerchantsListActivity.this, SetMealListActivity.class);
                intent.putExtra(SetMealListActivity.MERCHANTS_ID, merchantsId);
                startActivity(intent);
                break;
            case R.id.upGift:
                intent = new Intent(MySelfMerchantsListActivity.this, UpMerchantsGiftActivity.class);
                intent.putExtra(UpMerchantsGiftActivity.MERCHANTS_ID, merchantsId);
                startActivity(intent);
                break;
            case R.id.left_button:
                finish();
                break;
        }

    }

    @Override
    protected void initData() {

        OkGo.<LzyResponse<MerchantsModel>>get(Urls.GET_MERCHANTS_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<MerchantsModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MerchantsModel>> response) {
                        super.onSuccess(MySelfMerchantsListActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null && response.body().code == 0) {
                            merchantsBean = response.body().data;
                            merchantsName.setText(merchantsBean.getMerchantName());
                            tvDate.setText("创建日期：" + merchantsBean.getCreateDate());
                            Glide.with(mContext).load(merchantsBean.getLogo()).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into(merchantsLogo);
                            if (merchantsBean.getExamineModel() != null) {
                                if (merchantsBean.getExamineModel().getBlStatus() == -1 || merchantsBean.getExamineModel().getCardStatus() == -1 || merchantsBean.getExamineModel().getDcStatus() == -1) {
                                    tvStatus.setText("审核拒绝");
                                } else if (merchantsBean.getExamineModel().getBlStatus() == 0 || merchantsBean.getExamineModel().getCardStatus() == 0 || merchantsBean.getExamineModel().getDcStatus() == 0) {
                                    tvStatus.setText("审核中");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MerchantsModel>> response) {
                        super.onError(response);
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
