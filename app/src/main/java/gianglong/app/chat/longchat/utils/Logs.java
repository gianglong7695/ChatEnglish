package gianglong.app.chat.longchat.utils;

import android.util.Log;

/**
 * Created by VCCORP on 6/23/2017.
 */

public class Logs {
    public static boolean IS_DEBUG = true;

    public static void d(String message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.d(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message);
        }

    }

    public static void e(String message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.e(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message);
        }

    }

    public static void e(int message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.e(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message + "");
        }

    }

    public static void e(long message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.e(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message + "");
        }

    }

    public static void i(String message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.i(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message);
        }

    }

    public static void w(String message) {
        StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
        if (IS_DEBUG) {
            Log.w(stackTraceElement.getFileName() + " in " + stackTraceElement.getMethodName() +
                    " at line: " + stackTraceElement.getLineNumber(), message);
        }

    }
}
