package com.feitianzhu.huangliwo.pushshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.MultiItemComment;
import com.feitianzhu.huangliwo.pushshop.adapter.SetMealGoodsAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;
import com.feitianzhu.huangliwo.shop.adapter.EditCommentAdapter;
import com.feitianzhu.huangliwo.shop.ui.EditCommentsActivity;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.AddSetMealView;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/14
 * time: 16:33
 * email: 694125155@qq.com
 */
public class SetMealDetailActivity extends BaseActivity {
    public static final String SETMEAL_ID = "setmeal_id";
    private static final int REQUEST_CODE_CHOOSE = 1000;
    private static final int REQUEST_CODE_CAPTURE = 999;
    private MediaStoreCompat mMediaStoreCompat;
    private List<MultiItemComment> multiItemCommentList = new ArrayList<>();
    private EditCommentAdapter editCommentAdapter;
    private List<SingleGoodsModel> list = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();
    private List<String> allSelect = new ArrayList<>();
    private SetMealGoodsAdapter mAdapter;
    private SetMealInfo setMealInfo;
    private int maxSize = 6;
    private int setMealId = -1;
    private String token;
    private String userId;
    @BindView(R.id.right_img)
    ImageView imageView;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edit_setMeal_name)
    EditText editSetMealName;
    @BindView(R.id.edit_setMeal_description)
    EditText editSetMealDescription;
    @BindView(R.id.edit_setMeal_price)
    EditText editSetMealPrice;
    @BindView(R.id.edit_setMeal_discount)
    EditText editSetMealDiscount;
    @BindView(R.id.edit_rules)
    EditText editRules;
    @BindView(R.id.recyclerView2)
    RecyclerView goodsRecyclerView;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setmeal_detail;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        setMealId = getIntent().getIntExtra(SETMEAL_ID, -1);
        imageView.setBackgroundResource(R.mipmap.g06_01bianji);
        rightText.setText("编辑");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMediaStoreCompat = new MediaStoreCompat(this);
        editCommentAdapter = new EditCommentAdapter(multiItemCommentList);
        recyclerView.setAdapter(editCommentAdapter);
        editCommentAdapter.notifyDataSetChanged();

        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SetMealGoodsAdapter(list);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(goodsRecyclerView);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        goodsRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        EditTextUtils.afterDotTwo(editSetMealPrice);
        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("onItemSwipeStart", pos + "");
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("clearView", pos + "");
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.e("onItemSwiped", pos + "");
                list.remove(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                Log.e("onItemSwipeMoving", "dX=" + dX + ";dY=" + dY);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                addSingles(list.get(position), position);
            }
        });

        editCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                multiItemCommentList.remove(position);
                editCommentAdapter.setNewData(multiItemCommentList);
                editCommentAdapter.notifyDataSetChanged();
                allSelect.remove(position);
                maxSize = 6 - allSelect.size();
                if (maxSize == 1) {
                    MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
                    comment.setId(R.mipmap.g01_01shangchuan);
                    multiItemCommentList.add(comment);
                }
            }
        });

        editCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == MultiItemComment.upImg) {
                    selectPhoto(maxSize);
                } else {
                    // 仅需一行代码,默认配置为：
                    //      显示顶部进度指示器、
                    //      显示右侧下载按钮、
                    //      隐藏左侧关闭按钮、
                    //      开启点击图片关闭、
                    //      关闭下拉图片关闭、
                    //      加载方式为手动模式
                    //      加载原图的百分比在底部
                    ImagePreview
                            .getInstance()
                            // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                            .setContext(mContext)
                            .setEnableDragClose(true) //下拉图片关闭
                            .setShowDownButton(false)
                            // 设置从第几张开始看（索引从0开始）
                            .setIndex(position)
                            .setShowErrorToast(true)//加载失败提示
                            //=================================================================================================
                            // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                            // 1：第一步生成的imageInfo List
                            //.setImageInfoList(imageInfoList)

                            // 2：直接传url List
                            .setImageList(allSelect)

                            // 3：只有一张图片的情况，可以直接传入这张图片的url
                            //.setImage(String image)
                            //=================================================================================================

                            // 开启预览
                            .start();
                }
            }
        });
    }

    public void selectPhoto(int maxNum) {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(SetMealDetailActivity.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                //TakePhoto(false, 1);
                                Matisse.from(SetMealDetailActivity.this)
                                        .choose(MimeType.ofImage())
                                        //自定义选择选择的类型
                                        //.choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
                                        //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                        .showSingleMediaType(true)
                                        /*.capture(true)  // 使用相机，和 captureStrategy 一起使用
                                        .captureStrategy(new CaptureStrategy(true, "com.feitianzhu.fu700.fileprovider"))*/
                                        //有序选择图片 123456...
                                        .countable(true)
                                        //最大选择数量
                                        .maxSelectable(maxNum)
                                        //选择方向
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        //图片过滤
                                        //.addFilter()
                                        //界面中缩略图的质量
                                        .thumbnailScale(0.85f)
                                        //蓝色主题
                                        .theme(R.style.Matisse_Zhihu)
                                        //黑色主题
                                        //.theme(R.style.Matisse_Dracula)
                                        //Picasso加载方式
                                        //.imageEngine(new PicassoEngine())
                                        //Glide加载方式
                                        .originalEnable(true)
                                        .maxOriginalSize(10)
                                        .imageEngine(new Glide4Engine())
                                        .forResult(REQUEST_CODE_CHOOSE);
                            }
                        })
                        .setSelectCameraListener(new CustomSelectPhotoView.OnSelectCameraListener() {
                            @Override
                            public void onCameraClick() {
                                //TakeCamera(false);
                                requestPermission();
                            }
                        }))
                .show();
    }

    private void requestPermission() {
        XXPermissions.with(SetMealDetailActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Manifest.permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Matisse.from(SetMealDetailActivity.this)
                                    .capture()
                                    .captureStrategy(new CaptureStrategy(true, "com.feitianzhu.huangliwo.fileprovider", "bldby"))
                                    .forResult(REQUEST_CODE_CAPTURE, mMediaStoreCompat);
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        OkGo.<LzyResponse<SetMealInfo>>get(Urls.GET_SETMEAL_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("smId", setMealId + "")
                .execute(new JsonCallback<LzyResponse<SetMealInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<SetMealInfo>> response) {
                        super.onSuccess(SetMealDetailActivity.this, response.body().msg, response.body().code);
                        if (response.body().data != null && response.body().code == 0) {
                            setMealInfo = response.body().data;
                            showView();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<SetMealInfo>> response) {
                        super.onError(response);
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.submit, R.id.addSetMeal, R.id.ll_discount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.submit:
                submit();
                break;
            case R.id.addSetMeal:
                addSingles(null, -1);
                break;
            case R.id.ll_discount:
                //折扣比例说明
                String content = "套餐折扣比例区别于整体折扣比例，单独结算。";
                new XPopup.Builder(this)
                        .asConfirm("套餐折扣比例说明", content, "", "确定", null, null, true)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
        }

    }

    public void addSingles(SingleGoodsModel singleGoodsModel, int position) {
        new XPopup.Builder(SetMealDetailActivity.this)
                .asCustom(new AddSetMealView(SetMealDetailActivity.this)
                        .setData(singleGoodsModel)
                        .setOnConfirmClickListener(new AddSetMealView.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(String name, String num, String price) {
                                if (TextUtils.isEmpty(name)) {
                                    ToastUtils.show("请输入商品名称");
                                    return;
                                } else if (TextUtils.isEmpty(num)) {
                                    ToastUtils.show("请输入商品数量");
                                    return;
                                } else if (TextUtils.isEmpty(price)) {
                                    ToastUtils.show("请输入商品价格");
                                    return;
                                }
                                if (singleGoodsModel == null) {  //添加
                                    SingleGoodsModel model = new SingleGoodsModel();
                                    model.setSinglePrice(Double.valueOf(price));
                                    model.setName(name);
                                    model.setNum(Integer.valueOf(num));
                                    list.add(model);
                                } else { //修改
                                    list.get(position).setName(name);
                                    list.get(position).setNum(Integer.valueOf(num));
                                    list.get(position).setSinglePrice(Double.valueOf(price));
                                }
                                mAdapter.setNewData(list);
                                mAdapter.notifyDataSetChanged();
                            }
                        }))
                .show();
    }

    public void showView() {
        titleName.setText(setMealInfo.getSmName());
        String[] imgs = setMealInfo.getImgs().split(",");
        imgList = Arrays.asList(imgs);
        for (int i = 0; i < imgList.size(); i++) {
            MultiItemComment comment = new MultiItemComment(MultiItemComment.LookImg);
            comment.setPath(imgList.get(i));
            multiItemCommentList.add(comment);
            allSelect.add(imgList.get(i));
        }
        if (imgList.size() < 6) {
            MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
            comment.setId(R.mipmap.g01_01shangchuan);
            multiItemCommentList.add(comment);
        }
        maxSize = 6 - allSelect.size();
        list.addAll(setMealInfo.getSingleList());
        editCommentAdapter.setNewData(multiItemCommentList);
        editCommentAdapter.notifyDataSetChanged();

        editSetMealName.setText(setMealInfo.getSmName());
        editSetMealDescription.setText(setMealInfo.getRemark());
        editSetMealPrice.setText(setMealInfo.getPrice() + "");
        editSetMealDiscount.setText(String.valueOf(setMealInfo.getDiscount() * 100));
        editRules.setText(setMealInfo.getUseRules());

        mAdapter.setNewData(setMealInfo.getSingleList());
        mAdapter.notifyDataSetChanged();
    }

    private StringBuffer imgUrls = new StringBuffer();

    public void submit() {
        String setMealName = editSetMealName.getText().toString().trim();
        String setMealDescription = editSetMealDescription.getText().toString().trim();
        String setMealPrice = editSetMealPrice.getText().toString().trim();
        String percentage = editSetMealDiscount.getText().toString().trim();
        String rules = editRules.getText().toString().trim();
        if (allSelect.size() <= 0) {
            ToastUtils.show("请上传图片");
            return;
        }
        if (TextUtils.isEmpty(setMealName)) {
            ToastUtils.show("请输入套餐名称");
            return;
        }
        /*if (TextUtils.isEmpty(setMealDescription)) {
            ToastUtils.show("请输入套餐描述");
            return;
        }*/
        if (TextUtils.isEmpty(setMealPrice)) {
            ToastUtils.show("请输入套餐价格");
            return;
        }
        if (TextUtils.isEmpty(percentage)) {
            ToastUtils.show("请输入套餐折扣比例");
            return;
        }
        if (TextUtils.isEmpty(rules)) {
            ToastUtils.show("请输入套餐使用规则");
            return;
        }
        SetMealInfo info = new SetMealInfo();
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < allSelect.size(); i++) {
            if (allSelect.get(i).startsWith("http") || allSelect.get(i).startsWith("https")) {
                String url = allSelect.get(i) + ",";
                imgUrls.append(url);
            } else {
                fileList.add(new File(allSelect.get(i)));
            }
        }
        if (imgUrls.toString().endsWith(",")) {
            String substring = imgUrls.substring(0, imgUrls.lastIndexOf(","));
            info.setImgs(substring);
        }
        info.setSingleList(list);
        info.setSmId(setMealInfo.getSmId());
        info.setSmName(setMealName);
        info.setRemark(setMealDescription);
        info.setDiscount(Double.valueOf(percentage) / 100);
        info.setPrice(Double.valueOf(setMealPrice));
        info.setMerchantId(setMealInfo.getMerchantId());
        info.setUseRules(rules);
        String json = new Gson().toJson(info);


        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(Urls.UPDATE_SETMEAL).tag(this);

        if (fileList.size() > 0) {
            postRequest.addFileParams("files", fileList);
        } else {
            postRequest.isMultipart(true);
        }

        postRequest.params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("setMealStr", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(SetMealDetailActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0) {
                            ToastUtils.show("修改成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<Uri> uris = Matisse.obtainResult(data);
                List<String> currSelect = Matisse.obtainPathResult(data);
                allSelect.addAll(currSelect);
                for (int i = 0; i < currSelect.size(); i++) {
                    MultiItemComment comment = new MultiItemComment(MultiItemComment.LookImg);
                    comment.setPath(currSelect.get(i));
                    multiItemCommentList.add(multiItemCommentList.size() - 1, comment);
                }
                maxSize = 6 - allSelect.size();
                if (maxSize <= 0) {
                    multiItemCommentList.remove(multiItemCommentList.size() - 1);
                }
                editCommentAdapter.setNewData(multiItemCommentList);
                editCommentAdapter.notifyDataSetChanged();
                Log.d("Matisse", "mSelected: " + currSelect);
            } else if (requestCode == REQUEST_CODE_CAPTURE) {
                Uri contentUri = mMediaStoreCompat.getCurrentPhotoUri();
                String path = mMediaStoreCompat.getCurrentPhotoPath();
                allSelect.add(path);
                MultiItemComment comment = new MultiItemComment(MultiItemComment.LookImg);
                comment.setPath(path);
                multiItemCommentList.add(multiItemCommentList.size() - 1, comment);

                maxSize = 6 - allSelect.size();
                if (maxSize <= 0) {
                    multiItemCommentList.remove(multiItemCommentList.size() - 1);
                }
                editCommentAdapter.setNewData(multiItemCommentList);
                editCommentAdapter.notifyDataSetChanged();
            }
        }

    }
}
