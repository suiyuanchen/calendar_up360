package com.kevincq.calendarlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kevincq.calendarlibrary.adapter.MNCalendarVerticalAdapter;
import com.kevincq.calendarlibrary.listeners.OnCalendarRangeChooseListener;
import com.kevincq.calendarlibrary.model.Lunar;
import com.kevincq.calendarlibrary.model.MNCalendarItemModel;
import com.kevincq.calendarlibrary.model.MNCalendarVerticalConfig;
import com.kevincq.calendarlibrary.utils.LunarCalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by maning on 2017/5/10.
 * 垂直方向的日历
 */

public class MNCalendarVertical extends LinearLayout {

    private Context context;

    private RecyclerView recyclerViewCalendar;
    private LinearLayout ll_week;
    private TextView tv_week_01;
    private TextView tv_week_02;
    private TextView tv_week_03;
    private TextView tv_week_04;
    private TextView tv_week_05;
    private TextView tv_week_06;
    private TextView tv_week_07;
    private TextView tv_current_month;

    private Calendar currentCalendar = Calendar.getInstance();

    private MNCalendarVerticalConfig mnCalendarVerticalConfig = new MNCalendarVerticalConfig.Builder().build();
    private MNCalendarVerticalAdapter mnCalendarVerticalAdapter;
    //    private HashMap<String, ArrayList<Date>> dataMap;
    private HashMap<String, ArrayList<MNCalendarItemModel>> dataMap;

    private OnCalendarRangeChooseListener onCalendarRangeChooseListener;

    public MNCalendarVertical(Context context) {
        this(context, null);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        initViews();

        initCalendarDatas();

    }

    LinearLayoutManager l;
    Calendar cal = Calendar.getInstance();//获取一个Calendar对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

    private void initViews() {
        //绑定View
        View.inflate(context, R.layout.mn_layout_calendar_vertical, this);
        recyclerViewCalendar = (RecyclerView) findViewById(R.id.recyclerViewCalendar);
        ll_week = (LinearLayout) findViewById(R.id.ll_week);
        tv_week_01 = (TextView) findViewById(R.id.tv_week_01);
        tv_week_02 = (TextView) findViewById(R.id.tv_week_02);
        tv_week_03 = (TextView) findViewById(R.id.tv_week_03);
        tv_week_04 = (TextView) findViewById(R.id.tv_week_04);
        tv_week_05 = (TextView) findViewById(R.id.tv_week_05);
        tv_week_06 = (TextView) findViewById(R.id.tv_week_06);
        tv_week_07 = (TextView) findViewById(R.id.tv_week_07);
        tv_current_month = findViewById(R.id.tv_current_month);
//        cal.setTime(new Date());
//        cal.add(Calendar.MONTH, 1);//获取当前时间的下一个月
        tv_current_month.setText(sdf.format(new Date()));
        //初始化RecycleerView
        l = new LinearLayoutManager(context);
        recyclerViewCalendar.setLayoutManager(l);
        recyclerViewCalendar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstIndex = l.findFirstVisibleItemPosition();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, firstIndex);//获取当前时间的下一个月
                tv_current_month.setText(sdf.format(cal.getTime()));

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    private void initCalendarDatas() {
        //星期栏的显示和隐藏
        boolean mnCalendar_showWeek = mnCalendarVerticalConfig.isMnCalendar_showWeek();
        if (mnCalendar_showWeek) {
            ll_week.setVisibility(View.VISIBLE);
            tv_week_01.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeekEnd());
            tv_week_02.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_03.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_04.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_05.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_06.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_07.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeekEnd());
        } else {
            ll_week.setVisibility(View.GONE);
        }

        //日期集合
        dataMap = new HashMap<>();
        //计算日期
        int mnCalendar_countMonth = mnCalendarVerticalConfig.getMnCalendar_countMonth();
//        for (int i = 0; i < mnCalendar_countMonth; i++) {
//            int count7 = 0;
//            ArrayList<Date> mDatas = new ArrayList<>();
//            Calendar calendar = (Calendar) currentCalendar.clone();
//            calendar.add(Calendar.MONTH, i);
//            //至于当月的第一天
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            //获取当月第一天是星期几
//            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//            //移动到需要绘制的第一天
//            calendar.add(Calendar.DAY_OF_MONTH, -day_of_week);
//            //6*7
//            while (mDatas.size() < 6 * 7) {
//                mDatas.add(calendar.getTime());
//                //包含两个7就多了一行
//                if (String.valueOf(calendar.getTime().getDate()).equals("7")) {
//                    count7++;
//                }
//                //向前移动一天
//                calendar.add(Calendar.DAY_OF_MONTH, 1);
//            }
//            if (count7 >= 2) {
//                ArrayList<Date> mDatas2 = new ArrayList<>();
//                for (int j = 0; j < mDatas.size() - 7; j++) {
//                    mDatas2.add(mDatas.get(j));
//                }
//                mDatas = new ArrayList<>();
//                mDatas.addAll(mDatas2);
//            }
//
//            dataMap.put(String.valueOf(i), mDatas);
//        }

        for (int i = 0; i < mnCalendar_countMonth; i++) {
            int count7 = 0;
            ArrayList<MNCalendarItemModel> mDatas = new ArrayList<>();
            Calendar calendar = (Calendar) currentCalendar.clone();
            calendar.add(Calendar.MONTH, i);
            //至于当月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //获取当月第一天是星期几
            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //移动到需要绘制的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -day_of_week);
            //6*7
            while (mDatas.size() < 6 * 7) {
                Date date = calendar.getTime();
                //阴历计算
                Lunar lunar = LunarCalendarUtils.solarToLunar(date);
                mDatas.add(new MNCalendarItemModel(date, lunar));
                //包含两个7就多了一行
                if (String.valueOf(calendar.getTime().getDate()).equals("7")) {
                    count7++;
                }
                //向前移动一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (count7 >= 2) {
                ArrayList<MNCalendarItemModel> mDatas2 = new ArrayList<>();
                for (int j = 0; j < mDatas.size() - 7; j++) {
                    mDatas2.add(mDatas.get(j));
                }
                mDatas = new ArrayList<>();
                mDatas.addAll(mDatas2);
            }

            dataMap.put(String.valueOf(i), mDatas);
        }

        //设置Adapter
        initAdapter();
    }

    private Date mStartDate, mEndDate;

    public void initStartAndEndData(Date startDate, Date endDate) {
        mStartDate = startDate;
        mEndDate = endDate;
        if (mStartDate != null && mEndDate != null && mnCalendarVerticalAdapter != null) {
            mnCalendarVerticalAdapter.initStartAndEndData(mStartDate, mEndDate);
        }
    }

    private void initAdapter() {
        if (mnCalendarVerticalAdapter == null) {
            mnCalendarVerticalAdapter = new MNCalendarVerticalAdapter(context, dataMap, currentCalendar, mnCalendarVerticalConfig);
            recyclerViewCalendar.setAdapter(mnCalendarVerticalAdapter);
        } else {
            mnCalendarVerticalAdapter.updateDatas(dataMap, currentCalendar, mnCalendarVerticalConfig);
        }

        if (onCalendarRangeChooseListener != null) {
            mnCalendarVerticalAdapter.setOnCalendarRangeChooseListener(onCalendarRangeChooseListener);
        }

    }

    /**
     * 设置配置文件
     *
     * @param config
     */
    public void setConfig(MNCalendarVerticalConfig config) {
        this.mnCalendarVerticalConfig = config;
        initCalendarDatas();
    }


    public void setOnCalendarRangeChooseListener(OnCalendarRangeChooseListener onCalendarRangeChooseListener) {
        this.onCalendarRangeChooseListener = onCalendarRangeChooseListener;
        if (mnCalendarVerticalAdapter != null) {
            mnCalendarVerticalAdapter.setOnCalendarRangeChooseListener(onCalendarRangeChooseListener);
        }
    }

}
