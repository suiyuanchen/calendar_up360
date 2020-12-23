package com.kevincq.calendarlibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kevincq.calendarlibrary.R;
import com.kevincq.calendarlibrary.constant.MNConst;
import com.kevincq.calendarlibrary.model.MNCalendarItemModel;
import com.kevincq.calendarlibrary.model.MNCalendarVerticalConfig;
import com.kevincq.calendarlibrary.utils.LunarCalendarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maning on 2017/5/9.
 */

public class CalendarVerticalItemAdapter extends BaseQuickAdapter<MNCalendarItemModel, BaseViewHolder> {

    private Context context;

    private Calendar currentCalendar;

    private MNCalendarVerticalAdapter adapter;

    private String now_yyy_mm_dd;
    private Date nowDate = new Date();

    //配置信息
    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    public CalendarVerticalItemAdapter(int layoutResId, Context context, @Nullable ArrayList<MNCalendarItemModel> data, Calendar currentCalendar, MNCalendarVerticalAdapter adapter, MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        super(layoutResId, data);
        this.context = context;
        this.currentCalendar = currentCalendar;
        this.adapter = adapter;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;

        now_yyy_mm_dd = MNConst.sdf_yyy_MM_dd.format(nowDate);
        try {
            nowDate = MNConst.sdf_yyy_MM_dd.parse(now_yyy_mm_dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, final MNCalendarItemModel item) {
        int position = helper.getLayoutPosition();
        Date datePosition = item.getDate();
//        helper.itemView.setVisibility(View.VISIBLE);
        helper.getView(R.id.iv_select_bg).setVisibility(View.GONE);
        helper.getView(R.id.view_today).setVisibility(View.GONE);
        helper.setTextColor(R.id.tvDay, mnCalendarVerticalConfig.getMnCalendar_colorSolar());
        helper.setText(R.id.tvDay, String.valueOf(datePosition.getDate()));

        //不是本月的隐藏
        Date currentDate = currentCalendar.getTime();
        if (datePosition.getMonth() != currentDate.getMonth()) {
            helper.itemView.setVisibility(View.GONE);
        } else {
            helper.itemView.setVisibility(View.VISIBLE);
        }

        //判断是不是当天
        String position_yyy_MM_dd = MNConst.sdf_yyy_MM_dd.format(datePosition);
        if (now_yyy_mm_dd.equals(position_yyy_MM_dd)) {
//                helper.getView(R.id.tvDay.setText("今天");
            helper.getView(R.id.view_today).setVisibility(View.VISIBLE);
        }

        //判断是不是点击了起始日期
        if (adapter.startDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
            adapter.startDate = datePosition;
            helper.getView(R.id.iv_select_bg).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_select_bg).setBackgroundResource(R.drawable.mn_selected_bg_start);

            helper.setTextColor(R.id.tvDay, mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
            //动态修改颜色
            GradientDrawable myGrad = (GradientDrawable) helper.getView(R.id.iv_select_bg).getBackground();
            myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
        }
        if (adapter.endDate != null && simpleDateFormat.format(adapter.endDate).equals(simpleDateFormat.format(datePosition))) {
            adapter.endDate = datePosition;
            helper.getView(R.id.iv_select_bg).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_select_bg).setBackgroundResource(R.drawable.mn_selected_bg_start);
            helper.getView(R.id.iv_bg_right).setVisibility(View.INVISIBLE);
            helper.getView(R.id.iv_bg_right).setBackgroundResource(R.drawable.mn_selected_bg_right);
            helper.getView(R.id.iv_bg_left).setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.tvDay, mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
            //动态修改颜色
            GradientDrawable myGrad = (GradientDrawable) helper.getView(R.id.iv_select_bg).getBackground();
            myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
        }
//            if (adapter.endDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
        if (adapter.endDate != null && adapter.startDate == datePosition) {
            helper.getView(R.id.iv_bg_right).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_bg_left).setVisibility(View.INVISIBLE);
            helper.getView(R.id.iv_bg_left).setBackgroundResource(R.drawable.mn_selected_bg_left);
        }
        //判断是不是大于起始日期
        if (adapter.startDate != null && adapter.endDate != null && (datePosition.getTime() > adapter.startDate.getTime() && datePosition.getTime() < adapter.endDate.getTime())) {
            helper.getView(R.id.iv_bg_right).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_bg_left).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_select_bg).setVisibility(View.VISIBLE);
            if (position % 7 == 6) {
                helper.getView(R.id.iv_select_bg).setBackgroundResource(R.drawable.mn_selected_bg_right);
            } else if (position % 7 == 0) {
                helper.getView(R.id.iv_select_bg).setBackgroundResource(R.drawable.mn_selected_bg_left);
            }
            helper.setTextColor(R.id.tvDay, mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
            //动态修改颜色
            GradientDrawable myGrad = (GradientDrawable) helper.getView(R.id.iv_select_bg).getBackground();
            myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeBg());
//            }
        }

        if (position % 7 == 6 || LunarCalendarUtils.isLastDayOfMonth(datePosition)) {
            helper.getView(R.id.iv_bg_right).setVisibility(View.INVISIBLE);
        } else if (position % 7 == 0 || LunarCalendarUtils.isFirstDayOfMonth(datePosition)) {
            helper.getView(R.id.iv_bg_left).setVisibility(View.INVISIBLE);
        }

        //小于今天的颜色变灰
//            if (simpleDateFormat.format(datePosition).compareTo(simpleDateFormat.format(nowDate)) < 0) {
        if (datePosition.getTime() < nowDate.getTime()) {
            if (position % 7 == 6 || position % 7 == 0) {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#20F57223"));
            } else {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#20000000"));
            }

        } else {
            if (position % 7 == 6 || position % 7 == 0) {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#F57223"));
            } else {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#000000"));
            }
            if (adapter.startDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#ffffff"));
            }
            if (adapter.endDate != null && simpleDateFormat.format(adapter.endDate).equals(simpleDateFormat.format(datePosition))) {
                helper.setTextColor(R.id.tvDay, Color.parseColor("#ffffff"));
            }
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MNCalendarItemModel mnCalendarItemModel = mDatas.get(position);

                Date dateClick = item.getDate();
                //必须大于今天
                if (dateClick.getTime() < nowDate.getTime()) {
                    Toast.makeText(context, "选择的日期必须大于今天", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (adapter.startDate != null && adapter.endDate != null) {
                    adapter.startDate = dateClick;
                    adapter.endDate = null;
                }
                if (adapter.startDate == null) {
                    adapter.startDate = dateClick;
                } else {
                    //判断结束位置是不是大于开始位置
                    long deteClickTime = dateClick.getTime();
                    long deteStartTime = adapter.startDate.getTime();
                    if (deteClickTime <= deteStartTime) {
                        adapter.startDate = dateClick;
                    } else {
                        if ((deteClickTime - deteStartTime) / 24 / 60 / 60 / 1000 > 59) {
                            Toast.makeText(context, "最多只能选择60天", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            adapter.endDate = dateClick;
                        }
                    }
                }

                //刷新
                notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                //选取通知
                adapter.notifyChoose();


            }
        });
    }

}
