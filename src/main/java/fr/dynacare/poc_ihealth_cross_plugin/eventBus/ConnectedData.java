package fr.dynacare.poc_ihealth_cross_plugin.eventBus;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */
public class ConnectedData {
    public String mName;
    public String mAddress;
    public String mModel;

    public ConnectedData(String name, String address, String type){
        mName = name;
        mAddress = address;
        mModel = type;
    }
}
