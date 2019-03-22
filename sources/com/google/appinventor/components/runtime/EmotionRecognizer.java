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
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil.FileException;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@DesignerComponent(category = ComponentCategory.ARTIFICIALINTELLIGENCE, description = "Non-visible component that provides access to the Microsoft Emotion API. Please refer to the <a href=\"https://www.microsoft.com/cognitive-services/\">Microsoft Cognitive Services</a> for more information.", docUri = "image/microsoft-image-recognizer", iconName = "images/MicrosoftEmotionRecognizer.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "httpcore-4.3.2.jar,httpmime-4.3.4.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class EmotionRecognizer extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "EmotionRecognizer";
    protected Activity activity;
    protected String defaultMicrosoftEmotionAPIUrl = "https://westus.api.cognitive.microsoft.com/face/v1.0/detect?returnFaceAttributes=emotion";
    protected String defaultSubscriptionKey = "7b15c5a74f6d4c70b1761fd274d7e524";
    protected String microsoftEmotionAPIUrl = "";
    protected String subscriptionKey = "";

    public EmotionRecognizer(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), LOG_TAG);
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void subscriptionKey(String str) {
        this.subscriptionKey = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The subscripition key for the Microsoft Emotion API.")
    public String subscriptionKey() {
        return this.subscriptionKey;
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void serverUrl(String str) {
        this.microsoftEmotionAPIUrl = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The server url for the Microsoft Emotion API.")
    public String serverUrl() {
        return this.microsoftEmotionAPIUrl;
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property.")
    public void postImage(final String path) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    EmotionRecognizer.this.postImageFile(path);
                } catch (FileException e) {
                    EmotionRecognizer.this.form.dispatchErrorOccurredEvent(EmotionRecognizer.this, "PostFile", e.getErrorMessageNumber(), new Object[0]);
                } catch (Exception e2) {
                    EmotionRecognizer.this.form.dispatchErrorOccurredEvent(EmotionRecognizer.this, "PostFile", ErrorMessages.ERROR_WEB_UNABLE_TO_POST_OR_PUT_FILE, path, EmotionRecognizer.this.serverUrl());
                }
            }
        });
    }

    protected void postImageFile(String picPath) throws IOException {
        HttpPost httpPost;
        HttpClient httpclient = new DefaultHttpClient();
        if (picPath == null) {
            picPath = "";
        }
        if (serverUrl().equals("") || serverUrl().equals("DEFAULT")) {
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
        String currentMostLikelyEmotion = "There is something wrong.";
        double currentMostLikelyEmotionScore = 0.0d;
        if (responseCode == 200) {
            try {
                JSONArray jSONArray = new JSONArray(responseContent);
                if (jSONArray.length() != 1) {
                    currentMostLikelyEmotion = "There are no face or more than one face in the picture. Please check the response content.";
                } else {
                    JSONObject face = jSONArray.getJSONObject(0);
                    if (face.has("faceAttributes")) {
                        if (face.getJSONObject("faceAttributes").has("emotion")) {
                            JSONObject emotions = face.getJSONObject("faceAttributes").getJSONObject("emotion");
                            Iterator<String> emotionIterator = emotions.keys();
                            while (emotionIterator.hasNext()) {
                                String emotion = (String) emotionIterator.next();
                                double score = emotions.getDouble(emotion);
                                if (score > currentMostLikelyEmotionScore) {
                                    currentMostLikelyEmotion = emotion;
                                    currentMostLikelyEmotionScore = score;
                                }
                            }
                        }
                    }
                    currentMostLikelyEmotion = "Emotion info is not included in response content";
                }
            } catch (JSONException e) {
                currentMostLikelyEmotion = "There is something wrong. " + e.toString();
            }
        }
        final String mostLikelyEmotion = currentMostLikelyEmotion;
        final double mostLikelyEmotionScore = currentMostLikelyEmotionScore;
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                EmotionRecognizer.this.GotResponse(responseCode, responseContent, mostLikelyEmotion, mostLikelyEmotionScore);
            }
        });
    }

    @SimpleEvent
    public void GotResponse(int responseCode, String responseContent, String mostLikelyEmotion, double mostLikelyEmotionScore) {
        EventDispatcher.dispatchEvent(this, "GotResponse", Integer.valueOf(responseCode), responseContent, mostLikelyEmotion, Double.valueOf(mostLikelyEmotionScore));
    }
}
