package com.feitianzhu.huangliwo.im;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.im.request.ConverServiceUrlRequest;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomerserviceActivity extends BaseActivity {

    @BindView(R.id.left_button)
    RelativeLayout leftButton;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.shopissues)
    LinearLayout shopissues;
    @BindView(R.id.returnissue)
    LinearLayout returnissue;
    @BindView(R.id.platformissues)
    LinearLayout platformissues;
    private List<ConverzServiceListBean> response;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customerservice;
    }

    @Override
    protected void initView() {
        titleName.setText("在线客服");
    }

    @Override
    protected void initData() {
        ConverServiceUrlRequest converServiceRequest = new ConverServiceUrlRequest();
        converServiceRequest.isShowLoading = true;
        converServiceRequest.call(new ApiCallBack<List<ConverzServiceListBean>>() {
            @Override
            public void onAPIResponse(List<ConverzServiceListBean> response) {
                CustomerserviceActivity.this.response = response;
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });
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

    @OnClick({R.id.left_button, R.id.shopissues, R.id.returnissue, R.id.platformissues})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.shopissues:

                LoadingUtil.setLoadingViewShow(false);
                if (response != null && response.size() > 0) {
                    Intent intent = new Intent(CustomerserviceActivity.this, ImActivity.class);
                    //username为对方的环信id
//                    intent.putExtra("name",response.get(0).getNick());
//                    intent.putExtra("icon",response.get(0).getIcon());
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, "688577"+"-dev");
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, response.get(0).getUserId() + IMContent.IMTAG);
                    startActivity(intent);
                }
                break;
            case R.id.returnissue:
                startActivity(new Intent(CustomerserviceActivity.this, CustomerservicelistActivity.class));
                break;
            case R.id.platformissues:
                startActivity(new Intent(CustomerserviceActivity.this, CustomerservicelistActivity.class));
                break;
        }
    }
}
