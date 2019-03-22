package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

@DesignerComponent(category = ComponentCategory.EXPERIMENTAL, description = "Non-visible component that provides push notification using the OneSignal service. Please refer to the <a href=\"http://onesignal.com/\">OneSignal</a> for more information.", docUri = "push-notifications", iconName = "images/onesignal.png", nonVisible = true, version = 2)
@UsesLibraries(libraries = "google-play-services.jar,oneSignalSDK.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET, com.google.android.c2dm.permission.RECEIVE, android.permission.WAKE_LOCK, android.permission.VIBRATE, android.permission.ACCESS_NETWORK_STATE, android.permission.RECEIVE_BOOT_COMPLETED ,com.sec.android.provider.badge.permission.READ ,com.sec.android.provider.badge.permission.WRITE ,com.htc.launcher.permission.READ_SETTINGS ,com.htc.launcher.permission.UPDATE_SHORTCUT ,com.sonyericsson.home.permission.BROADCAST_BADGE ,com.sonymobile.home.permission.PROVIDER_INSERT_BADGE ,com.anddoes.launcher.permission.UPDATE_COUNT ,com.majeur.launcher.permission.UPDATE_BADGE ,com.huawei.android.launcher.permission.CHANGE_BADGE ,com.huawei.android.launcher.permission.READ_SETTINGS ,com.huawei.android.launcher.permission.WRITE_SETTINGS")
public class ThunkablePushNotification extends AndroidNonvisibleComponent implements OSSubscriptionObserver {
    private static final String LOG_TAG = "ThunkablePushNotification";
    private final ComponentContainer container;

    public ThunkablePushNotification(ComponentContainer container) {
        super(container.$form());
        OneSignal.startInit(container.$context()).init();
        OneSignal.addSubscriptionObserver(this);
        this.container = container;
        String packageName = container.$context().getPackageName();
        MobileAnalytics.fabricTracking(packageName, LOG_TAG);
        MobileAnalytics.amplitudeTracking(packageName, LOG_TAG, null, false);
        if (!GetUserId().equals("")) {
            UserIdReady(GetUserId());
        }
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(userVisible = false)
    public void OneSignalAppId(String key) {
    }

    @SimpleFunction(description = "Return current user id or empty string when the user id is not yet ready.")
    public String GetUserId() {
        String userId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        return userId == null ? "" : userId;
    }

    public void onOSSubscriptionChanged(final OSSubscriptionStateChanges changes) {
        if (changes.getTo().getUserId() != null) {
            this.container.$form().runOnUiThread(new Runnable() {
                public void run() {
                    ThunkablePushNotification.this.UserIdReady(changes.getTo().getUserId());
                }
            });
        }
    }

    @SimpleEvent
    public void UserIdReady(String userId) {
        EventDispatcher.dispatchEvent(this, "UserIdReady", userId);
    }
}
