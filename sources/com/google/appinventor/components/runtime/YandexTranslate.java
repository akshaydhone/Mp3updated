package com.google.appinventor.components.runtime;

import android.app.Activity;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3Constants.Opcode;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "Use this component to translate words and sentences between different languages. This component needs Internet access, as it will request translations to the Yandex.Translate service. Specify the source and target language in the form source-target using two letter language codes. So\"en-es\" will translate from English to Spanish while \"es-ru\" will translate from Spanish to Russian. If you leave out the source language, the service will attempt to detect the source language. So providing just \"es\" will attempt to detect the source language and translate it to Spanish.<p /> This component is powered by the Yandex translation service.  See http://api.yandex.com/translate/ for more information, including the list of available languages and the meanings of the language codes and status codes. <p />Note: Translation happens asynchronously in the background. When the translation is complete, the \"GotTranslation\" event is triggered.", docUri = "voice/yandex-translate", iconName = "images/yandex.png", nonVisible = true, version = 1)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public final class YandexTranslate extends AndroidNonvisibleComponent {
    private static final String LOG_TAG = "YandexTranslate";
    public static final String YANDEX_TRANSLATE_SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    private final Activity activity;
    private final String defaultYandexKey;
    private final byte[] key1 = new byte[]{(byte) -127, Opcode.OUTPUT_READ, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.JR_FALSE, Opcode.JR_EQ32, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_LTEQF, Opcode.ARRAY_WRITE, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.OUTPUT_STEP_SYNC, (byte) -65, Opcode.KEEP_ALIVE, Opcode.CP_NEQ8, Opcode.OUTPUT_TEST, Opcode.MOVEF_8, Opcode.UI_WRITE, Opcode.OUTPUT_READ, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.JR_FALSE, Opcode.JR_EQF, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_LTEQF, Opcode.ARRAY_WRITE, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.OUTPUT_STEP_SYNC, (byte) -65, Opcode.KEEP_ALIVE, Opcode.CP_NEQ8, Opcode.OUTPUT_TEST, Opcode.MOVEF_16, Opcode.UI_BUTTON, Opcode.OUTPUT_READ, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.JR_FALSE, Opcode.JR_NEQ8, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_LTEQF, Opcode.ARRAY_WRITE, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.OUTPUT_STEP_SYNC, (byte) -65, Opcode.KEEP_ALIVE, Opcode.CP_NEQ8, Opcode.OUTPUT_TEST, Opcode.MOVEF_32, Opcode.UI_DRAW, Opcode.OUTPUT_READ, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.JR_FALSE, Opcode.JR_NEQ16, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_LTEQF, Opcode.ARRAY_WRITE, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.OUTPUT_STEP_SYNC, (byte) -65, Opcode.KEEP_ALIVE, Opcode.CP_NEQ8, Opcode.OUTPUT_TEST, Opcode.MOVEF_F, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8};
    private final byte[] key1x = new byte[]{(byte) -11, Opcode.MAILBOX_READ, Opcode.OR16, (byte) 35, Opcode.RL16, Opcode.JR, (byte) -127, Opcode.JR_GTEQ16, (byte) -13, Opcode.CP_NEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.CP_EQF, Opcode.JR_GT16, Opcode.TIMER_READ, Byte.MIN_VALUE, Opcode.BP2, Opcode.OUTPUT_SET_TYPE, Opcode.JR_LTF, (byte) -3, (byte) 14, Opcode.OUTPUT_TIME_SYNC, (byte) -99, Opcode.JR_GTEQF, Opcode.JR_LT8, Opcode.JR_LTEQF, Opcode.MOVE16_16, (byte) -98, Opcode.JR_LT32, (byte) -5, Opcode.CP_GT8, Opcode.OUTPUT_PRG_STOP, Opcode.CP_GTF, Opcode.JR_EQ32, Opcode.COM_WRITEDATA, Opcode.UI_BUTTON, Opcode.MATH, (byte) -14, Opcode.JR_LT8, (byte) -104, (byte) 8, (byte) -31, (byte) -99, Opcode.RL16, Opcode.MEMORY_WRITE, Opcode.JR_LTEQ32, Opcode.JR, Opcode.COM_SET, Opcode.MOVE16_16, (byte) -14, (byte) 28, (byte) -74, Opcode.CP_EQ16, Opcode.MOVEF_32, Opcode.UI_DRAW, Opcode.COM_GET, Opcode.BP_SET, Opcode.OUTPUT_SET_TYPE, Opcode.JR_GT8, Opcode.WRITE8, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.WRITE8, Opcode.JR_LTEQF, Opcode.PORT_CNV_OUTPUT, (byte) 35, (byte) 21, Opcode.COM_TEST, Opcode.PORT_CNV_INPUT, (byte) -11, (byte) 28, (byte) -32, Opcode.CP_GT32, Opcode.JR_GT16, Opcode.COM_TEST, Opcode.TIMER_READ, Opcode.RANDOM, (byte) -96, Opcode.MOVE16_32, Opcode.WRITE8, (byte) 6, Opcode.CP_GT32, (byte) -25, (byte) 26, Opcode.JR_EQ32};
    private String yandexKey;
    private final String yandexKeyDefaultTag = "DEFAULT";

    public YandexTranslate(ComponentContainer container) {
        super(container.$form());
        this.form.setYandexTranslateTagline();
        byte[] retval = new byte[this.key1.length];
        for (int i = 0; i < this.key1.length; i++) {
            retval[i] = (byte) (this.key1[i] ^ this.key1x[i]);
        }
        this.defaultYandexKey = new String(retval);
        this.yandexKey = "DEFAULT";
        this.activity = container.$context();
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), LOG_TAG);
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void YandexKey(String str) {
        this.yandexKey = str;
    }

    @SimpleFunction(description = "By providing a target language to translate to (for instance, 'es' for Spanish, 'en' for English, or 'ru' for Russian), and a word or sentence to translate, this method will request a translation to the Yandex.Translate service.\nOnce the text is translated by the external service, the event GotTranslation will be executed.\nNote: Yandex.Translate will attempt to detect the source language. You can also specify prepending it to the language translation. I.e., es-ru will specify Spanish to Russian translation.")
    public void RequestTranslation(final String languageToTranslateTo, final String textToTranslate) {
        if (this.yandexKey.equals("")) {
            this.form.dispatchErrorOccurredEvent(this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_NO_KEY_FOUND, new Object[0]);
        } else {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        YandexTranslate.this.performRequest(languageToTranslateTo, textToTranslate);
                    } catch (IOException e) {
                        YandexTranslate.this.form.dispatchErrorOccurredEvent(YandexTranslate.this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_SERVICE_NOT_AVAILABLE, new Object[0]);
                    } catch (JSONException e2) {
                        YandexTranslate.this.form.dispatchErrorOccurredEvent(YandexTranslate.this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_JSON_RESPONSE, new Object[0]);
                    }
                }
            });
        }
    }

    private void performRequest(String languageToTranslateTo, String textToTranslate) throws IOException, JSONException {
        String sendYandexKey;
        if (this.yandexKey.equals("DEFAULT")) {
            sendYandexKey = this.defaultYandexKey;
        } else {
            sendYandexKey = this.yandexKey;
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(YANDEX_TRANSLATE_SERVICE_URL + sendYandexKey + "&lang=" + languageToTranslateTo + "&text=" + URLEncoder.encode(textToTranslate, "UTF-8")).openConnection();
        if (connection != null) {
            try {
                JSONObject jsonResponse = new JSONObject(getResponseContent(connection));
                final String responseCode = jsonResponse.getString("code");
                final String translation = (String) jsonResponse.getJSONArray(PropertyTypeConstants.PROPERTY_TYPE_TEXT).get(0);
                this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        YandexTranslate.this.GotTranslation(responseCode, translation);
                    }
                });
            } finally {
                connection.disconnect();
            }
        }
    }

    private static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        InputStreamReader reader = new InputStreamReader(connection.getInputStream(), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = contentLength != -1 ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            while (true) {
                int read = reader.read(buf);
                if (read == -1) {
                    break;
                }
                sb.append(buf, 0, read);
            }
            String stringBuilder = sb.toString();
            return stringBuilder;
        } finally {
            reader.close();
        }
    }

    @SimpleEvent(description = "Event triggered when the Yandex.Translate service returns the translated text. This event also provides a response code for error handling. If the responseCode is not 200, then something went wrong with the call, and the translation will not be available.")
    public void GotTranslation(String responseCode, String translation) {
        EventDispatcher.dispatchEvent(this, "GotTranslation", responseCode, translation);
    }
}
