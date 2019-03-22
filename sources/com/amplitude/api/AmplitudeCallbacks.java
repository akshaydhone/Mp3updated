package com.amplitude.api;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class AmplitudeCallbacks implements ActivityLifecycleCallbacks {
    private static final String NULLMSG = "Need to initialize AmplitudeCallbacks with AmplitudeClient instance";
    public static final String TAG = "com.amplitude.api.AmplitudeCallbacks";
    private static AmplitudeLog logger = AmplitudeLog.getLogger();
    private AmplitudeClient clientInstance = null;

    public AmplitudeCallbacks(AmplitudeClient clientInstance) {
        if (clientInstance == null) {
            logger.m5e(TAG, NULLMSG);
            return;
        }
        this.clientInstance = clientInstance;
        clientInstance.useForegroundTracking();
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        if (this.clientInstance == null) {
            logger.m5e(TAG, NULLMSG);
        } else {
            this.clientInstance.onExitForeground(getCurrentTimeMillis());
        }
    }

    public void onActivityResumed(Activity activity) {
        if (this.clientInstance == null) {
            logger.m5e(TAG, NULLMSG);
        } else {
            this.clientInstance.onEnterForeground(getCurrentTimeMillis());
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outstate) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
