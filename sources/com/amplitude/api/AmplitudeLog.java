package com.amplitude.api;

import android.util.Log;

public class AmplitudeLog {
    protected static AmplitudeLog instance = new AmplitudeLog();
    private volatile boolean enableLogging = true;
    private volatile int logLevel = 4;

    public static AmplitudeLog getLogger() {
        return instance;
    }

    private AmplitudeLog() {
    }

    AmplitudeLog setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
        return instance;
    }

    AmplitudeLog setLogLevel(int logLevel) {
        this.logLevel = logLevel;
        return instance;
    }

    /* renamed from: d */
    int m3d(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 3) {
            return 0;
        }
        return Log.d(tag, msg);
    }

    /* renamed from: d */
    int m4d(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 3) {
            return 0;
        }
        return Log.d(tag, msg, tr);
    }

    /* renamed from: e */
    int m5e(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 6) {
            return 0;
        }
        return Log.e(tag, msg);
    }

    /* renamed from: e */
    int m6e(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 6) {
            return 0;
        }
        return Log.e(tag, msg, tr);
    }

    String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    /* renamed from: i */
    int m7i(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 4) {
            return 0;
        }
        return Log.i(tag, msg);
    }

    /* renamed from: i */
    int m8i(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 4) {
            return 0;
        }
        return Log.i(tag, msg, tr);
    }

    boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    int println(int priority, String tag, String msg) {
        return Log.println(priority, tag, msg);
    }

    /* renamed from: v */
    int m9v(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 2) {
            return 0;
        }
        return Log.v(tag, msg);
    }

    /* renamed from: v */
    int m10v(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 2) {
            return 0;
        }
        return Log.v(tag, msg, tr);
    }

    /* renamed from: w */
    int m11w(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 5) {
            return 0;
        }
        return Log.w(tag, msg);
    }

    /* renamed from: w */
    int m13w(String tag, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 5) {
            return 0;
        }
        return Log.w(tag, tr);
    }

    /* renamed from: w */
    int m12w(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 5) {
            return 0;
        }
        return Log.w(tag, msg, tr);
    }

    int wtf(String tag, String msg) {
        if (!this.enableLogging || this.logLevel > 7) {
            return 0;
        }
        return Log.wtf(tag, msg);
    }

    int wtf(String tag, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 7) {
            return 0;
        }
        return Log.wtf(tag, tr);
    }

    int wtf(String tag, String msg, Throwable tr) {
        if (!this.enableLogging || this.logLevel > 7) {
            return 0;
        }
        return Log.wtf(tag, msg, tr);
    }
}
