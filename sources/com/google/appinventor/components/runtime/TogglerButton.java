package com.google.appinventor.components.runtime;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "Toggle Button with the ability to detect clicks.  Many aspects of its appearance can be changed, as well as whether it is clickable (<code>Enabled</code>), can be changed in the Designer or in the Blocks Editor.", docUri = "user-interface/togglerbutton", version = 6)
public final class TogglerButton extends ButtonBaseNoText implements OnCheckedChangeListener {
    protected static final String LOG_TAG = "TogglerButton";

    public TogglerButton(ComponentContainer container) {
        super(container, LOG_TAG);
        this.view.setOnClickListener(null);
        ((ToggleButton) this.view).setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Click();
    }

    public void click() {
        Click();
    }

    @SimpleEvent(description = "User tapped and released the button.")
    public void Click() {
        EventDispatcher.dispatchEvent(this, "Click", new Object[0]);
    }

    public boolean longClick() {
        return LongClick();
    }

    @SimpleEvent(description = "User held the button down.")
    public boolean LongClick() {
        return EventDispatcher.dispatchEvent(this, "LongClick", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Text to display on Toggle Button when turned on.")
    public String TextOn() {
        return this.view.getTextOn().toString();
    }

    @DesignerProperty(defaultValue = "On", editorType = "string")
    @SimpleProperty
    public void TextOn(String text) {
        ToggleButton view2 = this.view;
        view2.setTextOn(text);
        view2.requestLayout();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Text to display on Toggle Button when turned off.")
    public String TextOff() {
        return this.view.getTextOff().toString();
    }

    @DesignerProperty(defaultValue = "Off", editorType = "string")
    @SimpleProperty
    public void TextOff(String text) {
        ToggleButton view2 = this.view;
        view2.setTextOff(text);
        view2.setText(text);
        view2.requestLayout();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "If turned on, users can press the Toggle Button.")
    public boolean TurnedOn() {
        return this.view.isChecked();
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void TurnedOn(boolean bool) {
        this.view.setChecked(bool);
    }
}
