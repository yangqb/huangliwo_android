package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.enumeration.MultipleNumModel;
import com.necer.enumeration.SelectedModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.painter.InnerPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneCalendarActivity extends BaseActivity {
    public static final String SELECT_MODEL = "select_model";
    public static final String SELECT_DATE = "select_date";
    public String startDate;
    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.tv_data)
    TextView tv_data;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.miui10Calendar)
    Miui10Calendar miui10Calendar;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_calendar;
    }

    @Override
    protected void initView() {
        rightText.setText("确定");
        rightText.setVisibility(View.VISIBLE);
        tv_result = findViewById(R.id.tv_result);
        tv_data = findViewById(R.id.tv_data);
        tv_desc = findViewById(R.id.tv_desc);
        int selectType = getIntent().getIntExtra(SELECT_MODEL, 0);

        List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01", "2018-12-23");
        List<String> multiDay = Arrays.asList("2020-03-18", "2020-03-25");
        List<String> singerDay = Arrays.asList("2020-03-18");
        CustomPainter innerPainter = new CustomPainter(miui10Calendar);
        miui10Calendar = findViewById(R.id.miui10Calendar);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// HH:mm:ss
        Calendar rightNow = Calendar.getInstance();
        //当前时间 加10天
        rightNow.add(Calendar.DAY_OF_YEAR, 2);
        //new SimgpleDateFormat 进行格式化
        //利用Calendar的getTime方法，将时间转化为Date对象
        //利用SimpleDateFormat对象 把Date对象格式化
        if (selectType == 2 || selectType == 3) {
            miui10Calendar.getAllSelectDateList().remove(0);
            miui10Calendar.setMultipleNum(2, MultipleNumModel.FULL_REMOVE_FIRST);
            List<LocalDate> localDateList = new ArrayList<>();
            localDateList.add(new LocalDate(simpleDateFormat.format((Calendar.getInstance().getTime()))));
            localDateList.add(new LocalDate(simpleDateFormat.format(rightNow.getTime())));
            miui10Calendar.getAllSelectDateList().addAll(localDateList); //设置默认选中多个日期
        } else {
            miui10Calendar.setSelectedMode(SelectedModel.SINGLE_SELECTED);
            innerPainter.setMultiSelectDay(singerDay);
        }


        //InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
        innerPainter.setPointList(pointList);

        Map<String, String> strMap = new HashMap<>();
        strMap.put("2019-01-25", "测试");
        strMap.put("2019-01-23", "测试1");
        strMap.put("2019-01-24", "测试2");
        innerPainter.setReplaceLunarStrMap(strMap);

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("2019-08-25", Color.RED);

        colorMap.put("2019-08-5", Color.parseColor("#000000"));
        innerPainter.setReplaceLunarColorMap(colorMap);


        List<String> holidayList = new ArrayList<>();
        holidayList.add("2019-7-20");
        holidayList.add("2019-7-21");
        holidayList.add("2019-7-22");

        List<String> workdayList = new ArrayList<>();
        workdayList.add("2019-7-23");
        workdayList.add("2019-7-24");
        workdayList.add("2019-7-25");

        innerPainter.setLegalHolidayList(holidayList, workdayList);

        miui10Calendar.setCalendarPainter(innerPainter);

        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
                // Log.d(TAG, "   当前页面选中 " + localDate);

                if (localDate != null) {
                    CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                    Lunar lunar = calendarDate.lunar;
                    tv_data.setText(localDate.toString("yyyy年MM月dd日"));
                    tv_desc.setText(lunar.chineseEra + lunar.animals + "年" + lunar.lunarMonthStr + lunar.lunarDayStr);
                    startDate = localDate.toString();
                } else {
                    tv_data.setText("");
                    tv_desc.setText("");
                    startDate = null;
                }
            }
        });

        miui10Calendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currectSelectList, List<LocalDate> allSelectList) {

                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currectSelectList.size() + "个  总共选中" + allSelectList.size() + "个");
                //Log.d(TAG, year + "年" + month + "月");
                //Log.d(TAG, "当前页面选中：：" + currectSelectList);
                //Log.d(TAG, "全部选中：：" + allSelectList);
               /* if (currectSelectList.size() > 2) {
                    miui10Calendar.getAllSelectDateList().remove(0);
                }*/

                if (currectSelectList.size() == 2) {
                    startDate = currectSelectList.get(0).toString() + "=" + currectSelectList.get(1).toString();
                } else if (currectSelectList.size() == 1) {
                    startDate = currectSelectList.get(0).toString() + "=" + currectSelectList.get(0).toString();
                } else {
                    startDate = null;
                }
            }
        });

    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                if (startDate == null) {
                    ToastUtils.showShortToast("请选择日期");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(SELECT_DATE, startDate);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
