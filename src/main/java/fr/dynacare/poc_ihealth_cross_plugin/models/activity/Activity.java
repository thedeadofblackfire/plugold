package fr.dynacare.poc_ihealth_cross_plugin.models.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Activity {

    @SerializedName("activity_each_data")
    @Expose
    private List<ActivityEachDatum> activityEachData = new ArrayList<ActivityEachDatum>();

    /**
     * 
     * @return
     *     The activityEachData
     */
    public List<ActivityEachDatum> getActivityEachData() {
        return activityEachData;
    }

    /**
     * 
     * @param activityEachData
     *     The activity_each_data
     */
    public void setActivityEachData(List<ActivityEachDatum> activityEachData) {
        this.activityEachData = activityEachData;
    }

}
