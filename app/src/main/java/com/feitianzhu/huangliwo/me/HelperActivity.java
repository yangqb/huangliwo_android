package com.feitianzhu.huangliwo.me;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.adapter.CustomerAdapter;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.HelperInfo;
import com.feitianzhu.huangliwo.model.HelperModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.me
 * user: yangqinbo
 * date: 2020/4/21
 * time: 18:02
 * email: 694125155@qq.com
 */
public class HelperActivity extends BaseActivity {
    private String telePhone;
    private List<HelperModel> helpList = new ArrayList<>();
    private CustomerAdapter mAdapter;
    private String token;
    private String userId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_helper;
    }

    @Override
    protected void initView() {
        titleName.setText("帮助中心");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomerAdapter(helpList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                if (helpList.get(position).type == 1) {
                    intent = new Intent(HelperActivity.this, LazyWebActivity.class);
                    intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/bangzhu.html");
                    intent.putExtra(Constant.H5_TITLE, "常见问题");
                    startActivity(intent);
                } else if (helpList.get(position).type == 3) {
                    intent = new Intent(HelperActivity.this, CustomerQrcodeActivity.class);
                    intent.putExtra(CustomerQrcodeActivity.QRCODE_URL, helpList.get(position).qrcode);
                    startActivity(intent);
                } else {
                    telePhone = helpList.get(position).telephone;
                    new XPopup.Builder(HelperActivity.this)
                            .asConfirm("拨打客服电话", telePhone, "关闭", "确定", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    requestPermission();
                                }
                            }, null, false)
                            .bindLayout(R.layout.layout_dialog) //绑定已有布局
                            .show();
                }
            }
        });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<HelperInfo>>get(Urls.GET_HELP_INFO)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<HelperInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HelperInfo>> response) {
                        super.onSuccess(HelperActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null && response.body().data.helpList != null) {
                            helpList = response.body().data.helpList;
                            mAdapter.setNewData(helpList);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<HelperInfo>> response) {
                        super.onError(response);
                    }
                });

    }

    private void requestPermission() {
        XXPermissions.with(HelperActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CALL_PHONE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + telePhone));
                            startActivity(intent);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
