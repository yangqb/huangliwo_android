package com.feitianzhu.huangliwo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.ProductParameters;
import com.feitianzhu.huangliwo.model.ShoppingCartModel;
import com.feitianzhu.huangliwo.shop.adapter.ProductParametersAdapter;
import com.hjq.toast.ToastUtils;

import java.util.List;
import java.util.Locale;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/19
 * time: 12:37
 * email: 694125155@qq.com
 */
public class CustomSpecificationDialogtwo extends Dialog {
    private Context context;
    private RelativeLayout btn_cancel;
    private TextView btn_ok;
    private RecyclerView recyclerView;
    private ProductParametersAdapter mAdapter;
    private List<ProductParameters.GoodsSpecifications> data;
    ShoppingCartModel.CartGoodsModel shoppingCartModels;
    private OnOkClickListener negativeButtonClickListener;
    private int count = 0;


    public interface OnOkClickListener {
        void onOkClick(List<ProductParameters.GoodsSpecifications> data);
    }

    public CustomSpecificationDialogtwo(Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_specification);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok = findViewById(R.id.btn_ok);
        recyclerView = findViewById(R.id.recyclerView);
        TextView shopdetailprice = findViewById(R.id.shopdetailprice);
        TextView shopdetailname = findViewById(R.id.shopdetailname);
        ImageView shopdetailimg = findViewById(R.id.shopdetailimg);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ProductParametersAdapter(data);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        String goodsName = shoppingCartModels.title;
        shopdetailname.setText(goodsName);

        double price = shoppingCartModels.price;
        String pricec = String.format(Locale.getDefault(), "%.2f", price);
        shopdetailprice.setText("￥ " + pricec);
        Glide.with(context).load(shoppingCartModels.goodsImg)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.g10_04weijiazai)
                        .error(R.mipmap.g10_04weijiazai)
                        .dontAnimate())
                .into(shopdetailimg);


        listener();
    }


    public void listener() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).getSkuValueList().size(); j++) {
                        if (data.get(i).getSkuValueList().get(j).isSelect()) {
                            count++;
                        }
                    }
                }
                if (count != data.size()) {
                    ToastUtils.show("请选择商品规格");
                } else {
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onOkClick(data);
                        dismiss();
                    }
                }
                count = 0;
            }
        });
        mAdapter.setOnChildPositionListener(new ProductParametersAdapter.OnChildClickListener() {
            @Override
            public void success(int index, int pos) {
                Log.e("index==pos", "item=" + pos + "position=" + index);
                for (int i = 0; i < data.get(pos).getSkuValueList().size(); i++) {
                    if (i == index) {
                        data.get(pos).getSkuValueList().get(i).setSelect(true);
                    } else {
                        data.get(pos).getSkuValueList().get(i).setSelect(false);
                    }
                }
                mAdapter.setNewData(data);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public CustomSpecificationDialogtwo setDate(List<ProductParameters.GoodsSpecifications> data, ShoppingCartModel.CartGoodsModel shoppingCartModels) {
        this.data = data;
        this.shoppingCartModels = shoppingCartModels;
        return this;
    }

    public CustomSpecificationDialogtwo setNegativeButton(
            OnOkClickListener listener) {
        this.negativeButtonClickListener = listener;
        return this;
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
