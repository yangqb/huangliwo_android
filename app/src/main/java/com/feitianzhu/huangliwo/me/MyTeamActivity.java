package com.feitianzhu.huangliwo.me;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.TeamAdapter;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyTeamInfo;
import com.feitianzhu.huangliwo.model.TeamDetailInfo;
import com.feitianzhu.huangliwo.model.TeamModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
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
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.ll_team)
    LinearLayout llTeam;
    @BindView(R.id.ll_super)
    LinearLayout llSuper;
    @BindView(R.id.ll_opt)
    LinearLayout llOpt;
    @BindView(R.id.ll_consumer)
    LinearLayout llConsumer;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.superCount)
    TextView superCount;
    @BindView(R.id.optCount)
    TextView optCount;
    @BindView(R.id.consumerCount)
    TextView consumerCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_team2;
    }

    @Override
    protected void initView() {
        titleName.setText("我的团队");

        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        llTeam.setSelected(true);
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
        teamAdapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(this);
        tvName.setText(userInfo.getNickName());
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
                            teamModelList = teamInfo.listUser;
                            teamDetailInfo = teamInfo.map;
                            superCount.setText(teamDetailInfo.svipList.size() + "");
                            optCount.setText(teamDetailInfo.vipList.size() + "");
                            consumerCount.setText(teamDetailInfo.consumeList.size() + "");
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
                        teamAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Response<LzyResponse<MyTeamInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });

    }

    @OnClick({R.id.left_button, R.id.ll_team, R.id.ll_super, R.id.ll_opt, R.id.ll_consumer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.ll_team:
                selectPos = 0;
                llTeam.setSelected(true);
                llSuper.setSelected(false);
                llOpt.setSelected(false);
                llConsumer.setSelected(false);
                teamAdapter.setNewData(teamModelList);
                teamAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_super:
                selectPos = 1;
                llTeam.setSelected(false);
                llSuper.setSelected(true);
                llOpt.setSelected(false);
                llConsumer.setSelected(false);
                teamAdapter.setNewData(teamDetailInfo.svipList);
                teamAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_opt:
                selectPos = 2;
                llTeam.setSelected(false);
                llSuper.setSelected(false);
                llOpt.setSelected(true);
                llConsumer.setSelected(false);
                teamAdapter.setNewData(teamDetailInfo.vipList);
                teamAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_consumer:
                selectPos = 3;
                llTeam.setSelected(false);
                llSuper.setSelected(false);
                llOpt.setSelected(false);
                llConsumer.setSelected(true);
                teamAdapter.setNewData(teamDetailInfo.consumeList);
                teamAdapter.notifyDataSetChanged();
                break;
        }
    }
}
