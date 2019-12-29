package com.feitianzhu.fu700.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.base.SFFragment;
import com.feitianzhu.fu700.login.LoginEvent;
import com.feitianzhu.fu700.me.adapter.CenterAdapter;
import com.feitianzhu.fu700.me.ui.PersonalCenterActivity2;
import com.feitianzhu.fu700.me.ui.VerificationActivity2;
import com.feitianzhu.fu700.me.ui.totalScore.MineQrcodeActivity;
import com.feitianzhu.fu700.model.MineInfoModel;
import com.feitianzhu.fu700.settings.SettingsActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ui.MyOrderActivity2;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.CircleImageView;
import com.feitianzhu.fu700.vip.VipActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.POST_MINE_INFO;
import static com.feitianzhu.fu700.common.Constant.USERID;

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
    private String mParam1;
    private String mParam2;
    private CenterAdapter adapter;
    private String userId;
    private String token;
    Unbinder unbinder;
    private MineInfoModel mTempData = new MineInfoModel();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Integer[] integers = {R.mipmap.b08_05yuer, R.mipmap.b08_06zhanghu, R.mipmap.b08_07yinhangka, R.mipmap.b08_08dingdan, R.mipmap.b08_09shangpu,
            R.mipmap.b08_10shouchang, R.mipmap.b08_11dizhi, R.mipmap.b08_12huiyuan, R.mipmap.b08_15fenxiang, R.mipmap.b08_14gouwuche};

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
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new CenterAdapter(Arrays.asList(integers));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        token = SPUtils.getString(getActivity(), Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(getActivity(), Constant.SP_LOGIN_USERID);

        initListener();
        requestData();
        return view;
    }

    public void requestData() {
        OkHttpUtils.get()//
                .url(Common_HEADER + POST_MINE_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)
                .build().execute(new Callback<MineInfoModel>() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("wangyan", "onError---->" + e.getMessage());
                ToastUtils.showShortToast(e.getMessage());
            }

            @Override
            public void onResponse(MineInfoModel response, int id) {
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
                .apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).dontAnimate())
                .into(civHead);
        nickName.setText(response.getNickName() == null ? "小黄鹂" : response.getNickName());
        if (response.getAccountType() == 0) {
            gradeName.setText("普通用户");
        } else if (response.getAccountType() == 1) {
            gradeName.setText("市代理");
        } else if (response.getAccountType() == 2) {
            gradeName.setText("区代理");
        } else if (response.getAccountType() == 3) {
            gradeName.setText("合伙人");
        } else if (response.getAccountType() == 4) {
            gradeName.setText("超级会员");
        } else if (response.getAccountType() == 5) {
            gradeName.setText("普通会员");
        }
    }


    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                switch (position) {
                    case 0: //余额
                        //JumpActivity(getContext(), MyWalletActivity.class);
                        intent = new Intent(getActivity(), BalanceActivity.class);
                        startActivity(intent);
                        break;
                    case 1: //实名认证
                        intent = new Intent(getActivity(), VerificationActivity2.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //银行卡 //暂不提供银行卡功能
                        ToastUtils.showShortToast("敬请期待");
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
                    case 3://我的订单
                        // JumpActivity(getContext(), MyOrderActivity.class);
                        intent = new Intent(getActivity(), MyOrderActivity2.class);
                        startActivity(intent);
                        break;
                    case 4: //推店
                        ToastUtils.showShortToast("敬请期待");
                        //ShopHelp.veriJumpActivity(getActivity());
                       /* intent = new Intent(getActivity(), PushShopHomeActivity.class);
                        startActivity(intent);*/
                        break;
                    case 5://我的收藏
                        ToastUtils.showShortToast("敬请期待");
                        //JumpActivity(getContext(), MineCollectionActivity.class);
                        break;
                    case 6://地址管理
                        intent = new Intent(getActivity(), AddressManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 7://成为会员
                        intent = new Intent(getContext(), VipActivity.class);
                        intent.putExtra(VipActivity.MINE_INFO, mTempData);
                        startActivity(intent);
                        break;
                    case 8: //分享
                        intent = new Intent(getActivity(), MineQrcodeActivity.class);
                        intent.putExtra(MineQrcodeActivity.MINE_DATA, mTempData);
                        startActivity(intent);
                        break;
                    case 9: //购物车
                        ToastUtils.showShortToast("敬请期待");
                       /* intent = new Intent(getContext(), ShoppingCartActivity.class);
                        startActivity(intent);*/
                        break;
                }
            }
        });

    }


    @OnClick({R.id.ll_userInfo, R.id.iv_setting, R.id.iv_qrcode})
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        switch (event) {
            case EDITOR_INFO:
            case BUY_VIP:
                requestData();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
