package com.feitianzhu.huangliwo.shop.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.EvaluateMode;
import com.feitianzhu.huangliwo.model.MultiItemComment;
import com.feitianzhu.huangliwo.shop.adapter.EditCommentAdapter;
import com.feitianzhu.huangliwo.utils.FileUtils;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.feitianzhu.huangliwo.view.CustomSelectPhotoView;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/*
 * 评价
 * */
public class EditCommentsActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 1000;
    private static final int REQUEST_CODE_CAPTURE = 999;
    public static final String EVALUATE_DATA = "evaluate_data";
    private int maxSize = 6;
    private EditCommentAdapter mAdapter;
    private List<MultiItemComment> multiItemCommentList = new ArrayList<>();
    private EvaluateMode evaluateMode;
    private MediaStoreCompat mMediaStoreCompat;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edit_content)
    EditText editContent;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_comments;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("评价");
        rightText.setText("发布");
        rightText.setVisibility(View.VISIBLE);
        mMediaStoreCompat = new MediaStoreCompat(this);
        evaluateMode = (EvaluateMode) getIntent().getSerializableExtra(EVALUATE_DATA);
        MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
        comment.setId(R.mipmap.g01_01shangchuan);
        multiItemCommentList.add(comment);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new EditCommentAdapter(multiItemCommentList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        initListener();
    }

    public void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == MultiItemComment.upImg) {
                    KeyboardUtils.hideSoftInput(editContent);
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
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                multiItemCommentList.remove(position);
                mAdapter.setNewData(multiItemCommentList);
                mAdapter.notifyDataSetChanged();
                allSelect.remove(position);
                maxSize = 6 - allSelect.size();
                if (maxSize == 1) {
                    MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
                    comment.setId(R.mipmap.g01_01shangchuan);
                    multiItemCommentList.add(comment);
                }
            }
        });
    }

    public void selectPhoto(int maxNum) {
        new XPopup.Builder(this)
                .asCustom(new CustomSelectPhotoView(EditCommentsActivity.this)
                        .setOnSelectTakePhotoListener(new CustomSelectPhotoView.OnSelectTakePhotoListener() {
                            @Override
                            public void onTakePhotoClick() {
                                //TakePhoto(false, 1);
                                Matisse.from(EditCommentsActivity.this)
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
        XXPermissions.with(EditCommentsActivity.this)
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
                            Matisse.from(EditCommentsActivity.this)
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


    @OnClick({R.id.left_button, R.id.right_button})
    @SingleClick()
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                new XPopup.Builder(this)
                        .asConfirm("确定要退出评价？", "", "关闭", "确定", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                finish();
                            }
                        }, null, false)
                        .bindLayout(R.layout.layout_dialog) //绑定已有布局
                        .show();
                break;
            case R.id.right_button:
                if (TextUtils.isEmpty(editContent.getText().toString().trim())) {
                    ToastUtils.show("您还没有填写评价内容");
                    return;
                }
                if (editContent.getText().toString().trim().length() > 500) {
                    ToastUtils.show("最多可以输入500个字哦");
                    return;
                }

                if (allSelect.size() > 0) {
                    compressImg();
                } else {
                    submit(null);
                }
                break;
        }
    }


    public void submit(List<File> compressList) {
        String url;
        evaluateMode.setContent(editContent.getText().toString());
        String json = new Gson().toJson(evaluateMode);
        if (evaluateMode.getMerchantId() != 0) {
            url = Urls.EVALUATE_SETMEAL_ORDER;
        } else {
            url = Urls.EVALUATE_ORDER;
        }

        PostRequest<LzyResponse> postRequest = OkGo.<LzyResponse>post(url)
                .tag(this);
        if (compressList != null && compressList.size() > 0) {
            postRequest.addFileParams("files", compressList);
        } else {
            postRequest.isMultipart(true);
        }
        postRequest.params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("evaluateBody", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditCommentsActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            ToastUtils.show("发布成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                    }
                });
    }


    /*
     * 图片压缩同步
     * */
    public void compressImg() {
        Observable.just(allSelect).observeOn(Schedulers.io())
                .map(new Func1<List<String>, List<File>>() {
                    @Override
                    public List<File> call(List<String> strings) {
                        try {
                            return Luban.with(EditCommentsActivity.this)
                                    .ignoreBy(1024)
                                    .setTargetDir(getPath())
                                    .load(strings)
                                    .get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }).onErrorResumeNext(Observable.empty())
                .subscribe(new Action1<List<File>>() {
                    @Override
                    public void call(List<File> files) {
                        submit(files);
                    }
                });
    }


    @Override
    protected void initData() {

    }

    private List<String> allSelect = new ArrayList<>();

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
                mAdapter.setNewData(multiItemCommentList);
                mAdapter.notifyDataSetChanged();
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
                mAdapter.setNewData(multiItemCommentList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/bydby/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
}
