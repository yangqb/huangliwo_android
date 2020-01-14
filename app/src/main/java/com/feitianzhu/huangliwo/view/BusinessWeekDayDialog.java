package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/1/13
 * time: 11:20
 * email: 694125155@qq.com
 */
public class BusinessWeekDayDialog extends DialogFragment {
    private static WeakReference<BusinessWeekDayDialog> dialogWeakReference;
    private List<String> weekList = new ArrayList<>();
    private String strWeek = "星期三";
    private String endWeek = "星期三";
    private int selColor;
    private int unSelColor;
    private BusinessWeekDayDialog.WeekAdapter startAdapter;
    private BusinessWeekDayDialog.WeekAdapter endAdapter;
    private int maxsize = 24;
    private int minsize = 14;
    @BindView(R.id.startWeek)
    WheelView startWeekWheelView;
    @BindView(R.id.endWeek)
    WheelView endWeekWheelView;

    private BusinessWeekDayDialog.OnWeekPickListener onWeekPickListener;

    public interface OnWeekPickListener {
        void onWheelFinish(String startWeek, String endWeek);
    }

    public void setOnWeekPickListener(BusinessWeekDayDialog.OnWeekPickListener listener) {
        this.onWeekPickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dialog); //dialog全屏
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_business_week_dialog, container, false);
        ButterKnife.bind(this, view);
        this.getDialog().setCanceledOnTouchOutside(true);
        this.getDialog().setCancelable(true);
        Window window = this.getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        return view;
    }

    public static BusinessWeekDayDialog newInstance() {
        BusinessWeekDayDialog dialog = new BusinessWeekDayDialog();
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        selColor = getResources().getColor(R.color.color_wheel_sel);
        unSelColor = getResources().getColor(R.color.color_wheel_unsel);
        startAdapter = new BusinessWeekDayDialog.WeekAdapter(getActivity(), weekList, getWeekItem(strWeek), maxsize, minsize);
        startWeekWheelView.setVisibleItems(5);
        startWeekWheelView.setViewAdapter(startAdapter);
        startWeekWheelView.setCurrentItem(getWeekItem(strWeek));
        startWeekWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        startWeekWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        startWeekWheelView.setWheelForeground(R.drawable.province_wheel_val);
        startWeekWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) startAdapter.getItemText(startWeekWheelView.getCurrentItem());
                setTextviewSize(currentText, startAdapter);
            }
        });

        endAdapter = new BusinessWeekDayDialog.WeekAdapter(getActivity(), weekList, getWeekItem(endWeek), maxsize, minsize);
        endWeekWheelView.setVisibleItems(5);
        endWeekWheelView.setViewAdapter(endAdapter);
        endWeekWheelView.setCurrentItem(getWeekItem(endWeek));
        endWeekWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        endWeekWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        endWeekWheelView.setWheelForeground(R.drawable.province_wheel_val);
        endWeekWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) endAdapter.getItemText(endWeekWheelView.getCurrentItem());
                setTextviewSize(currentText, endAdapter);
            }
        });

        initListener();
    }

    public void initListener() {
        startWeekWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startAdapter.getItemText(wheel.getCurrentItem());
                strWeek = currentText;
                setTextviewSize(currentText, startAdapter);
            }
        });

        startWeekWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, startAdapter);
            }
        });


        endWeekWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endAdapter.getItemText(wheel.getCurrentItem());
                endWeek = currentText;
                setTextviewSize(currentText, endAdapter);
            }
        });

        endWeekWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, endAdapter);
            }
        });

    }

    @OnClick({R.id.iv_wheel_ok, R.id.iv_wheel_cancel})
    public void okClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wheel_ok:
                int starItem = startWeekWheelView.getCurrentItem();
                String strWeek = weekList.get(starItem);
                int endItem = endWeekWheelView.getCurrentItem();
                String endWeek = weekList.get(endItem);
                onWeekPickListener.onWheelFinish(strWeek, endWeek);
                dismiss();
                break;
            case R.id.iv_wheel_cancel:
                dismiss();
                break;
        }

    }

    /**
     * 初始化时间
     *
     * @param strWeek
     * @param endWeek
     */
    public void setBusinessTime(String strWeek,String endWeek) {
        if (!TextUtils.isEmpty(strWeek)) {
            this.strWeek = strWeek;
        }
        if (!TextUtils.isEmpty(endWeek)) {
            this.endWeek = endWeek;
        }
    }

    private void initData() {
        weekList.clear();
        weekList.add("星期一");
        weekList.add("星期二");
        weekList.add("星期三");
        weekList.add("星期四");
        weekList.add("星期五");
        weekList.add("星期六");
        weekList.add("星期日");
    }

    /**
     * 返回索引
     *
     * @param province
     * @return
     */
    public int getWeekItem(String province) {
        int size = weekList.size();
        int hoursIndex = 0;
        for (int i = 0; i < size; i++) {
            if (province.equals(weekList.get(i))) {
                return hoursIndex;
            } else {
                hoursIndex++;
            }
        }
        return hoursIndex;
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, BusinessWeekDayDialog.WeekAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(selColor);
            } else {
                textvew.setTextSize(14);
                textvew.setTextColor(unSelColor);
            }
        }
    }


    private class WeekAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected WeekAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_view, NO_RESOURCE, currentItem, maxsize, minsize, selColor, unSelColor);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    public void show(FragmentManager fragmentManager) {
        dialogWeakReference = new WeakReference<BusinessWeekDayDialog>(this);
        show(fragmentManager, "dialog");
    }
}
