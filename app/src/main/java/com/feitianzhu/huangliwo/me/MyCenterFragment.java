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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.TestActivity;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.SFFragment;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.adapter.CenterAdapter;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.me.ui.PersonalCenterActivity2;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.me.ui.totalScore.MineQrcodeActivity;
import com.feitianzhu.huangliwo.model.BalanceModel;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.pushshop.PushShopHomeActivity;
import com.feitianzhu.huangliwo.settings.SettingsActivity;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ui.MyOrderActivity2;
import com.feitianzhu.huangliwo.shop.ui.ShoppingCartActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.feitianzhu.huangliwo.view.CustomVerificationView;
import com.feitianzhu.huangliwo.vip.CustomPopup;
import com.feitianzhu.huangliwo.vip.VipActivity;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_USER_AUTH;
import static com.feitianzhu.huangliwo.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 6:28
 */
public class MyCenterFragment extends SFFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.gradeName)
    TextView gradeName;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_show_balance)
    LinearLayout llShowBalance;
    @BindView(R.id.img_show)
    ImageView imgShow;
    @BindView(R.id.toBeReleased_Amount)
    TextView toBeReleasedAmount;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    private String mParam1;
    private String mParam2;
    private CenterAdapter adapter;
    Unbinder unbinder;
    private MineInfoModel mTempData = new MineInfoModel();
    private BalanceModel balanceModel;
    private String token;
    private String userId;
    private String amount = "0.00";
    private String amount2 = "0.00";
    private String amount3 = "0.00";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Integer[] integers = {R.mipmap.b08_08dingdan, R.mipmap.b08_14gouwuche, R.mipmap.b08_11dizhi, R.mipmap.b08_06zhanghu, R.mipmap.b08_12huiyuan,
            R.mipmap.b08_07yinhangka, R.mipmap.b08_10shouchang, R.mipmap.b08_09shangpu, R.mipmap.b08_15fenxiang};

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
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        refreshLayout.setEnableLoadMore(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new CenterAdapter(Arrays.asList(integers));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        isShowBalance = SPUtils.getBoolean(getActivity(), Constant.SP_SHOW_BALANCE, true);
        if (isShowBalance) {
            imgShow.setBackgroundResource(R.mipmap.h01_01dakai);
        } else {
            imgShow.setBackgroundResource(R.mipmap.h01_02guanbi);
        }
        ShopDao.loadUserAuthImpl(getActivity());
        initListener();
        requestData();
        getData();
        return view;
    }

    public void getData() {
        OkHttpUtils.get()
                .url(Urls.GET_USER_MONEY_INFO)
                .addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, BalanceModel.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        balanceModel = (BalanceModel) response;
                        if (balanceModel != null) {
                            amount = String.format(Locale.getDefault(), "%.2f", balanceModel.getWaitRelease());
                            amount2 = String.format(Locale.getDefault(), "%.2f", balanceModel.getTotalAmount());
                            amount3 = String.format(Locale.getDefault(), "%.2f", balanceModel.getBalance());
                            setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, amount, amount2, amount3);
                        } else {
                            setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, "0.00", "0.00", "0.00");
                        }
                    }
                });
    }

    public void requestData() {
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build().execute(new Callback<MineInfoModel>() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                showloadDialog("");
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                goneloadDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                Log.e("wangyan", "onError---->" + e.getMessage());
                ToastUtils.showShortToast(e.getMessage());
            }

            @Override
            public void onResponse(MineInfoModel response, int id) {
                goneloadDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (response != null) {
                    mTempData = response;
                    setShowData(response);
                }
            }
        });
    }

    private void setShowData(MineInfoModel response) {
        Glide.with(mContext)
                .load(response.getHeadImg())
                .apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(civHead);
        nickName.setText(response.getNickName() == null ? "小黄鹂" : response.getNickName());
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
        }
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                switch (position) {
                    case 3: //实名认证
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
                    case 5:
                        //银行卡 //暂不提供银行卡功能
                        intent = new Intent(getActivity(), BindingAccountActivity.class);
                        intent.putExtra(BindingAccountActivity.MINE_INFO, mTempData);
                        startActivity(intent);
                        /*if (!Constant.loadUserAuth) {
                            ToastUtils.showShortToast("正在获取授权信息，稍候进入");
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
                                    ToastUtils.showShortToast(result);
                                }
                            });
                        }*/
                        break;
                    case 0://我的订单
                        // JumpActivity(getContext(), MyOrderActivity.class);
                        intent = new Intent(getActivity(), MyOrderActivity2.class);
                        startActivity(intent);
                        break;
                    case 7: //推店
                        //ShopHelp.veriJumpActivity(getActivity());
                        intent = new Intent(getActivity(), PushShopHomeActivity.class);
                        intent.putExtra(PushShopHomeActivity.MINE_INFO, mTempData);
                        startActivity(intent);
                        break;
                    case 6://我的收藏
                        ToastUtils.showShortToast("敬请期待");
                        /*intent = new Intent(getActivity(), TestActivity.class);
                        startActivity(intent);*/
                        //JumpActivity(getContext(), MineCollectionActivity.class);
                        break;
                    case 2://地址管理
                        intent = new Intent(getActivity(), AddressManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 4://成为会员
                        intent = new Intent(getContext(), VipActivity.class);
                        intent.putExtra(VipActivity.MINE_INFO, mTempData);
                        startActivity(intent);
                        break;
                    case 8: //分享
                        intent = new Intent(getActivity(), MineQrcodeActivity.class);
                        intent.putExtra(MineQrcodeActivity.MINE_DATA, mTempData);
                        startActivity(intent);
                        break;
                    case 1: //购物车
                        //ToastUtils.showShortToast("敬请期待");
                        /*intent = new Intent(getContext(), ShoppingCartActivity.class);
                        startActivity(intent);*/
                        break;
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
                getData();
            }
        });

    }


    private boolean isShowBalance = true;

    @OnClick({R.id.ll_userInfo, R.id.iv_setting, R.id.iv_qrcode, R.id.ll_show_balance, R.id.btn_withdrawal, R.id.detailed_rules})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_userInfo:
                startActivity(new Intent(getActivity(), PersonalCenterActivity2.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.iv_qrcode:
                Intent intent = new Intent(getActivity(), MineQrcodeActivity.class);
                intent.putExtra(MineQrcodeActivity.MINE_DATA, mTempData);
                startActivity(intent);
                break;
            case R.id.ll_show_balance:
                isShowBalance = !isShowBalance;
                if (isShowBalance) {
                    imgShow.setBackgroundResource(R.mipmap.h01_01dakai);
                } else {
                    imgShow.setBackgroundResource(R.mipmap.h01_02guanbi);
                }
                setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, amount, amount2, amount3);
                SPUtils.putBoolean(getActivity(), Constant.SP_SHOW_BALANCE, isShowBalance);
                break;
            case R.id.btn_withdrawal:
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
                    startActivity(intent);
                }
                break;
            case R.id.detailed_rules: //细则
                intent = new Intent(getActivity(), DetailedRulesActivity.class);
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

    public void setSpannableString(TextView view1, TextView view2, TextView view3, String str1, String str2, String str3) {
        view1.setText("");
        view2.setText("");
        view3.setText("");
        String str0 = "¥ ";
        SpannableString span1 = new SpannableString(str0);
        SpannableString span3 = new SpannableString(str0);
        SpannableString span2 = new SpannableString(str1);
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new AbsoluteSizeSpan(12, true), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan, 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span2.setSpan(new AbsoluteSizeSpan(24, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span4.setSpan(new AbsoluteSizeSpan(18, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        if (isShowBalance) {
            view1.append(span1);
            view1.append(span2);
            view2.append(span3);
            view2.append(span4);
            view3.append(span3);
            view3.append(span5);
        } else {
            view1.setText("****");
            view2.setText("****");
            view3.setText("****");
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        switch (event) {
            case EDITOR_INFO:
            case BUY_VIP:
            case BINDING_ALI_ACCOUNT:
                requestData();  //一定要获取token，userId,防止直接从商品列表进入VIP购买
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAuthEvent(AuthEvent mAuth) {
        if (mAuth == AuthEvent.SUCCESS) {
            ShopDao.loadUserAuthImpl(getActivity());  //实名认证后更新用户认证信息
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
