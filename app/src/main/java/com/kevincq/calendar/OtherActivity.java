package com.kevincq.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kevincq.calendarlibrary.MNCalendarVertical;
import com.kevincq.calendarlibrary.listeners.OnCalendarRangeChooseListener;
import com.kevincq.calendarlibrary.model.MNCalendarVerticalConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OtherActivity extends AppCompatActivity {

    private Context context;

    private MNCalendarVertical mnCalendarVertical;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        context = this;

        mnCalendarVertical = (MNCalendarVertical) findViewById(R.id.mnCalendarVertical);

        /**
         * 区间选取完成监听
         */
        mnCalendarVertical.setOnCalendarRangeChooseListener(new OnCalendarRangeChooseListener() {
            @Override
            public void onRangeDate(Date startDate, Date endDate) {
                String startTime = sdf.format(startDate);
                String endTime = sdf.format(endDate);
                Toast.makeText(context, "开始日期:" + startTime + ",结束日期:" + endTime, Toast.LENGTH_SHORT).show();
                System.out.println("----------间隔的工作日----------" + getWorkingDaysBetweenTwoDates(startDate, endDate).toString());
            }
        });

    }

    public static int[][] getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

//        日历
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;
        int totalDays = 0;

//如果start和end相同，则返回0
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return new int[0][0];
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {

            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            totalDays++;
        } //不包括结束日期
//        return totalDays + "," + workDays;
        return new int[totalDays][workDays];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_other, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_01:

                /**
                 *  自定义设置相关
                 */
                MNCalendarVerticalConfig mnCalendarVerticalConfig = new MNCalendarVerticalConfig.Builder()
                        .setMnCalendar_showWeek(true)                   //是否显示星期栏
                        .setMnCalendar_showLunar(false)                  //是否显示阴历
                        .setMnCalendar_colorWeek("#F57223")             //星期栏的颜色
                        .setMnCalendar_titleFormat("yyyy年MM月")           //每个月的标题样式
                        .setMnCalendar_colorTitle("#000000")            //每个月标题的颜色
                        .setMnCalendar_colorSolar("#000000")            //阳历的颜色
                        .setMnCalendar_colorLunar("#00ff00")            //阴历的颜色
                        .setMnCalendar_colorBeforeToday("#20000000")      //今天之前的日期的颜色
                        .setMnCalendar_colorRangeBg("#1014BF5C")        //区间中间的背景颜色
                        .setMnCalendar_colorRangeText("#000000")        //区间文字的颜色
                        .setMnCalendar_colorStartAndEndBg("#14BF5C")    //开始结束的背景颜色
                        .setMnCalendar_countMonth(4)                    //显示多少月(默认6个月)
                        .build();
                mnCalendarVertical.setConfig(mnCalendarVerticalConfig);
                break;
            case R.id.action_02:
                //隐藏星期
                MNCalendarVerticalConfig mnCalendarVerticalConfig2 = new MNCalendarVerticalConfig.Builder()
                        .setMnCalendar_showWeek(false)
                        .build();
                mnCalendarVertical.setConfig(mnCalendarVerticalConfig2);
                break;
            case R.id.action_03:
                //隐藏阴历
                MNCalendarVerticalConfig mnCalendarVerticalConfig3 = new MNCalendarVerticalConfig.Builder()
                        .setMnCalendar_showLunar(false)
                        .build();
                mnCalendarVertical.setConfig(mnCalendarVerticalConfig3);
                break;
            case R.id.action_04:
                //恢复默认
                MNCalendarVerticalConfig mnCalendarVerticalConfig4 = new MNCalendarVerticalConfig.Builder().build();
                mnCalendarVertical.setConfig(mnCalendarVerticalConfig4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
