package com.google.appinventor.components.runtime.util;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.http.SslError;
import android.view.Display;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.Player;
import com.google.appinventor.components.runtime.Player.State;
import java.util.Arrays;

public class FroyoUtil {
    private FroyoUtil() {
    }

    public static int getRotation(Display display) {
        return display.getRotation();
    }

    public static AudioManager setAudioManager(Activity activity) {
        return (AudioManager) activity.getSystemService("audio");
    }

    public static Object setAudioFocusChangeListener(final Player player) {
        return new OnAudioFocusChangeListener() {
            private boolean playbackFlag = false;

            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case -3:
                    case -2:
                        if (player != null && player.playerState == State.PLAYING) {
                            player.pause();
                            this.playbackFlag = true;
                            return;
                        }
                        return;
                    case -1:
                        this.playbackFlag = false;
                        player.OtherPlayerStarted();
                        return;
                    case 1:
                        if (player != null && this.playbackFlag && player.playerState == State.PAUSED_BY_EVENT) {
                            player.Start();
                            this.playbackFlag = false;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public static boolean focusRequestGranted(AudioManager am, Object afChangeListener) {
        if (am.requestAudioFocus((OnAudioFocusChangeListener) afChangeListener, 3, 1) == 1) {
            return true;
        }
        return false;
    }

    public static void abandonFocus(AudioManager am, Object afChangeListener) {
        am.abandonAudioFocus((OnAudioFocusChangeListener) afChangeListener);
    }

    private static boolean isFileProbablyWebPage(String filePath) {
        for (String extension : Arrays.asList(new String[]{".html", ".html5", ".htm", ".php", ".jsp", ".asp", ".aspx"})) {
            int lastOccurance = filePath.lastIndexOf(extension);
            if (lastOccurance != -1) {
                String stringAfterLastWebFileExtension = filePath.substring(extension.length() + lastOccurance);
                if (stringAfterLastWebFileExtension.startsWith("#") || stringAfterLastWebFileExtension.startsWith("?") || stringAfterLastWebFileExtension.length() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static WebViewClient getWebViewClient(boolean ignoreErrors, boolean followLinks, boolean useExternalBrowser, ComponentContainer container, Component component) {
        final Form form = container.$form();
        final Activity activity = container.$context();
        final boolean z = useExternalBrowser;
        final boolean z2 = followLinks;
        final boolean z3 = ignoreErrors;
        final Component component2 = component;
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    if (z) {
                        activity.startActivity(new Intent("android.intent.action.VIEW", NougatUtil.parseUri(form, url)));
                        return true;
                    }
                } else if (url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("file:")) {
                    if (!FroyoUtil.isFileProbablyWebPage(url)) {
                        activity.startActivity(new Intent("android.intent.action.VIEW", NougatUtil.parseUri(form, url)));
                        return true;
                    } else if (z2) {
                        return false;
                    } else {
                        return true;
                    }
                }
                if (z2) {
                    return false;
                }
                return true;
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (z3) {
                    handler.proceed();
                    return;
                }
                handler.cancel();
                form.dispatchErrorOccurredEvent(component2, "WebView", ErrorMessages.ERROR_WEBVIEW_SSL_ERROR, new Object[0]);
            }
        };
    }
}
