package com.auto.archcomponentsanalyst.router;

/**
 * Created by haohuidong on 18-6-11.
 */

public class RouterMap {

    /////////////////////////////////////////--> 18-6-11 下午3:30 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> pick target <-- ↓↓↓/////////////////////////////////////
    public static Class<?> pickTarget(RoutPointer pointer){
        try {
            try {
                return Class.forName(pointer.getTarget());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /////////////////////////////////////↑↑↑ --> pick target <-- ↑↑↑/////////////////////////////////////
}
