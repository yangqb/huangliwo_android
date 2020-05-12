package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;
import com.feitianzhu.huangliwo.shop.adapter.ShopInfoDetailAdapter;
import com.feitianzhu.huangliwo.shop.adapter.ShopInfoDetailImgAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 3:53
 * （线下商店）套餐详情页
 */
public class ShopSetMealDetailActivity extends BaseActivity {
    public static final String SETMEAL_INFO = "setmeal_info";
    private SetMealInfo setMealInfo;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.img_collect)
    ImageView collectImg;
    @BindView(R.id.amount)
    TextView bottomAmount;
    @BindView(R.id.recyclerView)
    RecyclerView hRecyclerView;
    @BindView(R.id.recyclerView2)
    RecyclerView mRecyclerView;
    @BindView(R.id.setMealName)
    TextView setMealName;
    @BindView(R.id.setMealDescription)
    TextView setMealDescription;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopinfo_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("套餐详情");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        //rightImg.setVisibility(View.VISIBLE);
        //collectImg.setVisibility(View.VISIBLE);
        setMealInfo = (SetMealInfo) getIntent().getSerializableExtra(SETMEAL_INFO);
        if (setMealInfo != null) {
            setMealName.setText(setMealInfo.getSmName());
            setMealDescription.setText(setMealInfo.getRemark());
            String[] imgUrls = setMealInfo.getImgs().split(",");
            hRecyclerView.setNestedScrollingEnabled(false);
            hRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ShopInfoDetailImgAdapter adapter = new ShopInfoDetailImgAdapter(Arrays.asList(imgUrls));
            hRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            List<SingleGoodsModel> singleList = setMealInfo.getSingleList();

            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ShopInfoDetailAdapter mAdapter = new ShopInfoDetailAdapter(singleList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            bottomAmount.setText("");
            String str1 = "合计：";
            String str2 = "¥ ";
            String str3 = String.format(Locale.getDefault(), "%.2f", setMealInfo.getPrice());

            SpannableString span1 = new SpannableString(str1);
            SpannableString span2 = new SpannableString(str2);
            SpannableString span3 = new SpannableString(str3);
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
            span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
            span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
            span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            bottomAmount.append(span1);
            bottomAmount.append(span2);
            bottomAmount.append(span3);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.tv_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                if (token == null || TextUtils.isEmpty(token)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(ShopSetMealDetailActivity.this, SetMealPayActivity.class);
                intent.putExtra(SetMealPayActivity.ORDER_NO,"");
                intent.putExtra(SetMealPayActivity.SETMEAL_ORDERI_NFO, setMealInfo);
                startActivity(intent);
                break;
        }

    }
}
