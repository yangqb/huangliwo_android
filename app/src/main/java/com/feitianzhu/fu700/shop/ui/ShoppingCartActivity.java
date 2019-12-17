package com.feitianzhu.fu700.shop.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.model.ShoppingCartMode;
import com.feitianzhu.fu700.shop.adapter.ShoppingCartAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.CustomSpecificationView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.shop.ui
 * user: yangqinbo
 * date: 2019/12/17
 * time: 13:56
 * email: 694125155@qq.com
 * 购物车列表
 */
public class ShoppingCartActivity extends BaseActivity {
    private String totalAmount = "0.00";
    private double p = 0.00;
    private ShoppingCartAdapter mAdapter;
    private List<ShoppingCartMode> shoppingCartModes;
    private List<ProductParameters.GoodslistBean.SkuValueListBean> data = new ArrayList<>();
    private String str1;
    private String str2;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.amount)
    TextView bottomAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void initView() {
        titleName.setText("购物车");
        str1 = "合计：";
        str2 = "¥ ";
        shoppingCartModes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShoppingCartMode cartMode = new ShoppingCartMode();
            cartMode.setSelect(false);
            cartMode.setPrice(100.00);
            cartMode.setCount(i);
            cartMode.setAttributeVal("规格");
            shoppingCartModes.add(cartMode);
        }

        for (int i = 0; i < 10; i++) {
            ProductParameters.GoodslistBean.SkuValueListBean listBean = new ProductParameters.GoodslistBean.SkuValueListBean();
            listBean.setAttributeVal("阿拉啦啦" + i);
            data.add(listBean);
        }


        mAdapter = new ShoppingCartAdapter(shoppingCartModes);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setSpannableString(totalAmount);
        initListener();
    }

    public void initListener() {

        mAdapter.setOnGoodsAmountListener(new ShoppingCartAdapter.OnGoodsAmountListener() {
            @Override
            public void getGoodsAmount(int pos, int count) {
                shoppingCartModes.get(pos).setCount(count);
                p = 0.00;
                for (ShoppingCartMode shoppingCartMode : shoppingCartModes
                ) {
                    if (shoppingCartMode.isSelect()) {
                        p += shoppingCartMode.getCount() * shoppingCartMode.getPrice();
                    }
                    totalAmount = String.format(Locale.getDefault(), "%.2f", p);
                    setSpannableString(totalAmount);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.delete:
                        shoppingCartModes.remove(position);
                        mAdapter.setNewData(shoppingCartModes);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.select_goods:
                        shoppingCartModes.get(position).setSelect(!shoppingCartModes.get(position).isSelect());
                        mAdapter.setNewData(shoppingCartModes);
                        mAdapter.notifyItemChanged(position);
                        for (ShoppingCartMode shoppingCartMode : shoppingCartModes
                        ) {
                            p = shoppingCartMode.getCount() * shoppingCartMode.getPrice();
                            if (!shoppingCartMode.isSelect()) {
                                p -= shoppingCartMode.getCount() * shoppingCartMode.getPrice();
                            } else {
                                p += shoppingCartMode.getCount() * shoppingCartMode.getPrice();
                            }
                        }
                        totalAmount = String.format(Locale.getDefault(), "%.2f", p);
                        setSpannableString(totalAmount);
                        break;
                    case R.id.summary:
                        new XPopup.Builder(ShoppingCartActivity.this)
                                .asCustom(new CustomSpecificationView(ShoppingCartActivity.this)
                                        .setData(data)
                                        .setOnItemClickListener(new CustomSpecificationView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int pos) {
                                                ToastUtils.showShortToast("选择了" + data.get(pos).getAttributeVal());
                                                shoppingCartModes.get(position).setAttributeVal(data.get(pos).getAttributeVal());
                                                mAdapter.notifyItemChanged(position);
                                            }
                                        }))
                                .show();
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button})
    public void onClick(View view) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3) {
        bottomAmount.setText("");
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
