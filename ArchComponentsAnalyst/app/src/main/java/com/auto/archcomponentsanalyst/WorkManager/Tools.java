package com.auto.archcomponentsanalyst.WorkManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haohuidong on 18-6-25.
 */

public class Tools {

    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt = new Date(time);
        String dateTime = sdf.format(dt);
        return dateTime;
    }

}
