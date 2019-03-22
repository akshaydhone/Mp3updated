package io.fabric.sdk.android;

public class SilentLogger implements Logger {
    private int logLevel = 7;

    public boolean isLoggable(String tag, int level) {
        return false;
    }

    /* renamed from: d */
    public void mo1330d(String tag, String text, Throwable throwable) {
    }

    /* renamed from: v */
    public void mo1341v(String tag, String text, Throwable throwable) {
    }

    /* renamed from: i */
    public void mo1335i(String tag, String text, Throwable throwable) {
    }

    /* renamed from: w */
    public void mo1343w(String tag, String text, Throwable throwable) {
    }

    /* renamed from: e */
    public void mo1332e(String tag, String text, Throwable throwable) {
    }

    /* renamed from: d */
    public void mo1329d(String tag, String text) {
    }

    /* renamed from: v */
    public void mo1340v(String tag, String text) {
    }

    /* renamed from: i */
    public void mo1334i(String tag, String text) {
    }

    /* renamed from: w */
    public void mo1342w(String tag, String text) {
    }

    /* renamed from: e */
    public void mo1331e(String tag, String text) {
    }

    public void log(int priority, String tag, String msg) {
    }

    public void log(int priority, String tag, String msg, boolean forceLog) {
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(int logLevel) {
    }
}
