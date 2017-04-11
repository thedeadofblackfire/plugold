
package fr.dynacare.poc_ihealth_cross_plugin.models.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AMActivity {

    @SerializedName("activity")
    @Expose
    private List<Activity> activity = new ArrayList<Activity>();

    /**
     * 
     * @return
     *     The activity
     */
    public List<Activity> getActivity() {
        return activity;
    }

    /**
     * 
     * @param activity
     *     The activity
     */
    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }

}
