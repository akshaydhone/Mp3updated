package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.runtime.ReplForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.DataChannel.Buffer;
import org.webrtc.DataChannel.Init;
import org.webrtc.DataChannel.Observer;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnection.IceConnectionState;
import org.webrtc.PeerConnection.IceGatheringState;
import org.webrtc.PeerConnection.IceServer;
import org.webrtc.PeerConnection.SignalingState;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PeerConnectionFactory.Options;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SessionDescription.Type;

public class WebRTCNativeMgr {
    private static final String LOG_TAG = "AppInvWebRTC";
    private static final CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
    private volatile ExecutorService background = Executors.newSingleThreadExecutor();
    private DataChannel dataChannel = null;
    Observer dataObserver = new C05033();
    private boolean first = true;
    private ReplForm form;
    private boolean haveOffer = false;
    private volatile boolean keepPolling = true;
    PeerConnection.Observer observer = new C05022();
    private PeerConnection peerConnection;
    private String rCode;
    private Random random = new Random();
    private String rendezvousServer = "rendezvous.appinventor.mit.edu";
    SdpObserver sdpObserver = new C05011();
    private TreeSet<String> seenNonces = new TreeSet();
    Timer timer = new Timer();

    /* renamed from: com.google.appinventor.components.runtime.util.WebRTCNativeMgr$1 */
    class C05011 implements SdpObserver {
        C05011() {
        }

        public void onCreateFailure(String str) {
        }

        public void onCreateSuccess(SessionDescription sessionDescription) {
            try {
                Log.d(WebRTCNativeMgr.LOG_TAG, "sdp.type = " + sessionDescription.type.canonicalForm());
                Log.d(WebRTCNativeMgr.LOG_TAG, "sdp.description = " + sessionDescription.description);
                Init init = new Init();
                if (sessionDescription.type == Type.OFFER) {
                    WebRTCNativeMgr.this.peerConnection.setRemoteDescription(WebRTCNativeMgr.this.sdpObserver, sessionDescription);
                } else if (sessionDescription.type == Type.ANSWER) {
                    WebRTCNativeMgr.this.peerConnection.setLocalDescription(WebRTCNativeMgr.this.sdpObserver, sessionDescription);
                    JSONObject offer = new JSONObject();
                    offer.put("type", "answer");
                    offer.put("sdp", sessionDescription.description);
                    JSONObject response = new JSONObject();
                    response.put("offer", offer);
                    WebRTCNativeMgr.this.sendRendezvous(response);
                }
            } catch (Exception e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Exception during onCreateSuccess", e);
            }
        }

        public void onSetFailure(String str) {
        }

        public void onSetSuccess() {
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.util.WebRTCNativeMgr$2 */
    class C05022 implements PeerConnection.Observer {
        C05022() {
        }

        public void onAddStream(MediaStream mediaStream) {
        }

        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreamArr) {
        }

        public void onDataChannel(DataChannel dataChannel) {
            Log.d(WebRTCNativeMgr.LOG_TAG, "Have Data Channel!");
            Log.d(WebRTCNativeMgr.LOG_TAG, "v5");
            WebRTCNativeMgr.this.dataChannel = dataChannel;
            dataChannel.registerObserver(WebRTCNativeMgr.this.dataObserver);
            WebRTCNativeMgr.this.keepPolling = false;
            WebRTCNativeMgr.this.seenNonces.clear();
        }

        public void onIceCandidate(IceCandidate iceCandidate) {
            try {
                Log.d(WebRTCNativeMgr.LOG_TAG, "IceCandidate = " + iceCandidate.toString());
                JSONObject response = new JSONObject();
                response.put("nonce", WebRTCNativeMgr.this.random.nextInt() % 100000);
                JSONObject jsonCandidate = new JSONObject();
                jsonCandidate.put("candidate", iceCandidate.sdp);
                jsonCandidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
                jsonCandidate.put("sdpMid", iceCandidate.sdpMid);
                response.put("candidate", jsonCandidate);
                WebRTCNativeMgr.this.sendRendezvous(response);
            } catch (Exception e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Exception during onIceCandidate", e);
            }
        }

        public void onIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
        }

        public void onIceConnectionChange(IceConnectionState iceConnectionState) {
        }

        public void onIceConnectionReceivingChange(boolean z) {
        }

        public void onIceGatheringChange(IceGatheringState iceGatheringState) {
        }

        public void onRemoveStream(MediaStream mediaStream) {
        }

        public void onRenegotiationNeeded() {
        }

        public void onSignalingChange(SignalingState signalingState) {
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.util.WebRTCNativeMgr$3 */
    class C05033 implements Observer {
        C05033() {
        }

        public void onBufferedAmountChange(long j) {
        }

        public void onMessage(Buffer buffer) {
            try {
                String input = WebRTCNativeMgr.utf8Decoder.decode(buffer.data).toString();
                Log.d(WebRTCNativeMgr.LOG_TAG, "onMessage: received: " + input);
                WebRTCNativeMgr.this.form.evalScheme(input);
            } catch (CharacterCodingException e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "onMessage decoder error", e);
            }
        }

        public void onStateChange() {
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.util.WebRTCNativeMgr$4 */
    class C05054 implements Runnable {

        /* renamed from: com.google.appinventor.components.runtime.util.WebRTCNativeMgr$4$1 */
        class C05041 extends TimerTask {
            C05041() {
            }

            public void run() {
                WebRTCNativeMgr.this.Poller();
            }
        }

        C05054() {
        }

        public void run() {
            try {
                HttpResponse response = new DefaultHttpClient().execute(new HttpGet("http://" + WebRTCNativeMgr.this.rendezvousServer + "/rendezvous2/" + WebRTCNativeMgr.this.rCode + "-s"));
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String str = "";
                while (true) {
                    str = rd.readLine();
                    if (str == null) {
                        break;
                    }
                    sb.append(str);
                }
                if (WebRTCNativeMgr.this.keepPolling) {
                    Log.d(WebRTCNativeMgr.LOG_TAG, "response = " + sb.toString());
                    JSONArray jsonArray = new JSONArray(sb.toString());
                    Log.d(WebRTCNativeMgr.LOG_TAG, "jsonArray.length() = " + jsonArray.length());
                    int i = 0;
                    while (i < jsonArray.length()) {
                        Log.d(WebRTCNativeMgr.LOG_TAG, "i = " + i);
                        Log.d(WebRTCNativeMgr.LOG_TAG, "element = " + jsonArray.optString(i));
                        JSONObject element = (JSONObject) jsonArray.get(i);
                        if (WebRTCNativeMgr.this.haveOffer) {
                            if (element.has("nonce")) {
                                String nonce = element.optString("nonce");
                                if (element.isNull("candidate")) {
                                    Log.d(WebRTCNativeMgr.LOG_TAG, "Received a null candidate, skipping...");
                                    i++;
                                } else {
                                    JSONObject candidate = (JSONObject) element.get("candidate");
                                    String sdpcandidate = candidate.optString("candidate");
                                    String sdpMid = candidate.optString("sdpMid");
                                    int sdpMLineIndex = candidate.optInt("sdpMLineIndex");
                                    Log.d(WebRTCNativeMgr.LOG_TAG, "candidate = " + sdpcandidate);
                                    if (!WebRTCNativeMgr.this.seenNonces.contains(nonce)) {
                                        WebRTCNativeMgr.this.seenNonces.add(nonce);
                                        Log.d(WebRTCNativeMgr.LOG_TAG, "new nonce, about to add candidate!");
                                        WebRTCNativeMgr.this.peerConnection.addIceCandidate(new IceCandidate(sdpMid, sdpMLineIndex, sdpcandidate));
                                        Log.d(WebRTCNativeMgr.LOG_TAG, "addIceCandidate returned");
                                    }
                                }
                            }
                        } else if (element.has("offer")) {
                            JSONObject offer = (JSONObject) element.get("offer");
                            String sdp = offer.optString("sdp");
                            String type = offer.optString("type");
                            Log.d(WebRTCNativeMgr.LOG_TAG, "sdb = " + sdp);
                            Log.d(WebRTCNativeMgr.LOG_TAG, "type = " + type);
                            WebRTCNativeMgr.this.haveOffer = true;
                            Log.d(WebRTCNativeMgr.LOG_TAG, "About to set remote offer");
                            WebRTCNativeMgr.this.peerConnection.setRemoteDescription(WebRTCNativeMgr.this.sdpObserver, new SessionDescription(Type.OFFER, sdp));
                            WebRTCNativeMgr.this.peerConnection.createAnswer(WebRTCNativeMgr.this.sdpObserver, new MediaConstraints());
                            Log.d(WebRTCNativeMgr.LOG_TAG, "createAnswer returned");
                            i = -1;
                        } else {
                            i++;
                        }
                        i++;
                    }
                    Log.d(WebRTCNativeMgr.LOG_TAG, "exited loop");
                    if (WebRTCNativeMgr.this.keepPolling) {
                        WebRTCNativeMgr.this.timer.schedule(new C05041(), 1000);
                        return;
                    }
                    return;
                }
                Log.d(WebRTCNativeMgr.LOG_TAG, "keepPolling is false, we're done!");
            } catch (IOException e) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Caught IOException: " + e.toString(), e);
            } catch (JSONException e2) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Caught JSONException: " + e2.toString(), e2);
            } catch (Exception e3) {
                Log.e(WebRTCNativeMgr.LOG_TAG, "Caught Exception: " + e3.toString(), e3);
            }
        }
    }

    public WebRTCNativeMgr(String rendezvousServer) {
        this.rendezvousServer = rendezvousServer;
    }

    public void initiate(ReplForm form, Context context, String code) {
        this.form = form;
        this.rCode = code;
        PeerConnectionFactory.initializeAndroidGlobals(context, false);
        this.peerConnection = new PeerConnectionFactory(new Options()).createPeerConnection(Collections.singletonList(IceServer.builder("turn:turn.appinventor.mit.edu:3478").setUsername("oh").setPassword("boy").createIceServer()), new MediaConstraints(), this.observer);
        Poller();
    }

    private void Poller() {
        this.background.submit(new C05054());
    }

    private void sendRendezvous(JSONObject data) {
        try {
            data.put("first", this.first);
            data.put("webrtc", true);
            data.put("key", this.rCode + "-r");
            if (this.first) {
                this.first = false;
                data.put("apiversion", SdkLevel.getLevel());
            }
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://" + this.rendezvousServer + "/rendezvous2/");
            try {
                Log.d(LOG_TAG, "About to send = " + data.toString());
                post.setEntity(new StringEntity(data.toString()));
                client.execute(post);
            } catch (IOException e) {
                Log.d(LOG_TAG, "sendRedezvous IOException = " + e.toString());
            }
        } catch (Exception e2) {
            Log.e(LOG_TAG, "Exception in sendRendezvous", e2);
        }
    }

    public void send(String output) {
        try {
            if (this.dataChannel == null) {
                Log.w(LOG_TAG, "No Data Channel in Send");
                return;
            }
            this.dataChannel.send(new Buffer(ByteBuffer.wrap(output.getBytes("UTF-8")), false));
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "While encoding data to send to companion", e);
        }
    }
}
