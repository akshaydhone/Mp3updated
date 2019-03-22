package io.fabric.sdk.android;

import android.util.Log;

public class DefaultLogger implements Logger {
    private int logLevel;

    public DefaultLogger(int logLevel) {
        this.logLevel = logLevel;
    }

    public DefaultLogger() {
        this.logLevel = 4;
    }

    public boolean isLoggable(String tag, int level) {
        return this.logLevel <= level;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /* renamed from: d */
    public void mo1330d(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, 3)) {
            Log.d(tag, text, throwable);
        }
    }

    /* renamed from: v */
    public void mo1341v(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, 2)) {
            Log.v(tag, text, throwable);
        }
    }

    /* renamed from: i */
    public void mo1335i(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, 4)) {
            Log.i(tag, text, throwable);
        }
    }

    /* renamed from: w */
    public void mo1343w(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, 5)) {
            Log.w(tag, text, throwable);
        }
    }

    /* renamed from: e */
    public void mo1332e(String tag, String text, Throwable throwable) {
        if (isLoggable(tag, 6)) {
            Log.e(tag, text, throwable);
        }
    }

    /* renamed from: d */
    public void mo1329d(String tag, String text) {
        mo1330d(tag, text, null);
    }

    /* renamed from: v */
    public void mo1340v(String tag, String text) {
        mo1341v(tag, text, null);
    }

    /* renamed from: i */
    public void mo1334i(String tag, String text) {
        mo1335i(tag, text, null);
    }

    /* renamed from: w */
    public void mo1342w(String tag, String text) {
        mo1343w(tag, text, null);
    }

    /* renamed from: e */
    public void mo1331e(String tag, String text) {
        mo1332e(tag, text, null);
    }

    public void log(int priority, String tag, String msg) {
        log(priority, tag, msg, false);
    }

    public void log(int priority, String tag, String msg, boolean forceLog) {
        if (forceLog || isLoggable(tag, priority)) {
            Log.println(priority, tag, msg);
        }
    }
}
