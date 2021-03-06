package com.feitianzhu.huangliwo.vip;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.LocationPost;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.VipGifListInfo;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CustomClassificationView;
import com.feitianzhu.huangliwo.view.CustomImgViewDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.vip.VipGiftDetailActivity.GIFT_ID;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/22 0022 下午 6:53
 */
public class VipActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String MINE_INFO = "mine_info";
    private static final int REQUEST_CODE = 1000;
    private int selectPresentPos = -1;
    private MineInfoModel mTempData = new MineInfoModel();
    private List<VipGifListInfo.VipGifModel> shopGiftList = new ArrayList<>();
    private List<VipGifListInfo.VipPresentsModel> presentsList = new ArrayList<>();
    private VipPresentsAdapter adapter;
    private VipPresentsAdapter2 adapter2;
    private VipGifListInfo presentsModel;
    private double longitude = 116.289189;
    private double latitude = 39.826552;
    private int clsId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.btn_submit)
    TextView btnSumbit;
    @BindView(R.id.cb_protocol)
    CheckBox mCheckBox;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.all_gif)
    TextView tvAllGif;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.presentsTitle)
    TextView presentsTitle;
    @BindView(R.id.ll_gifts_presents)
    LinearLayout llGiftsPresents;
    private String token;
    private String userId;
    private List<MerchantsClassifyModel.ListBean> listBean;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        mTempData = (MineInfoModel) getIntent().getSerializableExtra(MINE_INFO);
        if (mTempData != null) {
            if (mTempData.getAccountType() != 0) {
                btnSumbit.setText("恭喜您已成为会员");
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                btnSumbit.setEnabled(false);
                mCheckBox.setEnabled(false);
            }
        }
        titleName.setText("成为会员");
        mCheckBox.setChecked(true);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox.setBackgroundResource(R.mipmap.vip_selected);
        adapter = new VipPresentsAdapter(shopGiftList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        refreshLayout.setEnableLoadMore(false);
        adapter2 = new VipPresentsAdapter2(presentsList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setNestedScrollingEnabled(false);
        adapter2.notifyDataSetChanged();
        initListener();
        getVipGif(clsId);
    }


    @Override
    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent);
    }

    @OnClick({R.id.left_button, R.id.more_vip, R.id.btn_submit, R.id.tv_protocol, R.id.all_gif, R.id.btnRecord, R.id.tvInstruction})
    @SingleClick()
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btnRecord:
                intent = new Intent(VipActivity.this, GiftRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tvInstruction:
                //折扣比例说明
                String content = "赠品项目不断增加，所有新增赠品项目新老会员均可领取一次，到店消费。";
                new XPopup.Builder(this)
                        .asConfirm("", content, "", "确定", null, null, true)
                        .bindLayout(R.layout.layout_dialog_login) //绑定已有布局
                        .show();
                break;
            case R.id.more_vip:
                if (presentsModel != null) {

                    intent = new Intent(VipActivity.this, VipEquityActivity.class);
                    intent.putExtra("num", presentsModel.totalPrice);
                    startActivity(intent);
                }
                //跳转更多权益
                break;
            case R.id.btn_submit:
               /* UserAuth mAuth = Constant.mUserAuth;
                if (null == mAuth || 0 == mAuth.isRnAuth) {
                    //未实名 审核被拒
                    showDialog("你还没有进行实名认证，请先进行实名认证再进行该操作", true);
                } else if (-1 == mAuth.isRnAuth) {
                    showDialog("审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证", true);
                } else if (mAuth.isRnAuth == 2) {
                    showDialog("你的实名认证正在审核中，请等审核通过后再进行该操作", false);
                } else {
                    //验证用户审核通过
                    intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                    intent.putExtra(VipUpgradeActivity.PARENT_ID, mTempData.getParentId());
                    startActivityForResult(intent, REQUEST_CODE);
                }*/
                if (selectPresentPos == -1) {
                    ToastUtils.show("请选择您想要的赠品");
                } else {
                    intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
                    intent.putExtra(VipUpgradeActivity.PRESENT_ID, presentsList.get(selectPresentPos).giftId);
                    intent.putExtra(VipUpgradeActivity.PARENT_ID, mTempData.getParentId());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.tv_protocol:
                intent = new Intent(VipActivity.this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/huiyuanfuwu.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营会员服务协议");
                startActivity(intent);
                break;
            case R.id.all_gif:
                List<String> strings = new ArrayList<>();
                if (listBean != null && listBean.size() > 0) {
                    for (int i = 0; i < listBean.size(); i++) {
                        strings.add(listBean.get(i).getClsName());
                    }
                    strings.add(0, "全部");
                    new XPopup.Builder(this)
                            .asCustom(new CustomClassificationView(VipActivity.this)
                                    .setData(strings)
                                    .setOnItemClickListener(new CustomClassificationView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            if (position == 0) {
                                                clsId = 0;
                                                tvAllGif.setText("全部");
                                            } else {
                                                clsId = listBean.get(position - 1).getClsId();
                                                tvAllGif.setText(listBean.get(position - 1).getClsName());
                                            }
                                            getVipGif(clsId);
                                        }
                                    }))
                            .show();
                }
                break;
        }

    }

    public void initListener() {

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVipGif(clsId);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @SingleClick()
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (UserInfoUtils.getUserInfo(VipActivity.this).getAccountType() != 0) {
                    if (shopGiftList.get(i).isGet == 0) {
                        shopGiftList.get(i).isGet = 1;
                        //adapter.setNewData(shopGiftList);
                        adapter.notifyItemChanged(i);
                        ToastUtils.show("领取成功");
                        receiveGif(shopGiftList.get(i).giftId, shopGiftList.get(i).merchantId, i);

                    }
                } else {
                    ToastUtils.show("您还不是会员");
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //礼品详情页
                if (shopGiftList.get(i).isGet == 1) {
                    Intent intent = new Intent(VipActivity.this, VipGiftDetailActivity.class);
                    intent.putExtra(GIFT_ID, shopGiftList.get(i).giftId);
                    startActivity(intent);
                }
            }
        });

        adapter2.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (UserInfoUtils.getUserInfo(VipActivity.this).getAccountType() != 0) {
                    ToastUtils.show("您已是会员");
                } else {
                    for (int i = 0; i < presentsList.size(); i++) {
                        if (position == i) {
                            presentsList.get(position).isGet = 1;
                        } else {
                            presentsList.get(i).isGet = 0;
                        }
                    }
                    selectPresentPos = position;
                    adapter2.notifyDataSetChanged();
                }
            }
        });

        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new CustomImgViewDialog(VipActivity.this)
                        .setTitle("")
                        .setData(presentsList.get(position).giftDetailsImg)
                        .show();
            }
        });
    }

    public void receiveGif(int giftId, int merchantId, int position) {
        OkGo.<LzyResponse>get(Urls.GET_GIFT)
                .tag(this)
                .params("giftId", "" + giftId)
                .params("merchantId", "" + merchantId)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(Response<LzyResponse> response) {
                        super.onSuccess(VipActivity.this, response.body().msg, response.body().code);
                       /* if (response.body().code == 0) {
                        }*/
                    }

                    @Override
                    public void onError(Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }


    public void showDialog(String result, boolean isGoAuth) {
        new XPopup.Builder(VipActivity.this)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        if (isGoAuth) {
                            Intent mIntent = new Intent(VipActivity.this, VerificationActivity2.class);
                            startActivity(mIntent);
                        }
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MerchantsClassifyModel>>get(Urls.GET_MERCHANTS_TYPE)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .execute(new JsonCallback<LzyResponse<MerchantsClassifyModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        //super.onSuccess(VipActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            listBean = response.body().data.getList();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MerchantsClassifyModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getVipGif(int clsId) {
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude;
            latitude = myPoint.latitude;
        }
        OkGo.<LzyResponse<VipGifListInfo>>get(Urls.GET_VIP_PRESENT)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("clsId", clsId)
                .params("longitude", longitude + "")
                .params("latitude", latitude + "")
                .execute(new JsonCallback<LzyResponse<VipGifListInfo>>() {
                    @Override
                    public void onStart(Request<LzyResponse<VipGifListInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<VipGifListInfo>> response) {
                        super.onSuccess(VipActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        shopGiftList.clear();
                        presentsList.clear();
                        if (response.body().code == 0 && response.body().data != null) {
                            presentsModel = response.body().data;
                            presentsTitle.setText(presentsModel.title);
                            totalAmount.setText("¥" + response.body().data.totalPrice);
                            if (presentsModel.list != null && presentsModel.list.size() > 0) {
                                shopGiftList = presentsModel.list;
                                adapter.setNewData(shopGiftList);

                            }
                            if (presentsModel.shopGiftList != null && presentsModel.shopGiftList.size() > 0) {
                                presentsList = presentsModel.shopGiftList;
                                adapter2.setNewData(presentsList);
                            }
                            llGiftsPresents.setVisibility(View.VISIBLE);
                        } else {
                            llGiftsPresents.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        goneloadDialog();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<VipGifListInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                btnSumbit.setText("恭喜您已成为会员");
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
                btnSumbit.setEnabled(false);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
                mCheckBox.setEnabled(false);
                MineInfoModel mineInfoModel = UserInfoUtils.getUserInfo(VipActivity.this);
                mineInfoModel.setAccountType(5);
                UserInfoUtils.saveUserInfo(VipActivity.this, mineInfoModel);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onLocationDataSynEvent(LocationPost mMoel) {
        if (!mMoel.isLocationed || null == Constant.mPoint || 0 == Constant.mPoint.longitude) {
            ToastUtils.show("定位失败");
            Constant.mPoint = new MyPoint(116.232934, 39.541997);
            Constant.mCity = "北京";
        } else {
            KLog.e("用户经纬度" + Constant.mPoint);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_protocol) {
            if (!isChecked) {
                btnSumbit.setEnabled(false);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06weixuanzhong4);
                btnSumbit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            } else {
                btnSumbit.setEnabled(true);
                mCheckBox.setBackgroundResource(R.mipmap.f01_06xuanzhong5);
                btnSumbit.setBackgroundResource(R.drawable.shape_fed428_r5);
            }
        }
    }
}
