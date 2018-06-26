package com.auto.archcomponentsanalyst.router;


import com.auto.archcomponentsanalyst.WorkManager.WorkManagerActivity;

/**
 * Created by haohuidong on 18-6-11.
 */

public enum RoutPointer {
    WORKMANAGER(WorkManagerActivity.class.getName());

    String mRoutTarget = "";
    RoutPointer(String rout) {
        mRoutTarget = rout;
    }

    public String getTarget(){
        return mRoutTarget;
    }
}
