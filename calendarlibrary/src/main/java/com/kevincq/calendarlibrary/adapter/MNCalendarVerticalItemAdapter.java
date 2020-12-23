package com.kevincq.calendarlibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.kevincq.calendarlibrary.R;
import com.kevincq.calendarlibrary.constant.MNConst;
import com.kevincq.calendarlibrary.model.Lunar;
import com.kevincq.calendarlibrary.model.MNCalendarItemModel;
import com.kevincq.calendarlibrary.model.MNCalendarVerticalConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maning on 2017/5/9.
 */

public class MNCalendarVerticalItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<MNCalendarItemModel> mDatas;

    private LayoutInflater layoutInflater;

    private Context context;

    private Calendar currentCalendar;

    private MNCalendarVerticalAdapter adapter;

    private String now_yyy_mm_dd;
    private Date nowDate = new Date();

    //配置信息
    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    public MNCalendarVerticalItemAdapter(Context context, ArrayList<MNCalendarItemModel> mDatas, Calendar currentCalendar, MNCalendarVerticalAdapter adapter, MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        this.context = context;
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.adapter = adapter;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        layoutInflater = LayoutInflater.from(this.context);

        now_yyy_mm_dd = MNConst.sdf_yyy_MM_dd.format(nowDate);
        try {
            nowDate = MNConst.sdf_yyy_MM_dd.parse(now_yyy_mm_dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_vertical_item, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            MNCalendarItemModel mnCalendarItemModel = mDatas.get(position);

            Date datePosition = mnCalendarItemModel.getDate();
            Lunar lunar = mnCalendarItemModel.getLunar();

            myViewHolder.itemView.setVisibility(View.VISIBLE);
            myViewHolder.tv_small.setVisibility(View.GONE);
            myViewHolder.iv_select_bg.setVisibility(View.GONE);
            myViewHolder.tv_small.setText("");
            myViewHolder.view_today.setVisibility(View.GONE);
            myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorSolar());
            myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorLunar());

            myViewHolder.tvDay.setText(String.valueOf(datePosition.getDate()));

            //不是本月的隐藏
            Date currentDate = currentCalendar.getTime();
            if (datePosition.getMonth() != currentDate.getMonth()) {
                myViewHolder.itemView.setVisibility(View.GONE);
            }

            //阴历的显示
//            if (mnCalendarVerticalConfig.isMnCalendar_showLunar()) {
//                myViewHolder.tv_small.setVisibility(View.VISIBLE);
//                String lunarDayString = LunarCalendarUtils.getLunarDayString(lunar.lunarDay);
//                myViewHolder.tv_small.setText(lunarDayString);
//            } else {
            myViewHolder.tv_small.setVisibility(View.GONE);
//            }

            //判断是不是当天
            String position_yyy_MM_dd = MNConst.sdf_yyy_MM_dd.format(datePosition);
            if (now_yyy_mm_dd.equals(position_yyy_MM_dd)) {
//                myViewHolder.tvDay.setText("今天");
                myViewHolder.view_today.setVisibility(View.VISIBLE);
            }

            //判断是不是点击了起始日期
            if (adapter.startDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
                adapter.startDate = datePosition;
                myViewHolder.iv_select_bg.setVisibility(View.VISIBLE);
                myViewHolder.iv_select_bg.setBackgroundResource(R.drawable.mn_selected_bg_start);

                myViewHolder.tv_small.setVisibility(View.GONE);
                myViewHolder.tv_small.setText("开始");
                myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                //动态修改颜色
                GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_select_bg.getBackground();
                myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
            }
            if (adapter.endDate != null && simpleDateFormat.format(adapter.endDate).equals(simpleDateFormat.format(datePosition))) {
                adapter.endDate = datePosition;
                myViewHolder.iv_select_bg.setVisibility(View.VISIBLE);
                myViewHolder.iv_select_bg.setBackgroundResource(R.drawable.mn_selected_bg_start);
                myViewHolder.iv_bg_right.setVisibility(View.INVISIBLE);
                myViewHolder.iv_bg_right.setBackgroundResource(R.drawable.mn_selected_bg_right);
                myViewHolder.iv_bg_left.setVisibility(View.VISIBLE);
                myViewHolder.tv_small.setVisibility(View.GONE);
                myViewHolder.tv_small.setText("结束");
                myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                //动态修改颜色
                GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_select_bg.getBackground();
                myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
            }
//            if (adapter.endDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
            if (adapter.endDate != null && adapter.startDate == datePosition) {
                myViewHolder.iv_bg_right.setVisibility(View.VISIBLE);
                myViewHolder.iv_bg_left.setVisibility(View.INVISIBLE);
                myViewHolder.iv_bg_left.setBackgroundResource(R.drawable.mn_selected_bg_left);
            }
            //判断是不是大于起始日期
            if (adapter.startDate != null && adapter.endDate != null) {
                if (datePosition.getTime() > adapter.startDate.getTime() && datePosition.getTime() < adapter.endDate.getTime()) {
//                if (simpleDateFormat.format(datePosition).compareTo(simpleDateFormat.format(adapter.startDate)) > 0 && simpleDateFormat.format(datePosition).compareTo(simpleDateFormat.format(adapter.endDate)) < 0) {
//                    myViewHolder.iv_bg.setVisibility(View.VISIBLE);
//                    myViewHolder.iv_bg.setBackgroundResource(R.drawable.mn_selected_bg_centre);
                    myViewHolder.iv_bg_right.setVisibility(View.VISIBLE);
                    myViewHolder.iv_bg_left.setVisibility(View.VISIBLE);
                    myViewHolder.iv_select_bg.setVisibility(View.VISIBLE);
                    if (position % 7 == 6) {
                        myViewHolder.iv_select_bg.setBackgroundResource(R.drawable.mn_selected_bg_right);
                    } else if (position % 7 == 0) {
                        myViewHolder.iv_select_bg.setBackgroundResource(R.drawable.mn_selected_bg_left);
                    }
                    myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                    myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                    //动态修改颜色
                    GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_select_bg.getBackground();
                    myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeBg());
                }
            }
            if (position % 7 == 6) {
                myViewHolder.iv_bg_right.setVisibility(View.INVISIBLE);
            } else if (position % 7 == 0) {
                myViewHolder.iv_bg_left.setVisibility(View.INVISIBLE);
            }
            //小于今天的颜色变灰
//            if (simpleDateFormat.format(datePosition).compareTo(simpleDateFormat.format(nowDate)) < 0) {
            if (datePosition.getTime() < nowDate.getTime()) {
                if (position % 7 == 6 || position % 7 == 0) {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#20F57223"));
                } else {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#20000000"));
                }

            } else {
                if (position % 7 == 6 || position % 7 == 0) {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#F57223"));
                } else {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#000000"));
                }
                if (adapter.startDate != null && simpleDateFormat.format(adapter.startDate).equals(simpleDateFormat.format(datePosition))) {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#ffffff"));
                }
                if (adapter.endDate != null && simpleDateFormat.format(adapter.endDate).equals(simpleDateFormat.format(datePosition))) {
                    myViewHolder.tvDay.setTextColor(Color.parseColor("#ffffff"));
                }
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MNCalendarItemModel mnCalendarItemModel = mDatas.get(position);

                    Date dateClick = mnCalendarItemModel.getDate();
                    //必须大于今天
                    if (dateClick.getTime() < nowDate.getTime()) {
                        Toast.makeText(context, "选择的日期必须大于今天", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (adapter.startDate != null && adapter.endDate != null) {
                        adapter.startDate = null;
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
                            if ((deteClickTime - deteStartTime) / 24 / 60 / 60 / 1000 > 60) {
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

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDay;
        private TextView tv_small;
        private ImageView iv_bg_left;
        private ImageView iv_bg_right;
        private ImageView iv_select_bg;
        private View view_today;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tv_small = (TextView) itemView.findViewById(R.id.tv_small);
            iv_bg_left = (ImageView) itemView.findViewById(R.id.iv_bg_left);
            iv_bg_right = (ImageView) itemView.findViewById(R.id.iv_bg_right);
            iv_select_bg = (ImageView) itemView.findViewById(R.id.iv_select_bg);
            view_today = itemView.findViewById(R.id.view_today);
        }

    }

}
