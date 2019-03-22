package com.google.appinventor.components.runtime;

import android.support.v4.internal.view.SupportMenu;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.util.MapFactory.HasFill;
import com.google.appinventor.components.runtime.util.MapFactory.MapFeatureContainer;
import com.google.appinventor.components.runtime.util.MapFactory.MapFeatureVisitor;

@SimpleObject
public abstract class MapFeatureBaseWithFill extends MapFeatureBase implements HasFill {
    private int fillColor = Component.COLOR_RED;

    public MapFeatureBaseWithFill(MapFeatureContainer container, MapFeatureVisitor<Double> distanceComputation) {
        super(container, distanceComputation);
        FillColor(SupportMenu.CATEGORY_MASK);
    }

    @DesignerProperty(defaultValue = "&HFFF44336", editorType = "color")
    @SimpleProperty
    public void FillColor(int argb) {
        this.fillColor = argb;
        this.map.getController().updateFeatureFill(this);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The paint color used to fill in the map feature.")
    public int FillColor() {
        return this.fillColor;
    }
}
