package com.feitianzhu.fu700.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.shop.adapter.ProductParametersAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/19
 * time: 12:37
 * email: 694125155@qq.com
 */
public class CustomSpecificationDialog extends Dialog {
    private Context context;
    private RelativeLayout btn_cancel;
    private TextView btn_ok;
    private RecyclerView recyclerView;
    private ProductParametersAdapter mAdapter;
    private List<ProductParameters.GoodsSpecifications> data;
    private OnOkClickListener negativeButtonClickListener;
    private int count = 0;


    public interface OnOkClickListener {
        void onOkClick(List<ProductParameters.GoodsSpecifications> data);
    }

    public CustomSpecificationDialog(Context context) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ProductParametersAdapter(data);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
                    ToastUtils.showShortToast("请选择商品规格");
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

    public CustomSpecificationDialog setData(List<ProductParameters.GoodsSpecifications> data) {
        this.data = data;
        return this;
    }

    public CustomSpecificationDialog setNegativeButton(
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
