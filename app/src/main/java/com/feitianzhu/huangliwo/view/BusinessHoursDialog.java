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
import com.feitianzhu.huangliwo.shop.ui.dialog.ProvinceDialog2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
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
 * time: 9:29
 * email: 694125155@qq.com
 */
public class BusinessHoursDialog extends DialogFragment {
    private static WeakReference<BusinessHoursDialog> dialogWeakReference;
    private List<String> hours = new ArrayList<>();
    private String[] minutes = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "55"};
    private String strHours = "00";
    private String strMinutes = "00";
    private String endHours = "00";
    private String endMinutes = "00";
    private int selColor;
    private int unSelColor;
    private HoursAdapter startHoursAdapter;
    private HoursAdapter startMinutesAdapter;
    private HoursAdapter endHoursAdapter;
    private HoursAdapter endMinutesAdapter;
    private int maxsize = 24;
    private int minsize = 14;
    @BindView(R.id.startHours)
    WheelView startHoursWheelView;
    @BindView(R.id.startMinutes)
    WheelView startMinutesWheelView;
    @BindView(R.id.endHours)
    WheelView endHoursWheelView;
    @BindView(R.id.endMinutes)
    WheelView endMinutesWheelView;

    private OnTimePickListener onTimePickListener;

    public interface OnTimePickListener {
        void onWheelFinish(String startHours, String startMinutes, String endHours, String endMinutes);
    }

    public void setOnTimePickListener(OnTimePickListener listener) {
        this.onTimePickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dialog); //dialog全屏
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_business_time_dialog, container, false);
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

    public static BusinessHoursDialog newInstance() {
        BusinessHoursDialog dialog = new BusinessHoursDialog();
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        selColor = getResources().getColor(R.color.color_wheel_sel);
        unSelColor = getResources().getColor(R.color.color_wheel_unsel);
        startHoursAdapter = new HoursAdapter(getActivity(), hours, getHoursItem(strHours), maxsize, minsize);
        startHoursWheelView.setVisibleItems(5);
        startHoursWheelView.setViewAdapter(startHoursAdapter);
        startHoursWheelView.setCurrentItem(getHoursItem(strHours));
        startHoursWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        startHoursWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        startHoursWheelView.setWheelForeground(R.drawable.province_wheel_val);
        startHoursWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) startHoursAdapter.getItemText(startHoursWheelView.getCurrentItem());
                setTextviewSize(currentText, startHoursAdapter);
            }
        });

        startMinutesAdapter = new HoursAdapter(getActivity(), Arrays.asList(minutes), getMinutesItem(strMinutes), maxsize, minsize);
        startMinutesWheelView.setVisibleItems(5);
        startMinutesWheelView.setViewAdapter(startMinutesAdapter);
        startMinutesWheelView.setCurrentItem(getMinutesItem(strMinutes));
        startMinutesWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        startMinutesWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        startMinutesWheelView.setWheelForeground(R.drawable.province_wheel_val);
        startMinutesWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) startMinutesAdapter.getItemText(startMinutesWheelView.getCurrentItem());
                setTextviewSize(currentText, startMinutesAdapter);
            }
        });

        endHoursAdapter = new HoursAdapter(getActivity(), hours, getHoursItem(endHours), maxsize, minsize);
        endHoursWheelView.setVisibleItems(5);
        endHoursWheelView.setViewAdapter(endHoursAdapter);
        endHoursWheelView.setCurrentItem(getHoursItem(endHours));
        endHoursWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        endHoursWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        endHoursWheelView.setWheelForeground(R.drawable.province_wheel_val);
        endHoursWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) endHoursAdapter.getItemText(endHoursWheelView.getCurrentItem());
                setTextviewSize(currentText, endHoursAdapter);
            }
        });

        endMinutesAdapter = new HoursAdapter(getActivity(), Arrays.asList(minutes), getMinutesItem(endMinutes), maxsize, minsize);
        endMinutesWheelView.setVisibleItems(5);
        endMinutesWheelView.setViewAdapter(endMinutesAdapter);
        endMinutesWheelView.setCurrentItem(getMinutesItem(endMinutes));
        endMinutesWheelView.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        endMinutesWheelView.setWheelBackground(R.drawable.province_wheel_bg);
        endMinutesWheelView.setWheelForeground(R.drawable.province_wheel_val);
        endMinutesWheelView.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) endMinutesAdapter.getItemText(endMinutesWheelView.getCurrentItem());
                setTextviewSize(currentText, endMinutesAdapter);
            }
        });

        initListener();
    }

    public void initListener() {
        startHoursWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startHoursAdapter.getItemText(wheel.getCurrentItem());
                strHours = currentText;
                setTextviewSize(currentText, startHoursAdapter);
            }
        });

        startHoursWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startHoursAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, startHoursAdapter);
            }
        });

        startMinutesWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startMinutesAdapter.getItemText(wheel.getCurrentItem());
                strMinutes = currentText;
                setTextviewSize(currentText, startMinutesAdapter);
            }
        });

        startMinutesWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startMinutesAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, startMinutesAdapter);
            }
        });

        endHoursWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endHoursAdapter.getItemText(wheel.getCurrentItem());
                endHours = currentText;
                setTextviewSize(currentText, endHoursAdapter);
            }
        });

        endHoursWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endHoursAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, endHoursAdapter);
            }
        });

        endMinutesWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endMinutesAdapter.getItemText(wheel.getCurrentItem());
                endMinutes = currentText;
                setTextviewSize(currentText, endMinutesAdapter);
            }
        });

        endMinutesWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endMinutesAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, endMinutesAdapter);
            }
        });

    }

    @OnClick({R.id.iv_wheel_ok, R.id.iv_wheel_cancel})
    public void okClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wheel_ok:
                int starHoursItem = startHoursWheelView.getCurrentItem();
                String strHours = hours.get(starHoursItem);
                int startMinutesItem = startMinutesWheelView.getCurrentItem();
                String strMinutes = minutes[startMinutesItem];

                int endHoursItem = endHoursWheelView.getCurrentItem();
                String endHours = hours.get(endHoursItem);
                int endMinutesItem = endMinutesWheelView.getCurrentItem();
                String endMinutes = minutes[endMinutesItem];
                onTimePickListener.onWheelFinish(strHours, strMinutes, endHours, endMinutes);
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
     * @param strHours
     * @param strTimes
     */
    public void setBusinessTime(String strHours, String strTimes, String endHours, String endMinutes) {
        if (!TextUtils.isEmpty(strHours)) {
            this.strHours = strHours;
        }
        if (!TextUtils.isEmpty(strTimes)) {
            this.strMinutes = strTimes;
        }
        if (!TextUtils.isEmpty(endHours)) {
            this.endHours = endHours;
        }
        if (!TextUtils.isEmpty(endMinutes)) {
            this.endMinutes = endMinutes;
        }
    }

    private void initData() {
        hours.clear();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(i + "");
            }
        }
    }

    /**
     * 返回索引
     *
     * @param province
     * @return
     */
    public int getHoursItem(String province) {
        int size = hours.size();
        int hoursIndex = 0;
        for (int i = 0; i < size; i++) {
            if (province.equals(hours.get(i))) {
                return hoursIndex;
            } else {
                hoursIndex++;
            }
        }
        return hoursIndex;
    }

    /**
     * 得到城市索引
     *
     * @param city
     * @return
     */
    public int getMinutesItem(String city) {
        int size = minutes.length;
        int minutesIndex = 0;
        for (int i = 0; i < size; i++) {
            if (city.equals(minutes[i])) {
                return minutesIndex;
            } else {
                minutesIndex++;
            }
        }
        return minutesIndex;
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, HoursAdapter adapter) {
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


    private class HoursAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected HoursAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
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
        dialogWeakReference = new WeakReference<BusinessHoursDialog>(this);
        show(fragmentManager, "dialog");
    }
}
