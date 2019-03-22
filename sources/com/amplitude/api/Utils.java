package com.amplitude.api;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static final String TAG = "com.amplitude.api.Utils";
    private static AmplitudeLog logger = AmplitudeLog.getLogger();

    static JSONObject cloneJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        JSONArray nameArray = null;
        try {
            nameArray = obj.names();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.m5e(TAG, e.toString());
        }
        int len = nameArray != null ? nameArray.length() : 0;
        String[] names = new String[len];
        for (int i = 0; i < len; i++) {
            names[i] = nameArray.optString(i);
        }
        try {
            return new JSONObject(obj, names);
        } catch (JSONException e2) {
            logger.m5e(TAG, e2.toString());
            return null;
        }
    }

    static boolean compareJSONObjects(JSONObject o1, JSONObject o2) {
        if (o1 == o2) {
            return true;
        }
        if ((o1 != null && o2 == null) || (o1 == null && o2 != null)) {
            return false;
        }
        try {
            if (o1.length() != o2.length()) {
                return false;
            }
            Iterator<?> keys = o1.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!o2.has(key)) {
                    return false;
                }
                Object value1 = o1.get(key);
                Object value2 = o2.get(key);
                if (!value1.getClass().equals(value2.getClass())) {
                    return false;
                }
                if (value1.getClass() == JSONObject.class) {
                    if (!compareJSONObjects((JSONObject) value1, (JSONObject) value2)) {
                        return false;
                    }
                } else if (!value1.equals(value2)) {
                    return false;
                }
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean isEmptyString(String s) {
        return s == null || s.length() == 0;
    }

    static String normalizeInstanceName(String instance) {
        if (isEmptyString(instance)) {
            instance = Constants.DEFAULT_INSTANCE;
        }
        return instance.toLowerCase();
    }
}
