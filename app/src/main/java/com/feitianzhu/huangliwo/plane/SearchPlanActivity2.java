package com.feitianzhu.huangliwo.plane;

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
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.GoBackFlight;
import com.feitianzhu.huangliwo.model.GoBackFlightInfo;
import com.feitianzhu.huangliwo.model.GoBackFlightList;
import com.feitianzhu.huangliwo.model.GoBackTripInfo;
import com.feitianzhu.huangliwo.model.MultiGoBackFlightInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.model.SearchInternationalFlightModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchPlanActivity2 extends BaseActivity {
    public static final String SEARCH_TYPE = "search_type";
    public static final String FLIGHT_START_DATE = "flight_start_date";
    public static final String FLIGHT_END_DATE = "flight_end_date";
    private List<MultiGoBackFlightInfo> goBackFlightList = new ArrayList<>();
    private SearchResultAdapter2 mAdapter;
    private int searchType = 2;
    private String goDate;
    private String backDate;
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.plane_title)
    LinearLayout planeTitle;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.center_img)
    ImageView centerImg;
    @BindView(R.id.depDate)
    TextView depDate;
    @BindView(R.id.arrDate)
    TextView arrDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_plane2;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        searchType = getIntent().getIntExtra(SEARCH_TYPE, 2);
        goDate = getIntent().getStringExtra(FLIGHT_START_DATE);
        backDate = getIntent().getStringExtra(FLIGHT_END_DATE);
        startCity.setText("北京");
        endCity.setText("上海");
        centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);
        depDate.setText(DateUtils.strToStr(goDate) + DateUtils.strToDate2(goDate));
        arrDate.setText(DateUtils.strToStr(backDate) + DateUtils.strToDate2(backDate));
        mAdapter = new SearchResultAdapter2(goBackFlightList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchPlanActivity2.this, PlaneDetailActivity.class);
                intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, searchType);
                intent.putExtra(PlaneDetailActivity.FLIGHT_START_DATE, goDate);
                intent.putExtra(PlaneDetailActivity.FLIGHT_END_DATE, backDate);
                intent.putExtra(PlaneDetailActivity.FLIGHT_DATA, goBackFlightList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        if (searchType == 2) {
            OkGo.<PlaneResponse<GoBackFlightInfo>>get(Urls.SEARCH_GO_BACK_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", "PEK")
                    .params("arrCity", "SHA")
                    .params("goDate", goDate)
                    .params("backDate", backDate)
                    .params("exTrack", "retehui")
                    .params("sort", "1") //排序：1为价格最低 2为时间最早
                    .execute(new JsonCallback<PlaneResponse<GoBackFlightInfo>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<GoBackFlightInfo>> response) {
                            super.onSuccess(SearchPlanActivity2.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                List<GoBackFlightList> flightList = response.body().result.flightList;
                                if (flightList != null && flightList.size() > 0) {
                                    for (int i = 0; i < flightList.size(); i++) {
                                        MultiGoBackFlightInfo multiGoFlightInfo = new MultiGoBackFlightInfo(MultiGoBackFlightInfo.DOMESTIC);
                                        multiGoFlightInfo.domesticFlight = flightList.get(i);
                                        goBackFlightList.add(multiGoFlightInfo);
                                    }
                                    mAdapter.setNewData(goBackFlightList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<GoBackFlightInfo>> response) {
                            super.onError(response);
                        }
                    });

        } else {
            OkGo.<PlaneResponse<List<SearchInternationalFlightModel>>>get(Urls.SEARCH_INTERNATIONAL_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", "PAR")
                    .params("arrCity", "BER")
                    .params("depDate", goDate)
                    .params("retDate", backDate)//往返必填
                    .params("source", "ICP_SELECT_open.3724")
                    //.params("sort", "1") //排序：1为价格最低 2为时间最早
                    //.params("adultNum", "")成人数量
                    //.params("childNum", "")儿童数量
                    //.params("cabinLevel", "")舱位等级
                    .execute(new JsonCallback<PlaneResponse<List<SearchInternationalFlightModel>>>() {
                        @Override
                        public void onSuccess(Response<PlaneResponse<List<SearchInternationalFlightModel>>> response) {
                            super.onSuccess(SearchPlanActivity2.this, response.body().message, response.body().code);
                            if (response.body().code == 0 && response.body().result != null) {
                                List<SearchInternationalFlightModel> internationalFlightModelList = response.body().result;
                                if (internationalFlightModelList.size() > 0) {
                                    for (int i = 0; i < internationalFlightModelList.size(); i++) {
                                        MultiGoBackFlightInfo multiGoFlightInfo = new MultiGoBackFlightInfo(MultiGoBackFlightInfo.INTERNATIONAL);
                                        multiGoFlightInfo.internationalFlight = internationalFlightModelList.get(i);
                                        goBackFlightList.add(multiGoFlightInfo);
                                    }
                                    mAdapter.setNewData(goBackFlightList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onError(Response<PlaneResponse<List<SearchInternationalFlightModel>>> response) {
                            super.onError(response);

                        }
                    });
        }
    }

    @OnClick({R.id.left_button, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_next:

                break;
        }
    }
}
