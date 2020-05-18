package com.feitianzhu.huangliwo.me;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.adapter.TeamAdapter;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.TeamDetailInfo;
import com.feitianzhu.huangliwo.model.TeamModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/4/23
 * time: 18:32
 * email: 694125155@qq.com
 */
public class TeamDetailListActivity extends BaseActivity {
    public static final String TEAM_DETAIL_INFO = "team_detail_info";
    private TeamDetailInfo teamDetailInfo;
    public List<TeamModel> vipList = new ArrayList<>();// (Array[用户表], optional): 线下人员 ,
    public List<TeamModel> svipList = new ArrayList<>();// (Array[用户表], optional): 线下人员 ,
    public List<TeamModel> consumeList = new ArrayList<>();// (Array[用户表], optional): 线下人员 ,
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_super)
    TextView btnSuper;
    @BindView(R.id.btn_optimization)
    TextView btnOptimization;
    @BindView(R.id.btn_ordinary)
    TextView btnOrdinary;
    @BindView(R.id.title_name)
    TextView titleName;
    private String token;
    private String userId;
    private int type = 1;
    private TeamAdapter teamAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_detail_list;
    }

    @Override
    protected void initView() {
        titleName.setText("团队详情");
        teamDetailInfo = (TeamDetailInfo) getIntent().getSerializableExtra(TEAM_DETAIL_INFO);
        if (teamDetailInfo != null) {
            vipList = teamDetailInfo.vipList;
            svipList = teamDetailInfo.svipList;
            consumeList = teamDetailInfo.consumeList;
        }
        btnSuper.setSelected(true);
        btnOptimization.setSelected(false);
        btnOrdinary.setSelected(false);
        teamAdapter = new TeamAdapter(svipList);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.btn_super, R.id.btn_optimization, R.id.btn_ordinary})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_super:
                type = 1;
                teamAdapter.setNewData(svipList);
                teamAdapter.notifyDataSetChanged();
                btnSuper.setSelected(true);
                btnOptimization.setSelected(false);
                btnOrdinary.setSelected(false);
                break;
            case R.id.btn_optimization:
                type = 2;
                teamAdapter.setNewData(vipList);
                teamAdapter.notifyDataSetChanged();
                btnSuper.setSelected(false);
                btnOptimization.setSelected(true);
                btnOrdinary.setSelected(false);
                break;
            case R.id.btn_ordinary:
                type = 3;
                teamAdapter.setNewData(consumeList);
                teamAdapter.notifyDataSetChanged();
                btnSuper.setSelected(false);
                btnOptimization.setSelected(false);
                btnOrdinary.setSelected(true);
                break;
        }
    }
}
