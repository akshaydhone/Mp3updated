package com.google.appinventor.components.runtime;

import android.app.Activity;
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
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3Constants.Opcode;
import com.google.appinventor.components.runtime.util.FileUtil.FileException;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@DesignerComponent(category = ComponentCategory.ARTIFICIALINTELLIGENCE, description = "Non-visible component that provides access to the Microsoft Computer Vision API. Please refer to the <a href=\"https://www.microsoft.com/cognitive-services/\">Microsoft Cognitive Services</a> for more information.", docUri = "image/microsoft-image-recognizer", iconName = "images/MicrosoftImageRecognizer.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "httpcore-4.3.2.jar,httpmime-4.3.4.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class ImageRecognizer extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "ImageRecognizer";
    protected Activity activity;
    protected String defaultMicrosoftEmotionAPIUrl = "https://westus.api.cognitive.microsoft.com/vision/v1.0/describe";
    protected String defaultSubscriptionKey = "";
    protected byte[] key1 = new byte[]{Opcode.CP_LT16, Opcode.PORT_CNV_INPUT, Opcode.MOVE32_8, Opcode.SELECTF, Opcode.MOVEF_8, (byte) -96, Opcode.JR_GT16, Opcode.SYSTEM, Opcode.MOVE8_16, Opcode.MOVE16_32, Opcode.MOVE16_32, Opcode.JR_LT32, Opcode.MOVE16_8, Opcode.MOVE8_16, Opcode.MOVE16_32, (byte) 17, Opcode.PORT_CNV_INPUT, Opcode.MOVE16_F, Opcode.SELECT32, Opcode.MOVEF_32, (byte) -13, Opcode.MOVEF_32, Opcode.JR_LTF, Opcode.MOVE16_32, Opcode.MOVE16_F, Opcode.MOVE8_F, Opcode.MOVE32_16, Opcode.MOVE16_16, Opcode.PORT_CNV_INPUT, Opcode.MOVE8_8, Opcode.MOVE8_16, Opcode.SYSTEM};
    protected byte[] key2 = new byte[]{(byte) 35, (byte) 0, (byte) 1, Opcode.MOVE32_32, (byte) 13, Opcode.MEMORY_USAGE, (byte) 12, (byte) 5, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 35, (byte) 0, (byte) 1, Opcode.MOVE32_32, (byte) 13, Opcode.MEMORY_USAGE, (byte) 12, (byte) 5, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 2};
    protected String microsoftEmotionAPIUrl = "";
    protected String subscriptionKey = "";

    public ImageRecognizer(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        byte[] retval = new byte[this.key1.length];
        for (int i = 0; i < this.key1.length; i++) {
            retval[i] = (byte) (this.key1[i] ^ this.key2[i]);
        }
        this.defaultSubscriptionKey = new String(retval);
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), LOG_TAG);
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void subscriptionKey(String str) {
        this.subscriptionKey = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The subscripition key for the Microsoft Computer Vision API.")
    public String subscriptionKey() {
        return this.subscriptionKey;
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void serverUrl(String str) {
        this.microsoftEmotionAPIUrl = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The server url for the Microsoft Computer Vision API.")
    public String serverUrl() {
        return this.microsoftEmotionAPIUrl;
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property.")
    public void postImage(final String path) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    ImageRecognizer.this.postImageFile(path);
                } catch (FileException e) {
                    ImageRecognizer.this.form.dispatchErrorOccurredEvent(ImageRecognizer.this, "PostFile", e.getErrorMessageNumber(), new Object[0]);
                } catch (Exception e2) {
                    ImageRecognizer.this.form.dispatchErrorOccurredEvent(ImageRecognizer.this, "PostFile", ErrorMessages.ERROR_WEB_UNABLE_TO_POST_OR_PUT_FILE, path, ImageRecognizer.this.serverUrl());
                }
            }
        });
    }

    private void postImageFile(String picPath) throws IOException {
        HttpPost httpPost;
        HttpClient httpclient = new DefaultHttpClient();
        if (picPath == null) {
            picPath = "";
        }
        if (subscriptionKey().equals("") || subscriptionKey().equals("DEFAULT")) {
            httpPost = new HttpPost(this.defaultMicrosoftEmotionAPIUrl);
        } else {
            httpPost = new HttpPost(this.microsoftEmotionAPIUrl);
        }
        if (subscriptionKey().equals("") || subscriptionKey().equals("DEFAULT")) {
            httpPost = request;
            httpPost.setHeader("Ocp-Apim-Subscription-Key", this.defaultSubscriptionKey);
        } else {
            httpPost = request;
            httpPost.setHeader("Ocp-Apim-Subscription-Key", this.subscriptionKey);
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(MediaUtil.openMedia(this.form, picPath));
        byte[] data = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(data);
        bufferedInputStream.close();
        ByteArrayEntity bae = new ByteArrayEntity(data);
        bae.setContentType("application/octet-stream");
        request.setEntity(bae);
        HttpResponse response = httpclient.execute(request);
        final String responseContent = EntityUtils.toString(response.getEntity());
        final int responseCode = response.getStatusLine().getStatusCode();
        String tags = "???";
        String description = "???";
        double descriptionScore = 0.0d;
        if (responseCode == 200) {
            try {
                int ind;
                JSONObject descriptionJson = new JSONObject(responseContent).getJSONObject("description");
                JSONArray tagsJson = descriptionJson.getJSONArray("tags");
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                for (ind = 0; ind < tagsJson.length(); ind++) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(',');
                    }
                    sb.append(tagsJson.getString(ind));
                }
                tags = sb.toString();
                JSONArray captions = descriptionJson.getJSONArray("captions");
                for (ind = 0; ind < captions.length(); ind++) {
                    JSONObject caption = captions.getJSONObject(ind);
                    double score = caption.getDouble("confidence");
                    if (score > descriptionScore) {
                        description = caption.getString(PropertyTypeConstants.PROPERTY_TYPE_TEXT);
                        descriptionScore = score;
                    }
                }
            } catch (JSONException e) {
            }
        }
        final String _tags = tags;
        final String _description = description;
        final double _descriptionScore = descriptionScore;
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                ImageRecognizer.this.GotResponse(responseCode, responseContent, _tags, _description, _descriptionScore);
            }
        });
    }

    @SimpleEvent
    public void GotResponse(int responseCode, String responseContent, String tags, String description, double descriptionScore) {
        EventDispatcher.dispatchEvent(this, "GotResponse", Integer.valueOf(responseCode), responseContent, tags, description, Double.valueOf(descriptionScore));
    }
}
