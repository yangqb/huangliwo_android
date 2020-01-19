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
import java.util.Calendar;
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
 * date: 2020/1/17
 * time: 14:33
 * email: 694125155@qq.com
 */
public class DatePickDialog extends DialogFragment {
    private static WeakReference<DatePickDialog> dialogWeakReference;
    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private String strYear = "2019";
    private String strMonth = "05";
    private int selColor;
    private int unSelColor;
    private DatePickDialog.WeekAdapter startAdapter;
    private DatePickDialog.WeekAdapter endAdapter;
    private int maxsize = 24;
    private int minsize = 14;
    @BindView(R.id.startWeek)
    WheelView startWeekWheelView;
    @BindView(R.id.endWeek)
    WheelView endWeekWheelView;

    private DatePickDialog.OnPickListener onPickListener;

    public interface OnPickListener {
        void onWheelFinish(String year, String month);
    }

    public void setOnPickListener(DatePickDialog.OnPickListener listener) {
        this.onPickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dialog); //dialog全屏
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_date_pick_dialog, container, false);
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

    public static DatePickDialog newInstance() {
        DatePickDialog dialog = new DatePickDialog();
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        selColor = getResources().getColor(R.color.color_wheel_sel);
        unSelColor = getResources().getColor(R.color.color_wheel_unsel);
        startAdapter = new DatePickDialog.WeekAdapter(getActivity(), yearList, getYearItem(strYear), maxsize, minsize);
        startWeekWheelView.setVisibleItems(5);
        startWeekWheelView.setViewAdapter(startAdapter);
        startWeekWheelView.setCurrentItem(getYearItem(strYear));
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

        endAdapter = new DatePickDialog.WeekAdapter(getActivity(), monthList, getMonthItem(strMonth), maxsize, minsize);
        endWeekWheelView.setVisibleItems(5);
        endWeekWheelView.setViewAdapter(endAdapter);
        endWeekWheelView.setCurrentItem(getMonthItem(strMonth));
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
                strYear = currentText;
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
                strMonth = currentText;
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
                String strYear = yearList.get(starItem);
                int endItem = endWeekWheelView.getCurrentItem();
                String strMonth = monthList.get(endItem);
                onPickListener.onWheelFinish(strYear, strMonth);
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
     * @param year
     * @param month
     */
    public void setPickTime(String year, String month) {
        if (!TextUtils.isEmpty(year)) {
            this.strYear = year;
        }
        if (!TextUtils.isEmpty(month)) {
            this.strMonth = month;
        }
    }

    private void initData() {
        yearList.clear();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        for (int i = 2019; i < year + 1; i++) {
            yearList.add(i + "");
        }

        monthList.clear();
        monthList.add("01");
        monthList.add("02");
        monthList.add("03");
        monthList.add("04");
        monthList.add("05");
        monthList.add("06");
        monthList.add("07");
        monthList.add("08");
        monthList.add("09");
        monthList.add("10");
        monthList.add("11");
        monthList.add("12");
    }

    /**
     * 返回索引
     *
     * @param str
     * @return
     */
    public int getYearItem(String str) {
        int size = yearList.size();
        int hoursIndex = 0;
        for (int i = 0; i < size; i++) {
            if (str.equals(yearList.get(i))) {
                return hoursIndex;
            } else {
                hoursIndex++;
            }
        }
        return hoursIndex;
    }

    /**
     * 返回索引
     *
     * @param str
     * @return
     */
    public int getMonthItem(String str) {
        int size = monthList.size();
        int hoursIndex = 0;
        for (int i = 0; i < size; i++) {
            if (str.equals(monthList.get(i))) {
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
    public void setTextviewSize(String curriteItemText, DatePickDialog.WeekAdapter adapter) {
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
        dialogWeakReference = new WeakReference<DatePickDialog>(this);
        show(fragmentManager, "dialog");
    }
}
