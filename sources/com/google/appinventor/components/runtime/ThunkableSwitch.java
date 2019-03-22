package com.google.appinventor.components.runtime;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "Switch with the ability to detect clicks.  Many aspects of its appearance can be changed, as well as whether it is clickable (<code>Enabled</code>), can be changed in the Designer or in the Blocks Editor.", docUri = "user-interface/switch", version = 6)
public final class ThunkableSwitch extends ButtonBase implements OnCheckedChangeListener {
    protected static final String LOG_TAG = "Switch";
    private final Drawable defaultThumbDrawable;
    private final Drawable defaultTrackDrawable;
    private int thumbColor;
    private int trackColor;

    public ThunkableSwitch(ComponentContainer container) {
        super(container, "ThunkableSwitch");
        Switch view2 = this.view;
        this.defaultThumbDrawable = view2.getThumbDrawable();
        this.defaultTrackDrawable = view2.getTrackDrawable();
        TextColor(-16777216);
        TrackColor(-8355712);
        view2.setOnCheckedChangeListener(this);
    }

    public void click() {
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
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

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The color of the Thumb, the moving piece that slides.")
    public int ThumbColor() {
        return this.thumbColor;
    }

    @DesignerProperty(defaultValue = "&H00000000", editorType = "color")
    @SimpleProperty(description = "Color of the Thumb, the moving piece that slides.")
    public void ThumbColor(int argb) {
        this.thumbColor = argb;
        Switch switchView = this.view;
        if (this.thumbColor == 0) {
            switchView.setThumbDrawable(this.defaultThumbDrawable);
        } else {
            switchView.getThumbDrawable().setColorFilter(argb, Mode.MULTIPLY);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The color of the Track that the thumb slides on")
    public int TrackColor() {
        return this.trackColor;
    }

    @DesignerProperty(defaultValue = "&H00000000", editorType = "color")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Color of the Track that the Thumb slides on.")
    public void TrackColor(int argb) {
        this.trackColor = argb;
        Switch switchView = this.view;
        if (this.trackColor == 0) {
            switchView.setTrackDrawable(this.defaultTrackDrawable);
        } else {
            switchView.getTrackDrawable().setColorFilter(argb, Mode.LIGHTEN);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Indicates if the Toggle Button is actively turned on.")
    public boolean TurnedOn() {
        return this.view.isChecked();
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void TurnedOn(boolean bool) {
        this.view.setChecked(bool);
    }
}
