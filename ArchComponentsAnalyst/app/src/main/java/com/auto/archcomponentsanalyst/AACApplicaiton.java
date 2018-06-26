package com.auto.archcomponentsanalyst;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by haohuidong on 18-6-25.
 */

public class AACApplicaiton extends Application {

    /////////////////////////////////////////--> 18-6-25 下午7:18 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> singtone <-- ↓↓↓/////////////////////////////////////
    private static AACApplicaiton instance = null;

    public static AACApplicaiton getInstance() {
        return instance;
    }
    /////////////////////////////////////↑↑↑ --> singtone <-- ↑↑↑/////////////////////////////////////



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("--> ArchComponentsAnalyst <--", "-----------------------------< Process >---------------------------");
        instance = this;
    }
}
