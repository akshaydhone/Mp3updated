package com.google.appinventor.components.runtime;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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

@DesignerComponent(category = ComponentCategory.EXPERIMENTAL, description = "AdMob component allows you to display Ad. You need have a valid AdMob account and an AdUnitId. You should in thetestmode when you are still developing the app.", docUri = "monetisation/admob", version = 3)
@UsesLibraries(libraries = "google-play-services.jar,firebase.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET,android.permission.ACCESS_NETWORK_STATE")
public final class ThunkableAdMobBanner extends AndroidViewComponent implements OnDestroyListener, OnResumeListener, OnPauseListener, AdmobLoader {
    private static final String LOG_TAG = "ThunkableAdMobBanner";
    private String ThunkableAdUnitID = "";
    private boolean adEnabled = true;
    private String adUnitID = "";
    private AdView adView;
    protected final ComponentContainer container;
    private String currentInUseAdUnitID = "";
    private boolean hasLoaded = false;
    private boolean isTestMode = true;
    private boolean personalized = true;
    private boolean setupAdUnitId = false;
    private boolean takeChance = false;

    /* renamed from: com.google.appinventor.components.runtime.ThunkableAdMobBanner$1 */
    class C03941 extends AdListener {
        C03941() {
        }

        public void onAdClosed() {
            ThunkableAdMobBanner.this.tracking("ThunkableAdMobBanner onAdClosed");
        }

        public void onAdLoaded() {
            ThunkableAdMobBanner.this.AdLoaded();
            ThunkableAdMobBanner.this.tracking("ThunkableAdMobBanner onAdLoaded");
        }

        public void onAdFailedToLoad(int errorCode) {
            ThunkableAdMobBanner.this.AdFailedToLoad(errorCode);
            Log.d(ThunkableAdMobBanner.LOG_TAG, "onAdFailedToLoad and errorCode is " + errorCode);
        }
    }

    public ThunkableAdMobBanner(ComponentContainer componentContainer) {
        super(componentContainer);
        this.container = componentContainer;
        this.container.$form().registerForOnDestroy(this);
        this.container.$form().registerForOnResume(this);
        this.container.$form().registerForOnPause(this);
        generateNewAdView();
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

    @SimpleFunction(description = "Loads a new ad.")
    public void LoadAd() {
        AdmobUtil.loadWhenReady(this);
    }

    public void load() {
        if (AdmobUtil.shouldShowAd(this.container.$context())) {
            if (!this.setupAdUnitId) {
                setUnitId();
            }
            if (this.adEnabled && !this.adView.isLoading()) {
                Builder builder = new Builder();
                if (!this.personalized) {
                    builder = AdmobUtil.addNPA(builder);
                }
                if (this.isTestMode) {
                    builder = builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
                }
                this.adView.loadAd(builder.build());
                this.hasLoaded = true;
                tracking("ThunkableAdMobBanner LoadAd");
                return;
            }
            return;
        }
        this.adEnabled = false;
    }

    public void onDestroy() {
        if (this.adView != null) {
            this.adView.destroyDrawingCache();
            this.adView.destroy();
            this.adEnabled = false;
        }
    }

    @SimpleEvent(description = "Called when an ad is received.")
    public void AdLoaded() {
        EventDispatcher.dispatchEvent(this, "AdLoaded", new Object[0]);
    }

    @SimpleEvent(description = "Called when an ad is failed to load.")
    public void AdFailedToLoad(int errorCode) {
        EventDispatcher.dispatchEvent(this, "AdFailedToLoad", Integer.valueOf(errorCode));
    }

    public void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }
    }

    public void onResume() {
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    public View getView() {
        return this.adView;
    }

    private void setUnitId() {
        this.ThunkableAdUnitID = AdmobUtil.getBannerKey(this.container.$context());
        if (!this.adUnitID.equals("")) {
            this.takeChance = AdmobUtil.takeChance(this.container.$context(), this.ThunkableAdUnitID);
            if (this.takeChance) {
                this.currentInUseAdUnitID = this.ThunkableAdUnitID;
            } else {
                this.currentInUseAdUnitID = this.adUnitID;
            }
            this.adView.setAdUnitId(this.currentInUseAdUnitID);
            this.setupAdUnitId = true;
        }
    }

    private void generateNewAdView() {
        this.adView = new AdView(this.container.$context());
        this.adView.setAdSize(AdSize.SMART_BANNER);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(12);
        this.adView.setLayoutParams(layoutParams);
        this.adView.setAdListener(new C03941());
        this.container.$add(this);
    }

    public int Width() {
        return super.Width();
    }

    public void Width(int width) {
        super.Width(width);
    }

    public void WidthPercent(int pCent) {
        super.Width(pCent);
    }

    public int Height() {
        return super.Height();
    }

    public void Height(int height) {
        super.Height(height);
    }

    public void HeightPercent(int pCent) {
        super.HeightPercent(pCent);
    }

    private void tracking(String eventName) {
        if (!this.isTestMode) {
            String packageName = this.container.$context().getPackageName();
            MobileAnalytics.fabricTracking(packageName, eventName);
            MobileAnalytics.amplitudeTracking(packageName, eventName, null, this.takeChance);
        }
    }
}
