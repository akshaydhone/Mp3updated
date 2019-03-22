package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.amplitude.api.Amplitude;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileAnalytics {
    private static final String LOG_TAG = "MobileAnalytics";

    private MobileAnalytics() {
    }

    public static void fabricTracking(String packageName, String eventName) {
        Answers.getInstance().logCustom((CustomEvent) new CustomEvent(eventName).putCustomAttribute("Package Name", packageName));
    }

    public static void amplitudeTracking(String packageName, String eventName, String packageInstallerName, boolean usingThunkableKeys) {
        if (takeSample(0.01f)) {
            try {
                JSONObject json = new JSONObject();
                json.put("Package Name", packageName);
                if (packageInstallerName != null) {
                    json.put("Installer Name", packageInstallerName);
                }
                if (usingThunkableKeys) {
                    json.put("Using Thunkable Keys", usingThunkableKeys);
                }
                Amplitude.getInstance().logEvent(eventName, json);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JSONException is " + e.getLocalizedMessage());
            }
        }
    }

    private static boolean takeSample(float targetChance) {
        if (new Random().nextFloat() <= targetChance) {
            return true;
        }
        return false;
    }
}
