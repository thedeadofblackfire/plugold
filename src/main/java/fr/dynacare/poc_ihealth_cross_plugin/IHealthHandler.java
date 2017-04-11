package fr.dynacare.poc_ihealth_cross_plugin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import fr.dynacare.poc_ihealth_cross_plugin.eventBus.ConnectedData;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.EventStatus;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEvent;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEventType;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.ScanData;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */

public class IHealthHandler {
    private static IHealthHandler ourInstance = new IHealthHandler();

    public static IHealthHandler getInstance() {
        return ourInstance;
    }

    private IHealthHandler() {
    }

    //iHEALTH ACCOUNT
    final private String userName = "liu01234345555@jiuan.com";
    final private String clientId = "2a8387e3f4e94407a3a767a72dfd52ea";
    final private String clientSecret = "fd5e845c47944a818bc511fb7edb0a77";
	/*
	 final private String userName = "dynamoove@gmail.com";
    final private String clientId = "16493b954f53496ab970311e4a61eb9c";
    final private String clientSecret = "3300e2960cef4315b900f237b8fcaff6";
	*/

    private int callbackId;

    private static final int HANDLER_SCAN = 101;
    private static final int HANDLER_CONNECTED = 102;
    private static final int HANDLER_DISCONNECT = 103;
    private static final int HANDLER_CONNECTIONFAILED = 104;
    private static final int HANDLER_USER_STATUE = 105;
    private static final int HANDLER_DISCOVERY_FINISHED = 106;

    private com.ihealth.communication.manager.iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {
        @Override
        public void onScanDevice(String mac, String deviceType, int rssi) {
            Log.i("INFO", "[iHealthDevicesCallback -> onScanDevice] Scanning a device -> " + mac);
            Bundle bundle = new Bundle();
            bundle.putString("mac", mac);
            bundle.putString("type", deviceType);
            Message msg = new Message();
            msg.what = HANDLER_SCAN;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status, int errorID) {
            Bundle bundle = new Bundle();
            bundle.putString("mac", mac);
            bundle.putString("type", deviceType);
            Message msg = new Message();
            if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTED) {
                msg.what = HANDLER_CONNECTED;
                Log.i("INFO", "[iHealthDevicesCallback -> onDeviceConnectionStateChange] DEVICE_STATE_CONNECTED");
            } else if (status == iHealthDevicesManager.DEVICE_STATE_DISCONNECTED) {
                msg.what = HANDLER_DISCONNECT;
                Log.i("INFO", "[iHealthDevicesCallback -> onDeviceConnectionStateChange] DEVICE_STATE_DISCONNECTED");
            } else if (status == iHealthDevicesManager.DEVICE_STATE_CONNECTIONFAIL){
                msg.what = HANDLER_CONNECTIONFAILED;
                Log.i("INFO", "[iHealthDevicesCallback -> onDeviceConnectionStateChange] DEVICE_STATE_CONNECTIONFAIL");
            }
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onUserStatus(String username, int userStatus) {
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("userstatus", userStatus + "");
            Message msg = new Message();
            msg.what = HANDLER_USER_STATUE;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        @Override
        public void onScanFinish() {
            Log.i("INFO", "[iHealthDevicesCallback -> onScanFinish] The scan is finished");
            Message msg = new Message();
            msg.what = HANDLER_DISCOVERY_FINISHED;
            myHandler.sendMessage(msg);
        }
    };

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SCAN:
                    Bundle bundle_scan = msg.getData();
                    String mac_scan = bundle_scan.getString("mac");
                    String type_scan = bundle_scan.getString("type");
                    if (mac_scan == null || type_scan == null) {
                        return;
                    }
                    Log.i("INFO", "[myHandler -> HANDLER_SCAN] Scanning a device -> " + mac_scan);
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DEVICE_SCANNED, EventStatus.SUCCESS, new ScanData(type_scan,  mac_scan)));
                    break;
                case HANDLER_CONNECTED:
                    Bundle bundle_connect = msg.getData();
                    String mac_connect = bundle_connect.getString("mac");
                    String type_connect = bundle_connect.getString("type");
                    Log.i("INFO", "[myHandler -> HANDLER_CONNECTED] The device is connected => " + mac_connect);
                    HashMap<String, String> hm_connect = new HashMap<String, String>();
                    hm_connect.put("mac", mac_connect);
                    hm_connect.put("type", type_connect);
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DEVICE_CONNECTED, EventStatus.SUCCESS, new ConnectedData(type_connect, mac_connect, type_connect)));
                    break;
                case HANDLER_DISCONNECT:
                    Bundle bundle_disconnect = msg.getData();
                    String mac_disconnect = bundle_disconnect.getString("mac");
                    String type_disconnect = bundle_disconnect.getString("type");
                    Log.i("INFO", "[myHandler -> HANDLER_DISCONNECT] The device is disconnected => " + mac_disconnect);
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DEVICE_DISCONNECTED, EventStatus.SUCCESS, new ConnectedData(type_disconnect, mac_disconnect, type_disconnect)));
                    break;
                case HANDLER_CONNECTIONFAILED:
                    Bundle bundle_connection_failed = msg.getData();
                    String mac_connection_failed = bundle_connection_failed.getString("mac");
                    String type_connection_failed = bundle_connection_failed.getString("type");
                    Log.i("INFO", "[myHandler -> HANDLER_DISCONNECT] The device connection has failed => " + mac_connection_failed);
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DEVICE_CONNECTION_FAILED, EventStatus.SUCCESS, new ConnectedData(type_connection_failed, mac_connection_failed, type_connection_failed)));
                    break;
                case HANDLER_USER_STATUE:
                    Bundle bundle_status = msg.getData();
                    String username = bundle_status.getString("username");
                    String userstatus = bundle_status.getString("userstatus");
                    break;
                case HANDLER_DISCOVERY_FINISHED:
                    Log.i("INFO", "[myHandler -> HANDLER_DISCOVERY_FINISHED] The scan is finished");
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DISCOVERY_FINISHED, EventStatus.SUCCESS, null));
                    break;
                default:
                    break;
            }
        }
    };

    /*
    *
    * Common actions !
    * The actions below do not depend on a device type
    *
    * */

    public void init(Context context){
        Log.i("INFO", "[IHealthHandler] init function");
        try {
            iHealthDevicesManager.getInstance().init(context);
            callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
            iHealthDevicesManager.getInstance().sdkUserInAuthor(context, userName, clientId, clientSecret, callbackId);
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.INIT, EventStatus.SUCCESS, null));
        } catch (Exception e){
            e.printStackTrace();
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.INIT, EventStatus.ERROR, null, ""));
        }
    }

    public void reInit(Context context){
        iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
        iHealthDevicesManager.getInstance().destroy();
        init(context);
    }

    public void startDiscovery(long type) {
        Log.i("INFO", "[IHealthHandler] startDiscovery function");
        try{
            iHealthDevicesManager.getInstance().startDiscovery(type);
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DISCOVERY_STARTED, EventStatus.SUCCESS, null));
        }catch (Exception e){
            e.printStackTrace();
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DISCOVERY_STARTED, EventStatus.ERROR, null, ""));
        }
    }

    public void stopDiscovery() {
        Log.i("INFO", "[IHealthHandler] stopDiscovery function");
        try{
            iHealthDevicesManager.getInstance().stopDiscovery();
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DISCOVERY_STOPPED, EventStatus.SUCCESS, null));
        }catch (Exception e){
            e.printStackTrace();
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DISCOVERY_STOPPED, EventStatus.ERROR, null, ""));
        }
    }

    public void connectDevice(String address){
        Log.i("INFO", "[IHealthHandler] connectDevice function: Address => " + address);
        boolean ret = iHealthDevicesManager.getInstance().connectDevice(userName, address);
        if (!ret){
            EventBus.getDefault().post(new IHealthEvent(IHealthEventType.DEVICE_CONNECTED, EventStatus.ERROR, null, ""));
        }
    }

    public void disconnectSdk(){
        Log.i("INFO", "[IHealthHandler] disconnectSdk function");
        try {
            iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
            iHealthDevicesManager.getInstance().destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}