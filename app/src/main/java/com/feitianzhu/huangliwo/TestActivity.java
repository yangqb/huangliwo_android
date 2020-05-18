package com.feitianzhu.huangliwo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo
 * user: yangqinbo
 * date: 2020/1/6
 * time: 9:39
 * email: 694125155@qq.com
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.edit)
    EditText editText;
    @BindView(R.id.password)
    TextView tvPass;
    @BindView(R.id.fuZhi)
    TextView fuZhi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.submit, R.id.fuZhi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                String pass = editText.getText().toString().trim();
                String newPassword2 = EncryptUtils.encodePassword(pass);
                Log.e("password======", newPassword2);
                tvPass.setText(newPassword2);
                break;
            case R.id.fuZhi:
                //获取剪贴版
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                //第一个参数只是一个标记，随便传入。
                //第二个参数是要复制到剪贴版的内容
                ClipData clip = ClipData.newPlainText("simple text", tvPass.getText().toString().trim());
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                ToastUtils.show("已复制");
                break;
        }

    }

    @Override
    protected void initData() {

    }
}
