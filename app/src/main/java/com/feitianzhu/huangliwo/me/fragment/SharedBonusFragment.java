package com.feitianzhu.huangliwo.me.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.LazyFragment;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.me.adapter.ReleaseBonusAdapter;
import com.feitianzhu.huangliwo.me.ui.GetMoneyActivity;
import com.feitianzhu.huangliwo.me.ui.totalScore.ReleaseTotalScoreActivity;
import com.feitianzhu.huangliwo.model.GetMoneyModel;
import com.feitianzhu.huangliwo.model.TotalScoreModel;
import com.feitianzhu.huangliwo.shop.ShopHelp;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/9/4 0004.
 */

public class SharedBonusFragment extends LazyFragment {
    @BindView(R.id.recyclerview_shared)
    RecyclerView mSharedRecycler;
    @BindView(R.id.tv_name_num)
    TextView mNameNum;

    private List<TotalScoreModel.RebateRecordsBean.RebateItemBean> mList;
    private ReleaseBonusAdapter mAdapter;
    @BindView(R.id.textView13)
    TextView textView13;

    private View mEmptyView;

    private TotalScoreModel response;
    private int currentType = 7;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shared_bonus, null);
        ButterKnife.bind(this, v);
        return v;
    }
    protected MaterialDialog mDialog;
    protected void showloadDialog(String title) {
        mDialog = new MaterialDialog.Builder(getActivity()).title(title)
                .content("加载中,请稍候...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    protected void goneloadDialog() {
        if (null != mDialog && mDialog.isShowing()){
            if (mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }

    @Override
    protected void initData() {
        mEmptyView = View.inflate(getActivity(), R.layout.view_common_nodata, null);
        mList = new ArrayList<>();
        mAdapter = new ReleaseBonusAdapter(mList);
        mSharedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mSharedRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(mEmptyView);
        Bundle arguments = getArguments();
        response = (TotalScoreModel) arguments.getSerializable("model");
        if(response!=null){
            setShowData();
        }

    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    private void setShowData() {
        if(response == null || response.getRebateRecords()==null || response.getRebateRecords().getDistrPointsRecord()==null){
            ToastUtils.show("获取服务数据失败!");
            return;
        }
        String DistrPoints = "";
        String DistrPointsNum = "";
        if (response != null && response.getUserPoints() != null) {
            DistrPoints = response.getUserPoints().getDistrPoints() + "";
            DistrPointsNum = response.getUserPoints().getDistrPointsBalance()+"";
            if (TextUtils.isEmpty(DistrPoints)) {
                DistrPoints = "0";
            }
            if (TextUtils.isEmpty(DistrPointsNum)) {
                DistrPointsNum = "0";
            }
        } else {
            DistrPoints = "0";
            DistrPointsNum = "0";
        }
        mNameNum.setText(DistrPointsNum);
        textView13.setText(DistrPoints);

        mList.addAll(response.getRebateRecords().getDistrPointsRecord());
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rl_bottom_container,R.id.rl_release_totaldetail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bottom_container:
                getMoneyFun();
                break;
            case R.id.rl_release_totaldetail: //释放积分详情页
                Intent intent = new Intent(getContext(), ReleaseTotalScoreActivity.class);
                intent.putExtra("releaseFlag",currentType+"");
                intent.putExtra("releaseTotalScore",mNameNum.getText().toString());
                startActivity(intent);
                break;
        }
    }

    private void getMoneyFun() {

        ShopHelp.veriPassword(getActivity(), new onConnectionFinishLinstener() {

            @Override
            public void onSuccess(int code, Object result) {
                String mResult = "";
                if(TextUtils.isEmpty(result.toString())){
                    mResult = "";
                }else{
                    mResult = result.toString();
                }
                GetMoneyModel model = new GetMoneyModel(mResult); // 1:推广积分，2：消费积分，3：汇联积分，4：志愿者积分，5：合伙人积分，6：分红积分，7：共享红利，8：黄花梨积分

                model.setType("7");

                if (response == null || response.getExchangeRates() == null || response.getExchangeRates().size() <= 6) {
                    model.rate = 0;
                    model.points = 0;
                } else {
                    model.rate = response.getExchangeRates().get(6).getRate();
                    model.points = response.getUserPoints().getDistrPoints();
                }
                Intent intent = new Intent(getContext(), GetMoneyActivity.class);
                intent.putExtra("getMoneyBean", model);
                startActivity(intent);
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show(result);
            }
        });
    }

}
