package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.MobileAnalytics;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import gnu.math.DFloNum;
import gnu.math.IntNum;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

@DesignerComponent(category = ComponentCategory.MAPVIZ, docUri = "location/google-maps", version = 3)
@UsesLibraries(libraries = "google-play-services.jar, gson-2.1.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE, android.permission.ACCESS_NETWORK_STATE, android.permission.INTERNET, android.permission.ACCESS_COARSE_LOCATION, android.permission.ACCESS_FINE_LOCATION, com.google.android.providers.gsf.permission.READ_GSERVICES, android.permission.WRITE_EXTERNAL_STORAGE")
public class GoogleMap extends AndroidViewComponent implements OnResumeListener, OnInitializeListener, OnPauseListener, OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    public static final double RADIUS_OF_EARTH_METERS = 6371009.0d;
    private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000).setFastestInterval(16).setPriority(100);
    private static final String TAG = "GoogleMap";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final AtomicInteger snextCircleId = new AtomicInteger(1);
    private static final AtomicInteger snextMarkerId = new AtomicInteger(1);
    private static final AtomicInteger snextPolylineId = new AtomicInteger(1);
    private final String MAP_FRAGMENT_TAG;
    private final Handler androidUIHandler = new Handler();
    private HashMap<Object, Integer> circles = new HashMap();
    private boolean compassEnabled = false;
    private final Activity context;
    private boolean enableCameraChangeListener = false;
    private boolean enableMapClickListener = false;
    private boolean enableMapLongClickListener = false;
    private final Form form;
    private Float initialCameraBearingDegrees;
    private Float initialCameraTiltDegrees;
    private Float initialCameraZoomLevel;
    private LatLng initialLocation;
    private String initialStyleJson;
    private String initialTheme = this.theme;
    private boolean isMapIsReadyDispatched = false;
    private boolean isMapReady = false;
    private boolean isScreenInitialized = false;
    private List<DraggableCircle> mCircles = new ArrayList(1);
    private GoogleApiClient mGoogleApiClient;
    private com.google.android.gms.maps.GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private int mMarkerColor = Component.COLOR_BLUE;
    private boolean mMarkerDraggable = false;
    private UiSettings mUiSettings;
    private int mapType = 1;
    private HashMap<Marker, Integer> markers = new HashMap();
    private YailList markersList;
    private boolean myLocationEnabled = false;
    private final HashMap<Integer, Polyline> polylinesByIds = new HashMap();
    private boolean rotateEnabled = true;
    private Bundle savedInstanceState;
    private boolean scrollEnabled = true;
    private String theme = GoogleMapStyleOptions.THEME_STANDARD;
    private LinearLayout viewLayout;
    private boolean zoomControlEnabled = false;
    private boolean zoomGesturesEnabled = true;

    /* renamed from: com.google.appinventor.components.runtime.GoogleMap$1 */
    class C03271 implements OnMapReadyCallback {
        C03271() {
        }

        public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
            GoogleMap.this.mMap = googleMap;
            if (GoogleMap.this.mMap != null) {
                GoogleMap.this.setUpMap();
            } else {
                GoogleMap.this.form.dispatchErrorOccurredEvent(GoogleMap.this, "setUpMapIfNeeded", ErrorMessages.ERROR_GOOGLE_PLAY_NOT_INSTALLED, new Object[0]);
            }
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.GoogleMap$3 */
    class C03293 implements Runnable {
        C03293() {
        }

        public void run() {
            boolean dispatchEventNow = false;
            if (GoogleMap.this.mMapFragment != null) {
                dispatchEventNow = true;
            }
            if (dispatchEventNow) {
                FragmentTransaction fragmentTransaction = GoogleMap.this.form.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(GoogleMap.this.viewLayout.getId(), GoogleMap.this.mMapFragment, GoogleMap.this.MAP_FRAGMENT_TAG);
                fragmentTransaction.commit();
                GoogleMap.this.setUpMapIfNeeded();
                return;
            }
            GoogleMap.this.androidUIHandler.post(this);
        }
    }

    private class DraggableCircle {
        private final Marker centerMarker;
        private final Circle circle;
        private double radius;
        private Marker radiusMarker;

        public DraggableCircle(LatLng center, double radius, float strokeWidth, int strokeColor, int fillColor) {
            this.radius = radius;
            this.centerMarker = GoogleMap.this.mMap.addMarker(new MarkerOptions().position(center).draggable(true));
            this.radiusMarker = GoogleMap.this.mMap.addMarker(new MarkerOptions().position(GoogleMap.toRadiusLatLng(center, radius)).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(210.0f)));
            this.circle = GoogleMap.this.mMap.addCircle(new CircleOptions().center(center).radius(radius).strokeWidth(strokeWidth).strokeColor(strokeColor).fillColor(fillColor));
        }

        public DraggableCircle(LatLng center, LatLng radiusLatLng, float strokeWidth, int strokeColor, int fillColor) {
            this.radius = GoogleMap.toRadiusMeters(center, radiusLatLng);
            this.centerMarker = GoogleMap.this.mMap.addMarker(new MarkerOptions().position(center).draggable(true));
            this.radiusMarker = GoogleMap.this.mMap.addMarker(new MarkerOptions().position(radiusLatLng).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(210.0f)));
            this.circle = GoogleMap.this.mMap.addCircle(new CircleOptions().center(center).radius(this.radius).strokeWidth(strokeWidth).strokeColor(strokeColor).fillColor(fillColor));
        }

        public DraggableCircle(Marker center, Marker radius, float strokeWidth, int strokeColor, int fillColor) {
            this.radius = GoogleMap.toRadiusMeters(center.getPosition(), radius.getPosition());
            this.centerMarker = center;
            this.radiusMarker = radius;
            this.circle = GoogleMap.this.mMap.addCircle(new CircleOptions().center(center.getPosition()).radius(this.radius).strokeWidth(strokeWidth).strokeColor(strokeColor).fillColor(fillColor));
        }

        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(this.centerMarker)) {
                this.circle.setCenter(marker.getPosition());
                this.radiusMarker.setPosition(GoogleMap.toRadiusLatLng(marker.getPosition(), this.radius));
                return true;
            } else if (!marker.equals(this.radiusMarker)) {
                return false;
            } else {
                this.radius = GoogleMap.toRadiusMeters(this.centerMarker.getPosition(), this.radiusMarker.getPosition());
                this.circle.setRadius(this.radius);
                return true;
            }
        }

        public void removeFromMap() {
            this.circle.remove();
            this.centerMarker.remove();
            this.radiusMarker.remove();
        }

        public Marker getCenterMarker() {
            return this.centerMarker;
        }

        public Marker getRadiusMarker() {
            return this.radiusMarker;
        }

        public Circle getCircle() {
            return this.circle;
        }

        public Double getRadius() {
            return Double.valueOf(this.radius);
        }

        public void setRadiusMarker(Marker marker) {
            this.radiusMarker = marker;
        }
    }

    public GoogleMap(ComponentContainer container) throws IOException {
        super(container);
        this.context = container.$context();
        this.form = container.$form();
        this.savedInstanceState = this.form.getOnCreateBundle();
        this.mGoogleApiClient = new Builder(this.context, this, this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        this.viewLayout = new LinearLayout(this.context);
        this.viewLayout.setId(generateViewId());
        this.MAP_FRAGMENT_TAG = "map_" + System.currentTimeMillis();
        checkGooglePlayServiceSDK();
        checkGoogleMapInstalled();
        this.mMapFragment = (SupportMapFragment) this.form.getSupportFragmentManager().findFragmentByTag(this.MAP_FRAGMENT_TAG);
        if (this.mMapFragment == null) {
            this.mMapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = this.form.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(this.viewLayout.getId(), this.mMapFragment, this.MAP_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
        setUpMapIfNeeded();
        container.$add(this);
        Width(-2);
        Height(-2);
        this.form.registerForOnInitialize(this);
        this.form.registerForOnResume(this);
        this.form.registerForOnResume(this);
        this.form.registerForOnPause(this);
        MobileAnalytics.fabricTracking(container.$context().getPackageName(), TAG);
    }

    private static int generateViewId() {
        int result;
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    @SimpleProperty
    public void Width(int width) {
        if (width == -1) {
            width = -2;
        }
        super.Width(width);
    }

    @SimpleProperty
    public void Height(int height) {
        if (height == -1) {
            height = -2;
        }
        super.Height(height);
    }

    private void setUpMapIfNeeded() {
        if (this.myLocationEnabled) {
            setUpLocationClientIfNeeded();
            this.mGoogleApiClient.connect();
        }
        if (this.mMap == null) {
            this.mMapFragment.getMapAsync(new C03271());
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = new Builder(this.context, this, this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }
    }

    private void setUpMap() {
        CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder(this.mMap.getCameraPosition());
        if (this.initialLocation != null) {
            cameraPositionBuilder.target(this.initialLocation);
        }
        if (this.initialCameraZoomLevel != null) {
            cameraPositionBuilder.zoom(this.initialCameraZoomLevel.floatValue());
        }
        if (this.initialCameraTiltDegrees != null) {
            cameraPositionBuilder.tilt(this.initialCameraTiltDegrees.floatValue());
        }
        if (this.initialCameraBearingDegrees != null) {
            cameraPositionBuilder.bearing(this.initialCameraBearingDegrees.floatValue());
        }
        this.mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build()));
        if (this.initialStyleJson != null) {
            Style(this.initialStyleJson);
        } else if (this.initialTheme != this.theme) {
            Theme(this.initialTheme);
        }
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnInfoWindowClickListener(this);
        this.mMap.setOnMarkerDragListener(this);
        this.mUiSettings = this.mMap.getUiSettings();
        this.mUiSettings.setCompassEnabled(this.compassEnabled);
        this.mUiSettings.setRotateGesturesEnabled(this.rotateEnabled);
        this.mUiSettings.setScrollGesturesEnabled(this.scrollEnabled);
        this.mUiSettings.setZoomControlsEnabled(this.zoomControlEnabled);
        this.mUiSettings.setZoomGesturesEnabled(this.zoomGesturesEnabled);
        this.isMapReady = true;
        MapIsReady();
    }

    @SimpleFunction(description = "Enables/disables the compass widget on the map's ui. Call this only after event \"MapIsReady\" is received")
    public void EnableCompass(boolean enable) {
        this.compassEnabled = enable;
        this.mUiSettings.setCompassEnabled(enable);
    }

    @SimpleProperty(description = "Indicates whether the compass widget is currently enabled in the map ui")
    public boolean CompassEnabled() {
        return this.mUiSettings.isCompassEnabled();
    }

    @SimpleFunction(description = "Enables/disables the capability to rotate a map on the ui. Call this only after the event \"MapIsReady\" is received.")
    public void EnableRotate(boolean enable) {
        this.rotateEnabled = enable;
        this.mUiSettings.setRotateGesturesEnabled(enable);
    }

    @SimpleProperty(description = "Indicates whether the capability to rotate a map on the ui is currently enabled")
    public boolean RotateEnabled() {
        return this.mUiSettings.isRotateGesturesEnabled();
    }

    @SimpleFunction(description = "Enables/disables the capability to scroll a map on the ui. Call this only after the event \"MapIsReady\" is received")
    public void EnableScroll(boolean enable) {
        this.scrollEnabled = enable;
        this.mUiSettings.setScrollGesturesEnabled(enable);
    }

    @SimpleProperty(description = "Indicates whether the capability to scroll a map on the ui is currently enabled")
    public boolean ScrollEnabled() {
        return this.mUiSettings.isScrollGesturesEnabled();
    }

    @SimpleFunction(description = "Enables/disables the zoom widget on the map's ui. Call this only after the event \"MapIsReady\" is received")
    public void EnableZoomControl(boolean enable) {
        this.zoomControlEnabled = enable;
        this.mUiSettings.setZoomControlsEnabled(enable);
    }

    @SimpleProperty(description = "Indicates whether the zoom widget on the map ui is currently enabled")
    public boolean ZoomControlEnabled() {
        return this.mUiSettings.isZoomControlsEnabled();
    }

    @SimpleFunction(description = "Enables/disables zoom gesture on the map ui. Call this only after the event  \"MapIsReady\" is received. ")
    public void EnableZoomGesture(boolean enable) {
        this.zoomGesturesEnabled = enable;
        this.mUiSettings.setZoomGesturesEnabled(enable);
    }

    @SimpleProperty(description = "Indicates whether the zoom gesture is currently enabled")
    public boolean ZoomGestureEnabled() {
        return this.mUiSettings.isZoomGesturesEnabled();
    }

    @SimpleEvent(description = "Indicates that the map has been rendered and ready for adding markers or changing other settings. Please add or updating markers within this event")
    public void MapIsReady() {
        if (this.isMapReady && this.isScreenInitialized && !this.isMapIsReadyDispatched) {
            this.isMapIsReadyDispatched = true;
            EventDispatcher.dispatchEvent(this, "MapIsReady", new Object[0]);
        }
    }

    private void checkGooglePlayServiceSDK() {
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.context)) {
            case 1:
                this.form.dispatchErrorOccurredEvent(this, "checkGooglePlayServiceSDK", ErrorMessages.ERROR_GOOGLE_PLAY_NOT_INSTALLED, new Object[0]);
                return;
            case 2:
                this.form.dispatchErrorOccurredEvent(this, "checkGooglePlayServiceSDK", ErrorMessages.ERROR_GOOGLE_PLAY_SERVICE_UPDATE_REQUIRED, new Object[0]);
                return;
            case 3:
                this.form.dispatchErrorOccurredEvent(this, "checkGooglePlayServiceSDK", ErrorMessages.ERROR_GOOGLE_PLAY_DISABLED, new Object[0]);
                return;
            case 9:
                this.form.dispatchErrorOccurredEvent(this, "checkGooglePlayServiceSDK", ErrorMessages.ERROR_GOOGLE_PLAY_INVALID, new Object[0]);
                return;
            default:
                return;
        }
    }

    private void checkGoogleMapInstalled() {
        try {
            this.context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
        } catch (NameNotFoundException e) {
            this.form.dispatchErrorOccurredEvent(this, "checkGoogleMapInstalled", ErrorMessages.ERROR_GOOGLE_MAP_NOT_INSTALLED, new Object[0]);
        }
    }

    @SimpleFunction(description = "Create a circle overlay on the map UI with specified latitude and longitude for center. \"hue\" (min 0, max 360) and \"alpha\" (min 0, max 255) are used to set color and transparency level of the circle, \"strokeWidth\" and \"strokeColor\" are for the perimeter of the circle. Returning a unique id of the circle for future reference to events raised by moving this circle. If the circle isset to be draggable, two default markers will appear on the map: one in the center of the circle, another on the perimeter.")
    public int AddCircle(double lat, double lng, double radius, int alpha, float hue, float strokeWidth, int strokeColor, boolean draggable) {
        return AddCircle2(generateCircleId(), lat, lng, radius, alpha, hue, strokeWidth, strokeColor, draggable);
    }

    public int AddCircle2(int uid, double lat, double lng, double radius, int alpha, float hue, float strokeWidth, int strokeColor, boolean draggable) {
        return AddCircle3(uid, lat, lng, radius, Color.HSVToColor(alpha, new float[]{hue, 1.0f, 1.0f}), strokeWidth, strokeColor, draggable);
    }

    public int AddCircle3(int uid, double lat, double lng, double radius, int fillColor, float strokeWidth, int strokeColor, boolean draggable) {
        if (draggable) {
            DraggableCircle circle = new DraggableCircle(new LatLng(lat, lng), radius, strokeWidth, strokeColor, fillColor);
            circle.setRadiusMarker(null);
            this.mCircles.add(circle);
            this.circles.put(circle, Integer.valueOf(uid));
        } else {
            this.circles.put(this.mMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radius).strokeWidth(strokeWidth).strokeColor(strokeColor).fillColor(fillColor)), Integer.valueOf(uid));
        }
        return uid;
    }

    private Object getCircleIfExisted(int circleId) {
        Object circle = getKeyByValue(this.circles, Integer.valueOf(circleId));
        if (circle != null) {
            return circle;
        }
        this.form.dispatchErrorOccurredEvent(this, "getCircleIfExisted", ErrorMessages.ERROR_GOOGLE_MAP_CIRCLE_NOT_EXIST, Integer.toString(circleId));
        return null;
    }

    @SimpleFunction(description = "Remove a circle for the map. Returns true if successfully removed, false if the circle does not exist with the specified id")
    public boolean RemoveCircle(int circleId) {
        Object circle = getKeyByValue(this.circles, Integer.valueOf(circleId));
        if (circle == null) {
            return false;
        }
        if (circle instanceof DraggableCircle) {
            ((DraggableCircle) circle).removeFromMap();
            this.mCircles.remove(circle);
        }
        if (circle instanceof Circle) {
            ((Circle) circle).remove();
        }
        this.circles.remove(circle);
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.appinventor.components.annotations.SimpleFunction(description = "Set the property of an existing circle. Properties include: \"alpha\"(number, transparency level ranging from 0~255), \"hue\" (number, color value ranging 0~360), \"radius\"(number in meters), and many more. Please refer to parameters in the AddCircle method.")
    public void UpdateCircle(int r35, java.lang.String r36, java.lang.Object r37) {
        /*
        r34 = this;
        r4 = 3;
        r0 = new float[r4];
        r23 = r0;
        r19 = r34.getCircleIfExisted(r35);
        r27 = 0;
        r21 = 0;
        if (r19 == 0) goto L_0x01fe;
    L_0x000f:
        r0 = r19;
        r4 = r0 instanceof com.google.appinventor.components.runtime.GoogleMap.DraggableCircle;
        if (r4 == 0) goto L_0x001f;
    L_0x0015:
        r4 = r19;
        r4 = (com.google.appinventor.components.runtime.GoogleMap.DraggableCircle) r4;
        r27 = r4.getCircle();
        r21 = 1;
    L_0x001f:
        r0 = r19;
        r4 = r0 instanceof com.google.android.gms.maps.model.Circle;
        if (r4 == 0) goto L_0x0029;
    L_0x0025:
        r27 = r19;
        r27 = (com.google.android.gms.maps.model.Circle) r27;
    L_0x0029:
        r4 = 0;
        r28 = java.lang.Float.valueOf(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = "radius";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 != 0) goto L_0x0056;
    L_0x0038:
        r4 = "alpha";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 != 0) goto L_0x0056;
    L_0x0042:
        r4 = "hue";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 != 0) goto L_0x0056;
    L_0x004c:
        r4 = "strokeWidth";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x005e;
    L_0x0056:
        r4 = r37.toString();	 Catch:{ NumberFormatException -> 0x00c4 }
        r28 = java.lang.Float.valueOf(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
    L_0x005e:
        r4 = "alpha";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x008d;
    L_0x0068:
        r20 = r27.getFillColor();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r20;
        r1 = r23;
        android.graphics.Color.colorToHSV(r0, r1);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r28.intValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r17 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r17.intValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r23;
        r24 = android.graphics.Color.HSVToColor(r4, r0);	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r27;
        r1 = r24;
        r0.setFillColor(r1);	 Catch:{ NumberFormatException -> 0x00c4 }
    L_0x008c:
        return;
    L_0x008d:
        r4 = "hue";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x00e9;
    L_0x0097:
        r4 = r27.getFillColor();	 Catch:{ NumberFormatException -> 0x00c4 }
        r16 = android.graphics.Color.alpha(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = 3;
        r4 = new float[r4];	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = 0;
        r30 = r28.floatValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r4[r29] = r30;	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = 1;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4[r29] = r30;	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = 2;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4[r29] = r30;	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r16;
        r24 = android.graphics.Color.HSVToColor(r0, r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r27;
        r1 = r24;
        r0.setFillColor(r1);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x00c4:
        r22 = move-exception;
        r0 = r34;
        r4 = r0.form;
        r29 = "UpdateCircle";
        r30 = 12012; // 0x2eec float:1.6832E-41 double:5.9347E-320;
        r31 = 1;
        r0 = r31;
        r0 = new java.lang.Object[r0];
        r31 = r0;
        r32 = 0;
        r33 = r37.toString();
        r31[r32] = r33;
        r0 = r34;
        r1 = r29;
        r2 = r30;
        r3 = r31;
        r4.dispatchErrorOccurredEvent(r0, r1, r2, r3);
        goto L_0x008c;
    L_0x00e9:
        r4 = "radius";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x0161;
    L_0x00f3:
        r10 = r28;
        r4 = r10.floatValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = (double) r4;	 Catch:{ NumberFormatException -> 0x00c4 }
        r30 = r0;
        r0 = r27;
        r1 = r30;
        r0.setRadius(r1);	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r19;
        r4 = r0 instanceof com.google.appinventor.components.runtime.GoogleMap.DraggableCircle;	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x008c;
    L_0x0109:
        r0 = r19;
        r0 = (com.google.appinventor.components.runtime.GoogleMap.DraggableCircle) r0;	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r0;
        r18 = r4.getCenterMarker();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r19;
        r0 = (com.google.appinventor.components.runtime.GoogleMap.DraggableCircle) r0;	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r0;
        r26 = r4.getRadiusMarker();	 Catch:{ NumberFormatException -> 0x00c4 }
        r26.remove();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r34;
        r4 = r0.mMap;	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = new com.google.android.gms.maps.model.MarkerOptions;	 Catch:{ NumberFormatException -> 0x00c4 }
        r29.<init>();	 Catch:{ NumberFormatException -> 0x00c4 }
        r30 = r18.getPosition();	 Catch:{ NumberFormatException -> 0x00c4 }
        r31 = r10.floatValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r31;
        r0 = (double) r0;	 Catch:{ NumberFormatException -> 0x00c4 }
        r32 = r0;
        r0 = r30;
        r1 = r32;
        r30 = toRadiusLatLng(r0, r1);	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = r29.position(r30);	 Catch:{ NumberFormatException -> 0x00c4 }
        r30 = 1;
        r29 = r29.draggable(r30);	 Catch:{ NumberFormatException -> 0x00c4 }
        r30 = 1129447424; // 0x43520000 float:210.0 double:5.58021171E-315;
        r30 = com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(r30);	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = r29.icon(r30);	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r29;
        r25 = r4.addMarker(r0);	 Catch:{ NumberFormatException -> 0x00c4 }
        r19 = (com.google.appinventor.components.runtime.GoogleMap.DraggableCircle) r19;	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r19;
        r1 = r25;
        r0.setRadiusMarker(r1);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x0161:
        r4 = "strokeWidth";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x0176;
    L_0x016b:
        r4 = r28.floatValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r27;
        r0.setStrokeWidth(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x0176:
        r4 = "strokeColor";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x0197;
    L_0x0180:
        r4 = r37.toString();	 Catch:{ NumberFormatException -> 0x00c4 }
        r20 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = java.lang.Integer.valueOf(r20);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r4.intValue();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r27;
        r0.setStrokeColor(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x0197:
        r4 = "draggable";
        r0 = r36;
        r4 = r0.equals(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r4 == 0) goto L_0x01dd;
    L_0x01a1:
        r4 = r37.toString();	 Catch:{ NumberFormatException -> 0x00c4 }
        r15 = java.lang.Boolean.parseBoolean(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        if (r21 == 0) goto L_0x01ad;
    L_0x01ab:
        if (r15 != 0) goto L_0x008c;
    L_0x01ad:
        r4 = r27.getId();	 Catch:{ NumberFormatException -> 0x00c4 }
        r5 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r27.getCenter();	 Catch:{ NumberFormatException -> 0x00c4 }
        r6 = r4.longitude;	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r27.getCenter();	 Catch:{ NumberFormatException -> 0x00c4 }
        r8 = r4.latitude;	 Catch:{ NumberFormatException -> 0x00c4 }
        r10 = r27.getRadius();	 Catch:{ NumberFormatException -> 0x00c4 }
        r12 = r27.getFillColor();	 Catch:{ NumberFormatException -> 0x00c4 }
        r13 = r27.getStrokeWidth();	 Catch:{ NumberFormatException -> 0x00c4 }
        r14 = r27.getStrokeColor();	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r34;
        r0.RemoveCircle(r5);	 Catch:{ NumberFormatException -> 0x00c4 }
        r4 = r34;
        r4.AddCircle3(r5, r6, r8, r10, r12, r13, r14, r15);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x01dd:
        r0 = r34;
        r4 = r0.form;	 Catch:{ NumberFormatException -> 0x00c4 }
        r29 = "UpdateCircle";
        r30 = 12019; // 0x2ef3 float:1.6842E-41 double:5.938E-320;
        r31 = 1;
        r0 = r31;
        r0 = new java.lang.Object[r0];	 Catch:{ NumberFormatException -> 0x00c4 }
        r31 = r0;
        r32 = 0;
        r31[r32] = r36;	 Catch:{ NumberFormatException -> 0x00c4 }
        r0 = r34;
        r1 = r29;
        r2 = r30;
        r3 = r31;
        r4.dispatchErrorOccurredEvent(r0, r1, r2, r3);	 Catch:{ NumberFormatException -> 0x00c4 }
        goto L_0x008c;
    L_0x01fe:
        r0 = r34;
        r4 = r0.form;
        r29 = "UpdateCircle";
        r30 = 12015; // 0x2eef float:1.6837E-41 double:5.936E-320;
        r31 = 1;
        r0 = r31;
        r0 = new java.lang.Object[r0];
        r31 = r0;
        r32 = 0;
        r33 = java.lang.Integer.valueOf(r35);
        r31[r32] = r33;
        r0 = r34;
        r1 = r29;
        r2 = r30;
        r3 = r31;
        r4.dispatchErrorOccurredEvent(r0, r1, r2, r3);
        goto L_0x008c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.GoogleMap.UpdateCircle(int, java.lang.String, java.lang.Object):void");
    }

    @SimpleFunction(description = "Get all circles Ids. A short cut to get all the references for the eixisting circles")
    public YailList GetAllCircleIDs() {
        return YailList.makeList(this.circles.values());
    }

    public void FinishedDraggingCircle(int id, double centerLat, double centerLng, double radius) {
        final int i = id;
        final double d = centerLat;
        final double d2 = centerLng;
        final double d3 = radius;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "FinishedDraggingCircle", Integer.valueOf(i), Double.valueOf(d), Double.valueOf(d2), Double.valueOf(d3));
            }
        });
    }

    private static LatLng convertPointToLatLng(YailList point) {
        return new LatLng(((Number) point.getObject(0)).doubleValue(), ((Number) point.getObject(1)).doubleValue());
    }

    private static List<LatLng> convertPointsToLatLngs(YailList points) {
        List<LatLng> latLngs = new ArrayList();
        for (int index = 0; index < points.size(); index++) {
            latLngs.add(convertPointToLatLng((YailList) points.getObject(index)));
        }
        return latLngs;
    }

    @SimpleFunction(description = "Convert a JsonArray of points (lat, lng pairs) to a list.")
    public YailList GetPointsFromJson(String jsonString) {
        YailList points = null;
        try {
            List pointsList = new ArrayList();
            for (List point : (List) JsonUtil.getObjectFromJson(jsonString)) {
                pointsList.add(YailList.makeList(point));
            }
            points = YailList.makeList(pointsList);
        } catch (Exception e) {
            this.form.dispatchErrorOccurredEvent(this, "GetPointsFromJson", ErrorMessages.ERROR_GOOGLE_MAP_JSON_FORMAT_DECODE_FAILED, jsonString);
        }
        return points;
    }

    @SimpleFunction(description = "Create a polyline on the map. \"points\" (list of lat, lng pairs), line segments are drawn between consecutive points\"width\" (pixels) the width of the line segments, \"color\" the color of the line segments. Returns the unique id of the polyline.")
    public int AddPolyline(YailList points, float width, int color) {
        Polyline polyline = this.mMap.addPolyline(new PolylineOptions().addAll(convertPointsToLatLngs(points)).width(width).color(color));
        int id = generatePolylineId();
        this.polylinesByIds.put(Integer.valueOf(id), polyline);
        return id;
    }

    @SimpleFunction(description = "Remove a polyline for the map. Returns true if successfully removed, false if the polyline does not exist with the specified id.")
    public boolean RemovePolyline(int polylineId) {
        Polyline polyline = (Polyline) this.polylinesByIds.remove(Integer.valueOf(polylineId));
        if (polyline == null) {
            return false;
        }
        polyline.remove();
        return true;
    }

    @SimpleFunction(description = "Set the property of an existing polyline. Properties: \"points\" (list of lat, lng pairs), line segments are drawn between consecutive points\"width\" (pixels) the width of the line segments, \"color\" the color of the line segments.")
    public void UpdatePolyline(int polylineId, String propertyName, Object value) {
        String UPDATE_POLYLINE = "UpdatePolyline";
        Polyline polyline = (Polyline) this.polylinesByIds.get(Integer.valueOf(polylineId));
        boolean invalidInput = false;
        if (polyline != null) {
            if (propertyName.equals("points")) {
                List<LatLng> latLngs = null;
                try {
                    latLngs = convertPointsToLatLngs((YailList) value);
                } catch (Exception e) {
                    invalidInput = true;
                }
                if (latLngs != null) {
                    polyline.setPoints(latLngs);
                }
            } else {
                if (propertyName.equals(SettingsJsonConstants.ICON_WIDTH_KEY)) {
                    Float width = null;
                    try {
                        width = Float.valueOf(((Number) value).floatValue());
                    } catch (Exception e2) {
                        invalidInput = true;
                    }
                    if (width != null) {
                        polyline.setWidth(width.floatValue());
                    }
                } else {
                    if (propertyName.equals(PropertyTypeConstants.PROPERTY_TYPE_COLOR)) {
                        Integer color = null;
                        try {
                            color = Integer.valueOf(((Number) value).intValue());
                        } catch (Exception e3) {
                            invalidInput = true;
                        }
                        if (color != null) {
                            polyline.setColor(color.intValue());
                        }
                    } else {
                        this.form.dispatchErrorOccurredEvent(this, "UpdatePolyline", ErrorMessages.ERROR_GOOGLE_MAP_PROPERTY_NOT_EXIST, propertyName);
                    }
                }
            }
            if (invalidInput) {
                this.form.dispatchErrorOccurredEvent(this, "UpdatePolyline", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, value.toString());
                return;
            }
            return;
        }
        this.form.dispatchErrorOccurredEvent(this, "UpdatePolyline", ErrorMessages.ERROR_GOOGLE_MAP_POLYLINE_NOT_EXIST, Integer.valueOf(polylineId));
    }

    @SimpleFunction(description = "Get all polyline Ids.")
    public YailList GetAllPolylineIds() {
        return YailList.makeList(this.polylinesByIds.keySet());
    }

    public View getView() {
        return this.viewLayout;
    }

    public void onResume() {
    }

    public void onInitialize() {
        this.isScreenInitialized = true;
        MapIsReady();
    }

    private void prepareFragmentView() {
        this.mMapFragment = SupportMapFragment.newInstance();
        this.androidUIHandler.post(new C03293());
    }

    @SimpleFunction(description = "Enable or disable my location widget control for Google Map. One can call GetMyLocation() to obtain the current location after enable this.\"")
    public void EnableMyLocation(boolean enabled) {
        if (this.myLocationEnabled != enabled) {
            this.myLocationEnabled = enabled;
        }
        if (this.mMap == null) {
            setUpMapIfNeeded();
        }
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0 && ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            this.mMap.setMyLocationEnabled(enabled);
            if (enabled) {
                setUpLocationClientIfNeeded();
                this.mGoogleApiClient.connect();
                return;
            }
            this.mGoogleApiClient.disconnect();
        }
    }

    @SimpleProperty(description = "Indicates whether my locaiton UI control is currently enabled for the Google map.")
    public boolean MyLocationEnabled() {
        return this.myLocationEnabled;
    }

    @SimpleFunction(description = "Get current location using Google Map Service. Return a YailList with first item being the latitude, the second item being the longitude, and last time being the accuracy of the reading.")
    public YailList GetMyLocation() {
        List latLng = new ArrayList();
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
            if (location != null) {
                latLng.add(Double.valueOf(location.getLatitude()));
                latLng.add(Double.valueOf(location.getLongitude()));
                latLng.add(Float.valueOf(location.getAccuracy()));
            } else {
                latLng.add(Float.valueOf(0.0f));
                latLng.add(Float.valueOf(0.0f));
                latLng.add(Float.valueOf(0.0f));
            }
        }
        return YailList.makeList(latLng);
    }

    @SimpleFunction(description = "Set the layer of Google map. Default layer is \"normal\", other choices including \"hybrid\",\"satellite\", and \"terrain\" ")
    public void SetMapType(String layerName) {
        if (layerName.equals("normal")) {
            this.mapType = 1;
        } else if (layerName.equals("hybrid")) {
            this.mapType = 4;
        } else if (layerName.equals("satellite")) {
            this.mapType = 2;
        } else if (layerName.equals("terrain")) {
            this.mapType = 3;
        } else {
            this.form.dispatchErrorOccurredEvent(this, "SetMapType", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, layerName + " is not the correct type");
        }
        if (this.mMap != null) {
            this.mMap.setMapType(this.mapType);
        }
    }

    @SimpleFunction(description = "Enable/Disable to listen to map's click event")
    public void EnableMapClickListener(boolean enabled) {
        OnMapClickListener onMapClickListener;
        if (this.enableMapClickListener != enabled) {
            this.enableMapClickListener = enabled;
        }
        if (this.mMap == null) {
            setUpMapIfNeeded();
        }
        com.google.android.gms.maps.GoogleMap googleMap = this.mMap;
        if (!enabled) {
            onMapClickListener = null;
        }
        googleMap.setOnMapClickListener(onMapClickListener);
    }

    @SimpleProperty(description = "Indicates if the mapClick event listener is currently enabled")
    public boolean MapClickListenerEnabled() {
        return this.enableMapClickListener;
    }

    @SimpleFunction(description = "Enable/disable to listen to map's long click event")
    public void EnableMapLongClickListener(boolean enabled) {
        OnMapLongClickListener onMapLongClickListener;
        if (this.enableMapLongClickListener != enabled) {
            this.enableMapLongClickListener = enabled;
        }
        if (this.mMap == null) {
            setUpMapIfNeeded();
        }
        com.google.android.gms.maps.GoogleMap googleMap = this.mMap;
        if (!enabled) {
            onMapLongClickListener = null;
        }
        googleMap.setOnMapLongClickListener(onMapLongClickListener);
    }

    @SimpleProperty(description = "Indicates if the map longClick listener is currently enabled")
    public boolean MapLongClickListenerEnabled() {
        return this.enableMapLongClickListener;
    }

    public void EnableMapCameraPosChangeListener(boolean enabled) {
        OnCameraChangeListener onCameraChangeListener;
        if (this.enableCameraChangeListener != enabled) {
            this.enableCameraChangeListener = enabled;
        }
        if (this.mMap == null) {
            setUpMapIfNeeded();
        }
        com.google.android.gms.maps.GoogleMap googleMap = this.mMap;
        if (!enabled) {
            onCameraChangeListener = null;
        }
        googleMap.setOnCameraChangeListener(onCameraChangeListener);
    }

    public boolean MapCameraChangedListenerEnabled() {
        return this.enableCameraChangeListener;
    }

    public String MapType() {
        switch (this.mapType) {
            case 1:
                return "normal";
            case 2:
                return "satellite";
            case 3:
                return "terrain";
            case 4:
                return "hybrid";
            default:
                return null;
        }
    }

    public YailList AddMarkers(YailList markers) {
        float[] hsv = new float[3];
        List markerIds = new ArrayList();
        for (Object marker : markers.toArray()) {
            boolean addOne = true;
            if (marker instanceof YailList) {
                if (((YailList) marker).size() < 2) {
                    addOne = false;
                }
                Object latObj = ((YailList) marker).getObject(0);
                Object lngObj = ((YailList) marker).getObject(1);
                Double lat = new Double(0.0d);
                Double lng = new Double(0.0d);
                if ((latObj instanceof DFloNum) && (lngObj instanceof DFloNum)) {
                    lat = Double.valueOf(((DFloNum) latObj).doubleValue());
                    lng = Double.valueOf(((DFloNum) lngObj).doubleValue());
                } else {
                    addOne = false;
                }
                if (lat.doubleValue() < -90.0d || lat.doubleValue() > 90.0d || lng.doubleValue() < -180.0d || lng.doubleValue() > 180.0d) {
                    addOne = false;
                }
                int color = this.mMarkerColor;
                String title = "";
                String snippet = "";
                boolean draggable = this.mMarkerDraggable;
                if (((YailList) marker).size() >= 3) {
                    if (((YailList) marker).getObject(2) instanceof IntNum) {
                        color = ((IntNum) ((YailList) marker).getObject(2)).intValue();
                    } else {
                        addOne = false;
                    }
                }
                if (((YailList) marker).size() >= 4) {
                    title = ((YailList) marker).getObject(3).toString();
                }
                if (((YailList) marker).size() >= 5) {
                    snippet = ((YailList) marker).getObject(4).toString();
                }
                if (((YailList) marker).size() >= 6) {
                    if (((YailList) marker).getObject(5) instanceof Boolean) {
                        draggable = ((Boolean) ((YailList) marker).getObject(5)).booleanValue();
                    } else {
                        addOne = false;
                    }
                }
                Color.colorToHSV(color, hsv);
                if (addOne) {
                    int uniqueId = generateMarkerId();
                    markerIds.add(Integer.valueOf(uniqueId));
                    addMarkerToMap(lat, lng, uniqueId, hsv[0], "", title, snippet, draggable);
                }
            } else {
                this.form.dispatchErrorOccurredEvent(this, "AddMarkers", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "marker is not represented as list");
            }
        }
        return YailList.makeList(markerIds);
    }

    private static int generateMarkerId() {
        return snextMarkerId.incrementAndGet();
    }

    private static int generateCircleId() {
        return snextCircleId.incrementAndGet();
    }

    private static int generatePolylineId() {
        return snextPolylineId.incrementAndGet();
    }

    private int addMarkerToMap(Double lat, Double lng, int id, float hue, String icon, String title, String snippet, boolean draggable) {
        if (this.mMap == null) {
            setUpMapIfNeeded();
        }
        Marker marker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(lat.doubleValue(), lng.doubleValue())));
        if (icon.isEmpty()) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
        } else {
            Bitmap iconBitmap;
            try {
                iconBitmap = MediaUtil.getBitmapDrawable(this.form, icon).getBitmap();
            } catch (IOException e) {
                Log.e(TAG, "Unable to load " + icon);
                iconBitmap = null;
            }
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
        }
        if (!title.isEmpty()) {
            marker.setTitle(title);
        }
        if (!snippet.isEmpty()) {
            marker.setSnippet(snippet);
        }
        marker.setDraggable(draggable);
        this.markers.put(marker, Integer.valueOf(id));
        return id;
    }

    @SimpleFunction(description = "Add a list of markers composed of name-value pairs. Name fields for a marker are: \"lat\" (type double) [required], \"lng\"(type double) [required], \"color\"(type int)[in hue value ranging from 0~360], \"title\"(type String), \"snippet\"(type String), \"draggable\"(type boolean)")
    public YailList GetMarkers() {
        return this.markersList;
    }

    @SimpleFunction(description = "Add a standard marker to the map. \"lat\" (type double) [required], \"lng\"(type double) [required], \"color\"(type int)[in hue value ranging from 0~360], \"title\"(type String), \"snippet\"(type String), \"draggable\"(type boolean). Returns the unique id of the marker.")
    public int AddMarkerStandard(double lat, double lng, int color, String title, String snippet, boolean draggable) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return addMarkerToMap(Double.valueOf(lat), Double.valueOf(lng), generateMarkerId(), hsv[0], "", title, snippet, draggable);
    }

    @SimpleFunction(description = "Add a custom marker to the map. \"lat\" (type double) [required], \"lng\"(type double) [required], \"icon\"(type String)[the path of the icon], \"title\"(type String), \"snippet\"(type String), \"draggable\"(type boolean). Returns the unique id of the marker.")
    public int AddMarkerCustom(double lat, double lng, String icon, String title, String snippet, boolean draggable) {
        return addMarkerToMap(Double.valueOf(lat), Double.valueOf(lng), generateMarkerId(), 0.0f, icon, title, snippet, draggable);
    }

    @SimpleFunction(description = "Adding a list of markers that are represented as JsonArray.  The inner JsonObject represents a markerand is composed of name-value pairs. Name fields for a marker are: \"lat\" (type double) [required], \"lng\"(type double) [required], \"color\"(type int)[in hue value ranging from 0~360], \"icon\"(type String)[the path of the icon], \"title\"(type String), \"snippet\"(type String), \"draggable\"(type boolean)")
    public void AddMarkersFromJson(String jsonString) {
        List markerIds = new ArrayList();
        float[] hsv = new float[3];
        JsonElement markerList = new JsonParser().parse(jsonString);
        if (markerList.isJsonArray()) {
            Iterator it = markerList.getAsJsonArray().iterator();
            while (it.hasNext()) {
                JsonElement marker = (JsonElement) it.next();
                boolean addOne = true;
                if (marker.isJsonObject()) {
                    JsonObject markerJson = marker.getAsJsonObject();
                    if (markerJson.get("lat") != null) {
                        if (markerJson.get("lng") != null) {
                            JsonPrimitive jpLat = (JsonPrimitive) markerJson.get("lat");
                            JsonPrimitive jpLng = (JsonPrimitive) markerJson.get("lng");
                            double d = 0.0d;
                            double longitude = 0.0d;
                            try {
                                float defaultColor;
                                float color;
                                String icon;
                                String title;
                                String snippet;
                                boolean draggable;
                                int uniqueId;
                                if (jpLat.isString() && jpLng.isString()) {
                                    d = new Double(jpLat.getAsString()).doubleValue();
                                    longitude = new Double(jpLng.getAsString()).doubleValue();
                                    addOne = false;
                                    Color.colorToHSV(this.mMarkerColor, hsv);
                                    defaultColor = hsv[0];
                                    if (markerJson.get(PropertyTypeConstants.PROPERTY_TYPE_COLOR) != null) {
                                        color = (float) markerJson.get(PropertyTypeConstants.PROPERTY_TYPE_COLOR).getAsInt();
                                    } else {
                                        color = defaultColor;
                                    }
                                    addOne = false;
                                    if (markerJson.get(SettingsJsonConstants.APP_ICON_KEY) != null) {
                                        icon = markerJson.get(SettingsJsonConstants.APP_ICON_KEY).getAsString();
                                    } else {
                                        icon = "";
                                    }
                                    if (markerJson.get(SettingsJsonConstants.PROMPT_TITLE_KEY) != null) {
                                        title = markerJson.get(SettingsJsonConstants.PROMPT_TITLE_KEY).getAsString();
                                    } else {
                                        title = "";
                                    }
                                    if (markerJson.get("snippet") != null) {
                                        snippet = markerJson.get("snippet").getAsString();
                                    } else {
                                        snippet = "";
                                    }
                                    if (markerJson.get("draggable") != null) {
                                        draggable = markerJson.get("draggable").getAsBoolean();
                                    } else {
                                        draggable = this.mMarkerDraggable;
                                    }
                                    if (addOne) {
                                        uniqueId = generateMarkerId();
                                        markerIds.add(Integer.valueOf(uniqueId));
                                        addMarkerToMap(Double.valueOf(d), Double.valueOf(longitude), uniqueId, color, icon, title, snippet, draggable);
                                    }
                                } else {
                                    d = markerJson.get("lat").getAsDouble();
                                    longitude = markerJson.get("lng").getAsDouble();
                                    if (d < -90.0d || d > 90.0d || longitude < -180.0d || longitude > 180.0d) {
                                        addOne = false;
                                    }
                                    try {
                                        Color.colorToHSV(this.mMarkerColor, hsv);
                                        defaultColor = hsv[0];
                                        if (markerJson.get(PropertyTypeConstants.PROPERTY_TYPE_COLOR) != null) {
                                            color = defaultColor;
                                        } else {
                                            color = (float) markerJson.get(PropertyTypeConstants.PROPERTY_TYPE_COLOR).getAsInt();
                                        }
                                        if (color < 0.0f || color > 360.0f) {
                                            addOne = false;
                                        }
                                        if (markerJson.get(SettingsJsonConstants.APP_ICON_KEY) != null) {
                                            icon = "";
                                        } else {
                                            icon = markerJson.get(SettingsJsonConstants.APP_ICON_KEY).getAsString();
                                        }
                                        if (markerJson.get(SettingsJsonConstants.PROMPT_TITLE_KEY) != null) {
                                            title = "";
                                        } else {
                                            title = markerJson.get(SettingsJsonConstants.PROMPT_TITLE_KEY).getAsString();
                                        }
                                        if (markerJson.get("snippet") != null) {
                                            snippet = "";
                                        } else {
                                            snippet = markerJson.get("snippet").getAsString();
                                        }
                                        if (markerJson.get("draggable") != null) {
                                            draggable = this.mMarkerDraggable;
                                        } else {
                                            draggable = markerJson.get("draggable").getAsBoolean();
                                        }
                                        if (addOne) {
                                            uniqueId = generateMarkerId();
                                            markerIds.add(Integer.valueOf(uniqueId));
                                            addMarkerToMap(Double.valueOf(d), Double.valueOf(longitude), uniqueId, color, icon, title, snippet, draggable);
                                        }
                                    } catch (JsonSyntaxException e) {
                                        this.form.dispatchErrorOccurredEvent(this, "AddMarkersFromJson", ErrorMessages.ERROR_GOOGLE_MAP_JSON_FORMAT_DECODE_FAILED, jsonString);
                                        this.markersList = YailList.makeList(markerIds);
                                    }
                                }
                            } catch (NumberFormatException e2) {
                                addOne = false;
                            }
                        }
                    }
                }
            }
        } else {
            this.form.dispatchErrorOccurredEvent(this, "AddMarkersFromJson", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "markers needs to be represented as JsonArray");
            this.markersList = YailList.makeList(markerIds);
        }
        this.markersList = YailList.makeList(markerIds);
    }

    public YailList AddMarkersHue(YailList markers) {
        List markerIds = new ArrayList();
        Object[] toArray = markers.toArray();
        int length = toArray.length;
        int i = 0;
        while (i < length) {
            Object marker = toArray[i];
            boolean addOne = true;
            if (marker instanceof YailList) {
                if (((YailList) marker).size() < 2) {
                    this.form.dispatchErrorOccurredEvent(this, "AddMarkers", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "Need more than 2 inputs");
                    addOne = false;
                }
                Object latObj = ((YailList) marker).getObject(0);
                Object lngObj = ((YailList) marker).getObject(1);
                Double lat = new Double(0.0d);
                Double lng = new Double(0.0d);
                if ((latObj instanceof DFloNum) && (lngObj instanceof DFloNum)) {
                    lat = Double.valueOf(((DFloNum) latObj).doubleValue());
                    lng = Double.valueOf(((DFloNum) lngObj).doubleValue());
                } else {
                    this.form.dispatchErrorOccurredEvent(this, "AddMarkersHue", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "Not a number for latitude or longitude");
                    addOne = false;
                }
                if (lat.doubleValue() < -90.0d || lat.doubleValue() > 90.0d || lng.doubleValue() < -180.0d || lng.doubleValue() > 180.0d) {
                    addOne = false;
                    this.form.dispatchErrorOccurredEvent(this, "AddMarkers", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "Range for the latitude or longitude is wrong");
                }
                Integer uniqueId = Integer.valueOf(generateMarkerId());
                float color = 240.0f;
                String title = "";
                String snippet = "";
                boolean draggable = this.mMarkerDraggable;
                if (((YailList) marker).size() >= 3) {
                    if (((YailList) marker).getObject(2) instanceof IntNum) {
                        color = new Float((float) ((IntNum) ((YailList) marker).getObject(2)).intValue()).floatValue();
                    } else {
                        addOne = false;
                        this.form.dispatchErrorOccurredEvent(this, "AddMarkersHue", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, colorObj.toString() + " is not a number");
                    }
                }
                if (((YailList) marker).size() >= 4) {
                    title = (String) ((YailList) marker).getObject(3);
                }
                if (((YailList) marker).size() >= 5) {
                    snippet = (String) ((YailList) marker).getObject(4);
                }
                if (((YailList) marker).size() >= 6) {
                    if (((YailList) marker).getObject(5) instanceof Boolean) {
                        draggable = ((Boolean) ((YailList) marker).getObject(5)).booleanValue();
                    } else {
                        this.form.dispatchErrorOccurredEvent(this, "AddMarkers", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "marker not as a list");
                        addOne = false;
                    }
                }
                if (addOne) {
                    markerIds.add(uniqueId);
                    addMarkerToMap(lat, lng, uniqueId.intValue(), color, "", title, snippet, draggable);
                }
                i++;
            } else {
                this.form.dispatchErrorOccurredEvent(this, "AddMarkersHue", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, "Marker is not represented as list");
                return YailList.makeList(markerIds);
            }
        }
        return YailList.makeList(markerIds);
    }

    @SimpleFunction(description = "Set the property of a marker, note that the marker has to be added first or else will throw an exception! Properties include: \"color\"(hue value ranging from 0~360), \"icon\"(the path of the icon), \"title\", \"snippet\", \"draggable\"(give either true or false as the value).")
    public void UpdateMarker(int markerId, String propertyName, Object value) {
        String property = propertyName.trim();
        String propVal = value.toString().trim();
        Marker marker = getMarkerIfExisted(markerId);
        if (marker != null) {
            if (property.equals(PropertyTypeConstants.PROPERTY_TYPE_COLOR)) {
                Float hue = new Float(propVal);
                if (hue.floatValue() < 0.0f || hue.floatValue() > 360.0f) {
                    this.form.dispatchErrorOccurredEvent(this, "UpdateMarker", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_INPUT, hue.toString());
                } else {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(new Float(propVal).floatValue()));
                }
            }
            if (property.equals(SettingsJsonConstants.APP_ICON_KEY)) {
                Bitmap icon;
                try {
                    icon = MediaUtil.getBitmapDrawable(this.form, propVal).getBitmap();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to load " + propVal);
                    icon = null;
                }
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            }
            if (property.equals(SettingsJsonConstants.PROMPT_TITLE_KEY)) {
                marker.setTitle(propVal);
            }
            if (property.equals("snippet")) {
                marker.setSnippet(propVal);
            }
            if (property.equals("draggable")) {
                marker.setDraggable(new Boolean(propVal).booleanValue());
            }
        }
    }

    @SimpleFunction(description = "Get all the existing markers's Ids")
    public YailList GetAllMarkerID() {
        return YailList.makeList(this.markers.values());
    }

    private Marker getMarkerIfExisted(int markerId) {
        Marker marker = (Marker) getKeyByValue(this.markers, Integer.valueOf(markerId));
        if (marker.equals(null)) {
            this.form.dispatchErrorOccurredEvent(this, "getMarkerIfExisted", ErrorMessages.ERROR_GOOGLE_MAP_MARKER_NOT_EXIST, Integer.toString(markerId));
        }
        return marker;
    }

    @SimpleFunction(description = "Remove a marker from the map")
    public void RemoveMarker(int markerId) {
        Marker marker = getMarkerIfExisted(markerId);
        if (marker != null) {
            this.markers.remove(marker);
            marker.remove();
        }
    }

    public void onMarkerDrag(Marker marker) {
        Integer markerId = (Integer) this.markers.get(marker);
        if (markerId != null) {
            LatLng latlng = marker.getPosition();
            OnMarkerDrag(markerId.intValue(), latlng.latitude, latlng.longitude);
        }
        for (DraggableCircle dCircle : this.mCircles) {
            if (dCircle.getCenterMarker().equals(marker) || dCircle.getRadiusMarker().equals(marker)) {
                dCircle.onMarkerMoved(marker);
            }
        }
    }

    public void onMarkerDragEnd(Marker marker) {
        Integer markerId = (Integer) this.markers.get(marker);
        if (markerId != null) {
            LatLng latlng = marker.getPosition();
            OnMarkerDragEnd(markerId.intValue(), latlng.latitude, latlng.longitude);
        }
        for (DraggableCircle dCircle : this.mCircles) {
            if (dCircle.getCenterMarker().equals(marker) || dCircle.getRadiusMarker().equals(marker)) {
                dCircle.onMarkerMoved(marker);
                int uid = ((Integer) this.circles.get(dCircle)).intValue();
                LatLng center = dCircle.getCenterMarker().getPosition();
                FinishedDraggingCircle(uid, center.latitude, center.longitude, dCircle.getRadius().doubleValue());
            }
        }
    }

    public void onMarkerDragStart(Marker marker) {
        Integer markerId = (Integer) this.markers.get(marker);
        if (markerId != null) {
            LatLng latLng = marker.getPosition();
            OnMarkerDragStart(markerId.intValue(), latLng.latitude, latLng.longitude);
        }
        for (DraggableCircle dCircle : this.mCircles) {
            if (dCircle.getCenterMarker().equals(marker) || dCircle.getRadiusMarker().equals(marker)) {
                dCircle.onMarkerMoved(marker);
            }
        }
    }

    public void OnMarkerDragStart(int markerId, double latitude, double longitude) {
        final int i = markerId;
        final double d = latitude;
        final double d2 = longitude;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMarkerDragStart", Integer.valueOf(i), Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void OnMarkerDrag(int markerId, double latitude, double longitude) {
        final int i = markerId;
        final double d = latitude;
        final double d2 = longitude;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMarkerDrag", Integer.valueOf(i), Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void OnMarkerDragEnd(int markerId, double latitude, double longitude) {
        final int i = markerId;
        final double d = latitude;
        final double d2 = longitude;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMarkerDragEnd", Integer.valueOf(i), Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    @SimpleEvent(description = "When a marker is clicked")
    public void OnMarkerClick(int markerId, double latitude, double longitude) {
        final int i = markerId;
        final double d = latitude;
        final double d2 = longitude;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMarkerClick", Integer.valueOf(i), Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void InfoWindowClicked(final int markerId) {
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "InfoWindowClicked", Integer.valueOf(markerId));
            }
        });
    }

    public void onInfoWindowClick(Marker marker) {
        InfoWindowClicked(((Integer) this.markers.get(marker)).intValue());
    }

    public boolean onMarkerClick(Marker marker) {
        Integer markerId = (Integer) this.markers.get(marker);
        LatLng latLng = marker.getPosition();
        OnMarkerClick(markerId.intValue(), latLng.latitude, latLng.longitude);
        return false;
    }

    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void onCameraChange(CameraPosition position) {
        CameraPositionChanged(Double.valueOf(position.target.latitude).doubleValue(), Double.valueOf(position.target.longitude).doubleValue(), Float.valueOf(position.bearing).floatValue(), Float.valueOf(position.tilt).floatValue(), Float.valueOf(position.zoom).floatValue());
    }

    public void CameraPositionChanged(double lat, double lng, float bearing, float tilt, float zoom) {
        final double d = lat;
        final double d2 = lng;
        final float f = bearing;
        final float f2 = tilt;
        final float f3 = zoom;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "CameraPositionChanged", Double.valueOf(d), Double.valueOf(d2), Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3));
            }
        });
    }

    public void onMapLongClick(LatLng latLng) {
        OnMapLongClick(latLng.latitude, latLng.longitude);
    }

    @SimpleEvent(description = "Called when the user makes a long-press gesture on the map")
    public void OnMapLongClick(double lat, double lng) {
        final double d = lat;
        final double d2 = lng;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMapLongClick", Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void onMapClick(LatLng latLng) {
        OnMapClick(latLng.latitude, latLng.longitude);
    }

    @SimpleEvent(description = "Called when the user makes a tap gesture on the map")
    public void OnMapClick(double lat, double lng) {
        final double d = lat;
        final double d2 = lng;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnMapClick", Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void BoundCamera(double neLat, double neLng, double swLat, double swLng) {
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(neLat, neLng), new LatLng(swLat, swLng)), 0));
    }

    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        return new LatLng(center.latitude, center.longitude + (Math.toDegrees(radius / 6371009.0d) / Math.cos(Math.toRadians(center.latitude))));
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return (double) result[0];
    }

    public void onConnectionFailed(ConnectionResult arg0) {
    }

    public void onConnected(Bundle arg0) {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, REQUEST, this);
    }

    public void onPause() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.disconnect();
        }
    }

    public void onLocationChanged(Location location) {
        OnLocationChanged(location.getLatitude(), location.getLongitude());
    }

    @SimpleEvent(description = "Triggers this event when user location has changed. Only works when EnableMylocation is set to true")
    public void OnLocationChanged(double lat, double lng) {
        final double d = lat;
        final double d2 = lng;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(GoogleMap.this, "OnLocationChanged", Double.valueOf(d), Double.valueOf(d2));
            }
        });
    }

    public void onConnectionSuspended(int arg0) {
    }

    @SimpleFunction(description = "Move the map's camera to the specified location (latitude and longitude).")
    public void moveMapToLocationOnly(double lat, double lng) {
        if (this.mMap != null) {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        } else {
            this.initialLocation = new LatLng(lat, lng);
        }
    }

    @SimpleFunction(description = "Move the map's camera to the specified location (latitude and longitude) and zoom level.")
    public void moveMapToLocation(double lat, double lng, float zoomLevel) {
        if (this.mMap != null) {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoomLevel));
            return;
        }
        this.initialLocation = new LatLng(lat, lng);
        this.initialCameraZoomLevel = Float.valueOf(zoomLevel);
    }

    @SimpleProperty(description = "Gets the current zoom level of the map's camera.")
    public float CameraZoomLevel() {
        if (this.mMap != null) {
            return this.mMap.getCameraPosition().zoom;
        }
        return Float.NaN;
    }

    @DesignerProperty(defaultValue = "15", editorType = "non_negative_float")
    @SimpleProperty(description = "Move the map's camera to the specified zoom level.")
    public void CameraZoomLevel(float cameraZoomLevel) {
        if (this.mMap != null) {
            this.mMap.moveCamera(CameraUpdateFactory.zoomTo(cameraZoomLevel));
        } else {
            this.initialCameraZoomLevel = Float.valueOf(cameraZoomLevel);
        }
    }

    @SimpleProperty(description = "Gets the current tilt, the angle (in degrees) from the nadir (directly facing the Earth), of the map's camera.")
    public float CameraTiltDegrees() {
        if (this.mMap != null) {
            return this.mMap.getCameraPosition().tilt;
        }
        return Float.NaN;
    }

    @DesignerProperty(defaultValue = "0", editorType = "non_negative_float")
    @SimpleProperty(description = "Move the map's camera to the specified tilt, the angle (in degrees) from the nadir (directly facing the Earth).")
    public void CameraTiltDegrees(float cameraTiltDegrees) {
        if (this.mMap != null) {
            this.mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(this.mMap.getCameraPosition()).tilt(cameraTiltDegrees).build()));
        } else {
            this.initialCameraTiltDegrees = Float.valueOf(cameraTiltDegrees);
        }
    }

    @SimpleProperty(description = "Gets the current bearing, the direction that the camera is pointing in (in degrees clockwise from north), of the map's camera.")
    public float CameraBearingDegrees() {
        if (this.mMap != null) {
            return this.mMap.getCameraPosition().bearing;
        }
        return Float.NaN;
    }

    @DesignerProperty(defaultValue = "0", editorType = "non_negative_float")
    @SimpleProperty(description = "Move the map's camera to the specified bearing, the direction that the camera is pointing in (in degrees clockwise from north).")
    public void CameraBearingDegrees(float cameraBearingDegrees) {
        if (this.mMap != null) {
            this.mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(this.mMap.getCameraPosition()).bearing(cameraBearingDegrees).build()));
        } else {
            this.initialCameraBearingDegrees = Float.valueOf(cameraBearingDegrees);
        }
    }

    @SimpleProperty(description = "Gets the theme of the map. The choices are \"standard\"(default), \"silver\", \"retro\", \"dark\", \"night\", and \"aubergine\". Returns \"custom\" if the style of the map has been set from json.")
    public String Theme() {
        return this.theme;
    }

    @DesignerProperty(defaultValue = "standard", editorType = "google_map_theme")
    @SimpleProperty(description = "Sets the theme of the map. The choices are \"standard\"(default), \"silver\", \"retro\", \"dark\", \"night\", and \"aubergine\".")
    public void Theme(String theme) {
        if (this.mMap == null) {
            this.initialTheme = theme;
        } else if (!GoogleMapStyleOptions.JSON_BY_THEME.containsKey(theme)) {
            this.form.dispatchErrorOccurredEvent(this, "SetTheme", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_THEME, theme);
        } else if (this.mMap.setMapStyle(new MapStyleOptions((String) GoogleMapStyleOptions.JSON_BY_THEME.get(theme)))) {
            this.theme = theme;
        } else {
            this.form.dispatchErrorOccurredEvent(this, "SetStyle", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_STYLE_JSON, theme + "(theme)");
        }
    }

    @SimpleProperty(description = "Sets the style of the map from json. Create a custom map style at mapstyle.withgoogle.com. Set the theme to \"standard\" to clear the style json.")
    public void Style(String styleJson) {
        if (this.mMap == null) {
            this.initialStyleJson = styleJson;
        } else if (this.mMap.setMapStyle(new MapStyleOptions(styleJson))) {
            this.theme = GoogleMapStyleOptions.THEME_CUSTOM;
        } else {
            this.form.dispatchErrorOccurredEvent(this, "SetStyle", ErrorMessages.ERROR_GOOGLE_MAP_INVALID_STYLE_JSON, styleJson);
        }
    }
}
