package com.google.appinventor.components.runtime;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.HtmlEntities;
import com.google.appinventor.components.runtime.util.AnimationUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "A floating action button (FAB) represents the primary action in an application. Only one FAB is recommended per screen to represent the most common action. It is used for a promoted action.", docUri = "user-interface/fab-floating-action-button", iconName = "images/floatingActionButton.png", nonVisible = true, version = 1)
public class ThunkableFloatingActionButton extends AndroidNonvisibleComponent implements Component, OnOrientationChangeListener, OnClearListener {
    private static final int matchParentLength = -1;
    private Activity activity;
    private int animationDuration = 300;
    private int backgroundColor = Component.COLOR_BLUE;
    private LinearLayout buttonLayout;
    private int buttonSize;
    private ComponentContainer container;
    private Context context;
    private boolean created = false;
    private GradientDrawable drawable;
    private float elevation;
    private Typeface font;
    private int iconColor = -1;
    private String iconName = Component.ICON_NAME_PLUS;
    private int iconSize = 24;
    private TextView iconTextView;
    private LayoutParams layoutParam;
    private int paddingBottom;
    private int paddingRight;
    private RelativeLayout relative;
    private LinearLayout shadowLayout;
    private boolean visible = true;

    /* renamed from: com.google.appinventor.components.runtime.ThunkableFloatingActionButton$1 */
    class C04041 implements OnClickListener {
        C04041() {
        }

        public void onClick(View view) {
            ThunkableFloatingActionButton.this.Click();
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.ThunkableFloatingActionButton$3 */
    class C04073 extends AnimatorListenerAdapter {
        C04073() {
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            ThunkableFloatingActionButton.this.buttonLayout.setVisibility(8);
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.ThunkableFloatingActionButton$4 */
    class C04084 implements AnimatorListener {
        C04084() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    public ThunkableFloatingActionButton(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        this.context = container.$context();
        this.activity = container.$context();
        this.paddingBottom = convertDpToPixel(16);
        this.paddingRight = convertDpToPixel(16);
        this.buttonSize = convertDpToPixel(56);
        this.elevation = (float) convertDpToPixel(6);
        container.$form().addOrientationChangeListener(this);
        this.form.registerForOnClear(this);
        this.font = Typeface.createFromAsset(this.context.getAssets(), "MaterialIcons-Regular.ttf");
        create();
    }

    public void onOrientationChange() {
        if (this.visible) {
            onClear();
            create();
        }
    }

    public void onClear() {
        if (this.relative != null) {
            ((ViewGroup) this.relative.getParent()).removeView(this.relative);
        }
    }

    private void create() {
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(this.buttonSize, this.buttonSize);
        LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams(this.buttonSize, this.buttonSize);
        iconLayoutParams.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams shadowLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        this.shadowLayout = new LinearLayout(this.context);
        this.shadowLayout.setClipToPadding(false);
        this.shadowLayout.setOrientation(0);
        this.shadowLayout.setLayoutParams(shadowLayoutParams);
        this.shadowLayout.setPadding(0, 0, this.paddingRight, this.paddingBottom);
        this.shadowLayout.setGravity(85);
        this.buttonLayout = new LinearLayout(this.context);
        this.buttonLayout.setClipToPadding(false);
        this.buttonLayout.setOrientation(0);
        this.buttonLayout.setLayoutParams(buttonLayoutParams);
        this.buttonLayout.setGravity(85);
        if (SdkLevel.getLevel() >= 21) {
            this.buttonLayout.setElevation(this.elevation);
        }
        this.buttonLayout.setOnClickListener(new C04041());
        this.drawable = new GradientDrawable();
        this.drawable.setShape(1);
        this.drawable.setCornerRadius(0.0f);
        this.drawable.setColor(this.backgroundColor);
        this.buttonLayout.setBackgroundDrawable(this.drawable);
        this.iconTextView = new TextView(this.context);
        this.iconTextView.setTypeface(this.font);
        this.iconTextView.setTextSize(1, (float) this.iconSize);
        this.iconTextView.setText(HtmlEntities.decodeHtmlText(this.iconName));
        this.iconTextView.setTextColor(this.iconColor);
        this.iconTextView.setLayoutParams(iconLayoutParams);
        this.iconTextView.setGravity(17);
        this.buttonLayout.addView(this.iconTextView);
        this.shadowLayout.addView(this.buttonLayout);
        this.relative = new RelativeLayout(this.activity);
        this.relative.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.relative.setGravity(85);
        this.relative.setBackgroundColor(0);
        this.relative.addView(this.shadowLayout, shadowLayoutParams);
        this.layoutParam = new LayoutParams(-1, -1);
        this.layoutParam.addRule(12);
        this.activity.addContentView(this.relative, this.layoutParam);
        this.created = true;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Background color for the FAB.")
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "-14575885", editorType = "color")
    @SimpleProperty(description = "Specifies background color for the FAB.")
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        this.drawable.setColor(this.backgroundColor);
        this.buttonLayout.setBackgroundDrawable(this.drawable);
        this.buttonLayout.invalidate();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Color for the icon of the FAB.")
    public int IconColor() {
        return this.iconColor;
    }

    @DesignerProperty(defaultValue = "-1", editorType = "color")
    @SimpleProperty(description = "Specifies color for the icon of the FAB.")
    public void IconColor(int argb) {
        this.iconColor = argb;
        this.iconTextView.setTextColor(this.iconColor);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Visibility of the FAB.")
    public boolean Visible() {
        return this.visible;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Specifies whether the FAB should be visible on the screen. Value is true if the FAB is showing and false if hidden.")
    public void Visible(boolean visible) {
        this.visible = visible;
        if (this.visible) {
            showFAB();
        } else {
            hideFAB();
        }
    }

    @DesignerProperty(defaultValue = "300", editorType = "non_negative_integer")
    @SimpleProperty(description = "Specifies the duration of the transition animation of the FAB in milliseconds. The shorter the duration is, the faster the animation would be.")
    public void AnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    @DesignerProperty(defaultValue = "56", editorType = "fab_button_size")
    @SimpleProperty(userVisible = false)
    public void ButtonSize(int buttonSize) {
        this.buttonSize = convertDpToPixel(buttonSize);
        this.buttonLayout.getLayoutParams().width = this.buttonSize;
        this.buttonLayout.getLayoutParams().height = this.buttonSize;
        this.iconTextView.getLayoutParams().width = this.buttonSize;
        this.iconTextView.getLayoutParams().height = this.buttonSize;
    }

    @DesignerProperty(defaultValue = "16", editorType = "non_negative_integer")
    @SimpleProperty(description = "Specifies the distance between the right edge of the FAB and the right edge of the screen. The default value is 16 dp.")
    public void PaddingRight(int paddingRight) {
        this.paddingRight = convertDpToPixel(paddingRight);
        this.shadowLayout.setPadding(0, 0, this.paddingRight, this.paddingBottom);
        this.shadowLayout.requestLayout();
    }

    @DesignerProperty(defaultValue = "16", editorType = "non_negative_integer")
    @SimpleProperty(description = "Specifies the distance between the bottom edge of the FAB and the bottom edge of the screen. The default value is 16 dp.")
    public void PaddingBottom(int paddingBottom) {
        this.paddingBottom = convertDpToPixel(paddingBottom);
        this.shadowLayout.setPadding(0, 0, this.paddingRight, this.paddingBottom);
        this.shadowLayout.requestLayout();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Name (or code) for the FAB icon. You can find the icon name (or code) here https://material.io/icons/")
    public String IconName() {
        return this.iconName;
    }

    @DesignerProperty(defaultValue = "add", editorType = "string")
    @SimpleProperty(description = "Specifies the name (or code) for the FAB icon. You can find the icon name (or code) here https://material.io/icons/")
    public void IconName(String iconName) {
        if (this.animationDuration > 0) {
            changeIconWithFade(iconName);
            return;
        }
        this.iconName = iconName;
        this.iconTextView.setText(HtmlEntities.decodeHtmlText(this.iconName));
    }

    @SimpleEvent(description = "When the FAB is Clicked")
    public void Click() {
        EventDispatcher.dispatchEvent(this, "Click", new Object[0]);
    }

    private void changeIconWithFade(final String newIconName) {
        final int fadeDuration = this.animationDuration;
        this.iconTextView.animate().alpha(0.0f).setDuration((long) fadeDuration).setListener(new AnimatorListenerAdapter() {

            /* renamed from: com.google.appinventor.components.runtime.ThunkableFloatingActionButton$2$1 */
            class C04051 extends AnimatorListenerAdapter {
                C04051() {
                }

                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ThunkableFloatingActionButton.this.iconName = newIconName;
                ThunkableFloatingActionButton.this.iconTextView.setText(HtmlEntities.decodeHtmlText(newIconName));
                ThunkableFloatingActionButton.this.iconTextView.animate().alpha(1.0f).setDuration((long) fadeDuration).setListener(new C04051());
            }
        }).start();
    }

    private void hideFAB() {
        this.buttonLayout.animate().scaleY(0.0f).scaleX(0.0f).setInterpolator(AnimationUtil.getFastOutSlowInInterpolator(this.context)).setDuration((long) this.animationDuration).setListener(new C04073()).start();
    }

    private void showFAB() {
        this.buttonLayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(AnimationUtil.getFastOutSlowInInterpolator(this.context)).setDuration((long) this.animationDuration).setListener(new C04084()).start();
        this.buttonLayout.setVisibility(0);
    }

    private int convertDpToPixel(int dp) {
        return (int) ((((float) dp) * this.context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
