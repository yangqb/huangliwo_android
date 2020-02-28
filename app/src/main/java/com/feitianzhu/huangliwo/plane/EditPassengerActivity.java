package com.feitianzhu.huangliwo.plane;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomLuggageBuyTicketNoticeView;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPassengerActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tvCertificatesType)
    TextView tvCertificatesType;
    @BindView(R.id.tvPassengerType)
    TextView tvPassengerType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_passenger;
    }

    @Override
    protected void initView() {
        titleName.setText("添加乘机人");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.passenger_type, R.id.certificates_type, R.id.nameTips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.passenger_type:
                String[] strings1 = new String[]{"成人票（满12周岁）", "儿童票（2~12周岁）", "婴儿票（14天~2周岁）"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPassengerActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvPassengerType.setText(strings1[position]);
                                    }
                                }))
                        .show();
                break;
            case R.id.certificates_type:
                String[] strings2 = new String[]{"身份证", "护照", "外国人永久居留身份证", "其他/港澳台居民居住证"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPassengerActivity.this)
                                .setData(Arrays.asList(strings2))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvCertificatesType.setText(strings2[position]);
                                    }
                                }))
                        .show();
                break;
            case R.id.nameTips:
                String content = "1. 请您严格按照办理登记手续时所持有效证件上的姓名填写。\n" +
                        "2. 如果您的姓名中含有生僻字或繁体字，请用拼音代替生僻字（如：陶喆喆，请填写‘陶zhezhe’）。\n" +
                        "3. 少数民族乘客可不输入姓名中的符号点。\n" +
                        "4. 持护照或使用英文名的乘客，须按照护照或所持证件上的顺序填写姓名且不区分大小写，并将姓与名用/分割（姓/名）。\n" +
                        "5. 姓名过长时请使用缩写（中文名不超过12个汉字；英文名不超过29个字母，包括空格和/）。";

                new XPopup.Builder(EditPassengerActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;
        }
    }
}
