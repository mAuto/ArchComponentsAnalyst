package com.auto.archcomponentsanalyst.WorkManager;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.Worker;

/**
 * Created by haohuidong on 18-6-11.
 */

public class SampleWorker extends Worker {
//    @NonNull
    @Override
    public Result doWork() {
        Data input = getInputData();
        String requestStr = input.getString(WMConstants.DATA_INPUT_KEY_REQUEST, WMConstants.DATA_INPUT_DEFAULT_REQUEST);
        String inputStr = input.getString(WMConstants.DATA_INPUT_KEY_CONTENT, WMConstants.DATA_OUTPUT_DEFAULT_VALUE);
        Log.e(WMConstants.TAG, requestStr + " : " + inputStr + " | start date:" + "enqueue date:"+formatTime(System.currentTimeMillis(), "HH:mm:ss") + " id:"+getId().toString());

        int  count = 0;
        StringBuilder outputStr = new StringBuilder();
        while (count < 18){
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                outputStr.append("->").append(formatTime(System.currentTimeMillis(), "HH:mm:SS"));
                Data output = new Data.Builder().putString(WMConstants.DATA_OUTPUT_KEY_REQUEST, requestStr).putString(WMConstants.DATA_OUTPUT_KEY_CONTENT, outputStr.toString()).build();
                setOutputData(output);
            } catch (InterruptedException e) {
                outputStr.append("->").append("0");
            }
            count += 1;
        }

        Data output = new Data.Builder().putString(WMConstants.DATA_OUTPUT_KEY_REQUEST, requestStr).putString(WMConstants.DATA_OUTPUT_KEY_CONTENT, outputStr.toString()).build();
        setOutputData(output);
        return Result.SUCCESS;
    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt = new Date(time);
        String dateTime = sdf.format(dt);
        return dateTime;
    }

}
