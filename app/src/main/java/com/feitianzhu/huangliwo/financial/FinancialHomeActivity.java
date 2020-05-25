package com.feitianzhu.huangliwo.financial;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.financial.bean.MultiFinancialInfo;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.financial
 * user: yangqinbo
 * date: 2020/3/30
 * time: 13:59
 * email: 694125155@qq.com
 */
public class FinancialHomeActivity extends BaseActivity {
    private int clsId;
    private String token;
    private String userId;
    private List<MerchantsClassifyModel.ListBean> listBean;
    private List<MultiFinancialInfo> multiFinancialInfoList = new ArrayList<>();
    private FinancialHomeAdapter financialHomeAdapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ll_all_financial)
    LinearLayout llAllFinancial;
    @BindView(R.id.ll_my_financial)
    LinearLayout llMyFinancial;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_financial_home;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("金融");
        /*for (int i = 0; i < 10; i++) {
            MultiFinancialInfo multiFinancialInfo = new MultiFinancialInfo(MultiFinancialInfo.All_FINANCIAL);
            multiFinancialInfoList.add(multiFinancialInfo);
        }*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        financialHomeAdapter = new FinancialHomeAdapter(multiFinancialInfoList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        financialHomeAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(financialHomeAdapter);
        financialHomeAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);

        initListener();
    }

    public void initListener() {
        financialHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FinancialHomeActivity.this, FinancialDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MerchantsClassifyModel>>get(Urls.GET_MERCHANTS_TYPE)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MerchantsClassifyModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        //super.onSuccess(VipActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            listBean = response.body().data.getList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        super.onError(response);
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.ll_all_financial, R.id.ll_my_financial})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.ll_all_financial:
                llAllFinancial.setBackgroundResource(R.color.bg_yellow);
                llMyFinancial.setBackgroundResource(R.color.white);
                List<String> strings = new ArrayList<>();
                if (listBean != null && listBean.size() > 0) {
                    for (int i = 0; i < listBean.size(); i++) {
                        strings.add(listBean.get(i).getClsName());
                    }
                    strings.add(0, "全部");
                    new XPopup.Builder(this)
                            .asCustom(new CustomRefundView(FinancialHomeActivity.this)
                                    .setData(strings)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            if (position == 0) {
                                                clsId = 0;
                                                tvTitle.setText("全部");
                                            } else {
                                                clsId = listBean.get(position - 1).getClsId();
                                                tvTitle.setText(listBean.get(position - 1).getClsName());
                                            }
                                            multiFinancialInfoList.clear();
                                            /*for (int i = 0; i < 10; i++) {
                                                MultiFinancialInfo multiFinancialInfo = new MultiFinancialInfo(MultiFinancialInfo.All_FINANCIAL);
                                                multiFinancialInfoList.add(multiFinancialInfo);
                                            }*/
                                            financialHomeAdapter.setNewData(multiFinancialInfoList);
                                            financialHomeAdapter.notifyDataSetChanged();
                                            //getVipGif(clsId);
                                        }
                                    }))
                            .show();
                }
                break;

            case R.id.ll_my_financial:
                llAllFinancial.setBackgroundResource(R.color.white);
                llMyFinancial.setBackgroundResource(R.color.bg_yellow);
                multiFinancialInfoList.clear();
               /* for (int i = 0; i < 10; i++) {
                    MultiFinancialInfo multiFinancialInfo = new MultiFinancialInfo(MultiFinancialInfo.MY_FINANCIAL);
                    multiFinancialInfoList.add(multiFinancialInfo);
                }*/
                financialHomeAdapter.setNewData(multiFinancialInfoList);
                financialHomeAdapter.notifyDataSetChanged();
                break;

        }

    }
}
