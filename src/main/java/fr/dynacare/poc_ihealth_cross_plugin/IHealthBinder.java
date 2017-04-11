package fr.dynacare.poc_ihealth_cross_plugin;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.dynacare.poc_ihealth_cross_plugin.devices.AMParams;
import fr.dynacare.poc_ihealth_cross_plugin.devices.IHealthAM3S;
import fr.dynacare.poc_ihealth_cross_plugin.devices.IHealthAM4;
import fr.dynacare.poc_ihealth_cross_plugin.devices.Parameters;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.ConnectedData;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.Data;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.EventStatus;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEvent;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEventContract;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.IHealthEventType;
import fr.dynacare.poc_ihealth_cross_plugin.eventBus.ScanData;

/**
 * Created by guillaumeboufflers on 04/07/2016.
 */

public class IHealthBinder extends CordovaPlugin implements IHealthEventContract {
    // ACTIONS
    private final String INIT = "init";
    private final String START_DISCOVERY = "startDiscovery";
    private final String STOP_DISCOVERY = "stopDiscovery";
    private final String REGISTER_DISCOVERY_FINISHED = "registerOnDiscoveryFinished";
    private final String REGISTER_DEVICE_SCANNED = "registerOnScannedDevice";
    private final String REGISTER_DEVICE_DISCONNECTED = "registerOnDeviceDisconnected";
    private final String REGISTER_DEVICE_CONNECTION_FAILED = "registerOnDeviceConnectionFailed";
    private final String CONNECT = "connect";
    private final String DISCONNECT = "disconnect";
    private final String GET_ACTIVITY_DATA = "getActivityData";
    private final String GET_SLEEP_DATA = "getSleepData";
    private final String SET_USER_ID = "setUserId";
    private final String GET_USER_ID = "getUserId";
    private final String SET_USER_INFO = "setUserInfo";
    private final String SYNC_TIME = "syncTime";
    private final String SET_TIME_MODE = "setTimeMode";
    private final String SEND_RANDOM_NUMBER = "sendRandomNumber";

    // CALLBACK
    CallbackContext mInitCallback;
    CallbackContext mDiscoveryStartedCallback;
    CallbackContext mDiscoveryStoppedCallback;
    CallbackContext mDiscoveryFinishedCallback;
    CallbackContext mDeviceDiscoveredCallback;
    CallbackContext mDeviceConnectedCallback;
    CallbackContext mDeviceDisconnectedCallback;
    CallbackContext mDeviceConnectionFailedCallback;
    CallbackContext mGetActivityDataCallback;
    CallbackContext mGetSleepDataCallback;
    CallbackContext mGetUserIdCallback;
    CallbackContext mSetUserIdCallback;
    CallbackContext mSetUserInfoCallback;
    CallbackContext mSyncTimeCallback;
    CallbackContext mSetTimeModeCallback;
    CallbackContext mSendRandomNumber;

    // CONSTRUCTOR
    public IHealthBinder() {
        //EVENTBUS INIT
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) throws JSONException {
        Log.i("INFO", "[IHealthBinder] execute function, action => " + action);
        if (action.equals(INIT)) {
            mInitCallback = callback;
            init();
        } else if (action.equals(START_DISCOVERY)) {
            mDiscoveryStartedCallback = callback;
            startDiscovery(args);
        } else if (action.equals(STOP_DISCOVERY)) {
            mDiscoveryStoppedCallback = callback;
            stopDiscovery();
        } else if (action.equals(REGISTER_DEVICE_SCANNED)) {
            mDeviceDiscoveredCallback = callback;
        } else if (action.equals(REGISTER_DISCOVERY_FINISHED)) {
            mDiscoveryFinishedCallback = callback;
        } else if (action.equals(CONNECT)) {
            mDeviceConnectedCallback = callback;
            connectDevice(args);
        } else if (action.equals(DISCONNECT)) {
            disconnectDevice(args);
        } else if (action.equals(REGISTER_DEVICE_DISCONNECTED)){
            mDeviceDisconnectedCallback = callback;
        } else if (action.equals(GET_ACTIVITY_DATA)) {
            mGetActivityDataCallback = callback;
            getActivityData(args);
        } else if (action.equals(GET_SLEEP_DATA)) {
            mGetSleepDataCallback = callback;
            getSleepData(args);
        } else if (action.equals(REGISTER_DEVICE_CONNECTION_FAILED)) {
            mDeviceConnectionFailedCallback = callback;
        } else if (action.equals(GET_USER_ID)){
            mGetUserIdCallback = callback;
            getUserId(args);
        } else if (action.equals(SET_USER_ID)){
            mSetUserIdCallback = callback;
            setUserId(args);
        } else if (action.equals(SET_USER_INFO)){
            mSetUserInfoCallback = callback;
            setUserInfo(args);
        } else if (action.equals(SYNC_TIME)){
            mSyncTimeCallback = callback;
            syncTime(args);
        } else if (action.equals(SET_TIME_MODE)){
            mSetTimeModeCallback = callback;
            setTimeMode(args);
        } else if (action.equals(SEND_RANDOM_NUMBER)){
            mSendRandomNumber = callback;
            sendRandomNumber(args);
        } else {
            Log.i("INFO", "[IHealthBinder] execute function, action does not exist");
        }
        return true;
    }

    //ACTION METHODS
    private void init() {
        Log.i("INFO", "[IHealthBinder] init function");
        IHealthHandler.getInstance().init(cordova.getActivity().getApplicationContext());
    }

    private void sendRandomNumber(JSONArray args){
        Log.i("INFO", "[IHealthBinder] sendRandomNumber function");
        if (args == null || args.length() != 1){
            syncTimeCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            Log.i("INFO", "[IHealthBinder] sendRandomNumber deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] sendRandomNumber deviceAddress = " + deviceAddress);
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, SEND_RANDOM_NUMBER, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendRandomNumberCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, SEND_RANDOM_NUMBER, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendRandomNumberCallback(EventStatus.ERROR, null);
                }
            } else {
                //DeviceType is not supported, yet.
                sendRandomNumberCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            sendRandomNumberCallback(EventStatus.ERROR, null);
        }
    }

    private void setTimeMode(JSONArray args){
        Log.i("INFO", "[IHealthBinder] setTimeMode function");
        if (args == null || args.length() != 1){
            setTimeModeCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            int mode = jsonObject.getInt("mode");
            Log.i("INFO", "[IHealthBinder] setTimeMode deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] setTimeMode deviceAddress = " + deviceAddress);
            Log.i("INFO", "[IHealthBinder] setTimeMode mode = " + mode);
            Parameters params = new Parameters();
            params.mAmParams.mTimeMode = mode;
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, SET_TIME_MODE, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setTimeModeCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, SET_TIME_MODE, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setTimeModeCallback(EventStatus.ERROR, null);
                }
            } else {
                //DeviceType is not supported, yet.
                setTimeModeCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            setTimeModeCallback(EventStatus.ERROR, null);
        }
    }

    private void syncTime(JSONArray args){
        Log.i("INFO", "[IHealthBinder] syncTime function");
        if (args == null || args.length() != 1){
            syncTimeCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            Log.i("INFO", "[IHealthBinder] syncTime deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] syncTime deviceAddress = " + deviceAddress);
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, SYNC_TIME, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    syncTimeCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, SYNC_TIME, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    syncTimeCallback(EventStatus.ERROR, null);
                }
            } else {
                //DeviceType is not supported, yet.
                syncTimeCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            syncTimeCallback(EventStatus.ERROR, null);
        }
    }

    private void setUserInfo(JSONArray args){
        Log.i("INFO", "[IHealthBinder] setUserId function");
        if (args == null || args.length() != 1){
            setUserInfoCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            int age = jsonObject.getInt("age");
            int height = jsonObject.getInt("height");
            float weight = Float.parseFloat(jsonObject.getString("weight"));
            int gender = jsonObject.getInt("gender");
            int unit = jsonObject.getInt("unit");
            int target = jsonObject.getInt("target");
            int activityLevel = jsonObject.getInt("activityLevel");
            int min = jsonObject.getInt("min");

            Parameters params = new Parameters();
            params.mUserInfo.mAge = age;
            params.mUserInfo.mHeight = height;
            params.mUserInfo.mWeight = weight;
            params.mUserInfo.mGender = gender;
            params.mUserInfo.mUnit = unit;
            params.mUserInfo.mTarget = target;
            params.mUserInfo.mActivityLevel = activityLevel;
            params.mUserInfo.mSwimTarget = min;

            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, SET_USER_INFO, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setUserInfoCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, SET_USER_INFO, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setUserInfoCallback(EventStatus.ERROR, null);
                }
            } else {
                setUserInfoCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            setUserInfoCallback(EventStatus.ERROR, null);
        }
    }

    private void startDiscovery(JSONArray args) {
        if (args == null || args.length() != 1){
            discoveryStartedCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            Log.i("INFO", "[IHealthBinder] startDiscovery function");
            String model = jsonObject.getString("model");
            if (model == null || model.isEmpty()) throw new Exception("Type cannot be null or empty");
            if (model.equals("AM3S")){
                IHealthHandler.getInstance().startDiscovery(iHealthDevicesManager.DISCOVERY_AM3S);
            } else if (model.equals("AM4")){
                IHealthHandler.getInstance().startDiscovery(iHealthDevicesManager.DISCOVERY_AM4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            discoveryStartedCallback(EventStatus.ERROR, null);
        } catch (Exception e){
            e.printStackTrace();
            discoveryStartedCallback(EventStatus.ERROR, null);
        }
    }

    private void stopDiscovery() {
        Log.i("INFO", "[IHealthBinder] stopDiscovery function");
        IHealthHandler.getInstance().stopDiscovery();
    }

    private void connectDevice(JSONArray args) {
        Log.i("INFO", "[IHealthBinder] connectDevice function");
        if (args == null || args.length() != 1){
            discoveryStartedCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            Log.i("INFO", "[IHealthBinder] connectDevice function");
            String address = jsonObject.getString("address");
            if (address == null || address.isEmpty()) throw new Exception("address cannot be null or empty");
            IHealthHandler.getInstance().connectDevice(address);
        } catch (JSONException e){
            e.printStackTrace();
            deviceConnectedCallback(EventStatus.ERROR, null);
        }catch (Exception e){
            e.printStackTrace();
            deviceConnectedCallback(EventStatus.ERROR, null);
        }
    }

    private void disconnectDevice(JSONArray args) {
        Log.i("INFO", "[IHealthBinder] disconnectDevice function");
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            if (deviceModel == null || deviceAddress == null){
                throw new Exception("You need to pass valid parameters to disconnect a device.");
            }
            if (deviceModel.equals("AM4")){
                Log.i("INFO", "[IHealthBinder] Disconnecting AM4");
                IHealthAM4.getInstance().execAction(deviceAddress, DISCONNECT, null);
            }else if (deviceModel.equals("AM3S")){
                Log.i("INFO", "[IHealthBinder] Disconnecting AM3S");
                IHealthAM3S.getInstance().execAction(deviceAddress, DISCONNECT, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            deviceDisconnectedCallback(EventStatus.ERROR, null);
        } catch (Exception e) {
            e.printStackTrace();
            deviceDisconnectedCallback(EventStatus.ERROR, null);
        }
    }

    private void getActivityData(JSONArray args) {
        Log.i("INFO", "[IHealthBinder] getActivityData function");
        if (args == null || args.length() != 1){
            activityCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            Log.i("INFO", "[IHealthBinder] getActivityData deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] getActivityData deviceAddress = " + deviceAddress);
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, GET_ACTIVITY_DATA, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    activityCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, GET_ACTIVITY_DATA, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    activityCallback(EventStatus.ERROR, null);
                }
            } else {
                //DeviceType is not supported, yet.
                activityCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            activityCallback(EventStatus.ERROR, null);
        }
    }

    private void getUserId(JSONArray args){
        Log.i("INFO", "[IHealthBinder] getUserId function");
        if (args == null || args.length() != 1){
            getUserIdCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            Log.i("INFO", "[IHealthBinder] getActivityData deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] getActivityData deviceAddress = " + deviceAddress);
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, GET_USER_ID, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    getUserIdCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, GET_USER_ID, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    getUserIdCallback(EventStatus.ERROR, null);
                }
            } else {
                getUserIdCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            getUserIdCallback(EventStatus.ERROR, null);
        }
    }

    private void setUserId(JSONArray args){
        Log.i("INFO", "[IHealthBinder] setUserId function");
        if (args == null || args.length() != 1){
            setUserIdCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            int userId = jsonObject.getInt("user_id");
            Log.i("INFO", "[IHealthBinder] getActivityData deviceModel = " + deviceModel);
            Log.i("INFO", "[IHealthBinder] getActivityData deviceAddress = " + deviceAddress);
            Log.i("INFO", "[IHealthBinder] getActivityData userId = " + userId);
            if (userId == 0){
                Log.i("INFO", "User ID was equal to 0.");
                setUserIdCallback(EventStatus.ERROR, null);
                return;
            }
            Parameters params = new Parameters();
            params.mUserId = userId;
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, SET_USER_ID, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setUserIdCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, SET_USER_ID, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    setUserIdCallback(EventStatus.ERROR, null);
                }
            } else {
                setUserIdCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            setUserIdCallback(EventStatus.ERROR, null);
        }
    }

    private void getSleepData(JSONArray args) {
        if (args == null || args.length() != 1){
            sleepCallback(EventStatus.ERROR, null);
            return;
        }
        try {
            JSONObject jsonObject = args.getJSONObject(0);
            if (jsonObject == null) throw new Exception("You need to pass at least one type");
            String deviceModel = jsonObject.getString("model");
            String deviceAddress = jsonObject.getString("address");
            if (deviceModel.equals("AM3S")) {
                try {
                    IHealthAM3S.getInstance().execAction(deviceAddress, GET_SLEEP_DATA, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepCallback(EventStatus.ERROR, null);
                }
            } else if (deviceModel.equals("AM4")) {
                try {
                    IHealthAM4.getInstance().execAction(deviceAddress, GET_SLEEP_DATA, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepCallback(EventStatus.ERROR, null);
                }
            } else {
                //DeviceType is not supported, yet.
                sleepCallback(EventStatus.ERROR, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            sleepCallback(EventStatus.ERROR, null);
        }
    }

    //IHEALTH EVENT
    @Override
    @Subscribe
    public void onEvent(IHealthEvent event) {
        if (event == null || event.mEventType == null || event.mEventStatus == null) return;
        if (event.mEventType == IHealthEventType.INIT) {
            initCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DISCOVERY_STARTED)) {
            discoveryStartedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DEVICE_SCANNED)) {
            deviceScannedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DISCOVERY_STOPPED)) {
            discoveryStoppedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DISCOVERY_FINISHED)) {
            discoveryFinishedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DEVICE_CONNECTED)) {
            deviceConnectedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DEVICE_DISCONNECTED)) {
            deviceDisconnectedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.DEVICE_CONNECTION_FAILED)){
            deviceConnectionFailedCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SLEEP_DATA)) {
            sleepCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.ACTIVITY_DATA)) {
            activityCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.GET_USER_ID)){
            getUserIdCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SET_USER_ID)){
            setUserIdCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SET_USER_INFO)){
            setUserInfoCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SYNC_REAL_TIME)){
            syncTimeCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SET_TIME_MODE)){
            setTimeModeCallback(event.mEventStatus, event.mData);
        } else if (event.mEventType.equals(IHealthEventType.SEND_RANDOM_NUMBER)){
            sendRandomNumberCallback(event.mEventStatus, event.mData);
        }
    }

    //CALLBACK METHODS
    private void initCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] initCallback function");
        if (mInitCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mInitCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mInitCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] initCallback function, Can't fire JS callback, null");
        }
    }

    private void sendRandomNumberCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] sendRandomNumberCallback function");
        if (mSendRandomNumber != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                if (mData instanceof Data == false) {
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSendRandomNumber.sendPluginResult(deviceResult);
                    return;
                }
                Data data = (Data) mData;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    jsonObject.put("number", data.mRawData);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mSendRandomNumber.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSendRandomNumber.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSendRandomNumber.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] sendRandomNumberCallback function, Can't fire JS callback, null");
        }
    }

    private void setTimeModeCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] setTimeCallback function");
        if (mSyncTimeCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSetTimeModeCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSetTimeModeCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] setTimeCallback function, Can't fire JS callback, null");
        }
    }

    private void syncTimeCallback(EventStatus mEventStatus, Object mData){
        Log.i("INFO", "[IHealthBinder] syncTimeCallback function");
        if (mSyncTimeCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSyncTimeCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSyncTimeCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] syncTimeCallback function, Can't fire JS callback, null");
        }
    }

    private void setUserInfoCallback(EventStatus mEventStatus, Object mData){
        Log.i("INFO", "[IHealthBinder] setUserInfoCallback function");
        if (mSetUserInfoCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                if (mData instanceof Data == false) {
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSetUserInfoCallback.sendPluginResult(deviceResult);
                    return;
                }
                Data data = (Data) mData;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mSetUserInfoCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSetUserInfoCallback.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSetUserInfoCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] setUserInfoCallback function, Can't fire JS callback, null");
        }
    }

    private void setUserIdCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] setUserIdCallback function");
        if (mSetUserIdCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                if (mData instanceof Data == false) {
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSetUserIdCallback.sendPluginResult(deviceResult);
                    return;
                }
                Data data = (Data) mData;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mSetUserIdCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mSetUserIdCallback.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mSetUserIdCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] setUserIdCallback function, Can't fire JS callback, null");
        }
    }


    private void getUserIdCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] getUserIdCallback function");
        if (mGetUserIdCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                if (mData instanceof Data == false) {
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mGetUserIdCallback.sendPluginResult(deviceResult);
                    return;
                }
                Data data = (Data) mData;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    jsonObject.put("userId", Integer.parseInt(data.mRawData));
                    Log.i("INFO", "Here is my ID: " + data.mRawData);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mGetUserIdCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mGetUserIdCallback.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mGetUserIdCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] getUserIdCallback function, Can't fire JS callback, null");
        }
    }

    private void discoveryStartedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] discoveryStartedCallback function");
        if (mDiscoveryStartedCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryStartedCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryStartedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] discoveryStartedCallback function, Can't fire JS callback, null");
        }
    }

    private void deviceScannedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] deviceScannedCallback function");
        if (mDeviceDiscoveredCallback != null) {
            if (mData instanceof ScanData == false) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceDiscoveredCallback.sendPluginResult(deviceResult);
                return;
            }
            ScanData data = (ScanData) mData;
            if (mEventStatus == EventStatus.SUCCESS) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mDeviceAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mDeviceDiscoveredCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceDiscoveredCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] deviceScannedCallback function, Can't fire JS callback, null");
        }
    }

    private void discoveryStoppedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] discoveryStoppedCallback function");
        if (mDiscoveryStoppedCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryStoppedCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryStoppedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] discoveryStoppedCallback function, Can't fire JS callback, null");
        }
    }

    private void discoveryFinishedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] discoveryFinishedCallback function");
        if (mDiscoveryFinishedCallback != null) {
            IHealthHandler.getInstance().reInit(cordova.getActivity().getApplicationContext());
            if (mEventStatus == EventStatus.SUCCESS) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryFinishedCallback.sendPluginResult(deviceResult);
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDiscoveryFinishedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] discoveryFinishedCallback function, Can't fire JS callback, null");
        }
    }

    private void deviceConnectedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] mDeviceConnectedCallback function");
        if (mDeviceConnectedCallback != null) {
            if (mData instanceof ConnectedData == false){
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceConnectedCallback.sendPluginResult(deviceResult);
                return;
            }
            ConnectedData data = (ConnectedData)mData;
            if (mEventStatus == EventStatus.SUCCESS) {
                try {
                    if (data.mModel.equals("AM3S")){
                        //DO THAT HERE TOO!
                    }else if (data.mModel.equals("AM4")){
                        IHealthAM4.getInstance().buildAmControl(data.mAddress);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mDeviceConnectedCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceConnectedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] mDeviceConnectedCallback function, Can't fire JS callback, null");
        }
    }

    private void deviceConnectionFailedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] mDeviceConnectionFailedCallback function");
        if (mDeviceConnectionFailedCallback != null) {
            if (mData instanceof ConnectedData == false){
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceConnectionFailedCallback.sendPluginResult(deviceResult);
                return;
            }
            ConnectedData data = (ConnectedData)mData;
            if (mEventStatus == EventStatus.SUCCESS) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mDeviceConnectionFailedCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceConnectionFailedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] mDeviceConnectedCallback function, Can't fire JS callback, null");
        }
    }

    private void deviceDisconnectedCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] deviceDisconnectedCallback function");
        if (mDeviceDisconnectedCallback != null) {
            if (mData instanceof ConnectedData == false){
                Log.i("INFO", "Weirdly enough, mData is not an instance of ConnectedData");
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceDisconnectedCallback.sendPluginResult(deviceResult);
                return;
            }
            ConnectedData data = (ConnectedData)mData;
            if (mEventStatus == EventStatus.SUCCESS) {
                if (data.mModel.equals("AM3S")) {
                    Log.i("INFO", "[IHealthBinder] Attemp to disconnect AM3S.");
                    IHealthAM3S.getInstance().execAction(data.mAddress, "disconnect", null);
                }else if (data.mModel.equals("AM4")){
                    Log.i("INFO", "[IHealthBinder] Attemp to disconnect AM4.");
                    IHealthAM4.getInstance().destroyAmControl(data.mAddress);
                } else {
                    Log.i("INFO", "There is another type ? : " + data.mModel);
                }
                try {
                    Log.i("INFO", "[IHealthBinder] Building disconnection object to send back.");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("model", data.mModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mDeviceDisconnectedCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                Log.i("INFO", "[IHealthBinder] Disconnection is in error");
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mDeviceDisconnectedCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] deviceDisconnectedCallback function, Can't fire JS callback, null");
        }
    }

    private void activityCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] activityCallback function");
        if (mGetActivityDataCallback != null) {
            if (mData instanceof Data == false) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mGetActivityDataCallback.sendPluginResult(deviceResult);
                return;
            }
            Data data = (Data) mData;
            if (mEventStatus == EventStatus.SUCCESS) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(data.mRawData).getAsJsonObject();
                    jsonObject.put("data", obj);
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mGetActivityDataCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mGetActivityDataCallback.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mGetActivityDataCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] activityCallback function, Can't fire JS callback, null");
        }
    }

    private void sleepCallback(EventStatus mEventStatus, Object mData) {
        Log.i("INFO", "[IHealthBinder] sleepCallback function");
        if (mData instanceof Data == false) {
            Log.i("INFO", "[IHealthBinder] sleepCallback function, mData is not an instance of Data");
            PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
            deviceResult.setKeepCallback(true);
            mGetSleepDataCallback.sendPluginResult(deviceResult);
            return;
        }
        Data data = (Data) mData;
        if (mGetSleepDataCallback != null) {
            if (mEventStatus == EventStatus.SUCCESS) {
                Log.i("INFO", "[IHealthBinder] sleepCallback function, Status -> success");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("data", data.mRawData);
                    jsonObject.put("model", data.mDeviceModel);
                    jsonObject.put("address", data.mAddress);
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    deviceResult.setKeepCallback(true);
                    mGetSleepDataCallback.sendPluginResult(deviceResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                    deviceResult.setKeepCallback(true);
                    mGetSleepDataCallback.sendPluginResult(deviceResult);
                }
            } else if (mEventStatus == EventStatus.ERROR) {
                Log.i("INFO", "[IHealthBinder] sleepCallback function, Status -> Error");
                PluginResult deviceResult = new PluginResult(PluginResult.Status.ERROR, new JSONObject());
                deviceResult.setKeepCallback(true);
                mGetSleepDataCallback.sendPluginResult(deviceResult);
            }
        } else {
            Log.i("INFO", "[IHealthBinder] sleepCallback function, Can't fire JS callback, null");
        }
    }
}