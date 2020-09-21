package com.ntson;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.RemoteException;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.ntson.model.Server;
import com.ntson.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

import static android.app.Activity.RESULT_OK;

public class VpnModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static ReactApplicationContext reactContext = null;
    private static final String EVENT_NAME = "VPN_EVENT";
    private static final String TAG = "VPN_EVENT";

    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    private Utils utils;
    private Server mServer;

    public VpnModule(ReactApplicationContext reactContext) {
        super(reactContext);
        VpnModule.reactContext = reactContext;
        initializeVpn();
    }

    @Override
    public String getName() {
        return "Vpn";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + " stringArgument: " + stringArgument);
    }

    /**
     * Emit event from native to JS
     *
     * @param status - Connect status
     * @param error  - Error message
     */
    private void emitEvent(String status, String error) {
        WritableMap params = Arguments.createMap();
        params.putString("status", status);
        if (error == null || error.isEmpty()) {
            params.putNull("error");
        } else {
            params.putString("error", error);
        }

        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(EVENT_NAME, params);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //Permission granted, start the VPN
            connectVpn();
        } else {
            emitEvent("", "Permission Deny");
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    /**
     * Init VPN
     * - Start receiver
     * - Init utils
     */
    private void initializeVpn() {
        utils = new Utils();
        VpnStatus.initLogCache(reactContext.getCacheDir());
        LocalBroadcastManager.getInstance(reactContext).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
        reactContext.addActivityEventListener(this);
    }

    /**
     * Read config from *ovpn file
     *
     * @param assetsLink - link file
     * @return - config
     */
    private String readConfigFromAssets(String assetsLink) {
        InputStream conf = null;
        try {
            conf = reactContext.getAssets().open(assetsLink);

            InputStreamReader isr = new InputStreamReader(conf);
            BufferedReader br = new BufferedReader(isr);
            String config = "";
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) break;
                config += line + "\n";
            }

            br.readLine();
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ReactMethod
    public void setupServer() {
        if (!OpenVPNService.getStatus().equals("CONNECTED")) {
            Intent intent = VpnService.prepare(reactContext);
            if (intent != null) {
                reactContext.startActivityForResult(intent, 1, null);
            } else {
                connectVpn();
            }
        } else {
            disconnectVpn();
        }
    }

    @ReactMethod
    public void connectVpn() {
        try {
            Server server = new Server("United States",
                    null,
                    "japan.ovpn",
                    "vpn",
                    "vpn"
            );
            String config = readConfigFromAssets(server.getOvpn());
            Log.d(TAG, config);
//            OpenVpnApi.startVpn(reactContext, mServer.getConfig(), mServer.getCountry(), mServer.getOvpnUserName(), mServer.getOvpnUserPassword());
            OpenVpnApi.startVpn(reactContext, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void disconnectVpn() {
        try {
            OpenVPNThread.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public Server getCurrentVpn(Callback callback) {
        return mServer;
    }

    @ReactMethod
    public String getVpnStatus() {
        return OpenVPNService.getStatus();
    }

    /**
     * Receive broadcast message
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("state");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
                Log.d("duration ", duration);
                Log.d("lastPacketReceive ", lastPacketReceive);
                Log.d("byteIn ", byteIn);
                Log.d("byteOut ", byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}
