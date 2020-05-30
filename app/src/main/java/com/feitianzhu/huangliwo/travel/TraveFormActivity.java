package com.feitianzhu.huangliwo.travel;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.databinding.ActivityTraveFormBinding;
import com.feitianzhu.huangliwo.travel.adapter.TraveFormAdapter;
import com.feitianzhu.huangliwo.travel.bean.OilOrederBean;
import com.feitianzhu.huangliwo.travel.request.OilOrderRequest;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

public class TraveFormActivity extends BaseBindingActivity {

    private ActivityTraveFormBinding dataBinding;


    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(TraveFormActivity.this, R.layout.activity_trave_form);
        dataBinding.setViewModel(this);
    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white);
    }

    @Override
    public void init() {
        String phone = SPUtils.getString(this, Constant.SP_PHONE);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        TraveFormAdapter traveFormAdapter = new TraveFormAdapter(null);
        traveFormAdapter.setEmptyView(mEmptyView);
        dataBinding.recyclerView.setAdapter(traveFormAdapter);
        traveFormAdapter.notifyDataSetChanged();


        dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OilOrderRequest oilOrderRequest = new OilOrderRequest(50, 1, phone);
        oilOrderRequest.userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        oilOrderRequest.accessToken = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        oilOrderRequest.call(new ApiCallBack<List<OilOrederBean>>() {
            @Override
            public void onAPIResponse(List<OilOrederBean> response) {
                if (response != null && response.size() > 0) {
                    TraveFormAdapter traveFormAdapter = new TraveFormAdapter(response);
                    dataBinding.recyclerView.setAdapter(traveFormAdapter);
                }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });
    }

}
