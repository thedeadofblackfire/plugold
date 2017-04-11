package fr.dynacare.poc_ihealth_cross_plugin.eventBus;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */
public enum IHealthEventType {
    INIT,
    DISCOVERY_STARTED,
    DISCOVERY_STOPPED,
    DISCOVERY_FINISHED,
    DEVICE_CONNECTED,
    DEVICE_DISCONNECTED,
    DEVICE_CONNECTION_FAILED,
    DEVICE_SCANNED,
    ACTIVITY_DATA,
    SLEEP_DATA,
    GET_USER_ID,
    SET_USER_ID,
    SET_USER_INFO,
    SYNC_REAL_TIME,
    SET_TIME_MODE,
    SEND_RANDOM_NUMBER,
}