package com.feitianzhu.huangliwo.me.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MineCollectionMoneyModel;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.hjq.toast.ToastUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

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
        OkHttpUtils.post()//
                .url(Common_HEADER + Constant.POST_MINE_COLLECTION_MONEY)
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)
                .build()
                .execute(new Callback<MineCollectionMoneyModel>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("wangyan", "onError---->" + e.getMessage());
                        ToastUtils.show(e.getMessage());
                    }

                    @Override
                    public void onResponse(MineCollectionMoneyModel response, int id) {
                        setShowData(response);
                    }

                });
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
