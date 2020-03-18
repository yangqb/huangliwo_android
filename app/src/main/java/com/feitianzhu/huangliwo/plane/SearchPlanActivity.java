package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.FlightSegmentInfo;
import com.feitianzhu.huangliwo.model.MultipleGoSearchFightInfo;
import com.feitianzhu.huangliwo.model.SearchFlightModel;
import com.feitianzhu.huangliwo.model.SearchInternationalFlightModel;
import com.feitianzhu.huangliwo.model.TransitCityInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchPlanActivity extends BaseActivity {
    public static final String SEARCH_TYPE = "search_type";
    public static final String FLIGHT_DATE = "flight_date";
    private List<MultipleGoSearchFightInfo> goSearchFightInfoList = new ArrayList<>();
    private List<SearchFlightModel.FlightModel> flightInfos = new ArrayList<>();
    private List<SearchInternationalFlightModel> internationalFlightModels = new ArrayList<>();
    private SearchPlaneResultAdapter mAdapter;
    private String date;
    private String userId;
    private String token;
    private int searchType;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.plane_title)
    LinearLayout planeTitle;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.center_img)
    ImageView centerImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_plan;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        date = getIntent().getStringExtra(FLIGHT_DATE);
        searchType = getIntent().getIntExtra(SEARCH_TYPE, 0);
        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText("北京");
        endCity.setText("上海");
        centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
        rightImg.setBackgroundResource(R.mipmap.k01_10gengduo);
        rightText.setText("更多日期");
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        mAdapter = new SearchPlaneResultAdapter(goSearchFightInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);

        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchPlanActivity.this, PlaneDetailActivity.class);
                if (searchType == 0) {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, searchType);
                    intent.putExtra(PlaneDetailActivity.FLIGHT_START_DATE, date);
                    intent.putExtra(PlaneDetailActivity.FLIGHT_DATA, goSearchFightInfoList.get(position));
                } else if (searchType == 1) {
                    intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, searchType);
                    intent.putExtra(PlaneDetailActivity.FLIGHT_START_DATE, date);
                    intent.putExtra(PlaneDetailActivity.FLIGHT_DATA, goSearchFightInfoList.get(position));
                } else {

                }
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        if (searchType == 0) {
            OkGo.<PlaneResponse<SearchFlightModel>>get(Urls.SEARCH_FLIGHT)
                    .tag(this)
                    .params("dpt", "PEK")
                    .params("arr", "SHA")
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("date", date)
                    .params("ex_track", "tehui")
                    .execute(new JsonCallback<PlaneResponse<SearchFlightModel>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<SearchFlightModel>> response) {
                            super.onSuccess(SearchPlanActivity.this, response.body().message, response.body().code);
                            refreshLayout.finishRefresh();
                            SearchFlightModel searchFlightModel = response.body().result;
                            if (response.body().code == 0 && response.body().result.flightInfos != null) {
                                flightInfos = searchFlightModel.flightInfos;
                                goSearchFightInfoList.clear();
                                for (int i = 0; i < flightInfos.size(); i++) {
                                    MultipleGoSearchFightInfo goSearchFightInfo = new MultipleGoSearchFightInfo(MultipleGoSearchFightInfo.DOMESTIC_TYPE);
                                    goSearchFightInfo.flightModel = flightInfos.get(i);
                                    goSearchFightInfoList.add(goSearchFightInfo);
                                }
                                mAdapter.setNewData(goSearchFightInfoList);
                                mAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onError(Response<PlaneResponse<SearchFlightModel>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                        }
                    });
        } else if (searchType == 1) {
            OkGo.<PlaneResponse<List<SearchInternationalFlightModel>>>get(Urls.SEARCH_INTERNATIONAL_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", "PEK")
                    .params("arrCity", "WAS")
                    .params("depDate", date)
                    //.params("retDate", "")往返必填
                    .params("source", "ICP_SELECT_open.3724")
                    //.params("adultNum", "")成人数量
                    //.params("childNum", "")儿童数量
                    //.params("cabinLevel", "")舱位等级
                    .execute(new JsonCallback<PlaneResponse<List<SearchInternationalFlightModel>>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<List<SearchInternationalFlightModel>>> response) {
                            super.onSuccess(SearchPlanActivity.this, response.body().message, response.body().code);
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().result != null) {
                                goSearchFightInfoList.clear();
                                internationalFlightModels = response.body().result;
                                for (int i = 0; i < internationalFlightModels.size(); i++) {
                                    MultipleGoSearchFightInfo goSearchFightInfo = new MultipleGoSearchFightInfo(MultipleGoSearchFightInfo.INTERNATIONAL_TYPE);
                                    goSearchFightInfo.internationalFlightModel = internationalFlightModels.get(i);
                                    goSearchFightInfoList.add(goSearchFightInfo);
                                }
                                mAdapter.setNewData(goSearchFightInfoList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<List<SearchInternationalFlightModel>>> response) {
                            super.onError(response);
                            refreshLayout.finishRefresh(false);
                        }
                    });
        } else {

        }
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                break;
        }
    }
}