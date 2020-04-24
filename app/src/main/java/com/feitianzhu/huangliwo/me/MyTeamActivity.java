package com.feitianzhu.huangliwo.me;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.TeamAdapter;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyTeamInfo;
import com.feitianzhu.huangliwo.model.TeamDetailInfo;
import com.feitianzhu.huangliwo.model.TeamModel;
import com.feitianzhu.huangliwo.plane.EditPassengerActivity;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/3/4
 * time: 19:26
 * email: 694125155@qq.com
 */
public class MyTeamActivity extends BaseActivity {
    private List<TeamModel> teamModelList = new ArrayList<>();
    private TeamDetailInfo teamDetailInfo;
    private int selectPos;
    private MyTeamInfo teamInfo;
    private String userId;
    private String token;
    private TeamAdapter teamAdapter;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.headImg)
    CircleImageView headImg;
    @BindView(R.id.teamCount)
    TextView teamCount;
    @BindView(R.id.areaCount)
    TextView areaCount;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.addCount)
    TextView addCount;
    @BindView(R.id.addAmount)
    TextView addAmount;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    protected void initView() {
        titleName.setText("我的团队");
        rightText.setText("全部");
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setBackgroundResource(R.mipmap.i01_01xiala);

        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        refreshLayout.setEnableLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamAdapter = new TeamAdapter(teamModelList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        TextView noData = mEmptyView.findViewById(R.id.no_data);
        noData.setText("空空如也");
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        teamAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(this);
        if (userInfo.getAccountType() == 0) {
            tvLevel.setText("消费者");
        } else if (userInfo.getAccountType() == 1) {
            tvLevel.setText("市代理");
        } else if (userInfo.getAccountType() == 2) {
            tvLevel.setText("区代理");
        } else if (userInfo.getAccountType() == 3) {
            tvLevel.setText("合伙人");
        } else if (userInfo.getAccountType() == 4) {
            tvLevel.setText("超级会员");
        } else if (userInfo.getAccountType() == 5) {
            tvLevel.setText("优选会员");
        } else if (userInfo.getAccountType() == 7) {
            tvLevel.setText("省代理");
        }

        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MyTeamInfo>>get(Urls.GET_TEAM)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<MyTeamInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<MyTeamInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MyTeamInfo>> response) {
                        super.onSuccess(MyTeamActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (response.body().code == 0 && response.body().data != null) {
                            teamInfo = response.body().data;
                            Glide.with(MyTeamActivity.this).load(teamInfo.teamImg).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into(headImg);
                            teamCount.setText(teamInfo.teamNum + "");
                            if (teamInfo.monthIncome == 0) {
                                areaCount.setText("0");
                            } else {
                                areaCount.setText(MathUtils.subZero(String.valueOf(teamInfo.monthIncome)));
                            }

                            setSpannableString(String.valueOf(teamInfo.yesdayAddCount), addCount);
                            if (teamInfo.yesdayIncome == 0) {
                                setSpannableString("0", addAmount);
                            } else {
                                setSpannableString(MathUtils.subZero(String.valueOf(teamInfo.yesdayIncome)), addAmount);
                            }
                            teamModelList = teamInfo.listUser;
                            teamDetailInfo = teamInfo.map;
                            if (selectPos == 0) {
                                teamAdapter.setNewData(teamModelList);
                            } else if (selectPos == 1) {
                                teamAdapter.setNewData(teamDetailInfo.svipList);
                            } else if (selectPos == 2) {
                                teamAdapter.setNewData(teamDetailInfo.vipList);
                            } else {
                                teamAdapter.setNewData(teamDetailInfo.consumeList);
                            }
                            teamAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MyTeamInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });

    }

    @OnClick({R.id.left_button, R.id.ll_team_count, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.ll_team_count:
               /* Intent intent = new Intent(MyTeamActivity.this, TeamDetailListActivity.class);
                intent.putExtra(TeamDetailListActivity.TEAM_DETAIL_INFO, teamDetailInfo);
                startActivity(intent);*/
                break;
            case R.id.right_button:
                String[] strings3 = new String[]{"全部", "超级会员", "优选会员", "消费者"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(MyTeamActivity.this)
                                .setData(Arrays.asList(strings3))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        rightText.setText(strings3[position]);
                                        selectPos = position;
                                        if (position == 0) {
                                            teamAdapter.setNewData(teamModelList);
                                        } else if (position == 1) {
                                            teamAdapter.setNewData(teamDetailInfo.svipList);
                                        } else if (position == 2) {
                                            teamAdapter.setNewData(teamDetailInfo.vipList);
                                        } else {
                                            teamAdapter.setNewData(teamDetailInfo.consumeList);
                                        }
                                        teamAdapter.notifyDataSetChanged();
                                    }
                                }))
                        .show();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "昨日+";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(12, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        span3.setSpan(new AbsoluteSizeSpan(12, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }

}
