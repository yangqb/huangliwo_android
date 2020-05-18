package com.feitianzhu.huangliwo.me;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.adapter.UnionlevelAdapter2;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.me.helper.DialogHelper;
import com.feitianzhu.huangliwo.model.FuFriendModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 5:50
 */
public class UnionlevelActivity2 extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private List<FuFriendModel.ListEntity> mLists = new ArrayList<>();
    private int index = 1;
    private boolean hasNextPage = true;
    private View mEmptyView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private UnionlevelAdapter2 adapter2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unionlevel;
    }

    @Override
    protected void initView() {
        mEmptyView = View.inflate(this, R.layout.view_common_nodata, null);
        titleName.setText("联盟人数");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new UnionlevelAdapter2(mLists);
        adapter2.setEmptyView(mEmptyView);
        adapter2.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        initListener();
    }

    private String telNum = "";

    public void initListener() {
        adapter2.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                telNum = adapter2.getData().get(position).phone;
                new DialogHelper(UnionlevelActivity2.this).init(DialogHelper.DIALOG_ONE, view).buildDialog(adapter2.getData().get(position).phone, "呼叫", new DialogHelper.OnButtonClickListener<String>() {
                    @Override
                    public void onButtonClick(View v, String result, View clickView) {
                        telNum = result;
                        requestPermission();
                    }
                });
            }
        });
    }

    private void requestPermission() {
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    private void requestData(final boolean isLoadMore) {
    }

    @Override
    protected void initData() {
        requestData(false);
    }

    @Override
    public void onLoadMoreRequested() {
        if (!hasNextPage) {
            adapter2.loadMoreEnd();
        } else {
            index += 1;
            requestData(true);
        }
    }
}
