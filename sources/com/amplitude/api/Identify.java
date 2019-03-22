package com.amplitude.api;

import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Identify {
    public static final String TAG = "com.amplitude.api.Identify";
    protected Set<String> userProperties = new HashSet();
    protected JSONObject userPropertiesOperations = new JSONObject();

    public Identify setOnce(String property, boolean value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, Boolean.valueOf(value));
        return this;
    }

    public Identify setOnce(String property, double value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, Double.valueOf(value));
        return this;
    }

    public Identify setOnce(String property, float value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, Float.valueOf(value));
        return this;
    }

    public Identify setOnce(String property, int value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, Integer.valueOf(value));
        return this;
    }

    public Identify setOnce(String property, long value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, Long.valueOf(value));
        return this;
    }

    public Identify setOnce(String property, String value) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, value);
        return this;
    }

    public Identify setOnce(String property, JSONArray values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, values);
        return this;
    }

    public Identify setOnce(String property, JSONObject values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, values);
        return this;
    }

    public Identify setOnce(String property, boolean[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, booleanArrayToJSONArray(values));
        return this;
    }

    public Identify setOnce(String property, double[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, doubleArrayToJSONArray(values));
        return this;
    }

    public Identify setOnce(String property, float[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, floatArrayToJSONArray(values));
        return this;
    }

    public Identify setOnce(String property, int[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, intArrayToJSONArray(values));
        return this;
    }

    public Identify setOnce(String property, long[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, longArrayToJSONArray(values));
        return this;
    }

    public Identify setOnce(String property, String[] values) {
        addToUserProperties(Constants.AMP_OP_SET_ONCE, property, stringArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, boolean value) {
        addToUserProperties(Constants.AMP_OP_SET, property, Boolean.valueOf(value));
        return this;
    }

    public Identify set(String property, double value) {
        addToUserProperties(Constants.AMP_OP_SET, property, Double.valueOf(value));
        return this;
    }

    public Identify set(String property, float value) {
        addToUserProperties(Constants.AMP_OP_SET, property, Float.valueOf(value));
        return this;
    }

    public Identify set(String property, int value) {
        addToUserProperties(Constants.AMP_OP_SET, property, Integer.valueOf(value));
        return this;
    }

    public Identify set(String property, long value) {
        addToUserProperties(Constants.AMP_OP_SET, property, Long.valueOf(value));
        return this;
    }

    public Identify set(String property, String value) {
        addToUserProperties(Constants.AMP_OP_SET, property, value);
        return this;
    }

    public Identify set(String property, JSONObject values) {
        addToUserProperties(Constants.AMP_OP_SET, property, values);
        return this;
    }

    public Identify set(String property, JSONArray values) {
        addToUserProperties(Constants.AMP_OP_SET, property, values);
        return this;
    }

    public Identify set(String property, boolean[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, booleanArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, double[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, doubleArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, float[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, floatArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, int[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, intArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, long[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, longArrayToJSONArray(values));
        return this;
    }

    public Identify set(String property, String[] values) {
        addToUserProperties(Constants.AMP_OP_SET, property, stringArrayToJSONArray(values));
        return this;
    }

    public Identify add(String property, double value) {
        addToUserProperties(Constants.AMP_OP_ADD, property, Double.valueOf(value));
        return this;
    }

    public Identify add(String property, float value) {
        addToUserProperties(Constants.AMP_OP_ADD, property, Float.valueOf(value));
        return this;
    }

    public Identify add(String property, int value) {
        addToUserProperties(Constants.AMP_OP_ADD, property, Integer.valueOf(value));
        return this;
    }

    public Identify add(String property, long value) {
        addToUserProperties(Constants.AMP_OP_ADD, property, Long.valueOf(value));
        return this;
    }

    public Identify add(String property, String value) {
        addToUserProperties(Constants.AMP_OP_ADD, property, value);
        return this;
    }

    public Identify add(String property, JSONObject values) {
        addToUserProperties(Constants.AMP_OP_ADD, property, values);
        return this;
    }

    public Identify append(String property, boolean value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, Boolean.valueOf(value));
        return this;
    }

    public Identify append(String property, double value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, Double.valueOf(value));
        return this;
    }

    public Identify append(String property, float value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, Float.valueOf(value));
        return this;
    }

    public Identify append(String property, int value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, Integer.valueOf(value));
        return this;
    }

    public Identify append(String property, long value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, Long.valueOf(value));
        return this;
    }

    public Identify append(String property, String value) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, value);
        return this;
    }

    public Identify append(String property, JSONArray values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, values);
        return this;
    }

    public Identify append(String property, JSONObject values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, values);
        return this;
    }

    public Identify append(String property, boolean[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, booleanArrayToJSONArray(values));
        return this;
    }

    public Identify append(String property, double[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, doubleArrayToJSONArray(values));
        return this;
    }

    public Identify append(String property, float[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, floatArrayToJSONArray(values));
        return this;
    }

    public Identify append(String property, int[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, intArrayToJSONArray(values));
        return this;
    }

    public Identify append(String property, long[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, longArrayToJSONArray(values));
        return this;
    }

    public Identify append(String property, String[] values) {
        addToUserProperties(Constants.AMP_OP_APPEND, property, stringArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, boolean value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, Boolean.valueOf(value));
        return this;
    }

    public Identify prepend(String property, double value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, Double.valueOf(value));
        return this;
    }

    public Identify prepend(String property, float value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, Float.valueOf(value));
        return this;
    }

    public Identify prepend(String property, int value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, Integer.valueOf(value));
        return this;
    }

    public Identify prepend(String property, long value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, Long.valueOf(value));
        return this;
    }

    public Identify prepend(String property, String value) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, value);
        return this;
    }

    public Identify prepend(String property, JSONArray values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, values);
        return this;
    }

    public Identify prepend(String property, JSONObject values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, values);
        return this;
    }

    public Identify prepend(String property, boolean[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, booleanArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, double[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, doubleArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, float[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, floatArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, int[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, intArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, long[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, longArrayToJSONArray(values));
        return this;
    }

    public Identify prepend(String property, String[] values) {
        addToUserProperties(Constants.AMP_OP_PREPEND, property, stringArrayToJSONArray(values));
        return this;
    }

    public Identify unset(String property) {
        addToUserProperties(Constants.AMP_OP_UNSET, property, "-");
        return this;
    }

    public Identify clearAll() {
        if (this.userPropertiesOperations.length() <= 0) {
            try {
                this.userPropertiesOperations.put(Constants.AMP_OP_CLEAR_ALL, "-");
            } catch (JSONException e) {
                AmplitudeLog.getLogger().m5e(TAG, e.toString());
            }
        } else if (!this.userProperties.contains(Constants.AMP_OP_CLEAR_ALL)) {
            AmplitudeLog.getLogger().m11w(TAG, String.format("Need to send $clearAll on its own Identify object without any other operations, ignoring $clearAll", new Object[0]));
        }
        return this;
    }

    private void addToUserProperties(String operation, String property, Object value) {
        if (Utils.isEmptyString(property)) {
            AmplitudeLog.getLogger().m11w(TAG, String.format("Attempting to perform operation %s with a null or empty string property, ignoring", new Object[]{operation}));
        } else if (value == null) {
            AmplitudeLog.getLogger().m11w(TAG, String.format("Attempting to perform operation %s with null value for property %s, ignoring", new Object[]{operation, property}));
        } else if (this.userPropertiesOperations.has(Constants.AMP_OP_CLEAR_ALL)) {
            AmplitudeLog.getLogger().m11w(TAG, String.format("This Identify already contains a $clearAll operation, ignoring operation %s", new Object[]{operation}));
        } else if (this.userProperties.contains(property)) {
            AmplitudeLog.getLogger().m11w(TAG, String.format("Already used property %s in previous operation, ignoring operation %s", new Object[]{property, operation}));
        } else {
            try {
                if (!this.userPropertiesOperations.has(operation)) {
                    this.userPropertiesOperations.put(operation, new JSONObject());
                }
                this.userPropertiesOperations.getJSONObject(operation).put(property, value);
                this.userProperties.add(property);
            } catch (JSONException e) {
                AmplitudeLog.getLogger().m5e(TAG, e.toString());
            }
        }
    }

    private JSONArray booleanArrayToJSONArray(boolean[] values) {
        JSONArray array = new JSONArray();
        for (boolean value : values) {
            array.put(value);
        }
        return array;
    }

    private JSONArray floatArrayToJSONArray(float[] values) {
        JSONArray array = new JSONArray();
        for (float value : values) {
            try {
                array.put((double) value);
            } catch (JSONException e) {
                AmplitudeLog.getLogger().m5e(TAG, String.format("Error converting float %f to JSON: %s", new Object[]{Float.valueOf(value), e.toString()}));
            }
        }
        return array;
    }

    private JSONArray doubleArrayToJSONArray(double[] values) {
        JSONArray array = new JSONArray();
        for (double value : values) {
            try {
                array.put(value);
            } catch (JSONException e) {
                AmplitudeLog.getLogger().m5e(TAG, String.format("Error converting double %d to JSON: %s", new Object[]{Double.valueOf(value), e.toString()}));
            }
        }
        return array;
    }

    private JSONArray intArrayToJSONArray(int[] values) {
        JSONArray array = new JSONArray();
        for (int value : values) {
            array.put(value);
        }
        return array;
    }

    private JSONArray longArrayToJSONArray(long[] values) {
        JSONArray array = new JSONArray();
        for (long value : values) {
            array.put(value);
        }
        return array;
    }

    private JSONArray stringArrayToJSONArray(String[] values) {
        JSONArray array = new JSONArray();
        for (String value : values) {
            array.put(value);
        }
        return array;
    }

    Identify setUserProperty(String property, Object value) {
        addToUserProperties(Constants.AMP_OP_SET, property, value);
        return this;
    }

    public Identify setOnce(String property, Object value) {
        AmplitudeLog.getLogger().m11w(TAG, "This version of setOnce is deprecated. Please use one with a different signature.");
        return this;
    }

    public Identify set(String property, Object value) {
        AmplitudeLog.getLogger().m11w(TAG, "This version of set is deprecated. Please use one with a different signature.");
        return this;
    }

    public JSONObject getUserPropertiesOperations() {
        try {
            return new JSONObject(this.userPropertiesOperations.toString());
        } catch (JSONException e) {
            AmplitudeLog.getLogger().m5e(TAG, e.toString());
            return new JSONObject();
        }
    }
}
