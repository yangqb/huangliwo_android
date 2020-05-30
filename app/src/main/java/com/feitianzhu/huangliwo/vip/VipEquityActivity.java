package com.feitianzhu.huangliwo.vip;

import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.core.base.activity.BaseUiActivity;
import com.feitianzhu.huangliwo.databinding.ActivityVipEquityBinding;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.vip.adapter.VipShowAdapter;
import com.feitianzhu.huangliwo.vip.bean.VipBean;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;

import butterknife.OnClick;

public class VipEquityActivity extends BaseUiActivity {

    private ActivityVipEquityBinding viewDataBinding;
    private double num;

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }


    @Override
    protected View initContentView(LayoutInflater layoutInflater, ViewGroup viewGroup) {

        viewDataBinding = ActivityVipEquityBinding.inflate(layoutInflater, viewGroup, false);
//        DataBindingUtil.inflate()
//        viewDataBinding= DataBindingUtil.setContentView(this, R.layout.activity_vip_equity);
        viewDataBinding.setViewModel(this);
        initTitle("会员中心");
//        viewDataBinding.titleName.setText();
        num = getIntent().getDoubleExtra("num", -1);
        viewDataBinding.num.setText(num + "元起");
        setBackground(R.color.transparent);
        return viewDataBinding.getRoot();
    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return  ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.transparent)
                .statusBarColor(R.color.F3D18A)
                .navigationBarDarkIcon(true);
    }

    @Override
    public void init() {
        if ((UserInfoUtils.getUserInfo(VipEquityActivity.this).getAccountType() != 0)) {
            viewDataBinding.btnSubmit.setText("恭喜您已成为会员");
            viewDataBinding.btnSubmit.setBackgroundResource(R.drawable.shape_e6e5e5_r5);
            viewDataBinding.btnSubmit.setEnabled(false);
            viewDataBinding.btnSubmit.setEnabled(false);
        }
        viewDataBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("请选择您想要的赠品");
                finish();
            }
        });

        viewDataBinding.title1.setText(Html.fromHtml(getResources().getString(R.string.vip_title)));
        viewDataBinding.titl2.setText(Html.fromHtml(getResources().getString(R.string.vip_title2)));
        viewDataBinding.title3.setText(Html.fromHtml(getResources().getString(R.string.vip_title3)));
        ArrayList<VipBean> strings = new ArrayList<>();

        strings.add(new VipBean("品质大牌", R.mipmap.vip_equ1));
        strings.add(new VipBean("工厂价格", R.mipmap.vip_equ3));
        strings.add(new VipBean("行业齐全", R.mipmap.vip_equ2));
        strings.add(new VipBean("一件包邮", R.mipmap.vip_equ4));

        strings.add(new VipBean("自购省钱", R.mipmap.vip_equ5));
        strings.add(new VipBean("分享赚钱", R.mipmap.vip_equ6));
        strings.add(new VipBean("代理特权", R.mipmap.vip_equ7));
        strings.add(new VipBean("福利多多", R.mipmap.vip_equ8));

        strings.add(new VipBean("终身油惠", R.mipmap.vip_equ9));
        strings.add(new VipBean("质量保证", R.mipmap.vip_equ10));
        strings.add(new VipBean("专属售后", R.mipmap.vip_equ11));
        strings.add(new VipBean("贴心服务", R.mipmap.vip_equ12));

        strings.add(new VipBean("运营指导", R.mipmap.vip_equ13));
        strings.add(new VipBean("素材分享", R.mipmap.vip_equ14));
        strings.add(new VipBean("流量支持", R.mipmap.vip_equ15));
        strings.add(new VipBean("全程无忧", R.mipmap.vip_equ16));


        VipShowAdapter vipShowAdapter = new VipShowAdapter(strings);
        viewDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        viewDataBinding.recyclerView.setAdapter(vipShowAdapter);
    }
}
