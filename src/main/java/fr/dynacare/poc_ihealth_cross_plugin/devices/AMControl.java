package fr.dynacare.poc_ihealth_cross_plugin.devices;

/**
 * Created by guillaumeboufflers on 07/07/2016.
 */

public class AMControl<T>{
    private T mAmControl;
    private String mAddress;

    public AMControl(T control, String address){
        mAmControl = control;
        mAddress = address;
    }

    public T getmAmControl() {
        return mAmControl;
    }

    public void setmAmControl(T mAmControl) {
        this.mAmControl = mAmControl;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
