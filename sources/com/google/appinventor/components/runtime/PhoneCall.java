package com.google.appinventor.components.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.PhoneCallUtil;

@DesignerComponent(category = ComponentCategory.SOCIAL, description = "<p>A non-visible component that makes a phone call to the number specified in the <code>PhoneNumber</code> property, which can be set either in the Designer or Blocks Editor. The component has a <code>MakePhoneCall</code> method, enabling the program to launch a phone call.</p><p>Often, this component is used with the <code>ContactPicker</code> component, which lets the user select a contact from the ones stored on the phone and sets the <code>PhoneNumber</code> property to the contact's phone number.</p><p>To directly specify the phone number (e.g., 650-555-1212), set the <code>PhoneNumber</code> property to a Text with the specified digits (e.g., \"6505551212\").  Dashes, dots, and parentheses may be included (e.g., \"(650)-555-1212\") but will be ignored; spaces may not be included.</p>", docUri = "social/phone-call", iconName = "images/phoneCall.png", nonVisible = true, version = 3)
@SimpleObject
@UsesPermissions({"android.permission.READ_PHONE_STATE"})
public class PhoneCall extends AndroidNonvisibleComponent implements Component, OnDestroyListener, ActivityResultListener {
    private static final String LOG_TAG = PhoneCall.class.getSimpleName();
    private static final int PHONECALL_REQUEST_CODE = 1346916174;
    private final CallStateReceiver callStateReceiver;
    private final Context context;
    private boolean didRegisterReceiver = false;
    private boolean havePermission = false;
    private String phoneNumber;

    private class CallStateReceiver extends BroadcastReceiver {
        private String number = "";
        private int status = 0;

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.PHONE_STATE".equals(action)) {
                String state = intent.getStringExtra("state");
                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    this.status = 1;
                    this.number = intent.getStringExtra("incoming_number");
                    PhoneCall.this.PhoneCallStarted(1, this.number);
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                    if (this.status == 1) {
                        this.status = 3;
                        PhoneCall.this.IncomingCallAnswered(this.number);
                    }
                } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                    if (this.status == 1) {
                        PhoneCall.this.PhoneCallEnded(1, this.number);
                    } else if (this.status == 3) {
                        PhoneCall.this.PhoneCallEnded(2, this.number);
                    } else if (this.status == 2) {
                        PhoneCall.this.PhoneCallEnded(3, this.number);
                    }
                    this.status = 0;
                    this.number = "";
                }
            } else if ("android.intent.action.NEW_OUTGOING_CALL".equals(action)) {
                this.status = 2;
                this.number = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
                PhoneCall.this.PhoneCallStarted(2, this.number);
            }
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.PhoneCall$1 */
    class C06251 implements PermissionResultHandler {
        C06251() {
        }

        public void HandlePermissionResponse(String permission, boolean granted) {
            if (granted) {
                PhoneCall.this.registerCallStateMonitor();
            } else {
                PhoneCall.this.form.dispatchPermissionDeniedEvent(PhoneCall.this, "Initialize", "android.permission.PROCESS_OUTGOING_CALLS");
            }
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.PhoneCall$2 */
    class C06262 implements PermissionResultHandler {
        C06262() {
        }

        public void HandlePermissionResponse(String permission, boolean granted) {
            if (granted) {
                PhoneCall.this.havePermission = true;
                PhoneCall.this.MakePhoneCall();
                return;
            }
            PhoneCall.this.form.dispatchPermissionDeniedEvent(PhoneCall.this, "MakePhoneCall", "android.permission.CALL_PHONE");
        }
    }

    public PhoneCall(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.form.registerForOnDestroy(this);
        this.form.registerForActivityResult(this, PHONECALL_REQUEST_CODE);
        PhoneNumber("");
        this.callStateReceiver = new CallStateReceiver();
    }

    public void Initialize() {
        if (this.form.doesAppDeclarePermission("android.permission.PROCESS_OUTGOING_CALLS")) {
            this.form.askPermission("android.permission.PROCESS_OUTGOING_CALLS", new C06251());
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String PhoneNumber() {
        return this.phoneNumber;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @SimpleFunction
    public void MakePhoneCall() {
        Intent i = new Intent("android.intent.action.DIAL", Uri.fromParts("tel", this.phoneNumber, null));
        if (i.resolveActivity(this.form.getPackageManager()) != null) {
            this.form.startActivityForResult(i, PHONECALL_REQUEST_CODE);
        }
    }

    @UsesPermissions({"android.permission.CALL_PHONE"})
    @SimpleFunction
    public void MakePhoneCallDirect() {
        if (this.havePermission) {
            PhoneCallUtil.makePhoneCall(this.context, this.phoneNumber);
        } else {
            this.form.askPermission("android.permission.CALL_PHONE", new C06262());
        }
    }

    @SimpleEvent(description = "Event indicating that a phonecall has started. If status is 1, incoming call is ringing; if status is 2, outgoing call is dialled. phoneNumber is the incoming/outgoing phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS"})
    public void PhoneCallStarted(int status, String phoneNumber) {
        EventDispatcher.dispatchEvent(this, "PhoneCallStarted", Integer.valueOf(status), phoneNumber);
    }

    @SimpleEvent(description = "Event indicating that a phone call has ended. If status is 1, incoming call is missed or rejected; if status is 2, incoming call is answered before hanging up; if status is 3, outgoing call is hung up. phoneNumber is the ended call phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS"})
    public void PhoneCallEnded(int status, String phoneNumber) {
        EventDispatcher.dispatchEvent(this, "PhoneCallEnded", Integer.valueOf(status), phoneNumber);
    }

    @SimpleEvent(description = "Event indicating that an incoming phone call is answered. phoneNumber is the incoming call phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS"})
    public void IncomingCallAnswered(String phoneNumber) {
        EventDispatcher.dispatchEvent(this, "IncomingCallAnswered", phoneNumber);
    }

    public void resultReturned(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHONECALL_REQUEST_CODE) {
            PhoneCallStarted(2, "");
        }
    }

    private void registerCallStateMonitor() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.context.registerReceiver(this.callStateReceiver, intentFilter);
        this.didRegisterReceiver = true;
    }

    private void unregisterCallStateMonitor() {
        if (this.didRegisterReceiver) {
            this.context.unregisterReceiver(this.callStateReceiver);
            this.didRegisterReceiver = false;
        }
    }

    public void onDestroy() {
        unregisterCallStateMonitor();
    }
}
