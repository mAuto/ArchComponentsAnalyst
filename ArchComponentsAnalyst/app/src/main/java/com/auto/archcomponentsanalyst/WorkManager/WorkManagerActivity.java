package com.auto.archcomponentsanalyst.WorkManager;

import android.arch.lifecycle.Observer;
import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.auto.archcomponentsanalyst.R;
import com.auto.archcomponentsanalyst.WorkManager.db.CursorAction;
import com.auto.archcomponentsanalyst.WorkManager.db.DatabaseHelper;
import com.auto.archcomponentsanalyst.WorkManager.db.RequestPojo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.EnqueueRunnable;

public class WorkManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workmanager_activity_layout);

        initViews();
        fetchDataFromDB();
    }

    /////////////////////////////////////////--> 18-6-25 下午8:01 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> init view <-- ↓↓↓/////////////////////////////////////
    private RecyclerView mRvRequestList;
    private RequestAdapter mAdapter;

    private void initViews(){
        mRvRequestList = findViewById(R.id.rv_request_list);
        mRvRequestList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RequestAdapter(this);
        mRvRequestList.setAdapter(mAdapter);
    }

    private void fetchDataFromDB(){
        queryRequest();
    }
    /////////////////////////////////////↑↑↑ --> init view <-- ↑↑↑/////////////////////////////////////

    public void onClickOneTimeRequest(View view) {
        Data data = new Data.Builder().putString(WMConstants.DATA_INPUT_KEY_REQUEST, "OneTimeWorkRequest").putString(WMConstants.DATA_INPUT_KEY_CONTENT, "enqueue date:"+formatTime(System.currentTimeMillis(), "HH:mm:ss")).build();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data).build();
        saveRequest(0, mRequest.getId().toString());
        WorkManager.getInstance().enqueue(mRequest);
    }

    public void onClickPeriodicRequest(View view) {
        // 每12小时执行一次，执行时机不确定，可能在间隔的末尾也可能在任何实际成熟的时候。
        PeriodicWorkRequest mRequest0 = new PeriodicWorkRequest.Builder(SampleWorker.class
                , 12, TimeUnit.HOURS).build();

        // 每12小时执行一次，执行时机不确定，可能在间隔的末尾也可能在任何实际成熟的时候。
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            PeriodicWorkRequest mRequest1 = new PeriodicWorkRequest.Builder(SampleWorker.class
                    , Duration.ofHours(12)).build();
        }

        // 没12小时执行一次，但是从repeatInterval-flexInterval开始执行，且flexInterval <= repeatInterval。
        PeriodicWorkRequest mRequest2 = new PeriodicWorkRequest.Builder(SampleWorker.class
                , 12, TimeUnit.HOURS
                , 10, TimeUnit.HOURS).build();
    }

    private void test(){
        Data data = new Data.Builder().putString(WMConstants.DATA_INPUT_KEY_REQUEST, "OneTimeWorkRequest").putString(WMConstants.DATA_INPUT_KEY_CONTENT, "enqueue date:"+formatTime(System.currentTimeMillis(), "HH:mm:ss")).build();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data).build();
        WorkManager.getInstance().beginUniqueWork("unique", ExistingWorkPolicy.REPLACE, mRequest).enqueue();
    }

//    public void onClickFetchAvailableStatue(View view) {
//        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("work_id", "");
//        if (TextUtils.isEmpty(id))
//            return;
//        UUID uu = UUID.fromString(id);
//        WorkManager.getInstance().getStatusById(uu).observe(this, new Observer<WorkStatus>() {
//            @Override
//            public void onChanged(@Nullable WorkStatus workStatus) {
//                switch (workStatus.getState()){
//                    case ENQUEUED:{
//                        Log.e(WMConstants.TAG, "ENQUEUED");
//                    }break;
//                    case RUNNING:{
//                        Log.e(WMConstants.TAG, "RUNNING");
//                        Data output = workStatus.getOutputData();
//                        String requestStr = output.getString(WMConstants.DATA_OUTPUT_KEY_REQUEST, WMConstants.DATA_OUTPUT_DEFAULT_REQUEST);
//                        String outputStr = output.getString(WMConstants.DATA_OUTPUT_KEY_CONTENT, WMConstants.DATA_OUTPUT_DEFAULT_VALUE);
//                        Log.e(WMConstants.TAG, requestStr + " : "+outputStr);
//                    }break;
//                    case SUCCEEDED:{
//                        Log.e(WMConstants.TAG, "SUCCEEDED");
//                        Data output = workStatus.getOutputData();
//                        String requestStr = output.getString(WMConstants.DATA_OUTPUT_KEY_REQUEST, WMConstants.DATA_OUTPUT_DEFAULT_REQUEST);
//                        String outputStr = output.getString(WMConstants.DATA_OUTPUT_KEY_CONTENT, WMConstants.DATA_OUTPUT_DEFAULT_VALUE);
//                        Log.e(WMConstants.TAG, requestStr + " : "+outputStr);
//                        deleteRequest(workStatus.getId().toString());
//                    }break;
//                    case FAILED:{
//                        Log.e(WMConstants.TAG, "FAILED");
//                    }break;
//                    case BLOCKED:{
//                        Log.e(WMConstants.TAG, "BLOCKED");
//                    }break;
//                    case CANCELLED:{
//                        Log.e(WMConstants.TAG, "CANCELLED");
//                    }break;
//                }
//            }
//        });
//    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt = new Date(time);
        String dateTime = sdf.format(dt);
        return dateTime;
    }

    /////////////////////////////////////////--> 18-6-25 下午7:37 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> db <-- ↓↓↓/////////////////////////////////////
    private void saveRequest(int  type, String id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WM_REQUEST_ID, id);
        values.put(DatabaseHelper.COLUMN_WM_REQUEST_TYPE, type);
        values.put(DatabaseHelper.COLUMN_WM_REQUEST_ENQUEUE_DATE, System.currentTimeMillis());
        DatabaseHelper.getInstance().insertTableByName(DatabaseHelper.TABLE_WM_REQUEST, values);
        queryRequest();
    }

    private void queryRequest(){
        DatabaseHelper.getInstance().selectTableBySQL(DatabaseHelper.QUERY_WM_REQUEST+" order by "+ DatabaseHelper.COLUMN_WM_REQUEST_ENQUEUE_DATE+" desc", new CursorAction() {
            @Override
            public void action(Cursor cursor) {
                if (cursor != null){
                    List<RequestPojo> requests = new ArrayList<>();
                    RequestPojo varRequest;
                    while (cursor.moveToNext()){
                        varRequest = new RequestPojo();
                        varRequest.id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WM_REQUEST_ID));
                        varRequest.enqueueDate = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WM_REQUEST_ENQUEUE_DATE));
                        varRequest.type = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WM_REQUEST_TYPE));
                        requests.add(varRequest);
                    }
                    mAdapter.setDatas((ArrayList<RequestPojo>) requests);
                }
            }
        });
    }
    /////////////////////////////////////↑↑↑ --> db <-- ↑↑↑/////////////////////////////////////

}
