package com.feitianzhu.fu700.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.view.pickview.LoopView;
import com.feitianzhu.fu700.view.pickview.OnItemSelectedListener;
import com.socks.library.KLog;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TakePhotoOptions;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;

/**
 * package name: com.feitianzhu.fu700.common
 * user: yangqinbo
 * date: 2019/12/10
 * time: 16:49
 * email: 694125155@qq.com
 */
public abstract class SelectPhotoActivity2 extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {
    protected InvokeParam invokeParam;
    protected Context mContext;
    protected TakePhoto takePhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mContext = SelectPhotoActivity2.this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type =
                PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type =
                PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    protected void TakePhoto(boolean isCorp, int limit) {
        Uri imageUri = CommonPhoto();
        if (limit > 1) {
            if (isCorp) {
                takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
            } else {
                takePhoto.onPickMultiple(limit);
            }
            return;
        }
        //是否剪辑
        if (isCorp)
            takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
        else {
            takePhoto.onPickFromGallery();
        }
    }

    protected Uri CommonPhoto() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        //压缩
        configCompress();
        //选择图片配置
        configTakePhotoOption();
        return imageUri;
    }

    protected CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(800).setOutputY(800);
        //是否使用自带工具
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    protected void configTakePhotoOption() {
        //是否使用take相册
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //builder.setWithOwnGallery(true);
        //纠正拍照的照片旋转角度
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    protected void configCompress() {
        CompressConfig config;
        LubanOptions option =
                new LubanOptions.Builder().setMaxHeight(800).setMaxWidth(800).setMaxSize(102400).create();
        config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);
        takePhoto.onEnableCompress(config, true);
    }

    protected void TakeCamera(boolean isCorp) {
        Uri imageUri = CommonPhoto();
        if (isCorp)
            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
        else {
            takePhoto.onPickFromCapture(imageUri);
        }
    }

    /**
     * 获取TakePhoto实例
     */
    protected TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto =
                    (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    protected int selectIndex = 0;
    protected AlertDialog alertDialog;

    protected abstract void onWheelSelect(int num, ArrayList<String> mList);

    protected void showTypeDialog(final ArrayList<String> list) {
        selectIndex = 0;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view, null);
        dialogBuilder.setView(dialogView);
        LoopView loopView = (LoopView) dialogView.findViewById(R.id.loopView);
        loopView.setDividerColor(getResources().getColor(R.color.dialog_rect_bg));
        Button bt_sure = (Button) dialogView.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) dialogView.findViewById(R.id.bt_cancel);

        loopView.setNotLoop();
        // 滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                KLog.e(selectIndex);
                selectIndex = index;
            }
        });
        // 设置原始数据
        loopView.setItems(list);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIndex += 1;
                //Toast.makeText(VerificationActivity.this, "点击：" + list.get(selectIndex)+"selectIndex"+selectIndex, Toast.LENGTH_SHORT)
                //    .show();
                onWheelSelect(selectIndex, list);
                alertDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
