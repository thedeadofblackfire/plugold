package fr.dynacare.poc_ihealth_cross_plugin.eventBus;

/**
 * Created by guillaumeboufflers on 06/07/2016.
 */
public class ScanData {
    public String mDeviceModel;
    public String mDeviceAddress;

    public ScanData(String name, String address){
        mDeviceModel = name;
        mDeviceAddress = address;
    }
}
