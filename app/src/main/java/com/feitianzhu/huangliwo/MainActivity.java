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
import com.feitianzhu.huangliwo.home.HomeFragment;
import com.feitianzhu.huangliwo.home.HomeFragment2;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.MyCenterFragment;
import com.feitianzhu.huangliwo.message.MessageFragment;
import com.feitianzhu.huangliwo.model.HomePopModel;
import com.feitianzhu.huangliwo.model.LocationPost;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.feitianzhu.huangliwo.settings.ChangeLoginPassword;
import com.feitianzhu.huangliwo.shop.CommodityClassificationFragment;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.utils.LocationUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UpdateAppHttpUtil;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.VersionManagementUtil;
import com.feitianzhu.huangliwo.view.CustomNerYearPopView;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.socks.library.KLog;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.listener.IUpdateDialogFragmentListener;
import com.vector.update_app.utils.AppUpdateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onLocationDataSynEvent(LocationPost mMoel) {
        KLog.e("onLocationDataSynEvent" + Constant.mPoint);
        if (!mMoel.isLocationed || null == Constant.mPoint || 0 == Constant.mPoint.longitude) {
            ToastUtils.show("当前无法定位，选择城市为北京");
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

    private void stopAnimation() {
        if (null != animator)
            animator.cancel();
    }

    /*
     * 活动弹窗
     * */
    public void getPopData() {
        OkGo.<LzyResponse<HomePopModel>>get(Urls.GET_POP_DATA)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<HomePopModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HomePopModel>> response) {
                        super.onSuccess(MainActivity.this, "", response.body().code);
                        if (response.body().data != null && response.body().data.getPopup() != null) {
                            popupBean = response.body().data.getPopup();
                            isShow = response.body().data.getPopup().getStatus();
                            if (isShow == 1) {
                                showActivityPop();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<HomePopModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void updateDiy() {
        final String versionName = AppUpdateUtils.getVersionName(this);
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil(this))
                .setUpdateUrl(Urls.BASE_URL + UAPDATE)
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
                        if (VersionManagementUtil.VersionComparison(updateAppModel.data.versionName + "", versionName) == 1) {
                            update = "Yes";
                            if ("1".equals(updateAppModel.data.isForceUpdate)) {
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
                                .setNewVersion(updateAppModel.data.versionName + "")
//                                .setNewVer、sion("1.1.0"+ "")
                                //（必须）下载地址
                                .setApkFileUrl(updateAppModel.data.downloadUrl)
//                                    .setUpdateLog("测试")
                                .setUpdateLog(updateAppModel.data.updateDesc)
//                                    .setUpdateLog("")
                                .setTargetSize(updateAppModel.data.packSize + "Mb")
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

    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;
    /**
     * 两次点击的间隔时间
     */
    private static final int QUIT_INTERVAL = 3000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                ToastUtils.show("再按一次退出程序");
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void skipToCommodityFragment(int type, View view) {
        this.type = type;
        showFragment(view);
    }

}
