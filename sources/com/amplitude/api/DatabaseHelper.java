package com.amplitude.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT);";
    private static final String CREATE_IDENTIFYS_TABLE = "CREATE TABLE IF NOT EXISTS identifys (id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT);";
    private static final String CREATE_LONG_STORE_TABLE = "CREATE TABLE IF NOT EXISTS long_store (key TEXT PRIMARY KEY NOT NULL, value INTEGER);";
    private static final String CREATE_STORE_TABLE = "CREATE TABLE IF NOT EXISTS store (key TEXT PRIMARY KEY NOT NULL, value TEXT);";
    private static final String EVENT_FIELD = "event";
    protected static final String EVENT_TABLE_NAME = "events";
    protected static final String IDENTIFY_TABLE_NAME = "identifys";
    private static final String ID_FIELD = "id";
    private static final String KEY_FIELD = "key";
    protected static final String LONG_STORE_TABLE_NAME = "long_store";
    protected static final String STORE_TABLE_NAME = "store";
    private static final String TAG = "com.amplitude.api.DatabaseHelper";
    private static final String VALUE_FIELD = "value";
    static final Map<String, DatabaseHelper> instances = new HashMap();
    private static final AmplitudeLog logger = AmplitudeLog.getLogger();
    private File file;
    private String instanceName;

    @Deprecated
    static DatabaseHelper getDatabaseHelper(Context context) {
        return getDatabaseHelper(context, null);
    }

    static synchronized DatabaseHelper getDatabaseHelper(Context context, String instance) {
        DatabaseHelper dbHelper;
        synchronized (DatabaseHelper.class) {
            instance = Utils.normalizeInstanceName(instance);
            dbHelper = (DatabaseHelper) instances.get(instance);
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(context.getApplicationContext(), instance);
                instances.put(instance, dbHelper);
            }
        }
        return dbHelper;
    }

    private static String getDatabaseName(String instance) {
        return (Utils.isEmptyString(instance) || instance.equals(Constants.DEFAULT_INSTANCE)) ? "com.amplitude.api" : "com.amplitude.api_" + instance;
    }

    protected DatabaseHelper(Context context) {
        this(context, null);
    }

    protected DatabaseHelper(Context context, String instance) {
        super(context, getDatabaseName(instance), null, 3);
        this.file = context.getDatabasePath(getDatabaseName(instance));
        this.instanceName = Utils.normalizeInstanceName(instance);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORE_TABLE);
        db.execSQL(CREATE_LONG_STORE_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_IDENTIFYS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > newVersion) {
            logger.m5e(TAG, "onUpgrade() with invalid oldVersion and newVersion");
            resetDatabase(db);
        } else if (newVersion > 1) {
            switch (oldVersion) {
                case 1:
                    db.execSQL(CREATE_STORE_TABLE);
                    if (newVersion <= 2) {
                        return;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    return;
                default:
                    logger.m5e(TAG, "onUpgrade() with unknown oldVersion " + oldVersion);
                    resetDatabase(db);
                    return;
            }
            db.execSQL(CREATE_IDENTIFYS_TABLE);
            db.execSQL(CREATE_LONG_STORE_TABLE);
            if (newVersion > 3) {
            }
        }
    }

    private void resetDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS store");
        db.execSQL("DROP TABLE IF EXISTS long_store");
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS identifys");
        onCreate(db);
    }

    synchronized long insertOrReplaceKeyValue(String key, String value) {
        long deleteKeyFromTable;
        if (value == null) {
            deleteKeyFromTable = deleteKeyFromTable(STORE_TABLE_NAME, key);
        } else {
            deleteKeyFromTable = insertOrReplaceKeyValueToTable(STORE_TABLE_NAME, key, value);
        }
        return deleteKeyFromTable;
    }

    synchronized long insertOrReplaceKeyLongValue(String key, Long value) {
        long deleteKeyFromTable;
        if (value == null) {
            deleteKeyFromTable = deleteKeyFromTable(LONG_STORE_TABLE_NAME, key);
        } else {
            deleteKeyFromTable = insertOrReplaceKeyValueToTable(LONG_STORE_TABLE_NAME, key, value);
        }
        return deleteKeyFromTable;
    }

    synchronized long insertOrReplaceKeyValueToTable(String table, String key, Object value) {
        long result;
        result = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_FIELD, key);
            if (value instanceof Long) {
                contentValues.put(VALUE_FIELD, (Long) value);
            } else {
                contentValues.put(VALUE_FIELD, (String) value);
            }
            result = db.insertWithOnConflict(table, null, contentValues, 5);
            if (result == -1) {
                logger.m11w(TAG, "Insert failed");
            }
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("insertOrReplaceKeyValue in %s failed", new Object[]{table}), e);
            delete();
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("insertOrReplaceKeyValue in %s failed", new Object[]{table}), e2);
            delete();
            close();
        } catch (Throwable th) {
            close();
        }
        return result;
    }

    synchronized long deleteKeyFromTable(String table, String key) {
        long result;
        result = -1;
        try {
            result = (long) getWritableDatabase().delete(table, "key=?", new String[]{key});
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("deleteKey from %s failed", new Object[]{table}), e);
            delete();
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("deleteKey from %s failed", new Object[]{table}), e2);
            delete();
            close();
        } catch (Throwable th) {
            close();
        }
        return result;
    }

    synchronized long addEvent(String event) {
        return addEventToTable(EVENT_TABLE_NAME, event);
    }

    synchronized long addIdentify(String identifyEvent) {
        return addEventToTable(IDENTIFY_TABLE_NAME, identifyEvent);
    }

    private synchronized long addEventToTable(String table, String event) {
        long result;
        result = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("event", event);
            result = db.insert(table, null, contentValues);
            if (result == -1) {
                logger.m11w(TAG, String.format("Insert into %s failed", new Object[]{table}));
            }
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("addEvent to %s failed", new Object[]{table}), e);
            delete();
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("addEvent to %s failed", new Object[]{table}), e2);
            delete();
            close();
        } catch (Throwable th) {
            close();
        }
        return result;
    }

    synchronized String getValue(String key) {
        return (String) getValueFromTable(STORE_TABLE_NAME, key);
    }

    synchronized Long getLongValue(String key) {
        return (Long) getValueFromTable(LONG_STORE_TABLE_NAME, key);
    }

    protected synchronized Object getValueFromTable(String table, String key) {
        Object obj;
        obj = null;
        Cursor cursor = null;
        try {
            String str = table;
            cursor = queryDb(getReadableDatabase(), str, new String[]{KEY_FIELD, VALUE_FIELD}, "key = ?", new String[]{key}, null, null, null, null);
            if (cursor.moveToFirst()) {
                if (table.equals(STORE_TABLE_NAME)) {
                    obj = cursor.getString(1);
                } else {
                    obj = Long.valueOf(cursor.getLong(1));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("getValue from %s failed", new Object[]{table}), e);
            delete();
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("getValue from %s failed", new Object[]{table}), e2);
            delete();
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (RuntimeException e3) {
            convertIfCursorWindowException(e3);
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }
        return obj;
    }

    synchronized List<JSONObject> getEvents(long upToId, long limit) throws JSONException {
        return getEventsFromTable(EVENT_TABLE_NAME, upToId, limit);
    }

    synchronized List<JSONObject> getIdentifys(long upToId, long limit) throws JSONException {
        return getEventsFromTable(IDENTIFY_TABLE_NAME, upToId, limit);
    }

    protected synchronized List<JSONObject> getEventsFromTable(String table, long upToId, long limit) throws JSONException {
        List<JSONObject> events;
        events = new LinkedList();
        Cursor cursor = null;
        try {
            String str;
            SQLiteDatabase db = getReadableDatabase();
            String[] strArr = new String[]{ID_FIELD, "event"};
            String str2 = upToId >= 0 ? "id <= " + upToId : null;
            String str3 = "id ASC";
            if (limit >= 0) {
                str = "" + limit;
            } else {
                str = null;
            }
            cursor = queryDb(db, table, strArr, str2, null, null, null, str3, str);
            while (cursor.moveToNext()) {
                long eventId = cursor.getLong(0);
                String event = cursor.getString(1);
                if (!Utils.isEmptyString(event)) {
                    JSONObject jSONObject = new JSONObject(event);
                    jSONObject.put("event_id", eventId);
                    events.add(jSONObject);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("getEvents from %s failed", new Object[]{table}), e);
            delete();
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("removeEvent from %s failed", new Object[]{table}), e2);
            delete();
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (RuntimeException e3) {
            convertIfCursorWindowException(e3);
            if (cursor != null) {
                cursor.close();
            }
            close();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }
        return events;
    }

    synchronized long getEventCount() {
        return getEventCountFromTable(EVENT_TABLE_NAME);
    }

    synchronized long getIdentifyCount() {
        return getEventCountFromTable(IDENTIFY_TABLE_NAME);
    }

    synchronized long getTotalEventCount() {
        return getEventCount() + getIdentifyCount();
    }

    private synchronized long getEventCountFromTable(String table) {
        long numberRows;
        numberRows = 0;
        SQLiteStatement statement = null;
        try {
            statement = getReadableDatabase().compileStatement("SELECT COUNT(*) FROM " + table);
            numberRows = statement.simpleQueryForLong();
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("getNumberRows for %s failed", new Object[]{table}), e);
            delete();
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("getNumberRows for %s failed", new Object[]{table}), e2);
            delete();
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            close();
        }
        return numberRows;
    }

    synchronized long getNthEventId(long n) {
        return getNthEventIdFromTable(EVENT_TABLE_NAME, n);
    }

    synchronized long getNthIdentifyId(long n) {
        return getNthEventIdFromTable(IDENTIFY_TABLE_NAME, n);
    }

    private synchronized long getNthEventIdFromTable(String table, long n) {
        long nthEventId;
        nthEventId = -1;
        SQLiteStatement statement = null;
        try {
            statement = getReadableDatabase().compileStatement("SELECT id FROM " + table + " LIMIT 1 OFFSET " + (n - 1));
            nthEventId = -1;
            try {
                nthEventId = statement.simpleQueryForLong();
            } catch (Throwable e) {
                logger.m13w(TAG, e);
            }
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (SQLiteException e2) {
            logger.m6e(TAG, String.format("getNthEventId from %s failed", new Object[]{table}), e2);
            delete();
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (StackOverflowError e3) {
            logger.m6e(TAG, String.format("getNthEventId from %s failed", new Object[]{table}), e3);
            delete();
            if (statement != null) {
                statement.close();
            }
            close();
        } catch (Throwable th) {
            if (statement != null) {
                statement.close();
            }
            close();
        }
        return nthEventId;
    }

    synchronized void removeEvents(long maxId) {
        removeEventsFromTable(EVENT_TABLE_NAME, maxId);
    }

    synchronized void removeIdentifys(long maxId) {
        removeEventsFromTable(IDENTIFY_TABLE_NAME, maxId);
    }

    private synchronized void removeEventsFromTable(String table, long maxId) {
        try {
            getWritableDatabase().delete(table, "id <= " + maxId, null);
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("removeEvents from %s failed", new Object[]{table}), e);
            delete();
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("removeEvents from %s failed", new Object[]{table}), e2);
            delete();
            close();
        } catch (Throwable th) {
            close();
        }
    }

    synchronized void removeEvent(long id) {
        removeEventFromTable(EVENT_TABLE_NAME, id);
    }

    synchronized void removeIdentify(long id) {
        removeEventFromTable(IDENTIFY_TABLE_NAME, id);
    }

    private synchronized void removeEventFromTable(String table, long id) {
        try {
            getWritableDatabase().delete(table, "id = " + id, null);
            close();
        } catch (SQLiteException e) {
            logger.m6e(TAG, String.format("removeEvent from %s failed", new Object[]{table}), e);
            delete();
            close();
        } catch (StackOverflowError e2) {
            logger.m6e(TAG, String.format("removeEvent from %s failed", new Object[]{table}), e2);
            delete();
            close();
        } catch (Throwable th) {
            close();
        }
    }

    private void delete() {
        try {
            close();
            this.file.delete();
        } catch (SecurityException e) {
            logger.m6e(TAG, "delete failed", e);
        }
    }

    boolean dbFileExists() {
        return this.file.exists();
    }

    Cursor queryDb(SQLiteDatabase db, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    private static void convertIfCursorWindowException(RuntimeException e) {
        String message = e.getMessage();
        if (Utils.isEmptyString(message) || !message.startsWith("Cursor window allocation of")) {
            throw e;
        }
        throw new CursorWindowAllocationException(message);
    }
}
