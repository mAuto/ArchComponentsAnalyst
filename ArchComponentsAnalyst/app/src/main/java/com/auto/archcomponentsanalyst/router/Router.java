package com.auto.archcomponentsanalyst.router;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by haohuidong on 18-6-11.
 */

public class Router {

    /////////////////////////////////////////--> 18-6-11 下午3:25 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> Singletone <-- ↓↓↓/////////////////////////////////////
    private Router(){};

    private static class InstanceHolder{
        public final static Router mInstance = new Router();
    }

    public static Router getInstance(){
        return InstanceHolder.mInstance;
    }
    /////////////////////////////////////↑↑↑ --> Singletone <-- ↑↑↑/////////////////////////////////////

    /////////////////////////////////////////--> 18-6-11 下午3:28 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> Rout <-- ↓↓↓/////////////////////////////////////
    // jump without arguments
    public static void jump(Context from, RoutPointer pointer){
        jump(from, pointer, null);
    }

    // jump with arguments
    public static void jump(Context from, RoutPointer pointer, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(from, RouterMap.pickTarget(pointer));
        if (bundle != null)
            intent.putExtras(bundle);
        if (from instanceof Service){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        from.startActivity(intent);
    }
    /////////////////////////////////////↑↑↑ --> Rout <-- ↑↑↑/////////////////////////////////////
}
