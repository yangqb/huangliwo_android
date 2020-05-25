package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.adapter.CenterAdapter;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.BalanceModel;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.pushshop.PushShopHomeActivity;
import com.feitianzhu.huangliwo.settings.SettingsActivity;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ui.AfterSaleActivity;
import com.feitianzhu.huangliwo.shop.ui.MyOrderActivity2;
import com.feitianzhu.huangliwo.shop.ui.ShoppingCartActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomVerificationView;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 6:28
 */
public class MyCenterFragment extends SFFragment {
    public static final int REQUEST_CODE = 100;
    @BindView(R.id.recyclerView2)
    RecyclerView mRecyclerView;
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.gradeName)
    TextView gradeName;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.toBeReleased_Amount)
    TextView toBeReleasedAmount;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    @BindView(R.id.withdrawCount)
    TextView withdrawCount;
    @BindView(R.id.tv_wages)
    TextView tvWages;
    private String mParam1;
    private String mParam2;
    private CenterAdapter adapter;
    Unbinder unbinder;
    private MineInfoModel userInfo;
    private BalanceModel balanceModel;
    private String token;
    private String userId;
    private String amount = "0.00";
    private String amount2 = "0.00";
    private String amount3 = "0.00";
    private String wagesAmount = "0.00";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Integer[] integers = {R.mipmap.o05_01gouwuche, R.mipmap.o05_02dizhi, R.mipmap.o05_03renzheng, R.mipmap.o05_04bangding,
            R.mipmap.shoucang, R.mipmap.o05_06tuidian, R.mipmap.o05_tuiguang, R.mipmap.o05_09bagnzhu,
            R.mipmap.o05_kefu};

    public MyCenterFragment() {
    }

    public static MyCenterFragment newInstance(String param1, String param2) {
        MyCenterFragment fragment = new MyCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center2, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        refreshLayout.setEnableLoadMore(false);
        adapter = new CenterAdapter(Arrays.asList(integers));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 5);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRecyclerView.setNestedScrollingEnabled(false);

        getUserInfo();
        ShopDao.loadUserAuthImpl(getActivity());
        initListener();
        getData();
        return view;
    }

    public void getData() {
        OkGo.<LzyResponse<BalanceModel>>get(Urls.GET_USER_MONEY_INFO)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<BalanceModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<BalanceModel>> response) {
                        if (response.body().code == 0) {
                            balanceModel = response.body().data;
                            if (balanceModel != null) {
                                amount = String.format(Locale.getDefault(), "%.2f", balanceModel.getWaitRelease());
                                amount2 = String.format(Locale.getDefault(), "%.2f", balanceModel.getTotalAmount());
                                amount3 = String.format(Locale.getDefault(), "%.2f", balanceModel.getBalance());
                                setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, tvWages, amount, amount2, amount3, wagesAmount);
                                if (balanceModel.getWithdrawCount() != 0) {
                                    withdrawCount.setText(balanceModel.getWithdrawCount() + "笔正在提现中");
                                    withdrawCount.setVisibility(View.VISIBLE);
                                } else {
                                    withdrawCount.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, tvWages, "0.00", "0.00", "0.00", "0.00");
                            }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<BalanceModel>> response) {
                        super.onError(response);
                    }
                });
    }

    public void getUserInfo() {
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        OkGo.<LzyResponse<MineInfoModel>>get(Urls.BASE_URL + POST_MINE_INFO)
                .tag(this)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .execute(new JsonCallback<LzyResponse<MineInfoModel>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<MineInfoModel>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<MineInfoModel>> response) {
                        super.onSuccess(getActivity(), response.body().msg, response.body().code);
                        goneloadDialog();
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                        if (response.body().code == 0 && response.body().data != null) {
                            userInfo = response.body().data;
                            UserInfoUtils.saveUserInfo(getActivity(), userInfo);
                            setShowData(response.body().data);
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<MineInfoModel>> response) {
                        super.onError(response);
                        goneloadDialog();
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });
    }

    private void setShowData(MineInfoModel response) {
        Glide.with(mContext)
                .load(response.getHeadImg())
                .apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(civHead);
        nickName.setText(response.getNickName() == null ? "" : response.getNickName());
        if (response.getAccountType() == 0) {
            gradeName.setText("消费者");
        } else if (response.getAccountType() == 1) {
            gradeName.setText("市代理");
        } else if (response.getAccountType() == 2) {
            gradeName.setText("区代理");
        } else if (response.getAccountType() == 3) {
            gradeName.setText("合伙人");
        } else if (response.getAccountType() == 4) {
            gradeName.setText("超级会员");
        } else if (response.getAccountType() == 5) {
            gradeName.setText("优选会员");
        } else if (response.getAccountType() == 7) {
            gradeName.setText("省代理");
        }
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                switch (position) {
                    case 2: //实名认证
                        UserAuth mAuth = Constant.mUserAuth;
                        if (mAuth != null && mAuth.isRnAuth == -1) {
                            new XPopup.Builder(getActivity())
                                    .enableDrag(false)
                                    .asCustom(new CustomVerificationView(getActivity())
                                            .setContent(mAuth.rnAuthRefuseReason)
                                            .setOnConfirmListener(new CustomVerificationView.OnConfirmListener() {
                                                @Override
                                                public void onConfirm() {
                                                    Intent intent = new Intent(getActivity(), VerificationActivity2.class);
                                                    intent.putExtra(VerificationActivity2.AUTH_INFO, mAuth);
                                                    startActivity(intent);
                                                }
                                            }))
                                    .show();
                        } else {
                            intent = new Intent(getActivity(), VerificationActivity2.class);
                            intent.putExtra(VerificationActivity2.AUTH_INFO, mAuth);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        //银行卡 //暂不提供银行卡功能
                        intent = new Intent(getActivity(), BindingAccountActivity.class);
                        intent.putExtra(BindingAccountActivity.MINE_INFO, userInfo);
                        startActivity(intent);
                        /*if (!Constant.loadUserAuth) {
                            ToastUtils.show("正在获取授权信息，稍候进入");
                            ShopDao.loadUserAuthImpl();
                            return;
                        } else {
                            ShopHelp.veriUserAndPayPwdJumpActivity(getActivity(), new onConnectionFinishLinstener() {
                                @Override
                                public void onSuccess(int code, Object result) {
                                    startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                                }

                                @Override
                                public void onFail(int code, String result) {
                                    ToastUtils.show(result);
                                }
                            });
                        }*/
                        break;
                    case 5: //推店
                        //ShopHelp.veriJumpActivity(getActivity());
                        intent = new Intent(getActivity(), PushShopHomeActivity.class);
                        intent.putExtra(PushShopHomeActivity.MINE_INFO, userInfo);
                        startActivity(intent);
                        break;
                    case 4://我的收藏
                        /*intent = new Intent(getActivity(), TestActivity.class);
                        startActivity(intent);*/
                        //JumpActivity(getContext(), MineCollectionActivity.class);
                        intent = new Intent(getActivity(), MyCollectionActivity.class);
                        startActivity(intent);
                        break;
                    case 1://地址管理
                        intent = new Intent(getActivity(), AddressManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 6: //分享
                        intent = new Intent(getActivity(), RecruitActivity.class);
                        intent.putExtra(RecruitActivity.MINE_DATA, userInfo);
                        startActivity(intent);
                        break;
                    case 0: //购物车
                        intent = new Intent(getContext(), ShoppingCartActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //ToastUtils.show("敬请期待");
                        if (userInfo.getAccountType() == 0) {
                            String content = "您还不是会员无法查看我的团队，请尽快开通！";
                            new XPopup.Builder(getActivity())
                                    .asConfirm("", content, "关闭", "确定", null, null, true)
                                    .bindLayout(R.layout.layout_dialog) //绑定已有布局
                                    .show();
                        } else {
                            intent = new Intent(getContext(), MyTeamActivity.class);
                            startActivity(intent);
                        }

                        break;
                    case 8:
                        intent = new Intent(getActivity(), HelperActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserInfo();
                getData();
            }
        });

    }


    @OnClick({R.id.ll_userInfo, R.id.iv_setting, R.id.btn_withdrawal, R.id.detailed_rules, R.id.withdrawCount, R.id.wages_detailed_rules, R.id.btn_wages_withdrawal,
            R.id.goodsAllOrder, R.id.merchantAllOrder, R.id.upgrade_Vip, R.id.goods_wait_pay, R.id.goods_wait_deliver, R.id.goods_wait_receiving, R.id.goods_wait_comments, R.id.goods_after_sales,
            R.id.merchant_wait_pay, R.id.merchant_wait_use, R.id.merchant_wait_comments, R.id.merchant_after_sales})
    @SingleClick()
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_userInfo:
                startActivity(new Intent(getActivity(), PersonalCenterActivity2.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.btn_withdrawal:
            case R.id.btn_wages_withdrawal:
                UserAuth mAuth = Constant.mUserAuth;
                if (null == mAuth || 0 == mAuth.isRnAuth) {
                    //未实名 审核被拒
                    showDialog("您还没有进行实名认证，请先进行实名认证再进行该操作", false);
                } else if (-1 == mAuth.isRnAuth) {
                    showDialog("您的实名认证审核被拒：请重新进行实名认证", false);
                } else if (mAuth.isRnAuth == 2) {
                    showDialog("您的实名认证正在审核中，请等审核通过后再进行该操作", false);
                } else {
                    //验证用户审核通过
                    intent = new Intent(getActivity(), WithdrawActivity.class);
                    if (balanceModel == null) {
                        intent.putExtra(WithdrawActivity.BALANCE, 0.00);
                    } else {
                        intent.putExtra(WithdrawActivity.BALANCE, balanceModel.getBalance());
                    }
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.detailed_rules: //细则
                intent = new Intent(getActivity(), DetailedRulesActivity.class);
                startActivity(intent);
                break;
            case R.id.withdrawCount:
                intent = new Intent(getActivity(), WithdrawRecordActivity.class);
                intent.putExtra(WithdrawRecordActivity.MERCHANT_ID, -1);
                startActivity(intent);
                break;
            case R.id.wages_detailed_rules:
                intent = new Intent(getActivity(), WagesDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.goodsAllOrder:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 0);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, GoodsOrderInfo.TYPE_All);
                startActivity(intent);
                break;
            case R.id.merchantAllOrder:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 1);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, 0);
                startActivity(intent);
                break;
            case R.id.upgrade_Vip:
                intent = new Intent(getContext(), VipActivity.class);
                intent.putExtra(VipActivity.MINE_INFO, userInfo);
                startActivity(intent);
                break;
            case R.id.goods_wait_pay:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 0);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, GoodsOrderInfo.TYPE_NO_PAY);
                startActivity(intent);
                break;
            case R.id.goods_wait_deliver:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 0);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, GoodsOrderInfo.TYPE_WAIT_DELIVERY);
                startActivity(intent);
                break;
            case R.id.goods_wait_receiving:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 0);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, GoodsOrderInfo.TYPE_WAIT_RECEIVING);
                startActivity(intent);
                break;
            case R.id.goods_wait_comments:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 0);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, GoodsOrderInfo.TYPE_WAIT_COMMENTS);
                startActivity(intent);
                break;
            case R.id.goods_after_sales:
                intent = new Intent(getActivity(), AfterSaleActivity.class);
                intent.putExtra(AfterSaleActivity.ORDER_TYPE, 0);
                startActivity(intent);
                break;
            case R.id.merchant_wait_pay:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 1);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, 1);
                startActivity(intent);
                break;
            case R.id.merchant_wait_use:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 1);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, 2);
                startActivity(intent);
                break;
            case R.id.merchant_wait_comments:
                intent = new Intent(getActivity(), MyOrderActivity2.class);
                intent.putExtra(MyOrderActivity2.ORDER_TYPE, 1);
                intent.putExtra(MyOrderActivity2.ORDER_STATUS, 4);
                startActivity(intent);
                break;
            case R.id.merchant_after_sales:
                intent = new Intent(getActivity(), AfterSaleActivity.class);
                intent.putExtra(AfterSaleActivity.ORDER_TYPE, 1);
                startActivity(intent);
                break;
        }
    }

    public void showDialog(String result, boolean isGoAuth) {
        new XPopup.Builder(getActivity())
                .asConfirm("温馨提示", result, "取消", "确定", null, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    public void setSpannableString(TextView view1, TextView view2, TextView view3, TextView view4, String str1, String str2, String str3, String str4) {
        view1.setText("");
        view2.setText("");
        view3.setText("");
        view4.setText("");
        String str0 = "¥ ";
        SpannableString span1 = new SpannableString(str0);
        SpannableString span2 = new SpannableString(str1);
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        SpannableString span6 = new SpannableString(str4);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(14, true), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span2.setSpan(new AbsoluteSizeSpan(17, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span4.setSpan(new AbsoluteSizeSpan(17, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span5.setSpan(new AbsoluteSizeSpan(17, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span6.setSpan(new AbsoluteSizeSpan(17, true), 0, str4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span6.setSpan(new StyleSpan(Typeface.BOLD), 0, str4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span6.setSpan(colorSpan, 0, str4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view1.append(span1);
        view1.append(span2);
        view2.append(span1);
        view2.append(span4);
        view3.append(span1);
        view3.append(span5);
        view4.append(span1);
        view4.append(span6);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        switch (event) {
            case EDITOR_INFO:
            case BUY_VIP:
            case BINDING_ALI_ACCOUNT:
                getUserInfo();  //一定要获取token，userId,防止直接从商品列表进入VIP购买
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUserInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAuthEvent(AuthEvent mAuth) {
        if (mAuth == AuthEvent.SUCCESS) {
            ShopDao.loadUserAuthImpl(getActivity());  //实名认证后更新用户认证信息
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == REQUEST_CODE) {
                getData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
