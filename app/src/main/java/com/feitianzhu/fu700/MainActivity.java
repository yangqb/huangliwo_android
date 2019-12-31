package com.feitianzhu.fu700;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.base.SFActivity;
import com.feitianzhu.fu700.home.HomeFragment2;
import com.feitianzhu.fu700.me.MyCenterFragment;
import com.feitianzhu.fu700.me.ui.CollectMoneyActivity;
import com.feitianzhu.fu700.me.ui.ScannerActivity;
import com.feitianzhu.fu700.message.MessageFragment;
import com.feitianzhu.fu700.model.LocationPost;
import com.feitianzhu.fu700.model.MyPoint;
import com.feitianzhu.fu700.model.UpdateAppModel;
import com.feitianzhu.fu700.payforme.PayForMeActivity;
import com.feitianzhu.fu700.shop.CommodityClassificationFragment;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelp;
import com.feitianzhu.fu700.splash.SplashDao;
import com.feitianzhu.fu700.utils.LocationUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.UpdateAppHttpUtil;
import com.feitianzhu.fu700.utils.VersionManagementUtil;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.socks.library.KLog;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.AppUpdateUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.UAPDATE;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        ButterKnife.bind(this);
        LocationUtils.getInstance().start();
        initView();
        initData();
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

    private void initData() {
        updateDiy();
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
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(MainActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void updateDiy() {
        final String versionName = AppUpdateUtils.getVersionName(this);
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil(this))
                .setUpdateUrl(Common_HEADER + UAPDATE)
                .setPost(false)
                .setThemeColor(0xfffed428)
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
                        boolean constraint = false;
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
                        super.noNewApp(error);
                    }

                });

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
