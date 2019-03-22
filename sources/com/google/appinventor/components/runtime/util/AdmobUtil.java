package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class AdmobUtil {
    private static final int ADMOBUTIL_VERSION = 1;
    private static final String ADMOB_BANNER_TYPE = "com.google.appinventor.components.runtime.ThunkableAdMobBanner";
    private static final String ADMOB_INTERSTITIAL_TYPE = "com.google.appinventor.components.runtime.ThunkableAdMobInterstitial";
    private static final String BUILT_TIME = "builtTime";
    private static final String LOG_TAG = "AdmobUtil";
    private static final String SCREEN_TYPE = "com.google.appinventor.components.runtime.Form";
    private static final String THUNKABLE_GRAPHQL_ENDPOINT = "https://admob.thunkable.com/graphql";
    private static final String THUNKABLE_LIVE_PACKAGE_NAME = "com.thunkable.appinventor.aicompanion3";
    private static String admobConfigKey = "admob_config";
    private static String admobConfigSharedPreferencesName;
    private static JSONObject appAdmobConfig;
    private static boolean didRequestTimeOutOrException;
    private static HashSet<AdmobLoader> pendingAdLoaders = new HashSet();
    private static JSONObject projectInfo;

    public interface AdmobLoader {
        void load();
    }

    private AdmobUtil() {
    }

    public static void initializeSetup(Context context, String eventType) {
        long expirationDate = 0;
        long timeNow = System.currentTimeMillis();
        admobConfigSharedPreferencesName = context.getPackageName() + "_admob_config";
        try {
            JSONObject defaultAppAdmobConfig = new JSONObject();
            defaultAppAdmobConfig.put("showAd", true);
            defaultAppAdmobConfig.put("bannerAdKey", "null");
            defaultAppAdmobConfig.put("intersitialAdKey", "null");
            defaultAppAdmobConfig.put("expirationDate", -1);
            appAdmobConfig = new JSONObject(getFromPreference(context, admobConfigKey, defaultAppAdmobConfig.toString()));
            expirationDate = appAdmobConfig.getLong("expirationDate");
        } catch (JSONException e) {
            Log.d(LOG_TAG, "The JSONException is " + e.getMessage());
        }
        if (expirationDate <= timeNow) {
            try {
                Scanner s = new Scanner(context.getAssets().open("project-info.txt")).useDelimiter("\\A");
                projectInfo = new JSONObject(s.hasNext() ? s.next() : "");
                fetchAdmobConfig(context, projectInfo);
            } catch (IOException e2) {
                Log.d(LOG_TAG, "The IOException is " + e2.getMessage());
            } catch (JSONException e3) {
                Log.d(LOG_TAG, "The JSONException is " + e3.getMessage());
            }
        }
    }

    public static void loadWhenReady(AdmobLoader loader) {
        if (isAdmobConfigStillValid() || didRequestTimeOutOrException) {
            loader.load();
        } else {
            pendingAdLoaders.add(loader);
        }
    }

    private static void loadPendingAds() {
        Iterator it = pendingAdLoaders.iterator();
        while (it.hasNext()) {
            ((AdmobLoader) it.next()).load();
        }
        pendingAdLoaders = new HashSet();
    }

    public static boolean shouldShowAd(Context context) {
        return getShowAdStatus();
    }

    public static boolean takeChance(Context context, String key) {
        if (key == null || "com.thunkable.appinventor.aicompanion3".equals(context.getPackageName())) {
            return false;
        }
        if (new Random().nextFloat() < getNumPercentageCut(context)) {
            return true;
        }
        return false;
    }

    private static String getInstallerPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            return pm.getInstallerPackageName(context.getPackageName());
        }
        return "";
    }

    private static void writeToPreference(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(admobConfigSharedPreferencesName, 0).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getFromPreference(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(admobConfigSharedPreferencesName, 0).getString(key, defaultValue);
    }

    public static String getBannerKey(Context context) {
        try {
            if (!isAdmobConfigStillValid()) {
                return null;
            }
            String key = appAdmobConfig.getString("bannerAdKey");
            if ("null".equals(key)) {
                return null;
            }
            return key;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getInterstitialKey(Context context) {
        try {
            if (!isAdmobConfigStillValid()) {
                return null;
            }
            String key = appAdmobConfig.getString("intersitialAdKey");
            if ("null".equals(key)) {
                return null;
            }
            return key;
        } catch (JSONException e) {
            return null;
        }
    }

    private static float getNumPercentageCut(Context context) {
        try {
            if (isAdmobConfigStillValid()) {
                return (float) appAdmobConfig.getLong("percentageCut");
            }
            return 0.0f;
        } catch (JSONException e) {
            return 0.0f;
        }
    }

    private static boolean getShowAdStatus() {
        boolean z = true;
        try {
            if (appAdmobConfig != null) {
                z = appAdmobConfig.getBoolean("showAd");
            }
        } catch (JSONException e) {
        }
        return z;
    }

    private static long getExpirationDate() {
        long j = -1;
        try {
            if (appAdmobConfig != null) {
                j = appAdmobConfig.getLong("expirationDate");
            }
        } catch (JSONException e) {
        }
        return j;
    }

    private static boolean isAdmobConfigStillValid() {
        long timeNow = System.currentTimeMillis();
        if (getExpirationDate() == -1 || getExpirationDate() == 0 || getExpirationDate() > timeNow) {
            return true;
        }
        return false;
    }

    private static void fetchAdmobConfig(final Context context, final JSONObject projectInfo) {
        didRequestTimeOutOrException = false;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    int admobBannerComponentCount = projectInfo.getInt(AdmobUtil.ADMOB_BANNER_TYPE);
                    int admobInterstitialComponentCount = projectInfo.getInt(AdmobUtil.ADMOB_INTERSTITIAL_TYPE);
                    int screenComponentCount = projectInfo.getInt(AdmobUtil.SCREEN_TYPE);
                    long builtTime = projectInfo.getLong(AdmobUtil.BUILT_TIME);
                    if (admobBannerComponentCount != 0 || admobInterstitialComponentCount != 0) {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost request = new HttpPost(AdmobUtil.THUNKABLE_GRAPHQL_ENDPOINT);
                        request.setHeader("Content-Type", "application/json");
                        request.setHeader("Request-Type", "admob");
                        JSONObject content = new JSONObject();
                        String packageName = context.getPackageName();
                        content.put("query", "{admob(packageName: \"" + packageName + "\", installerPackageName: \"" + AdmobUtil.getInstallerPackageName(context) + "\", admobBannerComponentCount: " + admobBannerComponentCount + ", admobInterstitialComponentCount: " + admobInterstitialComponentCount + ", screenComponentCount: " + screenComponentCount + ", builtTime: " + builtTime + ", version: " + 1 + ") {showAd bannerAdKey intersitialAdKey expirationDate percentageCut}}");
                        request.setEntity(new StringEntity(content.toString(), "UTF-8"));
                        HttpResponse response = httpclient.execute(request);
                        String responseContent = EntityUtils.toString(response.getEntity());
                        if (response.getStatusLine().getStatusCode() == 200) {
                            AdmobUtil.appAdmobConfig = (JSONObject) ((JSONObject) new JSONObject(responseContent).get("data")).get("admob");
                            AdmobUtil.writeToPreference(context, AdmobUtil.admobConfigKey, AdmobUtil.appAdmobConfig.toString());
                        } else {
                            AdmobUtil.didRequestTimeOutOrException = true;
                        }
                        AdmobUtil.loadPendingAds();
                    }
                } catch (Exception e) {
                    AdmobUtil.didRequestTimeOutOrException = true;
                    Log.d(AdmobUtil.LOG_TAG, "The exception is " + e.getMessage());
                }
            }
        });
    }

    public static Builder addNPA(Builder builder) {
        Bundle extrasBundle = new Bundle();
        extrasBundle.putString("npa", "1");
        return builder.addNetworkExtras(new AdMobExtras(extrasBundle));
    }
}
