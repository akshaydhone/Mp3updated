package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
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
import com.google.appinventor.components.runtime.util.AdmobUtil;
import com.google.appinventor.components.runtime.util.AdmobUtil.AdmobLoader;
import com.google.appinventor.components.runtime.util.MobileAnalytics;

@DesignerComponent(category = ComponentCategory.EXPERIMENTAL, description = "AdMob component allows you to displayAd. You need have a valid AdMob account and an AdUnitId. You should in thetestmode when you are still developing the app.", docUri = "monetisation/admob", iconName = "images/adMob2.png", nonVisible = true, version = 3)
@UsesLibraries(libraries = "google-play-services.jar,firebase.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET,android.permission.ACCESS_NETWORK_STATE")
public final class ThunkableAdMobInterstitial extends AndroidNonvisibleComponent implements Component, AdmobLoader {
    private static final String LOG_TAG = "ThunkableAdMobInterstitial";
    private String ThunkableAdUnitID = "";
    private boolean adEnabled = true;
    private String adUnitID = "";
    protected final ComponentContainer container;
    private String currentInUseAdUnitID = "";
    private boolean hasLoaded = false;
    private boolean isTestMode = true;
    private InterstitialAd mInterstitialAd;
    private boolean personalized;
    private Boolean setAdUnitID = Boolean.valueOf(false);
    private boolean takeChance = false;

    /* renamed from: com.google.appinventor.components.runtime.ThunkableAdMobInterstitial$1 */
    class C03951 extends AdListener {
        C03951() {
        }

        public void onAdLoaded() {
            ThunkableAdMobInterstitial.this.AdLoaded();
            ThunkableAdMobInterstitial.this.tracking("ThunkableAdMobInterstitial onAdLoaded");
        }

        public void onAdFailedToLoad(int errorCode) {
            Log.d(ThunkableAdMobInterstitial.LOG_TAG, "onAdFailedToLoad and errorCode is " + errorCode);
            ThunkableAdMobInterstitial.this.AdFailedToLoad(errorCode);
        }
    }

    public ThunkableAdMobInterstitial(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.container = componentContainer;
        this.mInterstitialAd = new InterstitialAd(this.container.$context());
        this.mInterstitialAd.setAdListener(new C03951());
        this.adEnabled = true;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    public void AdUnitID(String str) {
        this.adUnitID = str;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String AdUnitID() {
        return this.adUnitID;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    public void TestMode(boolean z) {
        this.isTestMode = z;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean TestMode() {
        return this.isTestMode;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public void Personalized(boolean personalized) {
        if (this.personalized != personalized) {
            this.personalized = personalized;
            if (this.hasLoaded) {
                AdmobUtil.loadWhenReady(this);
            }
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean Personalized() {
        return this.personalized;
    }

    @SimpleFunction(description = "Starts loading a new ad.")
    public void LoadAd() {
        AdmobUtil.loadWhenReady(this);
    }

    public void load() {
        if (AdmobUtil.shouldShowAd(this.container.$context())) {
            if (!this.setAdUnitID.booleanValue()) {
                setUnitId();
                this.setAdUnitID = Boolean.valueOf(true);
            }
            if (this.adEnabled && !this.mInterstitialAd.isLoading()) {
                Builder builder = new Builder();
                if (!this.personalized) {
                    builder = AdmobUtil.addNPA(builder);
                }
                if (this.isTestMode) {
                    builder = builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
                }
                this.mInterstitialAd.loadAd(builder.build());
                this.hasLoaded = true;
                tracking("ThunkableAdMobInterstitial LoadAd");
                return;
            }
            return;
        }
        this.adEnabled = false;
    }

    @SimpleEvent(description = "Called when an ad is received.")
    public void AdLoaded() {
        ShowAd();
        EventDispatcher.dispatchEvent(this, "AdLoaded", new Object[0]);
    }

    @SimpleEvent(description = "Called when an ad is failed to load.")
    public void AdFailedToLoad(int errorCode) {
        EventDispatcher.dispatchEvent(this, "AdFailedToLoad", Integer.valueOf(errorCode));
    }

    public void ShowAd() {
        if (this.adEnabled && this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
        }
    }

    public void setUnitId() {
        this.ThunkableAdUnitID = AdmobUtil.getInterstitialKey(this.container.$context());
        if (!this.adUnitID.equals("")) {
            this.takeChance = AdmobUtil.takeChance(this.container.$context(), this.ThunkableAdUnitID);
            if (this.takeChance) {
                this.currentInUseAdUnitID = this.ThunkableAdUnitID;
            } else {
                this.currentInUseAdUnitID = this.adUnitID;
            }
            this.mInterstitialAd.setAdUnitId(this.currentInUseAdUnitID);
        }
    }

    private void tracking(String eventName) {
        if (!this.isTestMode) {
            String packageName = this.container.$context().getPackageName();
            MobileAnalytics.fabricTracking(packageName, eventName);
            MobileAnalytics.amplitudeTracking(packageName, eventName, null, this.takeChance);
        }
    }
}
