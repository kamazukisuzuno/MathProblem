package com.readboy.mathproblem;

import android.util.Log;

/**
 * Created by suzuno on 13-7-29.
 */
public class LogWriter {

    public void logError(String tag,String msg){
        if(Config.isDebugEnable){
            Log.e(tag,msg);
        }
    }

    public void logWarning(String tag,String msg){
        if(Config.isDebugEnable){
            Log.w(tag,msg);
        }
    }

    public void logVerbose(String tag,String msg){
        if(Config.isDebugEnable){
            Log.v(tag,msg);
        }
    }
}
