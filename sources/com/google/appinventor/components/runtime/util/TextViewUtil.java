package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.google.appinventor.components.runtime.Form;
import java.lang.reflect.Field;

public class TextViewUtil {
    private static Context ct;

    private TextViewUtil() {
    }

    public static void setContext(Context context) {
        ct = context;
    }

    public static void setAlignment(TextView textview, int alignment, boolean centerVertically) {
        int horizontalGravity;
        switch (alignment) {
            case 0:
                horizontalGravity = 3;
                break;
            case 1:
                horizontalGravity = 1;
                break;
            case 2:
                horizontalGravity = 5;
                break;
            default:
                throw new IllegalArgumentException();
        }
        textview.setGravity(horizontalGravity | (centerVertically ? 16 : 48));
        textview.invalidate();
    }

    public static void setBackgroundColor(TextView textview, int argb) {
        textview.setBackgroundColor(argb);
        textview.invalidate();
    }

    public static boolean isEnabled(TextView textview) {
        return textview.isEnabled();
    }

    public static void setEnabled(TextView textview, boolean enabled) {
        textview.setEnabled(enabled);
        textview.invalidate();
    }

    public static float getFontSize(TextView textview, Context context) {
        return textview.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static void setFontSize(TextView textview, float size) {
        textview.setTextSize(size);
        textview.requestLayout();
    }

    public static void setFontTypeface(TextView textview, int typeface, boolean bold, boolean italic) {
        Typeface tf;
        switch (typeface) {
            case 0:
                tf = Typeface.DEFAULT;
                break;
            case 1:
                tf = Typeface.SANS_SERIF;
                break;
            case 2:
                tf = Typeface.SERIF;
                break;
            case 3:
                tf = Typeface.MONOSPACE;
                break;
            case 4:
                tf = Typeface.createFromAsset(ct.getAssets(), "Thunkable-Roboto-Regular.ttf");
                break;
            case 5:
                tf = Typeface.createFromAsset(ct.getAssets(), "Thunkable-Roboto-Thin.ttf");
                break;
            default:
                throw new IllegalArgumentException();
        }
        int style = 0;
        if (bold) {
            style = 0 | 1;
        }
        if (italic) {
            style |= 2;
        }
        textview.setTypeface(Typeface.create(tf, style));
        textview.requestLayout();
    }

    public static void setCustomFontTypeface(Form form, TextView textview, String typefaceName, boolean bold, boolean italic) {
        Typeface tf;
        String typefacePath = MediaUtil.getAssetFilePath(form, typefaceName);
        if (typefacePath.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            tf = Typeface.createFromFile(typefacePath);
        } else {
            tf = Typeface.createFromAsset(ct.getAssets(), typefacePath);
        }
        int style = 0;
        if (bold) {
            style = 0 | 1;
        }
        if (italic) {
            style |= 2;
        }
        textview.setTypeface(Typeface.create(tf, style));
        textview.requestLayout();
    }

    public static String getText(TextView textview) {
        return textview.getText().toString();
    }

    public static void setTextHTML(TextView textview, String text) {
        textview.setClickable(true);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        textview.setText(Html.fromHtml(text));
        textview.requestLayout();
    }

    public static void setText(TextView textview, String text) {
        textview.setText(text);
        textview.requestLayout();
    }

    public static void setPadding(TextView textview, int padding) {
        textview.setPadding(padding, padding, 0, 0);
        textview.requestLayout();
    }

    public static void setTextColor(TextView textview, int argb) {
        textview.setTextColor(argb);
        textview.invalidate();
    }

    public static void setTextColors(TextView textview, ColorStateList colorStateList) {
        textview.setTextColor(colorStateList);
    }

    public static void setCursorColor(TextView textview, int argb) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(textview);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(textview);
            Field fCursorDrawable = editor.getClass().getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            if (mCursorDrawableRes > 0) {
                Drawable cursorDrawable = textview.getContext().getResources().getDrawable(mCursorDrawableRes);
                if (cursorDrawable != null) {
                    Drawable tintDrawable = tintDrawable(cursorDrawable, ColorStateList.valueOf(argb));
                    fCursorDrawable.set(editor, new Drawable[]{tintDrawable, tintDrawable});
                }
            }
        } catch (Throwable th) {
        }
    }

    private static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static void setMinWidth(TextView textview, int minWidth) {
        textview.setMinWidth(minWidth);
        textview.setMinimumWidth(minWidth);
    }

    public static void setMinHeight(TextView textview, int minHeight) {
        textview.setMinHeight(minHeight);
        textview.setMinimumHeight(minHeight);
    }

    public static void setMinSize(TextView textview, int minWidth, int minHeight) {
        setMinWidth(textview, minWidth);
        setMinHeight(textview, minHeight);
    }
}
