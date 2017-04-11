package fr.dynacare.poc_ihealth_cross_plugin.devices;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by guillaumeboufflers on 27/09/2016.
 */
public class AMControlList extends ArrayList<AMControl> {
    public AMControl getAMControlByAddress(String address){
        Log.i("INFO", "[AMControlList] getAMControlByAddress");
        for (AMControl control: this){
            if (control.getmAddress().equals(address)){
                return control;
            }
        }
        return null;
    }

    public void removeAmControlByAddress(String address){
        Log.i("INFO", "[AMControlList] removeAmControlByAddress");
        AMControl controlToDelete = null;
        for (AMControl control: this){
            if (control.getmAddress().equals(address)){
                controlToDelete = control;
            }
        }
        if (controlToDelete != null){
            this.remove(controlToDelete);
        }
    }
}
