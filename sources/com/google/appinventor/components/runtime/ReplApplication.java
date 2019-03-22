package com.google.appinventor.components.runtime;

import android.app.Application;
import android.content.Context;
import com.google.appinventor.components.runtime.multidex.MultiDex;

public class ReplApplication extends Application {
    public static boolean installed = true;
    private static ReplApplication thisInstance;
    private boolean active = false;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        installed = MultiDex.install(this, false);
    }

    public void onCreate() {
        super.onCreate();
        thisInstance = this;
    }

    public static void reportError(Throwable ex, String reportId) {
        reportError(ex);
    }

    public static void reportError(Throwable ex) {
    }

    public static boolean isAcraActive() {
        if (thisInstance == null || !thisInstance.active) {
            return false;
        }
        return true;
    }
}
