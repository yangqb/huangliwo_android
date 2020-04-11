package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CollectionInfo;
import com.feitianzhu.huangliwo.model.CustomPlaneDetailInfo;
import com.feitianzhu.huangliwo.model.PassengerModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PassengerListActivity extends BaseActivity {
    public static final String SELECT_PASSENGER = "select_passenger";
    private static final int REQUEST_CODE = 100;
    public static final String PRICE_DATA = "price_data";
    public static final String PLANE_TYPE = "plane_type";
    private int type;
    private CustomPlaneDetailInfo customPlaneDetailInfo;
    private String userId;
    private String token;
    private List<PassengerModel> list = new ArrayList<>();
    private ArrayList<PassengerModel> selectPassenger = new ArrayList<>();
    private List<PassengerModel> locModelList = new ArrayList<>();
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
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("选择乘机人");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        customPlaneDetailInfo = (CustomPlaneDetailInfo) getIntent().getSerializableExtra(PRICE_DATA);
        type = getIntent().getIntExtra(PLANE_TYPE, 0);

        mAdapter = new PassengerManagerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (type == 2) {
                    if (locModelList.get(position).ageType == 1) {
                        ToastUtils.showShortToast("不支持儿童购买");
                    } else {
                        locModelList.get(position).isSelect = !locModelList.get(position).isSelect;
                    }
                } else if (type == 1 || type == 3) {
                    if (customPlaneDetailInfo.customInterPriceInfo.cPrice == 0 && locModelList.get(position).ageType == 1) {
                        ToastUtils.showShortToast("不支持儿童购买");
                    } else {
                        locModelList.get(position).isSelect = !locModelList.get(position).isSelect;
                    }
                } else {
                    if (customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap != null) {
                        if (!customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChild && !customPlaneDetailInfo.customDocGoPriceInfo.businessExtMap.supportChildBuyAdult) {
                            if (locModelList.get(position).ageType == 1) {
                                ToastUtils.showShortToast("不支持儿童购买");
                            } else {
                                locModelList.get(position).isSelect = !locModelList.get(position).isSelect;
                            }
                        } else {
                            locModelList.get(position).isSelect = !locModelList.get(position).isSelect;
                        }
                    } else {
                        if (locModelList.get(position).ageType == 1) {
                            ToastUtils.showShortToast("不支持儿童购买");
                        } else {
                            locModelList.get(position).isSelect = !locModelList.get(position).isSelect;
                        }
                    }
                }
                mAdapter.notifyItemChanged(position);

            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PassengerListActivity.this, EditPassengerActivity.class);
                intent.putExtra(EditPassengerActivity.PASSENGER_INFO, locModelList.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                deletePassenger(locModelList.get(pos).id);
                locModelList.remove(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
    }

    public void deletePassenger(String id) {
        OkGo.<LzyResponse>post(Urls.DELETE_PASSENGER)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("id", id)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(PassengerListActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("删除成功");
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
        OkGo.<LzyResponse<List<PassengerModel>>>get(Urls.GET_PASSENGER_LIST)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<List<PassengerModel>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<PassengerModel>>> response) {
                        super.onSuccess(PassengerListActivity.this, response.body().msg, response.body().code);
                        locModelList.clear();
                        if (response.body().code == 0 && response.body().data != null) {
                            locModelList.addAll(response.body().data);
                            list = response.body().data;
                            mAdapter.setNewData(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<List<PassengerModel>>> response) {
                        super.onError(response);
                    }
                });


    }

    @OnClick({R.id.left_button, R.id.addPassenger, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.addPassenger:
                Intent intent = new Intent(PassengerListActivity.this, EditPassengerActivity.class);
                intent.putExtra(PassengerListActivity.PRICE_DATA, customPlaneDetailInfo);
                intent.putExtra(PassengerListActivity.PLANE_TYPE, type);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.right_button:
                for (int i = 0; i < locModelList.size(); i++) {
                    if (locModelList.get(i).isSelect) {
                        selectPassenger.add(locModelList.get(i));
                    }
                }
                if (selectPassenger.size() <= 0) {
                    ToastUtils.showShortToast("请选择乘机人");
                } else {
                    Intent data = new Intent();
                    data.putParcelableArrayListExtra(SELECT_PASSENGER, selectPassenger);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                initData();
            }
        }
    }
}
