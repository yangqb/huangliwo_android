package com.feitianzhu.huangliwo.shop.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.ProductParameters;
import com.feitianzhu.huangliwo.model.ShoppingCartEvent;
import com.feitianzhu.huangliwo.model.ShoppingCartModel;
import com.feitianzhu.huangliwo.model.UpdateShoppingCartBody;
import com.feitianzhu.huangliwo.shop.SettlementShoppingCartActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.shop.adapter.ShoppingCartAdapter;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CustomInputView;
import com.feitianzhu.huangliwo.view.CustomSpecificationDialog;
import com.feitianzhu.huangliwo.view.CustomSpecificationDialogtwo;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private StringBuffer carIds = new StringBuffer();
    private List<ShoppingCartModel.CartGoodsModel> allSelectList = new ArrayList<>();
    private boolean isSelectAll = false;
    private ShoppingCartAdapter mAdapter;
    private StringBuffer valueId;
    private StringBuffer speciName;
    private List<ShoppingCartModel.CartGoodsModel> shoppingCartModels;
    private UpdateShoppingCartBody shoppingCartBody = new UpdateShoppingCartBody();
    private ProductParameters productParameters;
    private List<ProductParameters.GoodsSpecifications> specifications = new ArrayList<>();
    private String str1;
    private String str2;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.amount)
    TextView bottomAmount;
    @BindView(R.id.swipeLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.select_img)
    ImageView selectImg;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("购物车");
        str1 = "合计：";
        str2 = "¥ ";
        shoppingCartModels = new ArrayList<>();
        mAdapter = new ShoppingCartAdapter(shoppingCartModels);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        TextView noData = mEmptyView.findViewById(R.id.no_data);
        noData.setText("购物车空空如也，快去下单吧");
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);
        setSpannableString(totalAmount);
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //商品详情
                Intent intent = new Intent(ShoppingCartActivity.this, ShopsDetailActivity.class);
                intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, shoppingCartModels.get(position).goodsId);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.delete: //删除购物车商品
                        new XPopup.Builder(ShoppingCartActivity.this)
                                .asConfirm("确定要删除该商品吗？", "", "取消", "确定", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        deleteShoppingCart(shoppingCartModels.get(position).carId + "");
                                        shoppingCartModels.remove(position);
                                        calculationAmount();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }, null, false)
                                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                .show();
                        break;
                    case R.id.select_goods: //选择支付的商品
                        //修改商品信息
                        shoppingCartModels.get(position).checks = shoppingCartModels.get(position).checks == 0 ? 1 : 0;
                        shoppingCartBody.carId = shoppingCartModels.get(position).carId;
                        shoppingCartBody.checks = shoppingCartModels.get(position).checks;
                        shoppingCartBody.goodsCount = shoppingCartModels.get(position).goodsCount;
                        shoppingCartBody.speci = shoppingCartModels.get(position).speci;
                        shoppingCartBody.speciName = shoppingCartModels.get(position).speciName;
                        if (isSelectAll()) {
                            selectImg.setBackgroundResource(R.mipmap.g07_02quan);
                        } else {
                            selectImg.setBackgroundResource(R.mipmap.g07_01quan);
                        }
                        mAdapter.notifyItemChanged(position);
                        calculationAmount();
                        upDateShoppingCart();
                        break;
                    case R.id.summary: //选择规格
                        getSpecifications(position, shoppingCartModels.get(position).goodsId, shoppingCartModels.get(position).speci);
                        break;
                    case R.id.btnDecrease:
                        if (shoppingCartModels.get(position).goodsCount != 1) {
                            shoppingCartModels.get(position).goodsCount = shoppingCartModels.get(position).goodsCount - 1;
                        }
                        mAdapter.notifyItemChanged(position);
                        calculationAmount();
                        shoppingCartBody.carId = shoppingCartModels.get(position).carId;
                        shoppingCartBody.checks = shoppingCartModels.get(position).checks;
                        shoppingCartBody.goodsCount = shoppingCartModels.get(position).goodsCount;
                        shoppingCartBody.speci = shoppingCartModels.get(position).speci;
                        shoppingCartBody.speciName = shoppingCartModels.get(position).speciName;
                        upDateShoppingCart();
                        break;
                    case R.id.btnIncrease:
                        if (shoppingCartModels.get(position).goodsCount >= 10) {
                            shoppingCartModels.get(position).goodsCount = 10;
                        } else {
                            shoppingCartModels.get(position).goodsCount = shoppingCartModels.get(position).goodsCount + 1;
                        }
                        mAdapter.notifyItemChanged(position);
                        calculationAmount();
                        shoppingCartBody.carId = shoppingCartModels.get(position).carId;
                        shoppingCartBody.checks = shoppingCartModels.get(position).checks;
                        shoppingCartBody.goodsCount = shoppingCartModels.get(position).goodsCount;
                        shoppingCartBody.speci = shoppingCartModels.get(position).speci;
                        shoppingCartBody.speciName = shoppingCartModels.get(position).speciName;
                        upDateShoppingCart();
                        break;
                    case R.id.etAmount:
                        shopEditDialog(position);
                        break;
                }
            }
        });
    }

    public boolean isSelectAll() {
        for (int i = 0; i < shoppingCartModels.size(); i++) {
            if (shoppingCartModels.get(i).checks == 0) {
                return false;
            }
        }
        return true;
    }

    public void shopEditDialog(int pos) {
        new XPopup.Builder(ShoppingCartActivity.this)
                .asCustom(new CustomInputView(ShoppingCartActivity.this)
                        .setTitle("请输入购买数量")
                        .setText(shoppingCartModels.get(pos).goodsCount)
                        .setOnConfirmClickListener(new CustomInputView.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(String account) {
                                if (!TextUtils.isEmpty(account) && Integer.valueOf(account) > 0) {
                                    shoppingCartModels.get(pos).goodsCount = Integer.valueOf(account);
                                    mAdapter.notifyItemChanged(pos);
                                    calculationAmount();
                                    shoppingCartBody.carId = shoppingCartModels.get(pos).carId;
                                    shoppingCartBody.checks = shoppingCartModels.get(pos).checks;
                                    shoppingCartBody.goodsCount = shoppingCartModels.get(pos).goodsCount;
                                    shoppingCartBody.speci = shoppingCartModels.get(pos).speci;
                                    shoppingCartBody.speciName = shoppingCartModels.get(pos).speciName;
                                    upDateShoppingCart();
                                }
                            }
                        }))
                .show();
    }

    public void deleteShoppingCart(String cartId) {
        OkGo.<LzyResponse>get(Urls.DELETE_SHOPPING_CART)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("carId", cartId)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShoppingCartActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {

                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void upDateShoppingCart() {
        String json = new Gson().toJson(shoppingCartBody);
        OkGo.<LzyResponse>post(Urls.UPDATE_SHOPPING_CART)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("shopingCarBody", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(ShoppingCartActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {

                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });

    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<ShoppingCartModel>>get(Urls.GET_SHOPPING_CART_LIST)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<ShoppingCartModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ShoppingCartModel>> response) {
                        super.onSuccess(ShoppingCartActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            if (response.body().data.allSCcar != null && response.body().data.allSCcar.size() > 0) {
                                llSelect.setVisibility(View.VISIBLE);
                                shoppingCartModels = response.body().data.allSCcar;
                                for (int i = 0; i < shoppingCartModels.size(); i++) {
                                    shoppingCartModels.get(i).checks = 0;
                                }
                                mAdapter.setNewData(shoppingCartModels);
                                mAdapter.notifyDataSetChanged();
                                calculationAmount();
                            } else {
                                llSelect.setVisibility(View.GONE);
                            }
                        } else {
                            llSelect.setVisibility(View.GONE);
                        }
                        mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Response<LzyResponse<ShoppingCartModel>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    public void getSpecifications(int pos, int goodsId, String specIds) {
        List<String> ids = new ArrayList<>();
        if (specIds.contains(",")) {
            String[] strs = specIds.split(",");
            ids.addAll(Arrays.asList(strs));
        } else {
            ids.add(specIds);
        }

        OkGo.<LzyResponse<ProductParameters>>post(Urls.GET_PRODUCT_PARAMETERS)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("goodsId", goodsId + "")
                .execute(new JsonCallback<LzyResponse<ProductParameters>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<ProductParameters>> response) {
                        //super.onSuccess(ShopsDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            productParameters = response.body().data;
                            /*
                             * 数据处理
                             * */
                            if (productParameters != null && productParameters.getGoodslist() != null && productParameters.getGoodslist().size() > 0) {
                                specifications = productParameters.getGoodslist();
                                /*
                                 * 默认选中第一个
                                 * */
                                for (int i = 0; i < specifications.size(); i++) {
                                    for (int j = 0; j < specifications.get(i).getSkuValueList().size(); j++) {
                                        for (int k = 0; k < ids.size(); k++) {
                                            if (specifications.get(i).getSkuValueList().get(j).getValueId() == Integer.valueOf(ids.get(k))) {
                                                specifications.get(i).getSkuValueList().get(j).setSelect(true);
                                            }
                                        }
                                    }
                                }
                                showSpecDialog(pos);
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<ProductParameters>> response) {
                        super.onError(response);
                    }
                });
    }

    public void showSpecDialog(int pos) {
        /* //商品规格
         */
        new CustomSpecificationDialogtwo(this).setDate(specifications, shoppingCartModels.get(pos))
                .setNegativeButton(new CustomSpecificationDialogtwo.OnOkClickListener() {
                    @Override
                    public void onOkClick(List<ProductParameters.GoodsSpecifications> data) {
                        valueId = new StringBuffer();
                        speciName = new StringBuffer();
                        List<ProductParameters.GoodsSpecifications.SkuValueListBean> selectSpec = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            for (int j = 0; j < data.get(i).getSkuValueList().size(); j++) {
                                if (data.get(i).getSkuValueList().get(j).isSelect()) {
                                    selectSpec.add(data.get(i).getSkuValueList().get(j));
                                }
                            }
                        }
                        if (selectSpec.size() > 0) {
                            for (int i = 0; i < selectSpec.size(); i++) {
                                if (i == selectSpec.size() - 1) {
                                    valueId.append(selectSpec.get(i).getValueId());
                                    speciName.append(selectSpec.get(i).getAttributeVal());
                                } else {
                                    valueId.append(selectSpec.get(i).getValueId() + ",");
                                    speciName.append(selectSpec.get(i).getAttributeVal() + ",");
                                }
                            }
                        }
                        shoppingCartModels.get(pos).speci = valueId.toString();
                        shoppingCartModels.get(pos).speciName = speciName.toString();
                        shoppingCartBody.carId = shoppingCartModels.get(pos).carId;
                        shoppingCartBody.checks = shoppingCartModels.get(pos).checks;
                        shoppingCartBody.goodsCount = shoppingCartModels.get(pos).goodsCount;
                        shoppingCartBody.speci = valueId.toString();
                        shoppingCartBody.speciName = speciName.toString();
                        mAdapter.notifyItemChanged(pos);
                        upDateShoppingCart();
                    }
                }).show();
    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.select_all, R.id.delete_all})
    @SingleClick()
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.select_all:
                selectAll();
                break;
            case R.id.delete_all:
                allSelectList.clear();
                for (int i = 0; i < shoppingCartModels.size(); i++) {
                    if (shoppingCartModels.get(i).checks == 1) {
                        allSelectList.add(shoppingCartModels.get(i));
                        carIds.append(shoppingCartModels.get(i).carId);
                        carIds.append(",");
                    }
                }
                if (allSelectList.size() <= 0) {
                    ToastUtils.show("请选择要删除的商品");
                    return;
                }
                shoppingCartModels.removeAll(allSelectList);
                mAdapter.notifyDataSetChanged();
                calculationAmount();
                deleteShoppingCart(carIds.substring(0, carIds.length() - 1));
                break;
            case R.id.tv_pay:
                ArrayList<ShoppingCartModel.CartGoodsModel> selectCartModels = new ArrayList<>();
                for (ShoppingCartModel.CartGoodsModel shoppingCartModel : shoppingCartModels
                ) {
                    if (shoppingCartModel.checks == 1) {
                        selectCartModels.add(shoppingCartModel);
                    }
                }
                if (selectCartModels.size() > 0) {
                    Intent intent = new Intent(ShoppingCartActivity.this, SettlementShoppingCartActivity.class);
                    intent.putParcelableArrayListExtra(SettlementShoppingCartActivity.CART_DATA, selectCartModels);
                    startActivity(intent);
                } else {
                    ToastUtils.show("没有要结算的商品");
                }
                break;
        }
    }

    public void selectAll() {
        isSelectAll = !isSelectAll;
        if (isSelectAll) {
            selectImg.setBackgroundResource(R.mipmap.g07_02quan);
        } else {
            selectImg.setBackgroundResource(R.mipmap.g07_01quan);
        }
        for (int i = 0; i < shoppingCartModels.size(); i++) {
            shoppingCartModels.get(i).checks = isSelectAll ? 1 : 0;
            shoppingCartBody.carId = shoppingCartModels.get(i).carId;
            shoppingCartBody.checks = shoppingCartModels.get(i).checks;
            shoppingCartBody.goodsCount = shoppingCartModels.get(i).goodsCount;
            shoppingCartBody.speci = shoppingCartModels.get(i).speci;
            shoppingCartBody.speciName = shoppingCartModels.get(i).speciName;
        }
        mAdapter.notifyDataSetChanged();
        calculationAmount();
        upDateShoppingCart();
    }

    public void calculationAmount() {
        p = 0.00;
        for (ShoppingCartModel.CartGoodsModel shoppingCartModel : shoppingCartModels
        ) {
            if (shoppingCartModel.checks == 1) {
                p += shoppingCartModel.goodsCount * shoppingCartModel.price;
            }
        }
        totalAmount = String.format(Locale.getDefault(), "%.2f", p);
        setSpannableString(totalAmount);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShoppingCartEvent(ShoppingCartEvent cartEvent) {
        if (cartEvent == ShoppingCartEvent.CREATE_ORDER_SUCCESS) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
