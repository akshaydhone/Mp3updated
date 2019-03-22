package com.google.appinventor.components.runtime.util;

import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.content.FileProvider;
import android.util.Log;
import com.google.appinventor.components.runtime.Form;
import java.io.File;

public final class NougatUtil {
    private static final String LOG_TAG = NougatUtil.class.getSimpleName();

    private NougatUtil() {
    }

    public static Uri parseUri(Form form, String uriString) {
        if (uriString.startsWith("file://")) {
            return getPackageUri(form, new File(Uri.parse(uriString).getPath()));
        }
        return Uri.parse(uriString);
    }

    public static Uri getPackageUri(Form form, File apk) {
        if (VERSION.SDK_INT < 24) {
            return Uri.fromFile(apk);
        }
        String packageName = form.$context().getPackageName();
        Log.d(LOG_TAG, "packageName = " + packageName);
        return FileProvider.getUriForFile(form.$context(), packageName + ".provider", apk);
    }
}
