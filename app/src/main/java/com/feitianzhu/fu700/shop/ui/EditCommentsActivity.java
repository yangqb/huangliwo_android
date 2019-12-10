package com.feitianzhu.fu700.shop.ui;

import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.MultiItemComment;
import com.feitianzhu.fu700.shop.adapter.EditCommentAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 评价
 * */
public class EditCommentsActivity extends BaseActivity {

    private EditCommentAdapter adapter;
    private List<MultiItemComment> multiItemCommentList = new ArrayList<>();
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_comments;
    }

    @Override
    protected void initView() {
        titleName.setText("评价");
        rightText.setText("发布");
        rightText.setVisibility(View.VISIBLE);


        MultiItemComment comment = new MultiItemComment(MultiItemComment.upImg);
        comment.setId(R.mipmap.g01_01shangchuan);
        multiItemCommentList.add(comment);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new EditCommentAdapter(multiItemCommentList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        initListener();
    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (adapter.getItemViewType(position) == MultiItemComment.upImg) {
                    //selectPhone();
                } else {

                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    public String getPath() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory().getPath() + "/huangLiWo";
        } else {
            cachePath = getCacheDir().getPath();
        }
        return cachePath;
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        new XPopup.Builder(this)
                .asConfirm("确定要退出评价？", "", "关闭", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        finish();
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    @Override
    protected void initData() {

    }
}
