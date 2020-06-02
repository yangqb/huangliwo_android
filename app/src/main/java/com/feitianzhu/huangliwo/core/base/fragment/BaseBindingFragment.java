package com.feitianzhu.huangliwo.core.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.fragment.SFFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bch on 2020.5.30.
 */
public abstract class BaseBindingFragment extends SFFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initBindingView(inflater, container);
        init();
        return view;
    }

    protected abstract void init();

    protected abstract View initBindingView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);


}
