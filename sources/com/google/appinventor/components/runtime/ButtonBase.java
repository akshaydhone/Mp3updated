package com.google.appinventor.components.runtime;

import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.runtime.util.TextViewUtil;

@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public abstract class ButtonBase extends ButtonBaseNoText implements OnClickListener, OnFocusChangeListener, OnLongClickListener, OnTouchListener {
    protected static final String LOG_TAG = "ButtonBase";

    public ButtonBase(ComponentContainer container) {
        super(container);
        Text("");
    }

    public ButtonBase(ComponentContainer container, String togglerButtonOrSwitch) {
        super(container, togglerButtonOrSwitch);
        Text("");
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Text to display on button.")
    public String Text() {
        return TextViewUtil.getText(this.view);
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void Text(String text) {
        TextViewUtil.setText(this.view, text);
    }
}
