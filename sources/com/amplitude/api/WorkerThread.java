package com.amplitude.api;

import android.os.Handler;
import android.os.HandlerThread;

public class WorkerThread extends HandlerThread {
    private Handler handler;

    public WorkerThread(String name) {
        super(name);
    }

    Handler getHandler() {
        return this.handler;
    }

    void post(Runnable r) {
        waitForInitialization();
        this.handler.post(r);
    }

    void postDelayed(Runnable r, long delayMillis) {
        waitForInitialization();
        this.handler.postDelayed(r, delayMillis);
    }

    void removeCallbacks(Runnable r) {
        waitForInitialization();
        this.handler.removeCallbacks(r);
    }

    private synchronized void waitForInitialization() {
        if (this.handler == null) {
            this.handler = new Handler(getLooper());
        }
    }
}
