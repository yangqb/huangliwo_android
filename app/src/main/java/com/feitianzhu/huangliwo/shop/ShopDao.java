package com.feitianzhu.huangliwo.shop;

import android.content.Context;
import android.text.TextUtils;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.AliPayCallBack;
import com.feitianzhu.huangliwo.common.impl.BaseCallBackObject;
import com.feitianzhu.huangliwo.common.impl.BaseCallBackT;
import com.feitianzhu.huangliwo.common.impl.MyInfomationCallback;
import com.feitianzhu.huangliwo.common.impl.NoDataCallBack;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.huangliwo.model.FuFriendModel;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.model.RecommndShopModel;
import com.feitianzhu.huangliwo.model.ServeOrderModel;
import com.feitianzhu.huangliwo.model.ShopOrderModel;
import com.feitianzhu.huangliwo.model.ShopType;
import com.feitianzhu.huangliwo.model.ShopVeriModel;
import com.feitianzhu.huangliwo.model.ShopsEvali;
import com.feitianzhu.huangliwo.model.ShopsIndex;
import com.feitianzhu.huangliwo.model.ShopsInfo;
import com.feitianzhu.huangliwo.model.ShopsNearby;
import com.feitianzhu.huangliwo.model.ShopsService;
import com.feitianzhu.huangliwo.model.ShopsType;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.model.UserInformation;
import com.feitianzhu.huangliwo.model.UserVeriModel;
import com.feitianzhu.huangliwo.model.WXModel;
import com.feitianzhu.huangliwo.model.WalletModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.AMOUNT;
import static com.feitianzhu.huangliwo.common.Constant.AREAID;
import static com.feitianzhu.huangliwo.common.Constant.AREANAME;
import static com.feitianzhu.huangliwo.common.Constant.AVATAR;
import static com.feitianzhu.huangliwo.common.Constant.BUSILICENSENAME;
import static com.feitianzhu.huangliwo.common.Constant.BUSINATURE;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFNO;
import static com.feitianzhu.huangliwo.common.Constant.CERTIFTYPE;
import static com.feitianzhu.huangliwo.common.Constant.CHANNEL;
import static com.feitianzhu.huangliwo.common.Constant.CITYID;
import static com.feitianzhu.huangliwo.common.Constant.CITYNAME;
import static com.feitianzhu.huangliwo.common.Constant.CLSID;
import static com.feitianzhu.huangliwo.common.Constant.COLLECTID;
import static com.feitianzhu.huangliwo.common.Constant.CONSUMEAMOUNT;
import static com.feitianzhu.huangliwo.common.Constant.CONTENT;
import static com.feitianzhu.huangliwo.common.Constant.Common_HEADER;
import static com.feitianzhu.huangliwo.common.Constant.DELETE_COLLECT;
import static com.feitianzhu.huangliwo.common.Constant.DELETE_SERVICE;
import static com.feitianzhu.huangliwo.common.Constant.DTLADDR;
import static com.feitianzhu.huangliwo.common.Constant.EDIT_SHOPS_INFO;
import static com.feitianzhu.huangliwo.common.Constant.FILES;
import static com.feitianzhu.huangliwo.common.Constant.FailCode;
import static com.feitianzhu.huangliwo.common.Constant.IDVALUE;
import static com.feitianzhu.huangliwo.common.Constant.INTRODUCE;
import static com.feitianzhu.huangliwo.common.Constant.JUBAO;
import static com.feitianzhu.huangliwo.common.Constant.LATITUDE;
import static com.feitianzhu.huangliwo.common.Constant.LEGALPERSON;
import static com.feitianzhu.huangliwo.common.Constant.LOADER_USERINFO;
import static com.feitianzhu.huangliwo.common.Constant.LOADER_VERI_SHOPS_INFO;
import static com.feitianzhu.huangliwo.common.Constant.LOADER_VERI_USER_INFO;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_FU_FRIEND;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_NEARBY_SHOPS;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_RECMOOND_SHOP_LIST;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_EVALUTE;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_INDEX;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_INFO;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_SEARCH_DATA;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_SERVICES;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_TYPE;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_SHOPS_WALLET;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_USER_AUTH;
import static com.feitianzhu.huangliwo.common.Constant.LONGITUDE;
import static com.feitianzhu.huangliwo.common.Constant.MERCHANTID;
import static com.feitianzhu.huangliwo.common.Constant.MERCHANTNAME;
import static com.feitianzhu.huangliwo.common.Constant.ORDERNO;
import static com.feitianzhu.huangliwo.common.Constant.PAGEINDEX;
import static com.feitianzhu.huangliwo.common.Constant.PAGEROWS;
import static com.feitianzhu.huangliwo.common.Constant.PAYCHANNEL;
import static com.feitianzhu.huangliwo.common.Constant.PAYPASS;
import static com.feitianzhu.huangliwo.common.Constant.POST_COLLECT;
import static com.feitianzhu.huangliwo.common.Constant.POST_REALAUTH;
import static com.feitianzhu.huangliwo.common.Constant.POST_SHOPS_INFO;
import static com.feitianzhu.huangliwo.common.Constant.POST_SHOPS_TYPE;
import static com.feitianzhu.huangliwo.common.Constant.POST_SHOPS_VERI;
import static com.feitianzhu.huangliwo.common.Constant.POST_SHOP_PAY;
import static com.feitianzhu.huangliwo.common.Constant.PROVINCEID;
import static com.feitianzhu.huangliwo.common.Constant.PROVINCENAME;
import static com.feitianzhu.huangliwo.common.Constant.REALNAME;
import static com.feitianzhu.huangliwo.common.Constant.REGISTENO;
import static com.feitianzhu.huangliwo.common.Constant.REPORTCONTENT;
import static com.feitianzhu.huangliwo.common.Constant.SERVICEID;
import static com.feitianzhu.huangliwo.common.Constant.SERVICEPHONE;
import static com.feitianzhu.huangliwo.common.Constant.SERVICE_ORDERS;
import static com.feitianzhu.huangliwo.common.Constant.SHOP_EVA;
import static com.feitianzhu.huangliwo.common.Constant.SHOP_ORDERS;
import static com.feitianzhu.huangliwo.common.Constant.STAR;
import static com.feitianzhu.huangliwo.common.Constant.SuccessCode;
import static com.feitianzhu.huangliwo.common.Constant.TYPE;
import static com.feitianzhu.huangliwo.common.Constant.UAPDATE;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static com.feitianzhu.huangliwo.common.Constant.VALUEID;
import static com.feitianzhu.huangliwo.common.Constant.WX_PAY_URL;

/**
 * Created by dicallc on 2017/9/4 0004.
 */

public class ShopDao {
    /**
     * 提交商户验证数据
     */
    public static void PostDataToVeriShop(Context context, String mPhoto_file_four, String mYinyeCard,
                                          String mRegisterNum, String Faren, int mSelectIndex,
                                          final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        String BUSINATUREs;
        if (mSelectIndex > 5) {
            BUSINATUREs = "10";
        } else {
            BUSINATUREs = mSelectIndex + "";
        }
        OkHttpUtils.post()//
                .addFile("certifFile", "01.png", new File(mPhoto_file_four))//
                .url(Common_HEADER + POST_SHOPS_VERI).addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(BUSILICENSENAME, mYinyeCard)//
                .addParams(REGISTENO, mRegisterNum)//
                .addParams(LEGALPERSON, Faren)//
                .addParams(BUSINATURE, BUSINATUREs)//
                .build()//
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        KLog.e(mData);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if ("数据为空".equals(e.getMessage())) {
                            mLinstener.onSuccess(1, "成功");
                            ToastUtils.showShortToast("商户验证成功");
                        } else {
                            mLinstener.onFail(FailCode, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (response == null) {
                            mLinstener.onFail(FailCode, "response is null!");
                            return;
                        }
                        KLog.i("response:%s", response.toString());

                        mLinstener.onSuccess(0, response);
                    }
                });
    }

    /**
     * 提交个人验证
     */
    public static void PostDataToVeriUser(Context context, String mPhoto_file_one, String mPhoto_file_two,
                                          String mName, String mId_num, int mSelectIndex, Province mOnSelectProvince,
                                          final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        String BUSINATUREs;
        if (mSelectIndex > 2) {
            BUSINATUREs = "10";
        } else {
            BUSINATUREs = mSelectIndex + "";
        }
        OkHttpUtils.post()//
                .addFile("certifFile", "01.png", new File(mPhoto_file_one))//
                .addFile("certifFile", "02.png", new File(mPhoto_file_two))//
                .url(Common_HEADER + POST_REALAUTH).addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(REALNAME, mName)//
                .addParams(CERTIFTYPE, BUSINATUREs)//
                .addParams(CERTIFNO, mId_num)//
                .addParams(PROVINCEID, mOnSelectProvince.id)//
                .addParams(PROVINCENAME, mOnSelectProvince.name)//
                .addParams(CITYID, mOnSelectProvince.pid)//
                .addParams(CITYNAME, mOnSelectProvince.city_name)//
                .build()//
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        KLog.e(mData);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if ("数据为空".equals(e.getMessage())) {
                            mLinstener.onSuccess(1, "成功");
                        } else {
                            mLinstener.onFail(FailCode, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (response == null) {
                            mLinstener.onFail(FailCode, "response is null!");
                            return;
                        }
                        KLog.i("response:%s", response.toString());

                        mLinstener.onSuccess(0, response);
                    }
                });
    }

    /**
     * 获取个人信息
     */
    public static void loadUserInfo(final onConnectionFinishLinstener mLinstener) {
        OkHttpUtils.get()
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .url(Common_HEADER + LOADER_USERINFO)
                .build()
                .execute(new MyInfomationCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLinstener.onFail(1, e.getMessage());
                    }

                    @Override
                    public void onResponse(UserInformation response, int id) {
                        mLinstener.onSuccess(1, response);
                    }
                });
    }

    public static void loadUserVeriInfo(Context context, final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.get()
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .url(Common_HEADER + LOADER_VERI_USER_INFO)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        UserVeriModel user = new Gson().fromJson(mData, UserVeriModel.class);
                        return user;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLinstener.onFail(FailCode, e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        mLinstener.onSuccess(SuccessCode, response);
                    }
                });
    }

    public static void loadShopsVeriInfo(final onConnectionFinishLinstener mLinstener) {
        //ShopVeriModel mUserVeriModel = new ShopVeriModel(-1);
        //mLinstener.onSuccess(SuccessCode, mUserVeriModel);
        OkHttpUtils.get()
                .addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)
                .addParams(USERID, Constant.LOGIN_USERID)
                .url(Common_HEADER + LOADER_VERI_SHOPS_INFO)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        ShopVeriModel user = new Gson().fromJson(mData, ShopVeriModel.class);
                        return user;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLinstener.onFail(FailCode, e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        mLinstener.onSuccess(SuccessCode, response);
                    }
                });
    }

    public static void postShopsInfo(Context context, final onConnectionFinishLinstener mLinstener, String avatar,
                                     String files, String merchantname, String dtladdr, String servicephone,
                                     Province mOnSelectProvince, String clsid, String introduce, MyPoint mPoint) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.post()//
                .addFile(AVATAR, "01.png", new File(avatar))//
                .addFile(FILES, "02.png", new File(files))//
                .url(Common_HEADER + POST_SHOPS_INFO).addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(MERCHANTNAME, merchantname)//
                .addParams(DTLADDR, dtladdr)//
                .addParams(SERVICEPHONE, servicephone)//
                .addParams(PROVINCEID, mOnSelectProvince.id)//
                .addParams(PROVINCENAME, mOnSelectProvince.name)//
                .addParams(CITYID, mOnSelectProvince.pid)//
                .addParams(CITYNAME, mOnSelectProvince.city_name)//
                .addParams(CLSID, clsid)//
                .addParams(AREAID, mOnSelectProvince.areaId)//
                .addParams(AREANAME, mOnSelectProvince.areaName)//
                .addParams(INTRODUCE, introduce)//
                .addParams(LONGITUDE, mPoint.longitude + "")//
                .addParams(LATITUDE, mPoint.latitude + "")//
                .build()//
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if ("数据为空".equals(e.getMessage())) {
                            mLinstener.onSuccess(SuccessCode, "商铺创建成功");
                        } else {
                            mLinstener.onFail(FailCode, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (response == null) {
                            mLinstener.onFail(FailCode, "response is null!");
                            return;
                        }
                        KLog.i("response:%s", response.toString());

                        mLinstener.onSuccess(0, response);
                    }
                });
    }

    /**
     * 编辑商铺信息
     */
    public static void updateShopsInfo(Context context, final onConnectionFinishLinstener mLinstener, String avatar,
                                       String files, String merchantname, String dtladdr, String servicephone,
                                       Province mOnSelectProvince, int clsid, String introduce, List<ShopsType> mList,
                                       int mMerchantId) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        PostFormBuilder mPost = OkHttpUtils.post();
        if (!TextUtils.isEmpty(avatar)) {
            mPost.addFile(AVATAR, "01.png", new File(avatar));
        }
        if (!TextUtils.isEmpty(files)) {
            mPost.addFile(FILES, "02.png", new File(files));
        }
        if (clsid != 0) {
            mPost.addParams(CLSID, mList.get(clsid - 1).clsId + "");
        }
        if (null != mOnSelectProvince) {
            mPost.addParams(PROVINCEID, mOnSelectProvince.id)//
                    .addParams(PROVINCENAME, mOnSelectProvince.name)//
                    .addParams(CITYID, mOnSelectProvince.pid)//
                    .addParams(CITYNAME, mOnSelectProvince.city_name)
                    .addParams(AREAID, mOnSelectProvince.areaId)//
                    .addParams(AREANAME, mOnSelectProvince.areaName);
        }
        mPost.setMultipart(true)
                .url(Common_HEADER + EDIT_SHOPS_INFO)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(MERCHANTNAME, merchantname)//
                .addParams(MERCHANTID, mMerchantId + "")//
                .addParams(DTLADDR, dtladdr)//
                .addParams(SERVICEPHONE, servicephone)//
                .addParams(INTRODUCE, introduce)//
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if ("数据为空".equals(e.getMessage())) {
                            mLinstener.onSuccess(SuccessCode, "成功");
                            ToastUtils.showShortToast("商铺编辑成功");
                        } else {
                            mLinstener.onFail(FailCode, e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if (response == null) {
                            mLinstener.onFail(FailCode, "response is null!");
                            return;
                        }
                        KLog.i("response:%s", response.toString());

                        mLinstener.onSuccess(0, response);
                    }
                });
    }

    public static void loadShopsType(Context context, final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.get()
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .url(Common_HEADER + POST_SHOPS_TYPE)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id)
                            throws Exception {
                        Type type = new TypeToken<List<ShopsType>>() {
                        }.getType();
                        List<ShopsType> list = new Gson().fromJson(mData, type);
                        return list;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLinstener.onFail(FailCode, e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {

                        mLinstener.onSuccess(SuccessCode, response);
                    }
                });
    }

    public static void loadShopsInfo(Context context, String merchantid,
                                     final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        PostFormBuilder builder = OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_SHOPS_INFO);
        if (!TextUtils.isEmpty(merchantid)) {
            builder.addParams(MERCHANTID, merchantid);
        }
        builder.addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(String mData, Response response, int id)
                    throws Exception {
                ShopsInfo user = new Gson().fromJson(mData, ShopsInfo.class);
                return user;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                mLinstener.onFail(FailCode, e.getMessage());
            }

            @Override
            public void onResponse(Object response, int id) {
                mLinstener.onSuccess(SuccessCode, response);
            }
        });//
    }

    public static void PostCollect(Context context, int type, int id, final onNetFinishLinstenerT mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.post()//
                .url(Common_HEADER + POST_COLLECT).addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(TYPE, type + "")//
                .addParams(IDVALUE, id + "")//
                .build().execute(new BaseCallBackT(mLinstener) {
            @Override
            public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                return mData;
            }
        });
    }

    public static void DeleteCollect(Context context, String id, final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.get()//
                .url(Common_HEADER + DELETE_COLLECT).addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams(COLLECTID, id + "")//
                .build().execute(new NoDataCallBack(mLinstener));
    }

    public static void LoadShopType(final onNetFinishLinstenerT mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + LOAD_SHOPS_TYPE).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .build().execute(new BaseCallBackT(mLinstener) {
            @Override
            public List<ShopType> parseNetworkResponse(String mData, Response response, int id)
                    throws Exception {
                Type type = new TypeToken<List<ShopType>>() {
                }.getType();
                List<ShopType> list = new Gson().fromJson(mData, type);
                return list;
            }
        });
    }

    public static void LoadReCommondShop(String type, int index, String pagerows,
                                         final onNetFinishLinstenerT mLinstener) {
        OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_RECMOOND_SHOP_LIST).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(TYPE, type)//
                .addParams(PAGEINDEX, index + "")//
                .addParams(PAGEROWS, pagerows)//
                .build().execute(new BaseCallBackT(mLinstener) {
            @Override
            public RecommndShopModel parseNetworkResponse(String mData, Response response, int id)
                    throws Exception {
                RecommndShopModel user = new Gson().fromJson(mData, RecommndShopModel.class);
                return user;
            }
        });
    }

    public static void LoadShopsIndex(String provinceId, String cityId,
                                      final onNetFinishLinstenerT<ShopsIndex> mLinstener) {
        GetBuilder mBuilder = OkHttpUtils.get()//
                .url(Common_HEADER + LOAD_SHOPS_INDEX);
        if (!TextUtils.isEmpty(provinceId)) {
            mBuilder.addParams(PROVINCEID, provinceId).addParams(CITYID, cityId);
        }
        mBuilder.addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(LONGITUDE, Constant.mPoint.longitude + "")//
                .addParams(LATITUDE, Constant.mPoint.latitude + "")//
                .build().execute(new BaseCallBackObject(mLinstener, ShopsIndex.class) {
        });
    }

    public static void LoadNearbyShops(int page, String provinceId, String cityId, String clsId,
                                       final onNetFinishLinstenerT<ShopsNearby> mLinstener) {
        GetBuilder builder = OkHttpUtils.get()//
                .url(Common_HEADER + LOAD_NEARBY_SHOPS);
        if (!TextUtils.isEmpty(provinceId)) {
            builder.addParams(PROVINCEID, provinceId)//
                    .addParams(CITYID, cityId);
        }
        if (!TextUtils.isEmpty(clsId)) {
            builder.addParams(CLSID, clsId);
        }
        builder.addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(LONGITUDE, Constant.mPoint.longitude + "")//
                .addParams(LATITUDE, Constant.mPoint.latitude + "")//
                .addParams(PAGEINDEX, page + "")//
                .addParams(PAGEROWS, 10 + "")//
                .build().execute(new BaseCallBackObject(mLinstener, ShopsNearby.class) {
        });
    }

    public static void LoadShopsSearchData(int page, String result,
                                           final onNetFinishLinstenerT<RecommndShopModel> mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + LOAD_SHOPS_SEARCH_DATA).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(LONGITUDE, Constant.mPoint.longitude + "")//
                .addParams(LATITUDE, Constant.mPoint.latitude + "")//
                .addParams(PAGEINDEX, page + "")//
                .addParams(PAGEROWS, 10 + "")//
                .addParams(MERCHANTNAME, result)//
                .build().execute(new BaseCallBackObject(mLinstener, RecommndShopModel.class) {
        });
    }

    public static void loadShopsServiceEvalu(int page, String mMerchantId, final onNetFinishLinstenerT<ShopsEvali> mLinstener) {
        PostFormBuilder mBuilder = OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_SHOPS_EVALUTE);
        if (!TextUtils.isEmpty(mMerchantId))
            mBuilder.addParams(MERCHANTID, mMerchantId);
        mBuilder.addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(PAGEINDEX, "1")//
                .addParams(PAGEROWS, "50")//
                .build().execute(new BaseCallBackObject(mLinstener, ShopsEvali.class) {
        });
    }

    public static void loadShopsServiceInfo(int page, String merchantId,
                                            final onNetFinishLinstenerT<ShopsService> mLinstener) {
        PostFormBuilder mBuilder = OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_SHOPS_SERVICES);
        if (!TextUtils.isEmpty(merchantId))
            mBuilder.addParams(MERCHANTID, merchantId);

        mBuilder.addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(PAGEINDEX, page + "")//
                .addParams(PAGEROWS, "10")//
                .build().execute(new BaseCallBackObject(mLinstener, ShopsService.class) {
        });
    }

    public static void loadMyWalletInfo(final onNetFinishLinstenerT<WalletModel> mLinstener) {
        OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_SHOPS_WALLET).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .build().execute(new BaseCallBackObject(mLinstener, WalletModel.class) {
        });
    }

    /**
     * 获取用户授权信息
     */
    public static void loadUserAuth(Context context, final onNetFinishLinstenerT<UserAuth> mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_USER_AUTH)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .build().execute(new BaseCallBackObject(mLinstener, UserAuth.class) {
        });
    }

    /**
     * 获取用户认证信息
     */
    public static void loadUserAuthImpl(Context context) {
        Constant.loadUserAuth = false;
        Constant.mUserAuth = null;
        loadUserAuth(context, new onNetFinishLinstenerT<UserAuth>() {
            @Override
            public void onSuccess(int code, UserAuth result) {
                Constant.mUserAuth = result;
                Constant.loadUserAuth = true;
            }

            @Override
            public void onFail(int code, String result) {
                Constant.loadUserAuth = false;
            }
        });
    }

    public static void loadFUFriend(String index,
                                    final onNetFinishLinstenerT<FuFriendModel> mLinstener) {
        OkHttpUtils.post()//
                .url(Common_HEADER + LOAD_FU_FRIEND).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(PAGEINDEX, index)//
                .addParams(PAGEROWS, 10 + "")//
                .addParams(PAGEROWS, 10 + "")//
                .build().execute(new BaseCallBackObject(mLinstener, FuFriendModel.class) {
        });
    }

    public static void postShopPay(String merchantid, String consumeamount, String payChannel,
                                   String mPassowrd, final onConnectionFinishLinstener mLinstener) {
        if ("alipay".equals(payChannel)) {
            OkHttpUtils.post()//
                    .url(Common_HEADER + POST_SHOP_PAY).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                    .addParams(USERID, Constant.LOGIN_USERID)//
                    .addParams(MERCHANTID, merchantid)//
                    .addParams(CONSUMEAMOUNT, consumeamount)//
                    .addParams(PAYCHANNEL, payChannel)//
                    .addParams(PAYPASS, mPassowrd)//
                    .build().execute(new Callback() {
                @Override
                public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                    JSONObject object = new JSONObject(mData);
                    String payParam = object.getString("payParam");

                    return payParam;
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    mLinstener.onFail(FailCode, e.getMessage());
                }

                @Override
                public void onResponse(Object response, int id) {
                    mLinstener.onSuccess(SuccessCode, response.toString());
                }
            });
        } else {
            OkHttpUtils.post()//
                    .url(Common_HEADER + POST_SHOP_PAY).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                    .addParams(USERID, Constant.LOGIN_USERID)//
                    .addParams(MERCHANTID, merchantid)//
                    .addParams(CONSUMEAMOUNT, consumeamount)//
                    .addParams(PAYCHANNEL, payChannel)//
                    .addParams(PAYPASS, mPassowrd)//
                    .build().execute(new NoDataCallBack(mLinstener));
        }

    }

    public static void postShopWxPay(String merchantid, String consumeamount, String payChannel,
                                     String mPassowrd, final onNetFinishLinstenerT mLinstener) {
        OkHttpUtils.post()//
                .url(Common_HEADER + POST_SHOP_PAY).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(MERCHANTID, merchantid)//
                .addParams(CONSUMEAMOUNT, consumeamount)//
                .addParams(PAYCHANNEL, payChannel)//
                .addParams(PAYPASS, mPassowrd)//
                .build().execute(new BaseCallBackObject(mLinstener, WXModel.class) {
        });

    }

    public static void postShopReport(String id, String type, String reportContent,
                                      final onConnectionFinishLinstener mLinstener) {
        OkHttpUtils.post()//
                .url(Common_HEADER + JUBAO).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(VALUEID, id)//
                .addParams(TYPE, type)//
                .addParams(REPORTCONTENT, reportContent)//
                .build().execute(new NoDataCallBack(mLinstener));
    }

    /**
     * 应用升级
     */
    public static void updateApp(final onNetFinishLinstenerT<UpdateAppModel> mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + UAPDATE).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(TYPE, "1")//
                .build().execute(new BaseCallBackObject(mLinstener, UpdateAppModel.class) {
        });
    }

    public static void loadShopOrderList(int page,
                                         final onNetFinishLinstenerT<ShopOrderModel> mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + SHOP_ORDERS).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(PAGEINDEX, page + "")//
                .addParams(PAGEROWS, "10")//
                .build().execute(new BaseCallBackObject(mLinstener, ShopOrderModel.class) {
        });
    }

    public static void loadServeOrderList(int page,
                                          final onNetFinishLinstenerT<ServeOrderModel> mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + SERVICE_ORDERS).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(PAGEINDEX, page + "")//
                .addParams(PAGEROWS, "10")//
                .build().execute(new BaseCallBackObject(mLinstener, ServeOrderModel.class) {
        });
    }

    public static void deleteServe(int id, final onConnectionFinishLinstener mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + DELETE_SERVICE).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(SERVICEID, id + "")//
                .build().execute(new NoDataCallBack(mLinstener) {
        });
    }

    /*
     * 微信充值支付
     * */
    public static void postWxPay(String money, String chanel, final onNetFinishLinstenerT<WXModel> mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + WX_PAY_URL).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(AMOUNT, money + "")//
                .addParams(CHANNEL, chanel)//
                .build().execute(new BaseCallBackObject(mLinstener, WXModel.class) {
        });
    }

    /*
     * 阿里充值支付
     * */
    public static void postAliPayPay(String money, final onConnectionFinishLinstener mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + WX_PAY_URL).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//goodsId
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(AMOUNT, money + "")//
                .addParams(CHANNEL, "alipay")//
                .build().execute(new AliPayCallBack(mLinstener));
    }

    public static void postShopEva(String orderNo, String content, String star, final onConnectionFinishLinstener mLinstener) {
        OkHttpUtils.get()//
                .url(Common_HEADER + SHOP_EVA).addParams(ACCESSTOKEN, Constant.ACCESS_TOKEN)//
                .addParams(USERID, Constant.LOGIN_USERID)//
                .addParams(ORDERNO, orderNo + "")//
                .addParams(CONTENT, content + "")//
                .addParams(STAR, star + "")//
                .build().execute(new NoDataCallBack(mLinstener));
    }
}
