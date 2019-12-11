package com.feitianzhu.fu700.pushshop;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.base.BaseTakePhotoActivity;
import com.feitianzhu.fu700.me.ui.MyVerificationActivity;
import com.feitianzhu.fu700.me.ui.PersonalCenterActivity2;
import com.feitianzhu.fu700.view.CustomSelectPhotoView;
import com.itheima.roundedimageview.RoundedImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.socks.library.KLog;

import org.devio.takephoto.model.TResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/10
 * time: 20:29
 * email: 694125155@qq.com
 */
public class EditMerchantsActivity extends BaseTakePhotoActivity {
    private int imgType;
    private String photo1 = "";
    private String photo2 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photo5 = "";
    private String photo6 = "";
    private String photo7 = "";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.imageView1)
    RoundedImageView imageView1;
    @BindView(R.id.imageView2)
    RoundedImageView imageView2;
    @BindView(R.id.imageView3)
    RoundedImageView imageView3;
    @BindView(R.id.imageView4)
    RoundedImageView imageView4;
    @BindView(R.id.imageView5)
    RoundedImageView imageView5;
    @BindView(R.id.imageView6)
    RoundedImageView imageView6;
    @BindView(R.id.imageView7)
    RoundedImageView imageView7;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_merchants;
    }

    @Override
    protected void initView() {
        titleName.setText("新增门店");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.tag8})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tag8:
                //折扣比例说明
                String content = "折扣比例由商家决定折扣部分全额返现给会员消费者，例如：折扣比例为80%，会员消费100元，其中80元直接进入店家账户，随时提现，20元进入会员待释放额度，45天平均返现。";
                new XPopup.Builder(this)
                        .asConfirm("折扣比例说明", content, "", "确定", null, null, true)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.imageView1:
                imgType = 1;
                showDialog();
                break;
            case R.id.imageView2:
                imgType = 2;
                showDialog();
                break;
            case R.id.imageView3:
                imgType = 3;
                showDialog();
                break;
            case R.id.imageView4:
                imgType = 4;
                showDialog();
                break;
            case R.id.imageView5:
                imgType = 5;
                showDialog();
                break;
            case R.id.imageView6:
                imgType = 6;
                showDialog();
                break;
            case R.id.imageView7:
                imgType = 7;
                showDialog();
                break;
        }
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(EditMerchantsActivity.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                TakePhoto(false, 1);
                            }
                        })
                        .setSelectCameraListener(new CustomSelectPhotoView.OnSelectCameraListener() {
                            @Override
                            public void onCameraClick() {
                                TakeCamera(false);
                            }
                        }))
                .show();
    }

    @Override
    protected void onWheelSelect(int num, ArrayList<String> mList) {

    }

    @Override
    public void takeSuccess(TResult result) {
        switch (imgType) {
            case 1:
                photo1 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo1).into(imageView1);
                break;
            case 2:
                photo2 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo2).into(imageView2);
                break;
            case 3:
                photo3 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo3).into(imageView3);
                break;
            case 4:
                photo4 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo4).into(imageView4);
                break;
            case 5:
                photo5 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo5).into(imageView5);
                break;
            case 6:
                photo6 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo6).into(imageView6);
                break;
            case 7:
                photo7 = result.getImage().getCompressPath();
                Glide.with(EditMerchantsActivity.this).load(photo7).into(imageView7);
                break;
        }
        KLog.i("takeSuccess：" + result.getImage().getCompressPath() + "photo_type" + imgType);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
