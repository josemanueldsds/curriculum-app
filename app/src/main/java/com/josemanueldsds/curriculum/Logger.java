package com.josemanueldsds.curriculum;

import android.util.Log;

/**
 * Common Logger Utility Class
 * Use this class for log messages in android console
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public final class Logger {


    /**
     * Log debug
     *
     * @param tag String Tag
     * @param msg String message
     */
    public static void log(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }


    /**
     * Log info
     *
     * @param tag String Tag
     * @param msg String message
     */
    public static void info(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null)
                Log.i(tag, msg);
        }
    }

    /**
     * Track any Exception
     *
     * @param tag String Tag
     * @param ex  Throwable Exception
     */
    public static void error(String tag, Throwable ex) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, ex.getMessage(), ex);
            ex.printStackTrace();
        }
    }
}
