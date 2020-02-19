package com.feitianzhu.huangliwo.vip;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.GiftDetailModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

public class VipGiftDetailActivity extends BaseActivity {
    public static final String GIFT_ID = "gift_id";
    private int giftId;
    private String token;
    private String userId;
    private Bitmap bitmap;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.imgViewCode)
    ImageView imgViewCode;
    @BindView(R.id.merchants_name)
    TextView merchantsName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.giftName)
    TextView giftName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvCode)
    TextView tvCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("赠品详情");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);

    }

    @Override
    protected void initData() {
        giftId = getIntent().getIntExtra(GIFT_ID, 0);
        OkGo.<LzyResponse<GiftDetailModel>>get(Urls.GET_GIFT_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("giftId", giftId + "")
                .execute(new JsonCallback<LzyResponse<GiftDetailModel>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<GiftDetailModel>> response) {
                        super.onSuccess(VipGiftDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            GiftDetailModel detailModel = response.body().data;
                            merchantsName.setText(detailModel.merchantName);
                            address.setText(detailModel.address);
                            giftName.setText(detailModel.giftName);
                            tvDesc.setText(detailModel.desc);
                            price.setText("¥" + String.format(Locale.getDefault(), "%.2f", detailModel.price));
                            if (detailModel.isUse == 0) {
                                tvUser.setText("未使用");
                            } else {
                                tvUser.setText("已使用");
                            }
                            createCode(detailModel.url);
                            tvCode.setText(detailModel.num);

                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<GiftDetailModel>> response) {
                        super.onError(response);
                    }
                });

    }

    private void createCode(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast("未获取到二维码");
            return;
        }
        Log.e("Test", "-------->" + url);
        //bitmap = CodeUtils.createImage(url, 400, 400, getLogoBitMap(logoUrl));
        bitmap = CodeUtils.createImage(url, 400, 400, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Glide.with(mContext).load(bytes).apply(RequestOptions.placeholderOf(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imgViewCode);
        }
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
