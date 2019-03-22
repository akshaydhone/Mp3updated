package com.google.appinventor.components.runtime.util;

import android.content.res.ColorStateList;
import android.telephony.PhoneNumberUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Locale;

public final class LollipopUtil {
    private LollipopUtil() {
    }

    public static void setTextBoxBackgroundTintList(EditText view, int colorCode) {
        view.getBackground().setTintList(ColorStateList.valueOf(colorCode));
    }

    public static void setCheckBoxButtonTintList(CheckBox view, int colorCode) {
        view.setButtonTintList(ColorStateList.valueOf(colorCode));
    }

    public static String formatNumber(String number) {
        return PhoneNumberUtils.formatNumber(number, Locale.getDefault().getCountry());
    }
}
