package com.feitianzhu.huangliwo.plane;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneReimbursementActivity extends BaseActivity {
    @BindView(R.id.select_electronic)
    ImageView selectElectronic;
    @BindView(R.id.select_paper)
    ImageView selectPaper;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.ll_paper)
    LinearLayout ll_paper;
    @BindView(R.id.ll_electronic)
    LinearLayout ll_electronic;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_reimbursement;
    }

    @Override
    protected void initView() {
        titleName.setText("索要报销凭证");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.select_electronic, R.id.select_paper, R.id.left_button, R.id.invoiceType, R.id.rl_address, R.id.identification_num_explain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.select_electronic:
                selectElectronic.setBackgroundResource(R.mipmap.k01_04xuanzhong);
                selectPaper.setBackgroundResource(R.mipmap.k01_03weixuanzhong);
                ll_bottom.setVisibility(View.VISIBLE);
                ll_electronic.setVisibility(View.VISIBLE);
                ll_paper.setVisibility(View.GONE);
                break;
            case R.id.select_paper:
                selectPaper.setBackgroundResource(R.mipmap.k01_04xuanzhong);
                selectElectronic.setBackgroundResource(R.mipmap.k01_03weixuanzhong);
                ll_bottom.setVisibility(View.VISIBLE);
                ll_electronic.setVisibility(View.GONE);
                ll_paper.setVisibility(View.VISIBLE);
                break;
            case R.id.invoiceType:
                String[] strings1 = new String[]{"企业", "个人", "政府机关行政单位"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(PlaneReimbursementActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                    }
                                }))
                        .show();
                break;
            case R.id.rl_address:
                break;
            case R.id.identification_num_explain:
                String content = "纳税人识别号是企业税务登记证上唯一识别企业的税号，由15/18或20位数码组成。根据国家税务局规定，自2017年7月1日起，开具增值税普通发票必须有纳税人识别号或统一社会信用代码，否则该发票为不符合规定的发票，不得作为税收凭证。纳税人识别号可登录纳税人信息查询网www.yibannashuiren.com 查询，或向公司财务人员咨询。";

                new XPopup.Builder(PlaneReimbursementActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;

        }
    }
}
