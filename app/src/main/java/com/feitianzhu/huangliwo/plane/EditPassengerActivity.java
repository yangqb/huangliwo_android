package com.feitianzhu.huangliwo.plane;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.http.PlaneResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CustomPlaneDetailInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomLuggageBuyTicketNoticeView;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPassengerActivity extends BaseActivity {
    public static final String PASSENGER_INFO = "passenger_info";
    private PassengerModel passengerModel;
    private int type;
    private CustomPlaneDetailInfo customPlaneDetailInfo;
    private Calendar calendar;
    private int ageType = -1;
    private String cardType;
    private int sex = -1;
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tvCertificatesType)
    TextView tvCertificatesType;
    @BindView(R.id.tvPassengerType)
    TextView tvPassengerType;
    @BindView(R.id.tvSexType)
    TextView tvSexType;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editCardId)
    EditText editCardId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_passenger;
    }

    @Override
    protected void initView() {
        titleName.setText("添加乘机人");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        passengerModel = (PassengerModel) getIntent().getParcelableExtra(PASSENGER_INFO);
        customPlaneDetailInfo = (CustomPlaneDetailInfo) getIntent().getSerializableExtra(PassengerListActivity.PRICE_DATA);
        type = getIntent().getIntExtra(PassengerListActivity.PLANE_TYPE, 0);
        calendar = Calendar.getInstance();
        if (passengerModel != null) {
            ageType = passengerModel.ageType;
            sex = passengerModel.sex;
            if (passengerModel.ageType == 0) {
                tvPassengerType.setText("成人票");
            } else {
                tvPassengerType.setText("儿童票");
            }
            editName.setText(passengerModel.name);
            if (passengerModel.sex == 0) {
                tvSexType.setText("女");
            } else {
                tvSexType.setText("男");
            }
            tvBirthday.setText(passengerModel.birthday);
            cardType = passengerModel.cardType;
            if ("NI".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("身份证");
            } else if ("PP".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("护照");
            } else if ("GA".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("港澳通行证");
            } else if ("TW".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("台湾通行证");
            } else if ("TB".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("台胞证");
            } else if ("HX".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("回乡证");
            } else if ("HY".equals(passengerModel.cardType)) {
                tvCertificatesType.setText("国际海员证");
            } else {
                tvCertificatesType.setText("其他");
            }
            editCardId.setText(passengerModel.cardNo);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(passengerModel.birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date);
        } else {
            Date date = new Date();
            calendar.setTime(date);
        }

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.passenger_type, R.id.certificates_type, R.id.nameTips, R.id.sex_type, R.id.rl_birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                if (TextUtils.isEmpty(tvPassengerType.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择乘机人类型");
                    return;
                }
                if (TextUtils.isEmpty(editName.getText().toString().trim())) {
                    ToastUtils.showShortToast("请填写乘机人姓名");
                    return;
                }
                if (TextUtils.isEmpty(tvSexType.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择乘机人性别");
                    return;
                }
                if (TextUtils.isEmpty(tvBirthday.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择乘机人生日");
                    return;
                }
                if (TextUtils.isEmpty(tvCertificatesType.getText().toString().trim())) {
                    ToastUtils.showShortToast("请选择证件类型");
                    return;
                }
                if (TextUtils.isEmpty(editCardId.getText().toString().trim())) {
                    ToastUtils.showShortToast("请填写证件号码");
                    return;
                }
                submit();
                break;
            case R.id.sex_type:
                String[] strings3 = new String[]{"女", "男"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPassengerActivity.this)
                                .setData(Arrays.asList(strings3))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvSexType.setText(strings3[position]);
                                        sex = position;
                                    }
                                }))
                        .show();
                break;
            case R.id.rl_birthday:
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(EditPassengerActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String birthday = DateUtils.dateToStr(date);
                        tvBirthday.setText(birthday);
                    }
                }).setDate(calendar).build();
                pvTime.show();
                break;
            case R.id.passenger_type:
                String[] strings1 = new String[]{"成人票（满12周岁）", "儿童票（2~12周岁）"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPassengerActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        if (position == 0) {
                                            tvPassengerType.setText("成人票");
                                        } else {
                                            tvPassengerType.setText("儿童票");
                                        }
                                        ageType = position;
                                    }
                                }))
                        .show();
                break;
            case R.id.certificates_type:
                String[] strings2 = new String[]{"身份证", "护照", "港澳通行证", "台湾通行证", "台胞证", "回乡证", "国际海员证", "其他"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditPassengerActivity.this)
                                .setData(Arrays.asList(strings2))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvCertificatesType.setText(strings2[position]);
                                        if (position == 0) {
                                            cardType = "NI";
                                        } else if (position == 1) {
                                            cardType = "PP";
                                        } else if (position == 2) {
                                            cardType = "GA";
                                        } else if (position == 3) {
                                            cardType = "TW";
                                        } else if (position == 4) {
                                            cardType = "TB";
                                        } else if (position == 5) {
                                            cardType = "HX";
                                        } else if (position == 6) {
                                            cardType = "HY";
                                        } else {
                                            cardType = "ID";
                                        }
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

    public void submit() {
        String url;
        if (passengerModel != null) {
            url = Urls.UPDATE_PASSENGER;
        } else {
            url = Urls.ADD_PASSENGER;
            if (type == 2) {
                if (ageType == 1) {
                    ToastUtils.showShortToast("不支持儿童购买");
                    return;
                }
            } else if (type == 1 || type == 3) {
                if (ageType == 1 && customPlaneDetailInfo.customInterPriceInfo.cPrice == 0) {
                    ToastUtils.showShortToast("不支持儿童购买");
                    return;
                }
            } else {
                if (customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap == null) {
                    if (ageType == 1) {
                        ToastUtils.showShortToast("不支持儿童购买");
                        return;
                    }
                } else {
                    if (!customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChild && !customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChildBuyAdult && ageType == 1) {
                        ToastUtils.showShortToast("不支持儿童购买");
                        return;
                    }
                }
            }
        }
        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(url)
                .tag(this);
        if (passengerModel != null) {
            postRequest.params("id", passengerModel.id);
        }
        postRequest.params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("name", editName.getText().toString().trim())
                .params("ageType", ageType + "")
                .params("cardType", cardType + "")
                .params("cardNo", editCardId.getText().toString().trim())
                .params("sex", sex + "")
                .params("birthday", tvBirthday.getText().toString().trim())
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(EditPassengerActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("添加成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }
}
