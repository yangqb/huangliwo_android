package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.pushshop.adapter.SetMealListAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealListInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/13
 * time: 17:41
 * email: 694125155@qq.com
 */
public class SetMealListActivity extends BaseActivity {
    private static final int EDIT_REQUEST_CODE = 1000;
    private static final int ADD_REQUEST_CODE = 999;
    public static final String MERCHANTS_ID = "merchants_id";
    private List<SetMealInfo> setMealInfoList = new ArrayList<>();
    private int merchantsId = -1;
    private SetMealListAdapter mAdapter;
    private String token;
    private String userId;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setmeal_list;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        rightText.setText("新增");
        rightImg.setBackgroundResource(R.mipmap.a01_04jia);
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        titleName.setText("上传套餐");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SetMealListAdapter(setMealInfoList);
        View mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        ImageView img_empty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
        img_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getEmptyView().setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setEnableLoadMore(false);
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SetMealListActivity.this, SetMealDetailActivity.class);
                intent.putExtra(SetMealDetailActivity.SETMEAL_ID, setMealInfoList.get(position).getSmId());
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (setMealInfoList.get(position).getIsShelf() == 0) {
                    setMealInfoList.get(position).setIsShelf(1);
                } else {
                    setMealInfoList.get(position).setIsShelf(0);
                }
                mAdapter.setNewData(setMealInfoList);
                mAdapter.notifyItemChanged(position);
                update(setMealInfoList.get(position).getIsShelf(), setMealInfoList.get(position).getSmId());
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    /*
     * 更新套餐的上架下架
     * */
    public void update(int isShelf, int smId) {
        OkGo.<LzyResponse>get(Urls.UPDATE_SETMEAL_SHELF)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("smId", smId + "")
                .params("isShelf", isShelf + "")
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(SetMealListActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("修改成功");
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<SetMealListInfo>>get(Urls.GET_SETMEAL_LIST)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("type", "1")
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<SetMealListInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<SetMealListInfo>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<SetMealListInfo>> response) {
                        super.onSuccess(SetMealListActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        if (response.body().code == 0 && response.body().data != null) {
                            setMealInfoList = response.body().data.getList();
                            mAdapter.setNewData(setMealInfoList);
                            mAdapter.notifyDataSetChanged();
                        }
                        mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<SetMealListInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                Intent intent = new Intent(SetMealListActivity.this, EditSetMealActivity.class);
                intent.putExtra(EditSetMealActivity.MERCHANTS_ID, merchantsId);
                startActivityForResult(intent, ADD_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_REQUEST_CODE || requestCode == ADD_REQUEST_CODE) {
                initData();
            }
        }
    }
}
