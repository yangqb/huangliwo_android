package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MineQRcodeModel;
import com.feitianzhu.huangliwo.share.ShareUtils;
import com.feitianzhu.huangliwo.utils.GlideUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ShareImageUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class ShareShopActivity extends BaseActivity {
    public static final String GOODS_DATA = "goods_data";
    private BaseGoodsListBean goodsListBean;
    private String str3 = "0.00";
    private String userId;
    private String token;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.shareImg)
    ImageView shareImg;
    @BindView(R.id.tvInstruction)
    TextView tvInstruction;
    @BindView(R.id.tv_rebate)
    TextView tvRebate;
    @BindView(R.id.ll_rebate)
    LinearLayout llRebate;
    @BindView(R.id.civ_pic)
    CircleImageView mCivPic;
    @BindView(R.id.iv_QRCode)
    ImageView mQRcode;
    @BindView(R.id.shareLayout)
    LinearLayout shareLayout;
    private Bitmap bitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_shop;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("分享");
        goodsListBean = (BaseGoodsListBean) getIntent().getSerializableExtra(GOODS_DATA);
        tvAmount.setText("");
        String str2 = "¥ ";
        if (goodsListBean != null) {
            str3 = MathUtils.subZero(String.valueOf(goodsListBean.getPrice()));
            /*Glide.with(this).load(goodsListBean.getGoodsImg())
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .placeholder(R.mipmap.g10_03weijiazai)
                            .error(R.mipmap.g10_03weijiazai))
                    .into(shareImg);*/
            Glide.with(this).load(goodsListBean.getGoodsImg()).apply(new RequestOptions().error(R.mipmap.g10_03weijiazai).placeholder(R.mipmap.g10_03weijiazai)).into(GlideUtils.getImageView(this, goodsListBean.getGoodsImg(), shareImg));
            tvInstruction.setText(goodsListBean.getGoodsName());

            String rebatePv = MathUtils.subZero(String.valueOf(goodsListBean.getRebatePv()));
            if (goodsListBean.getRebatePv() == 0) {
                llRebate.setVisibility(View.GONE);
            }
            tvRebate.setText("奖励¥" + rebatePv);

            SpannableString span2 = new SpannableString(str2);
            SpannableString span3 = new SpannableString(str3);

            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
            span2.setSpan(new AbsoluteSizeSpan(12, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FEA811"));
            span3.setSpan(new AbsoluteSizeSpan(17, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            tvAmount.append(span2);
            tvAmount.append(span3);
        }

        MineInfoModel userInfo = UserInfoUtils.getUserInfo(this);
        Glide.with(mContext).load(userInfo.getHeadImg()).apply(RequestOptions.placeholderOf(R.mipmap.b08_01touxiang).error(R.mipmap.b08_01touxiang).dontAnimate())
                .into(mCivPic);
        tvName.setText(userInfo.getNickName());
        tvUserId.setText("邀请码：" + userInfo.getUserId());

    }

    @OnClick({R.id.left_button, R.id.bt_shared, R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.bt_save:
                ShareImageUtils.saveImg(this, ShareImageUtils.viewToBitmap(shareLayout), "zxing_goods_image"+System.currentTimeMillis());
                // 通知图库更新
                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getPath())));
                break;
            case R.id.bt_shared:
                showShare();
                break;
        }

    }

    private void showShare() {
        Bitmap bitmap = ShareImageUtils.viewToBitmap(shareLayout);
        ShareUtils.shareImg(this, bitmap, "便利大本营");
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<MineQRcodeModel>>get(Urls.BASE_URL + Constant.POST_MINE_QRCODE)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)
                .params("type", "1")
                .execute(new JsonCallback<LzyResponse<MineQRcodeModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onSuccess(ShareShopActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null) {
                            setShowData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<MineQRcodeModel>> response) {
                        super.onError(response);
                    }
                });
    }

    private void setShowData(MineQRcodeModel response) {
        String qrUrl = response.getLink();
        if (TextUtils.isEmpty(qrUrl)) {
            ToastUtils.show("未获取到分享地址");
            return;
        }
        Log.e("Test", "-------->" + qrUrl);
        //bitmap = CodeUtils.createImage(qrUrl, 200, 200, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        bitmap = CodeUtils.createImage(qrUrl, 200, 200, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(mQRcode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
