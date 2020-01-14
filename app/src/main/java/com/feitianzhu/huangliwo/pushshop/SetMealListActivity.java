package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.adapter.SetMealListAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealListInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    private static final int REQUEST_CODE = 1000;
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
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SetMealListActivity.this, SetMealDetailActivity.class);
                intent.putExtra(SetMealDetailActivity.SETMEAL_ID, setMealInfoList.get(position).getSmId());
                startActivityForResult(intent, REQUEST_CODE);
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
    }

    /*
     * 更新套餐的上架下架
     * */
    public void update(int isShelf, int smId) {
        OkHttpUtils.get()
                .url(Urls.UPDATE_SETMEAL_SHELF)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("smId", smId + "")
                .addParams("isShelf", isShelf + "")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        ToastUtils.showShortToast("修改成功");
                    }
                });
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_SETMEAL_LIST)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<SetMealListInfo>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(SetMealListInfo response, int id) {
                        setMealInfoList = response.getList();
                        mAdapter.setNewData(setMealInfoList);
                        mAdapter.notifyDataSetChanged();
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
                startActivity(intent);
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
