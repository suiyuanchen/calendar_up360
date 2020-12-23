package com.kevincq.calendarlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kevincq.calendarlibrary.R;
import com.kevincq.calendarlibrary.listeners.OnCalendarRangeChooseListener;
import com.kevincq.calendarlibrary.model.MNCalendarItemModel;
import com.kevincq.calendarlibrary.model.MNCalendarVerticalConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by maning on 2017/5/9.
 */

public class MNCalendarVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HashMap<String, ArrayList<MNCalendarItemModel>> mDatas;

    private LayoutInflater layoutInflater;

    private Context context;

    private Calendar currentCalendar;

    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    public Date startDate = null;
    public Date endDate = null;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public MNCalendarVerticalAdapter(Context context, HashMap<String, ArrayList<MNCalendarItemModel>> mDatas, Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        this.context = context;
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_vertical, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            //标题
            Calendar calendarTitle = (Calendar) currentCalendar.clone();
            calendarTitle.add(Calendar.MONTH, position);
            Date titleDate = calendarTitle.getTime();

            //设置标题的格式
            String mnCalendar_titleFormat = mnCalendarVerticalConfig.getMnCalendar_titleFormat();
            SimpleDateFormat sdf = new SimpleDateFormat(mnCalendar_titleFormat);
            myViewHolder.tv_item_title.setText(sdf.format(titleDate));
            if (position == 0) {
                myViewHolder.tv_item_title.setVisibility(View.GONE);
            } else {
                myViewHolder.tv_item_title.setVisibility(View.VISIBLE);
            }
            //设置标题的颜色
            myViewHolder.tv_item_title.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorTitle());

            //日期数据
            ArrayList<MNCalendarItemModel> dates = mDatas.get(String.valueOf(position));
            //初始化RecycleerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 7);
            myViewHolder.recyclerViewItem.setLayoutManager(gridLayoutManager);

            //初始化Adapter
            Calendar calendarItem = (Calendar) currentCalendar.clone();
            calendarItem.add(Calendar.MONTH, position);
//            MNCalendarVerticalItemAdapter mnCalendarVerticalItemAdapter = new MNCalendarVerticalItemAdapter(context, dates, calendarItem, this, mnCalendarVerticalConfig);
            CalendarVerticalItemAdapter mnCalendarVerticalItemAdapter = new CalendarVerticalItemAdapter(R.layout.mn_item_calendar_vertical_item_new, context, dates, calendarItem, this, mnCalendarVerticalConfig);
            myViewHolder.recyclerViewItem.setAdapter(mnCalendarVerticalItemAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_title;
        private RecyclerView recyclerViewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            recyclerViewItem = (RecyclerView) itemView.findViewById(R.id.recyclerViewItem);
        }
    }

    public OnCalendarRangeChooseListener onCalendarRangeChooseListener;

    public void setOnCalendarRangeChooseListener(OnCalendarRangeChooseListener onCalendarRangeChooseListener) {
        this.onCalendarRangeChooseListener = onCalendarRangeChooseListener;
        notifyDataSetChanged();
    }

    public void notifyChoose() {
        if (this.onCalendarRangeChooseListener != null) {
            if (startDate != null && endDate != null) {
                onCalendarRangeChooseListener.onRangeDate(startDate, endDate);
            }
        }
    }

    public void updateDatas(HashMap<String, ArrayList<MNCalendarItemModel>> mDatas, Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        this.startDate = null;
        this.endDate = null;
        notifyDataSetChanged();
    }

    public void initStartAndEndData(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        notifyDataSetChanged();
    }
}
