package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.graphics.Canvas;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MultiMonthView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/3/17
 * time: 14:28
 * email: 694125155@qq.com
 */
public class CustomMonthView extends MultiMonthView {
    public CustomMonthView(Context context) {
        super(context);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {

    }
}
