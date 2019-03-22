package com.amplitude.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.v4.os.EnvironmentCompat;
import android.util.Pair;
import com.amplitude.security.MD5;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AmplitudeClient {
    public static final String DEVICE_ID_KEY = "device_id";
    public static final String END_SESSION_EVENT = "session_end";
    public static final String LAST_EVENT_ID_KEY = "last_event_id";
    public static final String LAST_EVENT_TIME_KEY = "last_event_time";
    public static final String LAST_IDENTIFY_ID_KEY = "last_identify_id";
    public static final String OPT_OUT_KEY = "opt_out";
    public static final String PREVIOUS_SESSION_ID_KEY = "previous_session_id";
    public static final String SEQUENCE_NUMBER_KEY = "sequence_number";
    public static final String START_SESSION_EVENT = "session_start";
    public static final String TAG = "com.amplitude.api.AmplitudeClient";
    public static final String USER_ID_KEY = "user_id";
    private static final AmplitudeLog logger = AmplitudeLog.getLogger();
    protected String apiKey;
    private boolean backoffUpload;
    private int backoffUploadBatchSize;
    protected Context context;
    protected DatabaseHelper dbHelper;
    protected String deviceId;
    private DeviceInfo deviceInfo;
    private int eventMaxCount;
    private int eventUploadMaxBatchSize;
    private long eventUploadPeriodMillis;
    private int eventUploadThreshold;
    private boolean flushEventsOnClose;
    protected OkHttpClient httpClient;
    WorkerThread httpThread;
    private boolean inForeground;
    protected boolean initialized;
    protected String instanceName;
    Throwable lastError;
    long lastEventId;
    long lastEventTime;
    long lastIdentifyId;
    WorkerThread logThread;
    private long minTimeBetweenSessionsMillis;
    private boolean newDeviceIdPerInstall;
    private boolean offline;
    private boolean optOut;
    long previousSessionId;
    long sequenceNumber;
    long sessionId;
    private long sessionTimeoutMillis;
    private boolean trackingSessionEvents;
    private AtomicBoolean updateScheduled;
    AtomicBoolean uploadingCurrently;
    String url;
    private boolean useAdvertisingIdForDeviceId;
    protected String userId;
    private boolean usingForegroundTracking;

    /* renamed from: com.amplitude.api.AmplitudeClient$2 */
    class C02142 implements Runnable {
        C02142() {
        }

        public void run() {
            if (AmplitudeClient.this.deviceInfo == null) {
                throw new IllegalStateException("Must initialize before acting on location listening.");
            }
            AmplitudeClient.this.deviceInfo.setLocationListening(true);
        }
    }

    /* renamed from: com.amplitude.api.AmplitudeClient$3 */
    class C02153 implements Runnable {
        C02153() {
        }

        public void run() {
            if (AmplitudeClient.this.deviceInfo == null) {
                throw new IllegalStateException("Must initialize before acting on location listening.");
            }
            AmplitudeClient.this.deviceInfo.setLocationListening(false);
        }
    }

    public AmplitudeClient() {
        this(null);
    }

    public AmplitudeClient(String instance) {
        this.newDeviceIdPerInstall = false;
        this.useAdvertisingIdForDeviceId = false;
        this.initialized = false;
        this.optOut = false;
        this.offline = false;
        this.sessionId = -1;
        this.sequenceNumber = 0;
        this.lastEventId = -1;
        this.lastIdentifyId = -1;
        this.lastEventTime = -1;
        this.previousSessionId = -1;
        this.eventUploadThreshold = 30;
        this.eventUploadMaxBatchSize = 100;
        this.eventMaxCount = 1000;
        this.eventUploadPeriodMillis = Constants.EVENT_UPLOAD_PERIOD_MILLIS;
        this.minTimeBetweenSessionsMillis = Constants.MIN_TIME_BETWEEN_SESSIONS_MILLIS;
        this.sessionTimeoutMillis = Constants.SESSION_TIMEOUT_MILLIS;
        this.backoffUpload = false;
        this.backoffUploadBatchSize = this.eventUploadMaxBatchSize;
        this.usingForegroundTracking = false;
        this.trackingSessionEvents = false;
        this.inForeground = false;
        this.flushEventsOnClose = true;
        this.updateScheduled = new AtomicBoolean(false);
        this.uploadingCurrently = new AtomicBoolean(false);
        this.url = Constants.EVENT_LOG_URL;
        this.logThread = new WorkerThread("logThread");
        this.httpThread = new WorkerThread("httpThread");
        this.instanceName = Utils.normalizeInstanceName(instance);
        this.logThread.start();
        this.httpThread.start();
    }

    public AmplitudeClient initialize(Context context, String apiKey) {
        return initialize(context, apiKey, null);
    }

    public synchronized AmplitudeClient initialize(final Context context, String apiKey, final String userId) {
        AmplitudeClient this;
        if (context == null) {
            logger.m5e(TAG, "Argument context cannot be null in initialize()");
            this = this;
        } else if (Utils.isEmptyString(apiKey)) {
            logger.m5e(TAG, "Argument apiKey cannot be null or blank in initialize()");
            this = this;
        } else {
            this.context = context.getApplicationContext();
            this.apiKey = apiKey;
            this.dbHelper = DatabaseHelper.getDatabaseHelper(this.context, this.instanceName);
            final AmplitudeClient client = this;
            runOnLogThread(new Runnable() {
                public void run() {
                    if (!AmplitudeClient.this.initialized) {
                        try {
                            boolean z;
                            if (AmplitudeClient.this.instanceName.equals(Constants.DEFAULT_INSTANCE)) {
                                AmplitudeClient.upgradePrefs(context);
                                AmplitudeClient.upgradeSharedPrefsToDB(context);
                            }
                            AmplitudeClient.this.httpClient = new OkHttpClient();
                            AmplitudeClient.this.initializeDeviceInfo();
                            if (userId != null) {
                                client.userId = userId;
                                AmplitudeClient.this.dbHelper.insertOrReplaceKeyValue(AmplitudeClient.USER_ID_KEY, userId);
                            } else {
                                client.userId = AmplitudeClient.this.dbHelper.getValue(AmplitudeClient.USER_ID_KEY);
                            }
                            Long optOutLong = AmplitudeClient.this.dbHelper.getLongValue(AmplitudeClient.OPT_OUT_KEY);
                            AmplitudeClient amplitudeClient = AmplitudeClient.this;
                            if (optOutLong == null || optOutLong.longValue() != 1) {
                                z = false;
                            } else {
                                z = true;
                            }
                            amplitudeClient.optOut = z;
                            AmplitudeClient.this.previousSessionId = AmplitudeClient.this.getLongvalue(AmplitudeClient.PREVIOUS_SESSION_ID_KEY, -1);
                            if (AmplitudeClient.this.previousSessionId >= 0) {
                                AmplitudeClient.this.sessionId = AmplitudeClient.this.previousSessionId;
                            }
                            AmplitudeClient.this.sequenceNumber = AmplitudeClient.this.getLongvalue(AmplitudeClient.SEQUENCE_NUMBER_KEY, 0);
                            AmplitudeClient.this.lastEventId = AmplitudeClient.this.getLongvalue(AmplitudeClient.LAST_EVENT_ID_KEY, -1);
                            AmplitudeClient.this.lastIdentifyId = AmplitudeClient.this.getLongvalue(AmplitudeClient.LAST_IDENTIFY_ID_KEY, -1);
                            AmplitudeClient.this.lastEventTime = AmplitudeClient.this.getLongvalue(AmplitudeClient.LAST_EVENT_TIME_KEY, -1);
                            AmplitudeClient.this.initialized = true;
                        } catch (CursorWindowAllocationException e) {
                            AmplitudeClient.logger.m5e(AmplitudeClient.TAG, String.format("Failed to initialize Amplitude SDK due to: %s", new Object[]{e.getMessage()}));
                            client.apiKey = null;
                        }
                    }
                }
            });
            this = this;
        }
        return this;
    }

    public AmplitudeClient enableForegroundTracking(Application app) {
        if (!this.usingForegroundTracking && contextAndApiKeySet("enableForegroundTracking()") && VERSION.SDK_INT >= 14) {
            app.registerActivityLifecycleCallbacks(new AmplitudeCallbacks(this));
        }
        return this;
    }

    private void initializeDeviceInfo() {
        this.deviceInfo = new DeviceInfo(this.context);
        this.deviceId = initializeDeviceId();
        this.deviceInfo.prefetch();
    }

    public AmplitudeClient enableNewDeviceIdPerInstall(boolean newDeviceIdPerInstall) {
        this.newDeviceIdPerInstall = newDeviceIdPerInstall;
        return this;
    }

    public AmplitudeClient useAdvertisingIdForDeviceId() {
        this.useAdvertisingIdForDeviceId = true;
        return this;
    }

    public AmplitudeClient enableLocationListening() {
        runOnLogThread(new C02142());
        return this;
    }

    public AmplitudeClient disableLocationListening() {
        runOnLogThread(new C02153());
        return this;
    }

    public AmplitudeClient setEventUploadThreshold(int eventUploadThreshold) {
        this.eventUploadThreshold = eventUploadThreshold;
        return this;
    }

    public AmplitudeClient setEventUploadMaxBatchSize(int eventUploadMaxBatchSize) {
        this.eventUploadMaxBatchSize = eventUploadMaxBatchSize;
        this.backoffUploadBatchSize = eventUploadMaxBatchSize;
        return this;
    }

    public AmplitudeClient setEventMaxCount(int eventMaxCount) {
        this.eventMaxCount = eventMaxCount;
        return this;
    }

    public AmplitudeClient setEventUploadPeriodMillis(int eventUploadPeriodMillis) {
        this.eventUploadPeriodMillis = (long) eventUploadPeriodMillis;
        return this;
    }

    public AmplitudeClient setMinTimeBetweenSessionsMillis(long minTimeBetweenSessionsMillis) {
        this.minTimeBetweenSessionsMillis = minTimeBetweenSessionsMillis;
        return this;
    }

    public AmplitudeClient setSessionTimeoutMillis(long sessionTimeoutMillis) {
        this.sessionTimeoutMillis = sessionTimeoutMillis;
        return this;
    }

    public AmplitudeClient setOptOut(final boolean optOut) {
        if (contextAndApiKeySet("setOptOut()")) {
            final AmplitudeClient client = this;
            runOnLogThread(new Runnable() {
                public void run() {
                    if (!Utils.isEmptyString(AmplitudeClient.this.apiKey)) {
                        client.optOut = optOut;
                        AmplitudeClient.this.dbHelper.insertOrReplaceKeyLongValue(AmplitudeClient.OPT_OUT_KEY, Long.valueOf(optOut ? 1 : 0));
                    }
                }
            });
        }
        return this;
    }

    public boolean isOptedOut() {
        return this.optOut;
    }

    public AmplitudeClient enableLogging(boolean enableLogging) {
        logger.setEnableLogging(enableLogging);
        return this;
    }

    public AmplitudeClient setLogLevel(int logLevel) {
        logger.setLogLevel(logLevel);
        return this;
    }

    public AmplitudeClient setOffline(boolean offline) {
        this.offline = offline;
        if (!offline) {
            uploadEvents();
        }
        return this;
    }

    public AmplitudeClient setFlushEventsOnClose(boolean flushEventsOnClose) {
        this.flushEventsOnClose = flushEventsOnClose;
        return this;
    }

    public AmplitudeClient trackSessionEvents(boolean trackingSessionEvents) {
        this.trackingSessionEvents = trackingSessionEvents;
        return this;
    }

    void useForegroundTracking() {
        this.usingForegroundTracking = true;
    }

    boolean isUsingForegroundTracking() {
        return this.usingForegroundTracking;
    }

    boolean isInForeground() {
        return this.inForeground;
    }

    public void logEvent(String eventType) {
        logEvent(eventType, null);
    }

    public void logEvent(String eventType, JSONObject eventProperties) {
        logEvent(eventType, eventProperties, false);
    }

    public void logEvent(String eventType, JSONObject eventProperties, boolean outOfSession) {
        logEvent(eventType, eventProperties, null, outOfSession);
    }

    public void logEvent(String eventType, JSONObject eventProperties, JSONObject groups) {
        logEvent(eventType, eventProperties, groups, false);
    }

    public void logEvent(String eventType, JSONObject eventProperties, JSONObject groups, boolean outOfSession) {
        logEvent(eventType, eventProperties, groups, getCurrentTimeMillis(), outOfSession);
    }

    public void logEvent(String eventType, JSONObject eventProperties, JSONObject groups, long timestamp, boolean outOfSession) {
        if (validateLogEvent(eventType)) {
            logEventAsync(eventType, eventProperties, null, null, groups, timestamp, outOfSession);
        }
    }

    public void logEventSync(String eventType) {
        logEventSync(eventType, null);
    }

    public void logEventSync(String eventType, JSONObject eventProperties) {
        logEventSync(eventType, eventProperties, false);
    }

    public void logEventSync(String eventType, JSONObject eventProperties, boolean outOfSession) {
        logEventSync(eventType, eventProperties, null, outOfSession);
    }

    public void logEventSync(String eventType, JSONObject eventProperties, JSONObject groups) {
        logEventSync(eventType, eventProperties, groups, false);
    }

    public void logEventSync(String eventType, JSONObject eventProperties, JSONObject groups, boolean outOfSession) {
        logEventSync(eventType, eventProperties, groups, getCurrentTimeMillis(), outOfSession);
    }

    public void logEventSync(String eventType, JSONObject eventProperties, JSONObject groups, long timestamp, boolean outOfSession) {
        if (validateLogEvent(eventType)) {
            logEvent(eventType, eventProperties, null, null, groups, timestamp, outOfSession);
        }
    }

    protected boolean validateLogEvent(String eventType) {
        if (!Utils.isEmptyString(eventType)) {
            return contextAndApiKeySet("logEvent()");
        }
        logger.m5e(TAG, "Argument eventType cannot be null or blank in logEvent()");
        return false;
    }

    protected void logEventAsync(String eventType, JSONObject eventProperties, JSONObject apiProperties, JSONObject userProperties, JSONObject groups, long timestamp, boolean outOfSession) {
        if (eventProperties != null) {
            eventProperties = Utils.cloneJSONObject(eventProperties);
        }
        if (userProperties != null) {
            userProperties = Utils.cloneJSONObject(userProperties);
        }
        if (groups != null) {
            groups = Utils.cloneJSONObject(groups);
        }
        final JSONObject copyEventProperties = eventProperties;
        final JSONObject copyUserProperties = userProperties;
        final JSONObject copyGroups = groups;
        final String str = eventType;
        final JSONObject jSONObject = apiProperties;
        final long j = timestamp;
        final boolean z = outOfSession;
        runOnLogThread(new Runnable() {
            public void run() {
                if (!Utils.isEmptyString(AmplitudeClient.this.apiKey)) {
                    AmplitudeClient.this.logEvent(str, copyEventProperties, jSONObject, copyUserProperties, copyGroups, j, z);
                }
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected long logEvent(java.lang.String r19, org.json.JSONObject r20, org.json.JSONObject r21, org.json.JSONObject r22, org.json.JSONObject r23, long r24, boolean r26) {
        /*
        r18 = this;
        r12 = logger;
        r13 = "com.amplitude.api.AmplitudeClient";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Logged event to Amplitude: ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r14 = r14.toString();
        r12.m3d(r13, r14);
        r0 = r18;
        r12 = r0.optOut;
        if (r12 == 0) goto L_0x0025;
    L_0x0022:
        r10 = -1;
    L_0x0024:
        return r10;
    L_0x0025:
        r0 = r18;
        r12 = r0.trackingSessionEvents;
        if (r12 == 0) goto L_0x020c;
    L_0x002b:
        r12 = "session_start";
        r0 = r19;
        r12 = r0.equals(r12);
        if (r12 != 0) goto L_0x003f;
    L_0x0035:
        r12 = "session_end";
        r0 = r19;
        r12 = r0.equals(r12);
        if (r12 == 0) goto L_0x020c;
    L_0x003f:
        r9 = 1;
    L_0x0040:
        if (r9 != 0) goto L_0x0051;
    L_0x0042:
        if (r26 != 0) goto L_0x0051;
    L_0x0044:
        r0 = r18;
        r12 = r0.inForeground;
        if (r12 != 0) goto L_0x020f;
    L_0x004a:
        r0 = r18;
        r1 = r24;
        r0.startNewSessionIfNeeded(r1);
    L_0x0051:
        r10 = -1;
        r5 = new org.json.JSONObject;
        r5.<init>();
        r12 = "event_type";
        r13 = r18.replaceWithJSONNull(r19);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "timestamp";
        r0 = r24;
        r5.put(r12, r0);	 Catch:{ JSONException -> 0x0239 }
        r12 = "user_id";
        r0 = r18;
        r13 = r0.userId;	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "device_id";
        r0 = r18;
        r13 = r0.deviceId;	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r14 = "session_id";
        if (r26 == 0) goto L_0x0218;
    L_0x008a:
        r12 = -1;
    L_0x008c:
        r5.put(r14, r12);	 Catch:{ JSONException -> 0x0239 }
        r12 = "version_name";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getVersionName();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "os_name";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getOsName();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "os_version";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getOsVersion();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "device_brand";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getBrand();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "device_manufacturer";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getManufacturer();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "device_model";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getModel();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "carrier";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getCarrier();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "country";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getCountry();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "language";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getLanguage();	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r13 = r0.replaceWithJSONNull(r13);	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "platform";
        r13 = "Android";
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "uuid";
        r13 = java.util.UUID.randomUUID();	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.toString();	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "sequence_number";
        r14 = r18.getNextSequenceNumber();	 Catch:{ JSONException -> 0x0239 }
        r5.put(r12, r14);	 Catch:{ JSONException -> 0x0239 }
        r6 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r6.<init>();	 Catch:{ JSONException -> 0x0239 }
        r12 = "name";
        r13 = "amplitude-android";
        r6.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "version";
        r13 = "2.16.0";
        r6.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "library";
        r5.put(r12, r6);	 Catch:{ JSONException -> 0x0239 }
        if (r21 != 0) goto L_0x0178;
    L_0x0171:
        r12 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r12.<init>();	 Catch:{ JSONException -> 0x0239 }
        r21 = r12;
    L_0x0178:
        r0 = r18;
        r12 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r7 = r12.getMostRecentLocation();	 Catch:{ JSONException -> 0x0239 }
        if (r7 == 0) goto L_0x01a0;
    L_0x0182:
        r8 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r8.<init>();	 Catch:{ JSONException -> 0x0239 }
        r12 = "lat";
        r14 = r7.getLatitude();	 Catch:{ JSONException -> 0x0239 }
        r8.put(r12, r14);	 Catch:{ JSONException -> 0x0239 }
        r12 = "lng";
        r14 = r7.getLongitude();	 Catch:{ JSONException -> 0x0239 }
        r8.put(r12, r14);	 Catch:{ JSONException -> 0x0239 }
        r12 = "location";
        r0 = r21;
        r0.put(r12, r8);	 Catch:{ JSONException -> 0x0239 }
    L_0x01a0:
        r0 = r18;
        r12 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r12 = r12.getAdvertisingId();	 Catch:{ JSONException -> 0x0239 }
        if (r12 == 0) goto L_0x01b9;
    L_0x01aa:
        r12 = "androidADID";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.getAdvertisingId();	 Catch:{ JSONException -> 0x0239 }
        r0 = r21;
        r0.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
    L_0x01b9:
        r12 = "limit_ad_tracking";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.isLimitAdTrackingEnabled();	 Catch:{ JSONException -> 0x0239 }
        r0 = r21;
        r0.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "gps_enabled";
        r0 = r18;
        r13 = r0.deviceInfo;	 Catch:{ JSONException -> 0x0239 }
        r13 = r13.isGooglePlayServicesEnabled();	 Catch:{ JSONException -> 0x0239 }
        r0 = r21;
        r0.put(r12, r13);	 Catch:{ JSONException -> 0x0239 }
        r12 = "api_properties";
        r0 = r21;
        r5.put(r12, r0);	 Catch:{ JSONException -> 0x0239 }
        r13 = "event_properties";
        if (r20 != 0) goto L_0x021e;
    L_0x01e2:
        r12 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r12.<init>();	 Catch:{ JSONException -> 0x0239 }
    L_0x01e7:
        r5.put(r13, r12);	 Catch:{ JSONException -> 0x0239 }
        r13 = "user_properties";
        if (r22 != 0) goto L_0x0227;
    L_0x01ee:
        r12 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r12.<init>();	 Catch:{ JSONException -> 0x0239 }
    L_0x01f3:
        r5.put(r13, r12);	 Catch:{ JSONException -> 0x0239 }
        r13 = "groups";
        if (r23 != 0) goto L_0x0230;
    L_0x01fa:
        r12 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0239 }
        r12.<init>();	 Catch:{ JSONException -> 0x0239 }
    L_0x01ff:
        r5.put(r13, r12);	 Catch:{ JSONException -> 0x0239 }
        r0 = r18;
        r1 = r19;
        r10 = r0.saveEvent(r1, r5);	 Catch:{ JSONException -> 0x0239 }
        goto L_0x0024;
    L_0x020c:
        r9 = 0;
        goto L_0x0040;
    L_0x020f:
        r0 = r18;
        r1 = r24;
        r0.refreshSessionTime(r1);
        goto L_0x0051;
    L_0x0218:
        r0 = r18;
        r12 = r0.sessionId;	 Catch:{ JSONException -> 0x0239 }
        goto L_0x008c;
    L_0x021e:
        r0 = r18;
        r1 = r20;
        r12 = r0.truncate(r1);	 Catch:{ JSONException -> 0x0239 }
        goto L_0x01e7;
    L_0x0227:
        r0 = r18;
        r1 = r22;
        r12 = r0.truncate(r1);	 Catch:{ JSONException -> 0x0239 }
        goto L_0x01f3;
    L_0x0230:
        r0 = r18;
        r1 = r23;
        r12 = r0.truncate(r1);	 Catch:{ JSONException -> 0x0239 }
        goto L_0x01ff;
    L_0x0239:
        r4 = move-exception;
        r12 = logger;
        r13 = "com.amplitude.api.AmplitudeClient";
        r14 = "JSON Serialization of event type %s failed, skipping: %s";
        r15 = 2;
        r15 = new java.lang.Object[r15];
        r16 = 0;
        r15[r16] = r19;
        r16 = 1;
        r17 = r4.toString();
        r15[r16] = r17;
        r14 = java.lang.String.format(r14, r15);
        r12.m5e(r13, r14);
        goto L_0x0024;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amplitude.api.AmplitudeClient.logEvent(java.lang.String, org.json.JSONObject, org.json.JSONObject, org.json.JSONObject, org.json.JSONObject, long, boolean):long");
    }

    protected long saveEvent(String eventType, JSONObject event) {
        String eventString = event.toString();
        if (Utils.isEmptyString(eventString)) {
            logger.m5e(TAG, String.format("Detected empty event string for event type %s, skipping", new Object[]{eventType}));
            return -1;
        }
        if (eventType.equals(Constants.IDENTIFY_EVENT)) {
            this.lastIdentifyId = this.dbHelper.addIdentify(eventString);
            setLastIdentifyId(this.lastIdentifyId);
        } else {
            this.lastEventId = this.dbHelper.addEvent(eventString);
            setLastEventId(this.lastEventId);
        }
        int numEventsToRemove = Math.min(Math.max(1, this.eventMaxCount / 10), 20);
        if (this.dbHelper.getEventCount() > ((long) this.eventMaxCount)) {
            this.dbHelper.removeEvents(this.dbHelper.getNthEventId((long) numEventsToRemove));
        }
        if (this.dbHelper.getIdentifyCount() > ((long) this.eventMaxCount)) {
            this.dbHelper.removeIdentifys(this.dbHelper.getNthIdentifyId((long) numEventsToRemove));
        }
        long totalEventCount = this.dbHelper.getTotalEventCount();
        if (totalEventCount % ((long) this.eventUploadThreshold) != 0 || totalEventCount < ((long) this.eventUploadThreshold)) {
            updateServerLater(this.eventUploadPeriodMillis);
        } else {
            updateServer();
        }
        if (eventType.equals(Constants.IDENTIFY_EVENT)) {
            return this.lastIdentifyId;
        }
        return this.lastEventId;
    }

    private long getLongvalue(String key, long defaultValue) {
        Long value = this.dbHelper.getLongValue(key);
        return value == null ? defaultValue : value.longValue();
    }

    long getNextSequenceNumber() {
        this.sequenceNumber++;
        this.dbHelper.insertOrReplaceKeyLongValue(SEQUENCE_NUMBER_KEY, Long.valueOf(this.sequenceNumber));
        return this.sequenceNumber;
    }

    void setLastEventTime(long timestamp) {
        this.lastEventTime = timestamp;
        this.dbHelper.insertOrReplaceKeyLongValue(LAST_EVENT_TIME_KEY, Long.valueOf(timestamp));
    }

    void setLastEventId(long eventId) {
        this.lastEventId = eventId;
        this.dbHelper.insertOrReplaceKeyLongValue(LAST_EVENT_ID_KEY, Long.valueOf(eventId));
    }

    void setLastIdentifyId(long identifyId) {
        this.lastIdentifyId = identifyId;
        this.dbHelper.insertOrReplaceKeyLongValue(LAST_IDENTIFY_ID_KEY, Long.valueOf(identifyId));
    }

    public long getSessionId() {
        return this.sessionId;
    }

    void setPreviousSessionId(long timestamp) {
        this.previousSessionId = timestamp;
        this.dbHelper.insertOrReplaceKeyLongValue(PREVIOUS_SESSION_ID_KEY, Long.valueOf(timestamp));
    }

    boolean startNewSessionIfNeeded(long timestamp) {
        if (inSession()) {
            if (isWithinMinTimeBetweenSessions(timestamp)) {
                refreshSessionTime(timestamp);
                return false;
            }
            startNewSession(timestamp);
            return true;
        } else if (!isWithinMinTimeBetweenSessions(timestamp)) {
            startNewSession(timestamp);
            return true;
        } else if (this.previousSessionId == -1) {
            startNewSession(timestamp);
            return true;
        } else {
            setSessionId(this.previousSessionId);
            refreshSessionTime(timestamp);
            return false;
        }
    }

    private void startNewSession(long timestamp) {
        if (this.trackingSessionEvents) {
            sendSessionEvent(END_SESSION_EVENT);
        }
        setSessionId(timestamp);
        refreshSessionTime(timestamp);
        if (this.trackingSessionEvents) {
            sendSessionEvent(START_SESSION_EVENT);
        }
    }

    private boolean inSession() {
        return this.sessionId >= 0;
    }

    private boolean isWithinMinTimeBetweenSessions(long timestamp) {
        return timestamp - this.lastEventTime < (this.usingForegroundTracking ? this.minTimeBetweenSessionsMillis : this.sessionTimeoutMillis);
    }

    private void setSessionId(long timestamp) {
        this.sessionId = timestamp;
        setPreviousSessionId(timestamp);
    }

    void refreshSessionTime(long timestamp) {
        if (inSession()) {
            setLastEventTime(timestamp);
        }
    }

    private void sendSessionEvent(String sessionEvent) {
        if (contextAndApiKeySet(String.format("sendSessionEvent('%s')", new Object[]{sessionEvent})) && inSession()) {
            JSONObject apiProperties = new JSONObject();
            try {
                apiProperties.put("special", sessionEvent);
                logEvent(sessionEvent, null, apiProperties, null, null, this.lastEventTime, false);
            } catch (JSONException e) {
            }
        }
    }

    void onExitForeground(final long timestamp) {
        runOnLogThread(new Runnable() {
            public void run() {
                if (!Utils.isEmptyString(AmplitudeClient.this.apiKey)) {
                    AmplitudeClient.this.refreshSessionTime(timestamp);
                    AmplitudeClient.this.inForeground = false;
                    if (AmplitudeClient.this.flushEventsOnClose) {
                        AmplitudeClient.this.updateServer();
                    }
                }
            }
        });
    }

    void onEnterForeground(final long timestamp) {
        runOnLogThread(new Runnable() {
            public void run() {
                if (!Utils.isEmptyString(AmplitudeClient.this.apiKey)) {
                    AmplitudeClient.this.startNewSessionIfNeeded(timestamp);
                    AmplitudeClient.this.inForeground = true;
                }
            }
        });
    }

    public void logRevenue(double amount) {
        logRevenue(null, 1, amount);
    }

    public void logRevenue(String productId, int quantity, double price) {
        logRevenue(productId, quantity, price, null, null);
    }

    public void logRevenue(String productId, int quantity, double price, String receipt, String receiptSignature) {
        if (contextAndApiKeySet("logRevenue()")) {
            JSONObject apiProperties = new JSONObject();
            try {
                apiProperties.put("special", Constants.AMP_REVENUE_EVENT);
                apiProperties.put("productId", productId);
                apiProperties.put("quantity", quantity);
                apiProperties.put("price", price);
                apiProperties.put("receipt", receipt);
                apiProperties.put("receiptSig", receiptSignature);
            } catch (JSONException e) {
            }
            logEventAsync(Constants.AMP_REVENUE_EVENT, null, apiProperties, null, null, getCurrentTimeMillis(), false);
        }
    }

    public void logRevenueV2(Revenue revenue) {
        if (contextAndApiKeySet("logRevenueV2()") && revenue != null && revenue.isValidRevenue()) {
            logEvent(Constants.AMP_REVENUE_EVENT, revenue.toJSONObject());
        }
    }

    public void setUserProperties(JSONObject userProperties, boolean replace) {
        setUserProperties(userProperties);
    }

    public void setUserProperties(JSONObject userProperties) {
        if (userProperties != null && userProperties.length() != 0 && contextAndApiKeySet("setUserProperties")) {
            JSONObject sanitized = truncate(userProperties);
            if (sanitized.length() != 0) {
                Identify identify = new Identify();
                Iterator<?> keys = sanitized.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    try {
                        identify.setUserProperty(key, sanitized.get(key));
                    } catch (JSONException e) {
                        logger.m5e(TAG, e.toString());
                    }
                }
                identify(identify);
            }
        }
    }

    public void clearUserProperties() {
        identify(new Identify().clearAll());
    }

    public void identify(Identify identify) {
        identify(identify, false);
    }

    public void identify(Identify identify, boolean outOfSession) {
        if (identify != null && identify.userPropertiesOperations.length() != 0 && contextAndApiKeySet("identify()")) {
            logEventAsync(Constants.IDENTIFY_EVENT, null, null, identify.userPropertiesOperations, null, getCurrentTimeMillis(), outOfSession);
        }
    }

    public void setGroup(String groupType, Object groupName) {
        if (contextAndApiKeySet("setGroup()") && !Utils.isEmptyString(groupType)) {
            JSONObject group = null;
            try {
                group = new JSONObject().put(groupType, groupName);
            } catch (JSONException e) {
                logger.m5e(TAG, e.toString());
            }
            JSONObject jSONObject = null;
            logEventAsync(Constants.IDENTIFY_EVENT, null, jSONObject, new Identify().setUserProperty(groupType, groupName).userPropertiesOperations, group, getCurrentTimeMillis(), false);
        }
    }

    public JSONObject truncate(JSONObject object) {
        if (object == null) {
            return new JSONObject();
        }
        if (object.length() > 1000) {
            logger.m11w(TAG, "Warning: too many properties (more than 1000), ignoring");
            return new JSONObject();
        }
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                Object value = object.get(key);
                if (key.equals(Constants.AMP_REVENUE_RECEIPT) || key.equals(Constants.AMP_REVENUE_RECEIPT_SIG)) {
                    object.put(key, value);
                } else if (value.getClass().equals(String.class)) {
                    object.put(key, truncate((String) value));
                } else if (value.getClass().equals(JSONObject.class)) {
                    object.put(key, truncate((JSONObject) value));
                } else if (value.getClass().equals(JSONArray.class)) {
                    object.put(key, truncate((JSONArray) value));
                }
            } catch (JSONException e) {
                logger.m5e(TAG, e.toString());
            }
        }
        return object;
    }

    public JSONArray truncate(JSONArray array) throws JSONException {
        if (array == null) {
            return new JSONArray();
        }
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value.getClass().equals(String.class)) {
                array.put(i, truncate((String) value));
            } else if (value.getClass().equals(JSONObject.class)) {
                array.put(i, truncate((JSONObject) value));
            } else if (value.getClass().equals(JSONArray.class)) {
                array.put(i, truncate((JSONArray) value));
            }
        }
        return array;
    }

    public String truncate(String value) {
        return value.length() <= 1024 ? value : value.substring(0, 1024);
    }

    public String getUserId() {
        return this.userId;
    }

    public AmplitudeClient setUserId(final String userId) {
        if (contextAndApiKeySet("setUserId()")) {
            final AmplitudeClient client = this;
            runOnLogThread(new Runnable() {
                public void run() {
                    if (!Utils.isEmptyString(client.apiKey)) {
                        client.userId = userId;
                        AmplitudeClient.this.dbHelper.insertOrReplaceKeyValue(AmplitudeClient.USER_ID_KEY, userId);
                    }
                }
            });
        }
        return this;
    }

    public AmplitudeClient setDeviceId(final String deviceId) {
        Set<String> invalidDeviceIds = getInvalidDeviceIds();
        if (!(!contextAndApiKeySet("setDeviceId()") || Utils.isEmptyString(deviceId) || invalidDeviceIds.contains(deviceId))) {
            final AmplitudeClient client = this;
            runOnLogThread(new Runnable() {
                public void run() {
                    if (!Utils.isEmptyString(client.apiKey)) {
                        client.deviceId = deviceId;
                        AmplitudeClient.this.dbHelper.insertOrReplaceKeyValue(AmplitudeClient.DEVICE_ID_KEY, deviceId);
                    }
                }
            });
        }
        return this;
    }

    public AmplitudeClient regenerateDeviceId() {
        if (contextAndApiKeySet("regenerateDeviceId()")) {
            final AmplitudeClient client = this;
            runOnLogThread(new Runnable() {
                public void run() {
                    if (!Utils.isEmptyString(client.apiKey)) {
                        AmplitudeClient.this.setDeviceId(DeviceInfo.generateUUID() + "R");
                    }
                }
            });
        }
        return this;
    }

    public void uploadEvents() {
        if (contextAndApiKeySet("uploadEvents()")) {
            this.logThread.post(new Runnable() {
                public void run() {
                    if (!Utils.isEmptyString(AmplitudeClient.this.apiKey)) {
                        AmplitudeClient.this.updateServer();
                    }
                }
            });
        }
    }

    private void updateServerLater(long delayMillis) {
        if (!this.updateScheduled.getAndSet(true)) {
            this.logThread.postDelayed(new Runnable() {
                public void run() {
                    AmplitudeClient.this.updateScheduled.set(false);
                    AmplitudeClient.this.updateServer();
                }
            }, delayMillis);
        }
    }

    protected void updateServer() {
        updateServer(false);
    }

    protected void updateServer(boolean limit) {
        if (!this.optOut && !this.offline && !this.uploadingCurrently.getAndSet(true)) {
            long j;
            long totalEventCount = this.dbHelper.getTotalEventCount();
            if (limit) {
                j = (long) this.backoffUploadBatchSize;
            } else {
                j = (long) this.eventUploadMaxBatchSize;
            }
            long batchSize = Math.min(j, totalEventCount);
            if (batchSize <= 0) {
                this.uploadingCurrently.set(false);
                return;
            }
            try {
                Pair<Pair<Long, Long>, JSONArray> merged = mergeEventsAndIdentifys(this.dbHelper.getEvents(this.lastEventId, batchSize), this.dbHelper.getIdentifys(this.lastIdentifyId, batchSize), batchSize);
                if (merged.second.length() == 0) {
                    this.uploadingCurrently.set(false);
                    return;
                }
                final long maxEventId = ((Long) ((Pair) merged.first).first).longValue();
                final long maxIdentifyId = ((Long) ((Pair) merged.first).second).longValue();
                final String mergedEventsString = ((JSONArray) merged.second).toString();
                this.httpThread.post(new Runnable() {
                    public void run() {
                        AmplitudeClient.this.makeEventUploadPostRequest(AmplitudeClient.this.httpClient, mergedEventsString, maxEventId, maxIdentifyId);
                    }
                });
            } catch (JSONException e) {
                this.uploadingCurrently.set(false);
                logger.m5e(TAG, e.toString());
            } catch (CursorWindowAllocationException e2) {
                this.uploadingCurrently.set(false);
                logger.m5e(TAG, String.format("Caught Cursor window exception during event upload, deferring upload: %s", new Object[]{e2.getMessage()}));
            }
        }
    }

    protected Pair<Pair<Long, Long>, JSONArray> mergeEventsAndIdentifys(List<JSONObject> events, List<JSONObject> identifys, long numEvents) throws JSONException {
        JSONArray merged = new JSONArray();
        long maxEventId = -1;
        long maxIdentifyId = -1;
        while (((long) merged.length()) < numEvents) {
            boolean noEvents = events.isEmpty();
            boolean noIdentifys = identifys.isEmpty();
            if (noEvents && noIdentifys) {
                logger.m11w(TAG, String.format("mergeEventsAndIdentifys: number of events and identifys less than expected by %d", new Object[]{Long.valueOf(numEvents - ((long) merged.length()))}));
                break;
            } else if (noIdentifys) {
                event = (JSONObject) events.remove(0);
                maxEventId = event.getLong("event_id");
                merged.put(event);
            } else if (noEvents) {
                identify = (JSONObject) identifys.remove(0);
                maxIdentifyId = identify.getLong("event_id");
                merged.put(identify);
            } else if (!((JSONObject) events.get(0)).has(SEQUENCE_NUMBER_KEY) || ((JSONObject) events.get(0)).getLong(SEQUENCE_NUMBER_KEY) < ((JSONObject) identifys.get(0)).getLong(SEQUENCE_NUMBER_KEY)) {
                event = (JSONObject) events.remove(0);
                maxEventId = event.getLong("event_id");
                merged.put(event);
            } else {
                identify = (JSONObject) identifys.remove(0);
                maxIdentifyId = identify.getLong("event_id");
                merged.put(identify);
            }
        }
        return new Pair(new Pair(Long.valueOf(maxEventId), Long.valueOf(maxIdentifyId)), merged);
    }

    protected void makeEventUploadPostRequest(OkHttpClient client, String events, long maxEventId, long maxIdentifyId) {
        String apiVersionString = "2";
        String timestampString = "" + getCurrentTimeMillis();
        String checksumString = "";
        try {
            checksumString = bytesToHexString(new MD5().digest((apiVersionString + this.apiKey + events + timestampString).getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            logger.m5e(TAG, e.toString());
        }
        try {
            boolean uploadSuccess = false;
            try {
                Response response = client.newCall(new Builder().url(this.url).post(new FormBody.Builder().add("v", apiVersionString).add("client", this.apiKey).add("e", events).add("upload_time", timestampString).add("checksum", checksumString).build()).build()).execute();
                String stringResponse = response.body().string();
                if (stringResponse.equals("success")) {
                    uploadSuccess = true;
                    final long j = maxEventId;
                    final long j2 = maxIdentifyId;
                    this.logThread.post(new Runnable() {

                        /* renamed from: com.amplitude.api.AmplitudeClient$14$1 */
                        class C02121 implements Runnable {
                            C02121() {
                            }

                            public void run() {
                                AmplitudeClient.this.updateServer(AmplitudeClient.this.backoffUpload);
                            }
                        }

                        public void run() {
                            if (j >= 0) {
                                AmplitudeClient.this.dbHelper.removeEvents(j);
                            }
                            if (j2 >= 0) {
                                AmplitudeClient.this.dbHelper.removeIdentifys(j2);
                            }
                            AmplitudeClient.this.uploadingCurrently.set(false);
                            if (AmplitudeClient.this.dbHelper.getTotalEventCount() > ((long) AmplitudeClient.this.eventUploadThreshold)) {
                                AmplitudeClient.this.logThread.post(new C02121());
                                return;
                            }
                            AmplitudeClient.this.backoffUpload = false;
                            AmplitudeClient.this.backoffUploadBatchSize = AmplitudeClient.this.eventUploadMaxBatchSize;
                        }
                    });
                } else {
                    if (stringResponse.equals("invalid_api_key")) {
                        logger.m5e(TAG, "Invalid API key, make sure your API key is correct in initialize()");
                    } else {
                        if (stringResponse.equals("bad_checksum")) {
                            logger.m11w(TAG, "Bad checksum, post request was mangled in transit, will attempt to reupload later");
                        } else {
                            if (stringResponse.equals("request_db_write_failed")) {
                                logger.m11w(TAG, "Couldn't write to request database on server, will attempt to reupload later");
                            } else if (response.code() == 413) {
                                if (this.backoffUpload && this.backoffUploadBatchSize == 1) {
                                    if (maxEventId >= 0) {
                                        this.dbHelper.removeEvent(maxEventId);
                                    }
                                    if (maxIdentifyId >= 0) {
                                        this.dbHelper.removeIdentify(maxIdentifyId);
                                    }
                                }
                                this.backoffUpload = true;
                                this.backoffUploadBatchSize = (int) Math.ceil(((double) Math.min((int) this.dbHelper.getEventCount(), this.backoffUploadBatchSize)) / 2.0d);
                                logger.m11w(TAG, "Request too large, will decrease size and attempt to reupload");
                                this.logThread.post(new Runnable() {
                                    public void run() {
                                        AmplitudeClient.this.uploadingCurrently.set(false);
                                        AmplitudeClient.this.updateServer(true);
                                    }
                                });
                            } else {
                                logger.m11w(TAG, "Upload failed, " + stringResponse + ", will attempt to reupload later");
                            }
                        }
                    }
                }
            } catch (ConnectException e2) {
                this.lastError = e2;
            } catch (UnknownHostException e3) {
                this.lastError = e3;
            } catch (IOException e4) {
                logger.m5e(TAG, e4.toString());
                this.lastError = e4;
            } catch (AssertionError e5) {
                logger.m6e(TAG, "Exception:", e5);
                this.lastError = e5;
            } catch (Exception e6) {
                logger.m6e(TAG, "Exception:", e6);
                this.lastError = e6;
            }
            if (!uploadSuccess) {
                this.uploadingCurrently.set(false);
            }
        } catch (IllegalArgumentException e7) {
            logger.m5e(TAG, e7.toString());
            this.uploadingCurrently.set(false);
        }
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    private Set<String> getInvalidDeviceIds() {
        Set<String> invalidDeviceIds = new HashSet();
        invalidDeviceIds.add("");
        invalidDeviceIds.add("9774d56d682e549c");
        invalidDeviceIds.add(EnvironmentCompat.MEDIA_UNKNOWN);
        invalidDeviceIds.add("000000000000000");
        invalidDeviceIds.add(Constants.PLATFORM);
        invalidDeviceIds.add("DEFACE");
        invalidDeviceIds.add("00000000-0000-0000-0000-000000000000");
        return invalidDeviceIds;
    }

    private String initializeDeviceId() {
        Set<String> invalidIds = getInvalidDeviceIds();
        String deviceId = this.dbHelper.getValue(DEVICE_ID_KEY);
        if (!Utils.isEmptyString(deviceId) && !invalidIds.contains(deviceId)) {
            return deviceId;
        }
        if (!this.newDeviceIdPerInstall && this.useAdvertisingIdForDeviceId) {
            String advertisingId = this.deviceInfo.getAdvertisingId();
            if (!(Utils.isEmptyString(advertisingId) || invalidIds.contains(advertisingId))) {
                this.dbHelper.insertOrReplaceKeyValue(DEVICE_ID_KEY, advertisingId);
                return advertisingId;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        DeviceInfo deviceInfo = this.deviceInfo;
        String randomId = stringBuilder.append(DeviceInfo.generateUUID()).append("R").toString();
        this.dbHelper.insertOrReplaceKeyValue(DEVICE_ID_KEY, randomId);
        return randomId;
    }

    protected void runOnLogThread(Runnable r) {
        if (Thread.currentThread() != this.logThread) {
            this.logThread.post(r);
        } else {
            r.run();
        }
    }

    protected Object replaceWithJSONNull(Object obj) {
        return obj == null ? JSONObject.NULL : obj;
    }

    protected synchronized boolean contextAndApiKeySet(String methodName) {
        boolean z = false;
        synchronized (this) {
            if (this.context == null) {
                logger.m5e(TAG, "context cannot be null, set context with initialize() before calling " + methodName);
            } else if (Utils.isEmptyString(this.apiKey)) {
                logger.m5e(TAG, "apiKey cannot be null or empty, set apiKey with initialize() before calling " + methodName);
            } else {
                z = true;
            }
        }
        return z;
    }

    protected String bytesToHexString(byte[] bytes) {
        char[] hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    static boolean upgradePrefs(Context context) {
        return upgradePrefs(context, null, null);
    }

    static boolean upgradePrefs(Context context, String sourcePkgName, String targetPkgName) {
        if (sourcePkgName == null) {
            try {
                sourcePkgName = "com.amplitude.api";
                try {
                    sourcePkgName = Constants.class.getPackage().getName();
                } catch (Exception e) {
                }
            } catch (Exception e2) {
                logger.m6e(TAG, "Error upgrading shared preferences", e2);
                return false;
            }
        }
        if (targetPkgName == null) {
            targetPkgName = "com.amplitude.api";
        }
        if (targetPkgName.equals(sourcePkgName)) {
            return false;
        }
        String sourcePrefsName = sourcePkgName + "." + context.getPackageName();
        SharedPreferences source = context.getSharedPreferences(sourcePrefsName, 0);
        if (source.getAll().size() == 0) {
            return false;
        }
        String prefsName = targetPkgName + "." + context.getPackageName();
        Editor target = context.getSharedPreferences(prefsName, 0).edit();
        if (source.contains(sourcePkgName + ".previousSessionId")) {
            target.putLong(Constants.PREFKEY_PREVIOUS_SESSION_ID, source.getLong(sourcePkgName + ".previousSessionId", -1));
        }
        if (source.contains(sourcePkgName + ".deviceId")) {
            target.putString(Constants.PREFKEY_DEVICE_ID, source.getString(sourcePkgName + ".deviceId", null));
        }
        if (source.contains(sourcePkgName + ".userId")) {
            target.putString(Constants.PREFKEY_USER_ID, source.getString(sourcePkgName + ".userId", null));
        }
        if (source.contains(sourcePkgName + ".optOut")) {
            target.putBoolean(Constants.PREFKEY_OPT_OUT, source.getBoolean(sourcePkgName + ".optOut", false));
        }
        target.apply();
        source.edit().clear().apply();
        logger.m7i(TAG, "Upgraded shared preferences from " + sourcePrefsName + " to " + prefsName);
        return true;
    }

    static boolean upgradeSharedPrefsToDB(Context context) {
        return upgradeSharedPrefsToDB(context, null);
    }

    static boolean upgradeSharedPrefsToDB(Context context, String sourcePkgName) {
        if (sourcePkgName == null) {
            sourcePkgName = "com.amplitude.api";
        }
        DatabaseHelper dbHelper = DatabaseHelper.getDatabaseHelper(context);
        String deviceId = dbHelper.getValue(DEVICE_ID_KEY);
        Long previousSessionId = dbHelper.getLongValue(PREVIOUS_SESSION_ID_KEY);
        Long lastEventTime = dbHelper.getLongValue(LAST_EVENT_TIME_KEY);
        if (Utils.isEmptyString(deviceId) || previousSessionId == null || lastEventTime == null) {
            SharedPreferences preferences = context.getSharedPreferences(sourcePkgName + "." + context.getPackageName(), 0);
            migrateStringValue(preferences, Constants.PREFKEY_DEVICE_ID, null, dbHelper, DEVICE_ID_KEY);
            migrateLongValue(preferences, Constants.PREFKEY_LAST_EVENT_TIME, -1, dbHelper, LAST_EVENT_TIME_KEY);
            migrateLongValue(preferences, Constants.PREFKEY_LAST_EVENT_ID, -1, dbHelper, LAST_EVENT_ID_KEY);
            migrateLongValue(preferences, Constants.PREFKEY_LAST_IDENTIFY_ID, -1, dbHelper, LAST_IDENTIFY_ID_KEY);
            migrateLongValue(preferences, Constants.PREFKEY_PREVIOUS_SESSION_ID, -1, dbHelper, PREVIOUS_SESSION_ID_KEY);
            migrateStringValue(preferences, Constants.PREFKEY_USER_ID, null, dbHelper, USER_ID_KEY);
            migrateBooleanValue(preferences, Constants.PREFKEY_OPT_OUT, false, dbHelper, OPT_OUT_KEY);
        }
        return true;
    }

    private static void migrateLongValue(SharedPreferences prefs, String prefKey, long defValue, DatabaseHelper dbHelper, String dbKey) {
        if (dbHelper.getLongValue(dbKey) == null) {
            dbHelper.insertOrReplaceKeyLongValue(dbKey, Long.valueOf(prefs.getLong(prefKey, defValue)));
            prefs.edit().remove(prefKey).apply();
        }
    }

    private static void migrateStringValue(SharedPreferences prefs, String prefKey, String defValue, DatabaseHelper dbHelper, String dbKey) {
        if (Utils.isEmptyString(dbHelper.getValue(dbKey))) {
            String oldValue = prefs.getString(prefKey, defValue);
            if (!Utils.isEmptyString(oldValue)) {
                dbHelper.insertOrReplaceKeyValue(dbKey, oldValue);
                prefs.edit().remove(prefKey).apply();
            }
        }
    }

    private static void migrateBooleanValue(SharedPreferences prefs, String prefKey, boolean defValue, DatabaseHelper dbHelper, String dbKey) {
        if (dbHelper.getLongValue(dbKey) == null) {
            dbHelper.insertOrReplaceKeyLongValue(dbKey, Long.valueOf(prefs.getBoolean(prefKey, defValue) ? 1 : 0));
            prefs.edit().remove(prefKey).apply();
        }
    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
