package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import com.google.appinventor.components.runtime.util.YailList;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

@DesignerComponent(category = ComponentCategory.DEPRECATED, description = "Non-visible component that provides access to the Cloudstitch spreadsheet storage. Please refer to the <a href=\"https://www.cloudstitch.com/\">Cloudstitch</a> for more information.", docUri = "storage/spreadsheets", iconName = "images/cloudstitch.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "cloudinary-android-1-4-5.jar, cloudinary-core-1-4-5.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class ThunkableCloudstitch extends AndroidNonvisibleComponent implements Component {
    private String LOG = "Cloudstitch";
    protected Activity activity;
    protected String apiEndpoint = "";
    protected Map<String, Integer> columnsMapping = new HashMap();
    protected String[][] dataIn2D = ((String[][]) Array.newInstance(String.class, new int[]{1600, 256}));
    private final Form form;
    protected String sheetName = "Sheet1";

    /* renamed from: com.google.appinventor.components.runtime.ThunkableCloudstitch$1 */
    class C04001 implements Runnable {
        C04001() {
        }

        public void run() {
            ThunkableCloudstitch.this.getData();
        }
    }

    public ThunkableCloudstitch(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        this.form = container.$form();
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), this.LOG);
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void apiEndpoint(String str) {
        this.apiEndpoint = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "")
    public String apiEndpoint() {
        return this.apiEndpoint;
    }

    @DesignerProperty(defaultValue = "Sheet1", editorType = "string")
    @SimpleProperty
    public void sheetName(String str) {
        this.sheetName = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "")
    public String sheetName() {
        return this.sheetName;
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property.")
    public void getAllData() {
        AsynchUtil.runAsynchronously(new C04001());
    }

    private void getData() {
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.apiEndpoint).openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_GET);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                result = result + line;
            }
            bufferedReader.close();
            if (conn.getResponseCode() == 200) {
                JSONArray a = (JSONArray) new JSONObject(result.toString()).get(this.sheetName);
                for (int i = 0; i < a.length(); i++) {
                    String[] keyValuePairs = a.get(i).toString().replace("{", "").replace("}", "").replace("\"", "").split(",");
                    if (i == 0) {
                        this.dataIn2D = (String[][]) Array.newInstance(String.class, new int[]{a.length(), keyValuePairs.length});
                    }
                    Map<String, String> map = new HashMap();
                    for (int j = 0; j < keyValuePairs.length; j++) {
                        String[] entry = keyValuePairs[j].split(":");
                        map.put(entry[0].trim(), entry[1].trim());
                        this.dataIn2D[i][j] = entry[1].trim();
                        if (i == 0) {
                            this.columnsMapping.put(entry[0].trim().toLowerCase(), Integer.valueOf(j));
                        }
                    }
                }
            } else {
                result = conn.getResponseMessage();
            }
        } catch (Exception e) {
            Log.i("Cloudstitch", e.getMessage());
            result = e.getMessage();
            this.form.dispatchErrorOccurredEvent(this, "AfterGetAllData", ErrorMessages.ERROR_WEB_UNABLE_TO_GET, this.apiEndpoint);
        }
        final String str = result.toString();
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                ThunkableCloudstitch.this.AfterGetAllData(str);
            }
        });
    }

    @SimpleFunction
    public YailList getColumnList(String columnName) {
        int columnNumber = ((Integer) this.columnsMapping.get(columnName.toLowerCase())).intValue();
        Object[] columnList = new String[this.dataIn2D.length];
        for (int j = 0; j < this.dataIn2D.length; j++) {
            columnList[j] = this.dataIn2D[j][columnNumber];
        }
        return YailList.makeList(columnList);
    }

    @SimpleFunction
    public YailList getRowList(int rowNumber) {
        String[] rowList = new String[this.dataIn2D[0].length];
        return YailList.makeList(this.dataIn2D[rowNumber - 1]);
    }

    @SimpleFunction
    public String getValue(String columnName, int rowNumber) {
        return this.dataIn2D[rowNumber - 1][((Integer) this.columnsMapping.get(columnName.toLowerCase())).intValue()];
    }

    @SimpleFunction
    public void postData(String data) {
        final String data2 = data;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                ThunkableCloudstitch.this.postTextData(data2);
            }
        });
    }

    public void postTextData(String data) {
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.apiEndpoint + "/" + this.sheetName).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(HttpRequest.METHOD_POST);
            conn.setRequestProperty("Content-Type", HttpRequest.CONTENT_TYPE_FORM);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length()));
            conn.getOutputStream().write(data.getBytes());
            result = conn.getResponseMessage();
        } catch (Exception e) {
            result = e.getMessage();
            Log.e(this.LOG, e.getMessage());
        }
        final String responseContent = result;
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                ThunkableCloudstitch.this.AfterUpload(responseContent);
            }
        });
    }

    @SimpleEvent
    public void AfterGetAllData(String responseContent) {
        EventDispatcher.dispatchEvent(this, "AfterGetAllData", responseContent);
    }

    @SimpleEvent
    public void AfterUpload(String responseContent) {
        EventDispatcher.dispatchEvent(this, "AfterUpload", responseContent);
    }
}
