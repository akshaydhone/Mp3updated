package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.runtime.util.PaintUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.theme.ClassicThemeHelper;
import com.google.appinventor.components.runtime.util.theme.HoneycombThemeHelper;
import com.google.appinventor.components.runtime.util.theme.IceCreamSandwichThemeHelper;
import com.google.appinventor.components.runtime.util.theme.ThemeHelper;
import io.fabric.sdk.android.services.common.CommonUtils;

public class AppInventorCompatActivity extends FragmentActivity implements AppCompatCallback {
    static final int DEFAULT_PRIMARY_COLOR = PaintUtil.hexStringToInt(ComponentConstants.DEFAULT_PRIMARY_COLOR);
    private static final String LOG_TAG = AppInventorCompatActivity.class.getSimpleName();
    private static boolean actionBarEnabled;
    private static boolean classicMode = false;
    private static Theme currentTheme = Theme.PACKAGED;
    private static boolean didSetClassicModeFromYail = false;
    private static int primaryColor;
    private AppCompatDelegate appCompatDelegate;
    LinearLayout frameWithTitle;
    protected ThemeHelper themeHelper;
    TextView titleBar;

    public enum Theme {
        PACKAGED,
        CLASSIC,
        DEVICE_DEFAULT,
        BLACK_TITLE_TEXT,
        DARK
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void onCreate(Bundle icicle) {
        boolean z = classicMode || SdkLevel.getLevel() < 11;
        classicMode = z;
        if (classicMode) {
            this.themeHelper = new ClassicThemeHelper();
        } else if (SdkLevel.getLevel() < 14) {
            this.themeHelper = new HoneycombThemeHelper(this);
            this.themeHelper.requestActionBar();
            actionBarEnabled = true;
        } else {
            this.themeHelper = new IceCreamSandwichThemeHelper(this);
            if (currentTheme != Theme.PACKAGED) {
                applyTheme();
            }
            this.appCompatDelegate = AppCompatDelegate.create((Activity) this, (AppCompatCallback) this);
            this.appCompatDelegate.onCreate(icicle);
        }
        super.onCreate(icicle);
        this.frameWithTitle = new LinearLayout(this);
        this.frameWithTitle.setOrientation(1);
        setContentView(this.frameWithTitle);
        actionBarEnabled = this.themeHelper.hasActionBar();
        this.titleBar = (TextView) findViewById(16908310);
        if (shouldCreateTitleBar()) {
            this.titleBar = new TextView(this);
            this.titleBar.setBackgroundResource(17301653);
            this.titleBar.setTextAppearance(this, 16973907);
            this.titleBar.setGravity(16);
            this.titleBar.setSingleLine();
            this.titleBar.setShadowLayer(2.0f, 0.0f, 0.0f, -1157627904);
            if (!isClassicMode() || SdkLevel.getLevel() < 11) {
                this.frameWithTitle.addView(this.titleBar, new LayoutParams(-1, -2));
                return;
            }
            return;
        }
        Log.d(LOG_TAG, "Already have a title bar (classic mode): " + this.titleBar);
    }

    public final boolean isAppCompatMode() {
        return this.appCompatDelegate != null;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.onPostCreate(savedInstanceState);
        }
    }

    protected void onPostResume() {
        super.onPostResume();
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.onPostResume();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.onConfigurationChanged(newConfig);
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.onStop();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.onDestroy();
        }
    }

    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.setTitle(title);
        } else if (this.titleBar != null) {
            this.titleBar.setText(title);
        }
    }

    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    @Nullable
    public ActionMode onWindowStartingSupportActionMode(Callback callback) {
        return null;
    }

    public void setContentView(View view) {
        if (view != this.frameWithTitle) {
            this.frameWithTitle.addView(view, new FrameLayout.LayoutParams(-1, -1));
            view = this.frameWithTitle;
        }
        if (this.appCompatDelegate != null) {
            this.appCompatDelegate.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    public ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        Window.Callback classicCallback = getWindow().getCallback();
        try {
            if (this.appCompatDelegate != null) {
                actionBar = this.appCompatDelegate.getSupportActionBar();
            }
        } catch (IllegalStateException e) {
            this.appCompatDelegate = null;
            classicMode = true;
            getWindow().setCallback(classicCallback);
        }
        return actionBar;
    }

    public static boolean isEmulator() {
        return Build.PRODUCT.contains(CommonUtils.GOOGLE_SDK) || Build.PRODUCT.equals(CommonUtils.SDK) || Build.PRODUCT.contains("sdk_gphone");
    }

    protected static boolean isActionBarEnabled() {
        return actionBarEnabled;
    }

    protected void setActionBarEnabled(boolean enabled) {
        actionBarEnabled = enabled;
    }

    public static boolean isClassicMode() {
        return classicMode;
    }

    protected void setClassicMode(boolean classic) {
        if (isRepl() && SdkLevel.getLevel() >= 11) {
            classicMode = classic;
        }
    }

    protected static int getPrimaryColor() {
        return primaryColor;
    }

    protected void setPrimaryColor(int color) {
        int newColor;
        ActionBar actionBar = getSupportActionBar();
        if (color == 0) {
            newColor = DEFAULT_PRIMARY_COLOR;
        } else {
            newColor = color;
        }
        if (actionBar != null && newColor != primaryColor) {
            primaryColor = newColor;
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    protected boolean isRepl() {
        return false;
    }

    protected void hideTitleBar() {
        if (this.titleBar == null) {
            return;
        }
        if (this.titleBar.getParent() == this.frameWithTitle) {
            this.titleBar.setVisibility(8);
        } else if (this.titleBar.getParent() != null) {
            ((View) this.titleBar.getParent()).setVisibility(8);
        }
    }

    protected void maybeShowTitleBar() {
        if (this.titleBar != null) {
            this.titleBar.setVisibility(0);
            Log.d(LOG_TAG, "titleBar visible");
            if (this.titleBar.getParent() != null && this.titleBar.getParent() != this.frameWithTitle) {
                Log.d(LOG_TAG, "Setting parent visible");
                ((View) this.titleBar.getParent()).setVisibility(0);
            }
        }
    }

    protected void styleTitleBar() {
        ActionBar actionBar = getSupportActionBar();
        Log.d(LOG_TAG, "actionBarEnabled = " + actionBarEnabled);
        Log.d(LOG_TAG, "!classicMode = " + (!classicMode));
        Log.d(LOG_TAG, "actionBar = " + actionBar);
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getPrimaryColor()));
            if (actionBarEnabled) {
                actionBar.show();
                hideTitleBar();
                return;
            }
            actionBar.hide();
        }
        maybeShowTitleBar();
    }

    protected void setAppInventorTheme(Theme theme) {
        if (Form.getActiveForm().isRepl() && theme != currentTheme) {
            currentTheme = theme;
            applyTheme();
        }
    }

    private void applyTheme() {
        Log.d(LOG_TAG, "applyTheme " + currentTheme);
        setClassicMode(false);
        switch (currentTheme) {
            case CLASSIC:
                setClassicMode(true);
                setTheme(16973829);
                return;
            case DEVICE_DEFAULT:
            case BLACK_TITLE_TEXT:
                setTheme(16974124);
                return;
            case DARK:
                setTheme(16974121);
                return;
            default:
                return;
        }
    }

    private boolean shouldCreateTitleBar() {
        if (isAppCompatMode() && (!this.themeHelper.hasActionBar() || !isActionBarEnabled())) {
            return true;
        }
        if (this.titleBar == null && (isRepl() || classicMode)) {
            return true;
        }
        return false;
    }

    public static void setClassicModeFromYail(boolean newClassicMode) {
        if (!didSetClassicModeFromYail) {
            Log.d(LOG_TAG, "Setting classic mode from YAIL: " + newClassicMode);
            classicMode = newClassicMode;
            didSetClassicModeFromYail = true;
        }
    }
}
