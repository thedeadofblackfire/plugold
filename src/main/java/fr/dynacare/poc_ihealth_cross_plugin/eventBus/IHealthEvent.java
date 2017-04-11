package fr.dynacare.poc_ihealth_cross_plugin.eventBus;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */
public class IHealthEvent {
    public EventStatus mEventStatus;
    public String mMessage;
    public IHealthEventType mEventType;
    public Object mData;

    public IHealthEvent(IHealthEventType eventType, EventStatus status, Object data, String message){
        mEventStatus = status;
        mEventType = eventType;
        mData = data;
        mMessage = message;
    }

    public IHealthEvent(IHealthEventType eventType, EventStatus status, Object data){
        mEventStatus = status;
        mEventType = eventType;
        mData = data;
        mMessage = "";
    }
}