package com.feitianzhu.huangliwo.pushshop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/13
 * time: 18:17
 * email: 694125155@qq.com
 * <p>
 * 商铺详情
 */
public class MerchantsDetailActivity extends BaseActivity {
    @BindView(R.id.right_img)
    ImageView imageView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edit_merchants_name)
    EditText editMerchantsName;
    @BindView(R.id.edit_merchants_phone)
    EditText editMerchantsPhone;
    @BindView(R.id.edit_merchants_code)
    EditText editMerchantsCode;
    @BindView(R.id.edit_merchants_address)
    EditText editMerchantsAddress;
    @BindView(R.id.edit_merchants_email)
    EditText editMerchantsEmail;
    @BindView(R.id.edit_merchants_discount)
    EditText editMerchantsDiscount;
    @BindView(R.id.edit_setMeal_name)
    EditText editSetMealName;
    @BindView(R.id.edit_setMeal_price)
    EditText editSetMealPrice;
    @BindView(R.id.edit_setMeal_discount)
    EditText editSetMealDiscount;
    @BindView(R.id.edit_merchants_introduction)
    EditText editMerchantsIntroduction;
    @BindView(R.id.select_merchants_type)
    RelativeLayout selectMerchantsType;
    @BindView(R.id.select_merchants_area)
    RelativeLayout selectMerchantsArea;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants_detail;
    }

    @Override
    protected void initView() {
        imageView.setBackgroundResource(R.mipmap.g06_01bianji);
        rightText.setText("编辑");
        rightText.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        setEditable(false);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                setEditable(true);
                break;
        }
    }

    /*
     * 是否可编辑
     * */
    public void setEditable(boolean editable) {
        if (editable) {
            editMerchantsName.setFocusable(true);
            editMerchantsName.setFocusableInTouchMode(true);
            editMerchantsName.requestFocus();
            editMerchantsPhone.setFocusable(true);
            editMerchantsPhone.setFocusableInTouchMode(true);
            editMerchantsCode.setFocusable(true);
            editMerchantsCode.setFocusableInTouchMode(true);
            editMerchantsAddress.setFocusable(true);
            editMerchantsAddress.setFocusableInTouchMode(true);
            editMerchantsEmail.setFocusable(true);
            editMerchantsEmail.setFocusableInTouchMode(true);
            editMerchantsDiscount.setFocusable(true);
            editMerchantsDiscount.setFocusableInTouchMode(true);
            editSetMealName.setFocusable(true);
            editSetMealName.setFocusableInTouchMode(true);
            editSetMealPrice.setFocusable(true);
            editSetMealPrice.setFocusableInTouchMode(true);
            editSetMealDiscount.setFocusable(true);
            editSetMealDiscount.setFocusableInTouchMode(true);
            editMerchantsIntroduction.setFocusable(true);
            editMerchantsIntroduction.setFocusableInTouchMode(true);
            selectMerchantsType.setEnabled(true);
            selectMerchantsArea.setEnabled(true);
        } else {
            editMerchantsName.setFocusable(false);
            editMerchantsName.setFocusableInTouchMode(false);
            editMerchantsPhone.setFocusable(false);
            editMerchantsPhone.setFocusableInTouchMode(false);
            editMerchantsCode.setFocusable(false);
            editMerchantsCode.setFocusableInTouchMode(false);
            editMerchantsAddress.setFocusable(false);
            editMerchantsAddress.setFocusableInTouchMode(false);
            editMerchantsEmail.setFocusable(false);
            editMerchantsEmail.setFocusableInTouchMode(false);
            editMerchantsDiscount.setFocusable(false);
            editMerchantsDiscount.setFocusableInTouchMode(false);
            editSetMealName.setFocusable(false);
            editSetMealName.setFocusableInTouchMode(false);
            editSetMealPrice.setFocusable(false);
            editSetMealPrice.setFocusableInTouchMode(false);
            editSetMealDiscount.setFocusable(false);
            editSetMealDiscount.setFocusableInTouchMode(false);
            editMerchantsIntroduction.setFocusable(false);
            editMerchantsIntroduction.setFocusableInTouchMode(false);
            selectMerchantsType.setEnabled(false);
            selectMerchantsArea.setEnabled(false);
        }
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
