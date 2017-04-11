package fr.dynacare.poc_ihealth_cross_plugin.devices;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ihealth.communication.control.Am4Control;
import com.ihealth.communication.control.AmProfile;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import fr.dynacare.poc_ihealth_cross_plugin.eventBus.Data;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.EventStatus;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEvent;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEventType;

public class IHealthAM4 {
    private static IHealthAM4 ourInstance = new IHealthAM4();

    public static IHealthAM4 getInstance() {
        return ourInstance;
    }


    private AMControlList mAmControls = new AMControlList();

    private int mCallbackId;

    private IHealthAM4() {
        mCallbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(mCallbackId, iHealthDevicesManager.TYPE_AM4);
    }

    private com.ihealth.communication.manager.iHealthDevicesCallback iHealthDevicesCallback = new com.ihealth.communication.manager.iHealthDevicesCallback() {
        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            Log.i("INFO", "Action: " + action);
            if (message == null) {
                //Throw an error
                Log.i("INFO", "[IHealthAM4] iHealthDevicesCallback, Message is null");
            }
            if (action.equals(AmProfile.ACTION_SYNC_SLEEP_DATA_AM)) {
                try {
                    Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the sleep callback");
                    Log.i("INFO", "[IHeathAM4] " + message);
                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    ad.mRawData = message;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SLEEP_DATA, EventStatus.SUCCESS, ad));

                    JSONObject info = new JSONObject(message);
                    String stage_info = info.getString(AmProfile.SYNC_SLEEP_DATA_AM);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "Sleep Data: " + stage_info;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SLEEP_DATA, EventStatus.ERROR, null, e.getMessage()));
                }
            } else if (action.equals(AmProfile.ACTION_SYNC_ACTIVITY_DATA_AM)) {
                try {
                    Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the activity callback");
                    Log.i("INFO", "[IHeathAM4] " + message);

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    ad.mRawData = message;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.ACTIVITY_DATA, EventStatus.SUCCESS, ad));

                    JSONObject info = new JSONObject(message);
                    String activity_info = info.getString(AmProfile.SYNC_ACTIVITY_DATA_AM);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "Activity Data: " + activity_info;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.ACTIVITY_DATA, EventStatus.ERROR, null, e.getMessage()));
                }
            } else if (action.equals(AmProfile.ACTION_SET_USERID_SUCCESS_AM)) {
                try {
                    Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the set user id callback");

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_USER_ID, EventStatus.SUCCESS, ad));

                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "Set ID success";
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AmProfile.ACTION_CLOUD_BINDING_AM_SUCCESS)){
                try {
                    Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the cloud binding AM success callback");

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_USER_ID, EventStatus.SUCCESS, ad));

                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "Set ID success (cloud binding)";
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AmProfile.ACTION_USERID_AM)) {
                try {
                    Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the get user id callback");
                    Log.i("INFO", "[IHeathAM4] " + message);

                    JSONObject info = new JSONObject(message);
                    String id = info.getString(AmProfile.USERID_AM);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "User ID: " + id;
                    myHandler.sendMessage(msg);

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    ad.mRawData = id;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.GET_USER_ID, EventStatus.SUCCESS, ad));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AmProfile.ACTION_SET_USERINFO_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the set user info callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set User Info success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM4";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_USER_INFO, EventStatus.SUCCESS, ad));
            } else if (action.equals(AmProfile.ACTION_SYNC_TIME_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the syncTime callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set Real Time success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM4";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SYNC_REAL_TIME, EventStatus.SUCCESS, ad));
            } else if (action.equals(AmProfile.ACTION_SET_HOUR_MODE_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM4] iHealthDevicesCallback, I'm in the setTimeMode callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set Time Mode success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM4";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_TIME_MODE, EventStatus.SUCCESS, ad));
            } else if (action.equals(AmProfile.ACTION_GET_RANDOM_AM)){
                try {
                    JSONObject info = new JSONObject(message);
                    String random =info.getString(AmProfile.GET_RANDOM_AM);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "Random: " + random;

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM4";
                    ad.mRawData = random;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SEND_RANDOM_NUMBER, EventStatus.SUCCESS, ad));
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private static final int HANDLER_MESSAGE = 101;
    Handler myHandler = new Handler( ){
        public void handleMessage(Message msg){
            if (msg.what == HANDLER_MESSAGE){
                //DO STUFF
            }
            super.handleMessage(msg);
        }
    };

    private boolean isActionAvailable(String action){
        if (action.equals(null)) return false;
        if (action.equals("getActivityData")){
            return true;
        } else if (action.equals("getSleepData")){
            return true;
        } else if (action.equals("disconnect")){
            return true;
        } else if (action.equals("setUserId")){
            return true;
        } else if (action.equals("getUserId")){
            return true;
        } else if (action.equals("setUserInfo")){
            return true;
        } else if (action.equals("syncTime")){
            return true;
        } else if (action.equals("setTimeMode")){
            return true;
        } else if (action.equals("sendRandomNumber")){
            return true;
        }
        return false;
    }

    public void buildAmControl(String address){
        Am4Control am4Control = iHealthDevicesManager.getInstance().getAm4Control(address);
        if (am4Control != null){
            mAmControls.add(new AMControl<Am4Control>(am4Control, address));
        }
    }

    public void destroyAmControl(String address){
        mAmControls.removeAmControlByAddress(address);
    }

    public void execAction(String address, String action, Parameters params){
        if (!isActionAvailable(action)){
            Log.i("INFO", "Could not find action");
            return;
        }
        AMControl<Am4Control> amControl = mAmControls.getAMControlByAddress(address);
        if (amControl == null || amControl.getmAmControl() == null){
            Log.i("INFO", "[IHeathAM4] It was null, should not be ...");
            return;
        }
        Log.i("INFO", "[IHeathAM4] Got it motherfucker, we have the AmControl !");
        Am4Control am4Control = amControl.getmAmControl();
        if (action.equals("getActivityData")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute syncActivityData with ID");
            try {
                am4Control.syncActivityData();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (action.equals("getSleepData")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute syncSleepData");
            try {
                am4Control.syncSleepData();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (action.equals("disconnect")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute disconnect");
            try {
                Log.i("INFO", "Address we disconnect: " + amControl.getmAddress());
                am4Control.disconnect();
            }catch (Exception e){
                Log.i("INFO", "[IHeathAM4] Error trying to disconnect the Am4Control");
                e.printStackTrace();
            }
            try {
                am4Control.destroy();
            }catch (Exception e){
                Log.i("INFO", "[IHeathAM4] Error trying to destroy the Am4Control");
                e.printStackTrace();
            }
        } else if (action.equals("setUserId")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute setUserId");
            int userId = params.mUserId;
            am4Control.setUserId(userId);
        } else if (action.equals("getUserId")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute getUserId");
            am4Control.getUserId();
        } else if (action.equals("setUserInfo")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute setUserInfo");
            am4Control.setUserInfo(params.mUserInfo.mAge, params.mUserInfo.mHeight, params.mUserInfo.mWeight, params.mUserInfo.mGender, params.mUserInfo.mUnit, params.mUserInfo.mTarget, params.mUserInfo.mActivityLevel,params.mUserInfo.mSwimTarget);
        } else if (action.equals("syncTime")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute syncTime");
            am4Control.syncRealTime();
        } else if (action.equals("setTimeMode")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute setTimeMode");
            am4Control.setHourMode(params.mAmParams.mTimeMode);
        } else if (action.equals("sendRandomNumber")){
            Log.i("INFO", "[IHeathAM4] execAction function, About to execute sendRandomNumber");
            am4Control.sendRandom();
        }
    }
}