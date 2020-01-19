package com.feitianzhu.huangliwo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFActivity;
import com.feitianzhu.huangliwo.home.HomeFragment2;
import com.feitianzhu.huangliwo.me.MyCenterFragment;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.message.MessageFragment;
import com.feitianzhu.huangliwo.model.HomePopModel;
import com.feitianzhu.huangliwo.model.LocationPost;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.feitianzhu.huangliwo.shop.CommodityClassificationFragment;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.utils.HProgressDialogUtils;
import com.feitianzhu.huangliwo.utils.LocationUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.UpdateAppHttpUtil;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.VersionManagementUtil;
import com.feitianzhu.huangliwo.view.CustomNerYearPopView;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.socks.library.KLog;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.listener.IUpdateDialogFragmentListener;
import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;
import com.vector.update_app.view.NumberProgressBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.UAPDATE;

public class MainActivity extends SFActivity implements View.OnClickListener, HomeFragment2.CallbackBFragment {

    @BindView(R.id.txt_index)
    TextView mTxtIndex;
    @BindView(R.id.txt_shop)
    TextView mTxtShop;
    /* @BindView(R.id.img_fu)
     ImageView mImgFu;*/
    @BindView(R.id.txt_jiaoliu)
    TextView mTxtJiaoliu;
    @BindView(R.id.txt_me)
    TextView mTxtMe;
    @BindView(R.id.tab_menu)
    LinearLayout mTabMenu;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.img_index)
    ImageView mImgIndex;
    @BindView(R.id.img_shop)
    ImageView mImgShop;
    @BindView(R.id.img_jiaoliu)
    ImageView mImgJiaoliu;
    @BindView(R.id.img_me)
    ImageView mImgMe;
    @BindView(R.id.ly_index)
    LinearLayout mLyIndex;
    @BindView(R.id.ly_shop)
    LinearLayout mLyShop;
    @BindView(R.id.ly_jiaoliu)
    LinearLayout mLyJiaoliu;
    @BindView(R.id.ly_me)
    LinearLayout mLyMe;
    private HomeFragment2 mHomeFragment;
    private CommodityClassificationFragment mShopFragment;
    private MessageFragment mMessageFragment;
    private MyCenterFragment mMeFragment;
    private FragmentTransaction mTransaction;
    private ObjectAnimator animator;
    private int type = 2;
    private String token;
    private String userId;
    private int isShow; //是否弹出活动的框
    boolean constraint = false; //是否强制更新
    private HomePopModel.PopupBean popupBean = new HomePopModel.PopupBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ButterKnife.bind(this);
        LocationUtils.getInstance().start();
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        initView();
        updateDiy();
        mTransaction = getSupportFragmentManager().beginTransaction();
        mHomeFragment = HomeFragment2.newInstance();
        mTransaction.add(R.id.fragment_container, mHomeFragment);
        mTransaction.commit();
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.bg_yellow)
                .init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onLocationDataSynEvent(LocationPost mMoel) {
        KLog.e("onLocationDataSynEvent" + Constant.mPoint);
        if (!mMoel.isLocationed || null == Constant.mPoint || 0 == Constant.mPoint.longitude) {
            ToastUtils.showShortToast("当前无法定位，选择城市为北京");
            Constant.mPoint = new MyPoint(116.232934, 39.541997);
            Constant.mCity = "北京";
        } else {
            KLog.e("用户经纬度" + Constant.mPoint);
        }
    }

    private void showFragment(View v) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(mTransaction);
        switch (v.getId()) {
            case R.id.ly_index:
                selected();
                mTxtIndex.setSelected(true);
                mImgIndex.setSelected(true);
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment2.newInstance();
                    mTransaction.add(R.id.fragment_container, mHomeFragment);
                } else {
                    mTransaction.show(mHomeFragment);
                }
                mTransaction.commit();
                /*ImmersionBar.with(this)
                        .statusBarDarkFont(true, 0.2f)
                        .statusBarColor(R.color.bg_yellow)
                        .init();*/
                break;

            case R.id.ly_shop:
                selected();
                mTxtShop.setSelected(true);
                mImgShop.setSelected(true);
                if (mShopFragment == null) {
                    mShopFragment = CommodityClassificationFragment.newInstance(type, "");
                    mTransaction.add(R.id.fragment_container, mShopFragment);
                } else {
                    mTransaction.show(mShopFragment);
                }
                mTransaction.commit();
                break;
            case R.id.rl_merchants:
                type = 1;
                selected();
                mTxtShop.setSelected(true);
                mImgShop.setSelected(true);
                mShopFragment = CommodityClassificationFragment.newInstance(type, "");
                mTransaction.add(R.id.fragment_container, mShopFragment);
                mTransaction.commit();
                break;
            case R.id.rl_mall:
                type = 2;
                selected();
                mTxtShop.setSelected(true);
                mImgShop.setSelected(true);
                mShopFragment = CommodityClassificationFragment.newInstance(type, "");
                mTransaction.add(R.id.fragment_container, mShopFragment);
                mTransaction.commit();
                break;
            case R.id.ly_jiaoliu:
                selected();
                mTxtJiaoliu.setSelected(true);
                mImgJiaoliu.setSelected(true);
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.newInstance("", "");
                    mTransaction.add(R.id.fragment_container, mMessageFragment);
                } else {
                    mTransaction.show(mMessageFragment);
                }
                mTransaction.commit();
                ToastUtils.showShortToast("敬请期待");
                break;

            case R.id.ly_me:
                selected();
                mTxtMe.setSelected(true);
                mImgMe.setSelected(true);
                if (mMeFragment == null) {
                    mMeFragment = MyCenterFragment.newInstance("", "");
                    mTransaction.add(R.id.fragment_container, mMeFragment);
                } else {
                    mTransaction.show(mMeFragment);
                }
                if (mTransaction != null) {
                    mTransaction.commitAllowingStateLoss();
                }
                break;
           /* case R.id.img_fu:
                setDialog();
                break;*/
        }

    }

    private void initView() {
        mLyIndex.setOnClickListener(this);
        mTxtIndex.setSelected(true);
        mImgIndex.setSelected(true);
        mLyShop.setOnClickListener(this);
        mLyJiaoliu.setOnClickListener(this);
        mLyMe.setOnClickListener(this);
        //mImgFu.setOnClickListener(this);
        //initShakeAnimation();
    }

    /*private void initShakeAnimation() {
        animator = AnimationUtils.tada(mImgFu);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }*/

    @Override
    public void onClick(View v) {
        showFragment(v);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mShopFragment != null) {
            transaction.hide(mShopFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    //重置所有文本的选中状态
    public void selected() {
        mTxtIndex.setSelected(false);
        mImgIndex.setSelected(false);

        mTxtShop.setSelected(false);
        mImgShop.setSelected(false);

        mTxtJiaoliu.setSelected(false);
        mImgJiaoliu.setSelected(false);

        mTxtMe.setSelected(false);
        mImgMe.setSelected(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimation();
    }

    private void stopAnimation() {
        if (null != animator)
            animator.cancel();
    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CAMERA
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

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(MainActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /*
     * 活动弹窗
     * */
    public void getPopData() {
        OkHttpUtils.get()
                .url(Urls.GET_POP_DATA)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback<HomePopModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(HomePopModel response, int id) {
                        if (response != null && response.getPopup() != null) {
                            popupBean = response.getPopup();
                            isShow = response.getPopup().getStatus();
                            if (isShow == 1) {
                                showActivityPop();
                            }
                        }
                    }
                });
    }

    public void updateDiy() {
        final String versionName = AppUpdateUtils.getVersionName(this);
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil(this))
                .setUpdateUrl(Common_HEADER + UAPDATE)
                .setPost(false)
                .setThemeColor(0xfffed428)
                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                    @Override
                    public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                        //用户点击关闭按钮，取消了更新，如果是下载完，用户取消了安装，则可以在 onActivityResult 监听到。
                        getPopData();
                    }
                })
                .build()
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        UpdateAppModel updateAppModel = new Gson().fromJson(json, UpdateAppModel.class);
                        String update = "No";
                        if (VersionManagementUtil.VersionComparison(updateAppModel.versionName + "", versionName) == 1) {
                            update = "Yes";
                            if ("1".equals(updateAppModel.isForceUpdate)) {
                                constraint = true;
                            } else {
                                constraint = false;
                            }
                        } else {
                            update = "No";
                        }

                        updateAppBean
                                //（必须）是否更新Yes,No
                                .setUpdate(update)
                                //（必须）新版本号，
                                .setNewVersion(updateAppModel.versionName + "")
//                                .setNewVer、sion("1.1.0"+ "")
                                //（必须）下载地址
                                .setApkFileUrl(updateAppModel.downloadUrl)
//                                    .setUpdateLog("测试")
                                .setUpdateLog(updateAppModel.updateDesc)
//                                    .setUpdateLog("")
                                .setTargetSize(updateAppModel.packSize + "Mb")
                                //是否强制更新，可以不设置
                                .setConstraint(constraint);
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        /*
                        自定义对话框
                        * */
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                showloadDialog("");
//                CProgressDialogUtils.showProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                CProgressDialogUtils.cancelProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    protected void noNewApp(String error) {
                        getPopData();
                    }
                });
    }

    public void showActivityPop() {
        new XPopup.Builder(MainActivity.this)
                .enableDrag(false)
                .asCustom(new CustomNerYearPopView(MainActivity.this)
                        .setImgUrl(popupBean)
                        .setOnConfirmClickListener(new CustomNerYearPopView.OnConfirmClickListener() {
                            @Override
                            public void onConfirm() {
                                Intent intent = new Intent(MainActivity.this, NewYearShoppingActivity.class);
                                startActivity(intent);
                            }
                        })).show();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void skipToCommodityFragment(int type, View view) {
        this.type = type;
        showFragment(view);
    }
}
