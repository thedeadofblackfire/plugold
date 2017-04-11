package fr.dynacare.poc_ihealth_cross_plugin.devices;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ihealth.communication.control.Am3sControl;
import com.ihealth.communication.control.AmProfile;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import fr.dynacare.poc_ihealth_cross_plugin.eventBus.Data;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.EventStatus;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEvent;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEventType;

/**
 * Created by guillaumeboufflers on 07/07/2016.
 */

public class IHealthAM3S {
    private static IHealthAM3S ourInstance = new IHealthAM3S();

    public static IHealthAM3S getInstance() {
        return ourInstance;
    }

    private Am3sControl mAm3sControl;

    private int mCallbackId;

    private IHealthAM3S() {
        mCallbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(mCallbackId, iHealthDevicesManager.TYPE_AM3S);
    }

    private com.ihealth.communication.manager.iHealthDevicesCallback iHealthDevicesCallback = new com.ihealth.communication.manager.iHealthDevicesCallback() {
        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            if (message == null) {
                //Throw an error
                Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, Message is null");
            }
            if (action.equals(AmProfile.ACTION_SYNC_SLEEP_DATA_AM)) {
                try {
                    Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the sleep callback");
                    Log.i("INFO", "[IHeathAM3S] " + message);
                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM3S";
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
            } else if (action.equals(AmProfile.ACTION_SYNC_ACTIVITY_DATA_AM)){
                try {
                    Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the activity callback");
                    Log.i("INFO", "[IHeathAM3S] " + message);

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM3S";
                    ad.mRawData = message;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.ACTIVITY_DATA, EventStatus.SUCCESS, ad));

                    JSONObject info = new JSONObject(message);
                    String activity_info =info.getString(AmProfile.SYNC_ACTIVITY_DATA_AM);
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
                    Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the setUserId callback");

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM3S";
                    ad.mRawData = message;
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
                    Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the cloud binding AM success callback");

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM3S";
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
                    Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the getUserId callback");
                    Log.i("INFO", "[IHeathAM3S] " + message);

                    JSONObject info = new JSONObject(message);
                    String id = info.getString(AmProfile.USERID_AM);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "User ID: " + id;
                    myHandler.sendMessage(msg);

                    Data ad = new Data();
                    ad.mAddress = mac;
                    ad.mDeviceModel = "AM3S";
                    ad.mRawData = id;
                    EventBus.getDefault().post(new IHealthEvent(IHealthEventType.GET_USER_ID, EventStatus.SUCCESS, ad));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(AmProfile.ACTION_SET_USERINFO_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the set user info callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set User Info success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM3S";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_USER_INFO, EventStatus.SUCCESS, ad));
            }  else if (action.equals(AmProfile.ACTION_SYNC_TIME_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the syncTime callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set User Info success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM3S";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SYNC_REAL_TIME, EventStatus.SUCCESS, ad));
            } else if (action.equals(AmProfile.ACTION_SET_HOUR_MODE_SUCCESS_AM)){
                Log.i("INFO", "[IHeathAM3S] iHealthDevicesCallback, I'm in the setTimeMode callback");

                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "Set User Info success";
                myHandler.sendMessage(msg);

                Data ad = new Data();
                ad.mAddress = mac;
                ad.mDeviceModel = "AM3S";
                EventBus.getDefault().post(new IHealthEvent(IHealthEventType.SET_TIME_MODE, EventStatus.SUCCESS, ad));
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
        }
        return false;
    }

    public void execAction(String address, String action, Parameters params){
        if (!isActionAvailable(action)){
            Log.i("INFO", "Could not find action");
            return;
        }
        if (mAm3sControl == null){
            Log.i("INFO", "[IHeathAM3S] Initiating the AM4Control");
            mAm3sControl = iHealthDevicesManager.getInstance().getAm3sControl(address);
        } else{
            Log.i("INFO", "[IHeathAM3S] mAm3SControl is not null");
        }
        if (mAm3sControl == null){
            Log.i("INFO", "[IHeathAM3S] mAm3SControl is null");
            return;
        }
        if (action.equals("getActivityData")){
            Log.i("INFO", "[IHeathAM3S] execAction function, About to execute syncActivityData with ID");
            try {
                mAm3sControl.syncActivityData();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (action.equals("getSleepData")){
            Log.i("INFO", "[IHeathAM3S] execAction function, About to execute syncSleepData");
            try {
                mAm3sControl.syncSleepData();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (action.equals("disconnect")){
            Log.i("INFO", "[IHeathAM3S] execAction function, About to execute disconnect");
            try {
                mAm3sControl.disconnect();
            }catch (Exception e){
                Log.i("INFO", "[IHeathAM3S] Error trying to disconnect the Am4Control");
                e.printStackTrace();
            }
            try {
                mAm3sControl.destroy();
                mAm3sControl = null;
            }catch (Exception e){
                Log.i("INFO", "[IHeathAM3S] Error trying to destroy the Am4Control");
                e.printStackTrace();
            }
        } else if (action.equals("setUserId")){
            Log.i("INFO", "[IHeathAM3S] execAction function, About to execute setUserId");
            int userId = params.mUserId;
            mAm3sControl.setUserId(userId);
        } else if (action.equals("getUserId")){
            Log.i("INFO", "[IHeathAM3S] execAction function, About to execute getUserId");
            mAm3sControl.getUserId();
        } else if (action.equals("setUserInfo")){
            mAm3sControl.setUserInfo(params.mUserInfo.mAge, params.mUserInfo.mHeight, params.mUserInfo.mWeight, params.mUserInfo.mGender, params.mUserInfo.mUnit, params.mUserInfo.mTarget, params.mUserInfo.mActivityLevel);
        } else if (action.equals("syncTime")){
            mAm3sControl.syncRealTime();
        } else if (action.equals("setTimeMode")){
            mAm3sControl.setHourMode(params.mAmParams.mTimeMode);
        }
    }
}