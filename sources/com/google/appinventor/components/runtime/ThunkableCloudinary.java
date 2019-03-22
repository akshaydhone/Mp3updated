package com.google.appinventor.components.runtime;

import android.app.Activity;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import com.google.appinventor.components.runtime.util.Ev3Constants.Opcode;
import com.google.appinventor.components.runtime.util.Ev3Constants.SystemCommand;
import com.google.appinventor.components.runtime.util.FileUtil.FileException;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@DesignerComponent(category = ComponentCategory.EXPERIMENTAL, description = "Non-visible component that provides access to the Cloudinary cloud storage. Please refer to the <a href=\"http://cloudinary.com/\">Cloudinary</a> for more information.", docUri = "storage/cloudinary-db", iconName = "images/cloudinary.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "cloudinary-android-1-4-5.jar, cloudinary-core-1-4-5.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class ThunkableCloudinary extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "ThunkableCloudinary";
    protected Activity activity;
    protected String cKey = "";
    protected String cSecret = "";
    protected String defaultCKey = "";
    protected String defaultCSecret = "";
    protected String defaultName = "";
    private final Form form;
    byte[] key1 = new byte[]{(byte) -127, Opcode.OUTPUT_READ, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.JR_FALSE, Opcode.JR_NEQ8, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_LTEQF, Opcode.ARRAY_WRITE, Opcode.MEMORY_WRITE, Opcode.UI_BUTTON, Opcode.MEMORY_READ, Opcode.CP_GTEQ8, Opcode.OUTPUT_TIME_SYNC, Opcode.OUTPUT_STEP_SYNC, (byte) -65, Opcode.KEEP_ALIVE, Opcode.CP_NEQ8, Opcode.OUTPUT_TEST, Opcode.MOVEF_32, Opcode.UI_WRITE, Opcode.STRINGS};
    byte[] key1x = new byte[]{(byte) -11, Opcode.FILE, Opcode.MOVE32_32, Opcode.MOVEF_32, Opcode.XOR32, (byte) 17, Opcode.COM_WRITEDATA, Opcode.MOVE32_F, Opcode.OUTPUT_POLARITY, (byte) 29, (byte) -17, (byte) 16, Opcode.RL16, Opcode.COM_TEST, Opcode.MAILBOX_WRITE, Opcode.COM_READDATA, (byte) -15, Opcode.OR32, Opcode.COM_READY, (byte) 15, Opcode.OUTPUT_STEP_SYNC, Opcode.CP_EQ32};
    byte[] key2 = new byte[]{(byte) 23, Opcode.MOVE16_F, Opcode.MOVE16_8, (byte) 12, Opcode.MOVE32_16, (byte) -14, Opcode.MOVE16_16, Opcode.MOVE8_16, Opcode.MOVE16_16, Opcode.MOVE16_32, Opcode.MOVE8_32, Opcode.MOVE16_32, Opcode.MOVE8_32, Opcode.MOVE8_8, Opcode.MOVE16_32};
    byte[] key2x = new byte[]{(byte) 35, (byte) 0, (byte) 1, Opcode.MOVE32_32, (byte) 13, Opcode.MEMORY_USAGE, (byte) 12, (byte) 5, (byte) 0, (byte) 7, (byte) 7, (byte) 0, (byte) 0, (byte) 1, (byte) 0};
    byte[] key3 = new byte[]{Opcode.CP_NEQ8, Opcode.JR_LTF, Opcode.COM_GET, Opcode.JR_NAN, (byte) 21, Opcode.PORT_CNV_OUTPUT, Opcode.JR_NEQF, (byte) -21, Opcode.PORT_CNV_INPUT, Opcode.JR_FALSE, Opcode.JR_NEQ16, Opcode.JR_TRUE, Opcode.MOVE32_16, Opcode.CP_GTEQ16, Opcode.JR_NEQ32, (byte) 6, Opcode.JR_LT32, (byte) 14, Opcode.MOVE8_32, Opcode.CP_GTEQ8, Opcode.JR_GTF, Opcode.JR_LT32, (byte) -9, Opcode.MOVE16_F, Opcode.CP_EQ8, Opcode.JR_EQF, Opcode.CP_GTEQ16};
    byte[] key3x = new byte[]{(byte) 0, (byte) 0, (byte) -2, (byte) 0, Opcode.JR_LT16, (byte) 0, (byte) 0, SystemCommand.CONTINUE_DOWNLOAD, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 23, (byte) 0, Opcode.CP_NEQ8, (byte) 0, Opcode.MOVE32_8, (byte) 0, (byte) 0, (byte) 0, (byte) 0, SystemCommand.CONTINUE_DOWNLOAD, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    protected String name = "";
    protected String packageName = "";
    byte[] retval1 = new byte[this.key1.length];
    byte[] retval2 = new byte[this.key2.length];
    byte[] retval3 = new byte[this.key3.length];

    public ThunkableCloudinary(ComponentContainer container) {
        int i;
        super(container.$form());
        this.activity = container.$context();
        this.form = container.$form();
        for (i = 0; i < this.retval1.length; i++) {
            this.retval1[i] = (byte) (this.key1[i] ^ this.key1x[i]);
        }
        for (i = 0; i < this.retval2.length; i++) {
            this.retval2[i] = (byte) (this.key2[i] ^ this.key2x[i]);
        }
        for (i = 0; i < this.retval3.length; i++) {
            this.retval3[i] = (byte) (this.key3[i] ^ this.key3x[i]);
        }
        this.defaultName = new String(this.retval1);
        this.defaultCKey = new String(this.retval2);
        this.defaultCSecret = new String(this.retval3);
        this.packageName = this.form.getPackageName();
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), LOG_TAG);
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void cloudName(String str) {
        this.name = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The Cloud Name for the Cloundinary API.")
    public String cloudName() {
        return this.name;
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void subscriptionKey(String str) {
        this.cKey = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The subscripition key for the Cloundinary API.")
    public String subscriptionKey() {
        return this.cKey;
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "string")
    @SimpleProperty
    public void subscriptionSecret(String str) {
        this.cSecret = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The subscripition secret for the Cloundinary API.")
    public String subscriptionSecret() {
        return this.cSecret;
    }

    @SimpleFunction(description = "Performs an HTTP POST request for image file.")
    public void postImage(final String path) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    ThunkableCloudinary.this.postMediaFile(0, path);
                } catch (FileException e) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", e.getErrorMessageNumber(), new Object[0]);
                } catch (Exception e2) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", ErrorMessages.ERROR_WEB_UNABLE_TO_POST_OR_PUT_FILE, path, "cloudinary");
                }
            }
        });
    }

    @SimpleFunction(description = "Performs an HTTP POST request for audio file.")
    public void postAudio(final String path) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    ThunkableCloudinary.this.postMediaFile(1, path);
                } catch (FileException e) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", e.getErrorMessageNumber(), new Object[0]);
                } catch (Exception e2) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", ErrorMessages.ERROR_WEB_UNABLE_TO_POST_OR_PUT_FILE, path, "cloudinary");
                }
            }
        });
    }

    @SimpleFunction(description = "Performs an HTTP POST request for video file.")
    public void postVideo(final String path) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    ThunkableCloudinary.this.postMediaFile(1, path);
                } catch (FileException e) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", e.getErrorMessageNumber(), new Object[0]);
                } catch (Exception e2) {
                    ThunkableCloudinary.this.form.dispatchErrorOccurredEvent(ThunkableCloudinary.this, "PostFile", ErrorMessages.ERROR_WEB_UNABLE_TO_POST_OR_PUT_FILE, path, "cloudinary");
                }
            }
        });
    }

    private void postMediaFile(int type, String picPath) throws IOException {
        String credential1 = "";
        String credential2 = "";
        String credential3 = "";
        if (this.name.equals("") || this.name.equals("DEFAULT")) {
            credential1 = this.defaultName;
        } else {
            credential1 = this.name;
        }
        if (this.cKey.equals("") || this.cKey.equals("DEFAULT")) {
            credential2 = this.defaultCKey;
        } else {
            credential2 = this.cKey;
        }
        if (this.cSecret.equals("") || this.cSecret.equals("DEFAULT")) {
            credential3 = this.defaultCSecret;
        } else {
            credential3 = this.cSecret;
        }
        BufferedInputStream is = new BufferedInputStream(MediaUtil.openMedia(this.form, picPath));
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(new Object[]{"cloud_name", credential1, "api_key", credential2, "api_secret", credential3}));
        String response = "";
        String url = "";
        Map<String, String> options = new HashMap();
        options.put("tags", this.packageName);
        if (type == 1) {
            options.put("resource_type", "video");
        }
        try {
            response = "successful";
            url = cloudinary.uploader().upload(is, options).get("secure_url").toString();
        } catch (Exception e) {
            response = e.toString();
            url = "";
        }
        final String responseContent = response;
        final String imageUrl = url;
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                ThunkableCloudinary.this.GotResponse(responseContent.toString(), imageUrl.toString());
            }
        });
    }

    @SimpleEvent
    public void GotResponse(String responseContent, String imageUrl) {
        EventDispatcher.dispatchEvent(this, "GotResponse", responseContent, imageUrl);
    }
}
