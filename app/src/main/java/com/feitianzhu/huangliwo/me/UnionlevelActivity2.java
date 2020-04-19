package com.feitianzhu.huangliwo.me;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.huangliwo.me.adapter.UnionlevelAdapter2;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.helper.DialogHelper;
import com.feitianzhu.huangliwo.model.FuFriendModel;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.hjq.toast.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .callback(
                        new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                                AndPermission.rationaleDialog(App.getAppContext(), rationale).show();
                            }
                        }
                )
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + telNum));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(UnionlevelActivity2.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    private void requestData(final boolean isLoadMore) {
        ShopDao.loadFUFriend(index + "", new onNetFinishLinstenerT<FuFriendModel>() {
            @Override
            public void onSuccess(int code, FuFriendModel result) {
                if (result != null && result.pager != null) {
                    hasNextPage = result.pager.hasNextPage;
                }
                if (result != null && result.list != null) {
                    adapter2.addData(result.list);
                }
                if (isLoadMore) adapter2.loadMoreComplete();
            }

            @Override
            public void onFail(int code, String result) {
                if (isLoadMore)
                    adapter2.loadMoreFail();
                ToastUtils.show(result);
            }
        });
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
