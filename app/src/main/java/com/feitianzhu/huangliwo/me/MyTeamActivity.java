package com.feitianzhu.huangliwo.me;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.feitianzhu.huangliwo.model.TeamModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    protected void initView() {
        titleName.setText("我的团队");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamAdapter = new TeamAdapter(teamModelList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        teamAdapter.setEmptyView(mEmptyView);
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MyTeamInfo>>get(Urls.GET_TEAM)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<MyTeamInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MyTeamInfo>> response) {
                        super.onSuccess(MyTeamActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            teamInfo = response.body().data;
                            Glide.with(MyTeamActivity.this).load(teamInfo.teamImg).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into(headImg);
                            teamCount.setText(teamInfo.teamNum + "人");
                            areaCount.setText(teamInfo.teamAgent + "人");
                            teamModelList = teamInfo.listUser;
                            teamAdapter.setNewData(teamModelList);
                            teamAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MyTeamInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    @OnClick({R.id.left_button, R.id.checkArea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.checkArea:
                MineInfoModel infoModel = UserInfoUtils.getUserInfo(this);
                if (infoModel.getAccountType() == 1 || infoModel.getAccountType() == 2) {
                    String content = "您所代理的区域为" + teamInfo.addrName + "\n会员总数" + teamInfo.memberNum + "人\n注册总数" + teamInfo.registerNum + "人";
                    new XPopup.Builder(MyTeamActivity.this)
                            .asConfirm("", content, "关闭", "确定", null, null, true)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                } else {
                    String content = "您还未是区域代理\n" + "申请成为区域代理可查看更多";
                    new XPopup.Builder(MyTeamActivity.this)
                            .asConfirm("", content, "关闭", "确定", null, null, true)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
                break;
        }

    }
}
