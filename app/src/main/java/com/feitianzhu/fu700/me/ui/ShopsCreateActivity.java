package com.feitianzhu.fu700.me.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.me.base.BaseTakePhotoActivity;
import com.feitianzhu.fu700.model.LocationPost;
import com.feitianzhu.fu700.model.MyPoint;
import com.feitianzhu.fu700.model.Province;
import com.feitianzhu.fu700.model.ShopsType;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ui.dialog.ProvinceCallBack;
import com.feitianzhu.fu700.shop.ui.dialog.ProvincehAreaDialog;
import com.feitianzhu.fu700.utils.LocationUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.CustomSelectPhotoView;
import com.lxj.xpopup.XPopup;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * description: 创建商铺
 * autour: dicallc
 */
public class ShopsCreateActivity extends BaseTakePhotoActivity {
    @BindView(R.id.txt_shop_name)
    EditText mTxtShopName;
    @BindView(R.id.ly_huji)
    LinearLayout mLyHuji;
    @BindView(R.id.txt_address)
    EditText mTxtAddress;
    @BindView(R.id.txt_phone)
    EditText mTxtPhone;
    @BindView(R.id.ly_shop_type)
    LinearLayout mLyShopType;
    @BindView(R.id.btn_icon)
    ImageView mBtnIcon;
    @BindView(R.id.btn_cover)
    ImageView mBtnCover;
    @BindView(R.id.edit_intro)
    EditText mEditIntro;
    @BindView(R.id.btn_create)
    TextView mBtnCreate;

    /**
     * 商铺类型
     */
    ArrayList<String> mShopsTypes = new ArrayList<>();
    @BindView(R.id.txt_select_address)
    TextView mTxtSelectAddress;
    @BindView(R.id.txt_shop_types)
    TextView mTxtShopTypes;
    private Province mOnSelectProvince;
    private int photo_type;
    private String photo_file_one;
    private String photo_file_two;
    private List<ShopsType> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shops_create;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onLocationDataSynEvent(LocationPost mMoel) {
        if (!mMoel.isLocationed || null == Constant.mPoint || 0 == Constant.mPoint.longitude) {
            ToastUtils.showShortToast("当前无法定位，选择城市为北京");
            Constant.mPoint = new MyPoint(116.232934, 39.541997);
            Constant.mCity = "北京";
        } else {
            KLog.e("用户经纬度" + Constant.mPoint);
        }
    }

    @Override
    protected void initData() {
        loadShopsType(false);
    }

    private void loadShopsType(final boolean mB) {
        if (mB) showloadDialog("");

        ShopDao.loadShopsType(new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                mList = (List<ShopsType>) result;
                for (ShopsType item : mList) {
                    mShopsTypes.add(item.clsName);
                }
                goneloadDialog();
                if (mB) showTypeDialog(mShopsTypes);
                KLog.e("成功");
            }

            @Override
            public void onFail(int code, String result) {
                KLog.e("失败");
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        switch (photo_type) {
            case 0:
                photo_file_one = result.getImage().getCompressPath();
                Glide.with(mContext).load(photo_file_one).into(mBtnIcon);
                break;
            case 1:
                photo_file_two = result.getImage().getCompressPath();
                Glide.with(mContext).load(photo_file_two).into(mBtnCover);
                break;
        }
        KLog.i("takeSuccess：" + result.getImage().getCompressPath() + "photo_type" + photo_type);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    protected void onWheelSelect(int num, ArrayList<String> mList) {
        mTxtShopTypes.setText(mShopsTypes.get(num - 1));
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        LocationUtils.getInstance().start();
    }

    @OnClick({R.id.btn_icon, R.id.btn_cover, R.id.btn_create, R.id.ly_huji, R.id.ly_shop_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_huji:
                ProvincehAreaDialog branchDialog = ProvincehAreaDialog.newInstance(mContext);
                branchDialog.setAddress("北京市", "北京市");
                branchDialog.setSelectOnListener(new ProvinceCallBack() {
                    @Override
                    public void onWhellFinish(Province province, Province.CityListBean city,
                                              Province.AreaListBean mAreaListBean) {
                        String mProvince_name = province.name;
                        String mCity_name = city.name;
                        String mProvince_id = province.id;
                        String mCity_id = city.id;
                        mOnSelectProvince = new Province(mProvince_name, mCity_name, mCity_id, mProvince_id, mAreaListBean.name, mAreaListBean.id);
                        KLog.e("mProvince" + mProvince_name + "mCity_name" + mCity_name);
                        mTxtSelectAddress.setText(mProvince_name + " " + mCity_name + " " + mAreaListBean.name);
                    }
                });
                branchDialog.show(getSupportFragmentManager());
                break;
            case R.id.btn_icon:
                photo_type = 0;
                showDialog();
                break;
            case R.id.btn_cover:
                photo_type = 1;
                showDialog();
                break;
            case R.id.ly_shop_type:
                //如果获取失败就在这里重新获取
                if (mShopsTypes.size() == 0) {
                    loadShopsType(true);
                } else {
                    showTypeDialog(mShopsTypes);
                }

                break;
            case R.id.btn_create:
                String shopname = mTxtShopName.getText().toString().trim();
                if (TextUtils.isEmpty(shopname)) {
                    ToastUtils.showShortToast("还没有填写商铺名称");
                    return;
                }
                String shopaddress = mTxtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(shopname)) {
                    ToastUtils.showShortToast("还没有填写商铺详细地址");
                    return;
                }
                String shopPhone = mTxtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(shopPhone)) {
                    ToastUtils.showShortToast("还没有填写商铺电话");
                    return;
                }
                String shopIntro = mEditIntro.getText().toString().trim();
                if (TextUtils.isEmpty(shopIntro)) {
                    ToastUtils.showShortToast("还没有填写介绍");
                    return;
                }
                if (TextUtils.isEmpty(photo_file_one)) {
                    ToastUtils.showShortToast("还没有店铺头像");
                    return;
                }
                if (TextUtils.isEmpty(photo_file_two)) {
                    ToastUtils.showShortToast("还没有店铺封面");
                    return;
                }
                if ("请选择地区".equals(mTxtSelectAddress.getText().toString().trim())) {
                    ToastUtils.showShortToast("还没有选择商铺地区");
                    return;
                }
                if ("请输入类型".equals(mTxtShopTypes.getText().toString().trim())) {
                    ToastUtils.showShortToast("还没有选择商铺类型");
                    return;
                }
                showloadDialog("");
                ShopDao.postShopsInfo(new onConnectionFinishLinstener() {
                                          @Override
                                          public void onSuccess(int code, Object result) {
                                              goneloadDialog();
                                              ToastUtils.showShortToast(result.toString());

                                              finish();
                                          }

                                          @Override
                                          public void onFail(int code, String result) {
                                              goneloadDialog();
                                              ToastUtils.showShortToast("创建失败" + result);
                                          }
                                      }, photo_file_one, photo_file_two, shopname, shopaddress, shopPhone, mOnSelectProvince,
                        mList.get(selectIndex - 1).clsId + "", shopIntro, Constant.mPoint);
                break;
        }
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(ShopsCreateActivity.this)
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
