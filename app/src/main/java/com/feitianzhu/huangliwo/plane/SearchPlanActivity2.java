package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import com.feitianzhu.huangliwo.model.CustomFightCityInfo;
import com.feitianzhu.huangliwo.model.GoBackFlight;
import com.feitianzhu.huangliwo.model.GoBackFlightInfo;
import com.feitianzhu.huangliwo.model.GoBackFlightList;
import com.feitianzhu.huangliwo.model.GoBackTripInfo;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiGoBackFlightInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.model.SearchInternationalFlightModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchPlanActivity2 extends BaseActivity {
    public static final String SEARCH_TYPE = "search_type";
    private static final int DATE_REQUEST_CODE = 100;
    public static final String FLIGHT_INFO = "flight_info";
    private int sortType = 1;
    private List<MultiGoBackFlightInfo> goBackFlightList = new ArrayList<>();
    private SearchResultAdapter2 mAdapter;
    private CustomFightCityInfo customFightCityInfo;
    private int searchType = 2;
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
    @BindView(R.id.tvTimeTitle)
    TextView tvTimeTitle;
    @BindView(R.id.tvPriceTitle)
    TextView tvPriceTitle;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.back_city_title)
    TextView backCityTitle;
    @BindView(R.id.go_city_title)
    TextView goCityTitle;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

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
        customFightCityInfo = (CustomFightCityInfo) getIntent().getSerializableExtra(FLIGHT_INFO);
        startCity.setText(customFightCityInfo.depCityName);
        endCity.setText(customFightCityInfo.arrCityName);
        goCityTitle.setText(customFightCityInfo.depCityName + "-" + customFightCityInfo.arrCityName);
        backCityTitle.setText(customFightCityInfo.arrCityName + "-" + customFightCityInfo.depCityName);
        centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);
        depDate.setText(DateUtils.strToStr(customFightCityInfo.goDate) + DateUtils.strToDate2(customFightCityInfo.goDate));
        arrDate.setText(DateUtils.strToStr(customFightCityInfo.backDate) + DateUtils.strToDate2(customFightCityInfo.backDate));
        mAdapter = new SearchResultAdapter2(goBackFlightList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        TextView noData = mEmptyView.findViewById(R.id.no_data);
        noData.setText("当前搜索无航线");
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        refreshLayout.setEnableLoadMore(false);

        initListener();
    }

    public void initListener() {

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchPlanActivity2.this, PlaneDetailActivity.class);
                intent.putExtra(PlaneDetailActivity.DETAIL_TYPE, searchType);
                intent.putExtra(PlaneDetailActivity.FLIGHT_INFO, customFightCityInfo);
                intent.putExtra(PlaneDetailActivity.FLIGHT_DATA, goBackFlightList.get(position));
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
                Intent intent = new Intent(SearchPlanActivity2.this, VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
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
                    .params("depCity", customFightCityInfo.depAirPortCode)
                    .params("arrCity", customFightCityInfo.arrAirPortCode)
                    .params("goDate", customFightCityInfo.goDate)
                    .params("backDate", customFightCityInfo.backDate)
                    .params("exTrack", "retehui")
                    .params("sort", sortType) //排序：1为价格最低 2为时间最早
                    .execute(new JsonCallback<PlaneResponse<GoBackFlightInfo>>() {
                        @Override
                        public void onStart(Request<PlaneResponse<GoBackFlightInfo>, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(Response<PlaneResponse<GoBackFlightInfo>> response) {
                            super.onSuccess(SearchPlanActivity2.this, response.body().message, response.body().code);
                            goneloadDialog();
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().result != null) {
                                goBackFlightList.clear();
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
                            goneloadDialog();
                            refreshLayout.finishRefresh(false);
                        }
                    });

        } else {
            OkGo.<PlaneResponse<List<SearchInternationalFlightModel>>>get(Urls.SEARCH_INTERNATIONAL_FLIGHT)
                    .tag(this)
                    .params(Constant.ACCESSTOKEN, token)
                    .params(Constant.USERID, userId)
                    .params("depCity", "PAR")
                    .params("arrCity", "BER")
                    .params("depDate", customFightCityInfo.goDate)
                    .params("retDate", customFightCityInfo.backDate)//往返必填
                    .params("source", "ICP_SELECT_open.3724")
                    //.params("sort", "1") //排序：1为价格最低 2为时间最早
                    //.params("adultNum", "")成人数量
                    //.params("childNum", "")儿童数量
                    //.params("cabinLevel", "")舱位等级
                    .execute(new JsonCallback<PlaneResponse<List<SearchInternationalFlightModel>>>() {

                        @Override
                        public void onStart(Request<PlaneResponse<List<SearchInternationalFlightModel>>, ? extends Request> request) {
                            super.onStart(request);
                            showloadDialog("");
                        }

                        @Override
                        public void onSuccess(Response<PlaneResponse<List<SearchInternationalFlightModel>>> response) {
                            super.onSuccess(SearchPlanActivity2.this, response.body().message, response.body().code);
                            goneloadDialog();
                            refreshLayout.finishRefresh();
                            if (response.body().code == 0 && response.body().result != null) {
                                goBackFlightList.clear();
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
                            goneloadDialog();
                            refreshLayout.finishRefresh(false);
                        }
                    });
        }
    }

    @OnClick({R.id.left_button, R.id.depDate, R.id.arrDate, R.id.sortTime, R.id.sortPrice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.depDate:
            case R.id.arrDate:
                Intent intent = new Intent(SearchPlanActivity2.this, PlaneCalendarActivity.class);
                intent.putExtra(PlaneCalendarActivity.SELECT_MODEL, searchType);
                startActivityForResult(intent, DATE_REQUEST_CODE);
                break;
            case R.id.sortTime:
                tvTimeTitle.setTextColor(getResources().getColor(R.color.color_ff8300));
                line2.setBackgroundColor(getResources().getColor(R.color.color_ff8300));
                tvPriceTitle.setTextColor(getResources().getColor(R.color.color_333333));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                sortType = 2;
                initData();
                break;
            case R.id.sortPrice:
                tvTimeTitle.setTextColor(getResources().getColor(R.color.color_333333));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                tvPriceTitle.setTextColor(getResources().getColor(R.color.color_ff8300));
                line1.setBackgroundColor(getResources().getColor(R.color.color_ff8300));
                sortType = 1;
                initData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DATE_REQUEST_CODE) {
                customFightCityInfo.goDate = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE).split("=")[0];
                customFightCityInfo.backDate = data.getStringExtra(PlaneCalendarActivity.SELECT_DATE).split("=")[1];
                depDate.setText(DateUtils.strToStr(customFightCityInfo.goDate) + DateUtils.strToDate2(customFightCityInfo.goDate));
                arrDate.setText(DateUtils.strToStr(customFightCityInfo.backDate) + DateUtils.strToDate2(customFightCityInfo.backDate));
                initData();
            }
        }
    }
}
