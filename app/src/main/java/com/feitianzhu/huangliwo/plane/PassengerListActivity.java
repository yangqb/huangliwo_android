package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.PassengerModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PassengerListActivity extends BaseActivity {
    public static final String SELECT_PASSENGER = "select_passenger";
    private static final int REQUEST_CODE = 100;
    private List<PassengerModel> list = new ArrayList<>();
    private PassengerManagerAdapter mAdapter;
    @BindView(R.id.addPassenger)
    TextView addPassenger;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_passenger_list;
    }

    @Override
    protected void initView() {
        titleName.setText("选择乘机人");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        for (int i = 0; i < 5; i++) {
            PassengerModel model = new PassengerModel();
            model.isSelect = false;
            list.add(model);
        }
        mAdapter = new PassengerManagerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                list.get(position).isSelect = true;
                mAdapter.notifyItemChanged(position);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PassengerListActivity.this, EditPassengerActivity.class);
                intent.putExtra("", list.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.addPassenger, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.addPassenger:
                Intent intent = new Intent(PassengerListActivity.this, EditPassengerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.right_button:
                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect) {
                        index++;
                    }
                }
                Intent data = new Intent();
                data.putExtra(SELECT_PASSENGER, index);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                PassengerModel model = new PassengerModel();
                model.isSelect = false;
                list.add(model);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
