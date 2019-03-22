package io.fabric.sdk.android;

public interface Logger {
    /* renamed from: d */
    void mo1329d(String str, String str2);

    /* renamed from: d */
    void mo1330d(String str, String str2, Throwable th);

    /* renamed from: e */
    void mo1331e(String str, String str2);

    /* renamed from: e */
    void mo1332e(String str, String str2, Throwable th);

    int getLogLevel();

    /* renamed from: i */
    void mo1334i(String str, String str2);

    /* renamed from: i */
    void mo1335i(String str, String str2, Throwable th);

    boolean isLoggable(String str, int i);

    void log(int i, String str, String str2);

    void log(int i, String str, String str2, boolean z);

    void setLogLevel(int i);

    /* renamed from: v */
    void mo1340v(String str, String str2);

    /* renamed from: v */
    void mo1341v(String str, String str2, Throwable th);

    /* renamed from: w */
    void mo1342w(String str, String str2);

    /* renamed from: w */
    void mo1343w(String str, String str2, Throwable th);
}
