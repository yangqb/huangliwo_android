package com.feitianzhu.huangliwo.shop.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.LazyBaseActivity;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopReportActivity extends LazyBaseActivity {

    @BindView(R.id.content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    TextView mBtnSubmit;
    private String mId;
    private String mtype;

    @Override
    protected void initView() {
        Intent mIntent = getIntent();
        mId = mIntent.getStringExtra(Constant.MERCHANTID);
        mtype = mIntent.getStringExtra(Constant.TYPE);
    }

    @Override
    protected void initData() {
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String str = mContent.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.show("举报内容不能为空");
            return;
        }
        ShopDao.postShopReport(mId, mtype, str, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.show("举报成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show(result);
            }
        });
    }

    @Override
    protected int getChildLayoutId() {
        return R.layout.activity_shop_report;
    }
}
