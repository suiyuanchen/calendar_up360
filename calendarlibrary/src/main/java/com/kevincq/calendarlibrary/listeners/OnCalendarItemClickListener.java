package com.kevincq.calendarlibrary.listeners;

import java.util.Date;

/**
 * Created by maning on 2017/5/9.
 */

public interface OnCalendarItemClickListener {

    void onClick(Date date);

    void onLongClick(Date date);

}
