package com.feitianzhu.huangliwo.me.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.MineCollectionMoneyModel;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/8/31 0031.
 */

public class CollectMoneyActivity extends BaseActivity {
    @BindView(R.id.iv_qrcode)
    ImageView mQrcode;
    @BindView(R.id.tv_localName)
    TextView mLocalName;
    @BindView(R.id.civ_pic)
    CircleImageView mCivPic;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect_money;
    }

    @Override
    protected void initTitle() {
        titleName.setText("我要收款");
    }

    //{"qrCodeUrl":"http://118.190.156.13/merchantqrcode/7.png","expire":30}
    @Override
    protected void initView() {
        requestData();
    }

    private void requestData() {
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    private void setShowData(MineCollectionMoneyModel response) {
        //mQrcode
        //Constant.USER_INFO.headImg;
        String qrUrl = response.getQrCodeUrl();
        if (TextUtils.isEmpty(qrUrl)) {
            qrUrl = "http://www.fu700.cn/?id=7";
        }
        Bitmap bitmap = CodeUtils.createImage(qrUrl, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai)).into(mQrcode);
        Glide.with(mContext).load(response.getMerchantHeadImg()).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai).dontAnimate())
                .into(mCivPic);
        mLocalName.setText(response.getMerchantName());
    }

    @Override
    protected void initData() {

    }
}
