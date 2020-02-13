package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.MultiItemComment;
import com.feitianzhu.huangliwo.pushshop.adapter.SetMealGoodsAdapter;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SingleGoodsModel;
import com.feitianzhu.huangliwo.shop.adapter.EditCommentAdapter;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.Glide4Engine;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.AddSetMealView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/13
 * time: 17:18
 * email: 694125155@qq.com
 */
public class EditSetMealActivity extends BaseActivity {
    public static final String MERCHANTS_ID = "merchants_id";
    private static final int REQUEST_CODE_CHOOSE = 1000;
    private int merchantsId = -1;
    private int maxSize = 6;
    private boolean isAdd = false; //是否还可以添加图片
    private List<String> mSelected = new ArrayList<>();
    private SetMealGoodsAdapter mAdapter;
    private List<MultiItemComment> multiItemCommentList = new ArrayList<>();
    private List<SingleGoodsModel> list = new ArrayList<>();
    private EditCommentAdapter editCommentAdapter;
    private String userId;
    private String token;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView2)
    RecyclerView goodsRecyclerView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.right_text)
    TextView rightText;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_setmeal;
    }

    @Override
    protected void initView() {
        merchantsId = getIntent().getIntExtra(MERCHANTS_ID, -1);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("上传套餐");
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SetMealGoodsAdapter(list);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(goodsRecyclerView);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        goodsRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
        comment.setId(R.mipmap.g01_01shangchuan);
        multiItemCommentList.add(comment);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        editCommentAdapter = new EditCommentAdapter(multiItemCommentList);
        recyclerView.setAdapter(editCommentAdapter);
        editCommentAdapter.notifyDataSetChanged();
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

        editCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == MultiItemComment.upImg) {
                    selectPhoto();
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

        editCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                multiItemCommentList.remove(position);
                editCommentAdapter.setNewData(multiItemCommentList);
                editCommentAdapter.notifyDataSetChanged();
                allSelect.remove(position);
                maxSize = 6 - allSelect.size();
                if (isAdd) {
                    MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
                    comment.setId(R.mipmap.g01_01shangchuan);
                    multiItemCommentList.add(comment);
                }
                isAdd = false;
            }
        });
    }

    public void addSingles(SingleGoodsModel singleGoodsModel, int position) {
        new XPopup.Builder(EditSetMealActivity.this)
                .asCustom(new AddSetMealView(EditSetMealActivity.this)
                        .setData(singleGoodsModel)
                        .setOnConfirmClickListener(new AddSetMealView.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(String name, String num, String price) {
                                if (TextUtils.isEmpty(name)) {
                                    ToastUtils.showShortToast("请输入商品名称");
                                    return;
                                } else if (TextUtils.isEmpty(num)) {
                                    ToastUtils.showShortToast("请输入商品数量");
                                    return;
                                } else if (TextUtils.isEmpty(price)) {
                                    ToastUtils.showShortToast("请输入商品价格");
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

    public void selectPhoto() {
        Matisse.from(EditSetMealActivity.this)
                .choose(MimeType.ofImage())
                //自定义选择选择的类型
                //.choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                /*.capture(true)  // 使用相机，和 captureStrategy 一起使用
                .captureStrategy(new CaptureStrategy(true, "com.feitianzhu.fu700.fileprovider"))*/
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为6
                .maxSelectable(maxSize)
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
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.left_button, R.id.addSetMeal, R.id.ll_discount, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
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
            case R.id.right_button:
                submit();
                break;
        }
    }

    public void submit() {
        String setMealName = editSetMealName.getText().toString().trim();
        String setMealDescription = editSetMealDescription.getText().toString().trim();
        String setMealPrice = editSetMealPrice.getText().toString().trim();
        String percentage = editSetMealDiscount.getText().toString().trim();
        String rules = editRules.getText().toString().trim();
        if (allSelect.size() <= 0) {
            ToastUtils.showShortToast("请上传套餐图片");
            return;
        }
        if (TextUtils.isEmpty(setMealName)) {
            ToastUtils.showShortToast("请输入套餐名称");
            return;
        }
        /*if (TextUtils.isEmpty(setMealDescription)) {
            ToastUtils.showShortToast("请输入套餐描述");
            return;
        }*/
        if (TextUtils.isEmpty(setMealPrice)) {
            ToastUtils.showShortToast("请输入套餐价格");
            return;
        }
        if (TextUtils.isEmpty(percentage)) {
            ToastUtils.showShortToast("请输入套餐折扣比例");
            return;
        }
        if (Double.valueOf(percentage) > 100 || Double.valueOf(percentage) < 0) {
            ToastUtils.showShortToast("折扣比例不能大于100小于0");
            return;
        }
        if (TextUtils.isEmpty(rules)) {
            ToastUtils.showShortToast("请输入套餐使用规则");
            return;
        }
        SetMealInfo info = new SetMealInfo();
        info.setSingleList(list);
        info.setSmName(setMealName);
        info.setRemark(setMealDescription);
        info.setDiscount(Double.valueOf(percentage) / 100);
        info.setPrice(Double.valueOf(setMealPrice));
        info.setMerchantId(merchantsId);
        info.setUseRules(rules);
        String json = new Gson().toJson(info);
        List<File> fileList = new ArrayList<>();
        if (allSelect.size() > 0) {
            for (int i = 0; i < allSelect.size(); i++) {
                fileList.add(new File(allSelect.get(i)));
            }
        }

        OkGo.<LzyResponse>post(Urls.ADD_SETMEAL)
                .tag(this)
                .addFileParams("files", fileList)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("setMealStr", json)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(EditSetMealActivity.this, response.body().msg, response.body().code);
                        goneloadDialog();
                        if (response.body().code == 0) {
                            ToastUtils.showShortToast("添加成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                        goneloadDialog();
                    }
                });
    }

    private List<String> allSelect = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            allSelect.addAll(Matisse.obtainPathResult(data));
            for (int i = 0; i < mSelected.size(); i++) {
                MultiItemComment comment = new MultiItemComment(MultiItemComment.LookImg);
                comment.setPath(mSelected.get(i));
                multiItemCommentList.add(multiItemCommentList.size() - 1, comment);
            }
            maxSize = 6 - allSelect.size();
            if (maxSize <= 0) {
                multiItemCommentList.remove(multiItemCommentList.size() - 1);
                isAdd = true;
            }
            editCommentAdapter.setNewData(multiItemCommentList);
            editCommentAdapter.notifyDataSetChanged();
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }
}
